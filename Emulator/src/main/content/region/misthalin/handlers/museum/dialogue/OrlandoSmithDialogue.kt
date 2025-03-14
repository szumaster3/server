package content.region.misthalin.handlers.museum.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class OrlandoSmithDialogue(
    player: Player? = null,
) : Dialogue(player) {

    override fun open(vararg args: Any): Boolean {
        npcl(FaceAnim.HALF_GUILTY, "G'day there, mate.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "Good day. Are you alright? You look a little lost.")
                stage++
            }

            1 -> {
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well, mate, to tell you the truth, I think I've come a gutser with these displays."
                )
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "Come a what?")
                stage++
            }

            3 -> {
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Gutser and no mistake. Me boss asked me to put together a quiz for the visitors."
                )
                stage++
            }

            4 -> {
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "But to be deadset with you, I wasn't paying much attention to me boss over there and I've done a bit of a rush job."
                )
                stage++
            }

            5 -> {
                playerl(FaceAnim.FRIENDLY, "You mean the natural historian?")
                stage++
            }

            6 -> {
                npcl(FaceAnim.HALF_GUILTY, "Yep, that's the bloke. Say, mate, you do me a favour?")
                stage++
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "Perhaps. What do you need?")
                stage++
            }

            8 -> {
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well, you look like a pretty smart cobber. Could you take a look at the display plaques and give 'em a runthrough?"
                )
                stage++
            }

            9 -> {
                options("Sure thing.", "No thanks.")
                stage++
            }

            10 -> when (buttonId) {
                1 -> {
                    player("Sure thing.")
                    stage = 11
                }

                2 -> {
                    player("No thanks I'm too busy.")
                    stage = 13
                }
            }

            11 -> {
                npcl(FaceAnim.HALF_GUILTY, "Bonza, mate! I reckon three questions per case should be bang to rights.")
                stage++
            }

            12 -> {
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Take a gander at each case and I'll look over your shoulder to give some advice."
                )
                stage = END_DIALOGUE
            }

            13 -> {
                npcl(FaceAnim.HALF_GUILTY, "Fair dinkum mate. I'm sure I'll get someone else to help me.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return OrlandoSmithDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ORLANDO_SMITH_5965)
    }
}
