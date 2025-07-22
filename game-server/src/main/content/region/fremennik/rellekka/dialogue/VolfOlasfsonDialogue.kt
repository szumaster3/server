package content.region.fremennik.rellekka.dialogue

import core.api.hasRequirement
import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class VolfOlasfsonDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        stage = when {
            !isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS) -> {
                npc(FaceAnim.ANNOYED, "Sorry, outlander, but I have things to be doing.")
                END_DIALOGUE
            }
            !hasRequirement(player, Quests.OLAFS_QUEST, false) -> {
                npc(FaceAnim.FRIENDLY, "Hello there. Enjoying the view?")
                0
            }
            else -> {
                npcl(FaceAnim.ASKING, "Hello again, friend! Does my father send any word... or treasures like before?")
                2
            }
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                player(FaceAnim.FRIENDLY, "Yes I am. You have a lovely yurt.")
                stage++
            }
            1 -> {
                npc(FaceAnim.FRIENDLY, "Thanks! I exercise it regularly.")
                stage = END_DIALOGUE
            }
            2 -> {
                playerl(FaceAnim.HALF_GUILTY, "Not today, but if he does, you will be the first to know.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?) = VolfOlasfsonDialogue(player)

    override fun getIds() = intArrayOf(NPCs.VOLF_OLAFSON_3695)
}
