package content.minigame.ratpits.dialogue

import core.api.openDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC

class SigraDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int, ) {
        npc = NPC(2995)
        when (stage) {
            0 -> playerl("Hello.").also { stage++ }
            1 -> npcl(FaceAnim.OLD_DEFAULT, "Hi, can I help you with something?").also { stage++ }
            2 -> playerl("You're not from around here are you?").also { stage++ }
            3 -> npcl(FaceAnim.OLD_DEFAULT, "Why do you say that?").also { stage++ }
            4 -> playerl("Well your accent for one sounds a little funny, and you seem to have a little more colour than most dwarves.").also { stage++ }
            5 -> npcl(FaceAnim.OLD_DEFAULT, "Living under a mountain isn't the best way to get a tan. No I'm not from around here. I was born and worked most of my life in the desert.").also { stage++ }
            6 -> npcl(FaceAnim.OLD_DEFAULT, "Just even thinking of the place makes me thirsty. Now can I help you with anything?").also { stage++ }
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
