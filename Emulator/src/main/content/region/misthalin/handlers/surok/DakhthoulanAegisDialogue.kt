package content.region.misthalin.handlers.surok

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class DakhthoulanAegisDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Hi there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("Hello, you one. What brings you to our humble", "dwelling?").also { stage++ }
            1 -> player("I was wondering what this place was?").also { stage++ }
            2 ->
                npc(
                    "These are the Tunnels of Chaos. They radiate",
                    "with the energy of chaos magic. At the far end of the",
                    "tunnel, you will find a portal to the Chaos Altar itself",
                    "where chaos runes are crafted!",
                ).also { stage++ }

            3 -> player("Thanks for your time.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DAKHTHOULAN_AEGIS_5840)
    }
}
