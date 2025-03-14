package content.global.ame.sandwichlady

import content.data.GameAttributes
import core.api.setAttribute
import core.game.interaction.InterfaceListener
import core.game.node.item.Item
import core.game.system.timer.impl.AntiMacro
import org.rs.consts.Components
import org.rs.consts.Items

class SandwichLadyInterface : InterfaceListener {
    val SANDWICH_INTERFACE = Components.MACRO_SANDWICH_TRAY_297
    val baguette = Items.BAGUETTE_6961
    val triangle_sandwich = Items.TRIANGLE_SANDWICH_6962
    val sandwich = Items.SQUARE_SANDWICH_6965
    val roll = Items.ROLL_6963
    val pie = Items.MEAT_PIE_2327
    val kebab = Items.KEBAB_1971
    val chocobar = Items.CHOCOLATE_BAR_1973

    override fun defineInterfaceListeners() {
        on(SANDWICH_INTERFACE) { player, _, _, buttonID, _, _ ->
            val event = AntiMacro.getEventNpc(player!!)
            if (event == null) {
                player.interfaceManager.close()
                return@on true
            }
            val item =
                when (buttonID) {
                    7 -> Item(baguette)
                    8 -> Item(triangle_sandwich)
                    9 -> Item(sandwich)
                    10 -> Item(roll)
                    11 -> Item(pie)
                    12 -> Item(kebab)
                    13 -> Item(chocobar)
                    else -> Item(baguette)
                }

            setAttribute(player, GameAttributes.S_LADY_ITEM_VALUE, item.id)
            player.interfaceManager.close()
            player.dialogueInterpreter.open(SandwichLadyDialogue(true), event)
            return@on true
        }
    }
}
