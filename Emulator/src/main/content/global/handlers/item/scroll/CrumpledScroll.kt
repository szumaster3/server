package content.global.handlers.item.scroll

import core.api.openInterface
import core.api.sendString
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Components
import org.rs.consts.Items

class CrumpledScroll : InteractionListener {
    companion object {
        private const val MESSAGE_SCROLL = Components.BLANK_SCROLL_222
        val crumpledScrollContent =
            arrayOf(
                "Rashiliyia's rage went unchecked. She killed",
                "",
                "without mercy for revenge of her son's life. Like",
                "",
                "a spectre through the night she entered houses",
                "",
                "and one by one quietly strangled life from the",
                "",
                "occupants. It is said that only a handful survived,",
                "",
                "protected by the necklace wards to keep the Witch",
                "",
                "Queen at bay.",
            )
    }

    override fun defineListeners() {
        on(Items.CRUMPLED_SCROLL_608, IntType.ITEM, "read") { player, _ ->
            openInterface(player, MESSAGE_SCROLL)
            sendString(player, crumpledScrollContent.joinToString("<br>"), MESSAGE_SCROLL, 2)
            return@on true
        }
    }
}
