package content.region.misc.dialogue.mosle

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
class BillTeachDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!inInventory(player, Items.BOOK_O_PIRACY_7144)) {
            options("Speak about Rocking Out.", "Talk about something else").also { stage = 1 }
        } else {
            core.api.sendDialogue(player, "Bill Teach is not interested in talking.")
            stage = END_DIALOGUE
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 ->
                when (buttonId) {
                    1 -> end()
                    2 ->
                        if (!inInventory(player, Items.BOOK_O_PIRACY_7144)) {
                            playerl(FaceAnim.SAD, "Cap'n, I lost that book you gave me.").also { stage = 2 }
                        } else {
                            npc("If yer after a lift go to the ship and I'll take ye where ye", " want to go.").also {
                                stage =
                                    END_DIALOGUE
                            }
                        }
                }
            2 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Ye'll notice I'm not fallin' to the floor in shock right",
                    "now.",
                ).also { stage++ }
            3 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Look, if I gives ye another copy will ye keep a better",
                    "eye on it?",
                ).also { stage++ }
            4 -> player(FaceAnim.HAPPY, "Aye Cap'n!").also { stage++ }
            5 ->
                npc(FaceAnim.NEUTRAL, "Then here, have this other copy I dug up.").also {
                    addItemOrDrop(player, Items.BOOK_O_PIRACY_7144)
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BillTeachDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BILL_TEACH_3155)
    }
}
