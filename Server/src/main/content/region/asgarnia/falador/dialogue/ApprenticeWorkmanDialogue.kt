package content.region.asgarnia.falador.dialogue

import core.api.sendNPCDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Apprentice Workman dialogue.
 */
class ApprenticeWorkmanDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> player(FaceAnim.FRIENDLY, "Hiya.").also { stage++ }
            1 -> npc(FaceAnim.SAD, "Sorry, I haven't got time to chat. We've only just", "finished a collossal order of furniture for the Varrock", "area, and already there's more work coming in.").also { stage++ }
            2 -> player(FaceAnim.ASKING, "Varrock?").also { stage++ }
            3 -> npc(FaceAnim.ROLLING_EYES, "Yeah, the Council's had it redecorated.").also { stage++ }
            4 -> sendNPCDialogue(player!!, NPCs.WORKMAN_3236, "Oi - stop gabbing and get that chair finished!").also { stage++ }
            5 -> npc(FaceAnim.SAD, "You'd better let me get on with my work.").also { stage++ }
            6 -> player(FaceAnim.NEUTRAL, "Ok, bye.").also { stage = END_DIALOGUE }
        }
    }
}
