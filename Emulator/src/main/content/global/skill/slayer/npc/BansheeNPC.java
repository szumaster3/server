package content.global.skill.slayer.npc;

import content.global.skill.slayer.SlayerEquipmentFlags;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.*;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import org.rs.consts.Animations;
import org.rs.consts.NPCs;

/**
 * The type Banshee npc.
 */
@Initializable
public class BansheeNPC extends AbstractNPC {

    /**
     * Instantiates a new Banshee npc.
     */
    public BansheeNPC() {
        super(0, null);
    }

    /**
     * Instantiates a new Banshee npc.
     *
     * @param id       the id
     * @param location the location
     */
    public BansheeNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new BansheeNPC(id, location);
    }

    @Override
    public void onImpact(Entity entity, BattleState state) {
        super.onImpact(entity, state);
        if (state.getAttacker() instanceof Player) {
            Player player = (Player) state.getAttacker();
            if (!hasEarMuffs(player)) {
                state.neutralizeHits();
            }
        }
        if (state.getEstimatedHit() > 0 || state.getSecondaryHit() > 0) {
            getSkills().heal(1);
        }
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return COMBAT_HANDLER;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.BANSHEE_1612 };
    }

    private static final int[] SKILLS = new int[] {
        Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.RANGE,
        Skills.MAGIC, Skills.PRAYER, Skills.AGILITY
    };

    private static final MeleeSwingHandler COMBAT_HANDLER = new MeleeSwingHandler() {
        @Override
        public void impact(Entity entity, Entity victim, BattleState state) {
            if (victim instanceof Player) {
                Player player = (Player) victim;
                if (!hasEarMuffs(player)) {
                    if (RandomFunction.random(10) < 4 && player.getProperties().getCombatPulse().getNextAttack() <= GameWorld.getTicks()) {
                        player.getWalkingQueue().reset();
                        player.getLocks().lockMovement(3);
                        player.getProperties().getCombatPulse().setNextAttack(3);
                        player.animate(new Animation(Animations.BEND_EARS_1572, Priority.HIGH));
                    }
                    for (int skill : SKILLS) {
                        int drain = (int) (player.getSkills().getStaticLevel(skill) * 0.5);
                        player.getSkills().updateLevel(skill, -drain, 0);
                    }
                    state.setEstimatedHit(8);
                }
            }
            super.impact(entity, victim, state);
        }

        @Override
        public InteractionType isAttackable(Entity entity, Entity victim) {
            return CombatStyle.MAGIC.getSwingHandler().isAttackable(entity, victim);
        }
    };

    /**
     * Has ear muffs boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public static boolean hasEarMuffs(Player player) {
        return SlayerEquipmentFlags.hasEarmuffs(player);
    }
}
