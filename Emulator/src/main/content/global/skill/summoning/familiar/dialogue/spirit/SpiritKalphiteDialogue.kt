package content.global.skill.summoning.familiar.dialogue.spirit

import core.api.hasAnItem
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Spirit kalphite dialogue.
 */
@Initializable
class SpiritKalphiteDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return SpiritKalphiteDialogue(player)
    }

    /**
     * Instantiates a new Spirit kalphite dialogue.
     */
    constructor()

    /**
     * Instantiates a new Spirit kalphite dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val hasKeris = hasAnItem(player, *kerisIDs).container === player.inventory
        if (hasKeris) {
            playerl(FaceAnim.ASKING, "How dare I what?")
            stage = 0
            return true
        }
        when ((Math.random() * 4).toInt()) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "This activity is not optimal for us.")
                stage = 4
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "We are growing infuriated. What is our goal?")
                stage = 6
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "We find this to be wasteful of our time.")
                stage = 9
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "We grow tired of your antics, biped.")
                stage = 11
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "That weapon offends us!")
                stage++
            }

            1 -> {
                playerl(FaceAnim.HALF_ASKING, "What weapon?")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "Oh...")
                stage++
            }

            3 -> {
                playerl(FaceAnim.FRIENDLY, "Awkward.")
                stage = END_DIALOGUE
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Well, you'll just have to put up with it for now.")
                stage++
            }

            5 -> {
                npcl(FaceAnim.CHILD_NORMAL, "We would not have to 'put up' with this in the hive.")
                stage = END_DIALOGUE
            }

            6 -> {
                playerl(FaceAnim.FRIENDLY, "Well, I haven't quite decided yet.")
                stage++
            }

            7 -> {
                npcl(FaceAnim.CHILD_NORMAL, "There is no indecision in the hive.")
                stage++
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "Or a sense of humour or patience, it seems.")
                stage = END_DIALOGUE
            }

            9 -> {
                playerl(FaceAnim.FRIENDLY, "Maybe I find you wasteful...")
                stage++
            }

            10 -> {
                npcl(FaceAnim.CHILD_NORMAL, "We would not face this form of abuse in the hive.")
                stage = END_DIALOGUE
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "What antics? I'm just getting on with my day.")
                stage++
            }

            12 -> {
                npcl(FaceAnim.CHILD_NORMAL, "In an inefficient way. In the hive, you would be replaced.")
                stage++
            }

            13 -> {
                playerl(FaceAnim.FRIENDLY, "In the hive this, in the hive that...")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_KALPHITE_6994, NPCs.SPIRIT_KALPHITE_6995)
    }

    companion object {
        private val kerisIDs = intArrayOf(
            Items.KERIS_10581, Items.KERISP_10582, Items.KERISP_PLUS_10583, Items.KERISP_PLUS_PLUS_10584
        )
    }
}
