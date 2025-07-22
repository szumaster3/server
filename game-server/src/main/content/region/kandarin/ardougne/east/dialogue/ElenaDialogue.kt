package content.region.kandarin.ardougne.east.dialogue

import content.region.kandarin.ardougne.east.quest.biohazard.dialogue.ElenaBiohazardDialogue
import core.api.openDialogue
import core.api.getQuestStage
import core.api.isQuestComplete
import core.api.isQuestInProgress
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Elena dialogue.
 */
@Initializable
class ElenaDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        return if (
            isQuestComplete(player, Quests.PLAGUE_CITY) &&
            !isQuestComplete(player, Quests.BIOHAZARD) && getQuestStage(player, Quests.BIOHAZARD) > 0) {
            end()
            openDialogue(player, ElenaBiohazardDialogue())
            true
        } else {
            npc("Hello.")
            stage = END_DIALOGUE
            true
        }
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean = true

    override fun newInstance(player: Player?): Dialogue = ElenaDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.ELENA_3209)
}
