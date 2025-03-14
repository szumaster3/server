package content.region.karamja.dialogue

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class JiminuaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Welcome to Jiminua's Jungle Store, Can I help you", "at all?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Yes please. What are you selling?",
                    "How should I use your shop?",
                    "Can you un-note any of my items?",
                    "No thanks.",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> player("Yes please. What are you selling?").also { stage++ }
                    2 -> player("How should I use your shop?").also { stage = 4 }
                    3 -> player("Can you un-note any of my items?").also { stage = 5 }
                    4 -> player("No thanks.").also { stage = END_DIALOGUE }
                }

            2 -> npc("Take a good look.").also { stage++ }
            3 -> {
                end()
                openNpcShop(player, NPCs.JIMINUA_560)
            }

            4 ->
                npc(
                    "I'm glad you ask! You can buy as many of the items",
                    "stocked as you wish. You can also sell most items",
                    "to the shop.",
                ).also { stage = END_DIALOGUE }

            5 ->
                npc(
                    "I can un-note pure essence, but nothing else. Just",
                    "give me the notes you wish to exchange.",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JIMINUA_560)
    }
}
