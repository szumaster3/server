package content.region.karamja.dialogue.shilovillage

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class BlackPrismDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.YANNI_SALIKA_515)
        when (stage) {
            0 -> sendDialogue(player!!, "You show the black prism to Yanni.").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ah you'd like to sell this to me would you? I can offer you 5000 coins!",
                ).also { stage++ }

            2 -> {
                setTitle(player!!, 2)
                sendDialogueOptions(
                    player!!,
                    "SELL THE PRISM FOR 5000 COINS?",
                    "Yes, I'll sell it for 5000",
                    "No, I think I'll hold on it a while longer yet.",
                )
                stage++
            }

            3 ->
                when (buttonID) {
                    1 -> player("Yes, I'll sell it for 5000 coins.").also { stage = 5 }
                    2 -> player("No, I think I'll hold on it a while longer yet.").also { stage++ }
                }

            4 -> npc("Very well my friend, come back if you change", "your mind.").also { stage = END_DIALOGUE }
            5 -> npc("Very well my friend, let me count out your reward.").also { stage++ }
            6 -> {
                end()
                if (removeItem(player!!, Items.BLACK_PRISM_4808)) {
                    sendMessage(player!!, "You sell the black prism for 5000 coins.")
                    addItemOrDrop(player!!, Items.COINS_995, 5000)
                    npc("Thanks!")
                }
            }
        }
    }
}
