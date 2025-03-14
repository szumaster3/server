package content.region.asgarnia.dialogue.dwarvenmines

import core.api.getAttribute
import core.api.interaction.openNpcShop
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class NurmofDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(
            FaceAnim.OLD_NORMAL,
            "Greetings and welcome to my pickaxe shop. Do you want to buy my premium quality pickaxes?",
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
                    "Yes, please.",
                    "No, thank you.",
                    "Are your pickaxes better than other pickaxes, then?",
                ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.NURMOF_594)
                    }
                    2 -> player(FaceAnim.FRIENDLY, "No thank you.").also { stage = 99 }
                    3 ->
                        player(FaceAnim.HALF_ASKING, "Are your pickaxes better than other pickaxes, then?").also {
                            stage =
                                10
                        }
                }

            10 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Of course they are! My pickaxes are made of higher grade metal than your ordinary bronze pickaxes, allowing you to mine ore just that little bit faster.",
                ).also {
                    if (!getAttribute(player, "pre-dq:said-hi", true)) {
                        stage++
                    } else {
                        stage = 99
                    }
                }
            11 -> playerl(FaceAnim.FRIENDLY, "By the way, Doric says hello!").also { stage++ }
            12 -> npcl(FaceAnim.OLD_HAPPY, "Oh! Thank you for telling me, adventurer!").also { stage = 99 }
            99 -> {
                setAttribute(player, "pre-dq:said-hi", true)
                end()
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return NurmofDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.NURMOF_594)
    }
}
