package content.minigame.ratpits.dialogue

import core.api.openDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs

/**
 * Represents the Oxi dialogue.
 */
class OxiDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.OXI_2993)
        when (stage) {
            0 -> playerl("Hello. What are you doing?").also { stage++ }
            1 -> npcl(FaceAnim.OLD_DEFAULT, "I'm the rat keeper, it's my job to look after these here rats.").also { stage++ }
            2 -> playerl("You do know that there are some rats running loose?").also { stage++ }
            3 -> npcl(FaceAnim.OLD_DEFAULT, "Some nibble their way out from time to time. Don't worry there's enough cats down here to sort anything that gets out.").also { stage++ }
            4 -> playerl("So how do these rat pits work?").also { stage++ }
            5 -> npcl(FaceAnim.OLD_DEFAULT, "The Keldagrim pits cater for those with the larger variety of cats.").also { stage++ }
            6 -> npcl(FaceAnim.OLD_DEFAULT, "If you have an overgrown cat and a couple of coins you can challenge someone else who also has an overgrown cat and some spare change.").also { stage++ }
            7 -> playerl("That sounds simple enough so far.").also { stage++ }
            8 -> npcl(FaceAnim.OLD_DEFAULT, "You then agree on a wager. It's rumoured that some ratcatchers can talk to their own cats and give them tactics to aid them.").also { stage++ }
            9 -> playerl("So then how do you win the challenge?").also { stage++ }
            10 -> npcl(FaceAnim.OLD_DEFAULT, "I was just getting to that. Both the gamblers' cats are placed inside of the fenced off area, and whoever kills 9 rats first wins.").also { stage++ }
            11 -> {
                end()
                openDialogue(player!!, ChallengeDialogue(npc!!.id))
            }
        }
    }
}
