package content.region.kandarin.miniquest.secret_ghost.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items

/**
 * Represents the [ValdezDialogue] & [ValdezDialogueFile].
 */
@Initializable
class ValdezDialogue(player: Player? = null) : Dialogue(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, ValdezDialogueFile(), npc)
        return false
    }
    override fun newInstance(player: Player?): Dialogue = ValdezDialogue(player)
    override fun getIds(): IntArray = intArrayOf(2381)
}

private class ValdezDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> {
                val hasItem = hasAnItem(player!!, Items.GHOSTLY_ROBE_6107).container != null
                when {
                    // Talk without ghostspeak amulet.
                    !inEquipment(player!!, Items.GHOSTSPEAK_AMULET_552) -> {
                        openDialogue(player!!, RandomDialogue(), npc!!)
                    }
                    // Talk again without item.
                    !hasItem && getAttribute(player!!, GameAttributes.ZAROS_VALDEZ_TALK, false) -> {
                        player(FaceAnim.SAD, "I lost that Robe top you gave me...", "Can I have another please?").also { stage = 38 }
                    }
                    // Talk again.
                    getAttribute(player!!, GameAttributes.ZAROS_VALDEZ_TALK, false) -> {
                        npc("Thank you for hearing my tale...", "It has been so lonely here...").also { stage = 303 }
                    }
                    // First talk.
                    else -> {
                        player("Hello.").also { stage++ }
                    }
                }
            }
            1 -> npc("H-hello!").also { stage++ }
            2 -> player("So what's up?").also { stage++ }
            3 -> npc("I cannot believe it!", "You can see me?", "You understand my words?").also { stage++ }
            4 -> player("Sure can.", "So why are you hanging out here?").also { stage++ }
            5 -> npc(FaceAnim.SAD, "My tale is one of woe...", "No doubt you will have little interest in hearing it...").also { stage++ }
            6 -> npc(FaceAnim.SAD, "Though it has been so many moons since last I had", "company in this endless non-life...").also { stage++ }
            7 -> options("tell em your story", "Goodbye then").also { stage++ }
            8 -> when (buttonID) {
                1 -> player("Well, actually I would like to know what happened to", "you to turn you into an invisible ghost.").also { stage++ }
                2 -> player("Well, that's all fascinating, but I just don't particularly care.", "Bye-bye.").also { stage = END_DIALOGUE }
            }
            9 -> player("If only so I can make sure it doesn't happen to me...").also { stage++ }
            10 -> npc("My name is Valdez.", "I served my Lord Saradomin faithfully for many years,", "as an explorer of this strange land we had been brought", "to.").also { stage++ }
            11 -> npc("I remember the day this curse fell upon me clearly...", "I had just discovered a huge temple, hidden below the", "ground, of one of Saradomins compatriots.").also { stage++ }
            12 -> npc(FaceAnim.THINKING, "I am unsure who built it, or why they had left it", "seemingly abandoned, but inside I located a great", "treasure...").also { stage++ }
            13 -> npc("It was the godstaff of Armadyl.", "Oh, how I rue my choice that day!").also { stage++ }
            14 -> npc("Choice?").also { stage++ }
            15 -> npc("Aye, stranger.", "I chose that day to take it so that my Lord Saradomin's", "power and prestige could be increased by its possession.").also { stage++ }
            16 -> npc(FaceAnim.HAPPY, "A god-weapon!", "Do you have any comprehension of the difficulty and", "rarity in obtaining such a thing?").also { stage++ }
            17 -> npc("To find such an artefact of power just lying around, it", "is almost incomprehensible...").also { stage++ }
            18 -> npc(FaceAnim.SAD, "So it was there in that deserted temple that I made my", "choice.", "I took the staff, and left that temple for Entrana", "immediately.").also { stage++ }
            19 -> npc(FaceAnim.SAD, "This was the cause of my cursed state.").also { stage++ }
            20 -> player("What, you mean you gave it to Saradomin and in", "return he cursed you???").also { stage++ }
            21 -> player("Seems kind of ungrateful if you ask me...").also { stage++ }
            22 -> npc("No stranger, you misunderstand completely...", "Firstly my gracious Lord would never treat anyone in", "such a manner;", "If he felt it was beyond my bounds as a mere mortal").also { stage++ }
            23 -> npc("to hold such an artefact, he would simply have", "commanded me to return it to whence I had claimed it,", "and I being eternally loyal would have obeyed without", "question...").also { stage++ }
            24 -> player(FaceAnim.ASKING, "And secondly?").also { stage++ }
            25 -> npc("And secondly, I never managed to pass the artefact on", "to my Lord...").also { stage++ }
            26 -> npc(FaceAnim.SAD, "The vile thief Rennard accosted me as I made my way", "to Entrana, and after defeating me with a sneak attack,", "plundered the staff from my person, and left me for", "dead...").also { stage++ }
            27 -> npc(FaceAnim.SAD, "I do not know what became of the staff, but I can feel", "in my very bones whatever its final fate was, it is", "somehow related to this curse upon me...").also { stage++ }
            28 -> player("Wow.", "Tough break!").also { stage++ }
            29 -> npc("I am sorry to bore you with my tale stranger, please", "allow me to compound my rudeness by asking you for", "one small favour, small to perform?").also { stage++ }
            30 -> player("Eh, I won't make any promises, but if it's nothing too", "annoying I guess I can help you out.").also { stage++ }
            31 -> npc("Many thanks stranger, this existence tortures me...").also { stage++ }
            32 -> npc("I need to find Rennard and if he has the staff yet", "reclaim it, or find out what hideous deed he performed", "to curse me so!").also { stage++ }
            33 -> npc("I have nothing I may offer you save my piece of", "clothing, please take it as payment...").also {
                setAttribute(player!!, GameAttributes.ZAROS_VALDEZ_TALK, true)
                setAttribute(player!!, GameAttributes.ZAROS_PATH_SEQUENCE, (1..3).random())
                addItemOrDrop(player!!, Items.GHOSTLY_ROBE_6107, 1)
                stage++
            }
            34 -> player("Where can I find Rennard then?").also { stage = (100 + getAttribute(player!!, GameAttributes.ZAROS_PATH_SEQUENCE, 0)) }
            35 -> player("Okay, well I'll try and find him for you then.").also { stage = END_DIALOGUE }
            38 -> {
                if (freeSlots(player!!) == 0) {
                    end()
                    sendDialogue(player!!, "You don't have space for the reward.")
                } else {
                    npc(FaceAnim.SAD, "It seems as though the curse that keeps me here", "extends to my very clothing...")
                    stage++
                }
            }
            39 -> {
                end()
                addItem(player!!, Items.GHOSTLY_ROBE_6107, 1)
                npc(FaceAnim.NEUTRAL, "Here, take it, the moment you lost it, it returned to", "me...")
            }
            // First path.
            101 -> npc(FaceAnim.HALF_GUILTY, "Ah, the infamous Rennard...", "The last I had heard of him, he had sought passage on a", "ship crewed by none but the most dastardly lowly pirates...", "I also heard that this ship had been caught in a").also { stage = 201 }
            201 -> npcl(FaceAnim.HALF_GUILTY, "violent storm, and stranded upon rocks, where the pirates then made their home...").also { stage = 35 }
            // Second path.
            102 -> npc(FaceAnim.HALF_GUILTY, "Ah, the infamous Rennard...", "The last I had heard of that vile thief, he had joined a", "group of bandits in a barren land to the South-east of", "here, where they prey upon the unsuspecting visitors to").also { stage = 202 }
            202 -> npc(FaceAnim.HALF_GUILTY, "the desert awaiting the return of their dark master...").also { stage = 35 }
            // Third path.
            103 -> npc(FaceAnim.HALF_GUILTY, "Ah, the infamous Rennard...", "The last I had heard of that vile thief, he had joined a", "group of bandits in an evil land to the North-east of", "here, where they had made their home living outside of").also { stage = 203 }
            203 -> npc(FaceAnim.HALF_GUILTY, "the reach of the authorities that pursued them...").also { stage = 35 }
            // Talking to npc again not having completed the miniquest.
            303 -> player("Can you remind me where to find the thief Rennard who", "caused this curse to befall you again?").also { stage++ }
            304 -> npc("Of course...").also {
                stage = (100 + getAttribute(player!!, GameAttributes.ZAROS_PATH_SEQUENCE, 0))
            }
        }
    }
}
