package content.global.skill.slayer.items;

import content.global.skill.slayer.SlayerEquipmentFlags;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.MeleeSwingHandler;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;

/**
 * The type Mirror shield handler.
 */
public final class MirrorShieldHandler extends MeleeSwingHandler {
    /**
     * The constant SINGLETON.
     */
    public static final MirrorShieldHandler SINGLETON = new MirrorShieldHandler();
    private static final int[] SKILLS = new int[]{Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.RANGE};

    @Override
    public void impact(Entity entity, Entity victim, BattleState state) {
        if (victim instanceof Player) {
            final Player player = (Player) victim;
            if (!hasShield(player)) {
                state.setEstimatedHit(11);
                for (int skill : SKILLS) {
                    int drain = (int) (player.getSkills().getStaticLevel(skill) * 0.25);
                    player.getSkills().updateLevel(skill, -drain, player.getSkills().getStaticLevel(skill) - drain);
                }
            }
        }
        super.impact(entity, victim, state);
    }

    /**
     * Check impact.
     *
     * @param state the state
     */
    public void checkImpact(final BattleState state) {
        if (state.getAttacker() instanceof Player) {
            final Player player = (Player) state.getAttacker();
            if (!hasShield(player)) {
                state.setEstimatedHit(0);
                if (state.getSecondaryHit() > 0) {
                    state.setSecondaryHit(0);
                }
            }
        }
    }

    private static boolean hasShield(final Player player) {
        return SlayerEquipmentFlags.hasMirrorShield(player);
    }

}
