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
import shared.consts.NPCs;

import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.getPathableRandomLocalCoordinate;
import static core.api.ContentAPIKt.sendGraphics;

/**
 * Represents a Hunter NPC used in the Hunter skill.
 * Handles imp behaviour including periodic teleporting.
 */
public final class HunterNPC extends AbstractNPC {

    /**
     * Percentage chance for an imp to teleport when the check occurs.
     */
    private static final int IMP_TELEPORT_CHANCE = 25;

    /**
     * Minimum interval between teleport checks (seconds).
     */
    private static final int IMP_MIN_INTERVAL = 30;

    /**
     * Maximum interval between teleport checks (seconds).
     */
    private static final int IMP_MAX_INTERVAL = 120;

    /**
     * Attribute key storing the next teleport check tick.
     */
    private static final String NEXT_IMP_TELEPORT = "imp:next_teleport";

    /**
     * The trap type associated with this NPC.
     */
    private final Traps trap;

    /**
     * The node/type of this NPC for trap catching logic.
     */
    private final TrapNode type;

    public HunterNPC() {
        this(0, null, null, null);
        setWalks(true);
    }

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
        TrapWrapper wrapper = trap.getByHook(getLocation());
        if (wrapper != null) {
            wrapper.getType().catchNpc(wrapper, this);
        }
    }

    @Override
    protected Location getMovementDestination() {
        if (trap.getHooks().isEmpty() || RandomFunction.random(170) > 5) {
            return super.getMovementDestination();
        }

        TrapHook hook = trap.getHooks().get(RandomFunction.random(trap.getHooks().size()));
        if (hook == null || !type.canCatch(hook.getWrapper(), this)) {
            return super.getMovementDestination();
        }

        Location destination = hook.getChanceLocation();
        return destination != null && destination.getDistance(getLocation()) <= 24
                ? destination
                : super.getMovementDestination();
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
        return ids.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public void tick() {
        super.tick();

        if (!isImp()) {
            return;
        }

        long now = GameWorld.getTicks();
        long nextCheck = getAttribute(NEXT_IMP_TELEPORT, 0L);

        if (now < nextCheck) {
            return;
        }

        if (RandomFunction.random(100) < IMP_TELEPORT_CHANCE) {
            teleportImp();
        }

        int delaySeconds = RandomFunction.random(IMP_MIN_INTERVAL, IMP_MAX_INTERVAL);
        long delayTicks = delaySeconds * 1000L / 600L;

        setAttribute(NEXT_IMP_TELEPORT, now + delayTicks);
    }

    private void teleportImp() {
        Location teleportLocation = getPathableRandomLocalCoordinate(
                this,
                walkRadius,
                getProperties().getSpawnLocation(),
                3
        );

        if (teleportLocation == null || teleportLocation.equals(getLocation())) {
            return;
        }

        sendGraphics(1119, getLocation());

        ContentAPIKt.teleport(this, teleportLocation, TeleportManager.TeleportType.INSTANT);

        getProperties().getCombatPulse().stop();
        removeAttribute("combat-time");
        getWalkingQueue().reset();
        face(null);

        setAttribute("no_combat", GameWorld.getTicks() + 5);
    }

    private boolean isImp() {
        int id = getId();
        return id == NPCs.IMP_708
                || id == NPCs.IMP_709
                || id == NPCs.IMP_1531;
    }

    public TrapNode getType() {
        return type;
    }

    public Traps getTrap() {
        return trap;
    }
}
