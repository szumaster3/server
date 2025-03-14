package content.region.desert.dialogue.nardah

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class RokuhDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            "Come one, come all, buy my amazing choc ice invention here!",
            "Chocolate on the outside. Iced cream on the inside.",
            "Oh I also have some chocolate left over from making",
            "choc ices which I'm selling too.",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Cool I'd like to buy some",
                    "No thanks, I'm not interested.",
                    "How do you stop your icy snacks melting?",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.ROKUH_3045)
                    }
                    2 -> player("No thanks, I'm not interested").also { stage = END_DIALOGUE }
                    3 ->
                        player(
                            "How do you stop your icy snacks melting?",
                            "The middle of the desert is the last place I'd expect to see ice.",
                        ).also {
                            stage++
                        }
                }
            2 ->
                npc(
                    "It's quite a surprise, isn't it, my friend. I actually have this",
                    "special magic box of ice, which I bought for a princely sum",
                    "from a strange man while adventuring in lands far away",
                    "He said it is imbued with some sort of",
                ).also {
                    stage++
                }
            3 ->
                npc(
                    "powerful ice magic which keeps it cold all the time. I had",
                    "never seen any ice magic before, it's clearly very rare",
                    "and powerful, so I felt I had to buy it.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return RokuhDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ROKUH_3045)
    }
}
