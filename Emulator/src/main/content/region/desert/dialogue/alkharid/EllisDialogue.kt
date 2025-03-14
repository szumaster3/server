package content.region.desert.dialogue.alkharid

import content.global.skill.crafting.Tanning
import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class EllisDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.FRIENDLY, "Greetings friend. I am a manufacturer of leather.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                var hasHides = false
                for (tanningProduct in Tanning.values()) {
                    if (inInventory(player, tanningProduct.item)) {
                        hasHides = true
                        break
                    }
                }
                if (hasHides) {
                    npcl(
                        FaceAnim.FRIENDLY,
                        "I see you have brought me some hides. Would you like me to tan them for you?",
                    ).also {
                        stage =
                            10
                    }
                } else {
                    options("Can I buy some leather?", "Leather is rather weak stuff.").also { stage = 20 }
                }
            }

            10 -> options("Yes please.", "No thanks.").also { stage++ }
            11 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HAPPY, "Yes please.").also { stage = 12 }
                    2 -> playerl(FaceAnim.NEUTRAL, "No thanks.").also { stage = 13 }
                }
            12 -> end().also { Tanning.open(player, NPCs.ELLIS_2824) }
            13 -> npcl(FaceAnim.FRIENDLY, "Very well, @g[sir,madam], as you wish.").also { stage = END_DIALOGUE }
            20 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.ASKING, "Can I buy some leather?").also { stage = 21 }
                    2 -> playerl(FaceAnim.SUSPICIOUS, "Leather is rather weak stuff.").also { stage = 22 }
                }
            21 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I make leather from animal hides. Bring me some cowhides and one gold coin per hide, and I'll tan them into soft leather for you.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            22 ->
                npcl(
                    FaceAnim.NOD_YES,
                    "Normal leather may be quite weak, but it's very cheap - I make it from cowhides for only 1 gp per hide - and it's so easy to craft that anyone can work with it.",
                ).also {
                    stage++
                }
            23 ->
                npcl(
                    FaceAnim.HALF_THINKING,
                    "Alternatively you could try hard leather. It's not so easy to craft, but I only charge 3 gp per cowhide to prepare it, and it makes much sturdier armour.",
                ).also {
                    stage++
                }
            24 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I can also tan snake hides and dragonhides, suitable for crafting into the highest quality armour for rangers.",
                ).also {
                    stage++
                }
            25 -> playerl(FaceAnim.NEUTRAL, "Thanks, I'll bear it in mind.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return EllisDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ELLIS_2824)
    }
}
