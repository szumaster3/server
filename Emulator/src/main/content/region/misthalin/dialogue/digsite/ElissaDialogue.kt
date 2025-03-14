package content.region.misthalin.dialogue.digsite

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class ElissaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    var qb = 2

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "Hello there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> (options("What do you do here?", "What is this place?")).also { stage++ }
            1 ->
                when (qb) {
                    1 -> player(FaceAnim.ASKING, "What do you do here?").also { stage = 10 }
                    2 -> player(FaceAnim.ASKING, "What is this place?").also { stage = 20 }
                }
            10 ->
                npcl(FaceAnim.FRIENDLY, "I am helping with the dig. I am an expert on Third Age architecture.").also {
                    stage =
                        99
                }
            20 ->
                npcl(
                    FaceAnim.ANNOYED,
                    "In the Third Age, this was a great city, which would have put Varrock to shame!",
                ).also {
                    stage++
                }
            21 ->
                options(
                    "I don't know, Varrock is pretty impressive.",
                    "What happened to the city?",
                ).also { stage++ }
            22 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "I don't know, Varrock is pretty impressive.").also { stage = 30 }
                    2 -> player(FaceAnim.ASKING, "What happened to the city?").also { stage = 40 }
                }
            30 ->
                npcl(
                    FaceAnim.ANNOYED,
                    "Hmph. I don't think it will look this good when it's been buried in the ground for three thousand years!",
                ).also {
                    stage =
                        99
                }
            40 -> npc(FaceAnim.ANNOYED, "No one knows for sure.").also { stage++ }
            41 ->
                npcl(
                    FaceAnim.ANNOYED,
                    "But the The Third Age was a time of destruction, when the gods were violently at war. Many great civilizations were destroyed then.",
                ).also {
                    stage =
                        99
                }
            50 ->
                options(
                    "What do you do here?",
                    "What is this place?",
                    "I found a letter in the desert with your name on.",
                ).also { stage++ }
            51 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "What do you do here?").also { stage = 10 }
                    2 -> player(FaceAnim.ASKING, "What is this place?").also { stage = 20 }
                    3 ->
                        playerl(FaceAnim.FRIENDLY, "I found a letter in the desert with your name on.").also {
                            stage =
                                60
                        }
                }
            60 -> npc(FaceAnim.AMAZED, "Ah, so you've found the ruins of Uzer.").also { stage++ }
            61 ->
                npcl(
                    FaceAnim.AMAZED,
                    "I wrote that letter to my late husband when he was exploring there.",
                ).also { stage++ }
            62 ->
                npcl(
                    FaceAnim.AMAZED,
                    "That was a great city as well, but the museum could only fund one excavation and this one was closer to home.",
                ).also {
                    stage++
                }
            63 ->
                npcl(
                    FaceAnim.AMAZED,
                    "If you're interested in his expedition, the notes he made are in the library in the Exam Centre.",
                ).also {
                    stage =
                        99
                }
            70 ->
                options(
                    "What do you do here?",
                    "What is this place?",
                    "Where did you say the notes were?",
                ).also { stage++ }
            71 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "What do you do here?").also { stage = 10 }
                    2 -> player(FaceAnim.ASKING, "What is this place?").also { stage = 20 }
                    3 -> playerl(FaceAnim.FRIENDLY, "Where did you say the notes were?").also { stage = 80 }
                }
            80 -> npc(FaceAnim.FRIENDLY, "They're on a bookcase in the Exam Centre.").also { stage = 99 }
            90 ->
                options(
                    "What do you do here?",
                    "What is this place?",
                    "Where is the statuette that Varmen took back from Uzer?",
                ).also { stage++ }
            91 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "What do you do here?").also { stage = 10 }
                    2 -> player(FaceAnim.ASKING, "What is this place?").also { stage = 20 }
                    3 ->
                        playerl(FaceAnim.FRIENDLY, "Where is the statuette that Varmen took back from Uzer?").also {
                            stage =
                                100
                        }
                }
            100 -> npc(FaceAnim.ASKING, "The statuette? Oh, yes...").also { stage++ }
            101 ->
                npcl(
                    FaceAnim.ANNOYED,
                    "That statuette was the only thing we had to show from that expedition.",
                ).also { stage++ }
            102 ->
                npcl(
                    FaceAnim.ASKING,
                    "It was very worn, but you can still make out a lot of detail. The Uzerians were expert sculptors. It's a pity we only have that small example.",
                ).also {
                    stage++
                }
            103 -> npc(FaceAnim.ASKING, "Now it's on display in the Varrock museum.").also { stage = 99 }
            99 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ELISSA_1912)
    }
}
