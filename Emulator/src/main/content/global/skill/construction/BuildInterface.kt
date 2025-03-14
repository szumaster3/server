package content.global.skill.construction

import core.api.amountInInventory
import core.api.log
import core.api.sendString
import core.game.interaction.InterfaceListener
import core.tools.Log
import org.rs.consts.Components
import org.rs.consts.Items

class BuildInterface : InterfaceListener {
    private val coinsToInt =
        listOf(
            100 to 138,
            5000 to 139,
            10000 to 140,
            15000 to 141,
            25000 to 142,
            50000 to 143,
            50000 to 149,
            50000 to 150,
            75000 to 145,
            75000 to 152,
            7500 to 147,
            7500 to 155,
            7500 to 156,
            7500 to 157,
            100000 to 144,
            100000 to 151,
            150000 to 146,
            150000 to 153,
            150000 to 154,
            250000 to 148,
            250000 to 159,
        )

    override fun defineInterfaceListeners() {
        onOpen(Components.POH_BUILD_SCREEN_402) { player, _ ->
            coinsToInt.forEach { (amount, childId) ->
                if (amountInInventory(player, Items.COINS_995) >= amount) {
                    sendString(player, core.tools.YELLOW + "$amount coins", Components.POH_BUILD_SCREEN_402, childId)
                }
            }
            return@onOpen true
        }

        on(Components.POH_BUILD_SCREEN_402) { player, _, _, buttonID, _, _ ->
            val index = buttonID - 160
            log(this.javaClass, Log.FINE, "BuildRoom Interface Index: $index")
            if (index > -1 && index < RoomProperties.values().size) {
                player.dialogueInterpreter.open("con:room", RoomProperties.values()[index])
            }
            return@on true
        }
    }
}
