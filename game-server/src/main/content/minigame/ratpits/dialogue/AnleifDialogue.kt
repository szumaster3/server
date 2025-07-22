package content.minigame.ratpits.dialogue

import core.api.openDialogue
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs

/**
 * Represents the Anleif dialogue.
 */
class AnleifDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int, ) {
        npc = NPC(NPCs.ANLEIF_2996)
        when (stage) {
            0 -> playerl("Hello.").also { stage++ }
            1 -> sendDialogue(player!!, "The dwarf lady seems to be looking for someone.").also { stage++ }
            2 -> npcl(FaceAnim.OLD_DEFAULT, "Hello, I'm looking for my husband. Have you seen him?").also { stage++ }
            3 -> playerl("What does he look like?").also { stage++ }
            4 -> npcl(FaceAnim.OLD_DEFAULT, "Oh to pot with this silly charade, I'm down here for a spot of gambling.").also { stage++ }
            5 -> playerl("Hang on I thought you were looking for your husband.").also { stage++ }
            6 -> npcl(FaceAnim.OLD_DEFAULT, "Um no, I was just pretending. It's frowned on a little to be seen in these parts.").also { stage++ }
            7 -> playerl("So how do these rat pits work?").also { stage++ }
            8 -> npcl(FaceAnim.OLD_DEFAULT, "The Keldagrim pits cater for those with the larger variety of cats.").also { stage++ }
            9 -> npcl(FaceAnim.OLD_DEFAULT, "If you have an overgrown cat and a couple of coins you can challenge someone else who also has an overgrown cat and some spare change.").also { stage++ }
            10 -> playerl("That sounds simple enough so far.").also { stage++ }
            11 -> npcl(FaceAnim.OLD_DEFAULT, "You then agree on a wager. It's rumoured that some ratcatchers can talk to their own cats and give them tactics to aid them.").also { stage++ }
            12 -> playerl("So then how do you win the challenge?").also { stage++ }
            13 -> npcl(FaceAnim.OLD_DEFAULT, "I was just getting to that. Both the gamblers' cats are placed inside of the fenced off area, and whoever kills 9 rats first wins.").also { stage++ }
            14 -> {
                end()
                openDialogue(player!!, ChallengeDialogue(npc!!.id))
            }
        }
    }
}
