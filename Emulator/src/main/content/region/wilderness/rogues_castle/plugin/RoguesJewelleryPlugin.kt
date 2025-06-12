package content.region.wilderness.rogues_castle.plugin

import core.api.*
import core.api.quest.hasRequirement
import core.api.ui.closeDialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class RoguesJewelleryPlugin : InteractionListener {
    private val jewelleryIDs = RoguesJewellery.values().map { it.item }.toIntArray()

    override fun defineListeners() {

        /*
         * Handles exchange jewellery with rogues.
         */

        onUseWith(IntType.NPC, jewelleryIDs, NPCs.ROGUE_8122) { player, used, _ ->
            if (!hasRequirement(player, Quests.SUMMERS_END)) return@onUseWith false

            val jewellery = RoguesJewellery.JewelleryMap[used.id] ?: return@onUseWith true
            val invAmount = amountInInventory(player, jewellery.item)
            val itemName = getItemName(used.id)

            sendNPCDialogue(
                player,
                NPCs.ROGUE_8122,
                "I'll give you ${jewellery.price * invAmount} coins each for that $itemName. Do we have a deal?",
                FaceAnim.HALF_ASKING
            )
            addDialogueAction(player) { _, _ ->
                sendDialogueOptions(player, "Select an option", "Yes, we do.", "No, we do not.")
                addDialogueAction(player) { _, button ->
                    when (button) {
                        2 -> {
                            closeDialogue(player)

                            if (invAmount >= 10000) {
                                sendNPCDialogue(
                                    player,
                                    NPCs.ROGUE_8122,
                                    "Whoa, that's quite a bit of jewellery you've got there! Please try to keep it in amounts smaller than 10000. Big numbers make my head hurt."
                                )
                                return@addDialogueAction
                            }

                            if (freeSlots(player) < 1) {
                                sendMessage(player, "You don't have enough inventory space for that.")
                                return@addDialogueAction
                            }

                            val removed = removeItem(player, Item(jewellery.item, invAmount), Container.INVENTORY)
                            if (removed) {
                                addItem(player, Items.COINS_995, invAmount * jewellery.price)
                                sendNPCDialogue(
                                    player,
                                    NPCs.ROGUE_8122,
                                    "It was a pleasure doing business with you. Come back if you have more jewellery to sell."
                                )
                            } else {
                                sendNPCDialogue(
                                    player,
                                    NPCs.ROGUE_8122,
                                    "Sorry, but I can’t seem to take those from you. Make sure it's unenchanted gold jewellery, and not noted."
                                )
                            }
                        }

                        else -> closeDialogue(player)
                    }
                }
            }
            return@onUseWith true
        }
    }
}

/**
 * Represents the rogue jewellery.
 */
private enum class RoguesJewellery(val item: Int, val amount: Int, val price: Int) {
    GOLD_RING(Items.GOLD_RING_1635, 1, 350),
    GOLD_RING_NOTED(Items.GOLD_RING_1636, 1, 350),
    SAPPHIRE_RING(Items.SAPPHIRE_RING_1637, 1, 900),
    SAPPHIRE_RING_NOTED(Items.SAPPHIRE_RING_1638, 1, 900),
    EMERALD_RING(Items.EMERALD_RING_1639, 1, 1275),
    EMERALD_RING_NOTED(Items.EMERALD_RING_1640, 1, 1275),
    RUBY_RING(Items.RUBY_RING_1641, 1, 2025),
    RUBY_RING_NOTED(Items.RUBY_RING_1642, 1, 2025),
    DIAMOND_RING(Items.DIAMOND_RING_1643, 1, 3525),
    DIAMOND_RING_NOTED(Items.DIAMOND_RING_1644, 1, 3525),
    DRAGONSTONE_RING(Items.DRAGONSTONE_RING_1645, 1, 17625),
    DRAGONSTONE_RING_NOTED(Items.DRAGONSTONE_RING_1646, 1, 17625),
    GOLD_NECKLACE(Items.GOLD_NECKLACE_1654, 1, 450),
    GOLD_NECKLACE_NOTED(Items.GOLD_NECKLACE_1655, 1, 450),
    SAPPHIRE_NECKLACE(Items.SAPPHIRE_NECKLACE_1656, 1, 1050),
    SAPPHIRE_NECKLACE_NOTED(Items.SAPPHIRE_NECKLACE_1657, 1, 1050),
    EMERALD_NECKLACE(Items.EMERALD_NECKLACE_1658, 1, 1425),
    EMERALD_NECKLACE_NOTED(Items.EMERALD_NECKLACE_1659, 1, 1425),
    RUBY_NECKLACE(Items.RUBY_NECKLACE_1660, 1, 2175),
    RUBY_NECKLACE_NOTED(Items.RUBY_NECKLACE_1661, 1, 2175),
    DIAMOND_NECKLACE(Items.DIAMOND_NECKLACE_1662, 1, 3675),
    DIAMOND_NECKLACE_NOTED(Items.DIAMOND_NECKLACE_1663, 1, 3675),
    DRAGON_NECKLACE(Items.DRAGON_NECKLACE_1664, 1, 18375),
    DRAGON_NECKLACE_NOTED(Items.DRAGON_NECKLACE_1665, 1, 18375),
    GOLD_BRACELET(Items.GOLD_BRACELET_11069, 1, 550),
    GOLD_BRACELET_NOTED(Items.GOLD_BRACELET_11070, 1, 550),
    SAPPHIRE_BRACELET(Items.SAPPHIRE_BRACELET_11072, 1, 1150),
    SAPPHIRE_BRACELET_NOTED(Items.SAPPHIRE_BRACELET_11073, 1, 1150),
    EMERALD_BRACELET(Items.EMERALD_BRACELET_11076, 1, 1525),
    EMERALD_BRACELET_NOTED(Items.EMERALD_BRACELET_11077, 1, 1525),
    RUBY_BRACELET(Items.RUBY_BRACELET_11085, 1, 2325),
    RUBY_BRACELET_NOTED(Items.RUBY_BRACELET_11086, 1, 2325),
    DIAMOND_BRACELET(Items.DIAMOND_BRACELET_11092, 1, 3825),
    DIAMOND_BRACELET_NOTED(Items.DIAMOND_BRACELET_11093, 1, 3825),
    DRAGON_BRACELET(Items.DRAGON_BRACELET_11115, 1, 19125),
    DRAGON_BRACELET_NOTED(Items.DRAGON_BRACELET_11116, 1, 19125),
    GOLD_AMULET(Items.GOLD_AMULET_1692, 1, 350),
    GOLD_AMULET_NOTED(Items.GOLD_AMULET_1693, 1, 350),
    SAPPHIRE_AMULET(Items.SAPPHIRE_AMULET_1694, 1, 900),
    SAPPHIRE_AMULET_NOTED(Items.SAPPHIRE_AMULET_1695, 1, 900),
    EMERALD_AMULET(Items.EMERALD_AMULET_1696, 1, 1275),
    EMERALD_AMULET_NOTED(Items.EMERALD_AMULET_1697, 1, 1275),
    RUBY_AMULET(Items.RUBY_AMULET_1698, 1, 2025),
    RUBY_AMULET_NOTED(Items.RUBY_AMULET_1699, 1, 2025),
    DIAMOND_AMULET(Items.DIAMOND_AMULET_1700, 1, 3525),
    DIAMOND_AMULET_NOTED(Items.DIAMOND_AMULET_1701, 1, 3525),
    DRAGONSTONE_AMMY(Items.DRAGONSTONE_AMMY_1702, 1, 17625),
    DRAGONSTONE_AMMY_NOTED(Items.DRAGONSTONE_AMMY_1703, 1, 17625);

    companion object {
        val JewelleryMap: MutableMap<Int, RoguesJewellery> = HashMap()

        init {
            for (jewellery in values()) {
                JewelleryMap[jewellery.item] = jewellery
            }
        }
    }
}