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
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs.consts.Animations;
import org.rs.consts.Sounds;

/**
 * The type Iban blast spell.
 */
@Initializable
public final class IbanBlastSpell extends CombatSpell {

    /**
     * Instantiates a new Iban blast spell.
     */
    public IbanBlastSpell() {
        super(SpellType.IBANS_BLAST,
                SpellBook.MODERN,
                50,
                60.5,
                Sounds.FIREWAVE_CAST_AND_FIRE_162,
                Sounds.FIREWAVE_HIT_163,
                new Animation(Animations.IBAN_STAFF_708, Priority.HIGH),
                new Graphics(org.rs.consts.Graphics.IBAN_BLAST_CAST_87, 96),
                Projectile.create((Entity) null, null, org.rs.consts.Graphics.IBAN_BLAST_PROJECTILE_88, 40, 36, 52, 75, 15, 11),
                new Graphics(org.rs.consts.Graphics.IBAN_BLAST_IMPACT_89, 96),
                Runes.FIRE_RUNE.getItem(5),
                Runes.DEATH_RUNE.getItem(1));
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        if (((Player) entity).getEquipment().getNew(EquipmentContainer.SLOT_WEAPON).getId() != 1409) {
            ((Player) entity).getPacketDispatch().sendMessage("You need to wear Iban's staff to cast this spell.");
            return false;
        }
        return super.cast(entity, target);
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.MODERN.register(29, this);
        return this;
    }

    @Override
    public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
        return type.getImpactAmount(entity, victim, 0);
    }
}
