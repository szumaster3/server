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
class FishmongerRellekkaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            npc(FaceAnim.ANNOYED, "I don't sell to outerlanders.").also { stage = END_DIALOGUE }
        } else {
            npcl(
                FaceAnim.FRIENDLY,
                "Hello there, ${getAttribute(player, "fremennikname", "fremmyname")}. Looking for fresh fish?",
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
                openNpcShop(player, NPCs.FISH_MONGER_1315)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return FishmongerRellekkaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FISH_MONGER_1315)
    }
}
