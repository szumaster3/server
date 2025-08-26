package content.global.skill.magic.spells.ancient

import core.api.applyPoison
import core.api.playGlobalAudio
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Animations

/**
 * Represents the Smoke spells.
 */
@Initializable
class SmokeSpell private constructor(
    private val definition: SmokeSpellDefinition
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
    constructor() : this(SmokeSpellDefinition.RUSH)

    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        SmokeSpellDefinition.values().forEach {
            SpellBook.ANCIENT.register(it.button, SmokeSpell(it))
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
            applyPoison(victim, entity, if (type.ordinal >= SpellType.BLITZ.ordinal) 4 else 2)
        }
    }

    override fun getTargets(entity: Entity, target: Entity): Array<BattleState> {
        if (animation?.id == Animations.CAST_SPELL_1978 || !entity.properties.isMultiZone || !target.properties.isMultiZone) {
            return super.getTargets(entity, target)
        }
        val list = getMultihitTargets(entity, target, 9)
        return list.map { BattleState(entity, it) }.toTypedArray()
    }

    override fun getMaximumImpact(entity: Entity, victim: Entity, state: BattleState): Int =
        definition.type.getImpactAmount(entity, victim, 1)
}
