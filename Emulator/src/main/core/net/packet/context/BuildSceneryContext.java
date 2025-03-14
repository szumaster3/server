package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.net.packet.Context;

/**
 * The type Build scenery context.
 */
public final class BuildSceneryContext implements Context {

    private final Player player;

    private final Scenery scenery;

    /**
     * Instantiates a new Build scenery context.
     *
     * @param player  the player
     * @param scenery the scenery
     */
    public BuildSceneryContext(Player player, Scenery scenery) {
        this.player = player;
        this.scenery = scenery;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets scenery.
     *
     * @return the scenery
     */
    public Scenery getScenery() {
        return scenery;
    }

}