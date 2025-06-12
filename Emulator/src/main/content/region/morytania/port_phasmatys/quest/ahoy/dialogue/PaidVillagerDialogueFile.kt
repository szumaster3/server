package content.region.morytania.port_phasmatys.quest.ahoy.dialogue

import content.region.morytania.quest.ahoy.plugin.GhostsAhoyUtils.collectSignature
import core.api.amountInInventory
import core.api.removeItem
import core.game.dialogue.DialogueFile
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class PaidVillagerDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val tokens = amountInInventory(player!!, Items.ECTO_TOKEN_4278)
        npc = NPC(NPCs.GHOST_VILLAGER_1697)
        when (stage) {
            0 -> player("how much?").also { stage++ }
            1 -> npc("Oh, it'll cost you 1 ecto-tokens.").also { stage++ }
            2 ->
                if (tokens < 1) {
                    player("I don't have that many on me.").also { stage++ }
                } else {
                    options("Okay, if you insist.", "There's no way I'm giving in to corruption.").also { stage = 4 }
                }

            3 -> npc("No tokens, no signature.").also { stage = END_DIALOGUE }
            4 ->
                when (buttonID) {
                    1 -> player("Okay, if you insist.").also { stage = 6 }
                    2 -> player("There's no way I'm giving in to corruption.").also { stage++ }
                }

            5 -> npc("Suit yourself.").also { stage = END_DIALOGUE }
            6 -> player("Okay, if you insist.").also { stage++ }
            7 ->
                if (removeItem(player!!, Item(Items.ECTO_TOKEN_4278, 1))) {
                    end()
                    collectSignature(player!!)
                }
        }
    }
}
