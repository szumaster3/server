package content.region.island.tutorial.dialogue

import content.region.island.tutorial.plugin.TutorialStage
import core.api.getAttribute
import core.api.setAttribute
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.START_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Banker dialogue in Tutorial island.
 */
class BankerGuideDialogue: DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.BANKER_953)
        when (stage) {
            START_DIALOGUE -> npc(FaceAnim.HALF_ASKING,"Good day, would you like to access your bank account?").also { stage++ }
            1 -> options("Yes.", "No, thanks.").also { stage++ }
            2 -> when (buttonID) {
                1 -> {
                    end()
                    player?.bank?.open()
                    if(getAttribute(player!!, TutorialStage.TUTORIAL_STAGE, 0) == 56) {
                        setAttribute(player!!, TutorialStage.TUTORIAL_STAGE, 57)
                        TutorialStage.load(player!!, 57)
                    }
                }
                2 -> end()
            }
        }
    }
}
