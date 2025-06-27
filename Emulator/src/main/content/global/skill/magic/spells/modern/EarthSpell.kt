package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Represents the Earth spell.
 */
@Initializable
class EarthSpell(
    private val definition: EarthSpellDefinition
) : CombatSpell(
    definition.type,
    SpellBook.MODERN,
    definition.level,
    definition.xp,
    definition.castSound,
    definition.castSound + 1,
    SpellProjectile.ANIMATION,
    definition.start,
    definition.projectile,
    definition.end,
    *definition.runes
) {

    override fun getMaximumImpact(entity: Entity, victim: Entity, state: BattleState): Int =
        definition.type.getImpactAmount(entity, victim, 3)

    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        EarthSpellDefinition.values().forEach {
            SpellBook.MODERN.register(it.buttonId, EarthSpell(it))
        }
        return this
    }
}