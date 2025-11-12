package content.region.kandarin.east_ardougne.dialogue

import content.region.kandarin.east_ardougne.quest.biohazard.dialogue.OmartBiohazardQuestDialogue
import core.api.getQuestStage
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Omart dialogue.
 */
@Initializable
class OmartDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val questStage = getQuestStage(player, Quests.BIOHAZARD)
        when (questStage) {
            in 2..99 -> end().also { openDialogue(player, OmartBiohazardQuestDialogue()) }
            100 ->  player("Hello Omart.").also { stage = 3 }
            else -> player("Hello there.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            0 -> npc("Hello.").also { stage++ }
            1 -> player(FaceAnim.HALF_ASKING, "How are you?").also { stage++ }
            2 -> npc(FaceAnim.HAPPY, "Fine thanks.").also { stage = END_DIALOGUE }
            3 -> npcl(FaceAnim.FRIENDLY, "Hello Adventurer.").also { stage++ }
            4 -> npcl(FaceAnim.FRIENDLY, "I'm afraid it's too risky to use the ladder again, but I believe that Edmond's working on another tunnel.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = OmartDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.OMART_350)
}
