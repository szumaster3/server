package content.global.skill.construction.decoration.skillhall.head

import core.api.sendNPCDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.world.GameWorld
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class KBDHeadDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 ->
                if (!player!!.houseManager.isInHouse(player!!)) {
                    playerl(FaceAnim.AFRAID, "Hey, House owner killed the King Black Dragon!").also { stage = 23 }
                } else {
                    playerl(FaceAnim.FRIENDLY, "How do you feel about all the more powerful monsters?").also { stage++ }
                }

            1 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.LEFT_HEAD_4231,
                    "There no monsters more powerful than us!",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage++ }

            2 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.MIDDLE_HEAD_4232,
                    "We top monster of all ${GameWorld.settings!!.name}!",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage++ }

            3 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "No you're not. The Kalphite Queen is more powerful than you!",
                ).also { stage++ }

            4 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.MIDDLE_HEAD_4232,
                    "Kalphite Queen? What that?",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage++ }

            5 -> playerl(FaceAnim.FRIENDLY, "She's a giant insect who lives in the desert.").also { stage++ }
            6 -> sendNPCDialogue(player!!, NPCs.MIDDLE_HEAD_4232, "Insect?", FaceAnim.CHILD_FRIENDLY).also { stage++ }
            7 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.RIGHT_HEAD_4233,
                    "Ha ha ha ha!",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage++ }

            8 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.LEFT_HEAD_4231,
                    "No insect tougher than us! We best!",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage++ }

            9 -> playerl(FaceAnim.FRIENDLY, "No, she's way tougher than you!").also { stage++ }
            10 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.LEFT_HEAD_4231,
                    "We no believe it!",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage++ }

            11 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.MIDDLE_HEAD_4232,
                    "Even if Kalphite Queen real, which we doubt, second best not bad, is it?",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage++ }

            12 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "But it's not just the Kalphite Queen. What about TzTok-Jad?",
                ).also { stage++ }

            13 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.LEFT_HEAD_4231,
                    "Never heard of it!",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage++ }

            14 -> playerl(FaceAnim.FRIENDLY, "Or Dagannoth Rex?").also { stage++ }
            15 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.RIGHT_HEAD_4233,
                    "You making it up!",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage++ }

            16 -> playerl(FaceAnim.FRIENDLY, "Or the Chaos Elemental?").also { stage++ }
            17 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.MIDDLE_HEAD_4232,
                    "Now then, how we know you not just making these monsters up to demoralise us?",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage++ }

            18 -> playerl(FaceAnim.FRIENDLY, "Alright, then, what about me?").also { stage++ }
            19 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.LEFT_HEAD_4231,
                    "Puny human! You not fearsome monster!",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage++ }

            20 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "I defeated you, didn't I? So, I must be stronger than you!",
                ).also { stage++ }

            21 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.LEFT_HEAD_4231,
                    "You got lucky! We get you next time!",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage++ }

            22 ->
                playerl(FaceAnim.FRIENDLY, "Now that you're just a stuffed head? I don't think so!").also {
                    stage = END_DIALOGUE
                }

            23 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.MIDDLE_HEAD_4232,
                    "No, he didn't?",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage++ }

            24 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.LEFT_HEAD_4231,
                    "What? Oh, ah, no. Course he didn't. We actually artificial likeness of King Black Dragon. No one could really kill King Black Dragon!",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage++ }

            25 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.MIDDLE_HEAD_4232,
                    "No! We...no, it far too powerful!",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage++ }

            26 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.RIGHT_HEAD_4233,
                    "What are you talking about? Of course we King Black Dragon!",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage++ }

            27 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.MIDDLE_HEAD_4232,
                    "Shut up, idiot!",
                    FaceAnim.CHILD_FRIENDLY,
                ).also { stage = END_DIALOGUE }
        }
    }
}
