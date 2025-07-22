package content.region.kandarin.miniquest.secret_ghost.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

/**
 * Represents the [WrongLocationDialogue].
 */
class WrongLocationDialogue : DialogueFile() {

    private var branch = -1

    override fun handle(componentID: Int, buttonID: Int) {
        if (branch == -1) {
            branch = (0..7).random()
        }
        when (branch) {
            0 -> when (stage) {
                0 -> {
                    playerl(FaceAnim.FRIENDLY, "Hello there.")
                    stage = 1
                }
                1 -> {
                    npcl(FaceAnim.NEUTRAL, "Mortal... Take heed of my example, and waste not your life, lest you may suffer the same fate as myself...")
                    stage = 2
                }
                2 -> {
                    playerl(FaceAnim.FRIENDLY, "Huh? You mean someone did this to you?")
                    stage = 3
                }
                3 -> {
                    npcl(FaceAnim.NEUTRAL, "You have ascertained the truth in my words...")
                    stage = 4
                }
                4 -> {
                    playerl(FaceAnim.FRIENDLY, "So who did it to you? And why?")
                    stage = 5
                }
                5 -> {
                    npcl(FaceAnim.NEUTRAL, "Events of moons past, I remember not clearly...")
                    stage = 6
                }
                6 -> {
                    playerl(FaceAnim.FRIENDLY, "Fine help you are then.")
                    stage = END_DIALOGUE
                }
            }
            1 -> when (stage) {
                0 -> {
                    playerl(FaceAnim.FRIENDLY, "Hello there.")
                    stage = 1
                }
                1 -> {
                    npcl(FaceAnim.NEUTRAL, "Hello.")
                    stage = 2
                }
                2 -> {
                    playerl(FaceAnim.FRIENDLY, "So you're a ghost, huh?")
                    stage = 3
                }
                3 -> {
                    npcl(FaceAnim.NEUTRAL, "Apparently.")
                    stage = 4
                }
                4 -> {
                    playerl(FaceAnim.FRIENDLY, "In which case I only have one thing to say to you:")
                    stage = 5
                }
                5 -> {
                    player(FaceAnim.FRIENDLY, "Woooo! Woowooooowoo,", "wooo WOOOOOOOOOOO woowoo woowoowoo woo!")
                    stage = 6
                }
                6 -> {
                    npcl(FaceAnim.NEUTRAL, "...what?")
                    stage = 7
                }
                7 -> {
                    playerl(FaceAnim.FRIENDLY, "Guess you don't have the amulet of humanspeak, huh?")
                    stage = 8
                }
                8 -> {
                    npcl(FaceAnim.NEUTRAL, "...huh?")
                    stage = 9
                }
                9 -> {
                    player(FaceAnim.FRIENDLY, "Yeah, that's right.", "WOO! WOOOWOOO WOO WOOOOWOO!", "Got no comeback for that, have you?")
                    stage = 10
                }
                10 -> {
                    npcl(FaceAnim.NEUTRAL, "You are very strange...")
                    stage = END_DIALOGUE
                }
            }
            2 -> when (stage) {
                0 -> {
                    playerl(FaceAnim.FRIENDLY, "Hello there.")
                    stage = 1
                }
                1 -> {
                    npcl(FaceAnim.NEUTRAL, "Please... Leave me to my fate. I have been here so long that I scarce remember aught else...")
                    stage = 2
                }
                2 -> {
                    playerl(FaceAnim.FRIENDLY, "Uh.... huh.")
                    stage = END_DIALOGUE
                }
            }
            3 -> when (stage) {
                0 -> {
                    playerl(FaceAnim.FRIENDLY, "Hello there.")
                    stage = 1
                }
                1 -> {
                    npcl(FaceAnim.NEUTRAL, "You can see me?")
                    stage = 2
                }
                2 -> {
                    playerl(FaceAnim.FRIENDLY, "Uh... yes?")
                    stage = 3
                }
                3 -> {
                    npcl(FaceAnim.NEUTRAL, "And you understand my words?")
                    stage = 4
                }
                4 -> {
                    playerl(FaceAnim.FRIENDLY, "Well, most of them...")
                    stage = 5
                }
                5 -> {
                    npcl(FaceAnim.NEUTRAL, "This is incredible! How can such a thing have come to pass?")
                    stage = 6
                }
                6 -> {
                    playerl(FaceAnim.FRIENDLY, "What can I say? I'm a professional.")
                    stage = END_DIALOGUE
                }
            }
            4 -> when (stage) {
                0 -> {
                    playerl(FaceAnim.FRIENDLY, "Hello there.")
                    stage = 1
                }
                1 -> {
                    npcl(FaceAnim.NEUTRAL, "Hello back at you.")
                    stage = 2
                }
                2 -> {
                    playerl(FaceAnim.FRIENDLY, "So what's a nice ghost like you doing in a place like this?")
                    stage = 3
                }
                3 -> {
                    npcl(FaceAnim.NEUTRAL, "I suppose you think that's funny?")
                    stage = 4
                }
                4 -> {
                    playerl(FaceAnim.FRIENDLY, "Well... Mildly amusing I guess.")
                    stage = 5
                }
                5 -> {
                    npcl(FaceAnim.NEUTRAL, "I don't think I want to talk to you anymore.")
                    stage = END_DIALOGUE
                }
            }
            5 -> when (stage) {
                0 -> {
                    playerl(FaceAnim.FRIENDLY, "Hello there.")
                    stage = 1
                }
                1 -> {
                    npcl(FaceAnim.NEUTRAL, "Hello stranger.")
                    stage = 2
                }
                2 -> {
                    player(FaceAnim.FRIENDLY, "Soooo....", "Invisible ghost haunting the same place for", "thousands of years, huh?")
                    stage = 3
                }
                3 -> {
                    playerl(FaceAnim.FRIENDLY, "That's got to be a pretty rubbish way to spend your life. Er, death.")
                    stage = 4
                }
                4 -> {
                    npcl(FaceAnim.NEUTRAL, "You have no idea...")
                    stage = 5
                }
                5 -> {
                    playerl(FaceAnim.FRIENDLY, "Well, bad luck and all that. See ya around!")
                    stage = END_DIALOGUE
                }
            }
            6 -> when (stage) {
                0 -> {
                    playerl(FaceAnim.FRIENDLY, "Hello there.")
                    stage = 1
                }
                1 -> {
                    npcl(FaceAnim.NEUTRAL, "Hello stranger. It is rare indeed that I meet one who can see my presence, let alone one who can understand my words.")
                    stage = 2
                }
                2 -> {
                    player(FaceAnim.FRIENDLY, "Soooo....", "Is it fun being a ghost?")
                    stage = 3
                }
                3 -> {
                    npcl(FaceAnim.NEUTRAL, "Does it look like fun to you?")
                    stage = 4
                }
                4 -> {
                    playerl(FaceAnim.FRIENDLY, "Erm... Well, yes actually.")
                    stage = 5
                }
                5 -> {
                    npcl(FaceAnim.NEUTRAL, "Then you are a fool, and I will waste no more words upon you.")
                    stage = 6
                }
                6 -> {
                    playerl(FaceAnim.FRIENDLY, "What, not even 'goodbye'?")
                    stage = 7
                }
                7 -> {
                    npcl(FaceAnim.NEUTRAL, " ")
                    stage = 8
                }
                8 -> {
                    playerl(FaceAnim.FRIENDLY, "Sheesh, what a grouch. You'd think you'd have more of a sense of humour being dead and all.")
                    stage = END_DIALOGUE
                }
            }
            7 -> when (stage) {
                0 -> {
                    playerl(FaceAnim.FRIENDLY, "Hello there.")
                    stage = 1
                }
                1 -> {
                    npc(FaceAnim.NEUTRAL, "The endless tragedy of fate...", "Why must you torment me so?")
                    stage = 2
                }
                2 -> {
                    playerl(FaceAnim.FRIENDLY, "Alright, alright, calm down, calm down. All I said was 'hello'!")
                    stage = END_DIALOGUE
                }
            }
        }
    }
}