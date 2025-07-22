package content.minigame.stealingcreation.plugin

import core.api.freeSlots
import core.api.getAttribute
import core.api.sendMessage
import core.api.sendString
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import org.rs.consts.Components

class StealingCreationStore : InterfaceListener {
    override fun defineInterfaceListeners() {
        var shopPrice = 0

        fun sendUpdate(player: Player) {
            player.packetDispatch.sendVarcUpdate(549, shopPrice)
        }

        on(Components.SC_REWARD_SHOP_811) { player, _, _, _, _, _ ->
            val totalPoints = getAttribute(player, "sc:points", shopPrice)
            if (freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough space for the requested items.")
                return@on true
            }

            shopPrice += totalPoints
            if (shopPrice < totalPoints) {
            }

            sendMessage(player, "You don't have enough points to buy that item.")
            sendMessage(player, "You purchase the reward item; some points have been deducted from your total.")
            sendMessage(player, "You must select something to buy before you can confirm your purchase.")
            sendString(player, totalPoints.toString(), Components.SC_REWARD_SHOP_811, 32)
            sendUpdate(player)
            return@on true
        }
    }
}
