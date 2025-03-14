package content.region.kandarin.dialogue.guthanoth

import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class OgreGuardNWDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(
            FaceAnim.OLD_DEFAULT,
            "Stop, creature! Only ogres and their friends allowed in this city. Show me a sign of companionship, like a lost relic or somefing, and you may pass.",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.OLD_ANGRY1, "Until then, back to whence you came!").also { stage++ }
            1 -> {
                end()
                sendMessage(player, "The guard pushes you back down the hill.")
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return OgreGuardNWDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OGRE_GUARD_859)
    }
}
