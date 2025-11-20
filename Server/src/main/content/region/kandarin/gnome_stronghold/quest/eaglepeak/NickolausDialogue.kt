package content.region.kandarin.gnome_stronghold.quest.eaglepeak

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

@Initializable
class NickolausDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Hello.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.NEUTRAL, "Shhhh! You'll scare the kebbits.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = NickolausDialogue(player)

    override fun getIds(): IntArray = intArrayOf(5125,NPCs.NICKOLAUS_5126,NPCs.NICKOLAUS_5127,NPCs.NICKOLAUS_5128,NPCs.NICKOLAUS_5129)
}