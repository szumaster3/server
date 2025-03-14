package content.region.kandarin.dialogue.stronghold

import content.region.kandarin.quest.grandtree.dialogue.GloughGTDialogue
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class GloughDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.THE_GRAND_TREE) >= 40) {
            openDialogue(player, GloughGTDialogue())
        } else {
            playerl(FaceAnim.FRIENDLY, "Hello there!")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.OLD_NORMAL, "You shouldn't be here human!").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "What do you mean?").also { stage++ }
            2 -> npcl(FaceAnim.OLD_NORMAL, "The Gnome Stronghold is for gnomes alone!").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "Surely not!").also { stage++ }
            4 -> npcl(FaceAnim.OLD_NORMAL, "We don't need your sort round here!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return GloughDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GLOUGH_671)
    }
}
