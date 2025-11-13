package content.region.fremennik.lunar

import core.api.location
import core.api.resetCamera
import core.api.sendMessage
import core.api.teleport
import core.api.utils.PlayerCamera
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager.TeleportType
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

class LunarIslePlugin : InteractionListener {

    companion object {
        private const val CLOSED_DOOR = Scenery.DOOR_16774
        private const val OPENED_DOOR = Scenery.DOOR_16777
        private const val HOUSE = NPCs.HOUSE_4512
        private val CYRISUS = intArrayOf(NPCs.CYRISUS_5893, NPCs.CYRISUS_5894, NPCs.CYRISUS_5895, NPCs.CYRISUS_5896, NPCs.CYRISUS_5897)
    }

    override fun defineListeners() {
        on(CLOSED_DOOR, IntType.SCENERY, "open") { player, _ ->
            teleport(player, location(2101, 3926, 0), TeleportType.INSTANT)
            resetCamera(player)
            return@on true
        }

        on(OPENED_DOOR, IntType.SCENERY, "close") { player, _ ->
            teleport(player, location(2101, 3926, 0), TeleportType.INSTANT)
            resetCamera(player)
            return@on true
        }

        on(HOUSE, IntType.NPC, "go-inside") { player, _ ->
            teleport(player, location(2451, 4645, 0), TeleportType.INSTANT)
            PlayerCamera(player).shake(1, 0, 0, 16, 20)
            return@on true
        }

        onUseWith(IntType.NPC, Items.CAVE_NIGHTSHADE_2398, *CYRISUS) { player, _, _ ->
            sendMessage(player, "How evil! Are you trying to kill him?")
            return@onUseWith true
        }
    }
}
