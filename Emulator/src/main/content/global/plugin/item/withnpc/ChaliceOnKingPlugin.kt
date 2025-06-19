package content.global.plugin.item.withnpc

import core.api.Container
import core.api.removeItem
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import org.rs.consts.Items
import org.rs.consts.NPCs

class ChaliceOnKingPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles using Poison chalice on King Arthur NPC.
         */

        onUseWith(IntType.NPC, Items.POISON_CHALICE_197, NPCs.KING_ARTHUR_251) { player, _, with ->
            dialogue(player) {
                npc(with.id, FaceAnim.SAD, "You have chosen poorly.")
                player(FaceAnim.ANNOYED, "Excuse me?")
                npc(with.id, FaceAnim.FRIENDLY, "Sorry, I meant to say 'thank you'. Most refreshing.")
                player(FaceAnim.DISGUSTED_HEAD_SHAKE, "Are you sure that stuff is safe to drink?")
                npc(with.id, FaceAnim.HAPPY, "Oh yes, Stankers' creations may be dangerous for those with weak constitutions, but, personally. I find them rather invigorating.")
                end {
                    removeItem(player, Items.POISON_CHALICE_197, Container.INVENTORY)
                    player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 0, 3)
                }
            }
            return@onUseWith true
        }
    }
}
