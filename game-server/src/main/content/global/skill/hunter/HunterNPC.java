package content.global.skill.hunter;

import core.api.ContentAPIKt;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.TeleportManager;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.tools.RandomFunction;
import org.rs.consts.NPCs;

import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.getPathableRandomLocalCoordinate;
import static core.api.ContentAPIKt.sendGraphics;

/**
 * The type Hunter npc.
 */
public final class HunterNPC extends AbstractNPC {

    private static final int IMP_TELEPORT_CHANCE_ON_HIT = 10;
    private static final int IMP_TELEPORT_CHANCE_ON_TICK = 1000;

    private final Traps trap;

    private final TrapNode type;

    /**
     * Instantiates a new Hunter npc.
     */
    public HunterNPC() {
        this(0, null, null, null);
        this.setWalks(true);
    }

    /**
     * Instantiates a new Hunter npc.
     *
     * @param id       the id
     * @param location the location
     * @param trap     the trap
     * @param type     the type
     */
    public HunterNPC(int id, Location location, Traps trap, TrapNode type) {
        super(id, location);
        this.trap = trap;
        this.type = type;
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        Object[] data = Traps.getNode(id);
        return new HunterNPC(id, location, (Traps) data[0], (TrapNode) data[1]);
    }

    @Override
    public void updateLocation(Location last) {
        final TrapWrapper wrapper = trap.getByHook(getLocation());
        if (wrapper != null) {
            wrapper.getType().catchNpc(wrapper, this);
        }
    }

    @Override
    protected Location getMovementDestination() {
        if (trap.getHooks().size() == 0 || RandomFunction.random(170) > 5) {
            return super.getMovementDestination();
        }
        TrapHook hook = trap.getHooks().get(RandomFunction.random(trap.getHooks().size()));
        if (hook == null || !type.canCatch(hook.getWrapper(), this)) {
            return super.getMovementDestination();
        }
        Location destination = hook.getChanceLocation();
        return destination != null && destination.getDistance(getLocation()) <= 24 ? destination : super.getMovementDestination();
    }

    @Override
    public void handleDrops(Player p, Entity killer) {
        int ticks = getAttribute("hunter", 0);
        if (ticks < GameWorld.getTicks()) {
            super.handleDrops(p, killer);
        }
    }

    @Override
    public int[] getIds() {
        List<Integer> ids = new ArrayList<>(10);
        for (Traps t : Traps.values()) {
            for (TrapNode node : t.nodes) {
                for (int id : node.getNpcIds()) {
                    ids.add(id);
                }
            }
        }
        int[] array = new int[ids.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = ids.get(i);
        }
        return array;
    }

    @Override
    public void checkImpact(BattleState state) {
        super.checkImpact(state);

        if (this.getId() == NPCs.IMP_708 || this.getId() == NPCs.IMP_709 || this.getId() == NPCs.IMP_1531) {
            if (RandomFunction.roll(IMP_TELEPORT_CHANCE_ON_HIT)) {
                getRandomLocAndTeleport();
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getId() == NPCs.IMP_708 || this.getId() == NPCs.IMP_709 || this.getId() == NPCs.IMP_1531) {
            if (RandomFunction.roll(IMP_TELEPORT_CHANCE_ON_TICK)) {
                getRandomLocAndTeleport();
            }
        }
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public TrapNode getType() {
        return type;
    }

    /**
     * Gets trap.
     *
     * @return the trap
     */
    public Traps getTrap() {
        return trap;
    }

    private void getRandomLocAndTeleport() {
        Location teleportLocation = getPathableRandomLocalCoordinate(this, walkRadius, getProperties().getSpawnLocation(), 3);

        if (getLocation() != teleportLocation) {
            sendGraphics(1119, getLocation());
            ContentAPIKt.teleport(this, teleportLocation, TeleportManager.TeleportType.INSTANT);
        }
    }
}
