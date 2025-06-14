package content.global.activity.ttrail

import core.api.openInterface
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import org.rs.consts.Components
import org.rs.consts.Items

/**
 * Represents sextant interface for solving coordinate clues on Treasure Trails.
 * @author Vexia
 */
class SextantListener : InterfaceListener, InteractionListener {

    override fun defineListeners() {
        /*
         * Handles interaction with sextant.
         */

        on(Items.SEXTANT_2574, IntType.ITEM, "look through") { player, _ ->
            if (player.houseManager.isInHouse(player)) {
                sendMessage(player,"The sextant doesn't seem to work here.")
                return@on true
            }
            openInterface(player, Components.TRAIL_SEXTANT_365)
            return@on true
        }
    }

    override fun defineInterfaceListeners() {

        /*
         * Handles sextant interface interaction.
         */

        on(Components.TRAIL_SEXTANT_365) { player, component, opcode, buttonID, slot, itemID ->
            when(buttonID) {
                11 -> sendMessage(player, "You need to get the horizon in the middle of the eye piece.")
                else -> sendMessage(player, "Nothing interesting happens.")
            }
            return@on true
        }
    }

}