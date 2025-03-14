package content.global.skill.hunter;

import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Trap wrapper.
 */
public final class TrapWrapper {
    private final List<Item> items = new ArrayList<>(10);
    private final Player player;
    private final Traps type;
    private NetTrapSetting.NetTrap netType;
    private final int originalId;
    private Scenery scenery;
    private Scenery secondary;
    private TrapHook hook;
    private TrapNode reward;
    private boolean smoked;
    private boolean baited;
    private boolean failed;
    private int busyTicks;
    private int ticks;
    private final HunterManager instance;

    /**
     * Instantiates a new Trap wrapper.
     *
     * @param player  the player
     * @param type    the type
     * @param scenery the scenery
     */
    public TrapWrapper(final Player player, Traps type, Scenery scenery) {
        this.player = player;
        this.type = type;
        this.scenery = scenery;
        this.originalId = scenery.getId();
        this.ticks = GameWorld.getTicks() + (100);
        this.instance = HunterManager.getInstance(player);
        this.scenery.getAttributes().setAttribute("trap-uid", instance.getUid());
    }

    /**
     * Cycle boolean.
     *
     * @return the boolean
     */
    public boolean cycle() {
        if (isTimeUp() && type.settings.clear(this, 0)) {
            if (!isCaught()) {
                player.sendMessage(type.settings.getTimeUpMessage());
            }
            return true;
        }
        return false;
    }

    /**
     * Sets object.
     *
     * @param id the id
     */
    public void setObject(final int id) {
        Scenery newScenery = scenery.transform(id);
        SceneryBuilder.remove(scenery);
        this.scenery = SceneryBuilder.add(newScenery);
        this.scenery.getAttributes().setAttribute("trap-uid", instance.getUid());
    }

    /**
     * Smoke.
     */
    public void smoke() {
        if (smoked) {
            player.sendMessage("This trap has already been smoked.");
            return;
        }
        if (player.skills.getStaticLevel(Skills.HUNTER) < 39) {
            player.sendMessage("You need a Hunter level of at least 39 to be able to smoke traps.");
            return;
        }
        smoked = true;
        player.lock(4);
        player.visualize(new Animation(5208), new Graphics(931));
        player.sendMessage("You use the smoke from the torch to remove your scent from the trap.");
    }

    /**
     * Bait.
     *
     * @param bait the bait
     */
    public void bait(Item bait) {
        if (baited) {
            player.sendMessage("This trap has already been baited.");
            return;
        }
        if (!type.settings.hasBait(bait)) {
            player.sendMessage("You can't use that on this trap.");
            return;
        }
        baited = true;
        bait = new Item(bait.getId(), 1);
        player.getInventory().remove(new Item(bait.getId(), 1));
    }

    /**
     * Gets chance rate.
     *
     * @return the chance rate
     */
    public double getChanceRate() {
        double chance = 0.0;
        if (baited) {
            chance += 1.0;
        }
        if (smoked) {
            chance += 1.0;
        }
        chance += HunterGear.getChanceRate(player);
        return chance;
    }

    /**
     * Add item.
     *
     * @param items the items
     */
    public void addItem(Item... items) {
        for (Item item : items) {
            addItem(item);
        }
    }

    /**
     * Add item.
     *
     * @param item the item
     */
    public void addItem(Item item) {
        items.add(item);
    }

    private boolean isTimeUp() {
        return ticks < GameWorld.getTicks();
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public Traps getType() {
        return type;
    }

    /**
     * Gets object.
     *
     * @return the object
     */
    public Scenery getObject() {
        return scenery;
    }

    /**
     * Gets original id.
     *
     * @return the original id
     */
    public int getOriginalId() {
        return originalId;
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
     * Gets ticks.
     *
     * @return the ticks
     */
    public int getTicks() {
        return ticks;
    }

    /**
     * Sets ticks.
     *
     * @param ticks the ticks
     */
    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    /**
     * Is smoked boolean.
     *
     * @return the boolean
     */
    public boolean isSmoked() {
        return smoked;
    }

    /**
     * Sets smoked.
     *
     * @param smoked the smoked
     */
    public void setSmoked(boolean smoked) {
        this.smoked = smoked;
    }

    /**
     * Gets hook.
     *
     * @return the hook
     */
    public TrapHook getHook() {
        return hook;
    }

    /**
     * Sets hook.
     *
     * @param hook the hook
     */
    public void setHook(TrapHook hook) {
        this.hook = hook;
    }

    /**
     * Is baited boolean.
     *
     * @return the boolean
     */
    public boolean isBaited() {
        return baited;
    }

    /**
     * Sets baited.
     *
     * @param baited the baited
     */
    public void setBaited(boolean baited) {
        this.baited = baited;
    }

    /**
     * Is caught boolean.
     *
     * @return the boolean
     */
    public boolean isCaught() {
        return getReward() != null;
    }

    /**
     * Gets reward.
     *
     * @return the reward
     */
    public TrapNode getReward() {
        return reward;
    }

    /**
     * Sets reward.
     *
     * @param reward the reward
     */
    public void setReward(TrapNode reward) {
        this.reward = reward;
        this.addItem(reward.rewards);
    }

    /**
     * Is busy boolean.
     *
     * @return the boolean
     */
    public boolean isBusy() {
        return getBusyTicks() > GameWorld.getTicks();
    }

    /**
     * Gets busy ticks.
     *
     * @return the busy ticks
     */
    public int getBusyTicks() {
        return busyTicks;
    }

    /**
     * Sets busy ticks.
     *
     * @param busyTicks the busy ticks
     */
    public void setBusyTicks(int busyTicks) {
        this.busyTicks = GameWorld.getTicks() + busyTicks;
    }

    /**
     * Gets items.
     *
     * @return the items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Gets secondary.
     *
     * @return the secondary
     */
    public Scenery getSecondary() {
        return secondary;
    }

    /**
     * Sets secondary.
     *
     * @param secondary the secondary
     */
    public void setSecondary(Scenery secondary) {
        this.secondary = secondary;
        this.secondary.getAttributes().setAttribute("trap-uid", player.getName().hashCode());
    }

    /**
     * Gets net type.
     *
     * @return the net type
     */
    public NetTrapSetting.NetTrap getNetType() {
        return netType;
    }

    /**
     * Sets net type.
     *
     * @param netType the net type
     */
    public void setNetType(NetTrapSetting.NetTrap netType) {
        this.netType = netType;
    }

    /**
     * Sets object.
     *
     * @param scenery the scenery
     */
    public void setObject(Scenery scenery) {
        this.scenery = scenery;
    }

    /**
     * Is failed boolean.
     *
     * @return the boolean
     */
    public boolean isFailed() {
        return failed;
    }

    /**
     * Sets failed.
     *
     * @param failed the failed
     */
    public void setFailed(boolean failed) {
        this.failed = failed;
    }

}
