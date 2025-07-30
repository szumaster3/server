package core.game.node.entity.combat.equipment.special;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.MeleeSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import org.rs.consts.Items;
import org.rs.consts.Sounds;

import static core.api.ContentAPIKt.playGlobalAudio;

/**
 * Handles the healing blade special attack.
 *
 * @author Emperor
 */
@Initializable
public final class HealingBladeSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 50;

    private static final Animation ANIMATION = new Animation(7071, Priority.HIGH);

    private static final Graphics GRAPHICS = new Graphics(org.rs.consts.Graphics.SARADOMIN_GODSWORD_SPECIAL_1220);

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        CombatStyle.MELEE.getSwingHandler().register(Items.SARADOMIN_GODSWORD_11698, this);
        CombatStyle.MELEE.getSwingHandler().register(Items.SARADOMIN_GODSWORD_13452, this);
        return this;
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        state.setStyle(CombatStyle.MELEE);
        int hit = 0;
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 2.0, 1.0)) {
            hit = RandomFunction.random(calculateHit(entity, victim, 1.1) + 1);
            int healthRestore = hit / 2;
            double prayerRestore = hit * 0.25;
            if (healthRestore < 10) {
                healthRestore = 10;
            }
            if (prayerRestore < 5) {
                prayerRestore = 5;
            }
            entity.getSkills().heal(healthRestore);
            entity.getSkills().incrementPrayerPoints(prayerRestore);
        }
        state.setEstimatedHit(hit);
        return 1;
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        playGlobalAudio(entity.getLocation(), Sounds.GODWARS_SARADOMIN_SPECIAL_3857);
        entity.visualize(ANIMATION, GRAPHICS);
    }

}
