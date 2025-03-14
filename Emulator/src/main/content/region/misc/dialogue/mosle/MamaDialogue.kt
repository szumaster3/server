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
class MamaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Hello!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                if (!inInventory(player, Items.BOOK_O_PIRACY_7144)) {
                    npcl(FaceAnim.FRIENDLY, "Ar, darlin'! How might ya' Mama help ye?").also { stage++ }
                } else {
                    npcl(FaceAnim.FRIENDLY, "Hello stranger, you come for a drink?").also { stage = 4 }
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
            4 -> playerl(FaceAnim.HALF_ASKING, "What sort of drinks do you have?").also { stage++ }
            5 -> npcl(FaceAnim.FRIENDLY, "We got some cold beers, some warm stews and some 'rum'.").also { stage++ }
            6 -> playerl(FaceAnim.HALF_ASKING, "Not, Captain Braindeath's 'rum'?").also { stage++ }
            7 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes! There was some sorta problem at the brewery, but they got it all sorted out now.",
                ).also {
                    stage++
                }
            8 -> npcl(FaceAnim.FRIENDLY, "You wanna buy something?").also { stage++ }
            9 -> options("Yes", "No").also { stage++ }
            10 ->
                when (buttonId) {
                    1 -> end().also { openNpcShop(player, NPCs.MAMA_3164) }
                    2 -> player("Nevermind.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return MamaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MAMA_3164)
    }
}
