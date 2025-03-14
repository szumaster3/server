package content.global.skill.slayer.npc;

import content.global.skill.slayer.SlayerEquipmentFlags;
import content.global.skill.slayer.Tasks;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.MagicSwingHandler;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.world.map.Location;
import core.plugin.Initializable;

/**
 * The type Aberrant spectre npc.
 */
@Initializable
public class AberrantSpectreNPC extends AbstractNPC {

    /**
     * Instantiates a new Aberrant spectre npc.
     */
    public AberrantSpectreNPC() {
        super(0, null);
    }

    private static final int[] SKILLS = new int[] {
            Skills.ATTACK,
            Skills.STRENGTH,
            Skills.DEFENCE,
            Skills.RANGE,
            Skills.MAGIC,
            Skills.PRAYER,
            Skills.AGILITY
    };

    private static final MagicSwingHandler COMBAT_HANDLER = new MagicSwingHandler() {
        @Override
        public void impact(Entity entity, Entity victim, BattleState state) {
            if (victim instanceof Player) {
                Player player = (Player) victim;
                if (!SlayerEquipmentFlags.hasNosePeg(player)) {
                    for (int skill : SKILLS) {
                        int drain = (int) (player.getSkills().getStaticLevel(skill) * 0.5);
                        player.getSkills().updateLevel(skill, -drain, 0);
                    }
                }
            }
            super.impact(entity, victim, state);
        }
    };

    /**
     * Instantiates a new Aberrant spectre npc.
     *
     * @param id       the id
     * @param location the location
     */
    public AberrantSpectreNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new AberrantSpectreNPC(id, location);
    }

    @Override
    public void checkImpact(BattleState state) {
        super.checkImpact(state);
        if (state.getAttacker() instanceof Player) {
            Player player = (Player) state.getAttacker();
            if (!SlayerEquipmentFlags.hasNosePeg(player)) {
                state.neutralizeHits();
            }
        }
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return COMBAT_HANDLER;
    }

    @Override
    public int[] getIds() {
        return Tasks.ABERRANT_SPECTRES.getNpcs();
    }
}
