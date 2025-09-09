package content.region.kandarin.guild.legends.plugin

import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.RegionManager
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

class LegendsGuildPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles interaction with legends guild gates.
         */

        on(intArrayOf(Scenery.GATE_2391, Scenery.GATE_2392), IntType.SCENERY, "open") { player, node ->
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            sendMessage(player, "The guards salute you as you walk past.")
            val nearbyNPCs = RegionManager.getLocalNpcs(player, 8)
            val guards = nearbyNPCs.filter { it.id == NPCs.LEGENDS_GUARD_398 || it.id == NPCs.LEGENDS_GUARD_399 }
            guards.forEach { guard ->
                guard.faceTemporary(player, 1)
                guard.sendChat("Legends' Guild member approaching!", 1)
            }
            return@on true
        }

        /*
         * Handles interaction with legend guild doors.
         */

        on(intArrayOf(Scenery.LEGENDS_GUILD_DOOR_2896, Scenery.LEGENDS_GUILD_DOOR_2897), IntType.SCENERY, "open") { player, node ->
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            sendMessage(player, "You push the huge Legends Guild doors open.", null)
            if (player.location.y < 3374) {
                sendMessage(player, "You approach the Legends Guild main doors.", null)
            }
            return@on true
        }

        /*
         * Handles interaction with staircase.
         */

        on(Scenery.STAIRCASE_32048, IntType.SCENERY, "climb-up", "open") { player, _ ->
            teleport(player, Location.create(2723, 3375, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        /*
         * Handles interaction with cupboard.
         */

        on(Scenery.OPEN_CUPBOARD_2886, IntType.SCENERY, "search") { player, _ ->
            animate(player, 537)

            if (inInventory(player, Items.MACHETE_975)) {
                sendMessage(player, "You search the cupboard but find nothing interesting.")
                return@on true
            }

            sendItemDialogue(player, Items.MACHETE_975, "You find a machete in the cupborad.")

            if(freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough inventory space to hold that item.")
                return@on true
            }

            sendMessage(player, "You find a machete.")
            addItem(player, Items.MACHETE_975)
            return@on true
        }
    }
}
