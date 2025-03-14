package content.region.misc.dialogue.zanaris

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class CoordinatorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hello, what are you doing?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.OLD_DISTRESSED, randomDialogue.random()).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return CoordinatorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CO_ORDINATOR_3302)
    }

    companion object {
        val randomDialogue =
            arrayOf(
                "Sorry, I don't have time for idle chit-chat, I need to find a Winter Fairy to send to Trollheim!",
                "Sorry, I don't have time for idle chit-chat, I need to send a fairy to get little Freddie's tooth!",
                "Sorry, I don't have time for idle chit-chat, I need to send an Autumn Fairy off to Burthorpe!",
                "Sorry, I don't have time to talk, I need to send a Tooth Fairy to visit Sarah-Jane!",
                "Sorry, I don't have time to stop, I need to send a weather fairy off to Etceteria!",
            )
    }
}
