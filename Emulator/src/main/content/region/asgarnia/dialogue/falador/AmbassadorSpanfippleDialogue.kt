package content.region.asgarnia.dialogue.falador

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AmbassadorSpanfippleDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_DEFAULT, "It's all very white round here, isn't it?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.THINKING, "Well, it is the White Knights' Castle.").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "I think it would all look better in pink. At least then I wouldn't be squinting all the time.",
                ).also {
                    stage++
                }
            2 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Yes, but then they'd have to become the Pink Knights. I think they'd have problems recruiting then.",
                ).also {
                    stage++
                }
            3 -> npc(FaceAnim.OLD_DEFAULT, "You're probably right. Maybe brown, then.").also { stage++ }
            4 -> player(FaceAnim.HALF_THINKING, "I think that may be worse...").also { stage++ }
            5 -> npc(FaceAnim.OLD_ANGRY1, "Bah, humans have no sense of style...").also { stage = END_DIALOGUE }
            99 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AMBASSADOR_SPANFIPPLE_4581)
    }
}
