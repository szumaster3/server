package content.region.morytania.dialogue.phasmatys

import content.region.morytania.quest.ahoy.dialogue.OldManGADialogue
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class OldManDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when {
            isQuestComplete(player, Quests.GHOSTS_AHOY) -> player("How is it going?").also { stage = 5 }
            getQuestStage(player, Quests.GHOSTS_AHOY) >= 4 -> openDialogue(player, OldManGADialogue())
            else -> player("What are you doing on this shipwreck?")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Shipwreck?!? Not shipwreck, surely not! Just in port, that's all!",
                ).also { stage++ }

            1 -> player("Don't be silly - half the ship's missing!").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "No no no - the captain's just waiting for the wind to change, then we're off!",
                ).also { stage++ }

            3 -> player("You mean the skeleton sitting here in this chair?").also { stage++ }
            4 -> npcl(FaceAnim.HALF_GUILTY, "You must show more respect to the Captain.").also { stage = END_DIALOGUE }
            5 -> npcl(FaceAnim.HAPPY, "Wonderful, wonderful! Mother's coming to get me!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OLD_MAN_1696)
    }
}
