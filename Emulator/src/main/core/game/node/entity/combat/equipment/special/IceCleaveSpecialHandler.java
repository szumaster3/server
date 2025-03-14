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

import static core.api.ContentAPIKt.*;

/**
 * The type Ice cleave special handler.
 */
@Initializable
public final class IceCleaveSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 60;

    private static final Animation ANIMATION = new Animation(7070, Priority.HIGH);

    private static final Graphics GRAPHICS = new Graphics(org.rs.consts.Graphics.ZAMORAK_GODSWORD_SPECIAL_ATTACK_1221);

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        CombatStyle.MELEE.getSwingHandler().register(Items.ZAMORAK_GODSWORD_11700, this);
        CombatStyle.MELEE.getSwingHandler().register(Items.ZAMORAK_GODSWORD_13453, this);
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
        }
        state.setEstimatedHit(hit);
        return 1;
    }

    @Override
    public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
        super.adjustBattleState(entity, victim, state);
        if (state.getEstimatedHit() > 0) {
            registerTimer(victim, spawnTimer("frozen", 33, true));
            victim.graphics(Graphics.create(369));
        }
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        playGlobalAudio(entity.getLocation(), Sounds.GODWARS_GODSWORD_SLASH_3846);
        entity.visualize(ANIMATION, GRAPHICS);
    }
}
