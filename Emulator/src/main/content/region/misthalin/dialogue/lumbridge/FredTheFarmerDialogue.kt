package content.region.misthalin.dialogue.lumbridge

import content.region.misthalin.quest.sheep.dialogue.FredTheFarmerDialogue
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class FredTheFarmerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.SHEEP_SHEARER) in 1..99) {
            openDialogue(player, FredTheFarmerDialogue(getQuestStage(player, Quests.SHEEP_SHEARER)), npc)
        } else {
            npc(
                FaceAnim.ANGRY,
                "What are you doing on my land? You're not the one",
                "who keeps leaving all my gates open and letting out all",
                "my sheep are you?",
            ).also {
                stage =
                    START_DIALOGUE
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE ->
                showTopics(
                    IfTopic(
                        FaceAnim.NEUTRAL,
                        "I'm looking for a quest.",
                        1000,
                        getQuestStage(player!!, Quests.SHEEP_SHEARER) == 0,
                    ),
                    Topic(FaceAnim.HALF_GUILTY, "I'm looking for something to kill.", 100),
                    Topic(FaceAnim.HALF_GUILTY, "I'm lost.", 200),
                )
            100 ->
                npc(FaceAnim.HALF_GUILTY, "What, on my land? Leave my livestock alone you", "scoundrel!").also {
                    stage =
                        END_DIALOGUE
                }
            200 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "How can you be lost? Just follow the road east and",
                    "south. You'll end up in Lumbridge fairly quickly.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            1000 -> openDialogue(player, FredTheFarmerDialogue(getQuestStage(player, Quests.SHEEP_SHEARER)), npc)
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.FRED_THE_FARMER_758)
}
