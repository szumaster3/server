package content.global.skill.magic.spells.modern;

import core.game.container.impl.EquipmentContainer;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.spell.CombatSpell;
import core.game.node.entity.combat.spell.Runes;
import core.game.node.entity.combat.spell.SpellType;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.entity.skill.Skills;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Items;
import org.rs.consts.Sounds;

/**
 * The type Magic dart spell.
 */
@Initializable
public class MagicDartSpell extends CombatSpell {

    /**
     * Instantiates a new Magic dart spell.
     */
    public MagicDartSpell() {
        super(SpellType.MAGIC_DART,
                SpellBook.MODERN,
                50,
                30.0,
                Sounds.WINDBOLT_CAST_AND_FIRE_218,
                Sounds.WINDBOLT_HIT_219,
                new Animation(1576, Priority.HIGH),
                null,
                Projectile.create((Entity) null, null, org.rs.consts.Graphics.SLAYER_DART_PROJECTILE_SLIGHTLY_BETTER_LOOKING_330, 40, 36, 52, 75, 15, 11),
                new Graphics(org.rs.consts.Graphics.SLAYER_DART_CONTACT_SAME_AS_ABOVE_SLIGHT_COLOR_CHANGE_331, 96),
                Runes.DEATH_RUNE.getItem(1),
                Runes.MIND_RUNE.getItem(4));
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        if (entity.getSkills().getLevel(Skills.SLAYER) < 55) {
            ((Player) entity).getPacketDispatch().sendMessage("You need a Slayer level of 55 to cast this spell.");
            return false;
        }
        if (((Player) entity).getEquipment().getNew(EquipmentContainer.SLOT_WEAPON).getId() != Items.SLAYERS_STAFF_4170) {
            ((Player) entity).getPacketDispatch().sendMessage("You need to wear a Slayer's staff to cast this spell.");
            return false;
        }
        return super.cast(entity, target);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.MODERN.register(31, this);
        return this;
    }

    @Override
    public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
        return type.getImpactAmount(entity, victim, 0);
    }
}
