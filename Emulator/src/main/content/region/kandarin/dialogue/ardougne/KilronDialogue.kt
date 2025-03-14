package content.region.kandarin.dialogue.ardougne

import content.region.kandarin.quest.biohazard.dialogue.KilronDialogue
import core.api.openDialogue
import core.api.quest.isQuestComplete
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
class KilronDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when {
            isQuestComplete(
                player,
                Quests.PLAGUE_CITY,
            ) &&
                isQuestComplete(
                    player,
                    Quests.BIOHAZARD,
                ) ->
                npcl(FaceAnim.FRIENDLY, "Looks like you won't be needing the rope ladder any more, adventurer.").also {
                    stage++
                }
            isQuestInProgress(player, Quests.BIOHAZARD, 1, 99) -> {
                end()
                openDialogue(player, KilronDialogue())
            }
            else -> npc("Hello.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(FaceAnim.FRIENDLY, "I heard it was you who started the revolution and freed West Ardougne!").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return KilronDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KILRON_349)
    }
}
