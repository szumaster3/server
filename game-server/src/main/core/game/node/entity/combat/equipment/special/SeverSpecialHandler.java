package core.game.node.entity.combat.equipment.special;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.MeleeSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.prayer.PrayerType;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import org.rs.consts.Items;
import org.rs.consts.Sounds;

import static core.api.ContentAPIKt.playGlobalAudio;

/**
 * Handles the Sever special attack.
 *
 * @author Emperor
 */
@Initializable
public final class SeverSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 55;
    private static final Animation ANIMATION = new Animation(1872, Priority.HIGH);
    private static final Graphics GRAPHICS = new Graphics(org.rs.consts.Graphics.DRAGON_SCIMITAR_SPECIAL_347, 96);

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        if (CombatStyle.MELEE.getSwingHandler().register(Items.DRAGON_SCIMITAR_4587, this) && CombatStyle.MELEE.getSwingHandler().register(Items.DRAGON_SCIMITAR_13477, this))
            ;
        return this;
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) return -1;
        state.setStyle(CombatStyle.MELEE);
        int hit = 0;
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.25, 1.0)) {
            hit = RandomFunction.random(calculateHit(entity, victim, 1.0) + 1);
            if (victim instanceof Player) {
                Player p = (Player) victim;
                if (p.getPrayer().get(PrayerType.PROTECT_FROM_MAGIC)) {
                    p.getPrayer().toggle(PrayerType.PROTECT_FROM_MAGIC);
                }
                if (p.getPrayer().get(PrayerType.PROTECT_FROM_MELEE)) {
                    p.getPrayer().toggle(PrayerType.PROTECT_FROM_MELEE);
                }
                if (p.getPrayer().get(PrayerType.PROTECT_FROM_MISSILES)) {
                    p.getPrayer().toggle(PrayerType.PROTECT_FROM_MISSILES);
                }
                if (p.getPrayer().get(PrayerType.PROTECT_FROM_SUMMONING)) {
                    p.getPrayer().toggle(PrayerType.PROTECT_FROM_SUMMONING);
                }
            }
        }
        state.setEstimatedHit(hit);
        return 1;
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        playGlobalAudio(entity.getLocation(), Sounds.SEVER_2540);
        entity.visualize(ANIMATION, GRAPHICS);
    }
}
