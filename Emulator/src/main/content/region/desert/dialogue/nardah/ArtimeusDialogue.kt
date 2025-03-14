package content.region.desert.dialogue.nardah

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class ArtimeusDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            "Greetings, friend; my business here deals with Hunter",
            "related items. Is there anything in which I can interest you?",
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
                    "What kind of items do you stock?",
                    "I'm not in the market for Hunter equipment right now, thanks.",
                ).also {
                    stage++
                }
            1 ->
                when (buttonId) {
                    1 -> player("What kind of items do you stock?").also { stage++ }
                    2 ->
                        player("I'm not in the market for Hunter equipment", "right now, thanks.").also {
                            stage =
                                END_DIALOGUE
                        }
                }
            2 -> npc("Have a look for yourself.").also { stage++ }
            3 -> {
                end()
                openNpcShop(player, NPCs.ARTIMEUS_5109)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ArtimeusDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ARTIMEUS_5109)
    }
}
