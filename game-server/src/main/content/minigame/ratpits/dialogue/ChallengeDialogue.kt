package content.minigame.ratpits.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE

/**
 * Represents the Challenge dialogue.
 */
class ChallengeDialogue(var npcId: Int) : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(npcId)
        when (stage) {
            0 -> options(
                "That sounds vile and cruel.",
                "It sounds a little dangerous for my cat.",
                "Wow that sounds like fun!",
                "Is there a limit to how much you can bet?",
                "Can I challenge you and your cat?",
            ).also { stage++ }

            1 -> when (buttonID) {
                1 -> playerl("That sounds vile and cruel.").also { stage++ }
                2 -> playerl("It sounds a little dangerous for my cat.").also { stage = 6 }
                3 -> playerl("Wow that sounds like fun!").also { stage = 14 }
                4 -> playerl("Is there a limit to how much you can bet?").also { stage = 16 }
                5 -> playerl("Can I challenge you and your cat?").also { stage = 20 }
            }

            2 -> npcl(FaceAnim.OLD_DEFAULT, "It's a cruel world and the pits are no exception.").also { stage++ }
            3 -> npcl(FaceAnim.OLD_DEFAULT, "Each to their own I say.").also { stage++ }
            4 -> options("One more thing...", "Thanks for your help.").also { stage++ }
            5 -> when (buttonID) {
                1 -> playerl("One more thing...").also { stage = 0 }
                2 -> playerl("Thanks for your help.").also { stage = END_DIALOGUE }
            }

            6 -> npcl(FaceAnim.OLD_DEFAULT, "I agree. Cats have been known to be killed in fights.").also { stage++ }
            7 -> playerl("Oh how terrible. Is there nothing you can do to save your cat?").also { stage++ }
            8 -> npcl(FaceAnim.OLD_DEFAULT, "Of course there is, you can instruct it to be cautious, and it will leave once it feels as if it is in danger.").also { stage++ }

            9 -> playerl("Oh that sounds quite wise.").also { stage++ }
            10 -> npcl(FaceAnim.OLD_DEFAULT, "That depends on how you value your cat.").also { stage++ }
            11 -> playerl("Sorry, I don't follow.").also { stage++ }
            12 -> npcl(FaceAnim.OLD_DEFAULT, "Well if your cat dies or it runs away you lose.").also { stage++ }
            13 -> npcl(FaceAnim.OLD_DEFAULT, "So the longer it stays in the fight the better chance you have of winning.").also { stage = 4 }
            14 -> npcl(FaceAnim.OLD_DEFAULT, "I don't know about that.").also { stage++ }
            15 -> npcl(FaceAnim.OLD_DEFAULT, "One thing I know though is that it's a quick way to make and lose money.").also { stage = 4 }
            16 -> npcl(FaceAnim.OLD_DEFAULT, "Hmmm...let me see. If I'm not mistaken, you can currently bet up to a maximum of 100 coins.").also { stage++ }
            17 -> playerl("That sounds like a silly amount of money to bet on a cat fight.").also { stage++ }
            18 -> npcl(FaceAnim.OLD_DEFAULT, "You'd be surprised what some people would be willing to bet.").also { stage++ }
            19 -> npcl(FaceAnim.OLD_DEFAULT, "There is a minimum bet of 100 coins too.").also { stage = 4 }
            20 -> npcl(FaceAnim.OLD_DEFAULT, "No, not now, we don't really bet with outsiders.").also { stage++ }
            21 -> playerl("But I'm not an outsider, am I not inside in the pits now?").also { stage++ }
            22 -> npcl(FaceAnim.OLD_DEFAULT, "I don't know, maybe once you've done something to win over my trust.").also { stage++ }
            23 -> playerl("Can it be bought?").also { stage++ }
            24 -> npcl(FaceAnim.OLD_DEFAULT, "What?").also { stage++ }
            25 -> playerl("Your trust.").also { stage++ }
            26 -> npcl(FaceAnim.OLD_DEFAULT, "No. Regular townsfolk don't like us, which leads us to distrust them and anyone else.").also { stage = 4 }
        }
    }
}
