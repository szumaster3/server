package content.region.kandarin.feldip.gutanoth.plugin

import core.api.getStatLevel
import core.api.sendItemSelect
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import shared.consts.NPCs

/**
 * Handles Bogrog npc.
 */
class BogrogPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles the "swap" interaction on the Bogrog NPC.
         */

        on(NPCs.BOGROG_4472, IntType.NPC, "swap") { player, _ ->
            openSwap(player)
            return@on true
        }
    }

    companion object {
        /**
         * Opens the pouch swap.
         */
        fun openSwap(player: Player) {
            if(player.inCombat()) {
                sendMessage(player, "You can't swap while in combat.")
                return
            }

            if (getStatLevel(player, Skills.SUMMONING) < 21) {
                sendMessage(player, "You need a Summoning level of at least 21 in order to do that.")
                return
            }

            sendMessage(player, "Pick the pouches and scrolls you wish to trade for shards.")
            sendItemSelect(player, "Value", "Swap 1", "Swap 5", "Swap 10", "Swap X", "Swap All", "Examine", keepAlive = true) { slot, index ->
                BogrogPouchUtils.handle(player, index, slot)
            }
        }
    }
}