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
import org.rs.consts.Animations

/**
 * Represents the Fire spell.
 */
@Initializable
class FireSpell(
    private val definition: FireSpellDefinition
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
        definition.type.getImpactAmount(entity, victim, 4)

    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        FireSpellDefinition.values().forEach {
            SpellBook.MODERN.register(it.buttonId, FireSpell(it))
        }
        return this
    }

}
