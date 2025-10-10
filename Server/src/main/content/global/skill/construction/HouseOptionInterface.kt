package content.global.skill.construction

import core.game.node.entity.player.link.WarningManager
import core.game.node.entity.player.link.Warnings
import core.api.sendMessage
import core.game.interaction.InterfaceListener
import shared.consts.Components

/**
 * Handles the house options interface.
 */
class HouseOptionInterface : InterfaceListener {

    companion object {
        private const val BUILD_MODE_ON = 14
        private const val BUILD_MODE_OFF = 1
        private const val EXPEL_GUESTS = 15
        private const val LEAVE_HOUSE = 13
    }

    override fun defineInterfaceListeners() {
        on(Components.POH_HOUSE_OPTIONS_398) { player, _, _, buttonID, _, _ ->
            if (buttonID == BUILD_MODE_ON) {
                if (player.houseManager.isInHouse(player) && !WarningManager.isWarningDisabled(player, Warnings.PLAYER_OWNED_HOUSES)) {
                    WarningManager.openWarningInterface(player, Warnings.PLAYER_OWNED_HOUSES)
                } else {
                    player.houseManager.toggleBuildingMode(player, true)
                }
                return@on true
            }
            if (buttonID == BUILD_MODE_OFF) {
                player.houseManager.toggleBuildingMode(player, false)
                return@on true
            }
            if (buttonID == EXPEL_GUESTS) {
                player.houseManager.expelGuests(player)
                return@on true
            }
            if (buttonID == LEAVE_HOUSE) {
                if (!player.houseManager.isInHouse(player)) {
                    sendMessage(player, "You can't do this outside of your house.")
                } else {
                    HouseManager.leave(player)
                }
                return@on true
            }

            return@on false
        }
    }
}
