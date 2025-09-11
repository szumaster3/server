package content.global.skill.construction

import core.api.amountInInventory
import core.api.log
import core.api.sendString
import core.game.interaction.InterfaceListener
import shared.consts.Components
import shared.consts.Items

class BuildInterface : InterfaceListener {

    companion object {
        private val COINS_VALUE_TO_CHILD = arrayOf(100 to 138, 5000 to 139, 7500 to 147, 7500 to 155, 7500 to 156, 7500 to 157, 10000 to 140, 15000 to 141, 25000 to 142, 50000 to 143, 50000 to 149, 50000 to 150, 75000 to 145, 75000 to 152, 100000 to 144, 100000 to 151, 150000 to 146, 150000 to 153, 150000 to 154, 250000 to 148, 250000 to 159)
        private const val BUILD_INTERFACE = Components.POH_BUILD_SCREEN_402
    }

    override fun defineInterfaceListeners() {
        onOpen(BUILD_INTERFACE) { player, _ ->
            val coins = amountInInventory(player, Items.COINS_995)
            for (i in COINS_VALUE_TO_CHILD.indices) {
                val (amount, childId) = COINS_VALUE_TO_CHILD[i]
                if (coins < amount) break
                sendString(player, core.tools.YELLOW + "$amount coins", BUILD_INTERFACE, childId)
            }
            return@onOpen true
        }

        on(BUILD_INTERFACE) { player, _, _, buttonID, _, _ ->
            val index = buttonID - 160
            log(this.javaClass, core.tools.Log.FINE, "BuildRoom Interface Index: $index")
            val rooms = RoomProperties.values()
            if (index in rooms.indices) {
                player.dialogueInterpreter.open("con:room", rooms[index])
            }
            return@on true
        }
    }
}
