package content.region.kandarin.ardougne.dialogue

import content.region.kandarin.ardougne.quest.cog.dialogue.BrotherKojoDialogueFile
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class BrotherKojoDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        openDialogue(player!!, BrotherKojoDialogueFile(), npc)
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BROTHER_KOJO_223)
}
