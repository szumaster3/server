package content.region.kandarin.quest.grandtree.dialogue

import content.region.kandarin.quest.grandtree.handlers.TheGrandTreeUtils
import core.api.*
import core.api.quest.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class FemiGTDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.FEMI_676)
        when (stage) {
            0 -> player("I can't believe they won't let me in!").also { stage++ }
            1 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "I don't believe all this rubbish about an invasion. If",
                    "mankind wanted to, they could have invaded before",
                    "now.",
                ).also {
                    stage++
                }

            2 -> player("I really need to see King Narnode. Could you help", "sneak me in?").also { stage++ }
            3 ->
                if (getAttribute(player!!, TheGrandTreeUtils.FEMI_HELP_TRUE, false)) {
                    npc(
                        FaceAnim.OLD_DEFAULT,
                        "Well, as you helped me I suppose I could. We'll have to",
                        "be careful. If I get caught I'll be in the cage!",
                    ).also {
                        stage++
                    }
                } else {
                    npc(FaceAnim.OLD_DEFAULT, "Why should I help you, you wouldn't help me!").also { stage = 10 }
                }

            4 -> player("OK, what should I do?").also { stage++ }
            5 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Jump in the back of the cart.",
                    "It's a food delivery, we should be fine",
                ).also {
                    stage =
                        17
                }

            10 -> player("Erm I know, but this is an emergency!").also { stage++ }
            11 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "So was lifting that barrel! Tell you what, call",
                    "it a round 1000 gold pieces.",
                ).also {
                    stage++
                }

            12 -> player("1000 gold pieces!").also { stage++ }
            13 -> npc(FaceAnim.OLD_DEFAULT, "That's right, 1000 and I'll sneak you in.").also { stage++ }
            14 -> options("No chance!", "OK then, here you go.").also { stage++ }
            15 ->
                when (buttonID) {
                    1 -> end()
                    2 -> {
                        if (!removeItem(player!!, Item(Items.COINS_995, 1000))) {
                            sendDialogue(player!!, "You don't have enough coins.").also { stage = END_DIALOGUE }
                        } else {
                            sendItemDialogue(player!!, Items.COINS_6964, "You give Femi 1000 coins.")
                            stage++
                        }
                    }
                }

            16 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Alright, jump in the back of the cart.",
                    "It's a food delivery, we should be fine.",
                ).also {
                    stage++
                }

            17 -> {
                end()
                TheGrandTreeUtils.sneakIn(player!!)
            }
        }
    }
}

class FemiCartDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.FEMI_676)
        when (stage) {
            0 -> {
                if (getQuestStage(player!!, Quests.THE_GRAND_TREE) == 55) {
                    npc(FaceAnim.OLD_DEFAULT, "OK traveller, you'd better get going.").also { stage++ }
                } else {
                    npc(FaceAnim.OLD_DEFAULT, "Thanks again, traveller!").also { stage = END_DIALOGUE }
                }
            }

            1 -> player("Thanks again!").also { stage++ }
            2 -> npc(FaceAnim.OLD_DEFAULT, "That's OK, all the best.").also { stage = END_DIALOGUE }
        }
    }
}
