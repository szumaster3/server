package content.region.kandarin.dialogue.seers

import content.region.kandarin.quest.grail.dialogue.MerlinDialogue
import content.region.kandarin.quest.merlin.dialogue.MerlinDialogueFile
import core.api.openDialogue
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class MerlinDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (!isQuestComplete(player, Quests.MERLINS_CRYSTAL)) {
            openDialogue(player, MerlinDialogueFile(false), NPCs.MERLIN_249)
        } else {
            openDialogue(player, MerlinDialogue(), NPCs.MERLIN_249)
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return MerlinDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MERLIN_249)
    }
}
