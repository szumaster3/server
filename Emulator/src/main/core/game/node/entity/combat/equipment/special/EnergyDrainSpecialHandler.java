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
import org.rs.consts.Animations;
import org.rs.consts.Items;
import org.rs.consts.Sounds;

import static core.api.ContentAPIKt.playGlobalAudio;

/**
 * The type Energy drain special handler.
 */
@Initializable
public final class EnergyDrainSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 50;

    private static final Animation ANIMATION = new Animation(Animations.THROW_WEAPON_1658, Priority.HIGH);

    private static final Graphics GRAPHICS = new Graphics(org.rs.consts.Graphics.ABYSSAL_WHIP_SPECIAL_341, 96);

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        CombatStyle.MELEE.getSwingHandler().register(Items.ABYSSAL_WHIP_4151, this);
        return this;
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        state.setStyle(CombatStyle.MELEE);
        int hit = 0;
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.25, 1.0)) {
            hit = RandomFunction.random(calculateHit(entity, victim, 1) + 1);
        }
        if (victim instanceof Player) {
            ((Player) victim).getSettings().updateRunEnergy(10);
            ((Player) entity).getSettings().updateRunEnergy(-10);
        }
        state.setEstimatedHit(hit);
        return 1;
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        playGlobalAudio(entity.getLocation(), Sounds.ENERGYDRAIN_2713);
        entity.animate(ANIMATION);
        victim.graphics(GRAPHICS);
    }
}
