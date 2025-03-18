package content.region.fremennik.dialogue.lighthouse

import content.data.GameAttributes
import content.data.GodBook
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class JossikDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (inInventory(player, Items.RUSTY_CASKET_3849, 1)) {
            playerl(FaceAnim.FRIENDLY, "I see you managed to escape from those monsters intact!")
            setAttribute(player, GameAttributes.GOD_BOOKS, true)
            stage = 100
            return true
        }
        npc("Hello again, adventurer.", "What brings you this way?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        var uncompleted: MutableList<GodBook>? = null
        when (stage) {
            0 -> options("Can I see your wares?", "Have you found any prayerbooks?").also { stage++ }
            1 ->
                stage =
                    if (buttonId == 1) {
                        player("Can I see your wares?")
                        10
                    } else {
                        player("Have you found any prayerbooks?")
                        20
                    }

            20 -> {
                var missing = false
                for (book in GodBook.values()) {
                    if (player.getSavedData().globalData.hasCompletedGodBook(book) && !player.hasItem(book.book)) {
                        missing = true
                        player.inventory.add(book.book, player)
                        npc(
                            "As a matter of fact, I did! This book washed up on the",
                            "beach, and I recognised it as yours!",
                        )
                    }
                }
                val damaged = player.getSavedData().globalData.getGodBook()
                if (damaged != -1 && !player.hasItem(GodBook.values()[damaged].damagedBook)) {
                    missing = true
                    player.inventory.add(GodBook.values()[damaged].damagedBook, player)
                    npc(
                        "As a matter of fact, I did! This book washed up on the",
                        "beach, and I recognised it as yours!",
                    )
                }
                if (missing) {
                    stage = 23
                    return true
                }
                uncompleted = ArrayList(5)
                for (book in GodBook.values()) {
                    if (!player.getSavedData().globalData.hasCompletedGodBook(book!!)) {
                        uncompleted.add(book!!)
                    }
                }
                var hasUncompleted = false
                for (book in GodBook.values()) {
                    if (player.hasItem(book.damagedBook)) {
                        hasUncompleted = true
                    }
                }
                if (uncompleted.size == 0 || hasUncompleted) {
                    npc("No, sorry adventurer, I haven't.")
                    stage = 23
                    return true
                }
                npc(
                    "Funnily enough I have! I found some books in caskets",
                    "just the other day! I'll sell one to you for 5000 coins;",
                    "what do you say?",
                )
                stage++
            }

            21 -> {
                val names = arrayOfNulls<String>(uncompleted!!.size + 1)
                var i = 0
                while (i < uncompleted!!.size) {
                    names[i] = uncompleted!![i].bookName
                    i++
                }
                names[names.size - 1] = "Don't buy anything."
                options(*names)
                stage++
            }

            22 -> {
                if (buttonId - 1 > uncompleted!!.size - 1) {
                    player("Don't buy anything.")
                    stage = 23
                }
                if (!inInventory(player, Items.COINS_995, 5000)) {
                    player("Sorry, I don't seem to have enough coins.")
                    stage = 23
                }
                if (freeSlots(player) == 0) {
                    player("Sorry, I don't have enough inventory space.")
                    stage = 23
                }
                val purchase = uncompleted!![buttonId - 1]
                if (purchase != null && removeItem(player, Item(Items.COINS_995, 5000))) {
                    npc("Here you go!")
                    player.getSavedData().globalData.setGodBook(purchase.ordinal)
                    player.inventory.add(purchase.damagedBook, player)
                    stage = 23
                } else {
                    end()
                }
            }

            23 -> end()
            10 -> {
                npc("Sure thing!", "I think you'll agree, my prices are remarkable!")
                stage++
            }

            11 -> {
                npc.openShop(player)
                end()
            }

            /*
             * Handles reward dialogue for Horror from the deep.
             */
            100 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "It seems I was not as injured as I thought I was after all! I must thank you for all of your help!",
                ).also {
                    stage++
                }

            101 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Now, about that casket you found on that monster's corpse...",
                ).also { stage++ }

            102 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "I have it here. You said you might be able to tell me something about it...?",
                ).also {
                    stage++
                }

            103 -> npcl(FaceAnim.FRIENDLY, "I can indeed! Here, let me have a closer look...").also { stage++ }
            104 -> npcl(FaceAnim.FRIENDLY, "Yes! There is something written on it!").also { stage++ }
            105 -> npcl(FaceAnim.FRIENDLY, "It is very faint however... Can you read it?").also { stage++ }
            106 -> options("Saradomin", "Zamorak", "Guthix").also { stage++ }
            107 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "I think it says... Saradomin...").also { stage = 108 }
                    2 -> playerl(FaceAnim.FRIENDLY, "I think it says... Zamorak...").also { stage = 114 }
                    3 -> playerl(FaceAnim.FRIENDLY, "I think it says... Guthix...").also { stage = 123 }
                }

            108 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Are you sure? I mean, are you REALLY sure?",
                    "Maybe you'd better look again...",
                ).also {
                    stage++
                }

            109 -> options("Saradomin", "Zamorak", "Guthix").also { stage++ }
            110 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Nope, it definitely says Saradomin.").also { stage = 111 }
                    2 -> playerl(FaceAnim.FRIENDLY, "I think it says... Zamorak...").also { stage = 114 }
                    3 -> playerl(FaceAnim.FRIENDLY, "I think it says... Guthix...").also { stage = 123 }
                }

            111 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I think you're right! Hand it over, and let's see what's inside!",
                ).also { stage++ }

            112 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Wow! It's an Holy Book of Saradomin! I thought these things had all vanished! Well, it's all yours, I hope you appreciate it.",
                ).also {
                    stage++
                }

            113 -> {
                end()
                if (removeItem(player!!, Items.RUSTY_CASKET_3849)) {
                    addItemOrDrop(player!!, Items.DAMAGED_BOOK_3839)
                }
            }

            114 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Are you sure? I mean, are you REALLY sure?",
                    "Maybe you'd better look again...",
                ).also {
                    stage++
                }

            115 -> options("Saradomin", "Zamorak", "Guthix").also { stage++ }
            116 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "I think it says... Saradomin...").also { stage = 108 }
                    2 -> playerl(FaceAnim.FRIENDLY, "Nope, it definitely says Zamorak.").also { stage = 117 }
                    3 -> playerl(FaceAnim.FRIENDLY, "I think it says... Guthix...").also { stage = 120 }
                }

            117 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I think you're right! Hand it over, and let's see what's inside!",
                ).also { stage++ }

            118 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Wow! It's an Unholy Book of Zamorak! I thought these things had all vanished! Well, it's all yours, I hope you appreciate it.",
                ).also {
                    stage++
                }

            119 -> {
                end()
                if (removeItem(player!!, Items.RUSTY_CASKET_3849)) {
                    addItemOrDrop(player!!, Items.DAMAGED_BOOK_3841)
                }
            }

            120 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Are you sure? I mean, are you REALLY sure?",
                    "Maybe you'd better look again...",
                ).also {
                    stage++
                }

            121 -> options("Saradomin", "Zamorak", "Guthix").also { stage++ }
            122 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "I think it says... Saradomin...").also { stage = 108 }
                    2 -> playerl(FaceAnim.FRIENDLY, "I think it says... Zamorak...").also { stage = 114 }
                    3 -> playerl(FaceAnim.FRIENDLY, "Nope, it definitely says Guthix.").also { stage = 123 }
                }

            123 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I think you're right! Hand it over, and let's see what's inside!",
                ).also { stage++ }

            124 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Wow! It's an Balance Book of Guthix! I thought these things had all vanished! Well, it's all yours, I hope you appreciate it.",
                ).also {
                    stage++
                }

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
