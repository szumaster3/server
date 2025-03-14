package content.region.misthalin.dialogue.varrock

import core.api.interaction.openNpcShop
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class ScavvoDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "'Ello matey! D'ya wanna buy some exiting new toys?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                showTopics(
                    Topic("No - toys are for kids.", END_DIALOGUE),
                    Topic("Let's have a look, then.", 6),
                    Topic(FaceAnim.HAPPY, "Ooh, goody-goody - toys!", 6),
                    IfTopic(
                        FaceAnim.HALF_ASKING,
                        "Why do you sell most rune armour but not platebodies?",
                        5,
                        !isQuestComplete(player, Quests.DRAGON_SLAYER),
                    ),
                )
            5 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Oh, you have to complete a special quest in order to wear rune platebodies. You should talk to the guild master downstairs about that",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            6 -> {
                end()
                openNpcShop(player, NPCs.SCAVVO_537)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SCAVVO_537)
    }
}
