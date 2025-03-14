package content.region.kandarin.quest.elena.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class JethickDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.PLAGUE_CITY) in 0..11) {
            npcl(
                FaceAnim.FRIENDLY,
                "Hello I don't recognise you. We don't get many newcomers around here.",
            ).also { stage++ }
        } else if (getQuestStage(player, Quests.PLAGUE_CITY) >= 12) {
            npcl(FaceAnim.FRIENDLY, "Hello. We don't get many newcomers around here.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (getQuestStage(player!!, Quests.PLAGUE_CITY)) {
            in 0..1 ->
                when (stage) {
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Well King Tyras has wandered off into the west kingdom. He doesn't care about the mess he's left here. The city warder Bravek is in charge at the moment... He's not much better.",
                        ).also { stage = END_DIALOGUE }
                }

            in 7..9 ->
                when (stage) {
                    1 ->
                        options(
                            "Hi, I'm looking for a woman from East Ardougne called Elena.",
                            "So who's in charge here?",
                        ).also { stage++ }

                    2 ->
                        when (buttonID) {
                            1 ->
                                playerl(
                                    FaceAnim.FRIENDLY,
                                    "Hi, I'm looking for a woman from East Ardougne called Elena.",
                                ).also { stage = 3 }

                            2 -> playerl(FaceAnim.FRIENDLY, "So who's in charge here?").also { stage = END_DIALOGUE }
                        }

                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "East Ardougnian women are easier to find in East Ardougne. Not many would come to West Ardougne to find one. Although the name is familiar, what does she look like?",
                        ).also { stage++ }

                    4 -> playerl(FaceAnim.NEUTRAL, "Um... brown hair... in her twenties...").also { stage++ }
                    5 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Hmm, that doesn't narrow it down a huge amount... I'll need to know more than that, or see a picture?",
                        ).also { stage++ }

                    6 -> {
                        if (inInventory(player!!, Items.PICTURE_1510)) {
                            sendItemDialogue(
                                player!!,
                                Items.PICTURE_1510,
                                "You show Jethick the picture.",
                            ).also { stage++ }
                        } else {
                            end()
                            stage = END_DIALOGUE
                        }
                    }

                    7 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "She came over here to help to aid plague victims. I think she is staying over with the Rehnison family. They live in the small timbered building at the far north side of town.",
                        ).also { stage++ }

                    8 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I've not seen her around here for a while, mind. I don't suppose you could run me a little errand while you're over there? I borrowed this book from them, could you return it?",
                        ).also { stage++ }

                    9 -> options("Yes, I'll return it for you.", "No, I don't have time for that.").also { stage++ }
                    10 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.NEUTRAL, "Yes, I'll return it for you.").also { stage = 11 }
                            2 ->
                                playerl(FaceAnim.NEUTRAL, "No, I don't have time for that.").also {
                                    stage =
                                        END_DIALOGUE
                                }
                        }

                    11 -> {
                        if (freeSlots(player) == 0) {
                            end()
                            sendItemDialogue(
                                player!!,
                                Items.BOOK_1509,
                                "Jethick shows you the book, but you don't have room to take it.",
                            )
                            stage = END_DIALOGUE
                        } else {
                            end()
                            sendItemDialogue(player!!, Items.BOOK_1509, "Jethick gives you a book.")
                            addItem(player!!, Items.BOOK_1509)
                            stage = END_DIALOGUE
                        }
                    }
                }

            in 10..11 ->
                when (stage) {
                    1 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I'm looking for a woman from East Ardougne called Elena.",
                        ).also { stage++ }

                    2 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Ah yes. She came over here to help the plague victims. I think she is staying over with the Rehnison family.",
                        ).also { stage++ }

                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "They live in the small timbered building at the far north side of town. I've not seen her around here in a while, mind.",
                        ).also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.JETHICK_725)
}
