package content.region.asgarnia.quest.ball.handlers

import core.api.setAttribute
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalPlayers
import core.plugin.Initializable

/**
 * Represents Witch's experiments NPCs.
 * @author Ethan Kyle Millard (March 25, 2020, 8:28 PM)
 */
@Initializable
class WitchExperimentNPC : AbstractNPC {
    var type: ExperimentType? = null
    private var commenced = false
    var p: Player? = null

    constructor() : super(0, null)

    internal constructor(id: Int, location: Location?, player: Player?) : super(id, location) {
        this.isWalks = true
        this.isRespawn = false
        this.type = ExperimentType.forId(id)
        p = player
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (!p!!.isActive || !getLocalPlayers(this).contains(p)) {
            p!!.removeAttribute("witchs-experiment:npc_spawned")
            clear()
        }
        if (!properties.combatPulse.isAttacking) {
            properties.combatPulse.attack(p)
        }
    }

    override fun startDeath(killer: Entity) {
        if (killer == p) {
            type!!.transform(this, p!!)
            return
        }
        super.startDeath(killer)
    }

    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
        return p == entity
    }

    override fun canSelectTarget(target: Entity): Boolean {
        if (target is Player) {
            return target == this.p
        }
        return true
    }


    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return WitchExperimentNPC(id, location, null)
    }

    override fun getIds(): IntArray {
        return intArrayOf(897, 898, 899, 900)
    }

    fun setCommenced(commenced: Boolean) {
        this.commenced = commenced
    }

    enum class ExperimentType(val id: Int, vararg message: String) {
        FIRST(897, ""), SECOND(
            898, "The shapeshifter's body begins to deform!", "The shapeshifter turns into a spider!"
        ),
        THIRD(899, "The shapeshifter's body begins to twist!", "The shapeshifter turns into a bear!"), FOURTH(
            900, "The shapeshifter's body pulses!", "The shapeshifter turns into a wolf!"
        ),
        END(-1, ""), ;

        private val message: Array<String> = message as Array<String>

        /**
         * Transforms the new npc.
         */
        fun transform(npc: WitchExperimentNPC, player: Player) {
            val newType = next()
            npc.lock()
            npc.pulseManager.clear()
            npc.walkingQueue.reset()
            setAttribute(player, "/save:witchs_house:experiment_id", id)
            Pulser.submit(object : Pulse(1, npc, player) {
                var counter: Int = 0

                override fun pulse(): Boolean {
                    when (++counter) {
                        1 -> {
                            npc.unlock()
                            npc.animator.reset()
                            npc.fullRestore()
                            npc.type = newType
                            npc.transform(newType.id)
                            npc.impactHandler.disabledTicks = 1
                            if (newType != END) {
                                npc.properties.combatPulse.attack(player)
                            }
                            if (newType.getMessage() != null && newType != END) {
                                player.sendMessage(newType.getMessage()!![0])
                                player.sendMessage(newType.getMessage()!![1])
                            }
                            if (newType == END) {
                                player.setAttribute("witchs_house:experiment_killed", true)
                                player.sendMessage("You finally kill the shapeshifter once and for all.")
                                npc.clear()
                                return false
                            }
                            player.unlock()
                            return true
                        }
                    }
                    return false
                }
            })
        }

        fun next(): ExperimentType {
            return experimentTypes[ordinal + 1]
        }

        fun getMessage(): Array<String>? {
            return message
        }

        companion object {
            fun forId(id: Int): ExperimentType? {
                for (type in values()) {
                    if (type.id == id) {
                        return type
                    }
                }
                return null
            }

            private val experimentTypes = values()
        }
    }
}