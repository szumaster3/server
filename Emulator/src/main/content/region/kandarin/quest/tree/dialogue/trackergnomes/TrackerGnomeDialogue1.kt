package content.region.kandarin.quest.tree.dialogue.trackergnomes

import core.api.inInventory
import core.api.quest.getQuestStage
import core.api.sendDialogue
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class TrackerGnomeDialogue1(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        val questStage = getQuestStage(player!!, Quests.TREE_GNOME_VILLAGE)
        when {
            questStage == 100 ->
                when (stage) {
                    0 -> sendDialogue(player, "Tracker Gnome seems too busy to talk.").also { stage = END_DIALOGUE }
                }

            questStage >= 40 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hello").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.OLD_NORMAL,
                            "When will this battle end? I feel like I've been fighting forever.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
            }

            questStage > 30 -> {
                if (inInventory(player!!, Items.ORB_OF_PROTECTION_587)) {
                    when (stage) {
                        0 -> playerl(FaceAnim.ASKING, "How are you tracker?").also { stage++ }
                        1 ->
                            npcl(
                                FaceAnim.OLD_NORMAL,
                                "Now we have the orb I'm much better. They won't stand a chance without it.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                    }
                } else {
                    when (stage) {
                        0 -> playerl(FaceAnim.FRIENDLY, "Hello again.").also { stage++ }
                        1 ->
                            npcl(
                                FaceAnim.OLD_NORMAL,
                                "Well done, you've broken down their defences. This battle must be ours.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                    }
                }
            }

            questStage == 30 -> {
                when (stage) {
                    0 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "Do you know the coordinates of the Khazard stronghold?",
                        ).also { stage++ }
                    1 -> npcl(FaceAnim.OLD_NORMAL, "I managed to get one, although it wasn't easy.").also { stage++ }
                    2 -> {
                        sendDialogue(player!!, "The gnome tells you the height coordinate.").also {
                            setAttribute(player!!, "/save:treegnome:tracker1", true)
                            stage++
                        }
                    }
                    3 -> player("Well done.").also { stage++ }
                    4 ->
                        npcl(
                            FaceAnim.OLD_NORMAL,
                            "The other two tracker gnomes should have the other coordinates if they're still alive.",
                        ).also {
                            stage++
                        }
                    5 -> playerl(FaceAnim.FRIENDLY, "OK, take care.").also { stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TRACKER_GNOME_1_481)
    }
}
