package content.global.travel.balloon

import core.api.openInterface
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class AssistantDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val faceExpression = if (npc.id != 5056) FaceAnim.HALF_GUILTY else FaceAnim.OLD_NORMAL
        npcl(
            faceExpression,
            "Do you want to use the balloon? Just so you know, some locations require special logs and high Firemaking skills.",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val faceExpression = if (npc.id != 5056) FaceAnim.HALF_GUILTY else FaceAnim.OLD_NORMAL
        when (stage) {
            0 ->
                if (npc.id != 5050) {
                    options("Yes.", "No.", "Who are you?").also { stage++ }
                } else {
                    options("Yes.", "No.").also { stage++ }
                }

            1 ->
                when (buttonId) {
                    1 -> {
                        if (!isQuestComplete(player!!, Quests.ENLIGHTENED_JOURNEY)) {
                            npcl(
                                faceExpression,
                                "Oh, Sorry...You must complete Enlightened Journey before you can use it.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                        } else {
                            player("Yes.").also { stage = 6 }
                        }
                    }

                    2 -> player("No.").also { stage = END_DIALOGUE }
                    3 -> player("Who are you?").also { stage++ }
                }

            2 -> {
                when (npc.id) {
                    NPCs.ASSISTANT_SERF_5053 ->
                        npcl(
                            faceExpression,
                            "I am a Serf. Assistant Serf to you! Auguste freed me and gave me this job.",
                        ).also {
                            stage++
                        }
                    NPCs.ASSISTANT_MARROW_5055 ->
                        npcl(
                            faceExpression,
                            "I am Assistant Marrow. I'm working here part time while I study to be a doctor.",
                        ).also {
                            stage++
                        }
                    NPCs.ASSISTANT_LE_SMITH_5056 ->
                        npcl(
                            faceExpression,
                            "I am Assistant Le Smith. I used to work as a glider pilot, but they kicked me off.",
                        ).also {
                            stage =
                                7
                        }
                    NPCs.ASSISTANT_STAN_5057 ->
                        npcl(
                            faceExpression,
                            "I am Stan. Auguste hired me to look after this balloon. I make sure people are prepared to fly.",
                        ).also {
                            stage++
                        }
                    5065 ->
                        npcl(
                            faceExpression,
                            "I am Assistant Brock. I serve under Auguste as his number two assistant.",
                        ).also {
                            stage++
                        }
                }
            }

            3 -> npcl(faceExpression, "Do you want to use the balloon?").also { stage++ }
            4 -> options("Yes.", "No.").also { stage++ }
            5 ->
                when (buttonId) {
                    1 -> {
                        if (!isQuestComplete(player!!, Quests.ENLIGHTENED_JOURNEY)) {
                            npcl(
                                faceExpression,
                                "Oh, Sorry...You must complete ${Quests.ENLIGHTENED_JOURNEY} before you can use it.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                        } else {
                            player("Yes.").also { stage = 6 }
                        }
                    }

                    2 -> player("No.").also { stage = END_DIALOGUE }
                }

            6 -> {
                end()
                openInterface(player, Components.ZEP_BALLOON_MAP_469)
                HotAirBalloonListener.showBalloonLocation(player!!, npc)
            }

            7 -> playerl(FaceAnim.FRIENDLY, "Why?").also { stage++ }
            8 -> npcl(faceExpression, "They said I was too full of hot air.").also { stage = 3 }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.AUGUSTE_5050,
            NPCs.ASSISTANT_SERF_5053,
            NPCs.ASSISTANT_MARROW_5055,
            NPCs.ASSISTANT_LE_SMITH_5056,
            NPCs.ASSISTANT_STAN_5057,
            5065,
        )
    }
}
