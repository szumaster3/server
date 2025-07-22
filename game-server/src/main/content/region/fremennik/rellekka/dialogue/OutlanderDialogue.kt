package content.region.fremennik.rellekka.dialogue

import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Outlander dialogue.
 */
@Initializable
class OutlanderDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            npcl(FaceAnim.ANNOYED, "I cannot speak to you Outlander! Talk to Brundt, the Chieftain!").also { stage = END_DIALOGUE }
        } else {
            player(FaceAnim.FRIENDLY, "Hello.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.FRIENDLY, "Hello to you, too!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = OutlanderDialogue(player)

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.BORROKAR_1307,
            NPCs.FREIDIR_1306,
            NPCs.INGA_1314,
            NPCs.JENNELLA_1312,
            NPCs.LANZIG_1308,
            NPCs.PONTAK_1309,
            NPCs.SASSILIK_1313,
        )
}
