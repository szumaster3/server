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
 * Represents the Water spell.
 */
@Initializable
class WaterSpell private constructor(
    private val data: WaterSpellDefinition
) : CombatSpell(
    data.type,
    SpellBook.MODERN,
    data.level,
    data.xp,
    data.sound,
    data.sound + 1,
    SpellProjectile.ANIMATION,
    data.startGraphics,
    data.projectile,
    data.endGraphics,
    *data.runes
) {
    constructor() : this(WaterSpellDefinition.STRIKE)

    override fun getMaximumImpact(entity: Entity, victim: Entity, state: BattleState): Int =
        getType().getImpactAmount(entity, victim, 2)

    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        WaterSpellDefinition.values().forEach {
            SpellBook.MODERN.register(it.buttonId, WaterSpell(it))
        }
        return this
    }
}
