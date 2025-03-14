package content.global.handlers.item.scroll

import core.api.openInterface
import core.api.sendString
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Components
import org.rs.consts.Items

class IorwerthsMessageScroll : InteractionListener {
    /*
     * Receive from the Lord Iorwerth after
     * killing King Tyras in Regicide quest.
     */

    private val kingsmessage = Components.KINGS_LETTER_V2_463
    private val iorwerthsscroll = Items.IORWERTHS_MESSAGE_3207

    override fun defineListeners() {
        on(iorwerthsscroll, IntType.ITEM, "read") { player, _ ->
            openInterface(player, kingsmessage)
            sendString(
                player,
                "Your Majesty King Lathas<br>Your man did well. The path is now open for the dark lord to enter this realm. You will yet live to see Camelot crushed under foot.",
                kingsmessage,
                1,
            )
            sendString(player, "Warmaster Iorwerth", kingsmessage, 2)
            return@on true
        }
    }
}
