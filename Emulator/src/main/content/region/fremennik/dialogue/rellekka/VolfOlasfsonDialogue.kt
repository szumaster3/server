package content.region.fremennik.dialogue.rellekka

import core.api.quest.hasRequirement
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class VolfOlasfsonDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            npc(FaceAnim.ANNOYED, "Sorry, outlander, but I have things to be doing.").also { stage = END_DIALOGUE }
        } else if (isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS) &&
            !hasRequirement(player, Quests.OLAFS_QUEST, false)
        ) {
            npc(FaceAnim.FRIENDLY, "Hello there. Enjoying the view?").also { stage = 0 }
        } else if (isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS) &&
            hasRequirement(player, Quests.OLAFS_QUEST, false)
        ) {
            npcl(
                FaceAnim.ASKING,
                "Hello again, friend! Does my father send any word... or treasures like before?",
            ).also {
                stage =
                    2
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.FRIENDLY, "Yes I am. You have a lovely yurt.").also { stage++ }
            1 -> npc(FaceAnim.FRIENDLY, "Thanks! I exercise it regularly.").also { stage = END_DIALOGUE }
            2 ->
                playerl(FaceAnim.HALF_GUILTY, "Not today, but if he does, you will be the first to know.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return VolfOlasfsonDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.VOLF_OLAFSON_3695)
    }
}
