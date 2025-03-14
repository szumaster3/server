package content.region.misthalin.dialogue.varrock

import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class GuidorsWifeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (getQuestStage(player, Quests.BIOHAZARD) >= 16) {
            player("Hello again.").also { stage = 2 }
        } else {
            player(FaceAnim.HALF_GUILTY, "Hello.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Oh hello, I can't chat now. I have to keep an eye on my",
                    "husband. He's very ill!",
                ).also { stage++ }

            1 -> playerl(FaceAnim.HALF_GUILTY, "I'm sorry to hear that!").also { stage = END_DIALOGUE }
            2 -> npc("Hello there. I fear Guidor may not be long for", "this world!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return GuidorsWifeDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUIDORS_WIFE_342)
    }
}
