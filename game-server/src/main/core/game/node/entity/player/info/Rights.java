package core.game.node.entity.player.info;

import core.ServerConstants;
import core.game.node.entity.player.Player;

/**
 * The enum Rights.
 */
public enum Rights {

    /**
     * Regular player rights.
     */
    REGULAR_PLAYER,

    /**
     * Player moderator rights.
     */
    PLAYER_MODERATOR,

    /**
     * The Administrator.
     */
    ADMINISTRATOR() {
        @Override
        public boolean isVisible(Player player) {

            return player.getAttribute("visible_rank", ADMINISTRATOR) == ADMINISTRATOR;
        }
    };

    /**
     * Gets chat icon.
     *
     * @param player the player
     * @return the chat icon
     */
    public static int getChatIcon(Player player) {

        Rights c = player.getAttribute("visible_rank", player.getDetails().getRights());

        if (c != Rights.REGULAR_PLAYER && c != null) {
            return c.toInteger();
        }

        if (ServerConstants.IRONMAN_ICONS) {
            if (player.getIronmanManager().isIronman()) {

                return player.getIronmanManager().getMode().icon;
            }
        }

        return 0;
    }

    /**
     * Is hidden boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public static boolean isHidden(final Player player) {

        return player.getAttribute("visible_rank", player.getDetails().getRights()) != player.getDetails().getRights();
    }

    /**
     * To integer int.
     *
     * @return the int
     */
    public final int toInteger() {

        return ordinal();
    }

    /**
     * For id rights.
     *
     * @param id the id
     * @return the rights
     */
    public static Rights forId(int id) {

        if (id < 0) {
            id = 0;
        }

        return values()[id];
    }

    /**
     * Is visible boolean.
     *
     * @param username the username
     * @return the boolean
     */
    public boolean isVisible(Player username) {

        return true;
    }
}