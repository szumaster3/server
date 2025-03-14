package content.region.wilderness.dialogue

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FatTonyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Go away, I'm very busy.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Sorry to disturb you.",
                    "What are you busy doing?",
                    "Have you anything to sell?",
                ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("Sorry to disturb you.").also { stage = END_DIALOGUE }
                    2 -> player("What are you busy doing?").also { stage++ }
                    3 -> player("Have you anything to sell?").also { stage = 12 }
                }
            2 -> npc(FaceAnim.HALF_GUILTY, "I'm cooking pizzas for the people in this camp.").also { stage++ }
            3 -> npc(FaceAnim.HALF_GUILTY, "Not that these louts appreciate my gourmet cooking!").also { stage++ }
            4 ->
                options(
                    "So what is a gourmet chef doing cooking for bandits?",
                    "Can I have some pizza too?",
                    "Okay, I'll leave you to it.",
                ).also {
                    stage++
                }
            5 ->
                when (buttonId) {
                    1 -> player("So what is a gourmet chef doing cooking for bandits?").also { stage++ }
                    2 -> player("Can I have some pizza too?").also { stage = 9 }
                    3 -> player("Okay, I'll leave you to it.").also { stage = END_DIALOGUE }
                }
            6 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well, I'm an outlaw. I was accused of giving the king food",
                    "poisoning!",
                ).also {
                    stage++
                }
            7 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "The thought of it! I think he just drank too much wine",
                    "that night.",
                ).also { stage++ }
            8 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I had to flee the kingdom of Misthalin. The bandits",
                    "give me refuge here as long as I cook for them.",
                ).also {
                    stage =
                        4
                }
            9 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well, this pizza is really meant to be for the bandits.",
                    "I guess I could sell you some pizza bases though.",
                ).also {
                    stage++
                }
            10 -> options("Yes, okay.", "Oh, if I have to pay I don't want any.").also { stage++ }
            11 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.FAT_TONY_596)
                    }
                    2 -> player("Oh, if I have to pay I don't want any.").also { stage = END_DIALOGUE }
                }
            12 -> npc(FaceAnim.HALF_GUILTY, "Well, I guess I can sell you some half-made pizzas.").also { stage++ }
            13 -> {
                end()
                openNpcShop(player, NPCs.FAT_TONY_596)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return FatTonyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FAT_TONY_596)
    }
}
