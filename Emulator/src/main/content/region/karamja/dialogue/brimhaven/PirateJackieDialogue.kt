package content.region.karamja.dialogue.brimhaven

import content.region.karamja.dialogue.PirateJackieDiaryDialogue
import core.api.openDialogue
import core.api.openInterface
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.NPCs

class PirateJackieDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.PIRATE_JACKIE_THE_FRUIT_1055)
        when (stage) {
            0 -> playerl(FaceAnim.NEUTRAL, "Ahoy there!").also { stage++ }
            1 -> npcl(FaceAnim.NEUTRAL, "Ahoy!").also { stage++ }
            2 ->
                options(
                    "What is this place?",
                    "What do you do?",
                    "I'd like to trade in my tickets, please.",
                    "I have a question about my Achievement Diary.",
                    "See you later.",
                ).also {
                    stage++
                }

            3 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.NEUTRAL, "What is this place?").also { stage++ }
                    2 -> playerl(FaceAnim.NEUTRAL, "What do you do?").also { stage = 6 }
                    3 -> playerl(FaceAnim.NEUTRAL, "I'd like to trade in my tickets, please.").also { stage = 8 }
                    4 -> playerl(FaceAnim.NEUTRAL, "I have a question about my Achievement Diary.").also { stage = 9 }
                    5 -> playerl(FaceAnim.NEUTRAL, "See you later.").also { stage = END_DIALOGUE }
                }
            4 -> npcl(FaceAnim.NEUTRAL, "Welcome to the Brimhaven Agility Arena!").also { stage++ }
            5 ->
                npcl(FaceAnim.NEUTRAL, "If ye want to know more talk to Cap'n Izzy, he found it!").also {
                    stage =
                        END_DIALOGUE
                }
            6 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "I be the Jack o' tickets. I exchange the tickets ye collect in the Agility Arena for " +
                        "more stuff. Ye can obtain more agility experience or some items ye won't find anywhere else!",
                ).also { stage++ }
            7 -> playerl(FaceAnim.NEUTRAL, "Sounds good!").also { stage = END_DIALOGUE }
            8 -> {
                npcl(FaceAnim.NEUTRAL, "Aye, ye be on the right track.").also { stage = END_DIALOGUE }
                end()
                openInterface(player!!, Components.AGILITYARENA_TRADE_6)
            }

            9 -> {
                end()
                openDialogue(player!!, PirateJackieDiaryDialogue())
            }
        }
    }
}
