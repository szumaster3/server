package content.region.fremennik.lighthouse.dialogue

import content.data.GodBook
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import java.util.*

/**
 * Represents the Jossik dialogue.
 */
@Initializable
class JossikDialogue : Dialogue {
    private var uncompleted: MutableList<GodBook>? = null

    constructor()
    constructor(player: Player) : super(player)

    override fun newInstance(player: Player): Dialogue = JossikDialogue(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY,"Hello again, adventurer.", "What brings you this way?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> showTopics(
                Topic("Can I see your wares?", 1),
                Topic("Have you found any prayerbooks?", 3),
            )
            1 -> npc(FaceAnim.HAPPY,"Sure thing!", "I think you'll agree, my prices are remarkable!").also {stage++ }
            2 -> {
                end()
                openNpcShop(player, npc.id)
            }

            3 -> {
                var missing = false
                for (book in GodBook.values()) {
                    if (player.savedData.globalData.hasCompletedGodBook(book) && hasAnItem(player, book.bookId).container == null) {
                        missing = true
                        addItemOrDrop(player, book.bookId, 1)
                    }
                }
                val damaged = player.savedData.globalData.godBook
                if (damaged != -1 && hasAnItem(player, GodBook.values()[damaged].damagedBookId).container == null) {
                    missing = true
                    addItemOrDrop(player, GodBook.values()[damaged].damagedBookId, 1)
                }
                if (missing) {
                    npc(FaceAnim.HAPPY, "As a matter of fact, I did! This book washed up on the", "beach, and I recognised it as yours!")
                    stage = END_DIALOGUE
                    return true
                }
                uncompleted = mutableListOf()
                for (book in GodBook.values()) {
                    if (!player.savedData.globalData.hasCompletedGodBook(book)) {
                        uncompleted!!.add(book)
                    }
                }
                val hasUncompleted = GodBook.values().any { hasAnItem(player, it.damagedBookId).container != null }
                if (uncompleted!!.isEmpty() || hasUncompleted) {
                    npc(FaceAnim.HALF_GUILTY, "No, sorry adventurer, I haven't.")
                    stage = END_DIALOGUE
                    return true
                }
                npc(FaceAnim.FRIENDLY, "Funnily enough I have! I found some books in caskets", "just the other day! I'll sell one to you for 5000 coins;", "what do you say?")
                stage++
            }

            4 -> {
                val names = uncompleted!!.map { it.name.lowercase().replace("_", " ", ignoreCase = true).replaceFirstChar { ch -> ch.titlecase(Locale.getDefault()) } }.toMutableList()
                names.add("Don't buy anything.")
                options(*names.toTypedArray())
                stage++
            }

            5 -> {
                if (buttonId - 1 > uncompleted!!.size - 1) {
                    end()
                    return true
                }
                if (freeSlots(player) == 0) {
                    player("Sorry, I don't have enough inventory space.")
                    stage = END_DIALOGUE
                    return true
                }
                if (!inInventory(player, Items.COINS_995, 5000)) {
                    player("Sorry, I don't seem to have enough coins.")
                    stage = END_DIALOGUE
                    return true
                }
                val purchase = uncompleted!![buttonId - 1]
                if (removeItem(player, Item(Items.COINS_995, 5000))) {
                    npc(FaceAnim.HAPPY,"Here you go!")
                    player.savedData.globalData.godBook = purchase.ordinal
                    addItem(player, purchase.damagedBookId)
                    stage = END_DIALOGUE
                } else {
                    end()
                }
            }

            /*
             * Handles reward dialogue for Horror from the deep.
             */

            100 -> npcl(FaceAnim.FRIENDLY, "It seems I was not as injured as I thought I was after all! I must thank you for all of your help!").also { stage++ }
            101 -> npcl(FaceAnim.FRIENDLY, "Now, about that casket you found on that monster's corpse...").also { stage++ }
            102 -> playerl(FaceAnim.FRIENDLY, "I have it here. You said you might be able to tell me something about it...?").also { stage++ }
            103 -> npcl(FaceAnim.FRIENDLY, "I can indeed! Here, let me have a closer look...").also { stage++ }
            104 -> npcl(FaceAnim.FRIENDLY, "Yes! There is something written on it!").also { stage++ }
            105 -> npcl(FaceAnim.FRIENDLY, "It is very faint however... Can you read it?").also { stage++ }
            106 -> options("Saradomin", "Zamorak", "Guthix").also { stage++ }
            107 -> when (buttonId) {
                1 -> playerl(FaceAnim.FRIENDLY, "I think it says... Saradomin...").also { stage = 108 }
                2 -> playerl(FaceAnim.FRIENDLY, "I think it says... Zamorak...").also { stage = 114 }
                3 -> playerl(FaceAnim.FRIENDLY, "I think it says... Guthix...").also { stage = 123 }
            }

            108 -> npc(FaceAnim.FRIENDLY, "Are you sure? I mean, are you REALLY sure?", "Maybe you'd better look again...").also { stage++ }
            109 -> options("Saradomin", "Zamorak", "Guthix").also { stage++ }
            110 -> when (buttonId) {
                1 -> playerl(FaceAnim.FRIENDLY, "Nope, it definitely says Saradomin.").also { stage = 111 }
                2 -> playerl(FaceAnim.FRIENDLY, "I think it says... Zamorak...").also { stage = 114 }
                3 -> playerl(FaceAnim.FRIENDLY, "I think it says... Guthix...").also { stage = 123 }
            }
            111 -> npcl(FaceAnim.FRIENDLY, "I think you're right! Hand it over, and let's see what's inside!").also { stage++ }
            112 -> npcl(FaceAnim.FRIENDLY, "Wow! It's an Holy Book of Saradomin! I thought these things had all vanished! Well, it's all yours, I hope you appreciate it.").also { stage++ }
            113 -> {
                end()
                if (removeItem(player!!, Items.RUSTY_CASKET_3849)) {
                    addItemOrDrop(player!!, Items.DAMAGED_BOOK_3839)
                }
            }

            114 -> npc(FaceAnim.FRIENDLY, "Are you sure? I mean, are you REALLY sure?", "Maybe you'd better look again...").also { stage++ }
            115 -> options("Saradomin", "Zamorak", "Guthix").also { stage++ }
            116 -> when (buttonId) {
                1 -> playerl(FaceAnim.FRIENDLY, "I think it says... Saradomin...").also { stage = 108 }
                2 -> playerl(FaceAnim.FRIENDLY, "Nope, it definitely says Zamorak.").also { stage = 117 }
                3 -> playerl(FaceAnim.FRIENDLY, "I think it says... Guthix...").also { stage = 120 }
            }
            117 -> npcl(FaceAnim.FRIENDLY, "I think you're right! Hand it over, and let's see what's inside!").also { stage++ }
            118 -> npcl(FaceAnim.FRIENDLY, "Wow! It's an Unholy Book of Zamorak! I thought these things had all vanished! Well, it's all yours, I hope you appreciate it.").also { stage++ }
            119 -> {
                end()
                if (removeItem(player!!, Items.RUSTY_CASKET_3849)) {
                    addItemOrDrop(player!!, Items.DAMAGED_BOOK_3841)
                }
            }
            120 -> npc(FaceAnim.FRIENDLY, "Are you sure? I mean, are you REALLY sure?", "Maybe you'd better look again...").also { stage++ }
            121 -> options("Saradomin", "Zamorak", "Guthix").also { stage++ }
            122 -> when (buttonId) {
                1 -> playerl(FaceAnim.FRIENDLY, "I think it says... Saradomin...").also { stage = 108 }
                2 -> playerl(FaceAnim.FRIENDLY, "I think it says... Zamorak...").also { stage = 114 }
                3 -> playerl(FaceAnim.FRIENDLY, "Nope, it definitely says Guthix.").also { stage = 123 }
            }
            123 -> npcl(FaceAnim.FRIENDLY, "I think you're right! Hand it over, and let's see what's inside!").also { stage++ }
            124 -> npcl(FaceAnim.FRIENDLY, "Wow! It's an Balance Book of Guthix! I thought these things had all vanished! Well, it's all yours, I hope you appreciate it.").also { stage++ }
            125 -> {
                end()
                if (removeItem(player!!, Items.RUSTY_CASKET_3849)) {
                    addItemOrDrop(player!!, Items.DAMAGED_BOOK_3843)
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.JOSSIK_1334)
}
