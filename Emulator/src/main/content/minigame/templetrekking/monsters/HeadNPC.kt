package content.minigame.templetrekking.monsters

import content.minigame.templetrekking.monsters.TentacleNPC.Companion.spawnTentacleNPC
import core.api.getAttribute
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.NPCs

class HeadNPC(
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
        return HeadNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HEAD_3619, NPCs.HEAD_3620)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (getAttribute(player, killCounter, -1) == 4) {
            spawnTentacleNPC(player)
        }
    }

    companion object {
        fun spawnTentacleHeadNPC(player: Player) {
            val head = HeadNPC(NPCs.HEAD_3620)
            head.location = Location.create(2573, 5035, 2).transform(Direction.EAST)
            head.isWalks = false
            head.isAggressive = true
            head.isActive = false
            head.isIgnoreMultiBoundaries(player)

            if (head.asNpc() != null && head.isActive) {
                head.properties.teleportLocation = head.properties.spawnLocation
            }
            head.isActive = true
            GameWorld.Pulser.submit(
                object : Pulse(0, head) {
                    var counter = 0

                    override fun pulse(): Boolean {
                        head.init()
                        head.transform(NPCs.HEAD_3619)
                        head.attack(player)
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
        clear()
        super.finalizeDeath(killer)
    }
}
