package core.game.node.entity.combat.equipment.special;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.MeleeSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Items;
import org.rs.consts.Sounds;

import static core.api.ContentAPIKt.playAudio;

/**
 * Handles the Rampage special attack.
 *
 * @author Emperor
 */
@Initializable
public final class RampageSpecialHandler extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 100;

    private static final Animation ANIMATION = new Animation(1056, Priority.HIGH);

    private static final Graphics GRAPHICS = new Graphics(246);

    @Override
    public Object fireEvent(String identifier, Object... args) {
        switch (identifier) {
            case "instant_spec":
            case "ncspec":
                return true;
        }
        return null;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        CombatStyle.MELEE.getSwingHandler().register(Items.DRAGON_BATTLEAXE_1377, this);
        return this;
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        Player p = (Player) entity;
        if (!p.getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        p.sendChat("Raarrrrrgggggghhhhhhh!");
        playAudio(entity.asPlayer(), Sounds.RAMPAGE_2538);
        p.visualize(ANIMATION, GRAPHICS);
        @SuppressWarnings("unused")
        int boost = 0;
        for (int i = 0; i < 7; i++) {
            if (i == Skills.ATTACK || i == Skills.DEFENCE || i == Skills.RANGE || i == Skills.MAGIC) {
                int drain = (int) (p.getSkills().getLevel(i) * 0.1);
                boost += drain;
                p.getSkills().updateLevel(i, -drain, 0);
            }
        }
        boost = 10 + (boost / 4);
        p.getSkills().updateLevel(Skills.STRENGTH, boost, Math.max(p.getSkills().getStaticLevel(Skills.STRENGTH) + boost, p.getSkills().getLevel(Skills.STRENGTH)));
        return -1;
    }

}
