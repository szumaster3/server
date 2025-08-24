package content.global.plugin.item.withnpc

import core.api.Container
import core.api.openDialogue
import core.api.removeItem
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import shared.consts.Items
import shared.consts.NPCs

class ChaliceOnKingPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles using Poison chalice on King Arthur NPC.
         */

        onUseWith(IntType.NPC, Items.POISON_CHALICE_197, NPCs.KING_ARTHUR_251) { player, _, _ ->
            openDialogue(player, ChaliceOnArthurDialogue())
            return@onUseWith true
        }
    }

    inner class ChaliceOnArthurDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> npc(FaceAnim.SAD, "You have chosen poorly.").also { stage++ }
                1 -> player(FaceAnim.ANNOYED, "Excuse me?").also { stage++ }
                2 -> npc(FaceAnim.FRIENDLY, "Sorry, I meant to say 'thank you'. Most refreshing.").also { stage++ }
                3 -> player(FaceAnim.DISGUSTED_HEAD_SHAKE, "Are you sure that stuff is safe to drink?").also { stage++ }
                4 -> npc(FaceAnim.HAPPY, "Oh yes, Stankers' creations may be dangerous for those with weak constitutions, but, personally. I find them rather invigorating.").also { stage++ }
                5 -> {
                    end()
                    if(!removeItem(player!!, Items.POISON_CHALICE_197, Container.INVENTORY)) return
                    player?.achievementDiaryManager?.finishTask(player, DiaryType.SEERS_VILLAGE, 0, 3)
                }
            }
        }

    }
}
