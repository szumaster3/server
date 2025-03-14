package content.region.fremennik.dialogue.piratescove

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class EagleEyeShultzDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        playerl(
            FaceAnim.HALF_ASKING,
            "What do you do for fun on this ship? You know, when you're not doing pirate stuff.",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "All sorts! Hide and seek, pin the patch on the pirate, walk the plank!",
                ).also { stage++ }
            1 ->
                playerl(
                    FaceAnim.WORRIED,
                    "What a life! Wait a minute. 'Walk the plank'? Surely that's a bit dangerous?",
                ).also {
                    stage++
                }
            2 -> npcl(FaceAnim.LAUGH, "Well of course, but where's the fun without a few deaths?").also { stage++ }
            3 -> player(FaceAnim.HALF_THINKING, "I think I'll stick to Runelink.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return EagleEyeShultzDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.EAGLE_EYE_SHULTZ_4542)
    }
}
