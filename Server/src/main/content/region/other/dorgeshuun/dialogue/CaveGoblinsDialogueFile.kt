package content.region.other.dorgeshuun.dialogue

import content.data.LightSources
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Cave Goblins dialogue.
 */
class CaveGoblinsDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.CAVE_GOBLIN_MINER_2069)
        val p = player ?: return

        if (isQuestComplete(p, Quests.THE_LOST_TRIBE)) {
            when ((0..5).random()) {
                0 -> when (stage) {
                    0 -> npcl(FaceAnim.OLD_NORMAL, "What are you doing down here without a lamp?").also { stage++ }
                    1 -> npcl(FaceAnim.OLD_NORMAL, "Here, I have a spare torch.").also { stage++ }
                    2 -> {
                        end()
                        addItemOrDrop(p, Items.LIT_TORCH_594)
                        stage = END_DIALOGUE
                    }
                }
                1 -> when (stage) {
                    0 -> npcl(FaceAnim.OLD_NORMAL, "Where did you come from?").also { stage = 50 }
                    50 -> playerl(FaceAnim.NEUTRAL, "From above ground.").also { stage = 60 }
                    60 -> npcl(FaceAnim.OLD_NORMAL, "Above ground? Where is that?").also { stage = 70 }
                    70 -> playerl(FaceAnim.NEUTRAL, "You know, out of caves, in the open air, with sunshine and wide open spaces!").also { stage = 80 }
                    80 -> npcl(FaceAnim.OLD_NORMAL, "Ick. Sounds horrible.").also { stage = END_DIALOGUE }
                }
                2 -> when (stage) {
                    0 -> npcl(FaceAnim.OLD_NORMAL, "Don't tread on my feet!").also { stage = 90 }
                    90 -> playerl(FaceAnim.NEUTRAL, "I'm not going to tread on your feet.").also { stage = END_DIALOGUE }
                }
                3 -> when (stage) {
                    0 -> npcl(FaceAnim.OLD_NORMAL, "Beware of swamp gas! Look out for the warning marks!").also { stage++ }
                    100 -> playerl(FaceAnim.NEUTRAL, "Um, Thanks.").also { stage = END_DIALOGUE }
                }
                4 -> when (stage) {
                    0 -> playerl(FaceAnim.NEUTRAL, "Hello, how are you?").also { stage = 110 }
                    110 -> npcl(FaceAnim.OLD_NORMAL, "I'm a bit worried about the increase of humans these days.").also { stage = 120 }
                    120 -> npcl(FaceAnim.OLD_NORMAL, "Present company excluded, of course!").also { stage = END_DIALOGUE }
                }
                5 -> when (stage) {
                    0 -> npcl(FaceAnim.OLD_NORMAL, "Nice weather we're having!").also { stage = 130 }
                    130 -> playerl(FaceAnim.NEUTRAL, "But you live underground. The weather is always the same!").also { stage = 140 }
                    140 -> npcl(FaceAnim.OLD_NORMAL, "Yes, it's always nice!").also { stage = END_DIALOGUE }
                }
            }
        } else if (LightSources.hasActiveLightSource(p)) {
            // Player has any lit source (torches, lanterns, etc.).
            npcl(FaceAnim.OLD_NORMAL, "Watch out! You don't want to let a naked flame near swamp gas! Look out for the warning marks.").also { stage = END_DIALOGUE }
        } else if (anyInInventory(p, Items.LIT_BLACK_CANDLE_32, Items.LIT_CANDLE_33)) {
            // Player has a lit candle in inventory.
            npcl(FaceAnim.OLD_NORMAL, "Don't shine that thing in my eyes!").also { stage = END_DIALOGUE }
        } else {
            // Player has no light source.
            end()
            sendMessage(p, "Cave goblin is not interested in talking.")
        }
    }
}
