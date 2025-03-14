package content.region.misc.dialogue.keldagrim

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class DrunkenDwarfDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hello there! Are you alright?").also { stage++ }
            1 -> npcl(FaceAnim.OLD_DRUNK_RIGHT, "Of courshe! Why why why *hic* why shouldn't I be?").also { stage++ }
            2 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Hey, you look vaguely familiar... haven't I seen you somewhere before?",
                ).also {
                    stage++
                }
            3 -> npcl(FaceAnim.OLD_DRUNK_RIGHT, "That'sh not likely... I've been here for eh...").also { stage++ }
            4 -> npcl(FaceAnim.OLD_DRUNK_RIGHT, "I've been here for...").also { stage++ }
            5 -> npcl(FaceAnim.OLD_DRUNK_RIGHT, "Yearsh!").also { stage++ }
            6 ->
                npcl(
                    FaceAnim.OLD_DRUNK_RIGHT,
                    "I've been looking after thish houshe for my... my coushin? Or wash it my brother? A family member anyway.",
                ).also {
                    stage++
                }
            7 -> playerl(FaceAnim.FRIENDLY, "And where is this... family member of yours?").also { stage++ }
            8 ->
                npcl(
                    FaceAnim.OLD_DRUNK_RIGHT,
                    "All over, all over... he shaish he went off to wander all over Gielinor. Don't remember why.",
                ).also {
                    stage++
                }
            9 -> playerl(FaceAnim.FRIENDLY, "Hmm... I wonder, I may have met him...").also { stage++ }
            10 ->
                npcl(
                    FaceAnim.OLD_DRUNK_RIGHT,
                    "He comesh back sometimesh... saysh he needsh more kebabsh.",
                ).also { stage++ }
            11 ->
                npcl(
                    FaceAnim.OLD_DRUNK_RIGHT,
                    "Or ish that jusht a dream? Sometimesh I dream kebabsh invade Keldi... Keldu... Keldashomething.",
                ).also {
                    stage++
                }
            12 ->
                npcl(
                    FaceAnim.OLD_DRUNK_RIGHT,
                    "When you eatsh them, they take over your mind, they do! An army of mindlesh kebab eating dwarvesh!",
                ).also {
                    stage++
                }
            13 -> playerl(FaceAnim.FRIENDLY, "Er, yes, well, I think I should be off now.").also { stage++ }
            14 -> npcl(FaceAnim.OLD_DRUNK_RIGHT, "THE KEBABSH WILL COME FOR YOU!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return DrunkenDwarfDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DRUNKEN_DWARF_2203)
    }
}
