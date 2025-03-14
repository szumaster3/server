package content.region.kandarin.dialogue.yanille

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AleckDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    "Hello, hello, and a most warm welcome to my Hunter",
                    "Emporium. We have everything the discerning Hunter",
                    "could need.",
                ).also {
                    stage++
                }
            1 ->
                npc(
                    "Would you like me to show you our range of",
                    "equipment? Or was there something specific you were",
                    "after?",
                ).also {
                    stage++
                }
            2 ->
                options(
                    "Ok, let's see what you've got.",
                    "I'm not interested, thanks.",
                    "Who's that guy over there?",
                ).also {
                    stage++
                }
            3 ->
                when (buttonId) {
                    1 -> player("Ok, let's see what you've got!").also { stage = 10 }
                    2 -> player("I'm not interested, thanks.").also { stage = END_DIALOGUE }
                    3 -> player("Who's that guy over there?").also { stage = 30 }
                }
            10 -> {
                end()
                openNpcShop(player, NPCs.ALECK_5110)
            }
            30 -> npc("Him? I think he might be crazy. Either that or he's", "seeking attention.").also { stage++ }
            31 ->
                npc(
                    "He keeps trying to sell me these barmy looking weapons",
                    "he's invented. I can't see them working, personally.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AleckDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALECK_5110)
    }
}
