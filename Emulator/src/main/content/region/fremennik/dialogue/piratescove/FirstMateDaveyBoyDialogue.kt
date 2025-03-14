package content.region.fremennik.dialogue.piratescove

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FirstMateDaveyBoyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when ((1..2).random()) {
            1 -> playerl(FaceAnim.HALF_ASKING, "What does it take to become first mate on a ship?").also { stage = 0 }
            2 ->
                npcl(
                    FaceAnim.ANNOYED,
                    "It is customary when stowing away on a vessel to not introduce yourself to the Captains First Mate, oh foolish one.",
                ).also {
                    stage =
                        10
                }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.LAUGH,
                    "Good question. We have a diplomatic consession at the turn of the financial year. Said pirate is chosen should the existing mate be absent without leave.",
                ).also {
                    stage++
                }
            1 ->
                playerl(
                    FaceAnim.THINKING,
                    "I had no idea. I always figured it was all about popularity.",
                ).also { stage++ }
            2 -> npc(FaceAnim.FRIENDLY, "It is. I'm just pulling your leg.").also { stage++ }
            3 -> player(FaceAnim.FRIENDLY, "Oh....").also { stage = END_DIALOGUE }
            10 -> player(FaceAnim.ANNOYED, "Hey! I'm not a stowaway!").also { stage++ }
            11 -> player(FaceAnim.ANNOYED, "That Lokar guy invited me aboard...").also { stage++ }
            12 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I see. Well, don't distract me as I'm making preparations for departure.",
                ).also {
                    stage++
                }
            13 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Try not to distract any of the crew either, Zamorak knows it's hard enough to get them to do any work around here without strangers wandering round the ship asking them inane questions.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return FirstMateDaveyBoyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FIRST_MATE_DAVEY_BOY_4543)
    }
}
