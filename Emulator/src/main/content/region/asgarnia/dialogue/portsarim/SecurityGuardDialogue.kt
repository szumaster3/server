package content.region.asgarnia.dialogue.portsarim

import core.api.addItemOrDrop
import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class SecurityGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_NORMAL, "Hiya. I'm giving out free books that teach you how to", "keep your account secure.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                if (!inInventory(player, Items.SECURITY_BOOK_9003)) {
                    addItemOrDrop(player, Items.SECURITY_BOOK_9003)
                    player("Oh? Thanks.").also { stage++ }
                } else {
                    playerl(FaceAnim.HALF_GUILTY, "I don't have space to take anything from you at the moment.").also {
                        stage =
                            2
                    }
                }

            1 -> npc(FaceAnim.OLD_NORMAL, "You're welcome.").also { stage = END_DIALOGUE }
            2 -> npc(FaceAnim.OLD_NORMAL, "Okay. Have a nice day.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SECURITY_GUARD_4375)
    }
}
