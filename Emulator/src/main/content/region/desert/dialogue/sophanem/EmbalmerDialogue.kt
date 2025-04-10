package content.region.desert.dialogue.sophanem

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class EmbalmerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var item = 0
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hi.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0  -> npcl(FaceAnim.HAPPY, "Hello.").also { stage++ }
            1  -> playerl(FaceAnim.FRIENDLY, "You seem to be fairly happy for someone so spotty.").also { stage++ }
            2  -> npcl(FaceAnim.FRIENDLY, "I'd nearly forgotten about those; running my spice stall has taken my mind off the plagues.").also { stage++ }
            3  -> playerl(FaceAnim.FRIENDLY, "I didn't think that a spice store would eat up so much time.").also { stage++ }
            4  -> npcl(FaceAnim.FRIENDLY, "Well, I'm trying to find a balm for our spots.").also { stage++ }
            5  -> playerl(FaceAnim.HALF_ASKING, "Any leads?").also { stage++ }
            6  -> npcl(FaceAnim.FRIENDLY, "I got some cream from a merchant in Al Kharid that was supposed to help but all it seems to do is repel insects.").also { stage++ }
            7  -> playerl(FaceAnim.FRIENDLY, "This merchant wouldn't be called Ali M, would he?").also { stage++ }
            8  -> npcl(FaceAnim.HALF_THINKING, "That's him. How do you know him?").also { stage++ }
            9  -> playerl(FaceAnim.NOD_YES, "I've bought things from him in the past, some more useful than others.").also { stage++ }
            10 -> npcl(FaceAnim.FRIENDLY, "Anyway, I'm rambling. You're here to buy something, not to chat.").also { stage++ }
            11 -> end().also { openNpcShop(player, NPCs.EMBALMER_1980) }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.EMBALMER_1980)
}


class EmbalmerDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.EMBALMER_1980)
        when(stage){
            0 -> npcl(FaceAnim.SUSPICIOUS, "What are you doing?").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY,"I have this potion which I thought might help ease your itching.").also { stage++ }
            2 -> npcl(FaceAnim.NEUTRAL, "If I thought that these spots could be cured by some potion, I would have mixed up one for myself before now.").also { stage++ }
            3 -> playerl(FaceAnim.HALF_GUILTY, "Sorry... I was just trying to help.").also { stage = END_DIALOGUE }
        }
    }

}
