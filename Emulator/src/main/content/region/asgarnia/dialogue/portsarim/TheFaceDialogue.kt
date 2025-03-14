package content.region.asgarnia.dialogue.portsarim

import core.api.quest.isQuestComplete
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class TheFaceDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!GameWorld.settings!!.isMembers) {
            npc("I have no time for you.").also { stage = END_DIALOGUE }
        } else {
            player("Hello.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                if (!isQuestComplete(player, Quests.RATCATCHERS)) {
                    sendDialogue(player!!, "She looks through you as if you don't exist.").also { stage = END_DIALOGUE }
                } else {
                    npcl(FaceAnim.FRIENDLY, "Oh it's you again.").also { stage++ }
                }
            1 -> sendDialogue(player!!, "*She glances at you with barely disguised disdain.*").also { stage++ }
            2 -> player(FaceAnim.FRIENDLY, "*Aside* I wonder what her problem is?").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I must congratulate you on your performance, I have only ever seen one other display of musicality to match, as you should know.",
                ).also {
                    stage++
                }
            4 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Thank you, and don't worry, I won't tell Felkrash that you helped me.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.THE_FACE_2950)
    }
}
