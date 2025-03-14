package core.net.packet.context;

import core.game.node.entity.player.Player;

/**
 * The type Dynamic scene context.
 */
public final class DynamicSceneContext extends SceneGraphContext {

    /**
     * Instantiates a new Dynamic scene context.
     *
     * @param player the player
     * @param login  the login
     */
    public DynamicSceneContext(Player player, boolean login) {
        super(player, login);
    }

}