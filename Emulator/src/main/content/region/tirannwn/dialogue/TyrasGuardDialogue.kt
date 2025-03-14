package content.region.tirannwn.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class TyrasGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "What is it?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "What's going on around here?",
                    "Do you know what's south of here?",
                    "I'll leave you alone.",
                ).also {
                    stage++
                }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "What's going on around here?").also { stage = 10 }
                    2 -> player(FaceAnim.ASKING, "Do you know what's south of here?").also { stage = 20 }
                    3 -> player(FaceAnim.NEUTRAL, "I'll leave you alone.").also { stage = END_DIALOGUE }
                }
            10 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Sorry, I shouldn't give out sensitive information to civilians. You should go to General Hining, in the camp along the road.",
                ).also {
                    stage++
                }
            11 -> options("Do you know what's south of here?", "Okay, thanks.").also { stage++ }
            12 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "Do you know what's south of here?").also { stage = 20 }
                    2 -> player(FaceAnim.NEUTRAL, "Ok, thanks.").also { stage = END_DIALOGUE }
                }
            20 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "No. We sent a scouting party in that direction, when we first established our camp. Some of the men got lost in the swamps. Eventually we listed them as dead.",
                ).also {
                    stage++
                }
            21 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Then suddenly they returned, with a wild gleam in their eyes, raving about gods and snakes and all kinds of madness.",
                ).also {
                    stage++
                }
            22 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "We had to drive them out, in case their condition infected the rest of the troops. Their wives will be given a full widow pension when we return home.",
                ).also {
                    stage++
                }
            23 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "General Hining concluded that, whatever is down there, it's not affiliated with any of the elf factions, and it should be left alone.",
                ).also {
                    stage++
                }
            24 -> player(FaceAnim.FRIENDLY, "Thank you.").also { stage = 0 }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return TyrasGuardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TYRAS_GUARD_1203)
    }
}
