package content.region.morytania.port_phasmatys.quest.ahoy.plugin

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import org.rs.consts.NPCs

class GiantLobsterNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = content.region.morytania.port_phasmatys.quest.ahoy.plugin.GiantLobsterNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.GIANT_LOBSTER_1693)

    companion object {
        fun spawnGiantLobster(player: Player) {
            val giantLobster =
                content.region.morytania.port_phasmatys.quest.ahoy.plugin.GiantLobsterNPC(NPCs.GIANT_LOBSTER_1693)
            giantLobster.location = location(3617, 3543, 0)
            giantLobster.isWalks = true
            giantLobster.isAggressive = true
            giantLobster.isActive = false

            if (giantLobster.asNpc() != null && giantLobster.isActive) {
                giantLobster.properties.teleportLocation = giantLobster.properties.spawnLocation
            }
            giantLobster.isActive = true
            GameWorld.Pulser.submit(
                object : Pulse(1, giantLobster) {
                    override fun pulse(): Boolean {
                        giantLobster.init()
                        sendMessage(player, "You are attacked by a giant lobster!!")
                        registerHintIcon(player, giantLobster)
                        giantLobster.attack(player)
                        return true
                    }
                },
            )
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            clearHintIcon(killer)
            setAttribute(killer,
                content.region.morytania.port_phasmatys.quest.ahoy.plugin.GhostsAhoyUtils.lastMapScrap, true)
        }
        clear()
        super.finalizeDeath(killer)
    }
}
