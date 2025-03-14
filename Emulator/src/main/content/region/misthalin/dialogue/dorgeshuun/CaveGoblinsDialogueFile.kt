package content.region.misthalin.dialogue.dorgeshuun

import content.data.LightSource
import core.api.addItemOrDrop
import core.api.anyInInventory
import core.api.quest.isQuestComplete
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class CaveGoblinsDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.CAVE_GOBLIN_MINER_2069)
        if (isQuestComplete(player!!, Quests.THE_LOST_TRIBE)) {
            when ((0..5).random()) {
                0 ->
                    when (stage) {
                        0 -> npcl(FaceAnim.OLD_NORMAL, "What are you doing down here without a lamp?").also { stage++ }
                        1 -> npcl(FaceAnim.OLD_NORMAL, "Here, I have a spare torch.").also { stage++ }
                        2 -> {
                            end()
                            addItemOrDrop(player!!, Items.LIT_TORCH_594)
                            stage = END_DIALOGUE
                        }
                    }
                1 ->
                    when (stage) {
                        0 -> npcl(FaceAnim.OLD_NORMAL, "Where did you come from?").also { stage = 50 }
                        50 -> playerl(FaceAnim.NEUTRAL, "From above ground.").also { stage = 60 }
                        60 -> npcl(FaceAnim.OLD_NORMAL, "Above ground? Where is that?").also { stage = 70 }
                        70 ->
                            playerl(
                                FaceAnim.NEUTRAL,
                                "You know, out of caves, in the open air, with sunshine and wide open spaces!",
                            ).also {
                                stage =
                                    80
                            }
                        80 -> npcl(FaceAnim.OLD_NORMAL, "Ick. Sounds horrible.").also { stage = END_DIALOGUE }
                    }
                2 ->
                    when (stage) {
                        0 -> npcl(FaceAnim.OLD_NORMAL, "Don't tread on my feet!").also { stage = 90 }
                        90 ->
                            playerl(
                                FaceAnim.NEUTRAL,
                                "I'm not going to tread on your feet.",
                            ).also { stage = END_DIALOGUE }
                    }
                3 ->
                    when (stage) {
                        0 ->
                            npcl(
                                FaceAnim.OLD_NORMAL,
                                "Beware of swamp gas! Look out for the warning marks!",
                            ).also { stage++ }
                        100 -> playerl(FaceAnim.NEUTRAL, "Um, Thanks.").also { stage = END_DIALOGUE }
                    }
                4 ->
                    when (stage) {
                        0 -> playerl(FaceAnim.NEUTRAL, "Hello, how are you?").also { stage = 110 }
                        110 ->
                            npcl(
                                FaceAnim.OLD_NORMAL,
                                "I'm a bit worried about the increase of humans these days.",
                            ).also {
                                stage =
                                    120
                            }
                        120 ->
                            npcl(
                                FaceAnim.OLD_NORMAL,
                                "Present company excluded, of course!",
                            ).also { stage = END_DIALOGUE }
                    }
                5 ->
                    when (stage) {
                        0 -> npcl(FaceAnim.OLD_NORMAL, "Nice weather we're having!").also { stage = 130 }
                        130 ->
                            playerl(
                                FaceAnim.NEUTRAL,
                                "But you live underground. The weather is always the same!",
                            ).also {
                                stage =
                                    140
                            }
                        140 -> npcl(FaceAnim.OLD_NORMAL, "Yes, it's always nice!").also { stage = END_DIALOGUE }
                    }
            }
        } else if (LightSource.hasActiveLightSource(player!!)) {
            npcl(
                FaceAnim.OLD_NORMAL,
                "Watch out! You don't want to let a naked flame near swamp gas! Look out for the warning marks.",
            ).also {
                stage =
                    END_DIALOGUE
            }
        } else if (LightSource.hasActiveLightSource(player!!) !=
            anyInInventory(player!!, Items.LIT_BLACK_CANDLE_32, Items.LIT_CANDLE_33)
        ) {
            npcl(FaceAnim.OLD_NORMAL, "Don't shine that thing in my eyes!").also { stage = END_DIALOGUE }
        } else {
            sendDialogue(player!!, "Cave goblin is not interested in talking.").also { stage = END_DIALOGUE }
        }
    }
}
