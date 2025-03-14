package content.region.misthalin.dialogue.varrock

import content.region.wilderness.dialogue.RogueCastleDialogue
import core.api.inBorders
import core.api.openDialogue
import core.api.quest.hasRequirement
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class RogueVarrockDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!hasRequirement(player, "Summer's End")) return true
        if (inBorders(player, 3282, 3934, 3287, 3942)) {
            end()
            openDialogue(player, RogueCastleDialogue())
            return true
        }
        npcl(FaceAnim.FRIENDLY, "Welcome to my humble abode. What can I do for you?")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("You mentioned a business.", "Nothing.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "Up at the Rogues' Castle, you mentioned a business. What was that about?",
                        ).also {
                            stage++
                        }
                    2 -> playerl(FaceAnim.NEUTRAL, "Nothing.").also { stage = END_DIALOGUE }
                }
            2 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ah, yes, I run...what do you call it? Something of a little jewellery business. So, if you happen to come across any unenchanted gold jewellery, I'll gladly take it off your hands for a decent sum of coins.",
                ).also {
                    stage++
                }
            3 -> playerl(FaceAnim.HALF_ASKING, "Only gold jewellery?").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Correct. I'm not buying anything that isn't beautiful, bright gold. My...eh...clients are picky about that.",
                ).also {
                    stage++
                }
            5 -> options("I have some right here.", "Alright, I'll remember that.").also { stage++ }
            6 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.NEUTRAL, "I have some right here.").also { stage++ }
                    2 -> playerl(FaceAnim.NEUTRAL, "Alright, I'll remember that.").also { stage = END_DIALOGUE }
                }
            7 ->
                npcl(FaceAnim.FRIENDLY, "Good, good. Just hand it over and I'll see what I can give for it.").also {
                    stage =
                        END_DIALOGUE
                }
        }

        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return RogueVarrockDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ROGUE_8122)
    }
}
