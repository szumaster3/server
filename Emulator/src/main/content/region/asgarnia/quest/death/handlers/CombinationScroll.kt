package content.region.asgarnia.quest.death.handlers

import core.api.openInterface
import core.api.sendString
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Components
import org.rs.consts.Items

class CombinationScroll : InteractionListener {
    companion object {
        fun combinationScroll(player: Player) {
            val combinationScroll =
                arrayOf(
                    "",
                    "Red is North of Blue. Yellow is South of Purple.",
                    "Green is North of Purple. Blue is West of",
                    "Yellow. Purple is East of Red.",
                    "",
                )
            sendString(player, combinationScroll.joinToString("<br>"), Components.BLANK_SCROLL_222, 4)
        }
    }

    override fun defineListeners() {
        on(Items.COMBINATION_3102, IntType.ITEM, "read") { player, _ ->
            openInterface(player, Components.BLANK_SCROLL_222).also { combinationScroll(player) }
            return@on true
        }
    }
}
