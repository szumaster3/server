package content.region.misthalin.quest.losttribe.dialogue

import content.region.misthalin.quest.losttribe.handlers.GoblinFollower
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class KazgarDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = (args[0] as NPC).getShownNPC(player)
        npc(FaceAnim.OLD_NORMAL, "Hello, surface-dweller.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Who are you?", "Can you show me the way to the mine?").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("Who are you?").also { stage = 10 }
                    2 -> player("Can you show me the way to the mine?").also { stage = 20 }
                }

            10 ->
                npc(FaceAnim.OLD_NORMAL, "I'm Kazgar, I guide people through the mines.").also {
                    stage = 1000
                }

            20 -> npc(FaceAnim.OLD_NORMAL, "All right. Follow me.").also { stage++ }
            21 ->
                end().also {
                    GoblinFollower.sendToMines(player)
                }

            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MISTAG_2085)
    }
}
