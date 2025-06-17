package content.region.kandarin.miniquest.zaros

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items

/**
 * Represents the [KharrimDialogue] & [KharrimDialogueFile].
 */
@Initializable
class KharrimDialogue(player: Player? = null) : Dialogue(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, KharrimDialogueFile(), npc)
        return false
    }
    override fun newInstance(player: Player?): Dialogue = KharrimDialogue(player)
    override fun getIds(): IntArray = intArrayOf(2388, 2389, 2390)
}
class KharrimDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> {
                val sequence = getAttribute(player!!, GameAttributes.ZAROS_PATH_SEQUENCE, 0)
                val hasItem = hasAnItem(player!!, Items.GHOSTLY_BOOTS_6106).container != null
                when {
                    // Talk without ghostspeak amulet.
                    !inEquipment(player!!, Items.GHOSTSPEAK_AMULET_552) -> {
                        openDialogue(player!!, RandomDialogue(), npc!!)
                    }
                    // Talk again if lost ghostly item.
                    !hasItem && getAttribute(player!!, GameAttributes.ZAROS_KHARRIM_TALK, false) -> {
                        player(FaceAnim.SAD, "I lost those boots you gave me...", "Can I have some more please?").also { stage = 41 }
                    }
                    // Talk again.
                    getAttribute(player!!, GameAttributes.ZAROS_KHARRIM_TALK, false) -> {
                        player(FaceAnim.ASKING, "But where can I find this Lennissa?").also { stage = (100 + getAttribute(player!!, GameAttributes.ZAROS_PATH_SEQUENCE, 0)) }
                    }
                    // First talk.
                    else -> {
                        // Correct path.
                        if(2387 + sequence == npc!!.id) {
                            player("Hello.").also { stage++ }
                        } else {
                            openDialogue(player!!, WrongLocationDialogue(), npc!!)
                        }
                    }
                }
            }
            1 -> npc(FaceAnim.SCARED, "How do you know my name, stranger?").also { stage++ }
            2 -> player("Well now...", "I had an interesting chat with Rennard the thief.").also { stage++ }
            3 -> player("It seems you redirected his message regarding a", "certain god-weapon for your own ends.").also { stage++ }
            4 -> npc("So THAT is what this is about...", "I should have known the deal was too good to have no", "repercussions...").also { stage++ }
            5 -> player("It seems as though you might be responsible for this", "curse that has befallen you by not delivering Rennard's", "message to the correct person.").also { stage++ }
            6 -> npc("Ha!", "That is not a truthful assessment of the story...", "You might think differently if you had heard my side of", "events.").also { stage++ }
            7 -> options("Tell em your story", "Goodbye then").also { stage++ }
            8 -> when (buttonID) {
                1 -> player("Please let me hear your side of the story then...").also { stage++ }
                2 -> player("Well, that's all fascinating, but I just don't particularly care.", "Bye-bye.").also { stage = END_DIALOGUE }
            }
            9 -> npc("Well, if you have spoken to Rennard, then you will", "know that he had somehow managed to obtain a very", "valuable weapon, and was looking for buyers.").also { stage++ }
            10 -> npc("What he probably didn't tell you, was that he met me in", "a drunken stupor in some smoke filled tavern, and I", "offered to arrange a purchaser for this item, in", "exchange for a small finders fee.").also { stage++ }
            11 -> player("So you knew what the staff was?").also { stage++ }
            12 -> npc("The god-staff of Armadyl?", "Well, of course I did.").also { stage++ }
            13 -> npc("Honestly, you would have to be pretty slow-witted to", "not recognise a legendary artefact such as that.").also { stage++ }
            14 -> player("Wait, I don't understand, Rennard said that he had a", "buyer already in mind, and that you diverted his", "message to a General Zamorak instead?").also { stage++ }
            15 -> npc("Ha!", "Here is a word of advice for you adventurer;", "Never trust the words of a drunk.").also { stage++ }
            16 -> npc("Whatever he might have thought he was doing with it, in", "the end all that happened was he left me to arrange a", "purchaser for the item.").also { stage++ }
            17 -> player("So you thought you would offer it to General Zamorak?").also { stage++ }
            18 -> npc("Ah yes, Lord Zamorak.", "He was merely a mortal back then, you know?").also { stage++ }
            19 -> npc("Yet I could see great things in store for him even then.", "He had a kind of brilliant ruthlessness...", "And the special kind of vicious streak you see so", "rarely...").also { stage++ }
            20 -> npc("Well anyway, when given the task of selling a weapon", "forged by the very gods themselves, I naturally thought", "of Zamorak as a potential buyer.").also { stage++ }
            21 -> npc("I was a messenger in his employ anyway, so it was a", "mere trifle to find him and deliver the news, and I", "knew of his particular interest in armour and weaponry", "of all kinds.").also { stage++ }
            22 -> npc("Yes, he was always quite the connoisseur when it came", "to weaponry...").also { stage++ }
            23 -> npc("But I digress.", "I let Lord Zamorak know that there was some drunken", "fool with an artefact of incredible power that could", "probably be bought off with a few jewels and trinkets,").also { stage++ }
            24 -> npc("and he escorted me to the tavern and made the", "purchase there and then.").also { stage++ }
            25 -> npc(FaceAnim.HAPPY, "It was a satisfactory deal all around, I got a share of", "the sale price from Rennard, and I greatly increased", "my prestige amongst Zamorak and his followers.").also { stage++ }
            26 -> npc(FaceAnim.SAD, "But maybe...", "Perhaps the event that followed were responsible for", "my cursed state...").also { stage++ }
            27 -> player(FaceAnim.HALF_ASKING, "Events that followed?").also { stage++ }
            28 -> npc("I can not tell you of them precisely, for I myself was", "not there to witness them.").also { stage++ }
            29 -> npc("I am after all, simply a messenger.", "When...'it' happened, I was busy elsewhere delivering a", "message from Zamorak to the rest of his Mahjarrat ilk.").also { stage++ }
            30 -> player("When 'it' happened?", "What was 'it'?").also { stage++ }
            31 -> npc("As I have explained, I was not there, and I do not", "know what Zamorak did with the staff, but whatever it", "was resulted in his banishment by the other gods for", "so many years.").also { stage++ }
            32 -> npc("The very strange thing was that Saradomin must have", "known about it, whatever it was, before it even", "happened...").also { stage++ }
            33 -> player(FaceAnim.HALF_ASKING, "Really?", "Why do you say that?").also { stage++ }
            34 -> npc("Well, it was the contents of the message I", "was returning to Zamorak;").also { stage++ }
            35 -> npc("Lucien seemed quite certain that there was a spy for", "Saradomin somewhere amongst his followers named", "Lennissa.").also { stage++ }
            36 -> npc("If whatever happened to the staff caused this curse to", "befall me, then it is certain that she too would have been", "afflicted, because she would have been in the very heart", "of the action.").also { stage++ }
            37 -> npc("Hmmm...").also { stage++ }
            38 -> npc("You have given me much to think on adventurer.", "I would like to reward you with my sturdy messenger", "boots, may they aid you in your travels.").also {
                setAttribute(player!!, "/save:zaros:kharrim-talk", true)
                addItemOrDrop(player!!, Items.GHOSTLY_BOOTS_6106, 1)
                stage++
            }
            39 -> player("But where can I find this Lennissa?").also { stage = (100 + getAttribute(player!!, GameAttributes.ZAROS_PATH_SEQUENCE, 0)) }
            40 -> player("Okay, well I'll try find her for you then.").also { stage = END_DIALOGUE }
            41 -> {
                end()
                if (freeSlots(player!!) == 0) {
                    sendDialogue(player!!, "You don't have space for the reward.")
                } else {
                    npc(FaceAnim.SAD, "How strange...", "They seemed to return to me when you lost them...")
                    addItem(player!!, Items.GHOSTLY_BOOTS_6106, 1)
                }
            }
            // First path.
            101 -> npc(FaceAnim.HALF_GUILTY, "Well, she was always sickeningly obedient to Saradomin,", "so I would expect her to have run to some great place of", "worship of him if she was affected by the curse to try", "and gain his blessing.").also { stage = 40 }
            // Second path.
            102 -> npc(FaceAnim.HALF_GUILTY, "Well, according to Lucien's intelligence report, she", "had been uncovered as a spy by her constant use of", "ships to ferry information... It is entirely possible", "she would be located somewhere coastal to this day!").also { stage = 40 }
            // Third path.
            103 -> npc(FaceAnim.HALF_GUILTY, "Well, we knew little about her or she would have been", "caught and exposed as a traitor and a spy, but Lucien", "did mention that he had evidence that she was a great", "fan of ball games...").also { stage = 40 }
        }
    }
}