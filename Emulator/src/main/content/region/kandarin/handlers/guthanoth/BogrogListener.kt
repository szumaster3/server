package content.region.kandarin.handlers.guthanoth

import core.api.getStatLevel
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs.consts.NPCs
import core.api.sendItemSelect
import core.api.sendMessage

class BogrogListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.BOGROG_4472, IntType.NPC, "swap") { player, _ ->
            openSwap(player)
            return@on true
        }
    }

    companion object {
        fun openSwap(player: Player) {
            if (getStatLevel(player, Skills.SUMMONING) < 21) {
                sendMessage(player, "You need a Summoning level of at least 21 in order to do that.")
                return
            }

            sendItemSelect(player, "Value", "Swap 1", "Swap 5", "Swap 10", "Swap X") { slot: Int?, index: Int? ->
                if (slot != null && index != null) {
                    BogrogPouchSwapper.handle(player, index, slot)
                }
            }
        }
    }
}
