package content.region.kandarin.ardougne.west.dialogue

import core.api.openDialogue
import core.api.isQuestComplete
import core.api.isQuestInProgress
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Kilron dialogue.
 */
@Initializable
class KilronDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when {
            isQuestComplete(player, Quests.PLAGUE_CITY) && isQuestComplete(player, Quests.BIOHAZARD) ->
                npcl(FaceAnim.FRIENDLY, "Looks like you won't be needing the rope ladder any more, adventurer.")

            isQuestInProgress(player, Quests.BIOHAZARD, 1, 99) -> {
                end()
                openDialogue(player, content.region.kandarin.ardougne.east.quest.biohazard.dialogue.KilronDialogue())
            }

            else -> npc("Hello.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.FRIENDLY, "I heard it was you who started the revolution and freed West Ardougne!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = KilronDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.KILRON_349)
}
