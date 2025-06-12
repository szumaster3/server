package content.region.morytania.port_phasmatys.dialogue

import core.api.inEquipment
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class GhostSailorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
            npcl(FaceAnim.FRIENDLY, "Woooo wooo wooooo woooo").also { stage = 3 }
            return true
        }
        player(
            "Hi there. Why do you still bother having ships here? I",
            "mean - you're dead, what use are they to you?",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    "We keep ships because we still need trade in",
                    "Phasmatys. Every trader that comes to Phastmatys is",
                    "made to worship the Ectofuntus, so that the Ectopower",
                    "doesn't run out.",
                ).also { stage++ }

            1 -> player("So, without traders to worship in the Temple you're", "history right?").also { stage++ }
            2 -> npc("Aye, matey.").also { stage = END_DIALOGUE }
            3 -> sendDialogue(player, "You cannot understand the ghost.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GHOST_SAILOR_1703)
}
