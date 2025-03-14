package content.region.morytania.dialogue.canifis

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class RufusDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HAPPY, "Hi!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.HAPPY,
                    "Grreeting frrriend! Welcome to my worrrld famous",
                    "food emporrium! All my meats are so frrresh you'd",
                    "swear you killed them yourrrself!",
                ).also {
                    stage++
                }
            1 ->
                options(
                    "Why do you only sell meats?",
                    "Do you sell cooked food?",
                    "Can I buy some food?",
                ).also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "Why do you only sell meats?").also { stage++ }
                    2 -> player(FaceAnim.ASKING, "Do you sell cooked food?").also { stage = 7 }
                    3 -> player(FaceAnim.FRIENDLY, "Can I buy some food?").also { stage = 8 }
                }
            3 ->
                npc(
                    FaceAnim.DISGUSTED,
                    "What? Why, what else would you want to eat? What",
                    "kind of lycanthrrope are you anyway?",
                ).also {
                    stage++
                }
            4 -> player(FaceAnim.HALF_GUILTY, "...A vegetarian one?").also { stage++ }
            5 -> npc(FaceAnim.EXTREMELY_SHOCKED, "Vegetarrrian...?").also { stage++ }
            6 -> player(FaceAnim.SUSPICIOUS, "Never mind.").also { stage = END_DIALOGUE }
            7 ->
                npc(
                    FaceAnim.EVIL_LAUGH,
                    "Cooked food? Who would want that? You lose all the",
                    "flavourrr of the meat when you can't taste the blood!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            8 -> npc(FaceAnim.HAPPY, "Cerrrtainly!").also { stage++ }
            9 -> {
                end()
                openNpcShop(player, NPCs.RUFUS_1038)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return RufusDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RUFUS_1038)
    }
}
