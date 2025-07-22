package content.region.misthalin.silvarea.dialogue

import content.region.misthalin.silvarea.quest.rag.dialogue.OddOldManDialogueFile
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * Represents the Odd Old Man dialogue.
 */
@Initializable
class OddOldManDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player!!, OddOldManDialogueFile(), npc)
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ODD_OLD_MAN_3670)
}
