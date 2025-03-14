package content.global.skill.construction.decoration.skillhall.head

import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class BasiliskHeadDialogue : DialogueFile() {
    private val randomWordA = arrayOf("boring", "fat", "hideous", "puny", "smelly", "stupid").random()
    private val randomWordB = arrayOf("beetle", "chicken", "egg", "mud", "slime", "worm").random()
    private val randomWordC = arrayOf("brained", "eating", "like", "loving", "smelling", "witted").random()
    private val randomWordD = arrayOf("basilisk", "idiot", "lizard", "mudworm", "slimeball", "weakling").random()

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.BASILISK_4228)
        when (stage) {
            0 -> npcl(FaceAnim.CHILD_FRIENDLY, "What do you want?").also { stage++ }
            1 ->
                if (!player!!.houseManager.isInHouse(player!!)) {
                    playerl(FaceAnim.AFRAID, "Oh, er, nothing!").also { stage = END_DIALOGUE }
                } else {
                    options(
                        "I want to mock you.",
                        "I want to apologise for killing you.",
                        "I just wanted to check that you're okay.",
                        "Nothing.",
                    ).also { stage++ }
                }

            2 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.FRIENDLY, "I want to mock you.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "I want to apologise for killing you.").also { stage = 6 }
                    3 -> playerl(FaceAnim.FRIENDLY, "I just wanted to check that you're okay.").also { stage = 30 }
                    4 -> playerl(FaceAnim.FRIENDLY, "Nothing.").also { stage = END_DIALOGUE }
                }

            3 -> npcl(FaceAnim.CHILD_FRIENDLY, "All right. Go on then.").also { stage++ }
            4 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "You're a $randomWordA $randomWordB $randomWordC $randomWordD.",
                ).also { stage++ }

            5 -> npcl(FaceAnim.CHILD_SAD, "I'm going back to sleep.").also { stage = END_DIALOGUE }
            6 -> npcl(FaceAnim.CHILD_FRIENDLY, "Go on then.").also { stage++ }
            7 -> playerl(FaceAnim.FRIENDLY, "I'm, um, very sorry I killed you.").also { stage++ }
            8 -> npcl(FaceAnim.CHILD_FRIENDLY, "Really sorry?").also { stage++ }
            9 -> options("No, not really.", "Yes, really.").also { stage++ }
            10 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.FRIENDLY, "No, not really!").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Yes really!").also { stage = 12 }
                }

            11 -> npcl(FaceAnim.CHILD_NORMAL, "I don't care.").also { stage = END_DIALOGUE }
            12 -> npcl(FaceAnim.CHILD_FRIENDLY, "Really really?").also { stage++ }
            13 -> options("Yes, really really.", "Don't push it.").also { stage++ }
            14 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Yes, really really!").also { stage = 16 }
                    2 -> playerl(FaceAnim.FRIENDLY, "Don't push it!").also { stage++ }
                }

            15 -> npcl(FaceAnim.CHILD_FRIENDLY, "I don't care anyway.").also { stage = END_DIALOGUE }
            16 -> npcl(FaceAnim.CHILD_FRIENDLY, "Fat lot of good that does, I'm still dead.").also { stage++ }
            17 ->
                options(
                    "I'm not THAT sorry.",
                    "But will you forgive me?",
                    "I promise not to do it again.",
                ).also { stage++ }

            18 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.FRIENDLY, "I'm not THAT sorry!").also { stage = 11 }
                    2 -> playerl(FaceAnim.FRIENDLY, "But will you forgive me?").also { stage = 19 }
                    3 -> playerl(FaceAnim.FRIENDLY, "I promise not to do it again.").also { stage = 22 }
                }

            19 -> npcl(FaceAnim.CHILD_FRIENDLY, "Of course I'll forgive you!").also { stage++ }
            20 -> playerl(FaceAnim.FRIENDLY, "Really?").also { stage++ }
            21 -> npcl(FaceAnim.CHILD_FRIENDLY, "No!").also { stage = END_DIALOGUE }
            22 ->
                npcl(
                    FaceAnim.CHILD_FRIENDLY,
                    "Of course you won't do it again, you can only kill me once.",
                ).also { stage++ }

            23 ->
                options(
                    "That's why I won't do it again.",
                    "But I won't do it to any other basilisks.",
                ).also { stage++ }

            24 ->
                when (buttonID) {
                    1 ->
                        playerl(FaceAnim.FRIENDLY, "That's why I won't do it again! There's be no point!").also {
                            stage = 15
                        }

                    2 -> playerl(FaceAnim.FRIENDLY, "But I won't do it to any other basilisks!").also { stage++ }
                }

            25 -> npcl(FaceAnim.CHILD_FRIENDLY, "Really?").also { stage++ }
            26 -> options("Yes, really!", "No, not really!", "Don't start that again!").also { stage++ }
            27 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Yes, really!").also { stage = 29 }
                    2 -> playerl(FaceAnim.FRIENDLY, "No, not really!").also { stage = 11 }
                    3 -> player(FaceAnim.FRIENDLY, "Don't start that again!").also { stage++ }
                }

            28 -> npcl(FaceAnim.CHILD_FRIENDLY, "Leave me alone then.").also { stage = END_DIALOGUE }
            29 ->
                npcl(FaceAnim.CHILD_FRIENDLY, "All right then. Apology accepted. Now leave me alone.").also {
                    stage = END_DIALOGUE
                }

            30 ->
                npcl(
                    FaceAnim.CHILD_FRIENDLY,
                    "Apart from being dead and stuffed and hanging on a wall, you mean?",
                ).also { stage++ }

            31 -> playerl(FaceAnim.FRIENDLY, "Uh... yeah, apart from that are you okay?").also { stage++ }
            32 ->
                npcl(
                    FaceAnim.CHILD_FRIENDLY,
                    "Actually there's something blocking my view of the far wall.",
                ).also { stage++ }

            33 -> playerl(FaceAnim.FRIENDLY, "I don't see anything.").also { stage++ }
            34 -> npcl(FaceAnim.CHILD_FRIENDLY, "Perhaps if you were to move to one side of me.").also { stage++ }
            35 -> sendDialogue(player!!, "You walk to the side of the basilisk head...").also { stage++ }
            36 -> playerl(FaceAnim.FRIENDLY, "I still don't see anything.").also { stage++ }
            37 -> npcl(FaceAnim.CHILD_FRIENDLY, "Oh, it's moved away. I can see now.").also { stage = END_DIALOGUE }
        }
    }
}
