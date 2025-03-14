package content.global.skill.slayer.npc;

import content.global.skill.slayer.Tasks;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.npc.AbstractNPC;
import core.game.world.map.Location;
import core.plugin.Initializable;

/**
 * The type Desert lizard npc.
 */
@Initializable
public class DesertLizardNPC extends AbstractNPC {

    /**
     * Instantiates a new Desert lizard npc.
     */
    public DesertLizardNPC() {
        super(0, null);
    }

    /**
     * Instantiates a new Desert lizard npc.
     *
     * @param id       the id
     * @param location the location
     */
    public DesertLizardNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new DesertLizardNPC(id, location);
    }

    @Override
    public void checkImpact(BattleState state) {
        super.checkImpact(state);
        int lifepoints = getSkills().getLifepoints();

        if (state.getEstimatedHit() > -1) {
            lifepoints -= state.getEstimatedHit();
            if (lifepoints < 1) {
                state.setEstimatedHit(lifepoints - 1);
            }
            if (state.getEstimatedHit() < 0) {
                state.setEstimatedHit(0);
                getSkills().setLifepoints(2);
            }
        }

        if (state.getSecondaryHit() > -1) {
            lifepoints -= state.getSecondaryHit();
            if (lifepoints < 1) {
                state.setSecondaryHit(lifepoints - 1);
            }
            if (state.getSecondaryHit() < 0) {
                state.setSecondaryHit(0);
            }
        }
    }

    @Override
    public int[] getIds() {
        return Tasks.DESERT_LIZARDS.getNpcs();
    }
}
