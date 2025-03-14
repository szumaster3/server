package core.game.node.entity.combat.equipment.special;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.RangeSwingHandler;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import org.rs.consts.Items;
import org.rs.consts.Sounds;

import static core.api.ContentAPIKt.playGlobalAudio;

/**
 * The type Powershot special handler.
 */
@Initializable
public final class PowershotSpecialHandler extends RangeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 35;

    private static final Graphics GRAPHICS = new Graphics(org.rs.consts.Graphics.MAGIC_LONGBOW_SPECIAL_250, 96);

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        CombatStyle.RANGE.getSwingHandler().register(Items.MAGIC_LONGBOW_859, this);
        CombatStyle.RANGE.getSwingHandler().register(Items.MAGIC_COMP_BOW_10284, this);
        return this;
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        Player p = (Player) entity;
        configureRangeData(p, state);
        if (state.getWeapon() == null || !Companion.hasAmmo(entity, state)) {
            entity.getProperties().getCombatPulse().stop();
            p.getSettings().toggleSpecialBar();
            return -1;
        }
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        state.setStyle(CombatStyle.RANGE);
        int hit = RandomFunction.random(calculateHit(entity, victim, 1.0) + 1);
        state.setEstimatedHit(hit);
        Companion.useAmmo(entity, state, victim.getLocation());
        return 1 + (int) Math.ceil(entity.getLocation().getDistance(victim.getLocation()) * 0.3);
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        playGlobalAudio(entity.getLocation(), Sounds.POWERSHOT_2536);
        entity.visualize(state.getRangeWeapon().getAnimation(), GRAPHICS);
        int speed = (int) (46 + (entity.getLocation().getDistance(victim.getLocation()) * 5));
        Projectile.create(entity, victim, 249, 40, 36, 45, speed, 5, 11).send();
    }
}
