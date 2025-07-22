package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.SpellProjectile
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.GameWorld.ticks
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Represents the Bind spell.
 */
@Initializable
class BindSpell private constructor(
    private val definition: BindSpellDefinition
) : CombatSpell(
    definition.type,
    SpellBook.MODERN,
    definition.level,
    definition.xp,
    definition.castSound,
    definition.impactSound,
    SpellProjectile.ANIMATION,
    definition.start,
    definition.projectile,
    definition.end,
    *definition.runes
) {
    constructor() : this(BindSpellDefinition.BIND)

    override fun fireEffect(entity: Entity, victim: Entity, state: BattleState) {
        if (victim is NPC && victim.name.contains("impling")) {
            state.estimatedHit = -2
        }
        if (state.estimatedHit == -1) return

        var tick = 9
        if (definition.type == SpellType.BIND) {
            state.estimatedHit = -2
        }
        when (state.spell.spellId) {
            30 -> tick = 17
            56 -> tick = 25
        }

        if (!victim.locks.isMovementLocked() && victim is Player) {
            victim.packetDispatch.sendMessage("A magical force stops you from moving!")
        }
        victim.walkingQueue.reset()
        victim.locks.lockMovement(tick)
        entity.setAttribute("entangleDelay", ticks + tick + 2)
    }

    override fun getMaximumImpact(entity: Entity, victim: Entity, state: BattleState): Int =
        if (definition.type == SpellType.ENTANGLE) 5 else 3

    @Throws(Throwable::class)
    override fun newInstance(type: SpellType?): Plugin<SpellType?> {
        BindSpellDefinition.values().forEach {
            SpellBook.MODERN.register(it.buttonId, BindSpell(it))
        }
        return this
    }
}