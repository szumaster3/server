package content.region.kandarin.quest.arena.dialogue

import core.api.quest.getQuestStage
import core.api.sendNPCDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class JustinServilDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (getQuestStage(player!!, Quests.FIGHT_ARENA)) {
            in 0..68 ->
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hello.").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "You've incurred the anger of Khazard himself. He won't let any of us go easily.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }

            in 69..71 ->
                when (stage) {
                    0 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Lady Servil sent me to rescue you and your son. Come on, we have to get out of here.",
                        ).also {
                            stage++
                        }
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I'm too old to fight. I'm afraid you'll have to kill that ogre by yourself.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }

            72 ->
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Are you alright").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "You saved my life and my son's. I am eternally in your debt brave traveller.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }

            in 73..89 ->
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Don't worry, I'll get us out of here.").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "You've incurred the anger of Khazard yourself now. He won't let any of us go easily.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }

            in 90..97 ->
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "You can run, Khazard's interest is in me now.").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "My thanks, we'll be ok now. I suggest you run too, General Khazard is mighty and his anger fearsome.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }

            in 98..99 ->
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "You saved my life and my son's, I am eternally in your debt brave taveller.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }

            100 ->
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Thank you again, ${player!!.username}. You have saved me and my boy from an awful fate.",
                        ).also {
                            stage++
                        }
                    1 ->
                        sendNPCDialogue(
                            player!!,
                            NPCs.JEREMY_SERVIL_266,
                            "Yeah, you were ace. I wanna be jus' like you.",
                        ).also { stage++ }
                    2 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "If you do well in your studies and work hard, maybe you will be.",
                        ).also { stage++ }
                    3 ->
                        sendNPCDialogue(
                            player!!,
                            NPCs.JEREMY_SERVIL_266,
                            "Daaad, studying's so boooooooring! I wanna hit things.",
                        ).also {
                            stage++
                        }
                    4 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I'm sure ${player!!.username} studied hard when he was younger.",
                        ).also { stage++ }
                    5 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "That's right, son, you should do as your father says. Anyway, I should go now, have a safe journey home. And you, young lad, work hard and stay out of trouble - at least until you're a little older.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JUSTIN_SERVIL_267)
    }
}
