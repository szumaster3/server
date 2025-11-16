package content.global.skill.summoning.familiar.dialogue.spirit

import core.api.anyInInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Spirit Kalphite familiar dialogue.
 */
@Initializable
class SpiritKalphiteDialogue(player: Player? = null) : Dialogue(player) {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = SpiritKalphiteDialogue(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (anyInInventory(player, *kerisIDs)) {
            playerl(FaceAnim.ASKING, "How dare I what?")
            stage = 0
            branch = 0
            return true
        }

        branch = (1..4).random()
        stage = when (branch) {
            1 -> 4
            2 -> 6
            3 -> 9
            4 -> 11
            else -> 4
        }

        when (branch) {
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "This activity is not optimal for us.")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "We are growing infuriated. What is our goal?")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "We find this to be wasteful of our time.")
            4 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "We grow tired of your antics, biped.")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "That weapon offends us!"); stage++ }
                1 -> { playerl(FaceAnim.HALF_ASKING, "What weapon?"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "Oh..."); stage++ }
                3 -> { playerl(FaceAnim.FRIENDLY, "Awkward."); stage = END_DIALOGUE }
            }
            1 -> when (stage) {
                4 -> { playerl(FaceAnim.FRIENDLY, "Well, you'll just have to put up with it for now."); stage++ }
                5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "We would not have to 'put up' with this in the hive."); stage = END_DIALOGUE }
            }
            2 -> when (stage) {
                6 -> { playerl(FaceAnim.FRIENDLY, "Well, I haven't quite decided yet."); stage++ }
                7 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "There is no indecision in the hive."); stage++ }
                8 -> { playerl(FaceAnim.FRIENDLY, "Or a sense of humour or patience, it seems."); stage = END_DIALOGUE }
            }
            3 -> when (stage) {
                9 -> { playerl(FaceAnim.FRIENDLY, "Maybe I find you wasteful..."); stage++ }
                10 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "We would not face this form of abuse in the hive."); stage = END_DIALOGUE }
            }
            4 -> when (stage) {
                11 -> { playerl(FaceAnim.FRIENDLY, "What antics? I'm just getting on with my day."); stage++ }
                12 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "In an inefficient way. In the hive, you would be replaced."); stage++ }
                13 -> { playerl(FaceAnim.FRIENDLY, "In the hive this, in the hive that..."); stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPIRIT_KALPHITE_6994, NPCs.SPIRIT_KALPHITE_6995)

    companion object {
        private val kerisIDs = intArrayOf(
            Items.KERIS_10581,
            Items.KERISP_10582,
            Items.KERISP_PLUS_10583,
            Items.KERISP_PLUS_PLUS_10584,
        )
    }
}
