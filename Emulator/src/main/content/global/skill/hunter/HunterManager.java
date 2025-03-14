package content.global.skill.hunter;

import core.api.Event;
import core.api.LoginListener;
import core.api.LogoutListener;
import core.game.event.EventHook;
import core.game.event.TickEvent;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.scenery.Scenery;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static core.api.ContentAPIKt.setAttribute;

/**
 * The type Hunter manager.
 */
public final class HunterManager implements LoginListener, LogoutListener, EventHook<TickEvent> {

    private final List<TrapWrapper> traps = new ArrayList<>(20);

    private final Player player;

    /**
     * Instantiates a new Hunter manager.
     *
     * @param player the player
     */
    public HunterManager(final Player player) {
        this.player = player;
    }

    /**
     * Instantiates a new Hunter manager.
     */
    public HunterManager() {
        this.player = null;
    }

    @Override
    public void login(@NotNull Player player) {
        HunterManager instance = new HunterManager(player);
        player.hook(Event.getTick(), instance);
        setAttribute(player, "hunter-manager", instance);
    }

    @Override
    public void logout(@NotNull Player player) {
        HunterManager instance = getInstance(player);
        if (instance == null) return;
        Iterator<TrapWrapper> iterator = instance.traps.iterator();
        TrapWrapper wrapper = null;
        while (iterator.hasNext()) {
            wrapper = iterator.next();
            if (wrapper.getType().settings.clear(wrapper, 0)) {
                iterator.remove();
            }
        }
    }

    @Override
    public void process(@NotNull Entity entity, @NotNull TickEvent event) {
        if (traps.size() == 0) {
            return;
        }
        Iterator<TrapWrapper> iterator = traps.iterator();
        TrapWrapper wrapper = null;
        while (iterator.hasNext()) {
            wrapper = iterator.next();
            if (wrapper.cycle()) {
                iterator.remove();
            }
        }
    }

    /**
     * Register boolean.
     *
     * @param trap    the trap
     * @param node    the node
     * @param scenery the scenery
     * @return the boolean
     */
    public boolean register(Traps trap, Node node, final Scenery scenery) {
        final TrapWrapper wrapper = new TrapWrapper(player, trap, scenery);
        trap.settings.reward(player, node, wrapper);
        wrapper.setHook(trap.addHook(wrapper));
        return traps.add(wrapper);
    }

    /**
     * Deregister boolean.
     *
     * @param wrapper the wrapper
     * @return the boolean
     */
    public boolean deregister(final TrapWrapper wrapper) {
        return traps.remove(wrapper);
    }

    /**
     * Is owner boolean.
     *
     * @param scenery the scenery
     * @return the boolean
     */
    public boolean isOwner(Scenery scenery) {
        return getUid(scenery) == getUid();
    }

    /**
     * Gets wrapper.
     *
     * @param scenery the scenery
     * @return the wrapper
     */
    public TrapWrapper getWrapper(Scenery scenery) {
        for (TrapWrapper wrapper : traps) {
            if (wrapper.getObject() == scenery || (wrapper.getSecondary() != null && wrapper.getSecondary() == scenery)) {
                return wrapper;
            }
        }
        return null;
    }

    /**
     * Exceeds trap limit boolean.
     *
     * @param trap the trap
     * @return the boolean
     */
    public boolean exceedsTrapLimit(Traps trap) {
        if (trap.settings.exceedsLimit(player)) {
            return true;
        }
        return traps.size() + 1 > getMaximumTraps();
    }

    /**
     * Gets trap amount.
     *
     * @return the trap amount
     */
    public int getTrapAmount() {
        return traps.size();
    }

    /**
     * Gets maximum traps.
     *
     * @return the maximum traps
     */
    public int getMaximumTraps() {
        final int level = getStaticLevel();
        return level >= 80 ? 5 : level >= 60 ? 4 : level >= 40 ? 3 : level >= 20 ? 2 : 1;
    }

    /**
     * Gets uid.
     *
     * @param scenery the scenery
     * @return the uid
     */
    public int getUid(Scenery scenery) {
        return scenery.getAttributes().getAttribute("trap-uid", 0);
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public int getUid() {
        return player.getName().hashCode();
    }

    /**
     * Gets static level.
     *
     * @return the static level
     */
    public int getStaticLevel() {
        return player.getSkills().getStaticLevel(Skills.HUNTER);
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
     * Gets traps.
     *
     * @return the traps
     */
    public List<TrapWrapper> getTraps() {
        return traps;
    }

    /**
     * Gets instance.
     *
     * @param player the player
     * @return the instance
     */
    public static HunterManager getInstance(Player player) {
        return player.getAttribute("hunter-manager");
    }
}
