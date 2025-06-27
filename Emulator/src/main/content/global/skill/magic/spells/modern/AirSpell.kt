package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Represents the Air spell.
 */
@Initializable
class AirSpell(
    private val definition: AirSpellDefinition
) : CombatSpell(
    definition.type,
    SpellBook.MODERN,
    definition.level,
    definition.xp,
    definition.sound,
    definition.sound + 1,
    SpellProjectile.ANIMATION,
    definition.start,
    definition.projectile,
    definition.end,
    *definition.runes
) {

    override fun getMaximumImpact(entity: Entity, victim: Entity, state: BattleState): Int =
        definition.type.getImpactAmount(entity, victim, 1)

    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        AirSpellDefinition.values().forEach {
                SpellBook.MODERN.register(it.button, AirSpell(it))
        }
        return this
    }
}
