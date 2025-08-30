package content.region.misthalin.varrock.dialogue

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Sawmill Operator dialogue.
 */
class SawmillOperatorDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.SAWMILL_OPERATOR_4250)
        when (stage) {
            0 -> npc(FaceAnim.NEUTRAL, "Do you want me to make some planks for you? Or", "would you be interested in some other housing supplies?").also { stage++ }
            1 -> showTopics(
                IfTopic("I have some questions about a cat.", 30,
                    getQuestStage(player!!, Quests.GERTRUDES_CAT) in 1..99),
                Topic("Planks please!", 10),
                Topic("What kind of planks can you make?", 20),
                Topic("Can I buy some housing supplies?", 12),
                Topic("Nothing, thanks.", 40)
            )
            10 -> npc(FaceAnim.HALF_ASKING, "What kind of planks do you want?").also { stage++ }
            11 -> {
                val p = player ?: return
                val logs = intArrayOf(Items.LOGS_1511, Items.OAK_LOGS_1521, Items.TEAK_LOGS_6333, Items.MAHOGANY_LOGS_6332)
                if (!anyInInventory(p, *logs)) {
                    end()
                    sendDialogue(p, "You are not carrying any logs to cut into planks.")
                } else {
                    end()
                    openInterface(p, Components.POH_SAWMILL_403)
                }
            }
            12 -> end().also { openNpcShop(player!!, NPCs.SAWMILL_OPERATOR_4250) }

            20 -> npc(FaceAnim.NEUTRAL, "I can make planks from wood, oak, teak and mahogany.", "I don't make planks from other woods as they're no", "good for making furniture.").also { stage++ }
            21 -> npc(FaceAnim.NEUTRAL, "Wood and oak are all over the place, but teak and", "mahogany can only be found in a few places like", "Karamja and Etceteria.").also { stage = END_DIALOGUE }

            30 -> npcl(FaceAnim.HALF_ASKING, "A cat? What makes you ask about that?").also { stage++ }
            31 -> playerl(FaceAnim.SAD, "I'm looking for a cat named Fluffs and my sources tell me she may be in your sawmill.").also { stage++ }
            32 -> npcl(FaceAnim.HALF_ASKING, "Your sources? Who's been talking about my sawmill behind my back? This is a business. We don't have animals on the premises.").also { stage = 1 }
            40 -> npc(FaceAnim.FRIENDLY, "Well come back when you want some. You can't get", "good quality planks anywhere but here!").also { stage = END_DIALOGUE }
        }
    }
}
