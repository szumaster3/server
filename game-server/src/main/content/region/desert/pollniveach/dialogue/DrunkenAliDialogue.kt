package content.region.desert.pollniveach.dialogue

import core.api.getVarbit
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Vars

/**
 * Represents the Drunken Ali dialogue.
 */
@Initializable
class DrunkenAliDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val beerCounter = getVarbit(player, Vars.VARBIT_QUEST_THE_FEUD_DRUNKEN_ALI_BEER_COUNT_318)
        when(beerCounter) {
            0 -> npc(FaceAnim.DRUNK, "Ahh, a kind stranger. Get this old man drink so that", "he may wet his throat and tell you of strange", "happenings in this town.").also { stage = END_DIALOGUE }
            4 -> npcl(FaceAnim.DRUNK, "What were we talking about again? Yes yes, when I was a boy..... no that's not it.").also { stage = END_DIALOGUE }
            else -> npcl(FaceAnim.DRUNK, "Get this old man another drink so that he may wet his lips and continue.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean = true

    override fun newInstance(player: Player?): Dialogue = DrunkenAliDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.DRUNKEN_ALI_1863)
}