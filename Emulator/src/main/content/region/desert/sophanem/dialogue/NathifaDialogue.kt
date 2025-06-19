package content.region.desert.sophanem.dialogue

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class NathifaDialogue(player: Player? = null) : Dialogue(player) {


    override fun open(vararg args: Any?): Boolean {
        npcl(FaceAnim.HAPPY, "Hello! You must be the adventurer that helped us get out of Menaphos. Thank you.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.HAPPY, "Don't mention it.").also { stage++ }
            1 -> options("What do you have for sale?", "What were things like in Menaphos?", "I'd better get going.").also { stage++ }
            2 -> when (buttonId) {
                1 -> player(FaceAnim.HALF_ASKING, "What do you have for sale?").also { stage++ }
                2 -> playerl(FaceAnim.HALF_ASKING, "What were things like in Menaphos?").also { stage = 5 }
                3 -> playerl(FaceAnim.NEUTRAL, "I'd better get going.").also { stage = 15 }
            }
            3 -> npcl(FaceAnim.FRIENDLY, "Take a look.").also { stage++ }
            4 -> end().also { openNpcShop(player, NPCs.NATHIFA_5264) }
            5 -> npcl(FaceAnim.SAD, "Not great. I'd just gone over there to purchase some goods from the market. When I got back to the gates, they'd been sealed shut.").also { stage++ }
            6 -> playerl(FaceAnim.HALF_ASKING, "So the guards wouldn't let you back?").also { stage++ }
            7 -> npcl(FaceAnim.SAD, "They were still allowing citizens from Sophanem to return at that point, but I foolishly chose to stay. I was scared of catching the pox.").also { stage++ }
            8 -> npcl(FaceAnim.SAD, "By the time I'd changed my mind, they'd stopped letting anyone through. Thankfully, I had some friends there with a spare bed.").also { stage++ }
            9 -> playerl(FaceAnim.HALF_ASKING, "I guess not everyone was as lucky?").also { stage++ }
            10 -> npcl(FaceAnim.SAD, "No. Many didn't have anywhere to go. Violence broke out in some parts of the city.").also { stage++ }
            11 -> npcl(FaceAnim.SAD, "They quickly put a stop to it in the Imperial and Merchant quarters, but there was still some unrest in the Workers district when I was finally able to leave.").also { stage++ }
            12 -> playerl(FaceAnim.HALF_GUILTY, "I'm sorry you had to go through all of that. Menaphos definitely took a hard stance on the plagues.").also { stage++ }
            13 -> npcl(FaceAnim.SAD, "They were brutal. They even shut off the port from the rest of the city over fears of the plague getting in that way. Many of their own citizens are still trapped there.").also { stage++ }
            14 -> npcl(FaceAnim.HAPPY, "Anyway, let's not talk any more of this. Is there anything else I can do for you?").also { stage = 1 }
            15 -> npcl(FaceAnim.FRIENDLY, "Until next time.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = NathifaDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.NATHIFA_5264)
}