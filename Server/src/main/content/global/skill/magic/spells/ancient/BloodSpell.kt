package content.global.skill.magic.spells.ancient

import content.global.skill.magic.spells.ancient.def.BloodSpellDefinition
import core.api.playGlobalAudio
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Animations

/**
 * Represents the Blood spells.
 */
@Initializable
class BloodSpell private constructor(
    private val definition: BloodSpellDefinition
) : CombatSpell(
    definition.type,
    SpellBook.ANCIENT,
    definition.level,
    definition.xp,
    definition.sound,
    definition.impactSound,
    definition.anim,
    definition.start,
    definition.projectile,
    definition.end,
    *definition.runes
) {
    constructor() : this(BloodSpellDefinition.RUSH)

    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        BloodSpellDefinition.values().forEach {
            SpellBook.ANCIENT.register(it.button, BloodSpell(it))
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
            val heal = state.estimatedHit / 4
            if (heal > 0) {
                entity.skills.heal(heal)
                if (entity is Player) {
                    entity.packetDispatch.sendMessage("You drain some of your opponent's health.")
                }
            }
        }
    }

    override fun getTargets(entity: Entity, target: Entity): Array<BattleState> {
        if (animation?.id == Animations.CAST_SPELL_1978 ||
            !entity.properties.isMultiZone ||
            !target.properties.isMultiZone
        ) {
            return super.getTargets(entity, target)
        }
        val list = getMultihitTargets(entity, target, 9)
        return list.map { BattleState(entity, it) }.toTypedArray()
    }

    override fun getMaximumImpact(entity: Entity, victim: Entity, state: BattleState): Int =
        definition.type.getImpactAmount(entity, victim, 3)
}
