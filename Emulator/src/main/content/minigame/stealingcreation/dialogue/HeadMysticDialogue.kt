package content.minigame.stealingcreation.dialogue

import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HeadMysticDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 ->
                if (!GameWorld.settings!!.isMembers) {
                    sendDialogue(player!!, "I'll only talk to you in a members' only world.").also {
                        stage = END_DIALOGUE
                    }
                } else {
                    end()
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.HEAD_MYSTIC_8227)
}
