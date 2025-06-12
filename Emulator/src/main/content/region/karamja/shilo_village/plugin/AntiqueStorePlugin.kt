package content.region.karamja.shilo_village.plugin

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class AntiqueStorePlugin : InteractionListener {

    val ANTIQUE_ITEMS = AntiqueItem.values().map { it.antique }.toIntArray()

    override fun defineListeners() {

        /*
         * Handles antique items exchange with Yanni NPCs.
         */

        onUseWith(IntType.NPC, ANTIQUE_ITEMS, NPCs.YANNI_SALIKA_515) { player, used, _ ->
            val antique = used.id

            if (!inInventory(player, antique, 1)) {
                sendNPCDialogue(player, NPCs.YANNI_SALIKA_515, "Sorry Bwana, you have nothing I am interested in.")
                return@onUseWith true
            }

            when (antique) {
                Items.BLACK_PRISM_4808 -> openDialogue(player, BlackPrismDialogue())
                else -> openDialogue(player, YanniDialogue(antique))
            }
            return@onUseWith true
        }
    }

}

/**
 * Represents the Antique items.
 */
private enum class AntiqueItem(val antique: Int, val price: Int) {
    BONE_KEY(Items.BONE_KEY_605, 100),
    STONE_PLAQUE(Items.STONE_PLAQUE_606, 100),
    TATTERED_SCROLL(Items.TATTERED_SCROLL_607, 100),
    CRUMPLED_SCROLL(Items.CRUMPLED_SCROLL_608, 100),
    LOCATING_CRYSTAL(Items.LOCATING_CRYSTAL_611, 500),
    BEADS_OF_THE_DEAD(Items.BEADS_OF_THE_DEAD_616, 1000),
    BERVIRIUS_NOTES(Items.BERVIRIUS_NOTES_624, 100),
    BLACK_PRISM(Items.BLACK_PRISM_4808, 5000);

    companion object {
        /**
         * Finds an [AntiqueItem] by item id.
         */
        fun getAntiqueItem(id: Int): AntiqueItem? = values().find { it.antique == id }
    }
}

private class BlackPrismDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.YANNI_SALIKA_515)
        when (stage) {
            0 -> sendDialogue(player!!, "You show the black prism to Yanni.").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "Ah you'd like to sell this to me would you? I can offer you 5000 coins!").also { stage++ }

            2 -> {
                setTitle(player!!, 2)
                sendDialogueOptions(
                    player!!,
                    "SELL THE PRISM FOR 5000 COINS?",
                    "Yes, I'll sell it for 5000",
                    "No, I think I'll hold on it a while longer yet.",
                )
                stage++
            }

            3 -> when (buttonID) {
                1 -> player("Yes, I'll sell it for 5000 coins.").also { stage = 5 }
                2 -> player("No, I think I'll hold on it a while longer yet.").also { stage++ }
            }

            4 -> npc("Very well my friend, come back if you change", "your mind.").also { stage = END_DIALOGUE }
            5 -> npc("Very well my friend, let me count out your reward.").also { stage++ }
            6 -> {
                end()
                if (removeItem(player!!, Items.BLACK_PRISM_4808)) {
                    sendMessage(player!!, "You sell the black prism for 5000 coins.")
                    addItemOrDrop(player!!, Items.COINS_995, 5000)
                    npc("Thanks!")
                }
            }
        }
    }
}

private class YanniDialogue(antique: Int) : DialogueFile() {

    private val item = AntiqueItem.getAntiqueItem(antique)

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.YANNI_SALIKA_515)

        val itemName = getItemName(item!!.antique)

        val dialogue = when (item.antique) {
            Items.BEADS_OF_THE_DEAD_616 -> "Impressive necklace there."
            Items.BLACK_PRISM_4808 -> "Ah you'd like to sell this to me would you? I can offer you 5000 coins!"
            Items.BERVIRIUS_NOTES_624 -> "That's a great copy of Bervirius notes."
            else -> "That's a great $itemName."
        }

        when (stage) {
            START_DIALOGUE -> {
                npcl(FaceAnim.FRIENDLY, dialogue)
                stage++
            }

            1 -> {
                npcl(FaceAnim.FRIENDLY, "I'll give you ${item.price} coins for your $itemName...")
                stage++
            }

            2 -> {
                setTitle(player!!, 2)
                sendDialogueOptions(player!!, "Sell the $itemName?", "Yes.", "No.")
                stage++
            }

            3 -> {
                if (buttonID != 1) {
                    end()
                    return
                }
                end()
                if (removeItem(player!!, item.antique)) {
                    npc("Here's ${item.price} for it.")
                    sendMessage(player!!, "You sell the $itemName for ${item.price} gold.")
                    addItemOrDrop(player!!, Items.COINS_995, item.price)
                } else {
                    sendMessage(player!!, "You don't have the $itemName.")
                }
            }
        }
    }
}