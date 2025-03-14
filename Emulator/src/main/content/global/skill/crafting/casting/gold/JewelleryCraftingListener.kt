package content.global.skill.crafting.casting.gold

import core.api.*
import core.api.quest.isQuestComplete
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Scenery

class JewelleryCraftingListener : InteractionListener {
    private val barId = intArrayOf(Items.GOLD_BAR_2357, Items.PERFECT_GOLD_BAR_2365)
    private val furnaceId =
        intArrayOf(
            Scenery.FURNACE_4304,
            Scenery.FURNACE_6189,
            Scenery.FURNACE_11010,
            Scenery.FURNACE_11666,
            Scenery.FURNACE_12100,
            Scenery.FURNACE_12809,
            Scenery.FURNACE_18497,
            Scenery.FURNACE_26814,
            Scenery.FURNACE_30021,
            Scenery.FURNACE_30510,
            Scenery.FURNACE_36956,
            Scenery.FURNACE_37651,
        )
    private val amuletId =
        intArrayOf(
            Items.GOLD_AMULET_1673,
            Items.SAPPHIRE_AMULET_1675,
            Items.EMERALD_AMULET_1677,
            Items.RUBY_AMULET_1679,
            Items.DIAMOND_AMULET_1681,
            Items.DRAGONSTONE_AMMY_1683,
            Items.ONYX_AMULET_6579,
        )

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, barId, *furnaceId) { player, used, _ ->
            if (used == Item(Items.PERFECT_GOLD_BAR_2365)) {
                if (isQuestComplete(player, Quests.FAMILY_CREST)) {
                    sendMessage(player, "You can no longer smelt this.")
                    return@onUseWith false
                }
                if (inInventory(player, Items.RUBY_1603) && inInventory(player, Items.RING_MOULD_1592)) {
                    sendItemOnInterface(player, Components.CRAFTING_GOLD_446, 25, Items.PERFECT_RING_773, 1)
                }
                if (inInventory(player, Items.RUBY_1603) && inInventory(player, Items.NECKLACE_MOULD_1597)) {
                    sendItemOnInterface(player, Components.CRAFTING_GOLD_446, 47, Items.PERFECT_NECKLACE_774, 1)
                }
            } else {
                Jewellery.open(player)
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, amuletId, Items.BALL_OF_WOOL_1759) { player, used, with ->
            val data =
                Jewellery.JewelleryItem.forProduct(
                    if (used.id ==
                        Items.ONYX_AMULET_6579
                    ) {
                        Items.ONYX_AMULET_6579
                    } else {
                        used.asItem().id
                    },
                )
                    ?: return@onUseWith false
            if (getStatLevel(player, Skills.CRAFTING) < data.level) {
                sendMessage(player, "You need a crafting level of at least " + data.level + " to do that.")
                return@onUseWith false
            }
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItem(
                    player,
                    if (data ==
                        Jewellery.JewelleryItem.ONYX_AMULET
                    ) {
                        Items.ONYX_AMULET_6581
                    } else {
                        data.sendItem + 19
                    },
                )
                sendMessage(player, "You put some string on your amulet.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.SALVE_SHARD_4082, Items.BALL_OF_WOOL_1759) { player, used, with ->
            if (removeItem(player, with.asItem())) {
                replaceSlot(player, used.asItem().slot, Item(Items.SALVE_AMULET_4081))
                sendMessage(player, "You carefully string the shard of crystal.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.SALVE_AMULET_4081, Items.TARNS_DIARY_10587) { player, used, with ->
            replaceSlot(player, used.asItem().slot, Item(Items.SALVE_AMULETE_10588))
            return@onUseWith true
        }
    }
}
