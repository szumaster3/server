package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import java.util.*

/**
 * The type Bull ant dialogue.
 */
@Initializable
class BullAntDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return BullAntDialogue(player)
    }

    /**
     * Instantiates a new Bull ant dialogue.
     */
    constructor()

    /**
     * Instantiates a new Bull ant dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (player.settings.runEnergy < 50) {
            npcl(FaceAnim.CHILD_NORMAL, "What's the matter, Private? Not enjoying the run?")
            stage = 0
            return true
        }
        when (Random().nextInt(4)) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "All right you worthless biped, fall in!")
                stage = 5
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Aten...hut!")
                stage = 9
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I can't believe they stuck me with you...")
                stage = 14
            }

            3 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "What in the name of all the layers of the abyss do you think you're doing, biped?"
                )
                stage = 17
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "Sir...wheeze...yes Sir!")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Not enjoying the run? You need more training biped?")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "Sir, no Sir! Sir, I'm enjoying the run a great deal, Sir!")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Then hop to, Private!")
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Sir, yes Sir!")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.FRIENDLY, "Sir, yes Sir!")
                stage++
            }

            6 -> {
                npcl(FaceAnim.CHILD_NORMAL, "We're going to work you so hard your boots fall off, understood?")
                stage++
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "Sir, yes Sir!")
                stage++
            }

            8 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Carry on Private!")
                stage = END_DIALOGUE
            }

            9 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Aten...hut!")
                stage++
            }

            10 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I can't believe they stuck me with you...")
                stage++
            }

            11 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "What in the name of all the layers of the abyss do you think you're doing, biped?"
                )
                stage++
            }

            12 -> {
                playerl(FaceAnim.FRIENDLY, "Sir, Private Player reporting for immediate active duty, Sir!")
                stage++
            }

            13 -> {
                npcl(FaceAnim.CHILD_NORMAL, "As you were, Private!")
                stage = END_DIALOGUE
            }

            14 -> {
                playerl(FaceAnim.FRIENDLY, "Buck up, Sir, it's not that bad.")
                stage++
            }

            15 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Stow that, Private, and get back to work!")
                stage++
            }

            16 -> {
                playerl(FaceAnim.FRIENDLY, "Sir, yes Sir!")
                stage = END_DIALOGUE
            }

            17 -> {
                playerl(FaceAnim.FRIENDLY, "Sir, nothing Sir!")
                stage++
            }

            18 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Well double-time it, Private, whatever it is!")
                stage++
            }

            19 -> {
                playerl(FaceAnim.FRIENDLY, "Sir, yes Sir!")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BULL_ANT_6867, NPCs.BULL_ANT_6868)
    }
}
