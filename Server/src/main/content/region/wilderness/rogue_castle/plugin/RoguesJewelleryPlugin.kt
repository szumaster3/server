package content.region.wilderness.rogue_castle.plugin

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
        private val baseJewellery = intArrayOf(Items.GOLD_RING_1635, Items.SAPPHIRE_RING_1637, Items.EMERALD_RING_1639, Items.RUBY_RING_1641, Items.DIAMOND_RING_1643, Items.DRAGONSTONE_RING_1645, Items.GOLD_NECKLACE_1654, Items.SAPPHIRE_NECKLACE_1656, Items.EMERALD_NECKLACE_1658, Items.RUBY_NECKLACE_1660, Items.DIAMOND_NECKLACE_1662, Items.DRAGON_NECKLACE_1664, Items.GOLD_BRACELET_11069, Items.SAPPHIRE_BRACELET_11072, Items.EMERALD_BRACELET_11076, Items.RUBY_BRACELET_11085, Items.DIAMOND_BRACELET_11092, Items.DRAGON_BRACELET_11115, Items.GOLD_AMULET_1692, Items.SAPPHIRE_AMULET_1694, Items.EMERALD_AMULET_1696, Items.RUBY_AMULET_1698, Items.DIAMOND_AMULET_1700, Items.DRAGONSTONE_AMMY_1702)
        private val JEWELLERY = baseJewellery
            .flatMap { id ->
                listOf(id) + ItemDefinition.forId(id).noteId
                    .takeIf { it > 0 }
                    .let { if (it != null) listOf(it) else emptyList() }
            }.toIntArray()
    }

    override fun defineListeners() {

        /*
         * Handles exchange jewellery with rogues.
         */

        onUseWith(IntType.NPC, JEWELLERY, NPCs.ROGUE_8122) { player, used, _ ->
            if (!hasRequirement(player, Quests.SUMMERS_END)) return@onUseWith false

            val itemAmount = amountInInventory(player, used.id)
            val itemPrice = itemDefinition(used.id).getValue()
            val itemName = getItemName(used.id)

            openDialogue(player, RogueJewelleryDialogue(used.id, itemAmount, itemName, itemPrice))
            return@onUseWith true
        }

    }

    inner class RogueJewelleryDialogue(
        private val itemId: Int,
        private val invAmount: Int,
        private val itemName: String,
        private val price: Int
    ) : DialogueFile() {

        init { stage = 0 }

        override fun handle(componentID: Int, buttonID: Int) {
            val player = player ?: return

            when (stage) {
                0 -> {
                    sendNPCDialogue(player, NPCs.ROGUE_8122, "I'll give you ${price * invAmount} coins each for that $itemName. Do we have a deal?", FaceAnim.HALF_ASKING)
                    stage = 1
                }

                1 -> {
                    options("Yes, we do.", "No, we do not.")
                    stage = 2
                }

                2 -> {
                    when (buttonID) {
                        1 -> {
                            end()
                            if (invAmount > 10000) {
                                sendNPCDialogue(player, NPCs.ROGUE_8122, "Whoa, that's quite a bit of jewellery you've got there! Please keep it below 10000 items at a time.")
                                return
                            }

                            if (freeSlots(player) < 1) {
                                sendMessage(player, "You don't have enough inventory space.")
                                return
                            }

                            val removed = removeItem(player, Item(itemId, invAmount))
                            if (removed) {
                                addItem(player, Items.COINS_995, invAmount * price)
                                sendNPCDialogue(player, NPCs.ROGUE_8122, "It was a pleasure doing business with you. Come back if you have more jewellery to sell.")
                            } else {
                                sendNPCDialogue(player, NPCs.ROGUE_8122, "Sorry, I can’t seem to take those from you. Make sure it's unenchanted gold jewellery, and not noted.")
                            }
                        }

                        2 -> end()
                    }
                }
            }
        }
    }
}
