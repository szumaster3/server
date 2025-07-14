package content.region.desert.alkharid.quest.feud.dialogue

import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Ali The Barman dialogue.
 */
@Initializable
class MenaphiteThugDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0  -> playerl(FaceAnim.FRIENDLY, "Hello.").also { stage++ }
            1  -> npcl(FaceAnim.FRIENDLY, "What do you want?").also { stage++ }
            2  -> playerl(FaceAnim.FRIENDLY, "I am trying to figure out the feud that exists between you and the Bandits.").also { stage++ }
            3  -> npcl(FaceAnim.FRIENDLY, "Not much to figure out there, the problem is those thieving bandits.").also { stage++ }
            4  -> playerl(FaceAnim.FRIENDLY, "In what way?").also { stage++ }
            5  -> npcl(FaceAnim.FRIENDLY, "They are always stealing, starting fights and causing disruption to the life of the people in this town.").also { stage++ }
            6  -> playerl(FaceAnim.FRIENDLY, "Word has it that the Menaphites are also involved in unsavoury behaviour.").also { stage++ }
            7  -> npcl(FaceAnim.FRIENDLY, "Admittedly, we are far better than them at that sort of thing, but they did start the cycle.").also { stage++ }
            8  -> playerl(FaceAnim.FRIENDLY, "How did 'they' start it?").also { stage++ }
            9  -> npcl(FaceAnim.FRIENDLY, "I cannot remember, as it's been going on for so long.").also { stage++ }
            10 -> playerl(FaceAnim.FRIENDLY, "Go on try.").also { stage++ }
            11 -> npcl(FaceAnim.FRIENDLY, "I think our leader once mentioned that they robbed one of our camels. A heinous crime around here, worthy of a stoning.").also { stage++ }
            12 -> playerl(FaceAnim.FRIENDLY, "So if they offered a camel as a gesture of goodwill would the Menaphites cease hostilities?").also { stage++ }
            13 -> npcl(FaceAnim.FRIENDLY, "Those bandits would never agree to that. They're such bad thieves they couldn't afford to buy even the mangiest excuse of a camel, never mind the desert traversing four legged beauty that we would demand.").also { stage++ }
            14 -> playerl(FaceAnim.FRIENDLY, "But if they did?").also { stage++ }
            15 -> npcl(FaceAnim.FRIENDLY, "Well I suppose so.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = MenaphiteThugDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.MENAPHITE_THUG_1904)
}
