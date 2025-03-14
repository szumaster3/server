package content.region.kandarin.quest.tree.dialogue.trackergnomes

import core.api.getAttribute
import core.api.quest.getQuestStage
import core.api.sendDialogue
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class TrackerGnomeDialogue3(
    player: Player? = null,
) : Dialogue(player) {
    private val xcoordMap =
        mapOf(
            1 to "Less than my hands.",
            2 to "More than my head, less than my fingers.",
            3 to "More than we but less than our feet.",
            4 to "My legs and your legs.",
        )

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
                    0 -> playerl(FaceAnim.ASKING, "Are you OK?").also { stage++ }
                    1 -> npcl(FaceAnim.OLD_NORMAL, "OK? Who's OK? Not me! Hee hee!").also { stage++ }
                    2 -> playerl(FaceAnim.ASKING, "What's wrong?").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.OLD_NORMAL,
                            "You can't see me, no one can. Monsters, demons, they're all around me!",
                        ).also {
                            stage++
                        }
                    4 -> playerl(FaceAnim.ASKING, "What do you mean?").also { stage++ }
                    5 -> npcl(FaceAnim.OLD_NORMAL, "They're dancing, all of them, hee hee.").also { stage++ }
                    6 -> sendDialogue(player!!, "He's clearly lost the plot.").also { stage++ }
                    7 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "Do you have the coordinate for the Khazard stronghold?",
                        ).also { stage++ }
                    8 -> npcl(FaceAnim.OLD_NORMAL, "Who holds the stronghold?").also { stage++ }
                    9 -> playerl(FaceAnim.ASKING, "What?").also { stage++ }
                    10 -> {
                        if (getAttribute(player!!, "treegnome:xcoord", 0) == 0) {
                            val answer = (1..4).random()
                            npcl(FaceAnim.OLD_NORMAL, xcoordMap[answer])
                            setAttribute(player!!, "/save:treegnome:xcoord", answer)
                        } else {
                            npcl(FaceAnim.OLD_NORMAL, xcoordMap[getAttribute(player!!, "treegnome:xcoord", 1)])
                        }
                        stage++
                    }

                    11 -> playerl(FaceAnim.HALF_GUILTY, "You're mad").also { stage++ }
                    12 -> npcl(FaceAnim.OLD_NORMAL, "Dance with me, and Khazard's men are beat.").also { stage++ }

                    13 -> sendDialogue(player!!, "The toll of war has affected his mind.").also { stage++ }
                    14 -> playerl(FaceAnim.HALF_GUILTY, "I'll pray for you little man.").also { stage++ }

                    15 -> {
                        npcl(FaceAnim.OLD_NORMAL, "All day we pray in the hay, hee hee.").also {
                            setAttribute(player!!, "/save:treegnome:tracker3", true)
                            stage = END_DIALOGUE
                        }
                    }
                }
            }

            questStage == 31 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hello again.").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.OLD_NORMAL,
                            "Don't talk to me, you can't see me. No one can, just the demons.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
            }

            questStage > 31 -> {
                when (stage) {
                    0 -> playerl(FaceAnim.FRIENDLY, "Hello").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.OLD_NORMAL,
                            "I feel dizzy, where am I? Oh dear, oh dear I need some rest.",
                        ).also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "I think you do.").also { stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TRACKER_GNOME_3_483)
    }
}
