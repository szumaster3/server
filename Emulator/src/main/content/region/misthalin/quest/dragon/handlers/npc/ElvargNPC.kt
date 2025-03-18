package content.region.misthalin.quest.dragon.handlers.npc

import content.region.misthalin.quest.dragon.DragonSlayer
import core.api.combat.calculateDragonFireMaxHit
import core.api.inInventory
import core.api.quest.getQuestStage
import core.api.sendMessage
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
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.NPCs
import org.rs.consts.Quests
import kotlin.math.ceil

@Initializable
class ElvargNPC : AbstractNPC {
    private val combatHandler: CombatSwingHandler = ElvargCombatSwingHandler()

    constructor() : super(0, null)

    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = ElvargNPC(id, location)

    override fun commenceDeath(killer: Entity) {
        val direction = Direction.getLogicalDirection(getLocation(), killer.location)
        Pulser.submit(
            object : Pulse(1, this) {
                override fun pulse(): Boolean {
                    faceLocation(centerLocation.transform(direction.stepX * 3, direction.stepY * 3, 0))
                    return true
                }
            },
        )
        setDirection(direction)
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        val scenery = Scenery(25202, getLocation(), rotation)
        SceneryBuilder.add(scenery)
        killer.faceLocation(scenery.centerLocation)
        killer.lock()
        Pulser.submit(
            object : Pulse(1) {
                var counter: Int = 0

                override fun pulse(): Boolean {
                    counter++
                    if (counter == 1) {
                        killer.animate(ANIMATIONS[0])
                    } else if (counter == 4) {
                        val player = (killer as Player)
                        SceneryBuilder.replace(scenery, scenery.transform(25203))
                        if (!player.inventory.add(DragonSlayer.ELVARG_HEAD)) {
                            GroundItemManager.create(DragonSlayer.ELVARG_HEAD, player)
                        }
                        killer.animate(ANIMATIONS[1])
                        killer.unlock()
                    } else if (counter == 12) {
                        SceneryBuilder.remove(getObject(scenery.location))
                        return true
                    }
                    return false
                }
            },
        )
    }

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler = combatHandler

    override fun getIds(): IntArray = intArrayOf(NPCs.ELVARG_742)

    val rotation: Int
        get() {
            when (getDirection()) {
                Direction.EAST -> return 3
                Direction.NORTH -> return 2
                Direction.WEST -> return 1
                Direction.SOUTH -> return 0
                else -> {}
            }
            return 0
        }

    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean {
        if (entity !is Player) {
            return super.isAttackable(entity, style, message)
        }
        val player = entity
        if (getQuestStage(player, Quests.DRAGON_SLAYER) == 40 && (inInventory(player, DragonSlayer.ELVARG_HEAD.id))) {
            if (message) {
                sendMessage(player, "You have already slain the dragon. Now you just need to return to Oziach for")
                sendMessage(player, "your reward!")
            }
            return false
        }
        if (getQuestStage(player, Quests.DRAGON_SLAYER) > 40) {
            if (message) {
                sendMessage(player, "You have already slain Elvarg.")
            }
            return false
        }
        return super.isAttackable(entity, style, message)
    }

    override fun getDragonfireProtection(fire: Boolean): Int = 0x2 or 0x4 or 0x8

    internal class ElvargCombatSwingHandler : CombatSwingHandler(CombatStyle.RANGE) {
        private var style = CombatStyle.RANGE
        private val fireType = FireType.FIERY_BREATH

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
                maxDamage = 60,
                wyvern = false,
                unprotectableDamage = 3,
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
            if (victim.centerLocation.withinDistance(entity.centerLocation, getCombatDistance(entity, victim, 9)) &&
                super.canSwing(entity, victim) != InteractionType.NO_INTERACT
            ) {
                entity.walkingQueue.reset()
                return InteractionType.STILL_INTERACT
            }
            return InteractionType.NO_INTERACT
        }

        override fun getArmourSet(e: Entity?): ArmourSet? = style.swingHandler.getArmourSet(e)

        override fun getSetMultiplier(
            e: Entity?,
            skillId: Int,
        ): Double = style.swingHandler.getSetMultiplier(e, skillId)

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

            if (victim!!.centerLocation.withinDistance(entity!!.centerLocation, getCombatDistance(entity, victim, 1))) {
                if (RandomFunction.random(10) < 7) {
                    style = CombatStyle.MELEE
                }
            } else {
                ticks += ceil(entity.location.getDistance(victim.location) * 0.3).toInt()
            }

            state!!.style = style

            if (isAccurateImpact(entity, victim)) {
                val max = calculateHit(entity, victim, 1.0)
                state.maximumHit = max
                hit = RandomFunction.random(max + 1)
            }
            state.estimatedHit = hit
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
                    Projectile.ranged(entity, victim, 450, 20, 36, 50, 15).send()
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
            private val MELEE_ATTACK = Animation(80, Priority.HIGH)

            private val DRAGONFIRE = DragonfireSwingHandler(false, 60, null, true)
        }
    }

    companion object {
        private val ANIMATIONS =
            arrayOf(Animation(Animations.TAKING_OFF_ELVARG_S_HEAD_6654), Animation(Animations.ELVARG_HEAD_6655))
    }
}
