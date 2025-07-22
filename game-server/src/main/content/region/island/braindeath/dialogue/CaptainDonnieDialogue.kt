package content.region.island.braindeath.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * Represents the Captain Doonie dialogue on Braindeath Island.
 */
@Initializable
class CaptainDonnieDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_ASKING, "Arr! What be ye wantin?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val ladLass = if (player!!.isMale) "lad" else "lass"
        when(stage) {
            0 -> player(FaceAnim.STRUGGLE, "I, err, came to...").also { stage++ }
            1 -> showTopics(
                Topic("Tell you to leave.", 2),
                Topic("Ask what you wanted.", 11),
                Topic("Join your crew.", 17)
            )
            // Tell you to leave.
            2 -> playerl(FaceAnim.NEUTRAL, "I have come to tell you to leave.").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "Ye have?").also { stage++ }
            4 -> playerl(FaceAnim.NEUTRAL, "Yes.").also { stage++ }
            5 -> npcl(FaceAnim.LAUGH, "Hahahahahahahahahahahahaha!").also { stage++ }
            6 -> npcl(FaceAnim.LOUDLY_LAUGHING, "Bwahahahahahahahahahahahaha!").also { stage++ }
            7 -> npcl(FaceAnim.HALF_THINKING, "Wheeze...wheeze... Gadzooks $ladLass, that be the funniest thing I've heard all day! Say it again!").also { stage++ }
            8 -> playerl(FaceAnim.NEUTRAL, "I have come to tell you to leave.").also { stage++ }
            9 -> npcl(FaceAnim.LAUGH, "Stop $ladLass! I'll shatter me ribs!").also { stage++ }
            10 -> end()
            // Ask what you wanted.
            11 -> playerl(FaceAnim.NEUTRAL, "I've come to ask you what you want.").also { stage++ }
            12 -> npcl(FaceAnim.HALF_ASKING, "Whadda we want? 'Rum'! When do we want it? Now!").also { stage++ }
            13 -> playerl(FaceAnim.ASKING, "So...if I were to give you 'rum' you would leave?").also { stage++ }
            14 -> npcl(FaceAnim.FRIENDLY, "Not really $ladLass. If ye were to give us 'rum' we'd kill ye quickly, as opposed to over a few weeks.").also { stage++ }
            15 -> playerl(FaceAnim.NEUTRAL, "Oh...").also { stage++ }
            16 -> end()
            // Join your crew.
            17 -> playerl(FaceAnim.NEUTRAL, "Err, I mean...").also { stage++ }
            18 -> playerl(FaceAnim.NEUTRAL, "Arr! Shiver me mainbraces and make them landlubbers walk the scurvy plank, Cap'n! I've come to join yer cut- throat, bilge swillin' crew!").also { stage++ }
            19 -> playerl(FaceAnim.NEUTRAL, "Also, arr!").also { stage++ }
            20 -> npcl(FaceAnim.FRIENDLY, "Are ye quite done, $ladLass?").also { stage++ }
            21 -> playerl(FaceAnim.NEUTRAL, "Yes, for the time being anyway.").also { stage++ }
            22 -> npcl(FaceAnim.FRIENDLY, "Well, ye'll be glad to know that after that little performance I'd be glad to have ye on me crew!").also { stage++ }
            23 -> playerl(FaceAnim.NEUTRAL, "Huzzah!").also { stage++ }
            24 -> npcl(FaceAnim.FRIENDLY, "Course, I'll have te kill ye first.").also { stage++ }
            25 -> playerl(FaceAnim.NEUTRAL, "Oh...").also { stage++ }
            26 -> npcl(FaceAnim.FRIENDLY, "Don't ye worry, $ladLass. After we take the island I'll have the boss haul yer body to the temple and...").also { stage++ }
            27 -> npcl(FaceAnim.FRIENDLY, "Err, never mind.").also { stage++ }
            28 -> playerl(FaceAnim.NEUTRAL, "Never mind what?").also { stage++ }
            29 -> npcl(FaceAnim.FRIENDLY, "Ferget I said anything.").also { stage++ }
            30 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = CaptainDonnieDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.CAPTAIN_DONNIE_2830)
}