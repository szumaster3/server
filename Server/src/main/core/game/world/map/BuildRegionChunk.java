package core.game.world.map;

import core.game.node.entity.player.Player;
import core.game.node.item.GroundItem;
import core.game.node.scenery.Constructed;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.build.DynamicRegion;
import core.game.world.map.build.LandscapeParser;
import core.game.world.update.flag.UpdateFlag;
import core.net.packet.IoBuffer;
import core.net.packet.out.ClearScenery;
import core.net.packet.out.ConstructGroundItem;
import core.net.packet.out.ConstructScenery;
import core.net.packet.out.UpdateAreaPosition;
import core.tools.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static core.api.ContentAPIKt.log;

/**
 * Represents a chunk of a buildable region.
 */
public class BuildRegionChunk {

    public static final int SIZE = 8;

    private Region region;
    protected Location base;
    protected Location currentBase;
    protected RegionPlane plane;
    protected List<GroundItem> items;
    protected int rotation;
    @SuppressWarnings("unchecked")
    private final List<Scenery>[][] objects;
    private final List<UpdateFlag<?>> flags = new ArrayList<>(20);

    /**
     * Creates a new chunk with the base location, rotation, and plane.
     *
     * @param base The base (bottom-left) location of the chunk.
     * @param rotation The rotation of this chunk (0-3).
     * @param plane The region plane this chunk belongs to.
     */
    @SuppressWarnings("unchecked")
    public BuildRegionChunk(Location base, int rotation, RegionPlane plane) {
        this.base = base;
        this.currentBase = base;
        this.rotation = rotation;
        this.plane = plane;
        this.items = new ArrayList<>();

        this.objects = new ArrayList[SIZE][SIZE];

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                objects[x][y] = new ArrayList<>();
            }
        }
    }

    /**
     * Creates a new chunk copying scenery from an old array of scenery.
     *
     * @param base The base location.
     * @param rotation The rotation of the chunk.
     * @param plane The region plane.
     * @param oldObjects The old scenery to copy.
     */
    public BuildRegionChunk(Location base, int rotation, RegionPlane plane, Scenery[][] oldObjects) {
        this(base, rotation, plane);
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (oldObjects[x][y] != null) {
                    this.objects[x][y].add(new Scenery(oldObjects[x][y]));
                }
            }
        }
    }

    /**
     * Creates a deep copy of this chunk on a given region plane.
     *
     * @param plane The target region plane.
     * @return A copied BuildRegionChunk instance.
     */
    public BuildRegionChunk copy(RegionPlane plane) {
        BuildRegionChunk chunk = new BuildRegionChunk(base, rotation, plane);

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                List<Scenery> originalList = this.objects[x][y];
                if (originalList == null) {
                    chunk.objects[x][y] = new ArrayList<>();
                    continue;
                }
                List<Scenery> copiedList = new ArrayList<>(originalList.size());
                for (Scenery obj : originalList) {
                    if (obj instanceof Constructed) {
                        Scenery constructedCopy = obj.transform(obj.getId(), obj.getRotation()).asConstructed();
                        copiedList.add(constructedCopy);
                    } else if (obj != null) {
                        Scenery copy = obj.transform(obj.getId());
                        copy.setActive(obj.isActive());
                        copy.setRenderable(obj.isRenderable());
                        copiedList.add(copy);
                    }
                }
                chunk.objects[x][y] = copiedList;
            }
        }

        return chunk;
    }

    /**
     * Adds an update flag to this chunk to mark it for processing.
     *
     * @param flag The update flag to add.
     */
    public void flag(UpdateFlag<?> flag) {
        flags.add(flag);
    }

   /**
     * Clears the chunk's update flags, items, and scenery objects.
     * If the plane's region is dynamic, also nullifies the items list.
     */
    public void clear() {
        flags.clear();
        if (items != null && plane.getRegion() instanceof DynamicRegion) {
            items.clear();
            items = null;
        }
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                objects[x][y].clear();
            }
        }
    }

    /**
     * Synchronizes the chunk with a player by sending updated scenery and ground items.
     *
     * @param player The player to synchronize with.
     */
    public void synchronize(Player player) {
        IoBuffer buffer = UpdateAreaPosition.getChunkUpdateBuffer(player, currentBase);
        if (appendUpdate(player, buffer)) {
            player.getSession().write(buffer);
        }
    }

    /**
     * Appends scenery and ground item updates to the buffer for the player.
     *
     * @param player The player to send updates to.
     * @param buffer The buffer to write updates into.
     * @return true if any updates were added, false otherwise.
     */
    protected boolean appendUpdate(Player player, IoBuffer buffer) {
        boolean updated = false;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (Scenery o : objects[x][y]) {
                    if (o instanceof Constructed) {
                        ConstructScenery.write(buffer, o);
                        updated = true;
                    } else if (!o.isRenderable()) {
                        ClearScenery.write(buffer, o);
                        updated = true;
                    }
                }
            }
        }
        ArrayList<GroundItem> totalItems = drawItems(items, player);
        for (GroundItem item : totalItems) {
            if (item != null && item.isActive() && item.getLocation() != null) {
                if (!item.isPrivate() || item.droppedBy(player)) {
                    ConstructGroundItem.write(buffer, item);
                    updated = true;
                }
            }
        }
        return updated;
    }

    /**
     * Gets a copy of ground items to be drawn for the player.
     *
     * @param items The current ground items.
     * @param player The player viewing the chunk.
     * @return A list of ground items to draw.
     */
    public ArrayList<GroundItem> drawItems(List<GroundItem> items, Player player) {
        return items != null ? new ArrayList<>(items) : new ArrayList<>();
    }

    /**
     * Sends update flags to the player if any updates are present.
     *
     * @param player The player to update.
     */
    public void update(Player player) {
        if (isUpdated()) {
            IoBuffer buffer = UpdateAreaPosition.getChunkUpdateBuffer(player, currentBase);
            for (UpdateFlag<?> flag : flags) {
                flag.writeDynamic(buffer, player);
            }
            player.getSession().write(buffer);
        }
    }

    /**
     * Rotates chunk's scenery objects.
     *
     * @param direction The direction to rotate.
     */
    public void rotate(Direction direction) {
        if (rotation != 0) {
            log(this.getClass(), Log.ERR, "Region chunk was already rotated!");
            return;
        }

        List<Scenery>[][] copy = new ArrayList[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                copy[x][y] = new ArrayList<>();
                for (Scenery object : objects[x][y]) {
                    copy[x][y].add(new Scenery(object));
                }
            }
        }

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                List<Scenery> toRemove = new ArrayList<>(objects[x][y]);
                for (Scenery object : toRemove) {
                    SceneryBuilder.remove(object);
                    this.remove(object);
                }
                objects[x][y].clear();
            }
        }

        switch (direction) {
            case NORTH:
                rotation = 0;
                break;
            case EAST:
                rotation = 1;
                break;
            case SOUTH:
                rotation = 2;
                break;
            case WEST:
                rotation = 3;
                break;
            default:
                rotation = (direction.toInteger() + (direction.toInteger() % 2 == 0 ? 2 : 0)) % 4;
                log(this.getClass(), Log.ERR, "Attempted to rotate a chunk in a non-cardinal direction - fallback rotation used.");
                break;
        }

        int baseX = currentBase.getLocalX();
        int baseY = currentBase.getLocalY();

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (Scenery object : copy[x][y]) {
                    int[] pos = getRotatedPosition(x, y, object.getDefinition().sizeX, object.getDefinition().sizeY, object.getRotation(), rotation);
                    Scenery obj = object.transform(
                            object.getId(),
                            (object.getRotation() + rotation) % 4,
                            object.getLocation().transform(pos[0] - x, pos[1] - y, 0)
                    );
                    if (object instanceof Constructed) {
                        obj = obj.asConstructed();
                    }
                    obj.setActive(object.isActive());
                    obj.setRenderable(object.isRenderable());

                    LandscapeParser.flagScenery(plane, baseX + pos[0], baseY + pos[1], obj, true, true);
                }
            }
        }
    }

    /**
     * Calculates rotated position of an object inside the chunk.
     *
     * @param x The original x coordinate within the chunk.
     * @param y The original y coordinate within the chunk.
     * @param sizeX The size of the object in X direction.
     * @param sizeY The size of the object in Y direction.
     * @param rotation The object's current rotation.
     * @param chunkRotation The chunk rotation to apply.
     * @return An array with the rotated [x, y] coordinates.
     */
    public static int[] getRotatedPosition(int x, int y, int sizeX, int sizeY, int rotation, int chunkRotation) {
        if ((rotation & 1) == 1) {
            int s = sizeX;
            sizeX = sizeY;
            sizeY = s;
        }

        switch (chunkRotation) {
            case 0:
                return new int[]{x, y};
            case 1:
                return new int[]{y, 7 - x - (sizeX - 1)};
            case 2:
                return new int[]{7 - x - (sizeX - 1), 7 - y - (sizeY - 1)};
            default:
                return new int[]{7 - y - (sizeY - 1), x};
        }
    }

    /**
     * Gets the list of ground items in this chunk.
     *
     * @return List of ground items.
     */
    public List<GroundItem> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    /**
     * Sets the ground items for this chunk.
     *
     * @param items The list of ground items.
     */
    public void setItems(List<GroundItem> items) {
        this.items = items;
    }

    /**
     * Gets the scenery objects at the given chunk-local coordinates.
     *
     * @param chunkX Local x coordinate (0-7).
     * @param chunkY Local y coordinate (0-7).
     * @return List of scenery objects.
     */
    public List<Scenery> getObjects(int chunkX, int chunkY) {
        return objects[chunkX][chunkY];
    }

    /**
     * Gets the scenery objects at the given local coordinates, or empty list if out of range.
     *
     * @param localX Local x coordinate.
     * @param localZ Local y coordinate.
     * @return List of scenery objects at the coordinates.
     */
    @SuppressWarnings("unchecked")
    public List<Scenery> getCoods(int localX, int localZ) {
        if (localX < 0 || localX >= SIZE || localZ < 0 || localZ >= SIZE) {
            return Collections.emptyList();
        }

        if (objects[localX][localZ] == null) {
            objects[localX][localZ] = new ArrayList<>();
        }
        return objects[localX][localZ];
    }

    /**
     * Sets the current base location of the chunk and updates all scenery object locations accordingly.
     *
     * @param currentBase The new base location.
     */
    public void setCurrentBase(Location currentBase) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (Scenery s : objects[x][y]) {
                    s.setLocation(currentBase.transform(x, y, 0));
                }
            }
        }
        this.currentBase = currentBase;
    }

    /**
     * Checks if the chunk has any pending update flags.
     *
     * @return True if updates exist, false otherwise.
     */
    public boolean isUpdated() {
        return !flags.isEmpty();
    }

    /**
     * Clears all update flags for this chunk.
     */
    public void resetFlags() {
        flags.clear();
    }

    /**
     * Rebuilds the clipping flags for this chunk based on another region plane.
     *
     * @param from The source region plane to copy flags from.
     */
    public void rebuildFlags(RegionPlane from) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Location loc = currentBase.transform(x, y, 0);
                Location fromLoc = base.transform(x, y, 0);
                plane.getFlags().getLandscape()[loc.getLocalX()][loc.getLocalY()] = from.getFlags().getLandscape()[fromLoc.getLocalX()][fromLoc.getLocalY()];
                plane.getFlags().clearFlag(x, y);
                for (Scenery obj : objects[x][y]) {
                    LandscapeParser.applyClippingFlagsFor(plane, loc.getLocalX(), loc.getLocalY(), obj);
                }
            }
        }
    }

    /**
     * Removes a scenery object from this chunk.
     *
     * @param object The scenery to remove.
     */
    public void remove(Scenery object) {
        int chunkX = object.getLocation().getChunkOffsetX();
        int chunkY = object.getLocation().getChunkOffsetY();
        objects[chunkX][chunkY].removeIf(s -> s.equals(object));
        object.setActive(false);
        object.setRenderable(false);
    }

    /**
     * Adds a scenery object to this chunk if not already present, activating it.
     *
     * @param object The scenery to add.
     */
    public void add(Scenery object) {
        int chunkX = object.getLocation().getChunkOffsetX();
        int chunkY = object.getLocation().getChunkOffsetY();
        List<Scenery> list = objects[chunkX][chunkY];
        for (Scenery current : list) {
            if (current.equals(object)) {
                current.setActive(true);
                current.setRenderable(true);
                return;
            }
        }
        list.add(object.asConstructed());
        object.setActive(true);
        object.setRenderable(true);
    }

    /**
     * Stores a scenery object into this chunk, activating and marking it renderable.
     *
     * @param object The scenery object to store.
     */
    public void store(Scenery object) {
        if (object == null) return;
        int chunkX = object.getLocation().getChunkOffsetX();
        int chunkY = object.getLocation().getChunkOffsetY();
        objects[chunkX][chunkY].add(object);
        object.setActive(true);
        object.setRenderable(true);
    }

    /**
     * Gets the index of a scenery object in the list at the specified chunk coordinates.
     *
     * @param x The local x coordinate.
     * @param y The local y coordinate.
     * @param objectId The object ID to find, or -1 for objects with options.
     * @return The index in the list or 0 if not found.
     */
    public int getIndex(int x, int y, int objectId) {
        List<Scenery> list = objects[x][y];
        for (int i = 0; i < list.size(); i++) {
            Scenery o = list.get(i);
            if ((objectId > -1 && o.getId() == objectId) || (objectId == -1 && o.getDefinition().hasOptions(false))) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Gets the scenery object at the given chunk coordinates and list index.
     *
     * @param x The chunk-local x coordinate.
     * @param y The chunk-local y coordinate.
     * @param index The index within the scenery list.
     * @return The scenery object or null if index is out of bounds.
     */
    public Scenery get(int x, int y, int index) {
        List<Scenery> list = objects[x][y];
        return index < list.size() ? list.get(index) : null;
    }

    /**
     * @return The base location of this chunk.
     */
    public Location getBase() {
        return base;
    }

    /**
     * Sets the base location of this chunk.
     * @param base the base
     */
    public void setBase(Location base) {
        this.base = base;
    }

    /**
     * Sets the region this chunk belongs to.
     * @param region the region
     */
    public void setRegion(Region region) {
        this.region = region;
    }

    /**
     * @return The region this chunk belongs to.
     */
    public Region getRegion() {
        return region;
    }

    /**
     * @return The rotation of this chunk.
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Sets the rotation of this chunk
     * @param rotation The rotation
     */
    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    /**
     * @return The region plane this chunk belongs to.
     */
    public RegionPlane getPlane() {
        return plane;
    }

    /**
     * Sets the region plane for this chunk.
     **/
    public void setPlane(RegionPlane plane) {
        this.plane = plane;
    }

    /**
     * @return The current base location of this chunk.
     */
    public Location getCurrentBase() {
        return currentBase;
    }

}
