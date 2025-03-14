package content.minigame.templetrekking.monsters

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class TentacleNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    val killCounter = "tentacle-kills"
    lateinit var player: Player

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return TentacleNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TENTACLE_3580)
    }

    companion object {
        fun spawnTentacleNPC(player: Player) {
            val tentacle = TentacleNPC(NPCs.TENTACLE_3580)
            tentacle.isWalks = false
            tentacle.isAggressive = true
            tentacle.isActive = false
            tentacle.isIgnoreMultiBoundaries(player)

            if (tentacle.asNpc() != null && tentacle.isActive) {
                tentacle.properties.teleportLocation = tentacle.properties.spawnLocation
            }
            tentacle.isActive = true
            GameWorld.Pulser.submit(
                object : Pulse(0, tentacle) {
                    override fun pulse(): Boolean {
                        tentacle.init()
                        tentacle.transform(NPCs.TENTACLE_3621)
                        tentacle.attack(player)
                        return true
                    }
                },
            )
        }
    }

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            val player = killer.asPlayer()
            incrementAttribute(killCounter)
        }
        clear()
        super.finalizeDeath(killer)
    }
}
