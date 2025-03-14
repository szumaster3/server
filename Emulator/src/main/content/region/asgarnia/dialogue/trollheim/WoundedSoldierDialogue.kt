package content.region.asgarnia.dialogue.trollheim

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class WoundedSoldierDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Are you OK?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("Urrrgggh.....").also { stage++ }
            1 ->
                npc(
                    "I'll be OK, the trolls only leave the plateau at nightfall.",
                    "The guys are bringing a stretcher shortly.",
                ).also {
                    stage++
                }
            2 -> player("As long as you're sure.").also { stage++ }
            3 ->
                npc(
                    "It's my own fault really, I was having a walk and",
                    "wandered past the danger sign. The trolls throw rocks",
                    "down at any one who goes up the path!",
                ).also {
                    stage++
                }
            4 -> npc("Don't go up there!").also { stage++ }
            5 -> player("OK, thanks for the warning.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SOLDIER_1069)
    }
}
