package content.global.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs

class ZombieHeadDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.ZOMBIE_HEAD_2868)
        when (stage) {
            0 ->
                options(
                    "How did you die?",
                    "What is your name?",
                    "Can you do any tricks?",
                    "Want a new hat?",
                    "Want to go scare some people?",
                ).also {
                    stage++
                }
            1 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.ASKING, "Hey, Head?").also { stage = 2 }
                    2 -> playerl(FaceAnim.ASKING, "Hey, Head?").also { stage = 7 }
                    3 -> playerl(FaceAnim.ASKING, "Hey, Head?").also { stage = 15 }
                    4 -> playerl(FaceAnim.ASKING, "Hey, Head?").also { stage = 27 }
                    5 -> playerl(FaceAnim.ASKING, "Hey, Head?").also { stage = 33 }
                }
            2 -> npcl(FaceAnim.FRIENDLY, "What?").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "How did you die?").also { stage++ }
            4 -> npcl(FaceAnim.FRIENDLY, "I stuck my neck out for an old friend.").also { stage++ }
            5 -> playerl(FaceAnim.FRIENDLY, "You shouldn't get so cut up about it.").also { stage++ }
            6 ->
                npcl(FaceAnim.FRIENDLY, "Well if I keep it all bottled up I'll turn into a total head case.").also {
                    stage =
                        0
                }
            7 -> npcl(FaceAnim.ANNOYED, "What?").also { stage++ }
            8 -> playerl(FaceAnim.FRIENDLY, "What is your name?").also { stage++ }
            9 -> npcl(FaceAnim.FRIENDLY, "Mumblemumblemumble...").also { stage++ }
            10 -> playerl(FaceAnim.HALF_ASKING, "What was that?").also { stage++ }
            11 -> npcl(FaceAnim.ANNOYED, "My name is Edward Cranium.").also { stage++ }
            12 -> playerl(FaceAnim.HALF_ASKING, "Edd Cranium?").also { stage++ }
            13 -> playerl(FaceAnim.LAUGH, "Hahahahahahahahahahaha!").also { stage++ }
            14 -> npcl(FaceAnim.FRIENDLY, "Har har...").also { stage = 0 }
            15 -> npcl(FaceAnim.ANGRY_WITH_SMILE, "What now?").also { stage++ }
            16 -> playerl(FaceAnim.FRIENDLY, "Can you do any tricks?").also { stage++ }
            17 -> npcl(FaceAnim.ANNOYED, "Not any more...").also { stage++ }
            18 -> playerl(FaceAnim.HALF_ASKING, "How come?").also { stage++ }
            19 ->
                npcl(
                    FaceAnim.ANNOYED,
                    "Because I used to be able to do a handstand for over an hour while juggling cannon balls with my feet...",
                ).also {
                    stage++
                }
            20 -> playerl(FaceAnim.FRIENDLY, "Wow, you were quite the entertainer.").also { stage++ }
            21 -> npcl(FaceAnim.ANNOYED, "Yep. Now I can barely roll my eyes...").also { stage++ }
            22 -> playerl(FaceAnim.FRIENDLY, "I know what you can do!").also { stage++ }
            23 -> npcl(FaceAnim.HALF_ASKING, "What?").also { stage++ }
            24 -> playerl(FaceAnim.FRIENDLY, "Vent...").also { stage++ }
            25 -> npcl(FaceAnim.ANNOYED, "Don't even suggest it!").also { stage++ }
            26 -> playerl(FaceAnim.FRIENDLY, "Ok.").also { stage = 0 }
            27 -> npcl(FaceAnim.ANGRY, "Can't I rest in peace?").also { stage++ }
            28 -> playerl(FaceAnim.FRIENDLY, "No!").also { stage++ }
            29 -> playerl(FaceAnim.HALF_ASKING, "Would you like a new hat?").also { stage++ }
            30 -> npcl(FaceAnim.ANGRY, "No, but could you screw a handle into the top of my head?").also { stage++ }
            31 -> playerl(FaceAnim.FRIENDLY, "A handle? Why?").also { stage++ }
            32 ->
                npcl(FaceAnim.ANNOYED, "Because currently you wave me about by my hair, and it hurts.").also {
                    stage =
                        0
                }
            33 -> npcl(FaceAnim.ROLLING_EYES, "Will you ever leave me alone?").also { stage++ }
            34 -> playerl(FaceAnim.HAPPY, "No!").also { stage++ }
            35 -> playerl(FaceAnim.HALF_ASKING, "Want to go scare some people?").also { stage++ }
            36 -> npcl(FaceAnim.OLD_NOT_INTERESTED, "Let's leave it for now.").also { stage++ }
            37 -> playerl(FaceAnim.ANNOYED, "All right...").also { stage++ }
            38 -> playerl(FaceAnim.ANNOYED, "We'll quit while we're ahead!").also { stage++ }
            39 ->
                npcl(FaceAnim.ANNOYED, "If I try really hard I might be able to will myself deader...").also {
                    stage =
                        0
                }
        }
    }
}
