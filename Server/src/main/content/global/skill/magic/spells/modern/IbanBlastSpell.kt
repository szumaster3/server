package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.ModernSpells
import content.global.skill.magic.spells.SpellProjectile
import core.game.container.impl.EquipmentContainer
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Sounds

/**
 * Represents the Iban blast spell.
 */
@Initializable
class IbanBlastSpell : CombatSpell(
    SpellType.IBANS_BLAST,
    SpellBook.MODERN,
    50,
    60.5,
    Sounds.FIREWAVE_CAST_AND_FIRE_162,
    Sounds.FIREWAVE_HIT_163,
    Animation(Animations.IBAN_STAFF_708, Priority.HIGH),
    Graphics(shared.consts.Graphics.IBAN_BLAST_CAST_87, 92),
    SpellProjectile.create(shared.consts.Graphics.IBAN_BLAST_PROJECTILE_88),
    Graphics(shared.consts.Graphics.IBAN_BLAST_IMPACT_89, 92),
    Runes.FIRE_RUNE.getItem(5),
    Runes.DEATH_RUNE.getItem(1),
) {

    override fun cast(entity: Entity, target: Node): Boolean {
        if ((entity as Player).equipment.getNew(EquipmentContainer.SLOT_WEAPON).id != Items.IBANS_STAFF_1409) {
            entity.packetDispatch.sendMessage("You need to wear Iban's staff to cast this spell.")
            return false
        }
        return super.cast(entity, target)
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: SpellType?): Plugin<SpellType?> {
        SpellBook.MODERN.register(ModernSpells.IBAN_BLAST, this)
        return this
    }

    override fun getMaximumImpact(
        entity: Entity,
        victim: Entity,
        state: BattleState,
    ): Int = type.getImpactAmount(entity, victim, 0)
}
