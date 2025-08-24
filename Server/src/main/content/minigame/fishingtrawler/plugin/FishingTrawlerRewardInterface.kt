package content.minigame.fishingtrawler.plugin

import core.api.sendMessage
import core.game.interaction.InterfaceListener

/**
 * Represents the Fishing trawler reward interface.
 */
class FishingTrawlerRewardInterface : InterfaceListener {

    override fun defineInterfaceListeners() {
        onOpen(FishingTrawlerContainer.INTERFACE_ID) { player, _ ->
            player.impactHandler.disabledTicks = 15
            return@onOpen true
        }

        /*
         * Handles close and drop items.
         */

        onClose(FishingTrawlerContainer.INTERFACE_ID) { player, _ ->
            val container = player.getAttribute<FishingTrawlerContainer>("ft-container")
            container?.close()
            player.removeAttribute("ft-container")
            sendMessage(player, core.tools.RED + "The remaining items have been dropped on the ground!")
            return@onClose true
        }

        /*
         * Handles withdraw for buttons.
         */

        on(FishingTrawlerContainer.INTERFACE_ID) { player, _, opcode, _, slot, _ ->
            val container = player.getAttribute<FishingTrawlerContainer>("ft-container") ?: return@on true

            when (opcode) {
                81 -> container.withdraw(slot, 1)
                206 -> {
                    val it = container[slot] ?: return@on true
                    container.withdraw(slot, it.amount)
                }
            }

            return@on true
        }
    }
}