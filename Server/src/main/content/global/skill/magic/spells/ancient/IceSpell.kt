package content.global.skill.magic.spells.ancient

import core.api.hasTimerActive
import core.api.playGlobalAudio
import core.api.registerTimer
import core.api.spawnTimer
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Animations
import shared.consts.Sounds

/**
 * Represents the Ice spells.
 */
@Initializable
class IceSpell private constructor(
    private val definition: IceSpellDefinition
) : CombatSpell(
    definition.type,
    SpellBook.ANCIENT,
    definition.level,
    definition.xp,
    Sounds.ICE_CAST_171,
    definition.impactSound,
    definition.anim,
    definition.start,
    definition.projectile,
    definition.end,
    *definition.runes
) {
    constructor() : this(IceSpellDefinition.RUSH)

    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        IceSpellDefinition.values().forEach {
            SpellBook.ANCIENT.register(it.button, IceSpell(it))
        }
        return this
    }

    override fun visualize(entity: Entity, target: Node) {
        entity.graphics(graphics)
        projectile?.transform(entity, target as Entity, false, 58, 10)?.send()
        entity.animate(animation)
        audio?.let { playGlobalAudio(entity.location, it.id, 20) }
    }

    override fun visualizeImpact(entity: Entity, target: Entity, state: BattleState) {
        if (state.isFrozen) {
            playGlobalAudio(target.location, impactAudio, 20)
            target.graphics(Graphics(1677, 96))
            return
        }
        super.visualizeImpact(entity, target, state)
    }

    override fun fireEffect(entity: Entity, victim: Entity, state: BattleState) {
        if (state.estimatedHit == -1) return

        val ticks = (1 + (definition.type.ordinal - SpellType.RUSH.ordinal)) * 8
        if (state.estimatedHit > -1) {
            if (!hasTimerActive(victim, "frozen:immunity")) {
                registerTimer(victim, spawnTimer("frozen", ticks, true))
            } else if (definition.type == SpellType.BARRAGE) {
                state.isFrozen = true
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
        definition.type.getImpactAmount(entity, victim, 4)
}