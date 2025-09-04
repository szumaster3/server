package content.global.skill.crafting.jewellery

import content.global.skill.slayer.SlayerManager
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.StringUtils
import shared.consts.Components
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Scenery

class JewelleryCraftingPlugin : InteractionListener , InterfaceListener {

    private val barId = intArrayOf(Items.GOLD_BAR_2357, Items.PERFECT_GOLD_BAR_2365)
    private val furnaceId = intArrayOf(Scenery.FURNACE_4304, Scenery.FURNACE_6189, Scenery.FURNACE_11010, Scenery.FURNACE_11666, Scenery.FURNACE_12100, Scenery.FURNACE_12809, Scenery.FURNACE_18497, Scenery.FURNACE_26814, Scenery.FURNACE_30021, Scenery.FURNACE_30510, Scenery.FURNACE_36956, Scenery.FURNACE_37651)
    private val amuletId = intArrayOf(Items.GOLD_AMULET_1673, Items.SAPPHIRE_AMULET_1675, Items.EMERALD_AMULET_1677, Items.RUBY_AMULET_1679, Items.DIAMOND_AMULET_1681, Items.DRAGONSTONE_AMMY_1683, Items.ONYX_AMULET_6579)

    override fun defineListeners() {

        /*
         * Handles crafting interface.
         */

        onUseWith(IntType.SCENERY, barId, *furnaceId) { player, used, _ ->
            if (used.id == Items.PERFECT_GOLD_BAR_2365) {
                if (isQuestComplete(player, Quests.FAMILY_CREST)) {
                    sendMessage(player, "You can no longer smelt this.")
                    return@onUseWith false
                }

                Jewellery.open(player)

                val hasRuby = inInventory(player, Items.RUBY_1603)

                if (hasRuby) {
                    if (inInventory(player, Items.RING_MOULD_1592)) {
                        sendItemOnInterface(player, Components.CRAFTING_GOLD_446, 25, Items.PERFECT_RING_773, 1)
                    }

                    if (inInventory(player, Items.NECKLACE_MOULD_1597)) {
                        sendItemOnInterface(player, Components.CRAFTING_GOLD_446, 47, Items.PERFECT_NECKLACE_774, 1)
                    }
                }
                return@onUseWith true
            }

            Jewellery.open(player)
            return@onUseWith true
        }

        /*
         * Handles crafting onyx amulet.
         */

        onUseWith(IntType.ITEM, amuletId, Items.BALL_OF_WOOL_1759) { player, used, with ->
            val amuletItem = used.asItem()
            val productId = if (amuletItem.id == Items.ONYX_AMULET_6579) Items.ONYX_AMULET_6579 else amuletItem.id
            val data = Jewellery.JewelleryItem.forProduct(productId) ?: return@onUseWith false
            if (getStatLevel(player, Skills.CRAFTING) < data.level) {
                sendMessage(player, "You need a crafting level of at least ${data.level} to do that.")
                return@onUseWith false
            }

            if (removeItem(player, amuletItem) && removeItem(player, with.asItem())) {
                val resultId = if (data == Jewellery.JewelleryItem.ONYX_AMULET) {
                    Items.ONYX_AMULET_6581
                } else {
                    data.sendItem + 19
                }

                addItem(player, resultId)
                sendMessage(player, "You put some string on your amulet.")
            }

            return@onUseWith true
        }

        /*
         * Handles crafting salve amulet.
         */

        onUseWith(IntType.ITEM, Items.SALVE_SHARD_4082, Items.BALL_OF_WOOL_1759) { player, used, with ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY)) {
                addItem(player, Items.SALVE_AMULET_4081, 1)
                sendMessage(player, "You carefully string the shard of crystal.")
            }
            return@onUseWith true
        }

        /*
         * Handles enchanting salve amulet.
         */

        onUseWith(IntType.ITEM, Items.SALVE_AMULET_4081, Items.TARNS_DIARY_10587) { player, used, with ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY)) {
                addItem(player, Items.SALVE_AMULETE_10588, 1)
            }
            return@onUseWith true
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.CRAFTING_GOLD_446) { player, _, opcode, buttonID, _, itemID ->
            var amount = 0
            var data: Jewellery.JewelleryItem? = null
            when (buttonID) {
                20 -> data = Jewellery.JewelleryItem.GOLD_RING
                22 -> data = Jewellery.JewelleryItem.SAPPHIRE_RING
                24 -> data = Jewellery.JewelleryItem.EMERALD_RING
                26 ->
                    data =
                        if (inInventory(player, Jewellery.PERFECT_GOLD_BAR)) {
                            Jewellery.JewelleryItem.PERFECT_RING
                        } else {
                            Jewellery.JewelleryItem.RUBY_RING
                        }

                28 -> data = Jewellery.JewelleryItem.DIAMOND_RING
                30 -> data = Jewellery.JewelleryItem.DRAGONSTONE_RING
                32 -> data = Jewellery.JewelleryItem.ONYX_RING
                35 -> data = Jewellery.JewelleryItem.SLAYER_RING
            }

            when (buttonID - 3) {
                39 -> data = Jewellery.JewelleryItem.GOLD_NECKLACE
                41 -> data = Jewellery.JewelleryItem.SAPPHIRE_NECKLACE
                43 -> data = Jewellery.JewelleryItem.EMERALD_NECKLACE
                45 ->
                    data =
                        if (inInventory(player, Jewellery.PERFECT_GOLD_BAR)) {
                            Jewellery.JewelleryItem.PERFECT_NECKLACE
                        } else {
                            Jewellery.JewelleryItem.RUBY_NECKLACE
                        }

                47 -> data = Jewellery.JewelleryItem.DIAMOND_NECKLACE
                49 -> data = Jewellery.JewelleryItem.DRAGONSTONE_NECKLACE
                51 -> data = Jewellery.JewelleryItem.ONYX_NECKLACE
                58 -> data = Jewellery.JewelleryItem.GOLD_AMULET
                60 -> data = Jewellery.JewelleryItem.SAPPHIRE_AMULET
                62 -> data = Jewellery.JewelleryItem.EMERALD_AMULET
                64 -> data = Jewellery.JewelleryItem.RUBY_AMULET
                66 -> data = Jewellery.JewelleryItem.DIAMOND_AMULET
                68 -> data = Jewellery.JewelleryItem.DRAGONSTONE_AMULET
                70 -> data = Jewellery.JewelleryItem.ONYX_AMULET
                77 -> data = Jewellery.JewelleryItem.GOLD_BRACELET
                79 -> data = Jewellery.JewelleryItem.SAPPHIRE_BRACELET
                81 -> data = Jewellery.JewelleryItem.EMERALD_BRACELET
                83 -> data = Jewellery.JewelleryItem.RUBY_BRACELET
                85 -> data = Jewellery.JewelleryItem.DIAMOND_BRACELET
                87 -> data = Jewellery.JewelleryItem.DRAGONSTONE_BRACELET
                89 -> data = Jewellery.JewelleryItem.ONYX_BRACELET
            }

            if (data == null) {
                return@on true
            }

            val name = getItemName(data.sendItem).lowercase()

            if (getStatLevel(player, Skills.CRAFTING) < data.level) {
                val an = if (StringUtils.isPlusN(name)) "an" else "a"
                sendMessage(player, "You need a crafting level of " + data.level + " to craft " + an + " " + name + ".")
                return@on true
            }

            var flag = false

            if (name.contains("ring") && !player.inventory.contains(Jewellery.RING_MOULD, 1)) {
                flag = true
            }
            if (name.contains("necklace") && !player.inventory.contains(Jewellery.NECKLACE_MOULD, 1)) {
                flag = true
            }
            if (name.contains("amulet") && !player.inventory.contains(Jewellery.AMULET_MOULD, 1)) {
                flag = true
            }
            if (name.contains("bracelet") && !player.inventory.contains(Jewellery.BRACELET_MOULD, 1)) {
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
                    amount =
                        if (itemID == Jewellery.GOLD_BAR) {
                            player.inventory.getAmount(Item(Jewellery.GOLD_BAR))
                        } else if (itemID ==
                            Jewellery.PERFECT_GOLD_BAR
                        ) {
                            player.inventory.getAmount(Item(Jewellery.PERFECT_GOLD_BAR))
                        } else {
                            val first = player.inventory.getAmount(Item(data.items[0]))
                            val second = player.inventory.getAmount(Item(data.items[1]))

                            if (first == second) {
                                first
                            } else if (first > second) {
                                second
                            } else {
                                first
                            }
                        }
                }

                199 -> {
                    val d: Jewellery.JewelleryItem = data
                    sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                        Jewellery.make(player, d, value as Int)
                    }
                    return@on true
                }
            }

            if (!SlayerManager.getInstance(player).flags.isRingUnlocked() &&
                data == Jewellery.JewelleryItem.SLAYER_RING
            ) {
                sendMessages(
                    player,
                    "You don't know how to make this. Talk to any Slayer master in order to learn the",
                    "ability that creates Slayer rings.",
                )
                return@on true
            }

            Jewellery.make(player, data, amount)
            return@on true
        }
    }
}
