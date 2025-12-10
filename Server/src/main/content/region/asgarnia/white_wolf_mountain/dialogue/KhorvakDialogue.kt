package content.region.asgarnia.white_wolf_mountain.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Khorvak, a dwarven engineer dialogue.
 */
@Initializable
class KhorvakDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        player(FaceAnim.FRIENDLY, "Hello there.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.OLD_DRUNK_LEFT, "Ooooh, heeellooo! Anyshing a bright dwarf like me can help you with?").also { stage++ }
            1 -> playerl(FaceAnim.HALF_THINKING, "The only thing you can help me with is finding the nearest place to get a drink, I think...").also { stage++ }
            2 -> npcl(FaceAnim.OLD_DRUNK_LEFT, "That ish shimple! Help yourshelf to the dwarvern shtout! I mean dwarfen! I mean... I'm not shure what I mean.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = KhorvakDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.KHORVAK_A_DWARVEN_ENGINEER_1842)
}
