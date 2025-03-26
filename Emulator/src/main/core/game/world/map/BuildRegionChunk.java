package core.game.world.map;

import core.game.node.entity.player.Player;
import core.game.node.item.GroundItem;
import core.game.node.scenery.Constructed;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.build.LandscapeParser;
import core.net.packet.IoBuffer;
import core.net.packet.out.ClearScenery;
import core.net.packet.out.ConstructGroundItem;
import core.net.packet.out.ConstructScenery;
import core.tools.Log;

import java.util.ArrayList;

import static core.api.ContentAPIKt.log;

/**
 * The type Build region chunk.
 */
public class BuildRegionChunk extends RegionChunk {

    /**
     * The constant ARRAY_SIZE.
     */
    public static final int ARRAY_SIZE = 10;

    private final Scenery[][][] objects;

    /**
     * Instantiates a new Build region chunk.
     *
     * @param base     the base
     * @param rotation the rotation
     * @param plane    the plane
     */
    public BuildRegionChunk(Location base, int rotation, RegionPlane plane) {
        super(base, rotation, plane);
        this.objects = new Scenery[ARRAY_SIZE][8][8];
    }

    /**
     * Instantiates a new Build region chunk.
     *
     * @param base     the base
     * @param rotation the rotation
     * @param plane    the plane
     * @param objects  the objects
     */
    public BuildRegionChunk(Location base, int rotation, RegionPlane plane, Scenery[][] objects) {
        this(base, rotation, plane);
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (objects[x][y] != null) {
                    this.objects[0][x][y] = new Scenery(objects[x][y]);
                }
            }
        }
    }

    @Override
    protected boolean appendUpdate(Player player, IoBuffer buffer) {
        boolean updated = false;//super.appendUpdate(player, buffer);
        for (int i = 0; i < objects.length; i++) {
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    Scenery o = objects[i][x][y];
                    if (o instanceof Constructed) {
                        ConstructScenery.write(buffer, o);
                        updated = true;
                    } else if (o != null && !o.isRenderable()) {
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

    @Override
    public void rotate(Direction direction) {
        if (rotation != 0) {
            log(this.getClass(), Log.ERR, "Region chunk was already rotated!");
            return;
        }
        Scenery[][][] copy = new Scenery[ARRAY_SIZE][SIZE][SIZE];
        int baseX = currentBase.getLocalX();
        int baseY = currentBase.getLocalY();
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (int i = 0; i < objects.length; i++) {
                    Scenery object = copy[i][x][y] = objects[i][x][y];
                    if (object != null) {
                        SceneryBuilder.remove(object);
                        this.remove(object);
                    }
                }
            }
        }
        clear();
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
                log(this.getClass(), Log.ERR, "Attempted to rotate a chunk in a non-cardinal direction - using fallback rotation code. This should be investigated!");
                break;
        }
        ;
        for (int i = 0; i < objects.length; i++) {
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    Scenery object = copy[i][x][y];
                    if (object != null) {
                        int[] pos = getRotatedPosition(x, y, object.getDefinition().sizeX, object.getDefinition().sizeY, object.getRotation(), rotation);
                        Scenery obj = object.transform(object.getId(), (object.getRotation() + rotation) % 4, object.getLocation().transform(pos[0] - x, pos[1] - y, 0));
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
    }

    @Override
    public BuildRegionChunk copy(RegionPlane plane) {
        BuildRegionChunk chunk = new BuildRegionChunk(base, rotation, plane);
        for (int i = 0; i < chunk.objects.length; i++) {
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    Scenery o = objects[i][x][y];
                    if (o instanceof Constructed) {
                        chunk.objects[i][x][y] = o.transform(o.getId(), o.getRotation()).asConstructed();
                    } else if (o != null) {
                        chunk.objects[i][x][y] = o.transform(o.getId());
                        chunk.objects[i][x][y].setActive(o.isActive());
                        chunk.objects[i][x][y].setRenderable(o.isRenderable());
                    }
                }
            }
        }
        return chunk;
    }

    @Override
    public void rebuildFlags(RegionPlane from) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Location loc = currentBase.transform(x, y, 0);
                Location fromLoc = base.transform(x, y, 0);
                plane.getFlags().getLandscape()[loc.getLocalX()][loc.getLocalY()] = from.getFlags().getLandscape()[fromLoc.getLocalX()][fromLoc.getLocalY()];
                plane.getFlags().clearFlag(x, y);
                for (int i = 0; i < ARRAY_SIZE; i++) {
                    Scenery obj = objects[i][x][y];
                    if (obj != null)
                        LandscapeParser.applyClippingFlagsFor(plane, loc.getLocalX(), loc.getLocalY(), obj);
                }
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
        for (int i = 0; i < objects.length; i++) {
            for (int x = 0; x < objects[i].length; x++) {
                for (int y = 0; y < objects[i][x].length; y++) {
                    objects[i][x][y] = null;
                }
            }
        }
    }

    /**
     * Remove.
     *
     * @param object the object
     */
    public void remove(Scenery object) {
        int chunkX = object.getLocation().getChunkOffsetX();
        int chunkY = object.getLocation().getChunkOffsetY();
        Scenery current = null;
        int index = -1;
        int i = 0;
        while ((current == null || current.getId() != object.getId()) && i < objects.length) {
            current = objects[i++][chunkX][chunkY];
            if ((current == null || current.getId() < 1) && index == -1) {
                index = i - 1;
            }
        }
        if (current != null && current.equals(object)) {
            current.setActive(false);
            object.setRenderable(false);
        } else {
            objects[index][chunkX][chunkY] = object;
        }
        object.setActive(false);
        object.setRenderable(false);
    }

    /**
     * Add.
     *
     * @param object the object
     */
    public void add(Scenery object) {
        int chunkX = object.getLocation().getChunkOffsetX();
        int chunkY = object.getLocation().getChunkOffsetY();
        Scenery current = null;
        int index = -1;
        int i = 0;
        while ((current == null || current.getId() != object.getId()) && i < objects.length) {
            current = objects[i++][chunkX][chunkY];
            if ((current == null || current.getId() < 1) && index == -1) {
                index = i - 1;
            }
        }
        if (current != null && current.equals(object)) {
            current.setActive(true);
            current.setRenderable(true);
        } else if (index == -1) {
            throw new IllegalStateException("Insufficient array length for storing object!");
        } else {
            objects[index][chunkX][chunkY] = object = object.asConstructed();
        }
        object.setActive(true);
        object.setRenderable(true);
    }

    /**
     * Store.
     *
     * @param object the object
     */
    public void store(Scenery object) {
        if (object == null) {
            return;
        }
        int chunkX = object.getLocation().getChunkOffsetX();
        int chunkY = object.getLocation().getChunkOffsetY();
        for (int i = 0; i < objects.length; i++) {
            Scenery stat = objects[i][chunkX][chunkY];
            if (stat == null || stat.getId() < 1) {
                objects[i][chunkX][chunkY] = object;
                object.setActive(true);
                object.setRenderable(true);
                return;
            }
        }
        System.err.print("Objects - [");
        for (int i = 0; i < objects.length; i++) {
            System.err.print(objects[i][chunkX][chunkY]);
            if (i < objects.length - 1) {
                System.err.print(", ");
            }
        }
        log(this.getClass(), Log.ERR, "]!");
        throw new IllegalStateException("Insufficient array length for storing all objects! ");
    }

    /**
     * Gets index.
     *
     * @param x        the x
     * @param y        the y
     * @param objectId the object id
     * @return the index
     */
    public int getIndex(int x, int y, int objectId) {
        for (int i = 0; i < objects.length; i++) {
            Scenery o = get(x, y, i);
            if (o != null && ((objectId > -1 && o.getId() == objectId) || (objectId == -1 && o.getDefinition().hasOptions(false)))) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Get scenery.
     *
     * @param x     the x
     * @param y     the y
     * @param index the index
     * @return the scenery
     */
    public Scenery get(int x, int y, int index) {
        return objects[index][x][y];
    }

    @Override
    public Scenery[] getObjects(int chunkX, int chunkY) {
        Scenery[] objects = new Scenery[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            objects[i] = this.objects[i][chunkX][chunkY];
        }
        return objects;
    }

    /**
     * Get objects scenery [ ] [ ].
     *
     * @param index the index
     * @return the scenery [ ] [ ]
     */
    public Scenery[][] getObjects(int index) {
        return objects[index];
    }

    @Override
    public void setCurrentBase(Location currentBase) {
        for (int i = 0; i < objects.length; i++) {
            for (int x = 0; x < objects[i].length; x++) {
                for (int y = 0; y < objects[i][x].length; y++) {
                    if (objects[i][x][y] != null) {
                        Location newLoc = currentBase.transform(x, y, 0);
                        objects[i][x][y].setLocation(newLoc);
                    }
                }
            }
        }
        super.setCurrentBase(currentBase);
    }
}
