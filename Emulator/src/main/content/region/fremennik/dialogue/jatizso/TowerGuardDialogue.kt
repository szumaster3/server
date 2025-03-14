package content.region.fremennik.dialogue.jatizso

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class TowerGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.HALF_ASKING, "What are you doing here?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.NEUTRAL, "I'M ON SHOUTING DUTY!").also { stage++ }
            1 -> playerl(FaceAnim.HALF_THINKING, "No need to shout.").also { stage++ }
            2 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "I'M SORRY I'VE BEEN SHOUTING",
                    "INSULTS SO LONG I CAN'T HELP IT!",
                ).also { stage++ }
            3 -> playerl(FaceAnim.HALF_THINKING, "Who are you insulting?").also { stage++ }
            4 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "THE TOWER IN ${if (npc.id == NPCs.GUARD_5489) "NEITIZNOT" else "JATIZSO"}.",
                    "THEY SHOUT INSULTS BACK.",
                ).also {
                    stage++
                }
            5 -> playerl(FaceAnim.ASKING, "Err, why?").also { stage++ }
            6 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "THE ${if (npc.id == NPCs.GUARD_5489) "KING" else "BURGHER"} TELLS US TO.",
                ).also { stage++ }
            7 ->
                playerl(
                    FaceAnim.HALF_THINKING,
                    "Your ${if (npc.id == NPCs.GUARD_5489) "King" else "Burgher"} is a strange person.",
                ).also {
                    stage++
                }
            8 -> options("Can I watch? I'm curious.", "Oh well, I'd better get going.").also { stage++ }
            9 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.ASKING, "Can I watch? I'm curious.").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_THINKING, "Oh well, I'd better get going.").also { stage = END_DIALOGUE }
                }
            10 ->
                npcl(FaceAnim.NEUTRAL, "IF YOU LIKE!").also {
                    stage = END_DIALOGUE
                    InteractionListeners.run(NPCs.GUARD_5489, IntType.NPC, "watch-shouting", player, npc)
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return TowerGuardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUARD_5489, NPCs.GUARD_5490)
    }
}
