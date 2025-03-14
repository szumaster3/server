package core.game.node.entity.player.link;

import core.game.node.entity.player.Player;

/**
 * The type Ironman manager.
 */
public class IronmanManager {

    private final Player player;

    private IronmanMode mode = IronmanMode.NONE;

    /**
     * Instantiates a new Ironman manager.
     *
     * @param player the player
     */
    public IronmanManager(Player player) {
        this.player = player;
    }

    /**
     * Check restriction boolean.
     *
     * @return the boolean
     */
    public boolean checkRestriction() {
        return checkRestriction(IronmanMode.STANDARD);
    }

    /**
     * Check restriction boolean.
     *
     * @param mode the mode
     * @return the boolean
     */
    public boolean checkRestriction(IronmanMode mode) {
        if (isIronman() && this.mode.ordinal() >= mode.ordinal()) {
            player.sendMessage("You can't do that as an Ironman.");
            return true;
        }
        return false;
    }

    /**
     * Is ironman boolean.
     *
     * @return the boolean
     */
    public boolean isIronman() {
        return mode != IronmanMode.NONE;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets mode.
     *
     * @return the mode
     */
    public IronmanMode getMode() {
        return mode;
    }

    /**
     * Sets mode.
     *
     * @param mode the mode
     */
    public void setMode(IronmanMode mode) {
        this.mode = mode;
    }

}
