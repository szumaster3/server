package content.region.kandarin.miniquest.zaros.dialogue

import content.region.kandarin.miniquest.zaros.CurseOfZaros
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class KharrimDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
            player("All your base are belong to us.")
            return true
        }

        if (CurseOfZaros.hasTag(player, npc.id) || CurseOfZaros.hasComplete(player)) {
            val item = Items.GHOSTLY_BOOTS_6106
            if (!CurseOfZaros.hasItems(player, item)) {
                player(FaceAnim.SAD, "I lost those boots you gave me...", "Can I have some more please?").also {
                    stage = 42
                }
            } else {
                sendDialogue(player, "You have already talked to this NPC.").also { stage = END_DIALOGUE }
            }
            return true
        }

        player("Hello.")
        stage = 1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 -> npc(FaceAnim.SCARED, "How do you know my name, stranger?").also { stage++ }
            2 -> player("Well now...", "I had an interesting chat with Rennard the thief.").also { stage++ }
            3 ->
                player(
                    "It seems you redirected his message regarding a",
                    "certain god-weapon for your own ends.",
                ).also { stage++ }
            4 ->
                npc(
                    "So THAT is what this is about...",
                    "I should have known the deal was too good to have no",
                    "repercussions...",
                ).also {
                    stage++
                }
            5 ->
                player(
                    "It seems as though you might be responsible for this",
                    "curse that has befallen you by not delivering Rennard's",
                    "message to the correct person.",
                ).also {
                    stage++
                }
            6 ->
                npc(
                    "Ha!",
                    "That is not a truthful assessment of the story...",
                    "You might think differently if you had heard my side of",
                    "events.",
                ).also {
                    stage++
                }
            7 -> options("tell em your story", "Goodbye then").also { stage++ }
            8 ->
                when (buttonId) {
                    1 -> player("Please let me hear your side of the story then...").also { stage++ }
                    2 ->
                        player("Well, that's all fascinating, but I just don't particularly care.", "Bye-bye.").also {
                            stage =
                                END_DIALOGUE
                        }
                }
            9 ->
                npc(
                    "Well, if you have spoken to Rennard, then you will",
                    "know that he had somehow managed to obtain a very",
                    "valuable weapon, and was looking for buyers.",
                ).also {
                    stage++
                }
            10 ->
                npc(
                    "What he probably didn't tell you, was that he met me in",
                    "a drunken stupor in some smoke filled tavern, and I",
                    "offered to arrange a purchaser for this item, in",
                    "exchange for a small finders fee.",
                ).also {
                    stage++
                }
            11 -> player("So you knew what the staff was?").also { stage++ }
            12 -> npc("The god-staff of Armadyl?", "Well, of course I did.").also { stage++ }
            13 ->
                npc(
                    "Honestly, you would have to be pretty slow-witted to",
                    "not recognise a legendary artefact such as that.",
                ).also {
                    stage++
                }
            14 ->
                player(
                    "Wait, I don't understand, Rennard said that he had a",
                    "buyer already in mind, and that you diverted his",
                    "message to a General Zamorak instead?",
                ).also {
                    stage++
                }
            15 ->
                npc(
                    "Ha!",
                    "Here is a word of advice for you adventurer;",
                    "Never trust the words of a drunk.",
                ).also { stage++ }
            16 ->
                npc(
                    "Whatever he might have thought he was doing with it, in",
                    "the end all that happened was he left me to arrange a",
                    "purchaser for the item.",
                ).also {
                    stage++
                }
            17 -> player("So you thought you would offer it to General Zamorak?").also { stage++ }
            18 -> npc("Ah yes, Lord Zamorak.", "He was merely a mortal back then, you know?").also { stage++ }
            19 ->
                npc(
                    "Yet I could see great things in store for him even then.",
                    "He had a kind of brilliant ruthlessness...",
                    "And the special kind of vicious streak you see so",
                    "rarely...",
                ).also {
                    stage++
                }
            20 ->
                npc(
                    "Well anyway, when given the task of selling a weapon",
                    "forged by the very gods themselves, I naturally thought",
                    "of Zamorak as a potential buyer.",
                ).also {
                    stage++
                }
            21 ->
                npc(
                    "I was a messenger in his employ anyway, so it was a",
                    "mere trifle to find him and deliver the news, and I",
                    "knew of his particular interest in armour and weaponry",
                    "of all kinds.",
                ).also {
                    stage++
                }
            22 -> npc("Yes, he was always quite the connoisseur when it came", "to weaponry...").also { stage++ }
            23 ->
                npc(
                    "But I digress.",
                    "I let Lord Zamorak know that there was some drunken",
                    "fool with an artefact of incredible power that could",
                    "probably be bought off with a few jewels and trinkets,",
                ).also {
                    stage++
                }
            24 -> npc("and he escorted me to the tavern and made the", "purchase there and then.").also { stage++ }
            25 ->
                npc(
                    FaceAnim.HAPPY,
                    "It was a satisfactory deal all around, I got a share of",
                    "the sale price from Rennard, and I greatly increased",
                    "my prestige amongst Zamorak and his followers.",
                ).also {
                    stage++
                }
            26 ->
                npc(
                    FaceAnim.SAD,
                    "But maybe...",
                    "Perhaps the event that followed were responsible for",
                    "my cursed state...",
                ).also {
                    stage++
                }
            27 -> player(FaceAnim.HALF_ASKING, "Events that followed?").also { stage++ }
            28 ->
                npc(
                    "I can not tell you of them precisely, for I myself was",
                    "not there to witness them.",
                ).also { stage++ }
            29 ->
                npc(
                    "I am after all, simply a messenger.",
                    "When...'it' happened, I was busy elsewhere delivering a",
                    "message from Zamorak to the rest of his Mahjarrat ilk.",
                ).also {
                    stage++
                }
            30 -> player("When 'it' happened?", "What was 'it'?").also { stage++ }
            31 ->
                npc(
                    "As I have explained, I was not there, and I do not",
                    "know what Zamorak did with the staff, but whatever it",
                    "was resulted in his banishment by the other gods for",
                    "so many years.",
                ).also {
                    stage++
                }
            32 ->
                npc(
                    "The very strange thing was that Saradomin must have",
                    "known about it, whatever it was, before it even",
                    "happened...",
                ).also {
                    stage++
                }
            33 -> player(FaceAnim.HALF_ASKING, "Really?", "Why do you say that?").also { stage++ }
            34 -> npc("Well, it was the contents of the message I", "was returning to Zamorak;").also { stage++ }
            35 ->
                npc(
                    "Lucien seemed quite certain that there was a spy for",
                    "Saradomin somewhere amongst his followers named",
                    "Lennissa.",
                ).also {
                    stage++
                }
            36 ->
                npc(
                    "If whatever happened to the staff caused this curse to",
                    "befall me, then it is certain that she too would have been",
                    "afflicted, because she would have been in the very heart",
                    "of the action.",
                ).also {
                    stage++
                }
            37 -> npc("Hmmm...").also { stage++ }
            38 ->
                npc(
                    "You have given me much to think on adventurer.",
                    "I would like to reward you with my sturdy messenger",
                    "boots, may they aid you in your travels.",
                ).also {
                    CurseOfZaros.tagDialogue(player, npc.id)
                    addItemOrDrop(player, Items.GHOSTLY_BOOTS_6106, 1)
                    stage++
                }
            39 -> player("But where can I find this Lennissa?").also { stage++ }
            40 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    CurseOfZaros.getLocationDialogue(npc.id, npc.location)?.joinToString("\n") ?: "",
                ).also { stage++ }
            41 -> player("Okay, well I'll try find her for you then.").also { stage = END_DIALOGUE }
            42 -> {
                end()
                if (freeSlots(player) == 0) {
                    sendDialogue(player, "You don't have space for the reward.")
                    return true
                }
                npc(FaceAnim.SAD, "How strange...", "They seemed to return to me when you lost them...")
                addItem(player!!, Items.GHOSTLY_BOOTS_6106, 1)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return KharrimDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MYSTERIOUS_GHOST_2400)
    }
}
