package content.region.kandarin.dialogue.ardougne

import content.region.kandarin.quest.biohazard.dialogue.OmartBiohazardDialogue
import core.api.openDialogue
import core.api.quest.isQuestInProgress
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class OmartDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isQuestInProgress(player, Quests.BIOHAZARD, 2, 100)) {
            end().also { openDialogue(player, OmartBiohazardDialogue()) }
        } else {
            npc("Hello.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return OmartDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OMART_350)
    }
}
