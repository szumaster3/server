package content.region.fremennik.neitiznot

import core.api.*
import core.api.openBankAccount
import core.api.utils.Vector
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.world.map.Direction
import core.game.world.map.zone.ZoneBorders
import shared.consts.NPCs
import shared.consts.Scenery

class NeitiznotPlugin : InteractionListener, MapArea {

    private val ROPE_BRIDGE = intArrayOf(Scenery.ROPE_BRIDGE_21306, Scenery.ROPE_BRIDGE_21307, Scenery.ROPE_BRIDGE_21308, Scenery.ROPE_BRIDGE_21309)

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(
        getRegionBorders(9275), ZoneBorders(2313, 3786, 2331, 3802)
    )

    override fun defineListeners() {

        /*
         * Handles using any item on Yak NPC.
         */

        onUseWith(IntType.NPC, -1, NPCs.YAK_5529) { player, _, with ->
            if (with is NPC && with.id == NPCs.YAK_5529) {
                sendMessage(player, "The cow doesn't want that.")
            }
            return@onUseWith true
        }

        /*
         * Handles opening bank chest.
         */

        on(Scenery.BANK_CHEST_21301, IntType.SCENERY, "open") { player, _ ->
            openBankAccount(player)
            return@on true
        }

        /*
         * Handles cure yak-hide.
         */

        on(NPCs.THAKKRAD_SIGMUNDSON_5506, IntType.NPC, "Craft-goods") { player, _ ->
            openDialogue(player, "thakkrad-yak")
            return@on true
        }

        /*
         * Handles crossing the rope bridges.
         */

        on(ROPE_BRIDGE, IntType.SCENERY, "walk-across") { player, node ->
            val playerLocation = player.location
            val destination = Vector.betweenLocs(playerLocation, node.location)
            val direction = when (node.id) {
                Scenery.ROPE_BRIDGE_21307, Scenery.ROPE_BRIDGE_21309 -> Direction.SOUTH
                else -> Direction.NORTH
            }
            lock(player,10)
            registerLogoutListener(player,"rope-bridge"){ p -> p.location = playerLocation }
            forceMove(player, playerLocation, playerLocation.transform(destination.toDirection(), 11), 0, 330, direction)
            runTask(player, 10) { clearLogoutListener(player, "rope-bridge") }
            return@on true
        }
    }
}