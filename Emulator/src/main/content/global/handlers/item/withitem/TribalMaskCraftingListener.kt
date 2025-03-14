package content.global.handlers.item.withitem

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items

class TribalMaskCraftingListener : InteractionListener {
    private val itemIDs =
        mapOf(
            Items.TRIBAL_MASK_6335 to Items.BROODOO_SHIELD_10_6215,
            Items.TRIBAL_MASK_6337 to Items.BROODOO_SHIELD_10_6237,
            Items.TRIBAL_MASK_6339 to Items.BROODOO_SHIELD_10_6259,
        )
    private val nailIDs =
        intArrayOf(
            Items.BRONZE_NAILS_4819,
            Items.IRON_NAILS_4820,
            Items.STEEL_NAILS_1539,
            Items.BLACK_NAILS_4821,
            Items.MITHRIL_NAILS_4822,
            Items.ADAMANTITE_NAILS_4823,
            Items.RUNE_NAILS_4824,
        )
    private val snakeskinId = Items.SNAKESKIN_6289

    override fun defineListeners() {
        itemIDs.forEach { (maskId, shieldId) ->
            onUseWith(IntType.ITEM, Items.HAMMER_2347, maskId) { player, _, _ ->
                val animation =
                    when (maskId) {
                        Items.TRIBAL_MASK_6335 -> Animations.CRAFT_SHIELD_GREEN_2410
                        Items.TRIBAL_MASK_6337 -> Animations.CRAFT_SHIELD_ORANGE_2411
                        Items.TRIBAL_MASK_6339 -> Animations.CRAFT_SHIELD_WHITE_2409
                        else -> return@onUseWith false
                    }

                if (getStatLevel(player, Skills.CRAFTING) < 35) {
                    sendMessage(player, "You don't have the crafting level needed to do that.")
                    return@onUseWith false
                }

                val resourcesNeeded = arrayOf(Item(snakeskinId, 2), Item(maskId, 1))
                val totalNails = nailIDs.sumOf { player.inventory.getAmount(Item(it, 1)) }

                if (totalNails < 8) {
                    sendMessage(player, "You don't have the resources needed to create a Broodoo Shield.")
                    return@onUseWith false
                }

                if (!removeItem(player, resourcesNeeded + Array(8) { Item(nailIDs[0], 1) })) {
                    sendMessage(player, "You don't have the resources needed to create a Broodoo Shield.")
                    return@onUseWith false
                }

                addItemOrDrop(player, shieldId, 1)
                sendMessage(player, "You create a Broodoo shield!")
                rewardXP(player, Skills.CRAFTING, 100.0)

                animate(player, animation)
                return@onUseWith true
            }
        }
    }
}
