package content.region.misc.dialogue.keldagrim

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class BlastFusionHammerDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.BLAST_FURNACE_FOREMAN_2553)
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "For that hammer I can offer you 1,000,000 coins for the trade-in. Do you accept?",
                ).also {
                    stage++
                }
            1 -> options("Yes, I'll take your offer.", "Sorry, I'll keep hold of the hammer for now.").also { stage++ }
            2 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Yes, I'll take your offer.").also { stage++ }
                    2 ->
                        playerl(FaceAnim.FRIENDLY, "Sorry, I'll keep hold of the hammer for now.").also {
                            stage = END_DIALOGUE
                        }
                }

            3 -> {
                end()
                if (!inInventory(player!!, Items.COINS_995) && freeSlots(player!!) == 0) {
                    sendMessage(player!!, "You don't have enough inventory space for this.")
                    return
                }
                if (!removeItem(player!!, Items.BLAST_FUSION_HAMMER_14478)) {
                    sendDialogue(player!!, "You don't have required item in your inventory.")
                } else {
                    sendItemDialogue(
                        player!!,
                        Items.BLAST_FUSION_HAMMER_14478,
                        "You hand over the hammer: Blast fusion hammer. and get 1,000,000 coins.",
                    )
                    addItemOrDrop(player!!, Items.COINS_995, 1000000)
                }
            }
        }
    }
}
