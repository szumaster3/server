package core.game.world.map;

import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * The type Viewport.
 */
public final class Viewport {

    /**
     * The constant CHUNK_SIZE.
     */
    public static final int CHUNK_SIZE = 5;

	private Region region;

	private RegionChunk[][] chunks = new RegionChunk[CHUNK_SIZE][CHUNK_SIZE];

	private RegionPlane currentPlane;

	private List<RegionPlane> viewingPlanes = new LinkedList<>();

    /**
     * Instantiates a new Viewport.
     */
    public Viewport() {

	}

    /**
     * Update viewport.
     *
     * @param entity the entity
     */
    public void updateViewport(Entity entity) {
		RegionChunk chunk = RegionManager.getRegionChunk(entity.getLocation());
		int center = chunks.length >> 1;
		if (chunks[center][center] == chunk) {
			return;
		}
		int offset = center * -8;
		Location l = chunk.getCurrentBase();
		for (int x = 0; x < chunks.length; x++) {
			for (int y = 0; y < chunks[x].length; y++) {
				chunks[x][y] = RegionManager.getRegionChunk(l.transform(offset + (8 * x), offset + (8 * y), 0));
			}
		}
	}

    /**
     * Remove.
     *
     * @param entity the entity
     */
    public void remove(Entity entity) {
		if (region == null) {
			return;
		}
		if (entity instanceof Player) {
			region.remove((Player) entity);
			Region region = null;
			for (RegionPlane r : viewingPlanes) {
				if (region != r.getRegion()) {
					region = r.getRegion();
					region.decrementViewAmount();
					region.checkInactive();
				}
			}
		} else {
			region.remove((NPC) entity);
		}
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
     * Sets region.
     *
     * @param region the region
     */
    public void setRegion(Region region) {
		this.region = region;
	}

    /**
     * Get chunks region chunk [ ] [ ].
     *
     * @return the region chunk [ ] [ ]
     */
    public RegionChunk[][] getChunks() {
		return chunks;
	}

    /**
     * Sets chunks.
     *
     * @param chunks the chunks
     */
    public void setChunks(RegionChunk[][] chunks) {
		this.chunks = chunks;
	}

    /**
     * Gets viewing planes.
     *
     * @return the viewing planes
     */
    public List<RegionPlane> getViewingPlanes() {
		return viewingPlanes;
	}

    /**
     * Sets viewing planes.
     *
     * @param regions the regions
     */
    public void setViewingPlanes(List<RegionPlane> regions) {
		this.viewingPlanes = regions;
	}

    /**
     * Gets current plane.
     *
     * @return the current plane
     */
    public RegionPlane getCurrentPlane() {
		return currentPlane;
	}

    /**
     * Sets current plane.
     *
     * @param currentPlane the current plane
     */
    public void setCurrentPlane(RegionPlane currentPlane) {
		this.currentPlane = currentPlane;
	}

}
