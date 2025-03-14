package content.region.kandarin.handlers.guilds.ranging

import content.global.skill.crafting.Tanning
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Leather worker dialogue.
 */
@Initializable
class LeatherWorkerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        player("Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("Can I help you?").also { stage++ }
            1 -> options("What do you do here?", "No thanks.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player("What do you do here?").also { stage++ }
                    2 -> player("No thanks.").also { stage = 6 }
                }

            3 -> npc("Well, I can cure plain cowhides into pieces of leather", "ready for crafting.").also { stage++ }
            4 -> npc("I work with ordinary, hard or dragonhide leather and", "also snakeskin.").also { stage++ }
            5 -> end().also { Tanning.open(player, npc.id) }
            6 -> npc("Suit yourself.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LEATHERWORKER_680)
    }
}
