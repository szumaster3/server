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
class TrackerGnomeDialogue2(
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

            questStage == 30 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.WORRIED, "Are you OK?").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.OLD_NORMAL,
                            "They caught me spying on the stronghold. They beat and tortured me.",
                        ).also { stage++ }
                    2 ->
                        npcl(
                            FaceAnim.OLD_NORMAL,
                            "But I didn't crack. I told them nothing. They can't break me!",
                        ).also { stage++ }
                    3 -> playerl(FaceAnim.HALF_GUILTY, "I'm sorry little man.").also { stage++ }
                    4 -> npcl(FaceAnim.OLD_NORMAL, "Don't be. I have the position of the stronghold!").also { stage++ }
                    5 -> {
                        sendDialogue(player!!, "The gnome tells you the y coordinate.").also {
                            setAttribute(player!!, "/save:treegnome:tracker2", true)
                            stage++
                        }
                    }
                    6 -> playerl(FaceAnim.FRIENDLY, "Well done.").also { stage++ }
                    7 -> npcl(FaceAnim.OLD_NORMAL, "Now leave before they find you and all is lost.").also { stage++ }
                    8 -> playerl(FaceAnim.FRIENDLY, "Hang in there.").also { stage++ }
                    9 -> npcl(FaceAnim.OLD_NORMAL, "Go!").also { stage = END_DIALOGUE }
                }
            }

            questStage >= 40 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hello").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.OLD_NORMAL,
                            "When will this battle end? I feel like I've been locked up my whole life.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
            }

            questStage > 30 -> {
                if (inInventory(player!!, Items.ORB_OF_PROTECTION_587)) {
                    when (stage) {
                        0 -> playerl(FaceAnim.HALF_ASKING, "How are you tracker?").also { stage++ }
                        1 ->
                            npcl(
                                FaceAnim.OLD_NORMAL,
                                "Now we have the orb I'm much better. Soon my comrades will come and free me.",
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
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TRACKER_GNOME_2_482)
    }
}
