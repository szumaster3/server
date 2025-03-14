package core.game.world.map;

import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItem;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.map.build.DynamicRegion;
import core.game.world.map.build.RegionFlags;
import core.game.world.update.flag.chunk.ItemUpdateFlag;
import core.net.packet.PacketRepository;
import core.net.packet.context.BuildItemContext;
import core.net.packet.out.ClearGroundItem;
import core.net.packet.out.ConstructGroundItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The type Region plane.
 */
public final class RegionPlane {

    /**
     * The constant REGION_SIZE.
     */
    public static final int REGION_SIZE = 64;

    /**
     * The constant CHUNK_SIZE.
     */
    public static final int CHUNK_SIZE = REGION_SIZE >> 3;

    /**
     * The constant NULL_OBJECT.
     */
    public static final Scenery NULL_OBJECT = new Scenery(0, Location.create(0, 0, 0));

	private final int plane;

	private final Region region;

	private final RegionFlags flags;

	private final RegionFlags projectileFlags;

	private final RegionChunk[][] chunks;

	private final List<NPC> npcs;

	private final List<Player> players;

	private Scenery[][] objects;

    /**
     * Instantiates a new Region plane.
     *
     * @param region the region
     * @param plane  the plane
     */
    public RegionPlane(Region region, int plane) {
		this.plane = plane;
		this.region = region;
		this.players = new CopyOnWriteArrayList<Player>();
		this.npcs = new CopyOnWriteArrayList<NPC>();
		Location base = region.getBaseLocation();
		this.flags = new RegionFlags(plane, base.getX(), base.getY());
		this.projectileFlags = new RegionFlags(plane, base.getX(), base.getY(), true);
		this.objects = new Scenery[REGION_SIZE][REGION_SIZE];
		this.chunks = new RegionChunk[CHUNK_SIZE][CHUNK_SIZE];
	}

    /**
     * Pulse.
     */
    public void pulse() {
		Arrays.stream(chunks).forEach(regionChunks -> {Arrays.stream(regionChunks).filter(Objects::nonNull).forEach(RegionChunk::resetFlags);});
	}

    /**
     * Add.
     *
     * @param object    the object
     * @param x         the x
     * @param y         the y
     * @param landscape the landscape
     */
    public void add(Scenery object, int x, int y, boolean landscape) {
		setChunkObject(x, y, object);
		if (landscape) {
			objects[x][y] = object;
		}
		if (object != null) {
			object.setRenderable(true);
		}
	}

    /**
     * Gets region chunk.
     *
     * @param chunkX the chunk x
     * @param chunkY the chunk y
     * @return the region chunk
     */
    public RegionChunk getRegionChunk(int chunkX, int chunkY) {
		RegionChunk r = chunks[chunkX][chunkY];
		if (r != null) {
			return r;
		}
		if (region.isBuild()) {
			return chunks[chunkX][chunkY] = new BuildRegionChunk(region.getBaseLocation().transform(chunkX << 3, chunkY << 3, plane), 0, this);
		}
		return chunks[chunkX][chunkY] = new RegionChunk(region.getBaseLocation().transform(chunkX << 3, chunkY << 3, plane), 0, this);
	}

    /**
     * Sets region chunk.
     *
     * @param chunkX the chunk x
     * @param chunkY the chunk y
     * @param chunk  the chunk
     */
    public void setRegionChunk(int chunkX, int chunkY, RegionChunk chunk) {
		chunks[chunkX][chunkY] = chunk;
	}

    /**
     * Remove.
     *
     * @param x the x
     * @param y the y
     */
    public void remove(int x, int y) {
		remove(x, y, -1);
	}

    /**
     * Remove.
     *
     * @param x        the x
     * @param y        the y
     * @param objectId the object id
     */
    public void remove(int x, int y, int objectId) {
		int chunkX = x / CHUNK_SIZE;
		int chunkY = y / CHUNK_SIZE;
		int offsetX = x - chunkX * CHUNK_SIZE;
		int offsetY = y - chunkY * CHUNK_SIZE;
		RegionChunk chunk = getRegionChunk(chunkX, chunkY);
		Scenery remove = new Scenery(0, region.getBaseLocation().transform(x, y, plane), 22, 0);
		remove.setRenderable(false);
		if (chunk instanceof BuildRegionChunk) {
			int index = ((BuildRegionChunk) chunk).getIndex(offsetX, offsetY, objectId);
			((BuildRegionChunk) chunk).getObjects(index)[offsetX][offsetY] = remove;
			return;
		}
		chunk.getObjects()[offsetX][offsetY] = remove;
	}

	private void setChunkObject(int x, int y, Scenery object) {
		int chunkX = x / CHUNK_SIZE;
		int chunkY = y / CHUNK_SIZE;
		int offsetX = x - chunkX * CHUNK_SIZE;
		int offsetY = y - chunkY * CHUNK_SIZE;
		RegionChunk r = getRegionChunk(chunkX, chunkY);
		if (r instanceof BuildRegionChunk) {
			((BuildRegionChunk) r).store(object);
			return;
		}
		r.getObjects()[offsetX][offsetY] = object;
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
     * Gets object list.
     *
     * @return the object list
     */
    public List<Scenery> getObjectList() {
            ArrayList<Scenery> list = new ArrayList();
            for (int x = 0; x < REGION_SIZE; x++) {
                for (int y = 0; y < REGION_SIZE; y++) {
                    if (objects[x][y] != null)
                        list.add(objects[x][y]);
                }
            }
            return list;
        }

    /**
     * Clear.
     */
    public void clear() {
		for (RegionChunk[] c : chunks) {
			for (RegionChunk chunk : c) {
				if (chunk != null) {
					chunk.clear();
				}
			}
		}
		if (region instanceof DynamicRegion && objects != null) {
			for (int x = 0; x < REGION_SIZE; x++) {
				for (int y = 0; y < REGION_SIZE; y++) {
					objects[x][y] = null;
				}
			}
			objects = null;
		}
	}

    /**
     * Add.
     *
     * @param npc the npc
     */
    public void add(NPC npc) {
		npcs.add(npc);
	}

    /**
     * Add.
     *
     * @param player the player
     */
    public void add(Player player) {
		players.add(player);
	}

    /**
     * Add.
     *
     * @param item the item
     */
    public void add(GroundItem item) {
		Location l = item.getLocation();
		RegionChunk c = getRegionChunk(l.getLocalX() / RegionChunk.SIZE, l.getLocalY() / RegionChunk.SIZE);
		if (!c.getItems().add(item)) {
			return;
		}
		GroundItem g = (GroundItem) item;
		if (g.isPrivate()) {
			if (g.getDropper() != null) {
				PacketRepository.send(ConstructGroundItem.class, new BuildItemContext(g.getDropper(), item));
			}
			return;
		}
		c.flag(new ItemUpdateFlag(g, ItemUpdateFlag.CONSTRUCT_TYPE));
	}

    /**
     * Remove.
     *
     * @param npc the npc
     */
    public void remove(NPC npc) {
		npcs.remove(npc);
	}

    /**
     * Remove.
     *
     * @param player the player
     */
    public void remove(Player player) {
		players.remove(player);
	}

    /**
     * Remove.
     *
     * @param item the item
     */
    public void remove(GroundItem item) {
		Location l = item.getLocation();
		RegionChunk c = getRegionChunk(l.getLocalX() / RegionChunk.SIZE, l.getLocalY() / RegionChunk.SIZE);
		if (!c.getItems().remove(item)) {
			return;
		}
		if (item.isPrivate()) {
			if (item.getDropper() != null && item.getDropper().isPlaying() && item.getDropper().getLocation().withinDistance(l)) {
				PacketRepository.send(ClearGroundItem.class, new BuildItemContext(item.getDropper(), item));
			}
			return;
		}
		c.flag(new ItemUpdateFlag(item, ItemUpdateFlag.REMOVE_TYPE));
	}

    /**
     * Gets flags.
     *
     * @return the flags
     */
    public RegionFlags getFlags() {
		return flags;
	}

    /**
     * Gets projectile flags.
     *
     * @return the projectile flags
     */
    public RegionFlags getProjectileFlags() {
		return projectileFlags;
	}

    /**
     * Gets npcs.
     *
     * @return the npcs
     */
    public List<NPC> getNpcs() {
		return npcs;
	}

    /**
     * Gets entities.
     *
     * @return the entities
     */
    public List<Node> getEntities()
	{
	    List<Node> entities = new ArrayList<>(npcs);
		Arrays.stream(getObjects()).forEach(o -> entities.addAll(Arrays.asList(o)));
	    return  entities;
	}

    /**
     * Gets players.
     *
     * @return the players
     */
    public List<Player> getPlayers() {
		return players;
	}

    /**
     * Gets plane.
     *
     * @return the plane
     */
    public int getPlane() {
		return plane;
	}

    /**
     * Gets region.
     *
     * @return the region
     */
    public Region getRegion() {
		return region;
	}

    /**
     * Gets chunk object.
     *
     * @param x the x
     * @param y the y
     * @return the chunk object
     */
    public Scenery getChunkObject(int x, int y) {
		return getChunkObject(x, y, -1);
	}

    /**
     * Gets chunk object.
     *
     * @param x        the x
     * @param y        the y
     * @param objectId the object id
     * @return the chunk object
     */
    public Scenery getChunkObject(int x, int y, int objectId) {
		int chunkX = x / CHUNK_SIZE;
		int chunkY = y / CHUNK_SIZE;
		int offsetX = x - chunkX * CHUNK_SIZE;
		int offsetY = y - chunkY * CHUNK_SIZE;
		RegionChunk chunk = getRegionChunk(chunkX, chunkY);
		if (chunk instanceof BuildRegionChunk) {
			BuildRegionChunk brc = (BuildRegionChunk) chunk;
			return brc.get(offsetX, offsetY, brc.getIndex(offsetX, offsetY, objectId));
		}
		return getRegionChunk(chunkX, chunkY).getObjects()[offsetX][offsetY];
	}

    /**
     * Gets chunk items.
     *
     * @param x the x
     * @param y the y
     * @return the chunk items
     */
    public List<GroundItem> getChunkItems(int x, int y) {
		int chunkX = x / CHUNK_SIZE;
		int chunkY = y / CHUNK_SIZE;
		return getRegionChunk(chunkX, chunkY).getItems();
	}

    /**
     * Gets item.
     *
     * @param itemId the item id
     * @param l      the l
     * @param player the player
     * @return the item
     */
    public GroundItem getItem(int itemId, Location l, Player player) {
		GroundItem groundItem = null;
		for (Item item : getChunkItems(l.getLocalX(), l.getLocalY())) {
			GroundItem g = (GroundItem) item;
			if (g.getId() == itemId && l.equals(g.getLocation()) && !g.isRemoved()) {
				if (groundItem != null && (!g.isPrivate() || player == null)) {
					continue;
				}
				if ((!g.isPrivate() || player == null || g.droppedBy(player))) {
					groundItem = g;
				}
			}
		}
		return groundItem;
	}

    /**
     * Get chunks region chunk [ ] [ ].
     *
     * @return the region chunk [ ] [ ]
     */
    public RegionChunk[][] getChunks() {
		return chunks;
	}

}
