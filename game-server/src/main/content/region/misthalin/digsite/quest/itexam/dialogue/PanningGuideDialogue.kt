package content.region.misthalin.digsite.quest.itexam.dialogue

import content.region.misthalin.digsite.quest.itexam.TheDigSite
import core.api.getAttribute
import core.api.inInventory
import core.api.removeItem
import core.api.setAttribute
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class PanningGuideDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.PANNING_GUIDE_620)
        when (stage) {
            0 ->
                when {
                    getAttribute(player!!, TheDigSite.attributePanningGuideTea, false) -> {
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Ah! Lovely! You can't beat a good cuppa! You're free to pan all you want!",
                        ).also { stage = 10 }
                    }

                    else -> {
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Hello, I am the panning guide. I teach students how to pan in these waters.",
                        ).also { stage = 1 }
                    }
                }

            1 -> playerl(FaceAnim.FRIENDLY, "So, how do I become invited?").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I'm not supposed to let people pan here unless they have permission. Mind you, I could let you have a go if you're willing to do me a favour...",
                ).also { stage++ }

            3 -> playerl(FaceAnim.FRIENDLY, "What's that?").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well, to be honest, what I would really like is a nice cup of tea!",
                ).also { stage++ }

            5 ->
                if (inInventory(player!!, Items.CUP_OF_TEA_712)) {
                    playerl(FaceAnim.FRIENDLY, "I've some here that you can have.").also {
                        stage = 7
                    }
                } else {
                    playerl(FaceAnim.FRIENDLY, "Tea?").also { stage++ }
                }

            6 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Absolutely, I'm parched! If you could bring me one of those, I would be more than willing to let you pan here.",
                ).also { stage = END_DIALOGUE }

            7 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ah! Lovely! You can't beat a good cuppa! You're free to pan all you want!",
                ).also {
                    if (removeItem(player!!, Items.CUP_OF_TEA_712)) {
                        setAttribute(player!!, TheDigSite.attributePanningGuideTea, true)
                    }
                    end()
                }
            }

            8 -> npcl(FaceAnim.ANNOYED, "Hey! You can't pan yet!").also { stage++ }
            9 -> playerl(FaceAnim.THINKING, "Why not?").also { stage++ }
            10 -> npcl(FaceAnim.FRIENDLY, "We do not allow the uninvited to pan here.").also { stage++ }
            11 -> options("OK, forget it.", "So how do I become invited then?").also { stage++ }
            12 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.FRIENDLY, "OK, forget it.").also { stage = END_DIALOGUE }
                    2 -> playerl(FaceAnim.FRIENDLY, "So how do I become invited then?").also { stage = 5 }
                }
        }
    }
}
