package content.region.misc.dialogue.mosle

import core.api.inInventory
import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class HonestJimmyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "You here for the...stuff?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                if (!inInventory(player, Items.BOOK_O_PIRACY_7144)) {
                    npcl(FaceAnim.FRIENDLY, "Arr? Be ye wantin' te go on account with our gang o' fillibusters?").also {
                        stage =
                            1
                    }
                } else {
                    playerl(FaceAnim.ASKING, "What stuff?").also { stage = 4 }
                }

            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "The powder monkey be takin' a caulk after gettin' rowdy on bumboo, so there be plenty of room for ye.",
                ).also {
                    stage++
                }
            2 -> player(FaceAnim.STRUGGLE, "Riiiiight...").also { stage++ }
            3 -> playerl(FaceAnim.STRUGGLE, "I'll just be over here if you need me.").also { stage = END_DIALOGUE }
            4 -> npcl(FaceAnim.NEUTRAL, "Look pal I got the goods if you have the cash.").also { stage++ }
            5 -> npcl(FaceAnim.NEUTRAL, "We talkin' the same language yet?").also { stage++ }
            6 -> options("Yes, I am here for the stuff.", "I have no idea what you are talking about.").also { stage++ }
            7 ->
                when (buttonId) {
                    1 -> {
                        openNpcShop(player, NPCs.HONEST_JIMMY_4362)
                        end()
                    }
                    2 ->
                        playerl(FaceAnim.STRUGGLE, "I have no idea what you are talking about.").also {
                            stage =
                                END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return HonestJimmyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HONEST_JIMMY_4362)
    }
}
