package core.game.node.item;

import core.game.node.entity.player.Player;
import core.game.world.GameWorld;
import core.game.world.map.Location;

/**
 * The type Ground item.
 */
public class GroundItem extends Item {

    private Player dropper;

    private int dropperUid;

    private int ticks;

    private int decayTime;

    private boolean remainPrivate;

    private boolean removed;

    /**
     * The Force visible.
     */
    public boolean forceVisible;

    /**
     * Instantiates a new Ground item.
     *
     * @param item the item
     */
    public GroundItem(Item item) {
        this(item, null, null);
    }

    /**
     * Instantiates a new Ground item.
     *
     * @param item     the item
     * @param location the location
     */
    public GroundItem(Item item, Location location) {
        this(item, location, 200, null);
    }

    /**
     * Instantiates a new Ground item.
     *
     * @param item     the item
     * @param location the location
     * @param player   the player
     */
    public GroundItem(Item item, Location location, Player player) {
        this(item, location, 200, player);
    }

    /**
     * Instantiates a new Ground item.
     *
     * @param item      the item
     * @param location  the location
     * @param playerUid the player uid
     * @param ticks     the ticks
     */
    public GroundItem(Item item, Location location, int playerUid, int ticks) {
        this(item, location);
        this.dropperUid = playerUid;
        this.decayTime = ticks;
    }

    /**
     * Instantiates a new Ground item.
     *
     * @param item     the item
     * @param location the location
     * @param decay    the decay
     * @param player   the player
     */
    public GroundItem(Item item, Location location, int decay, Player player) {
        super(item.getId(), item.getAmount(), item.getCharge());
        super.location = location;
        super.index = -1;
        super.interactPlugin.setDefault();
        this.dropper = player;
        this.dropperUid = player != null ? player.getDetails().getUid() : -1;
        this.ticks = GameWorld.getTicks();
        this.decayTime = ticks + decay;
    }

    /**
     * Respawn.
     */
    public void respawn() {
        // Placeholder for respawn logic
    }

    /**
     * Is private boolean.
     *
     * @return the boolean
     */
    public boolean isPrivate() {
        return !forceVisible && (remainPrivate || (decayTime - GameWorld.getTicks() > 100));
    }

    @Override
    public boolean isActive() {
        // Returns {@code true} if the item is active, i.e., it hasn't decayed or been removed.
        return !removed && GameWorld.getTicks() < decayTime;
    }

    /**
     * Dropped by boolean.
     *
     * @param p the p
     * @return the boolean
     */
    public boolean droppedBy(Player p) {
        if (p.getDetails().getUid() == dropperUid) {
            dropper = p;
            return true;
        }
        return false;
    }

    /**
     * Gets dropper.
     *
     * @return the dropper
     */
    public Player getDropper() {
        return dropper;
    }

    /**
     * Sets dropper.
     *
     * @param player the player
     */
    public void setDropper(Player player) {
        this.dropper = player;
    }

    /**
     * Gets ticks.
     *
     * @return the ticks
     */
    public int getTicks() {
        return ticks;
    }

    /**
     * Gets decay time.
     *
     * @return the decay time
     */
    public int getDecayTime() {
        return decayTime;
    }

    /**
     * Sets decay time.
     *
     * @param decayTime the decay time
     */
    public void setDecayTime(int decayTime) {
        this.decayTime = GameWorld.getTicks() + decayTime;
    }

    /**
     * Is auto spawn boolean.
     *
     * @return the boolean
     */
    public boolean isAutoSpawn() {
        return false;
    }

    /**
     * Is remain private boolean.
     *
     * @return the boolean
     */
    public boolean isRemainPrivate() {
        return remainPrivate;
    }

    /**
     * Sets remain private.
     *
     * @param remainPrivate the remain private
     */
    public void setRemainPrivate(boolean remainPrivate) {
        this.remainPrivate = remainPrivate;
    }

    /**
     * Is removed boolean.
     *
     * @return the boolean
     */
    public boolean isRemoved() {
        return removed;
    }

    /**
     * Sets removed.
     *
     * @param removed the removed
     */
    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    /**
     * Gets dropper uid.
     *
     * @return the dropper uid
     */
    public int getDropperUid() {
        return dropperUid;
    }

    @Override
    public String toString() {
        return "GroundItem [dropper=" + (dropper != null ? dropper.getUsername() : dropper) +
            ", ticks=" + ticks +
            ", decayTime=" + decayTime +
            ", remainPrivate=" + remainPrivate +
            ", removed=" + removed + "]";
    }
}
