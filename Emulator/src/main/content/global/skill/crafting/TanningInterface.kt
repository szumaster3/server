package content.global.skill.crafting

import core.api.sendInputDialogue
import core.game.interaction.InterfaceListener
import core.game.node.item.Item
import org.rs.consts.Components

class TanningInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.TANNER_324) { player, _, opcode, buttonID, _, _ ->
            var type: Tanning? = null
            when (buttonID) {
                1 -> type = Tanning.SOFT_LEATHER
                2 -> type = Tanning.HARD_LEATHER
                3 -> type = Tanning.SNAKESKIN
                4 -> type = Tanning.SNAKESKIN2
                5 -> type = Tanning.GREENDHIDE
                6 -> type = Tanning.BLUEDHIDE
                7 -> type = Tanning.REDDHIDE
                8 -> type = Tanning.BLACKDHIDE
            }
            if (type == null) {
                return@on true
            }
            var amount = 0
            val product: Tanning = type
            when (opcode) {
                155 -> amount = 1
                196 -> amount = 5
                124 -> {
                    amount = 10
                    sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                        Tanning.tan(player, value as Int, product)
                    }
                }

                199 ->
                    sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                        Tanning.tan(player, value as Int, product)
                    }

                234 -> amount = player.inventory.getAmount(Item(type.item, 1))
            }
            Tanning.tan(player, amount, type)
            return@on true
        }
    }
}
