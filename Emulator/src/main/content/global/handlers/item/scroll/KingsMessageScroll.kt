package content.global.handlers.item.scroll

import core.api.openInterface
import core.api.sendString
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Components
import org.rs.consts.Items

class KingsMessageScroll : InteractionListener {
    val kingsmessage = Components.KINGS_LETTER_V2_463
    val kingsscroll = Items.KINGS_MESSAGE_3206

    override fun defineListeners() {
        on(kingsscroll, IntType.ITEM, "read") { player, _ ->
            openInterface(player, kingsmessage)
            sendString(
                player,
                "Squire ${player.username}<br>You are needed to serve the Kingdom of Kandarin. At last our magi have reopened the well of voyage. We must discuss your next commission.",
                kingsmessage,
                1,
            )
            sendString(player, "<col=8A0808>His Majesty King Lathas</col>", kingsmessage, 2)
            return@on true
        }
    }
}
