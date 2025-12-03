package content.global.skill.crafting

import content.global.skill.slayer.SlayerManager
import core.api.*
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.StringUtils
import shared.consts.Components
import shared.consts.Items
import shared.consts.Quests
import kotlin.math.min

class JewelleryCraftingPlugin : InteractionListener, InterfaceListener {

    private val goldBarIds = intArrayOf(Items.GOLD_BAR_2357, Items.PERFECT_GOLD_BAR_2365)
    private val amuletId = intArrayOf(
        Items.GOLD_AMULET_1673,
        Items.SAPPHIRE_AMULET_1675,
        Items.EMERALD_AMULET_1677,
        Items.RUBY_AMULET_1679,
        Items.DIAMOND_AMULET_1681,
        Items.DRAGONSTONE_AMMY_1683,
        Items.ONYX_AMULET_6579
    )

    override fun defineListeners() {

        /*
         * Handles crafting interface.
         */

        onUseWith(IntType.SCENERY, goldBarIds, *CraftingObject.FURNACES) { player, used, _ ->
            if (used.id == Items.PERFECT_GOLD_BAR_2365) {
                if (isQuestComplete(player, Quests.FAMILY_CREST)) {
                    sendMessage(player, "You can no longer smelt this.")
                    return@onUseWith false
                }

                JewelleryUtils.open(player)

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

            JewelleryUtils.open(player)
            return@onUseWith true
        }

        /*
         * Handles crafting onyx amulet.
         */

        onUseWith(IntType.ITEM, amuletId, Items.BALL_OF_WOOL_1759) { player, used, with ->
            val amuletItem = used.asItem()
            val productId = if (amuletItem.id == Items.ONYX_AMULET_6579) Items.ONYX_AMULET_6579 else amuletItem.id
            val data = CraftingDefinition.JewelleryItem.forProduct(productId) ?: return@onUseWith false
            if (getStatLevel(player, Skills.CRAFTING) < data.level) {
                sendMessage(player, "You need a crafting level of at least ${data.level} to do that.")
                return@onUseWith false
            }

            if (removeItem(player, amuletItem) && removeItem(player, with.asItem())) {
                val resultId = if (data == CraftingDefinition.JewelleryItem.ONYX_AMULET) {
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

        onUseWith(IntType.ITEM, Items.SALVE_SHARD_4082, Items.BALL_OF_WOOL_1759) { player, used, _ ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY)) {
                addItem(player, Items.SALVE_AMULET_4081, 1)
                sendMessage(player, "You carefully string the shard of crystal.")
            }
            return@onUseWith true
        }

        /*
         * Handles enchanting salve amulet.
         */

        onUseWith(IntType.ITEM, Items.SALVE_AMULET_4081, Items.TARNS_DIARY_10587) { player, used, _ ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY)) {
                addItem(player, Items.SALVE_AMULETE_10588, 1)
            }
            return@onUseWith true
        }
    }

    override fun defineInterfaceListeners() {
        val buttonMap: Map<Int, (Player) -> CraftingDefinition.JewelleryItem> = mapOf(
            // RINGS
            20 to { _ -> CraftingDefinition.JewelleryItem.GOLD_RING },
            22 to { _ -> CraftingDefinition.JewelleryItem.SAPPHIRE_RING },
            24 to { _ -> CraftingDefinition.JewelleryItem.EMERALD_RING },
            26 to { player ->
                if (inInventory(player, CraftingDefinition.PERFECT_GOLD_BAR)) CraftingDefinition.JewelleryItem.PERFECT_RING
                else CraftingDefinition.JewelleryItem.RUBY_RING
            },
            28 to { _ -> CraftingDefinition.JewelleryItem.DIAMOND_RING },
            30 to { _ -> CraftingDefinition.JewelleryItem.DRAGONSTONE_RING },
            32 to { _ -> CraftingDefinition.JewelleryItem.ONYX_RING },
            35 to { _ -> CraftingDefinition.JewelleryItem.SLAYER_RING },

            // NECKLACES
            39 to { _ -> CraftingDefinition.JewelleryItem.GOLD_NECKLACE },
            41 to { _ -> CraftingDefinition.JewelleryItem.SAPPHIRE_NECKLACE },
            43 to { _ -> CraftingDefinition.JewelleryItem.EMERALD_NECKLACE },
            45 to { player ->
                if (inInventory(player, CraftingDefinition.PERFECT_GOLD_BAR)) CraftingDefinition.JewelleryItem.PERFECT_NECKLACE
                else CraftingDefinition.JewelleryItem.RUBY_NECKLACE
            },
            47 to { _ -> CraftingDefinition.JewelleryItem.DIAMOND_NECKLACE },
            49 to { _ -> CraftingDefinition.JewelleryItem.DRAGONSTONE_NECKLACE },
            51 to { _ -> CraftingDefinition.JewelleryItem.ONYX_NECKLACE },

            // AMULETS
            58 to { _ -> CraftingDefinition.JewelleryItem.GOLD_AMULET },
            60 to { _ -> CraftingDefinition.JewelleryItem.SAPPHIRE_AMULET },
            62 to { _ -> CraftingDefinition.JewelleryItem.EMERALD_AMULET },
            64 to { _ -> CraftingDefinition.JewelleryItem.RUBY_AMULET },
            66 to { _ -> CraftingDefinition.JewelleryItem.DIAMOND_AMULET },
            68 to { _ -> CraftingDefinition.JewelleryItem.DRAGONSTONE_AMULET },
            70 to { _ -> CraftingDefinition.JewelleryItem.ONYX_AMULET },

            // BRACELETS
            77 to { _ -> CraftingDefinition.JewelleryItem.GOLD_BRACELET },
            79 to { _ -> CraftingDefinition.JewelleryItem.SAPPHIRE_BRACELET },
            81 to { _ -> CraftingDefinition.JewelleryItem.EMERALD_BRACELET },
            83 to { _ -> CraftingDefinition.JewelleryItem.RUBY_BRACELET },
            85 to { _ -> CraftingDefinition.JewelleryItem.DIAMOND_BRACELET },
            87 to { _ -> CraftingDefinition.JewelleryItem.DRAGONSTONE_BRACELET },
            89 to { _ -> CraftingDefinition.JewelleryItem.ONYX_BRACELET }
        )

        val mouldMap = mapOf(
            "ring"     to CraftingDefinition.RING_MOULD,
            "necklace" to CraftingDefinition.NECKLACE_MOULD,
            "amulet"   to CraftingDefinition.AMULET_MOULD,
            "bracelet" to CraftingDefinition.BRACELET_MOULD
        )

        on(Components.CRAFTING_GOLD_446) { player, _, opcode, buttonID, _, itemID ->
            if (!clockReady(player, Clocks.SKILLING)) return@on true

            val data = buttonMap[buttonID]?.invoke(player) ?: return@on true
            val name = getItemName(data.sendItem).lowercase()

            if (getStatLevel(player, Skills.CRAFTING) < data.level) {
                val an = if (StringUtils.isPlusN(name)) "an" else "a"
                sendMessage(player, "You need a crafting level of ${data.level} to craft $an $name.")
                return@on true
            }

            val requiredMould = mouldMap.entries.firstOrNull { name.contains(it.key) }?.value
            if (requiredMould != null && !player.inventory.contains(requiredMould, 1)) {
                sendMessage(player, "You don't have the required mould to make this.")
                return@on true
            }

            val amount = when (opcode) {
                155 -> 1
                196 -> 5
                124 -> {
                    when (itemID) {
                        CraftingDefinition.GOLD_BAR         ->
                            amountInInventory(player, CraftingDefinition.GOLD_BAR)
                        CraftingDefinition.PERFECT_GOLD_BAR ->
                            amountInInventory(player, CraftingDefinition.PERFECT_GOLD_BAR)
                        else ->
                            min(amountInInventory(player, data.items[0]), amountInInventory(player, data.items[1]))
                    }
                }
                199 -> {
                    sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                        JewelleryUtils.make(player, data, value as Int)
                    }
                    return@on true
                }
                else -> 0
            }

            if (!SlayerManager.getInstance(player).flags.isRingUnlocked() && data == CraftingDefinition.JewelleryItem.SLAYER_RING) {
                sendMessages(player, "You don't know how to make this. Talk to any Slayer master in order to learn the", "ability that creates Slayer rings.")
                return@on true
            }

            JewelleryUtils.make(player, data, amount)
            return@on true
        }
    }
}
