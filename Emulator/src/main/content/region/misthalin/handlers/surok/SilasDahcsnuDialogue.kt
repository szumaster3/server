package content.region.misthalin.handlers.surok

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SilasDahcsnuDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Hello there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("Can't you see that I'm busy here?").also { stage++ }
            1 -> player("Oh, Sorry, you don't look very busy.").also { stage++ }
            2 -> npc("Don't look busy? I've got a lot of important work to do", "here.").also { stage++ }
            3 -> player("Really? What do you do?").also { stage++ }
            4 -> npc("That doesn't concern you. What are you doing", "here anyway?").also { stage++ }
            5 -> player("None of your business!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SILAS_DAHCSNU_5841)
    }
}
