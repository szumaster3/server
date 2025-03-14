package content.region.karamja.dialogue.tzhaar

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class TzHaarMejKahDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.CHILD_GUILTY, "You want help JalYt-Ket-" + player.username + "?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "What is this place?",
                    "Who's the current champion?",
                    "What did you call me?",
                    "No I'm fine thanks.",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "What is this place?").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_GUILTY, "Who's the current champion?").also { stage = 21 }
                    3 -> player(FaceAnim.HALF_GUILTY, "What did you call me?").also { stage = 30 }
                    4 -> player(FaceAnim.HALF_GUILTY, "No I'm fine thanks.").also { stage = END_DIALOGUE }
                }

            30 -> npc(FaceAnim.CHILD_GUILTY, "Are you not JalYt-Ket?").also { stage++ }
            31 -> player(FaceAnim.HALF_GUILTY, "I guess so...").also { stage++ }
            32 -> npc(FaceAnim.CHILD_GUILTY, "Well then, no problems.").also { stage = END_DIALOGUE }
            10 ->
                npc(
                    FaceAnim.CHILD_GUILTY,
                    "This is the fight pit, TzHaar-Xil made it for their sport",
                    "but many JalYt come here to fight too.",
                    "If you are wanting to fight then enter the cage, you",
                    "will be summoned when the next round is ready to begin.",
                ).also { stage++ }

            11 -> options("Are there any rules?", "Ok thanks.").also { stage++ }
            12 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Are there any rules?").also { stage = 14 }
                    2 -> player(FaceAnim.HALF_GUILTY, "Ok thanks.").also { stage = END_DIALOGUE }
                }

            14 ->
                npc(
                    FaceAnim.CHILD_GUILTY,
                    "No rules, you use whatever you want. Last person",
                    "standing wins and is declared champion, they stay in",
                    "the pit for the next fight.",
                ).also { stage++ }

            15 -> options("Do I win anything?", "Sounds good.").also { stage++ }
            16 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Do I win anything?").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "Sounds good.").also { stage = END_DIALOGUE }
                }

            17 ->
                npc(
                    FaceAnim.CHILD_GUILTY,
                    "You ask a lot of questions.",
                    "Champion gets TokKul as reward, but more fights the more",
                    "TokKul they get.",
                ).also { stage++ }

            18 -> player(FaceAnim.HALF_GUILTY, "...").also { stage++ }
            19 -> npc(FaceAnim.CHILD_GUILTY, "Before you ask, TokKul is like your coins.").also { stage = 400 }
            20 ->
                npc(
                    FaceAnim.CHILD_GUILTY,
                    "Gold is like you JalYt, soft and easily broken, we use",
                    "hard rock forged in fire like TzHaar!",
                ).also { stage = END_DIALOGUE }

            21 ->
                npc(
                    FaceAnim.CHILD_GUILTY,
                    "Ah that would be " + npc.getAttribute("fp_champion", "JalYt-Ket-Emperor"),
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TZHAAR_MEJ_KAH_2618)
    }
}
