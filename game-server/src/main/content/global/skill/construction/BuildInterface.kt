package content.global.skill.construction

import core.api.amountInInventory
import core.api.log
import core.api.sendString
import core.game.interaction.InterfaceListener
import core.tools.Log
import org.rs.consts.Components
import org.rs.consts.Items

class BuildInterface : InterfaceListener {

    private val coinsToInt = arrayOf(100 to 138, 5000 to 139, 7500 to 147, 7500 to 155, 7500 to 156, 7500 to 157, 10000 to 140, 15000 to 141, 25000 to 142, 50000 to 143, 50000 to 149, 50000 to 150, 75000 to 145, 75000 to 152, 100000 to 144, 100000 to 151, 150000 to 146, 150000 to 153, 150000 to 154, 250000 to 148, 250000 to 159)
    private val buildInterface = Components.POH_BUILD_SCREEN_402

    override fun defineInterfaceListeners() {
        onOpen(buildInterface) { player, _ ->
            val coins = amountInInventory(player, Items.COINS_995)
            for ((amount, childId) in coinsToInt) {
                if (coins < amount) break
                sendString(player, core.tools.YELLOW + "$amount coins", buildInterface, childId)
            }
            return@onOpen true
        }

        on(buildInterface) { player, _, _, buttonID, _, _ ->
            val index = buttonID - 160
            log(this.javaClass, Log.FINE, "BuildRoom Interface Index: $index")
            if (index >= 0 && index < RoomProperties.values().size) {
                player.dialogueInterpreter.open("con:room", RoomProperties.values()[index])
            }
            return@on true
        }
    }
}
