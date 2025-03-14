package content.region.kandarin.dialogue.ardougne

import core.api.openDialogue
import core.api.quest.isQuestInProgress
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class JericoDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isQuestInProgress(player, Quests.BIOHAZARD, 1, 100)) {
            end()
            openDialogue(
                player,
                content.region.kandarin.quest.biohazard.dialogue
                    .JericoDialogue(),
            )
        } else {
            npcl(FaceAnim.SUSPICIOUS, "Hello.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.NEUTRAL, "Can I help you?").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "Just passing by.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return JericoDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JERICO_366)
    }
}
