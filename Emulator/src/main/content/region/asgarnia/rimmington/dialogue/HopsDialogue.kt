package content.region.asgarnia.rimmington.dialogue

import content.region.kandarin.ardougne.east.quest.biohazard.plugin.BiohazardUtils
import core.api.getAttribute
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class HopsDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> {
                end()
                if (getQuestStage(player, Quests.BIOHAZARD) < 1) {
                    sendDialogue(player, "Hops doesn't feel like talking.")
                } else if (getAttribute(player, BiohazardUtils.THIRD_VIAL_CORRECT, true)) {
                    openDialogue(player, HopsDialogue())
                } else {
                    npcl(FaceAnim.NEUTRAL, "I suppose I'd better get going. I'll meet you at the Dancing Donkey Inn.")
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.HOPS_340)
}
