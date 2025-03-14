package content.region.kandarin.dialogue.monastery

import content.region.kandarin.quest.drunkmonk.dialogue.BrotherCedricDialogueFile
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class BrotherCedricDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.MONKS_FRIEND) in 10..100) {
            end()
            openDialogue(player, BrotherCedricDialogueFile())
        } else {
            sendDialogue(player, "Brother Cedric is not interested in talking.")
            stage = 0
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BROTHER_CEDRIC_280)
    }
}
