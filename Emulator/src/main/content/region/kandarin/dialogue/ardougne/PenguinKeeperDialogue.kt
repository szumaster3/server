package content.region.kandarin.dialogue.ardougne

import core.api.addItemOrDrop
import core.api.getStatLevel
import core.api.hasAnItem
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class PenguinKeeperDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.HALF_ASKING, "Hello there. How are the penguins doing today?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val hasPenguinEgg = hasAnItem(player, Items.PENGUIN_EGG_12483).container != null
        when (stage) {
            0 ->
                if (hasPenguinEgg && getStatLevel(player, Skills.SUMMONING) >= 30) {
                    npcl(FaceAnim.FRIENDLY, "They are doing fine, thanks.").also { stage = END_DIALOGUE }
                } else {
                    npcl(FaceAnim.FRIENDLY, "They are doing fine, thanks.").also { stage++ }
                }
            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Actually, you might be able to help me with something - if you are interested.",
                ).also {
                    stage++
                }
            2 -> playerl(FaceAnim.FRIENDLY, "What do you mean?").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Well, you see, the penguins have been laying so many eggs recently that we can't afford to raise them all ourselves.",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    FaceAnim.HALF_ASKING,
                    "You seem to know a bit about raising animals - would you like to raise a penguin for us?",
                ).also {
                    stage++
                }
            5 -> options("Yes, of course.", "No thanks.").also { stage++ }
            6 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Yes, of course.").also { stage = 8 }
                    2 -> playerl(FaceAnim.FRIENDLY, "No thanks.").also { stage = 7 }
                }
            7 ->
                npcl(FaceAnim.FRIENDLY, "Fair enough. The offer still stands if you change your mind.").also {
                    stage =
                        END_DIALOGUE
                }
            8 -> npcl(FaceAnim.FRIENDLY, "Wonderful!").also { stage++ }
            9 -> npcl(FaceAnim.FRIENDLY, "Here you go - this egg will hatch into a baby penguin.").also { stage++ }
            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "They eat raw fish and aren't particularly fussy about anything, so it won't be any trouble to raise.",
                ).also {
                    stage++
                }
            11 -> playerl(FaceAnim.FRIENDLY, "Okay, thank you very much.").also { stage++ }
            12 -> {
                end()
                addItemOrDrop(player!!, Items.PENGUIN_EGG_12483)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return PenguinKeeperDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PENGUIN_KEEPER_6891)
    }
}
