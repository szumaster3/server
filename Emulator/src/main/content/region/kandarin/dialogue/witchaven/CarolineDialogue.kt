package content.region.kandarin.dialogue.witchaven

import content.region.kandarin.quest.seaslug.dialogue.CarolineDialogueFile
import core.api.openDialogue
import core.api.quest.isQuestInProgress
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class CarolineDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when {
            isQuestInProgress(player, Quests.SEA_SLUG, 0, 50) ->
                end().also {
                    openDialogue(
                        player,
                        CarolineDialogueFile(),
                    )
                }

            else -> player(FaceAnim.FRIENDLY, "Hello")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.FRIENDLY, "Hello traveller, how are you?").also { stage++ }
            1 -> player(FaceAnim.FRIENDLY, "Not bad thanks, yourself?").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I'm good. Busy as always looking after Kent and Kennith but no complaints.",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return CarolineDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAROLINE_696)
    }
}
