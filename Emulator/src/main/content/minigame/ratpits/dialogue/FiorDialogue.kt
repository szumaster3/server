package content.minigame.ratpits.dialogue

import core.api.openDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs

class FiorDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.FIOR_2994)
        when (stage) {
            0 -> playerl("Hi. What are you doing?").also { stage++ }
            1 -> npcl(FaceAnim.OLD_DEFAULT, "Relaxing after a long day at the factory.").also { stage++ }
            2 -> npcl(FaceAnim.OLD_DEFAULT, "If I keep up this winning streak, I mightn't have to go into work tomorrow.").also { stage++ }
            3 -> playerl("Do you mind me asking how much you have won?").also { stage++ }
            4 -> npcl(FaceAnim.OLD_DEFAULT, "Not at all, I'm up 10 coins so far.").also { stage++ }
            5 -> playerl("Umm ok.").also { stage++ }
            6 -> playerl("So how do these rat pits work?").also { stage++ }
            7 -> npcl(FaceAnim.OLD_DEFAULT, "The Keldagrim pits cater for those with the larger variety of cats.").also { stage++ }
            8 -> npcl(FaceAnim.OLD_DEFAULT, "If you have an overgrown cat and a couple of coins you can challenge someone else who also has an overgrown cat and some spare change.").also { stage++ }
            9 -> playerl("That sounds simple enough so far.").also { stage++ }
            10 -> npcl(FaceAnim.OLD_DEFAULT, "You then agree on a wager. It's rumoured that some ratcatchers can talk to their own cats and give them tactics to aid them.").also { stage++ }
            11 -> playerl("So then how do you win the challenge?").also { stage++ }
            12 -> npcl(FaceAnim.OLD_DEFAULT, "I was just getting to that. Both the gamblers' cats are placed inside of the fenced off area, and whoever kills 9 rats first wins.").also { stage++ }
            13 -> {
                end()
                openDialogue(player!!, ChallengeDialogue(npc!!.id))
            }
        }
    }
}
