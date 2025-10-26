package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.SpellProjectile
import content.global.skill.magic.spells.modern.def.FireSpellDefinition
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Represents the Fire spell.
 */
@Initializable
class FireSpell private constructor(
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
    constructor() : this(FireSpellDefinition.STRIKE)

    override fun getMaximumImpact(entity: Entity, victim: Entity, state: BattleState): Int =
        definition.type.getImpactAmount(entity, victim, 4)

    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        FireSpellDefinition.values().forEach {
            SpellBook.MODERN.register(it.buttonId, FireSpell(it))
        }
        return this
    }

}
