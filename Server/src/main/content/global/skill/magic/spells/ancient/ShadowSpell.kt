package content.global.skill.magic.spells.ancient

import content.global.skill.magic.spells.ancient.def.ShadowSpellDefinition
import core.api.playGlobalAudio
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Represents the Shadow spells.
 */
@Initializable
class ShadowSpell private constructor(
    private val definition: ShadowSpellDefinition
) : CombatSpell(
    definition.type,
    SpellBook.ANCIENT,
    definition.level,
    definition.xp,
    definition.castSound,
    definition.impactSound,
    definition.anim,
    definition.start,
    definition.projectile,
    definition.end,
    *definition.runes
) {
    constructor() : this(ShadowSpellDefinition.RUSH)

    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        ShadowSpellDefinition.values().forEach {
            SpellBook.ANCIENT.register(it.button, ShadowSpell(it))
        }
        return this
    }

    override fun visualize(entity: Entity, target: Node) {
        entity.graphics(graphics)
        projectile?.transform(entity, target as Entity, false, 58, 10)?.send()
        entity.animate(animation)
        audio?.let { playGlobalAudio(entity.location, it.id, 20) }
    }

    override fun fireEffect(entity: Entity, victim: Entity, state: BattleState) {
        if (state.estimatedHit > -1) {
            val level = victim.skills.getStaticLevel(Skills.ATTACK)
            victim.skills.updateLevel(
                Skills.ATTACK,
                -(level * 0.1).toInt(),
                (level - (level * 0.1)).toInt()
            )
        }
    }

    override fun getTargets(entity: Entity, target: Entity): Array<BattleState> {
        val singleTarget = definition.type == SpellType.RUSH || definition.type == SpellType.BLITZ
        if (singleTarget || !entity.properties.isMultiZone || !target.properties.isMultiZone) {
            return super.getTargets(entity, target)
        }
        val list = getMultihitTargets(entity, target, 9)
        return list.map { BattleState(entity, it) }.toTypedArray()
    }

    override fun getMaximumImpact(entity: Entity, victim: Entity, state: BattleState): Int =
        definition.type.getImpactAmount(entity, victim, 2)
}
