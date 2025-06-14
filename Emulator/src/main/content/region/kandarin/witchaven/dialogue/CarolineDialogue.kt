package content.region.kandarin.witchaven.dialogue

import content.region.kandarin.witchaven.quest.seaslug.dialogue.CarolineDialogueFile
import core.api.openDialogue
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Caroline dialogue.
 *
 * Relations:
 * - [Sea Slug quest][content.region.kandarin.witchaven.quest.seaslug.SeaSlug]
 */
@Initializable
class CarolineDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        // Sea slug dialogue.
        if (!isQuestComplete(player, Quests.SEA_SLUG)) {
            openDialogue(player, CarolineDialogueFile())
        }
        // Post-Quest Dialogue.
        else {
            player(FaceAnim.FRIENDLY, "Hello again.")
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
            2 -> npcl(
                FaceAnim.FRIENDLY, "I'm good. Busy as always looking after Kent and Kennith but no complaints."
            ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = CarolineDialogue(player)
    override fun getIds(): IntArray = intArrayOf(NPCs.CAROLINE_696)
}
