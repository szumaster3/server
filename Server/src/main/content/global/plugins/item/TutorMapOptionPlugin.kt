package content.global.plugins.item

import core.api.openInterface
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Components
import shared.consts.Items

class TutorMapOptionPlugin: InteractionListener {

    private val newcomerMap = Items.NEWCOMER_MAP_550
    private val newcomerMapContent = Components.TUTOR_MAP_270

    override fun defineListeners() {
        on(newcomerMap, IntType.ITEM, "read") { player, _ ->
            openInterface(player, newcomerMapContent)
            return@on true
        }
    }
}
