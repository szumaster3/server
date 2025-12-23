package content.global.skill.crafting

import content.global.skill.slayer.SlayerManager
import core.api.*
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.StringUtils
import shared.consts.Components
import shared.consts.Items
import shared.consts.Quests

class JewelleryCraftingPlugin : InteractionListener, InterfaceListener {

    private val goldBarIds = intArrayOf(Items.GOLD_BAR_2357, Items.PERFECT_GOLD_BAR_2365)
    private val amuletId = intArrayOf(Items.GOLD_AMULET_1673, Items.SAPPHIRE_AMULET_1675, Items.EMERALD_AMULET_1677, Items.RUBY_AMULET_1679, Items.DIAMOND_AMULET_1681, Items.DRAGONSTONE_AMMY_1683, Items.ONYX_AMULET_6579)

    override fun defineListeners() {

        /*
         * Handles crafting interface.
         */

        onUseWith(IntType.SCENERY, goldBarIds, *CraftingDefinition.FURNACES) { player, used, _ ->
            if (used.id == Items.PERFECT_GOLD_BAR_2365 && isQuestComplete(player, Quests.FAMILY_CREST)) {
                sendMessage(player, "You can no longer smelt this.")
                return@onUseWith false
            }
            CraftingDefinition.openGoldJewelleryInterface(player)
            if (used.id == Items.PERFECT_GOLD_BAR_2365 && inInventory(player, Items.RUBY_1603)) {
                if (inInventory(player, Items.RING_MOULD_1592)) sendItemOnInterface(player, Components.CRAFTING_GOLD_446, 25, Items.PERFECT_RING_773, 1)
                if (inInventory(player, Items.NECKLACE_MOULD_1597)) sendItemOnInterface(player, Components.CRAFTING_GOLD_446, 47, Items.PERFECT_NECKLACE_774, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles crafting onyx amulet.
         */

        onUseWith(IntType.ITEM, Items.BALL_OF_WOOL_1759, *amuletId) { player, used, with ->
            val amuletItem = with.asItem()
            val productId = if (amuletItem.id == Items.ONYX_AMULET_6579) Items.ONYX_AMULET_6579 else amuletItem.id
            val data = CraftingDefinition.Jewellery.forProduct(productId) ?: return@onUseWith false

            if (getStatLevel(player, Skills.CRAFTING) < data.level) {
                sendMessage(player, "You need a Crafting level of at least ${data.level} to do that.")
                return@onUseWith false
            }

            if (removeItem(player, used.id) && removeItem(player, amuletItem)) {
                val resultId = if (data == CraftingDefinition.Jewellery.ONYX_AMULET) Items.ONYX_AMULET_6581 else data.productId + 19
                addItem(player, resultId, 1)
                sendMessage(player, "You put some string on your amulet.")
            }
            return@onUseWith true
        }

        /*
         * Handles crafting salve amulet.
         */

        onUseWith(IntType.ITEM, Items.BALL_OF_WOOL_1759, Items.SALVE_SHARD_4082) { player, used, with ->
            if (removeItem(player, used.id) && removeItem(player, with.id)) {
                addItem(player, Items.SALVE_AMULET_4081, 1)
                sendMessage(player, "You carefully string the shard of crystal.")
            }
            return@onUseWith true
        }

        /*
         * Handles enchanting salve amulet.
         */

        onUseWith(IntType.ITEM, Items.TARNS_DIARY_10587, Items.SALVE_AMULET_4081) { player, _, with ->
            if (removeItem(player, with.id)) {
                addItem(player, Items.SALVE_AMULETE_10588, 1)
            }
            return@onUseWith true
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.CRAFTING_GOLD_446) { player, _, opcode, buttonID, _, itemID ->
            if (!clockReady(player, Clocks.SKILLING)) return@on true
            var amount = 0
            var data: CraftingDefinition.Jewellery? = null
            when (buttonID) {
                20 -> data = CraftingDefinition.Jewellery.GOLD_RING
                22 -> data = CraftingDefinition.Jewellery.SAPPHIRE_RING
                24 -> data = CraftingDefinition.Jewellery.EMERALD_RING
                26 ->
                    data =
                        if (inInventory(player, CraftingDefinition.PERFECT_GOLD_BAR)) {
                            CraftingDefinition.Jewellery.PERFECT_RING
                        } else {
                            CraftingDefinition.Jewellery.RUBY_RING
                        }

                28 -> data = CraftingDefinition.Jewellery.DIAMOND_RING
                30 -> data = CraftingDefinition.Jewellery.DRAGONSTONE_RING
                32 -> data = CraftingDefinition.Jewellery.ONYX_RING
                35 -> data = CraftingDefinition.Jewellery.SLAYER_RING
            }

            when (buttonID - 3) {
                39 -> data = CraftingDefinition.Jewellery.GOLD_NECKLACE
                41 -> data = CraftingDefinition.Jewellery.SAPPHIRE_NECKLACE
                43 -> data = CraftingDefinition.Jewellery.EMERALD_NECKLACE
                45 ->
                    data =
                        if (inInventory(player, CraftingDefinition.PERFECT_GOLD_BAR)) {
                            CraftingDefinition.Jewellery.PERFECT_NECKLACE
                        } else {
                            CraftingDefinition.Jewellery.RUBY_NECKLACE
                        }

                47 -> data = CraftingDefinition.Jewellery.DIAMOND_NECKLACE
                49 -> data = CraftingDefinition.Jewellery.DRAGONSTONE_NECKLACE
                51 -> data = CraftingDefinition.Jewellery.ONYX_NECKLACE
                58 -> data = CraftingDefinition.Jewellery.GOLD_AMULET
                60 -> data = CraftingDefinition.Jewellery.SAPPHIRE_AMULET
                62 -> data = CraftingDefinition.Jewellery.EMERALD_AMULET
                64 -> data = CraftingDefinition.Jewellery.RUBY_AMULET
                66 -> data = CraftingDefinition.Jewellery.DIAMOND_AMULET
                68 -> data = CraftingDefinition.Jewellery.DRAGONSTONE_AMULET
                70 -> data = CraftingDefinition.Jewellery.ONYX_AMULET
                77 -> data = CraftingDefinition.Jewellery.GOLD_BRACELET
                79 -> data = CraftingDefinition.Jewellery.SAPPHIRE_BRACELET
                81 -> data = CraftingDefinition.Jewellery.EMERALD_BRACELET
                83 -> data = CraftingDefinition.Jewellery.RUBY_BRACELET
                85 -> data = CraftingDefinition.Jewellery.DIAMOND_BRACELET
                87 -> data = CraftingDefinition.Jewellery.DRAGONSTONE_BRACELET
                89 -> data = CraftingDefinition.Jewellery.ONYX_BRACELET
            }

            if (data == null) {
                return@on true
            }

            val name = getItemName(data.productId).lowercase()

            if (getStatLevel(player, Skills.CRAFTING) < data.level) {
                val an = if (StringUtils.isPlusN(name)) "an" else "a"
                sendMessage(player, "You need a crafting level of " + data.level + " to craft " + an + " " + name + ".")
                return@on true
            }

            var flag = false

            if (name.contains("ring") && !player.inventory.contains(CraftingDefinition.RING_MOULD, 1)) {
                flag = true
            }
            if (name.contains("necklace") && !player.inventory.contains(CraftingDefinition.NECKLACE_MOULD, 1)) {
                flag = true
            }
            if (name.contains("amulet") && !player.inventory.contains(CraftingDefinition.AMULET_MOULD, 1)) {
                flag = true
            }
            if (name.contains("bracelet") && !player.inventory.contains(CraftingDefinition.BRACELET_MOULD, 1)) {
                flag = true
            }

            if (flag) {
                sendMessage(player, "You don't have the required mould to make this.")
                return@on flag
            }

            when (opcode) {
                155 -> amount = 1
                196 -> amount = 5
                124 -> {
                    amount = data.items.minOfOrNull { player.inventory.getAmount(Item(it)) } ?: 0
                }

                199 -> {
                    val d: CraftingDefinition.Jewellery = data
                    sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                        val inputAmount = (value.toString().toIntOrNull() ?: 0).coerceAtLeast(0)
                        if (inputAmount > 0) {
                            CraftingDefinition.make(player, d, inputAmount)
                        } else {
                            sendMessage(player, "Invalid amount entered.")
                        }
                    }
                    return@on true
                }
            }

            if (!SlayerManager.getInstance(player).flags.isRingUnlocked() &&
                data == CraftingDefinition.Jewellery.SLAYER_RING
            ) {
                sendMessages(
                    player,
                    "You don't know how to make this. Talk to any Slayer master in order to learn the",
                    "ability that creates Slayer rings.",
                )
                return@on true
            }

            CraftingDefinition.make(player, data, amount)
            return@on true
        }
    }
}
