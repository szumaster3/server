package content.global.handlers.item.withnpc

import core.api.Container
import core.api.removeItem
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class ChaliceOnKingArthurListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.NPC, Items.POISON_CHALICE_197, NPCs.KING_ARTHUR_251) { player, _, with ->
            player.dialogueInterpreter.open(PoisonChaliceOnKingArthurDialogue(), with)
            return@onUseWith true
        }
    }

    inner class PoisonChaliceOnKingArthurDialogue : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            when (stage) {
                0 -> npcl(FaceAnim.SAD, "You have chosen poorly.").also { stage++ }
                1 -> playerl(FaceAnim.ANNOYED, "Excuse me?").also { stage++ }
                2 -> npcl(FaceAnim.FRIENDLY, "Sorry, I meant to say 'thank you'. Most refreshing.").also { stage++ }
                3 ->
                    playerl(
                        FaceAnim.DISGUSTED_HEAD_SHAKE,
                        "Are you sure that stuff is safe to drink?",
                    ).also { stage++ }

                4 ->
                    npcl(
                        FaceAnim.HAPPY,
                        "Oh yes, Stankers' creations may be dangerous for those with weak constitutions, but, personally. I find them rather invigorating.",
                    ).also {
                        removeItem(player!!, Items.POISON_CHALICE_197, Container.INVENTORY)
                        stage = END_DIALOGUE
                    }
            }
        }
    }
}
