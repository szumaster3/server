package content.region.kandarin.quest.itwatchtower.dialogue

import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.map.RegionManager
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class GrewDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (getQuestStage(player, Quests.WATCHTOWER) >= 3) {
            npc(FaceAnim.OLD_NEUTRAL, "What do you want, little morsel? You would look good", "on my plate!")
        } else if (!isQuestComplete(player, Quests.WATCHTOWER)) {
            sendMessage(player, "The ogre has nothing to say at the moment.")
        } else {
            sendMessage(player, "The ogre is not interested in you anymore.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player("I want to enter the city of ogres.").also { stage++ }
            1 -> npc(FaceAnim.OLD_NEUTRAL, "Hah! I should eat you instead!").also { stage++ }
            2 -> options("Don't eat me; I can help you.", "You will have to kill me first.").also { stage++ }
            3 ->
                when (buttonId) {
                    1 -> player("Don't eat me; I can help you.").also { stage += 2 }
                    2 -> player("You will have to kill me first.").also { stage++ }
                }
            4 ->
                npc(FaceAnim.OLD_NEUTRAL, "That's not tricky - guards!").also {
                    end()
                    RegionManager.getLocalNpcs(player)
                    if (npc.id == NPCs.OGRE_115) npc.attack(player)
                }
            5 -> npc(FaceAnim.OLD_NEUTRAL, "What can a morsel like you do for me?").also { stage++ }
            6 -> player("I am a mighty adventurer, slayer of monsters and user", "of magic powers.").also { stage++ }
            7 ->
                npc(
                    FaceAnim.OLD_NEUTRAL,
                    "Well, well, perhaps the morsel can help after all... If you t'ink you're tough, find Gorad, my enemy to the south- east, and knock one of his teeth out!",
                ).also {
                    stage++
                }
            8 ->
                npc(FaceAnim.OLD_NEUTRAL, "Heheheheh!").also {
                    player.questRepository.setStageNonmonotonic(player.questRepository.forIndex(131), 4)
                    stage = END_DIALOGUE
                }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GREW_854)
}
