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
class IngridHradsonDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            npcl(FaceAnim.ANNOYED, "Outlander, I have work to be getting on with... Please stop bothering me.").also {
                stage =
                    END_DIALOGUE
            }
        } else if (isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS) &&
            !hasRequirement(player, Quests.OLAFS_QUEST, false)
        ) {
            npc(FaceAnim.FRIENDLY, "Good afternoon! How do you like our village?").also { stage = 0 }
        } else if (isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS) &&
            hasRequirement(player, Quests.OLAFS_QUEST, false)
        ) {
            npc(FaceAnim.ASKING, "Hello again! Have you any word from my husband?").also { stage = 2 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "It's lovely. You have a fine collection of goats.").also { stage++ }
            1 ->
                npcl(FaceAnim.FRIENDLY, "We polish them every day to get them nice and clean.").also {
                    stage =
                        END_DIALOGUE
                }
            2 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "Err, no, not yet. It takes a while for the messages to reach me you know.",
                ).also {
                    stage++
                }
            3 ->
                npcl(FaceAnim.FRIENDLY, "Well, when you do, tell him we'll be more than happy to see him again.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return IngridHradsonDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.INGRID_HRADSON_3696)
    }
}
