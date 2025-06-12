package content.region.tirannwn.dialogue

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the General Hining dialogue.
 */
@Initializable
class GeneralHiningDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Hello.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            0 -> npc("Good day adventurer.").also { stage++ }
            1 -> player("I'm surprised you're still here.", "Are you not going back to Ardougne?").also { stage++ }
            2 -> npc("King Thoros and the Prifddinas Elders have agreed that we continue to maintain this outpost. We're now able to rotate our troops between here and Ardougne though, so they can spend more time with their families.").also { stage++ }
            3 -> player("Good to hear. Anyway, I'll leave you to it.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GENERAL_HINING_7121)
    }
}
