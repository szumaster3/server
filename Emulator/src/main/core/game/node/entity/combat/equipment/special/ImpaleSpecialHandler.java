package core.game.node.entity.combat.equipment.special;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.MeleeSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.Rights;
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
 * The type Impale special handler.
 */
@Initializable
public final class ImpaleSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 25;

    private static final Animation ANIMATION = new Animation(Animations.BIG_ATTACK_923, Priority.HIGH);

    private static final Graphics GRAPHICS = new Graphics(org.rs.consts.Graphics.RUNE_CLAW_SPECIAL_274, 96);

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        CombatStyle.MELEE.getSwingHandler().register(Items.RUNE_CLAWS_3101, this);
        return this;
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        Player player = (Player) entity;
        if (!player.getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        state.setStyle(CombatStyle.MELEE);
        int hit = 0;
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.1, 0.98)) {
            hit = RandomFunction.random(calculateHit(entity, victim, 1.1) + 1);
        }
        state.setEstimatedHit(hit);
        return 1;
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        playGlobalAudio(entity.getLocation(), Sounds.IMPALE_2534);
        if (((Player) entity).getDetails().getRights() == Rights.ADMINISTRATOR) {
            entity.animate(Animation.create(2068));
            return;
        }
        entity.visualize(ANIMATION, GRAPHICS);
    }
}
