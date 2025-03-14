package content.region.asgarnia.quest.eadgar

import core.api.quest.isQuestComplete
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class EadgarDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.TROLL_STRONGHOLD)) {
            sendDialogue(player, "Mad Eadgar seems too busy to talk.").also { stage = END_DIALOGUE }
        } else {
            npcl(
                FaceAnim.HALF_GUILTY,
                "Welcome to Mad Eadgar's! Happiness in a bowl! Would you care to sample our delicious home cooking?",
            )
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "I need to find some goutweed. Sanfew said you might be able to help.",
                ).also { stage++ }

            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Sanfew, you say? Ah, haven't seen him in a while. Goutweed is used as an ingredient in troll cooking. You should ask one of their cooks.",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.EADGAR_1113)
    }
}
