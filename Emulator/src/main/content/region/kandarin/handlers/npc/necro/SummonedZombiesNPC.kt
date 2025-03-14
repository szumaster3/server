package content.region.kandarin.handlers.npc.necro

import core.api.getAttribute
import core.api.getPathableRandomLocalCoordinate
import core.api.withinDistance
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class SummonedZombiesNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    var clearTime = 0

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return SummonedZombiesNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SUMMONED_ZOMBIE_77)
    }

    companion object {
        fun summonZombie(player: Player) {
            val summonedZombie = SummonedZombiesNPC(NPCs.SUMMONED_ZOMBIE_77)
            val randomLocation = getPathableRandomLocalCoordinate(summonedZombie, 1, player.location, 2)
            if (randomLocation != null) {
                summonedZombie.location = randomLocation
            } else {
                println("Failed to find a valid location for summoned zombie.")
                return
            }

            summonedZombie.isWalks = true
            summonedZombie.isAggressive = true
            summonedZombie.isActive = false
            summonedZombie.isRespawn = false

            if (summonedZombie.asNpc() != null) {
                summonedZombie.properties.teleportLocation = summonedZombie.properties.spawnLocation
            }

            summonedZombie.isActive = true

            GameWorld.Pulser.submit(
                object : Pulse(1, summonedZombie) {
                    override fun pulse(): Boolean {
                        if (getAttribute(player, "necro:zombie_alive", 0) >= 3) {
                            return true
                        }
                        if (!player.isActive || !withinDistance(player, SummonedZombiesNPC().asNpc().location, 10)) {
                            println("Player is either dead or out of range for zombie attack.")
                            return true
                        }
                        summonedZombie.init()
                        summonedZombie.attack(player)
                        return true
                    }
                },
            )
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            val player = killer.asPlayer()
            val counter = player.getAttribute("necro:zombie_alive", 0)
            if (counter > 0) {
                player.setAttribute("necro:zombie_alive", counter - 1)
            }
        }
        clear()
    }
}
