package content.global.skill.magic.spells.modern

import core.api.drainStatLevel
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Represents the Curse spell.
 */
@Initializable
class CurseSpell private constructor(private val definition: CurseSpellDefinition) :
    CombatSpell(
        definition.type,
        SpellBook.MODERN,
        definition.level,
        definition.xp,
        definition.castSound,
        definition.impactSound,
        definition.animation,
        definition.start,
        definition.projectile,
        definition.end,
        *definition.runes
    ) {
    constructor() : this(CurseSpellDefinition.CURSE)

    override fun getMaximumImpact(entity: Entity, victim: Entity, state: BattleState): Int = 1

    override fun fireEffect(entity: Entity, victim: Entity, state: BattleState) {
        if (state.estimatedHit == -1) return
        state.estimatedHit = -2

        val effect =
            when (definition.type) {
                SpellType.CONFUSE -> Skills.ATTACK to 0.05
                SpellType.WEAKEN -> Skills.STRENGTH to 0.05
                SpellType.CURSE -> Skills.DEFENCE to 0.05
                SpellType.VULNERABILITY -> Skills.DEFENCE to 0.10
                SpellType.ENFEEBLE -> Skills.STRENGTH to 0.10
                SpellType.STUN -> Skills.ATTACK to 0.10
                else -> null
            }

        effect?.let { (skill, percent) -> drainStatLevel(victim, skill, percent, percent) }
    }

    override fun addExperience(entity: Entity, hit: Int) {
        entity.getSkills().addExperience(Skills.MAGIC, experience)
    }

    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        CurseSpellDefinition.values().forEach {
            SpellBook.MODERN.register(it.buttonId, CurseSpell(it))
        }
        return this
    }
}
