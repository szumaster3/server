package content.minigame.blastfurnace.dialogue

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import core.tools.Log
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Blast Fusion Hammer dialogue.
 */
class BlastFusionHammerDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.BLAST_FURNACE_FOREMAN_2553)
        when (stage) {
            0 -> npcl(FaceAnim.OLD_DEFAULT, "For that hammer I can offer you 1,000,000 coins for the trade-in. Do you accept?").also { stage++ }
            1 -> options("Yes, I'll take your offer.", "Sorry, I'll keep hold of the hammer for now.").also { stage++ }
            2 -> when (buttonID) {
                1 -> playerl(FaceAnim.FRIENDLY, "Yes, I'll take your offer.").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "Sorry, I'll keep hold of the hammer for now.").also { stage = END_DIALOGUE
                }
            }

            3 -> {
                val coins = Items.COINS_995
                val hammer = Items.BLAST_FUSION_HAMMER_14478

                end()

                if (!inInventory(player!!, hammer)) {
                    sendMessage(player!!, "You don't have the required item in your inventory.")
                    return
                }

                if (!removeItem(player!!, hammer)) {
                    log(this.javaClass, Log.INFO, "[${player?.username}]: Failed to remove the $hammer from inventory.")
                    return
                }

                sendItemDialogue(player!!, hammer, "You hand over the Blast Fusion Hammer and receive 1,000,000 coins.")
                addItemOrBank(player!!, coins, 1_000_000)
            }
        }
    }
}
