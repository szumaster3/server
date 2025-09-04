package content.global.skill.crafting.items.armour

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items

class CrabItemCraftingPlugin : InteractionListener {

    private val CRAB_ITEMS = mapOf(
        Items.FRESH_CRAB_CLAW_7536 to Pair(Items.CRAB_CLAW_7537, 32.5),
        Items.FRESH_CRAB_SHELL_7538 to Pair(Items.CRAB_HELMET_7539, 32.5)
    )

    override fun defineListeners() {

        /*
         * Handles crafting crab claws & crab helmet.
         */

        onUseWith(IntType.ITEM, Items.CHISEL_1755, *CRAB_ITEMS.keys.toIntArray()) { player, _, used ->
            val (productId, xp) = CRAB_ITEMS[used.id] ?: return@onUseWith true
            val productName = getItemName(productId).lowercase()

            if (!hasLevelDyn(player, Skills.CRAFTING, 15)) {
                sendDialogue(player, "You need a crafting level of at least 15 in order to do this.")
                return@onUseWith true
            }

            val available = amountInInventory(player, used.id)
            if (available < 1) {
                sendMessage(player, "You do not have enough ${getItemName(used.id).lowercase()} to craft this.")
                return@onUseWith true
            }

            if (available == 1) {
                if (removeItem(player, used.id)) {
                    addItem(player, productId)
                    rewardXP(player, Skills.CRAFTING, xp)
                    sendMessage(player, "You craft a $productName.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(productId)
                create { _, amount ->
                    runTask(player, 1, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(used.id, amount))) {
                            addItem(player, productId, amount)
                            rewardXP(player, Skills.CRAFTING, xp * amount)
                            sendMessage(player, "You craft $amount ${if (amount > 1) "${productName}s" else productName}.")
                        }
                    }
                }
                calculateMaxAmount { available }
            }

            return@onUseWith true
        }
    }
}
