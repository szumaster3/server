package content.region.wilderness.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class RogueCastleDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.ROGUE_8122)
        when (stage) {
            0 -> npcl(FaceAnim.HALF_ASKING, "Have you come to bust me out of here?").also { stage++ }
            1 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Yes, I have. The doors were locked but I used this magical ring to travel into a parallel realm where all recent changes are opposite to the other side. So by closing the doors there, I opened the doors he-",
                ).also {
                    stage++
                }
            2 ->
                npcl(
                    FaceAnim.HALF_ASKING,
                    "Man, did you break the doors open using your head, or what? Regardless, I appreciate the help, man. If you ever need anything, come see me at my place in Varrock. I've got a business you might be interested in.",
                ).also {
                    stage++
                }
            3 -> playerl(FaceAnim.HALF_ASKING, "Whereabouts in Varrock?").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Assuming it hasn't been occupied by any of the muggers, I've got a place in the not-so-nice parts of town, right behind the archery store. Come and see me there.",
                ).also {
                    stage++
                }
            5 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Anyway, I've got to run now before my so-called companions realise what you and your thick head have done.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}
