package content.global.skill.magic.spells.modern

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations

/**
 * Represents the Curse spell.
 */
@Initializable
class CurseSpell(
    private val definition: CurseSpellDefinition
) : CombatSpell(
    definition.type,
    SpellBook.MODERN,
    definition.level,
    definition.xp,
    definition.castSound,
    definition.impactSound,
    if (definition.type.ordinal <= SpellType.CURSE.ordinal) LOW_ANIMATION else HIGH_ANIMATION,
    definition.start,
    definition.projectile,
    definition.end,
    *definition.runes
) {

    override fun getMaximumImpact(entity: Entity, victim: Entity, state: BattleState): Int = 1

    override fun fireEffect(entity: Entity, victim: Entity, state: BattleState) {
        if (state.estimatedHit == -1) return
        state.estimatedHit = -2
        when (definition.type) {
            SpellType.CONFUSE -> victim.getSkills().drainLevel(Skills.ATTACK, 0.05, 0.05)
            SpellType.WEAKEN -> victim.getSkills().drainLevel(Skills.STRENGTH, 0.05, 0.05)
            SpellType.CURSE -> victim.getSkills().drainLevel(Skills.DEFENCE, 0.05, 0.05)
            SpellType.VULNERABILITY -> victim.getSkills().drainLevel(Skills.DEFENCE, 0.10, 0.10)
            SpellType.ENFEEBLE -> victim.getSkills().drainLevel(Skills.STRENGTH, 0.10, 0.10)
            SpellType.STUN -> victim.getSkills().drainLevel(Skills.ATTACK, 0.10, 0.10)
            else -> {}
        }
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

    companion object {
        private val LOW_ANIMATION = Animation(716, Priority.HIGH)
        private val HIGH_ANIMATION = Animation(Animations.CAST_SPELL_B_729, Priority.HIGH)
    }
}