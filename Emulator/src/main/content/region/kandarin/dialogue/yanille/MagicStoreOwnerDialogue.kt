package content.region.kandarin.dialogue.yanille

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class MagicStoreOwnerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Welcome to the Magic Guild Store. Would you like to", "buy some magic supplies?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes please.", "No thank you.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("Yes please.").also { stage++ }
                    2 -> end()
                }

            2 -> {
                end()
                openNpcShop(player, NPCs.MAGIC_STORE_OWNER_461)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return MagicStoreOwnerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MAGIC_STORE_OWNER_461)
    }
}
