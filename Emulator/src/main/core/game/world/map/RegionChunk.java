package core.game.world.map;

import core.game.node.entity.player.Player;
import core.game.node.item.GroundItem;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
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
import java.util.List;

import static core.api.ContentAPIKt.log;

/**
 * The type Region chunk.
 */
public class RegionChunk {

    /**
     * The constant SIZE.
     */
    public static final int SIZE = 8;

    /**
     * The Base.
     */
    protected Location base;

    /**
     * The Current base.
     */
    protected Location currentBase;

    /**
     * The Plane.
     */
    protected RegionPlane plane;

    /**
     * The Items.
     */
    protected List<GroundItem> items;

    /**
     * The Objects.
     */
    protected Scenery[][] objects;

    /**
     * The Rotation.
     */
    protected int rotation;

	private List<UpdateFlag<?>> flags = new ArrayList<>(20);

    /**
     * Instantiates a new Region chunk.
     *
     * @param base     the base
     * @param rotation the rotation
     * @param plane    the plane
     */
    public RegionChunk(Location base, int rotation, RegionPlane plane) {
		this.base = base;
		this.currentBase = base;
		this.rotation = rotation;
		this.plane = plane;
		this.objects = new Scenery[SIZE][SIZE];
	}

    /**
     * Copy build region chunk.
     *
     * @param plane the plane
     * @return the build region chunk
     */
    public BuildRegionChunk copy(RegionPlane plane) {
		return new BuildRegionChunk(base, rotation, plane, this.objects);
	}

    /**
     * Flag.
     *
     * @param flag the flag
     */
    public void flag(UpdateFlag<?> flag) {
		flags.add(flag);
	}

    /**
     * Clear.
     */
    public void clear() {
		flags.clear();
		if (items != null && plane.getRegion() instanceof DynamicRegion) {
			items.clear();
			items = null;
		}
	}

    /**
     * Synchronize.
     *
     * @param player the player
     */
    public void synchronize(Player player) {
		IoBuffer buffer = UpdateAreaPosition.getChunkUpdateBuffer(player, currentBase);
		if (appendUpdate(player, buffer)) {
			player.getSession().write(buffer);
		}
	}

    /**
     * Append update boolean.
     *
     * @param player the player
     * @param buffer the buffer
     * @return the boolean
     */
    protected boolean appendUpdate(Player player, IoBuffer buffer) {
		boolean updated = false;
		int baseX = currentBase.getLocalX();
		int baseY = currentBase.getLocalY();
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				Scenery dyn = objects[x][y];
				if (dyn == null || plane.getObjects() == null) {
					continue;
				}
				Scenery stat = plane.getObjects()[baseX + x][baseY + y];
				if (!dyn.isRenderable() && stat != null) {
					ClearScenery.write(buffer, stat);
					updated = true;
				}
				else if (dyn != stat) {
					if (stat != null) {
						ClearScenery.write(buffer, stat);
					}
					ConstructScenery.write(buffer, dyn);
					updated = true;
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
     * Draw items array list.
     *
     * @param items  the items
     * @param player the player
     * @return the array list
     */
    public ArrayList<GroundItem> drawItems(List<GroundItem> items, Player player) {
		ArrayList<GroundItem> totalItems = items != null ? new ArrayList<GroundItem>(items) : new ArrayList<GroundItem>();

		if (player.getAttribute("chunkdraw", false)) {
			Location l = currentBase;
			for (int x = 0; x < SIZE; x++) {
				for (int y = 0; y < SIZE; y++) {
					boolean add = false;
					if (y == 0 || y == SIZE - 1)
						add = true;
					else if (x == 0 || x == SIZE - 1)
						add = true;
					if (add)
						totalItems.add(new GroundItem(new Item(13444), l.transform(x, y, 0), player));
				}
			}
		}

		if (player.getAttribute("regiondraw", false)) {
			Location l = currentBase;
			int localX = l.getLocalX();
			int localY = l.getLocalY();

			for (int x = 0; x < SIZE; x++)
				for (int y = 0; y < SIZE; y++) {
					boolean add = false;
					if (localY == 0 || localY == 56)
						if (localY == 0 && y == 0)
							add = true;
						else if (localY == 56 && y == SIZE - 1)
							add = true;
					if (localX == 0 || localX == 56)
						if (localX == 0 && x == 0)
							add = true;
						else if (localX == 56 && x == SIZE - 1)
							add = true;

					if (add)
						totalItems.add(new GroundItem(new Item(13444), l.transform(x,y,0), player));
				}
		}

		if (player.getAttribute("clippingdraw", false)) {
			Location l = currentBase;
			for (int x = 0; x < SIZE; x++) {
				for (int y = 0; y < SIZE; y++) {
					int flag = RegionManager.getClippingFlag(l.getZ(), l.getX() + x, l.getY() + y);
					if (flag > 0) {
						totalItems.add(new GroundItem(new Item(flag), l.transform(x, y, 0), player));
					}
				}
			}
		}

		return totalItems;
	}

    /**
     * Update.
     *
     * @param player the player
     */
    public void update(Player player) {
		if (isUpdated()) {
			IoBuffer buffer = UpdateAreaPosition.getChunkUpdateBuffer(player, currentBase);
			Object[] flagsArray = flags.toArray();
			int size = flagsArray.length;
			for (int i = 0; i < size; i++) {
				UpdateFlag<?> flag = (UpdateFlag<?>) flagsArray[i];
				flag.writeDynamic(buffer, player);
			}
			player.getSession().write(buffer);
		}
	}

    /**
     * Rotate.
     *
     * @param direction the direction
     */
    public void rotate(Direction direction) {
		if (rotation != 0) {
			log(this.getClass(), Log.ERR,  "Region chunk was already rotated!");
			return;
		}
		Scenery[][] copy = new Scenery[SIZE][SIZE];
		Scenery[][] staticCopy = new Scenery[SIZE][SIZE];
		int baseX = currentBase.getLocalX();
		int baseY = currentBase.getLocalY();
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				copy[x][y] = objects[x][y];
				staticCopy[x][y] = plane.getObjects()[baseX + x][baseY + y];
				objects[x][y] = plane.getObjects()[baseX + x][baseY + y] = null;
				plane.getFlags().clearFlag(baseX + x, baseY + y);
			}
		}
		rotation = direction.toInteger();
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				Scenery object = copy[x][y];
				Scenery stat = staticCopy[x][y];
				boolean match = object == stat;
				if (stat == null) {
					continue;
				}
				int[] pos = getRotatedPosition(x, y, stat.getDefinition().getSizeX(), stat.getDefinition().getSizeY(), stat.getRotation(), rotation);
				if (object != null) {
					object = object.transform(object.getId(), (object.getRotation() + rotation) % 4, object.getLocation().transform(pos[0] - x, pos[1] - y, 0));
					LandscapeParser.flagScenery(plane, baseX + pos[0], baseY + pos[1], object, true, true);
				}
				if (match) {
					stat = object;
				} else {
					stat = stat.transform(stat.getId(), (stat.getRotation() + rotation) % 4, stat.getLocation().transform(pos[0] - x, pos[1] - y, 0));
				}
				plane.getObjects()[baseX + pos[0]][baseY + pos[1]] = stat;
			}
		}
	}

    /**
     * Get rotated position int [ ].
     *
     * @param x             the x
     * @param y             the y
     * @param sizeX         the size x
     * @param sizeY         the size y
     * @param rotation      the rotation
     * @param chunkRotation the chunk rotation
     * @return the int [ ]
     */
    public static int[] getRotatedPosition(int x, int y, int sizeX, int sizeY, int rotation, int chunkRotation) {
		if ((rotation & 0x1) == 1) {
			int s = sizeX;
			sizeX = sizeY;
			sizeY = s;
		}
		if (chunkRotation == 0) {
			return new int[] { x, y };
		}
		if (chunkRotation == 1) {
			return new int[] { y, 7 - x - (sizeX - 1) };
		}
		if (chunkRotation == 2) {
			return new int[] { 7 - x - (sizeX - 1), 7 - y - (sizeY - 1) };
		}
		return new int[] { 7 - y - (sizeY - 1), x };
	}

    /**
     * Gets items.
     *
     * @return the items
     */
    public List<GroundItem> getItems() {
		if (items == null) {
			items = new ArrayList<GroundItem>();
		}
		return items;
	}

    /**
     * Sets items.
     *
     * @param items the items
     */
    public void setItems(List<GroundItem> items) {
		this.items = items;
	}

    /**
     * Get objects scenery [ ].
     *
     * @param chunkX the chunk x
     * @param chunkY the chunk y
     * @return the scenery [ ]
     */
    public Scenery[] getObjects(int chunkX, int chunkY) {
		return new Scenery[] { objects[chunkX][chunkY] };
	}

    /**
     * Get objects scenery [ ] [ ].
     *
     * @return the scenery [ ] [ ]
     */
    public Scenery[][] getObjects() {
		return objects;
	}

    /**
     * Sets objects.
     *
     * @param objects the objects
     */
    public void setObjects(Scenery[][] objects) {
		this.objects = objects;
	}

    /**
     * Gets base.
     *
     * @return the base
     */
    public Location getBase() {
		return base;
	}

    /**
     * Sets base.
     *
     * @param base the base
     */
    public void setBase(Location base) {
		this.base = base;
	}

    /**
     * Gets rotation.
     *
     * @return the rotation
     */
    public int getRotation() {
		return rotation;
	}

    /**
     * Sets rotation.
     *
     * @param rotation the rotation
     */
    public void setRotation(int rotation) {
		this.rotation = rotation;
	}

    /**
     * Is updated boolean.
     *
     * @return the boolean
     */
    public boolean isUpdated() {
		return !flags.isEmpty();
	}

    /**
     * Reset flags.
     */
    public void resetFlags() {
		flags.clear();
	}

    /**
     * Gets plane.
     *
     * @return the plane
     */
    public RegionPlane getPlane() {
		return plane;
	}

    /**
     * Gets current base.
     *
     * @return the current base
     */
    public Location getCurrentBase() {
		return currentBase;
	}

    /**
     * Sets current base.
     *
     * @param currentBase the current base
     */
    public void setCurrentBase(Location currentBase) {
		this.currentBase = currentBase;
	}

    /**
     * Rebuild flags.
     *
     * @param from the from
     */
    public void rebuildFlags(RegionPlane from) {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Location loc = currentBase.transform(x,y,0);
				Location fromLoc = base.transform(x,y,0);
				plane.getFlags().getLandscape()[loc.getLocalX()][loc.getLocalY()] = from.getFlags().getLandscape()[fromLoc.getLocalX()][fromLoc.getLocalY()];
				plane.getFlags().clearFlag(x, y);
				Scenery obj = objects[x][y];
				if (obj != null)
					LandscapeParser.flagScenery(plane, loc.getLocalX(), loc.getLocalY(), obj, false, true);
			}
		}
	}

}
