package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Magpie dialogue.
 */
@Initializable
class MagpieDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return MagpieDialogue(player)
    }

    /**
     * Instantiates a new Magpie dialogue.
     */
    constructor()

    /**
     * Instantiates a new Magpie dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 4).toInt()) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "There's nowt gannin on here...")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Howway, let's gaan see what's happenin' in toon.")
                stage = 2
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Are we gaan oot soon? I'm up fer a good walk me.")
                stage = 3
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Ye' been plowdin' i' the claarts aall day.")
                stage = 4
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.HALF_ASKING, "Err...sure? Maybe?")
                stage++
            }

            1 -> {
                playerl(FaceAnim.HALF_ASKING, "It seems upset, but what is it saying?")
                stage = END_DIALOGUE
            }

            2 -> {
                playerl(FaceAnim.HALF_ASKING, "What? I can't understand what you're saying.")
                stage = END_DIALOGUE
            }

            3 -> {
                playerl(FaceAnim.HALF_ASKING, "That...that was just noise. What does that mean?")
                stage = END_DIALOGUE
            }

            4 -> {
                playerl(FaceAnim.HALF_ASKING, "What? That made no sense.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MAGPIE_6824)
    }
}
