package content.region.wilderness.plugin.rogue_castle

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

class RoguesJewelleryPlugin : InteractionListener {

    companion object {
        private val BASE_JEWELLERY = intArrayOf(
            Items.GOLD_RING_1635,       Items.SAPPHIRE_RING_1637,       Items.EMERALD_RING_1639,
            Items.RUBY_RING_1641,       Items.DIAMOND_RING_1643,        Items.DRAGONSTONE_RING_1645,
            Items.GOLD_NECKLACE_1654,   Items.SAPPHIRE_NECKLACE_1656,   Items.EMERALD_NECKLACE_1658,
            Items.RUBY_NECKLACE_1660,   Items.DIAMOND_NECKLACE_1662,    Items.DRAGON_NECKLACE_1664,
            Items.GOLD_BRACELET_11069,  Items.SAPPHIRE_BRACELET_11072,  Items.EMERALD_BRACELET_11076,
            Items.RUBY_BRACELET_11085,  Items.DIAMOND_BRACELET_11092,   Items.DRAGON_BRACELET_11115,
            Items.GOLD_AMULET_1692,     Items.SAPPHIRE_AMULET_1694,     Items.EMERALD_AMULET_1696,
            Items.RUBY_AMULET_1698,     Items.DIAMOND_AMULET_1700,      Items.DRAGONSTONE_AMMY_1702
        )

        private val JEWELLERY: IntArray = BASE_JEWELLERY.flatMap { id ->
            listOfNotNull(id, ItemDefinition.forId(id).noteId.takeIf { it > 0 })
        }.toIntArray()
    }

    override fun defineListeners() {
        onUseWith(IntType.NPC, JEWELLERY, NPCs.ROGUE_8122) { player, used, _ ->
            if (!hasRequirement(player, Quests.SUMMERS_END)) return@onUseWith false

            val actualAmount = amountInInventory(player, used.id)
            if (actualAmount <= 0) return@onUseWith false

            val itemPrice = itemDefinition(used.id).value
            val itemName = getItemName(used.id)

            openDialogue(player, RogueJewelleryDialogue(used.id, actualAmount, itemName, itemPrice))
            return@onUseWith true
        }
    }

    inner class RogueJewelleryDialogue(private val itemId: Int, private val invAmount: Int, private val itemName: String, private val price: Int) : DialogueFile() {

        init { stage = 0 }

        override fun handle(componentID: Int, buttonID: Int) {
            val player = player ?: return

            when (stage) {
                0 -> sendNPCDialogue(player, NPCs.ROGUE_8122, "I'll give you ${price * invAmount} coins each for that $itemName. Do we have a deal?", FaceAnim.HALF_ASKING).also { stage++ }
                1 -> options("Yes, we do.", "No, we do not.").also { stage++ }
                2 -> when (buttonID) {
                    1 -> {
                        end()
                        val sellAmount = invAmount.coerceAtMost(10000)
                        if (sellAmount < invAmount) {
                            sendNPCDialogue(player, NPCs.ROGUE_8122, "Whoa, that's quite a bit of jewellery! Selling only $sellAmount at a time.")
                            return
                        }
                        val coinsToGive = sellAmount * price

                        if (freeSlots(player) == 0 && !inInventory(player, Items.COINS_995)) {
                            sendMessage(player, "You don't have enough inventory space for the coins.")
                            return
                        }

                        val removed = removeItem(player, Item(itemId, sellAmount))
                        if (removed) {
                            addItem(player, Items.COINS_995, coinsToGive)
                            sendNPCDialogue(player, NPCs.ROGUE_8122, "It was a pleasure doing business with you. Come back if you have more jewellery to sell.")
                        } else {
                            sendNPCDialogue(player, NPCs.ROGUE_8122, "Sorry, I can't seem to take those from you. Make sure it's unenchanted gold jewellery.")
                        }
                    }

                    2 -> end()
                }
            }
        }
    }
}
