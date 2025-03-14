package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Walk option context.
 */
public final class WalkOptionContext implements Context {

    private final Player player;

    private final String option;

    /**
     * Instantiates a new Walk option context.
     *
     * @param player the player
     * @param option the option
     */
    public WalkOptionContext(Player player, String option) {
        this.player = player;
        this.option = option;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets option.
     *
     * @return the option
     */
    public String getOption() {
        return option;
    }

}