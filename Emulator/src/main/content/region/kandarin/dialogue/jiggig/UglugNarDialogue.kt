package content.region.kandarin.dialogue.jiggig

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class UglugNarDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        options("Hello again.", "What are you selling?", "Ok, thanks.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 -> player("Hello again.").also { stage++ }
                    2 -> player("What are you selling?").also { stage = 5 }
                    3 -> player("Ok, thanks.").also { stage = END_DIALOGUE }
                }
            1 -> npc(FaceAnim.OLD_DEFAULT, "Hey yous creature...yous still here?").also { stage++ }
            2 -> player("Yeah, I'm going to help Grish by figuring out what went", "on here.").also { stage++ }
            3 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "If yous finds somefin for da sickies, yous brings to",
                    "me...and I's gives you bright pretties, den me",
                    "make more for alls pepels.",
                ).also {
                    stage++
                }
            4 -> player("Hmm, ok, I'll try to bear that in mind.").also { stage = END_DIALOGUE }
            5 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Me's not got no glug-glugs to sell, yous bring me da sickies",
                    "glug-glug den me's open da stufsies for ya.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return UglugNarDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.UGLUG_NAR_2039)
    }
}
