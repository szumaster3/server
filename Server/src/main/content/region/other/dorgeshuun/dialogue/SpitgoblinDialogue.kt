package content.region.other.dorgeshuun.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import shared.consts.NPCs

/**
 * Represents the Spitgoblin dialogue.
 */
class SpitgoblinDialogue: DialogueFile(){

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.SPIT_GOBLIN_5823)
        when (stage) {
            0 -> player(FaceAnim.HAPPY, "That smells nice!")
            1 -> npcl(FaceAnim.OLD_DEFAULT, "Mmm, yes. Some hunters caught it earlier today.").also { stage++ }
            2 -> player(FaceAnim.HALF_ASKING, "Can I have some?").also { stage++ }
            3 -> npcl(FaceAnim.OLD_DEFAULT, "Oh, it's not cooked yet. But they sell frogburgers in the market!").also { stage++ }
            4 -> end()
        }
    }
}
