package content.region.wilderness.handlers.npc

import core.api.combat.calculateDragonFireMaxHit
import core.api.utils.BossKillCounter
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.InteractionType
import core.game.node.entity.combat.equipment.ArmourSet
import core.game.node.entity.combat.equipment.FireType
import core.game.node.entity.combat.equipment.special.DragonfireSwingHandler
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.NPCs
import kotlin.math.ceil

@Initializable
class KingBlackDragonNPC : AbstractNPC {
    private val combatHandler: CombatSwingHandler = KBDCombatSwingHandler()

    constructor() : super(-1, null)

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        BossKillCounter.addToKillCount(killer as Player, this.id)
    }

    constructor(id: Int, l: Location?) : super(id, l)

    override fun init() {
        super.init()
        configureBossData()
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return KingBlackDragonNPC(id, location)
    }

    override fun getDragonfireProtection(fire: Boolean): Int {
        return 0x2 or 0x4 or 0x8
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KING_BLACK_DRAGON_50)
    }

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler {
        return combatHandler
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        return super.newInstance(arg)
    }

    internal class KBDCombatSwingHandler : CombatSwingHandler(CombatStyle.RANGE) {
        private var style = CombatStyle.RANGE
        var fireType: FireType = FireType.FIERY_BREATH
            private set

        override fun adjustBattleState(
            entity: Entity,
            victim: Entity,
            state: BattleState,
        ) {
            if (style == CombatStyle.RANGE) {
                fireType.task.exec(victim, entity)
                state.style = null
                DRAGONFIRE.adjustBattleState(entity, victim, state)
                state.style = CombatStyle.RANGE
                return
            }
            style.swingHandler.adjustBattleState(entity, victim, state)
        }

        override fun calculateAccuracy(entity: Entity?): Int {
            if (style == CombatStyle.MELEE) {
                return style.swingHandler.calculateAccuracy(entity)
            }
            return CombatStyle.MAGIC.swingHandler.calculateAccuracy(entity)
        }

        override fun calculateDefence(
            victim: Entity?,
            attacker: Entity?,
        ): Int {
            if (style == CombatStyle.MELEE) {
                return style.swingHandler.calculateDefence(victim, attacker)
            }
            return CombatStyle.MAGIC.swingHandler.calculateDefence(victim, attacker)
        }

        override fun calculateHit(
            entity: Entity?,
            victim: Entity?,
            modifier: Double,
        ): Int {
            if (style == CombatStyle.MELEE) {
                return style.swingHandler.calculateHit(entity, victim, modifier)
            }
            return calculateDragonFireMaxHit(
                entity = victim!!,
                maxDamage = 56,
                wyvern = false,
                unprotectableDamage = if (fireType != FireType.FIERY_BREATH) 10 else 0,
                sendMessage = true,
            )
        }

        override fun canSwing(
            entity: Entity,
            victim: Entity,
        ): InteractionType? {
            if (!isProjectileClipped(entity, victim, false)) {
                return InteractionType.NO_INTERACT
            }
            if (victim.centerLocation.withinMaxnormDistance(
                    entity.centerLocation,
                    getCombatDistance(entity, victim, 9),
                ) &&
                super.canSwing(entity, victim) == InteractionType.STILL_INTERACT
            ) {
                entity.walkingQueue.reset()
                return InteractionType.STILL_INTERACT
            }
            return InteractionType.NO_INTERACT
        }

        override fun getArmourSet(e: Entity?): ArmourSet? {
            return style.swingHandler.getArmourSet(e)
        }

        override fun getSetMultiplier(
            e: Entity?,
            skillId: Int,
        ): Double {
            return style.swingHandler.getSetMultiplier(e, skillId)
        }

        override fun impact(
            entity: Entity?,
            victim: Entity?,
            state: BattleState?,
        ) {
            style.swingHandler.impact(entity, victim, state)
        }

        override fun swing(
            entity: Entity?,
            victim: Entity?,
            state: BattleState?,
        ): Int {
            style = CombatStyle.RANGE
            var hit = 0
            var ticks = 1
            if (victim!!.centerLocation.withinMaxnormDistance(
                    entity!!.centerLocation,
                    getCombatDistance(entity, victim, 1),
                ) &&
                RandomFunction.random(10) < 7
            ) {
                style = CombatStyle.MELEE
            } else {
                ticks += ceil(entity.location.getDistance(victim.location) * 0.3).toInt()
            }
            fireType = FireType.values()[RandomFunction.random(FireType.values().size)]
            state!!.style = style
            if (isAccurateImpact(entity, victim)) {
                val max = calculateHit(entity, victim, 1.0)
                state.maximumHit = max
                hit = RandomFunction.random(max + 1)
            }
            state.estimatedHit = hit
            (entity as NPC?)!!.aggressiveHandler.pauseTicks = 2
            return ticks
        }

        override fun visualize(
            entity: Entity,
            victim: Entity?,
            state: BattleState?,
        ) {
            when (style) {
                CombatStyle.MELEE -> entity.animate(MELEE_ATTACK)
                CombatStyle.RANGE -> {
                    Projectile.ranged(entity, victim, fireType.projectileId, 20, 36, 50, 15).send()
                    entity.animate(fireType.animation)
                }

                else -> {}
            }
        }

        override fun visualizeImpact(
            entity: Entity?,
            victim: Entity?,
            state: BattleState?,
        ) {
            if (style != CombatStyle.MELEE) {
                DRAGONFIRE.visualizeImpact(entity, victim, state)
            } else {
                style.swingHandler.visualizeImpact(entity, victim, state)
            }
        }

        companion object {
            private val DRAGONFIRE = DragonfireSwingHandler(false, 56, null, true)
            private val MELEE_ATTACK = Animation(80, Priority.HIGH)
        }
    }

    companion object {
        private val DEFAULT_SPAWN: Location = Location.create(2273, 4698, 0)
    }
}
