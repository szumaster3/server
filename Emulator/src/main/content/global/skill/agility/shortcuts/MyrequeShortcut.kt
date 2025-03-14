package content.global.skill.agility.shortcuts

import core.api.sendMessage
import core.api.sendMessageWithDelay
import core.api.teleport
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs.consts.Scenery

class MyrequeShortcut : InteractionListener {
    override fun defineListeners() {
        on(Scenery.TRAPDOOR_5055, IntType.SCENERY, "open") { player, node ->
            teleport(player, Location(3477, 9845))
            sendMessage(player, "You open the trap door and find yourself in the inn basement.")
            return@on true
        }

        on(Scenery.LADDER_5054, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, Location(3496, 3465, 0))
            return@on true
        }

        on(Scenery.WALL_5052, IntType.SCENERY, "search") { player, node ->
            sendMessage(player, "You search the wall and find the door which Veliaf told you about.")
            sendMessageWithDelay(player, "You walk through.", 1)
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }
    }
}
