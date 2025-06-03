package content.region.kandarin.quest.itwatchtower.dialogue

import core.api.quest.isQuestComplete
import core.game.dialogue.FaceAnim
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Enclave guard dialogue.
 *
 * Relations
 * - [Watchtower Quest][content.region.kandarin.quest.itwatchtower.Watchtower]
 */
@Initializable
class EnclaveGuardDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        if(isQuestComplete(player, Quests.WATCHTOWER)) {
            sendMessage(player, "The guard is occupied at the moment.")
        } else {
            npc(FaceAnim.OLD_DEFAULT, "What do you want?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            0 -> npc(FaceAnim.OLD_DEFAULT, "What do you want?").also { stage++ }
            1 -> options("I want to go in there.", "I want to rid the world of ogres!").also { stage++ }
            2 -> when(buttonId) {
                1 -> player("I want to go in there.").also { stage++ }
                2 -> player("I want to rid the world of ogres.").also { stage = 5 }
            }
            3 -> npc(FaceAnim.OLD_ANGRY1, "Oh you do, do you? How about 'no'?").also { stage++ }
            4 -> {
                end()
                npc?.attack(player)
            }
            5 -> npc(FaceAnim.OLD_ANGRY1, "You dare mock me, creature!").also { stage = 4 }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ENCLAVE_GUARD_870)
}