package content.region.misthalin.handlers.playersafety

import core.ServerConstants
import core.api.*
import core.api.quest.getQuestPoints
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the Professor Henry dialogue.
 */
@Initializable
class ProfessorHenryDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> {
                if (player.savedData.globalData.getTestStage() == 2 && inInventory(player, Items.TEST_PAPER_12626)) {
                    player("Hello, Professor.").also { stage = 10 }
                } else if (player.savedData.globalData.getTestStage() >= 3) {
                    npcl(
                        FaceAnim.HAPPY,
                        "Good job ${player.name}, you completed the test!",
                    ).also { stage = END_DIALOGUE }
                    return true
                } else {
                    player(FaceAnim.NEUTRAL, "Hello.").also { stage = 100 }
                }
            }

            10 -> npcl(FaceAnim.HAPPY, "Ah, ${player.name}. How's the test going?").also { stage++ }
            11 -> playerl(FaceAnim.NEUTRAL, "I think I've finished.").also { stage++ }
            12 -> npcl(FaceAnim.HAPPY, "Excellent! Let me just mark the paper for you then.").also { stage++ }
            13 -> npcl(FaceAnim.HAPPY, "Hmm. Uh-huh, yes I see. Good! Yes, that's right.").also { stage++ }
            14 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Excellent! Allow me to reward you for your work. " +
                        "I have these two old lamps that you may find useful.",
                ).also { stage++ }
            15 ->
                npc(
                    "Also, there is an old jail block connected to the cells",
                    "below the training centre, which have been overrun with",
                    "vicious creatures. If you search around the jail cells",
                    "downstairs, you should find it easily enough.",
                ).also {
                    stage++
                }
            16 -> npcl(FaceAnim.HAPPY, "Now, your rewards.").also { stage++ }
            17 -> {
                end()
                if (freeSlots(player) >= 1) {
                    showReward()
                } else {
                    npcl(FaceAnim.SAD, "You do not have space in your inventory for the rewards").also {
                        stage =
                            END_DIALOGUE
                    }
                }
            }

            100 -> npcl(FaceAnim.ANNOYED, "Hello what?").also { stage++ }
            101 -> playerl(FaceAnim.HALF_GUILTY, "Uh...hello there?").also { stage++ }
            102 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Hello, 'Professor'. Manners cost nothing, you know. " +
                        "When you're in my classroom, I ask that you use the proper address for my station.",
                ).also { stage++ }
            103 -> playerl(FaceAnim.HALF_GUILTY, "Your station?").also { stage++ }
            104 -> npcl(FaceAnim.HALF_GUILTY, "Yes. It means 'position', so to speak.").also { stage++ }
            105 -> playerl(FaceAnim.HALF_GUILTY, "Oh, okay.").also { stage++ }
            106 -> npcl(FaceAnim.HALF_GUILTY, "Now, what can I do for you, exactly?").also { stage++ }
            107 -> playerl(FaceAnim.HALF_GUILTY, "What is this place?").also { stage++ }
            108 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "This is the Misthalin Training Centre of Excellence. " +
                        "It is where bold adventurers, such as yourself, can come to learn of the dangers of " +
                        "the wide world and gain some valuable experience at the same time.",
                ).also { stage++ }
            109 -> playerl(FaceAnim.HALF_GUILTY, "What can I do here?").also { stage++ }
            110 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Here you can take part in the Player Safety test: " +
                        "a set of valuable lessons to learn about staying safe " +
                        "in ${ServerConstants.SERVER_NAME}.",
                ).also { stage++ }
            111 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I can give you a test paper to take and, once completed, " +
                        "you can bring it back to me for marking. Would you like to take the test? " +
                        "It will not cost you anything.",
                ).also { stage++ }
            112 -> showTopics(Topic("Yes, please.", 200), Topic("Not right now, thanks.", END_DIALOGUE))
            200 -> {
                if (freeSlots(player) == 0) {
                    npcl(FaceAnim.HALF_GUILTY, "It seems your inventory is full.").also { stage = END_DIALOGUE }
                } else if (amountInInventory(player, Items.TEST_PAPER_12626) > 0) {
                    npcl(
                        FaceAnim.HALF_GUILTY,
                        "You already have a test, please fill it out and return it to me.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                } else {
                    player.savedData.globalData.setTestStage(1)
                    addItem(player, Items.TEST_PAPER_12626)
                    npcl(
                        FaceAnim.HALF_GUILTY,
                        "Right then. Here is the test paper. " +
                            "When you have completed all the questions, bring it back to me for marking.",
                    ).also { stage++ }
                }
            }

            201 -> playerl(FaceAnim.HALF_GUILTY, "Okay, thanks.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PROFESSOR_HENRY_7143)
    }

    private fun showReward() {
        /*
         * Show the poster tunnel.
         */
        setVarp(player, 1203, 1 shl 29, true)
        player.savedData.globalData.setTestStage(3)
        removeItem(player, Items.TEST_PAPER_12626)
        addItem(player, Items.ANTIQUE_LAMP_4447, 2)
        player.emoteManager.unlock(Emotes.SAFETY_FIRST)
        openInterface(player, INTERFACE)

        /*
         * Clear the other lines.
         */

        for (i in 9..18) {
            sendString(player, "", INTERFACE, i)
        }
        sendString(player, "You have completed the Player Safety test!", INTERFACE, 4)
        sendString(player, getQuestPoints(player).toString(), INTERFACE, 7)
        sendString(player, "2 Experience lamps", INTERFACE, 9)
        sendString(player, "Access to the Stronghold of", INTERFACE, 10)
        sendString(player, "Player Safety Dungeon", INTERFACE, 11)
        sendString(player, "The Safety First' emote", INTERFACE, 12)
        sendItemZoomOnInterface(player, INTERFACE, 5, Items.TEST_PAPER_12626)
    }

    companion object {
        const val INTERFACE = Components.QUEST_COMPLETE_SCROLL_277
    }
}
