package content.region.morytania.dialogue.canifis

import content.region.morytania.quest.route.dialogue.VanstromKlauseQuestDialogue
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.Quests

@Initializable
class VanstromKlauseDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Hello there, how goes it stranger?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.HALF_GUILTY, "Quite well thanks for asking, how about you?").also { stage++ }
            1 -> npcl(FaceAnim.HALF_GUILTY, "Quite well my self.").also { stage++ }
            2 -> {
                when {
                    getQuestStage(player, Quests.IN_SEARCH_OF_THE_MYREQUE) > 1 -> {
                        openDialogue(player, VanstromKlauseQuestDialogue(), npc)
                    }

                    else -> end()
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return VanstromKlauseDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(2020)
    }
}
