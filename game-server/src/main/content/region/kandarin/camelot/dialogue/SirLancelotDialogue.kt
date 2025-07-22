package content.region.kandarin.camelot.dialogue

import core.api.getQuestStage
import core.api.hasRequirement
import core.api.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents Sir Lancelot dialogue.
 */
@Initializable
class SirLancelotDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.FRIENDLY, "Greetings! I am Sir Lancelot, the greatest Knight in the land! What do you want?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val merlinStage = getQuestStage(player, Quests.MERLINS_CRYSTAL)
        val kingRansomRequirements = hasRequirement(player, Quests.KINGS_RANSOM, false)

        when (stage) {
            0 -> {
                // Non-quest dialogue: After all quests.
                if (kingRansomRequirements) {
                    npcl(FaceAnim.JOLLY, "Humph. You are indeed a better knight than I first suspected.").also {
                        stage = 1
                    }
                    return true
                }
                // Holy Grail: Searching for a quest.
                if (merlinStage == 100) {
                    npcl(FaceAnim.NEUTRAL, "Hmmm. I heard you freed Merlin. Either you're better than you look or you got lucky. I think the latter.").also { stage = END_DIALOGUE }
                } else {
                    playerl(FaceAnim.HAPPY, "I am questing for the Holy Grail.").also { stage = 11 }
                }

                showTopics(
                    // Merlin Crystal: Investigating how to free Merlin.
                    IfTopic("I want to get Merlin out of the crystal.", 6, merlinStage != 100),
                    // Non-quest dialogue: Default.
                    Topic(FaceAnim.NEUTRAL, "You're a little full of yourself aren't you?", 4),
                    // Merlin Crystal: Searching for a quest.
                    IfTopic("I seek a quest!", 2, merlinStage == 0),
                    // Merlin Crystal: Investigating how to free Merlin: Learning how to enter Keep Le Faye.
                    IfTopic("Any ideas on how to get into Morgan Le Faye's stronghold?", 3, merlinStage == 20)
                )
            }

            // Non-quest dialogue: After all quests.
            1 -> playerl(FaceAnim.STRUGGLE, "Gee, it was nothing saving you all from the clutches of Morgan Le Faye and freeing your King.").also { stage = END_DIALOGUE }

            // Merlin Crystal: Searching for a quest.
            2 -> npcl(FaceAnim.NEUTRAL, "Leave questing to the professionals.").also { stage++ }
            3 -> npcl(FaceAnim.NEUTRAL, "Such as myself.").also { stage = END_DIALOGUE }

            // Non-quest dialogue: Default.
            4 -> npcl(FaceAnim.FRIENDLY, "I have every right to be proud of myself.").also { stage++ }
            5 -> npcl(FaceAnim.FRIENDLY, "My prowess in battle is world renowned!").also { stage = END_DIALOGUE }

            // Merlin Crystal: Investigating how to free Merlin.
            6 -> npc(FaceAnim.STRUGGLE, "Well, if the Knights of the Round Table can't manage", "it, I can't see how a commoner like you could succeed", "where we have failed.").also { stage = END_DIALOGUE }

            // Merlin Crystal: Investigating how to free Merlin: Learning how to enter Keep Le Faye.
            7 -> if (merlinStage >= 30) {
                npc(FaceAnim.STRUGGLE, "Well, if the Knights of the Round Table can't manage", "it, I can't see how a commoner like you could succeed", "where we have failed.").also { stage = END_DIALOGUE }
            } else {
                npcl(FaceAnim.NEUTRAL, "That stronghold is built in a strong defensive position.").also { stage++ }
            }

            8 -> npcl(FaceAnim.NEUTRAL, "It's on a big rock sticking out into the sea.").also { stage++ }
            9 -> npcl(FaceAnim.NEUTRAL, "There are two ways in that I know of, the large heavy front doors, and the sea entrance, only penetrable by boat.").also { stage++ }

            10 -> {
                end()
                npcl(FaceAnim.NEUTRAL, "They take all their deliveries by boat.")
                setQuestStage(player!!, Quests.MERLINS_CRYSTAL, 30)
            }

            // Holy Grail: Searching for a quest.
            11 -> npcl(FaceAnim.LAUGH, "The Grail? Ha! Frankly, little man, you're not in that league.").also { stage++ }

            12 -> player(FaceAnim.HALF_ASKING, "Why do you say that?").also { stage++ }
            13 -> npcl(FaceAnim.FRIENDLY, "You got lucky with freeing Merlin but there's no way a puny wannabe like you is going to find the Holy Grail where so many others have failed.").also { stage++ }

            14 -> playerl(FaceAnim.CALM, "We'll see about that.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = SirLancelotDialogue(player)
    override fun getIds(): IntArray = intArrayOf(NPCs.SIR_LANCELOT_239)
}
