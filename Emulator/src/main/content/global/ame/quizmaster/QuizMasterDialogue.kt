package content.global.ame.quizmaster

import core.ServerConstants
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the Quizmaster dialogue.
 * @author ovenbreado
 */
class QuizMasterDialogue : DialogueFile() {
    companion object {
        const val QUIZMASTER_INTERFACE = Components.MACRO_QUIZSHOW_191
        const val QUIZMASTER_ATTRIBUTE_RETURN_LOC = "/save:original-loc"
        const val QUIZMASTER_ATTRIBUTE_QUESTIONS_CORRECT = "/save:quizmaster:questions-correct"
        const val QUIZMASTER_ATTRIBUTE_RANDOM_ANSWER = "quizmaster:random-answer"

        /*
            NULL_6192 BATTLEAXE
            NULL_6189 TROUT
            NULL_6190 TUNA
            NULL_6198 NECKLACE
            NULL_6194 SHIELD
            NULL_6193 HELM
            NULL_6197 RING
            NULL_6195 SECATEURS
            NULL_6191 SWORD
            NULL_6196 SPADE
         */
        val sets =
            arrayOf(
                intArrayOf(Items.NULL_6192, Items.NULL_6189, Items.NULL_6189),
                intArrayOf(Items.NULL_6198, Items.NULL_6196, Items.NULL_6195),
                intArrayOf(Items.NULL_6190, Items.NULL_6194, Items.NULL_6193),
                intArrayOf(Items.NULL_6195, Items.NULL_6197, Items.NULL_6198),
                intArrayOf(Items.NULL_6196, Items.NULL_6191, Items.NULL_6192),
            )

        fun randomQuestion(player: Player): Int {
            val randomSet = intArrayOf(*sets.random())
            val answer = intArrayOf(*randomSet)[0]
            randomSet.shuffle()
            val correctButton = randomSet.indexOf(answer) + 2
            player.packetDispatch.sendItemOnInterface(randomSet[0], 1, QUIZMASTER_INTERFACE, 6)
            player.packetDispatch.sendItemOnInterface(randomSet[1], 1, QUIZMASTER_INTERFACE, 7)
            player.packetDispatch.sendItemOnInterface(randomSet[2], 1, QUIZMASTER_INTERFACE, 8)

            player.packetDispatch.sendAngleOnInterface(QUIZMASTER_INTERFACE, 6, 512, 0, 0)
            player.packetDispatch.sendAngleOnInterface(QUIZMASTER_INTERFACE, 7, 512, 0, 0)
            player.packetDispatch.sendAngleOnInterface(QUIZMASTER_INTERFACE, 8, 512, 0, 0)
            return correctButton
        }
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.QUIZ_MASTER_2477)
        when (stage) {
            0 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "WELCOME to the GREATEST QUIZ SHOW in the",
                    "whole of ${ServerConstants.SERVER_NAME}:",
                    "<col=8A0808>O D D</col>  <col=8A088A>O N E</col>  <col=08088A>O U T</col>",
                ).also { stage++ }

            1 -> player(FaceAnim.THINKING, "I'm sure I didn't ask to take part in a quiz show...").also { stage++ }
            2 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Please welcome our newest contestant:",
                    "<col=FF0000>${player?.username}</col>!",
                    "Just pick the O D D  O N E  O U T.",
                    "Four questions right, and then you win!",
                ).also { stage++ }

            3 -> {
                setAttribute(player!!, QUIZMASTER_ATTRIBUTE_RANDOM_ANSWER, randomQuestion(player!!))
                player!!.interfaceManager.openChatbox(QUIZMASTER_INTERFACE)
                stage++
            }

            4 -> {
                if (buttonID == getAttribute(player!!, QUIZMASTER_ATTRIBUTE_RANDOM_ANSWER, 0)) {
                    /*
                     * Correct Answer.
                     */
                    setAttribute(
                        player!!,
                        QUIZMASTER_ATTRIBUTE_QUESTIONS_CORRECT,
                        getAttribute(player!!, QUIZMASTER_ATTRIBUTE_QUESTIONS_CORRECT, 0) + 1
                    )
                    if (getAttribute(player!!, QUIZMASTER_ATTRIBUTE_QUESTIONS_CORRECT, 0) >= 4) {
                        npc(
                            FaceAnim.FRIENDLY,
                            "<col=08088A>CONGRATULATIONS!</col>",
                            "You are a <col=8A0808>WINNER</col>!",
                            "Please choose your <col=08088A>PRIZE</col>!",
                        ).also { stage = 5 }
                    } else {
                        npc(
                            FaceAnim.HAPPY,
                            QuizMaster.CORRECT.random(),
                            "Okay, next question!",
                        )
                        stage = 3
                    }
                } else {
                    /*
                     * Wrong Answer.
                     */
                    npc(
                        FaceAnim.NEUTRAL,
                        QuizMaster.WRONG.random(),
                        "You're supposed to pick the ODD ONE OUT.",
                        "Now, let's start again...",
                    ).also { stage = 3 }
                }
            }

            5 -> options("1000 Coins", "Mystery Box").also { stage++ }
            6 -> {
                QuizMaster.cleanup(player!!)
                when (buttonID) {
                    1 -> {
                        queueScript(player!!, 1, QueueStrength.SOFT) {
                            addItemOrDrop(player!!, QuizMaster.COINS, 1000)
                            return@queueScript stopExecuting(player!!)
                        }
                    }

                    2 -> {
                        queueScript(player!!, 1, QueueStrength.SOFT) {
                            addItemOrDrop(player!!, QuizMaster.MYSTERY_BOX)
                            return@queueScript stopExecuting(player!!)
                        }
                    }
                }
                removeAttribute(player!!, QUIZMASTER_ATTRIBUTE_RETURN_LOC)
                removeAttribute(player!!, QUIZMASTER_ATTRIBUTE_QUESTIONS_CORRECT)
                removeAttribute(player!!, QUIZMASTER_ATTRIBUTE_RANDOM_ANSWER)
                stage = END_DIALOGUE
                end()
            }
        }
    }
}
