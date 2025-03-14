package content.region.kandarin.quest.hazeelcult.dialogue

import content.region.kandarin.quest.hazeelcult.handlers.HazeelCultListener
import core.api.getAttribute
import core.api.quest.getQuestStage
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HenryetaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        val questName = "Hazeel Cult"
        val questStage = getQuestStage(player!!, questName)

        when {
            (questStage == 0) ->
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Oh, hello. If you wish to look around the Carnillean family home please refrain from touching anything with those grubby hands of yours.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }

            (questStage in 0..2) ->
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Oh, hello. I'm afraid I'm not very presentable at the moment. I've been under incredible stress of late! It's ruining my complexion!",
                        ).also {
                            stage++
                        }
                    1 -> playerl(FaceAnim.FRIENDLY, "Why? What's wrong?").also { stage++ }
                    2 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "It's those awful cultists! I just don't feel safe here anymore!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }

            (questStage == 3) ->
                when (stage) {
                    0 -> {
                        if (getAttribute(player, HazeelCultListener.MAHJARRAT, true) &&
                            !getAttribute(player, HazeelCultListener.CARNILLEAN, true)
                        ) {
                            playerl(FaceAnim.FRIENDLY, "Hello.").also { stage++ }
                        } else {
                            sendMessage(
                                player,
                                "They aren't interested in talking to you.",
                            ).also { stage = END_DIALOGUE }
                        }
                    }

                    1 ->
                        npcl(
                            FaceAnim.SAD,
                            "Those hooligans! They slaughtered my precious Scruffy! I shall never recover! I am emotionally scarred for life!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    2 ->
                        playerl(
                            FaceAnim.NEUTRAL,
                            "Yeah... Hopefully someone finds them soon.",
                        ).also { stage = END_DIALOGUE }
                }

            (questStage == 100) ->
                when (stage) {
                    0 -> {
                        if (getAttribute(player, HazeelCultListener.MAHJARRAT, true) &&
                            !getAttribute(player, HazeelCultListener.CARNILLEAN, true)
                        ) {
                            playerl(FaceAnim.FRIENDLY, "Hello.").also { stage = 1 }
                        } else {
                            playerl(FaceAnim.FRIENDLY, "Hello.").also { stage = 3 }
                        }
                    }
                    1 ->
                        npcl(
                            FaceAnim.HALF_ASKING,
                            "Oh, it's you. I'm very disappointed that you never found those awful cultists. You're quite useless really, aren't you?",
                        ).also {
                            stage++
                        }
                    2 -> playerl(FaceAnim.HAPPY, "Yup, I guess I am.").also { stage = END_DIALOGUE }
                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Hello again adventurer! Since you dealt with those horrible hooligan cultists, things have vastly improved for us around here!",
                        ).also {
                            stage++
                        }
                    4 -> playerl(FaceAnim.HAPPY, "My pleasure, Lady Henryeta.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HENRYETA_CARNILLEAN_889)
    }
}
