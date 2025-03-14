package content.region.fremennik.dialogue.miscellania

import core.api.hasRequirement
import core.api.interaction.openNpcShop
import core.api.quest.hasRequirement
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class FishmongerMiscDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!hasRequirement(player, Quests.THRONE_OF_MISCELLANIA)) {
            npcl(
                FaceAnim.FRIENDLY,
                "Greetings, Sir. Get your fresh fish here! I've heard that the Etceterian fish is stored in a cow shed.",
            ).also {
                stage =
                    0
            }
        } else {
            npcl(
                FaceAnim.FRIENDLY,
                "Greetings, Your Highness. Have some fresh fish! I've heard that the Etceterian fish is stored in a cow shed.",
            ).also {
                stage =
                    0
            }
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
                openNpcShop(player, NPCs.FISHMONGER_1393)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return FishmongerMiscDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FISHMONGER_1393)
    }
}
