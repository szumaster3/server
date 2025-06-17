package content.region.kandarin.feldip.gutanoth.plugin

import core.api.getStatLevel
import core.api.openDialogue
import core.api.sendItemSelect
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs.consts.NPCs

/**
 * Handles Bogrog npc.
 */
class BogrogPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles the "swap" interaction on the Bogrog NPC.
         */

        on(NPCs.BOGROG_4472, IntType.NPC, "swap") { player, _ ->
            sendMessage(player, "Pick the pouches and scrolls you wish to trade for shards.")
            openSwap(player)
            return@on true
        }

        /*
         * Handles talking to Bogrog NPC.
         */

        on(NPCs.BOGROG_4472, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, node.id, node)
            return@on true
        }

    }

    companion object {
        /**
         * Opens the pouch swap.
         */
        fun openSwap(player: Player) {
            if (getStatLevel(player, Skills.SUMMONING) < 21) {
                sendMessage(player, "You need a Summoning level of at least 21 in order to do that.")
                return
            }

            sendItemSelect(
                player,
                "Value",
                "Swap 1",
                "Swap 5",
                "Swap 10",
                "Swap X",
                "Swap All",
                "Examine",
                keepAlive = true
            ) { slot, index ->
                BogrogPouchUtils.handle(player, index, slot)
            }
        }
    }
}