package content.region.fremennik.dialogue.rellekka

import core.api.getAttribute
import core.api.interaction.openNpcShop
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
class FurTraderDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            npc(FaceAnim.ANNOYED, "I don't sell to outerlanders.").also { stage = END_DIALOGUE }
        } else {
            npcl(
                FaceAnim.FRIENDLY,
                "Welcome back, ${getAttribute(
                    player,
                    "fremennikname",
                    "fremmyname",
                )}. Have you seen the furs I have today?",
            )
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                end()
                openNpcShop(player, NPCs.FUR_TRADER_1316)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return FurTraderDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FUR_TRADER_1316)
    }
}
