package content.region.wilderness.dialogue

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class CapeMerchantDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Hello there, are you interested in buying one of my", "special capes?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("What's so special about your capes?", "Yes please!", "No thanks.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("What's so special about your capes?").also { stage++ }
                    2 -> player("Yes please!").also { stage = 4 }
                    3 -> player("No thanks.").also { stage = END_DIALOGUE }
                }
            2 ->
                npc(
                    "Ahh well they make it less likely that you'll accidently",
                    "attack anyone wearing the same cape as you and easier",
                    "to attack everyone else. They also make it easier to",
                    "distinguish people who're wearing the same cape as you",
                ).also {
                    stage++
                }
            3 ->
                npc(
                    "from everyone else. They're very useful when out in",
                    "the wilderness with friends or anyone else you don't",
                    "want to harm.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            4 -> {
                end()
                openNpcShop(player, npc.id)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return CapeMerchantDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.WILLIAM_1778,
            NPCs.IAN_1779,
            NPCs.LARRY_1780,
            NPCs.DARREN_1781,
            NPCs.EDWARD_1782,
            NPCs.RICHARD_1783,
            NPCs.NEIL_1784,
            NPCs.EDMOND_1785,
            NPCs.SIMON_1786,
            NPCs.SAM_1787,
        )
    }
}
