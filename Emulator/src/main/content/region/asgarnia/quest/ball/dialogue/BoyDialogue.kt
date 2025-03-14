package content.region.asgarnia.quest.ball.dialogue

import core.api.inInventory
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.startQuest
import core.api.removeItem
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Boy dialogue in Witches' House quest.
 */
@Initializable
class BoyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        val quest = getQuestStage(player, Quests.WITCHS_HOUSE)

        if (quest < 10) {
            playerl(FaceAnim.FRIENDLY, "Hello young man.")
            stage = 1
            return true
        }

        if (quest == 100) {
            sendDialogue("The boy is too busy playing with his ball to talk.")
            stage = END_DIALOGUE
            return true
        }

        if (!inInventory(player, Items.BALL_2407)) {
            npcl(FaceAnim.CHILD_GUILTY, "Have you gotten my ball back yet?")
            stage = 11
            return true
        }

        playerl(FaceAnim.FRIENDLY, "Hi, I have got your ball back. It was MUCH harder than I thought it would be.")
        stage = 11
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 -> {
                sendDialogue("The boy sobs.")
                stage++
            }

            2 -> {
                options("What's the matter?", "Well if you're not going to answer then I'll go.")
                stage++
            }

            3 -> when (buttonId) {
                1 -> {
                    playerl(FaceAnim.HALF_ASKING, "What's the matter?")
                    stage = 5
                }

                2 -> {
                    playerl(FaceAnim.NEUTRAL, "Well if you're not going to answer then I'll go.")
                    stage++
                }
            }

            4 -> {
                sendDialogue("The boy sniffs slightly.")
                stage = END_DIALOGUE
            }

            5 -> {
                npc(
                    FaceAnim.CHILD_SAD,
                    "I've kicked my ball over that hedge, into that garden!",
                    "The old lady who lives there is scary... She's locked the",
                    "ball in her wooden shed! Can you get my ball back for",
                    "me please?",
                )
                stage++
            }

            6 -> {
                options("Ok, I'll see what I can do.", "Get it back yourself.")
                stage++
            }

            7 -> when (buttonId) {
                1 -> {
                    playerl(FaceAnim.HAPPY, "Ok, I'll see what I can do.")
                    stage = 10
                }

                2 -> {
                    player(FaceAnim.NEUTRAL, "Get it back yourself.")
                    stage++
                }
            }

            8 -> {
                npc(FaceAnim.CHILD_SAD, "You're a meany.")
                stage++
            }

            9 -> {
                sendDialogue("The boy starts crying again.")
                stage++
            }

            10 -> {
                npc(FaceAnim.CHILD_FRIENDLY, "Thanks " + if (!player.isMale) "lady" else "mister" + "!")
                startQuest(player, Quests.WITCHS_HOUSE)
                stage = END_DIALOGUE
            }

            11 -> if (!removeItem(player, Items.BALL_2407)) {
                player("Not yet.")
                stage++
            } else {
                sendItemDialogue(player, Items.BALL_2407, "You give the ball back.")
                stage = 13
            }

            12 -> {
                npc(FaceAnim.CHILD_GUILTY, "Well it's in the shed in that garden.")
                stage = END_DIALOGUE
            }

            13 -> {
                npc(FaceAnim.CHILD_FRIENDLY, "Thank you so much!")
                stage++
            }
            14 -> {
                end()
                finishQuest(player, Quests.WITCHS_HOUSE)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BOY_895)
    }

}
