package content.global.skill.slayer.npc;

import content.global.skill.slayer.SlayerEquipmentFlags;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.*;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import org.rs.consts.NPCs;

/**
 * The type Cave horror npc.
 */
@Initializable
public class CaveHorrorNPC extends AbstractNPC {

    /**
     * Instantiates a new Cave horror npc.
     */
    public CaveHorrorNPC() {
        super(0, null);
    }

    /**
     * Instantiates a new Cave horror npc.
     *
     * @param id       the id
     * @param location the location
     */
    public CaveHorrorNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new CaveHorrorNPC(id, location);
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return COMBAT_HANDLER;
    }

    @Override
    public int[] getIds() {
        return new int[]{
            NPCs.CAVE_HORROR_4354,
            NPCs.CAVE_HORROR_4355,
            NPCs.CAVE_HORROR_4353,
            NPCs.CAVE_HORROR_4356,
            NPCs.CAVE_HORROR_4357
        };
    }

    private static final MeleeSwingHandler COMBAT_HANDLER = new MeleeSwingHandler() {
        @Override
        public void impact(Entity entity, Entity victim, BattleState state) {
            if (victim instanceof Player) {
                Player player = (Player) victim;
                if (!hasWitchwood(player)) {
                    if (RandomFunction.random(10) < 5) {
                        state.setEstimatedHit(player.getSkills().getLifepoints() / 10);
                    }
                }
            }
            super.impact(entity, victim, state);
        }

        @Override
        public InteractionType isAttackable(Entity entity, Entity victim) {
            return CombatStyle.MELEE.getSwingHandler().isAttackable(entity, victim);
        }
    };

    /**
     * Has witchwood boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public static boolean hasWitchwood(Player player) {
        return SlayerEquipmentFlags.hasWitchwoodIcon(player);
    }
}
