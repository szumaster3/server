package content.region.kandarin.dialogue.seers

import content.region.kandarin.quest.scorpcatcher.dialogue.ThormacDialogueFile
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class ThormacDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> openDialogue(player, ThormacDialogueFile())
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ThormacDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.THORMAC_389)
    }
}
