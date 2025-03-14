package content.region.kandarin.dialogue.witchaven

import content.region.kandarin.handlers.FishingPlatform
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
class JebDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.SEA_SLUG)) {
            npc(FaceAnim.FRIENDLY, "Hello there.").also { stage = END_DIALOGUE }
        } else {
            playerl(FaceAnim.FRIENDLY, "I understand you can take me to the Fishing Platform.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("Yes, we can do that.").also { stage++ }
            1 -> player("Will you take me please?").also { stage++ }
            2 -> npc("Board the boat and we shall depart.").also { stage++ }
            3 -> {
                end()
                FishingPlatform.sail(player!!, FishingPlatform.Travel.WITCHAVEN_TO_FISHING_PLATFORM)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return JebDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JEB_4895)
    }
}
