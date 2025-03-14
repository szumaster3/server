package content.region.misthalin.dialogue.wizardstower

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class DrOnglewipDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.ASKING, "Do you live here too?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Oh no, I come from Gnome Stronghold. I've been",
                    "sent here by King Narnode to learn about human",
                    "magics.",
                ).also { stage++ }

            1 -> player(FaceAnim.ASKING, "So where's this Gnome Stronghold?").also { stage++ }
            2 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "It's in the North West of the continent - a long way",
                    "away. You should visit us there some time. The food's",
                    "great, and the company's delightful.",
                ).also { stage++ }

            3 -> player(FaceAnim.FRIENDLY, "I'll try and make time for it. Sounds like a nice place.").also { stage++ }
            4 ->
                npc(FaceAnim.OLD_NORMAL, "Well, it's full of gnomes. How much nicer could it be?").also {
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PROFESSOR_ONGLEWIP_4585)
    }
}
