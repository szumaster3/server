package content.minigame.magearena.dialogue

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class LundailDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Hello sir.").also { stage = 0 }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("How can I help you, brave adventurer?").also { stage++ }
            1 -> options("What are you selling?", "What's that big old building above us?").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player("What are you selling?").also { stage++ }
                    2 -> player("What's that big old building above us?").also { stage = 5 }
                }

            3 ->
                npc(
                    "I sell rune stones. I've got some good stuff, some really",
                    "powerful little rocks. Take a look.",
                ).also { stage++ }

            4 -> end().also { openNpcShop(player, NPCs.LUNDAIL_903) }
            5 ->
                npc(
                    "That, my friend is the mage battle arena. Top mages",
                    "come from all over " + settings!!.name + " to compete in the arena.",
                ).also { stage++ }

            6 -> player("Wow.").also { stage++ }
            7 -> npc("Few return, most get fried, hence the smell.").also { stage++ }
            8 -> player("Hmmm... I did notice.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LUNDAIL_903)
    }
}
