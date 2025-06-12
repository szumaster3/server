package content.region.morytania.port_phasmatys.shops

import core.api.inEquipment
import core.api.interaction.openNpcShop
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class GhostShopkeeperDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
            npcl(FaceAnim.FRIENDLY, "Woooo wooo wooooo woooo").also { stage = 2 }
        } else {
            npcl(FaceAnim.FRIENDLY, "Can I help you at all?")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Yes, please. What are you selling?",
                    "How should I use your shop?",
                    "No, thanks.",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.GHOST_SHOPKEEPER_1699)
                    }

                    2 ->
                        npc(
                            FaceAnim.HAPPY,
                            "I'm glad you ask! You can buy as many of the items",
                            "stocked as you wish. You can also sell most items to the",
                            "shop.",
                        ).also { stage = END_DIALOGUE }

                    3 -> end()
                }

            2 -> sendDialogue(player, "You cannot understand the ghost.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GHOST_SHOPKEEPER_1699)
}
