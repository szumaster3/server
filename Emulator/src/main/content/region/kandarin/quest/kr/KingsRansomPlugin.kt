package content.region.kandarin.quest.kr

import core.api.openInterface
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class KingsRansomPlugin : InteractionListener {

    override fun defineListeners() {
        on(Items.ADDRESS_FORM_11680, IntType.ITEM, "read") { player, _ ->
            openInterface(player, 586)
            return@on true
        }

        on(Items.SCRAP_PAPER_11681, IntType.ITEM, "read") { player, _ ->
            openInterface(player, 587)
            return@on true
        }
    }
}