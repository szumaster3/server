package content.region.desert.dialogue.alkharid

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AlKharidCitizens(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val randomDialogue = (1..15).random()
        when (randomDialogue) {
            1 -> npcl(FaceAnim.FRIENDLY, "Well. This beats doing the shopping!").also { stage = END_DIALOGUE }
            2 -> npcl(FaceAnim.FRIENDLY, "I wouldn't want to be the poor guy that has to clean up after the duels.").also { stage = 2 }
            3 -> npcl(FaceAnim.FRIENDLY, "Hi!").also { stage = END_DIALOGUE }
            4 -> npcl(FaceAnim.FRIENDLY, "What did the skeleton say before it ate?").also { stage = 4 }
            5 -> npcl(FaceAnim.FRIENDLY, "Ooh. This is exciting!").also { stage = 24 }
            6 -> npcl(FaceAnim.ASKING, "How can you make a very lively handkerchief?").also { stage = 6 }
            7 -> npcl(FaceAnim.ASKING, "Why did the skeleton burp?").also { stage = 8 }
            8 -> npcl(FaceAnim.HAPPY, "My son just won his first duel!").also { stage = 10 }
            9 -> npcl(FaceAnim.FRIENDLY, "Can't you see I'm watching the duels?").also { stage = END_DIALOGUE }
            10 -> npcl(FaceAnim.FRIENDLY, "Knock knock!").also { stage = 15 }
            11 -> npcl(FaceAnim.FRIENDLY, "Waaaaassssssuuuuupp?!").also { stage = END_DIALOGUE }
            12 -> npcl(FaceAnim.FRIENDLY, "Hi! I'm here to watch the duels!").also { stage = END_DIALOGUE }
            13 -> npcl(FaceAnim.FRIENDLY, "My favourite fighter is Mubariz!").also { stage = 19 }
            14 -> npcl(FaceAnim.NEUTRAL, "Hmph.").also { stage = END_DIALOGUE }
            15 -> npcl(FaceAnim.HALF_ASKING, "Did you know they think this place dates back to the second age?!").also { stage = 22 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            2 -> playerl(FaceAnim.FRIENDLY, "Me neither.").also { stage = END_DIALOGUE }
            4 -> playerl(FaceAnim.ASKING, "I don't know?").also { stage++ }
            5 -> npcl(FaceAnim.FRIENDLY, "Bone-appetit.").also { stage = END_DIALOGUE }
            6 -> playerl(FaceAnim.ASKING, "I don't know?").also { stage++ }
            7 -> npcl(FaceAnim.FRIENDLY, "Put a little boogey in it.").also { stage = END_DIALOGUE }
            8 -> playerl(FaceAnim.ASKING, "I don't know?").also { stage++ }
            9 -> npcl(FaceAnim.FRIENDLY, "'Cause it didn't have the guts to fart!").also { stage = END_DIALOGUE }
            10 -> playerl(FaceAnim.HAPPY, "Congratulations!").also { stage++ }
            11 -> npcl(FaceAnim.AMAZED, "He ripped his opponent in half!").also { stage++ }
            12 -> playerl(FaceAnim.FRIENDLY, "That's gotta hurt!").also { stage++ }
            13 -> npcl(FaceAnim.FRIENDLY, "He's only 10 as well!").also { stage++ }
            14 -> playerl(FaceAnim.FRIENDLY, "You gotta start 'em young!").also { stage = END_DIALOGUE }
            15 -> playerl(FaceAnim.FRIENDLY, "Who's there?").also { stage++ }
            16 -> npcl(FaceAnim.NEUTRAL, "Boo.").also { stage++ }
            17 -> playerl(FaceAnim.FRIENDLY, "Boo who?").also { stage++ }
            18 -> npcl(FaceAnim.LAUGH, "Don't cry, it's just me!").also { stage = END_DIALOGUE }
            19 -> playerl(FaceAnim.HALF_ASKING, "The guy at the information kiosk?").also { stage++ }
            20 -> npcl(FaceAnim.FRIENDLY, "Yeah! He rocks!").also { stage++ }
            21 -> playerl(FaceAnim.FRIENDLY, "Takes all sorts, I guess.").also { stage = END_DIALOGUE }
            22 -> playerl(FaceAnim.ASKING, "Really?").also { stage++ }
            23 -> npcl(FaceAnim.FRIENDLY, "Yeah. The guy at the information kiosk was telling me.").also { stage = END_DIALOGUE }
            24 -> playerl(FaceAnim.FRIENDLY, "Yup.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AlKharidCitizens(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.IMA_964, NPCs.SABEIL_965, NPCs.JADID_966, NPCs.DALAL_967, NPCs.AFRAH_968, NPCs.JEED_969)
    }
}
