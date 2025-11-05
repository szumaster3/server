package content.region.fremennik.dialogue

import content.region.fremennik.rellekka.quest.viking.dialogue.CouncilWorkerDialogue
import core.api.getQuestStage
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Council Workman dialogue.
 */
@Initializable
class CouncilWorkmanDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        end()
        if (getQuestStage(player, Quests.THE_FREMENNIK_TRIALS) in 1..99) {
            player.dialogueInterpreter.open((CouncilWorkerDialogue(1)))
        } else {
            openDialogue(player, CouncilWorkmanDiaryDialogue(), npc)
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean = true

    override fun newInstance(player: Player?): Dialogue = CouncilWorkmanDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.COUNCIL_WORKMAN_1287)
}
