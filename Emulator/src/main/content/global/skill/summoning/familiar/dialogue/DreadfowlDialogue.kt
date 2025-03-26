package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Dreadfowl dialogue.
 */
@Initializable
class DreadfowlDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return DreadfowlDialogue(player)
    }

    /**
     * Instantiates a new Dreadfowl dialogue.
     */
    constructor()

    /**
     * Instantiates a new Dreadfowl dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 3).toInt()) {
            0 -> {
                npcl(FaceAnim.OLD_NORMAL, "Attack! Fight! Annihilate!")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.OLD_NORMAL, "Can it be fightin' time, please?")
                stage = 1
            }

            2 -> {
                npcl(FaceAnim.OLD_NORMAL, "I want to fight something.")
                stage = 2
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.HALF_ASKING, "It always worries me when you're so happy saying that.")
                stage = END_DIALOGUE
            }

            1 -> {
                playerl(FaceAnim.FRIENDLY, "Look I'll find something for you to fight, just give me a second.")
                stage = END_DIALOGUE
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "I'll find something for you in a minute - just be patient.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DREADFOWL_6825, NPCs.DREADFOWL_6826)
    }
}
