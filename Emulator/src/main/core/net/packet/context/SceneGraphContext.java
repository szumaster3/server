package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Scene graph context.
 */
public class SceneGraphContext implements Context {

    private final Player player;

    private final boolean login;

    /**
     * Instantiates a new Scene graph context.
     *
     * @param player the player
     * @param login  the login
     */
    public SceneGraphContext(Player player, boolean login) {
        this.player = player;
        this.login = login;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Is login boolean.
     *
     * @return the boolean
     */
    public boolean isLogin() {
        return login;
    }

}