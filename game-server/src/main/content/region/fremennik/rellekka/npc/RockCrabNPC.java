package content.region.fremennik.rellekka.npc;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.npc.agg.AggressiveBehavior;
import core.game.node.entity.npc.agg.AggressiveHandler;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * The type Rock crab npc.
 */
@Initializable
public final class RockCrabNPC extends AbstractNPC {

    private static final AggressiveBehavior AGGRO_BEHAVIOR = new AggressiveBehavior() {
        @Override
        public boolean ignoreCombatLevelDifference() {
            return true;
        }

        @Override
        public boolean canSelectTarget(Entity entity, Entity target) {
            return super.canSelectTarget(entity, target) &&
                entity.getLocation().withinDistance(target.getLocation(), 3);
        }
    };

    @Override
    public void onAttack(Entity target) {
        this.aggressor = true;
        this.target = target;
        if (getId() == getOriginalId()) {
            this.transform(getOriginalId() - 1);
        }
    }

    private boolean aggressor;

    private Entity target;

    /**
     * Instantiates a new Rock crab npc.
     *
     * @param id       the id
     * @param location the location
     */
    public RockCrabNPC(int id, Location location) {
        super(id, location, false);
        super.setAggressiveHandler(new AggressiveHandler(this, AGGRO_BEHAVIOR));
        this.setAggressive(true);
        this.setWalks(false);
    }

    /**
     * Instantiates a new Rock crab npc.
     */
    public RockCrabNPC() {
        super(-1, null);
    }

    @Override
    public void handleTickActions() {
        super.handleTickActions();
        if ((aggressor && !inCombat() && target.getLocation().getDistance(this.getLocation()) > 12) || isInvisible()) {
            reTransform();
            aggressor = false;
            target = null;
            getWalkingQueue().reset();
            setWalks(false);
        }
    }

    @Override
    public void sendImpact(BattleState state) {
        if (state.getEstimatedHit() >= 3 && RandomFunction.random(30) != 5) {
            state.setEstimatedHit(0);
        }
        if (state.getEstimatedHit() == 2 && RandomFunction.random(30) != 5) {
            state.setEstimatedHit(0);
        }
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new RockCrabNPC(id, location);
    }

    private int getTransformId() {
        if (getId() != super.getOriginalId()) {
            return getOriginalId();
        }
        return getId() - 1;
    }

    @Override
    public int[] getIds() {
        return new int[]{1266, 1268, 2453, 2890};
    }

}
