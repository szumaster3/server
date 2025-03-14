package content.region.misthalin.handlers.surok

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MishkalunDornDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("You are excused. And you are welcome.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player("Excuse me...er..thanks.").also { stage++ }
            1 -> npc("We are the Order of the Dagon'hai.").also { stage++ }
            2 -> player("Who are you?").also { stage++ }
            3 -> npc("Through my magic, I can see a short way into", "the future.").also { stage++ }
            4 -> player("How do you seem to know what i'm going to", "say? ...Er...oh.").also { stage++ }
            5 -> npc("These are the Tunnels of Chaos.").also { stage++ }
            6 -> player("What is...uh..aha! I'm not going to ask that. So you got it", "wrong!").also { stage++ }
            7 -> npc("Indeed. You are very clever.").also { stage++ }
            8 -> player("So I won!").also { stage++ }
            9 -> npc("Yes.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MISHKALUN_DORN_5839)
    }
}
