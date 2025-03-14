package content.region.kandarin.dialogue.ardougne

import content.region.kandarin.quest.biohazard.dialogue.ElenaDialogue
import core.api.openDialogue
import core.api.quest.isQuestComplete
import core.api.quest.isQuestInProgress
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class ElenaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isQuestComplete(player, Quests.PLAGUE_CITY) && isQuestInProgress(player, Quests.BIOHAZARD, 0, 100)) {
            end().also { openDialogue(player, ElenaDialogue()) }
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
        return ElenaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ELENA_3209)
    }
}
