package content.region.misthalin.varrock.npc;

import core.game.interaction.DestinationFlag;
import core.game.interaction.MovementPulse;
import core.game.node.entity.impl.PulseType;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.path.Pathfinder;
import core.game.world.map.zone.ZoneBorders;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import shared.consts.NPCs;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a stray dog NPC in Varrock.
 */
@Initializable
public class StrayDogNPC extends AbstractNPC {

    /**
     * The stray dog npc ids.
     */
    private static final int[] ID = {NPCs.STRAY_DOG_5918, NPCs.STRAY_DOG_5917};

    /**
     * The player currently being followed by the dog.
     */
    private Player target;

    /**
     * The delay between movements.
     */
    private long delay;

    /**
     * The primary bank zone that the dog avoids.
     */
    private static final ZoneBorders BANK = new ZoneBorders(3179, 3432, 3194, 3446);

    /**
     * Secondary bank zone that the dog avoids.
     */
    private static final ZoneBorders BANK_2 = new ZoneBorders(3250, 3416, 3257, 3423);

    /**
     * List of nearby players within range of the dog.
     */
    private List<Player> players = new ArrayList<>(20);

    /**
     * Instantiates a new Stray dog npc.
     */
    public StrayDogNPC() {
        super(0, null, true);
    }

    private StrayDogNPC(int id, Location location) {
        super(id, location, true);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new StrayDogNPC(id, location);
    }

    @Override
    public void tick() {
        super.tick();
        if (delay < System.currentTimeMillis() && RandomFunction.random(1, 16) == 2) {
            getPulseManager().clear();
            target = null;
            players = RegionManager.getLocalPlayers(this, 7);
            if (players.size() != 0) {
                target = players.get(RandomFunction.random(players.size()));
                getPulseManager().run(getFollowPulse(target), PulseType.STANDARD);
                delay = System.currentTimeMillis() + 150000;
            }
        }
        if (target != null && target.getZoneMonitor().isInZone("bank")) {
            Pathfinder.find(this, getProperties().getSpawnLocation()).walk(this);
            getPulseManager().clear();
            target = null;
            delay = System.currentTimeMillis() + 150000;
        }
    }

    @Override
    public Location getMovementDestination() {
        return super.getMovementDestination();
    }

    @Override
    public boolean canMove(Location l) {
        return !BANK.insideBorder(l) && !BANK_2.insideBorder(l);
    }

    @Override
    public int[] getIds() {
        return ID;
    }

    /**
     * Gets follow pulse.
     *
     * @param target the target.
     * @return the follow pulse.
     */
    public MovementPulse getFollowPulse(final Player target) {
        return new MovementPulse(this, target, DestinationFlag.FOLLOW_ENTITY) {
            @Override
            public boolean pulse() {
                return false;
            }
        };
    }

    @Override
    public int getWalkRadius() {
        return 17;
    }
}
