package core.game.world.update.flag;

import core.game.world.map.Location;
import core.game.world.map.RegionChunk;
import core.game.world.map.Viewport;

/**
 * The type Player flag.
 */
public final class PlayerFlag {

    private boolean updateSceneGraph;

    private RegionChunk[][] lastViewport = new RegionChunk[Viewport.CHUNK_SIZE][Viewport.CHUNK_SIZE];

    private Location lastSceneGraph;

    /**
     * Instantiates a new Player flag.
     */
    public PlayerFlag() {

    }

    /**
     * Is update scene graph boolean.
     *
     * @return the boolean
     */
    public boolean isUpdateSceneGraph() {
        return updateSceneGraph;
    }

    /**
     * Sets update scene graph.
     *
     * @param updateSceneGraph the update scene graph
     */
    public void setUpdateSceneGraph(boolean updateSceneGraph) {
        this.updateSceneGraph = updateSceneGraph;
    }

    /**
     * Gets last scene graph.
     *
     * @return the last scene graph
     */
    public Location getLastSceneGraph() {
        return lastSceneGraph;
    }

    /**
     * Sets last scene graph.
     *
     * @param lastSceneGraph the last scene graph
     */
    public void setLastSceneGraph(Location lastSceneGraph) {
        this.lastSceneGraph = lastSceneGraph;
    }

    /**
     * Get last viewport region chunk [ ] [ ].
     *
     * @return the region chunk [ ] [ ]
     */
    public RegionChunk[][] getLastViewport() {
        return lastViewport;
    }

    /**
     * Sets last viewport.
     *
     * @param lastViewport the last viewport
     */
    public void setLastViewport(RegionChunk[][] lastViewport) {
        this.lastViewport = lastViewport;
    }

}
