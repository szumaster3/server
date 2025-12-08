package content.region.other.keldagrim.plugin

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.NPCs

class OrdanUnnoteListener : InteractionListener {

    companion object {
        val notedOre = intArrayOf(
            Items.IRON_ORE_441,
            Items.COPPER_ORE_437,
            Items.TIN_ORE_439,
            Items.COAL_454,
            Items.MITHRIL_ORE_448,
            Items.ADAMANTITE_ORE_450,
            Items.SILVER_ORE_443,
            Items.GOLD_ORE_445,
            Items.RUNITE_ORE_452,
        )

        val UNNOTE_PRICES = hashMapOf(
            Items.IRON_ORE_441 to 8,
            Items.COPPER_ORE_437 to 10,
            Items.TIN_ORE_439 to 10,
            Items.COAL_454 to 22,
            Items.MITHRIL_ORE_448 to 81,
            Items.ADAMANTITE_ORE_450 to 330,
            Items.SILVER_ORE_443 to 37,
            Items.GOLD_ORE_445 to 75,
            Items.RUNITE_ORE_452 to 1000,
        )
    }

    override fun defineListeners() {
        onUseWith(IntType.NPC, notedOre, NPCs.ORDAN_2564) { player, noteType, _ ->
            val noteAmount = amountInInventory(player, noteType.id)
            val noteName = noteType.name
            val inventorySlots = freeSlots(player)

            player.dialogueInterpreter.sendOptions("$noteName un-note?", "1", "5", "10", "X")
            player.dialogueInterpreter.addAction { _, button ->
                var unNoteAmount = when (button) {
                    2 -> 1
                    3 -> 5
                    4 -> 10
                    5 -> {
                        sendInputDialogue(player, true, "Enter amount:")
                        { value ->
                            var actualAmount = (value as Int).coerceAtMost(inventorySlots).coerceAtMost(noteAmount)
                            val totalPrice = UNNOTE_PRICES[noteType.id]!! * actualAmount
                            if (amountInInventory(player, Items.COINS_995) >= totalPrice && actualAmount > 0)
                            {
                                removeItem(player, Item(Items.COINS_995, totalPrice), Container.INVENTORY)
                                removeItem(player, Item(noteType.id, actualAmount), Container.INVENTORY)
                                addItem(player, noteType.id - 1, actualAmount)
                            }
                        }
                        return@addAction
                    }

                    else -> return@addAction
                }

                unNoteAmount = unNoteAmount.coerceAtMost(inventorySlots).coerceAtMost(noteAmount)
                val totalPrice = UNNOTE_PRICES[noteType.id]!! * unNoteAmount
                if (amountInInventory(player, Items.COINS_995) >= totalPrice && unNoteAmount > 0)
                {
                    removeItem(player, Item(Items.COINS_995, totalPrice), Container.INVENTORY)
                    removeItem(player, Item(noteType.id, unNoteAmount), Container.INVENTORY)
                    addItem(player, noteType.id - 1, unNoteAmount)
                }
            }
            return@onUseWith true
        }
    }
}