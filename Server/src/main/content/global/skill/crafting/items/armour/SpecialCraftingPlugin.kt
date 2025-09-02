package content.global.skill.crafting.items.armour

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items

/**
 * Handles special armour crafting.
 */
class SpecialCraftingPlugin : InteractionListener {

    override fun defineListeners() {
        craft(
            items = FeatherHeaddress.FEATHER,
            base = Items.COIF_1169,
            level = 79,
            amount = 20
        ) { player, used ->
            val headdress = FeatherHeaddress.forBase(used.id) ?: return@craft
            addItem(player, headdress.product, 1)
            rewardXP(player, Skills.CRAFTING, 50.0)
            sendMessage(player, "You add the feathers to the coif to make a feathered headdress.")
        }

        craft(
            items = Snelms.SHELLS,
            base = Items.CHISEL_1755,
            level = 15
        ) { player, used ->
            val snelm = Snelms.fromShellId(used.id) ?: return@craft
            addItem(player, snelm.product, 1)
            rewardXP(player, Skills.CRAFTING, 32.5)
            sendMessage(player, "You craft the shell into a helmet.")
        }

        craft(
            items = intArrayOf(Items.FRESH_CRAB_CLAW_7536, Items.FRESH_CRAB_SHELL_7538),
            base = Items.CHISEL_1755,
            level = 15
        ) { player, used ->
            val productId = when (used.id) {
                Items.FRESH_CRAB_CLAW_7536 -> Items.CRAB_CLAW_7537
                Items.FRESH_CRAB_SHELL_7538 -> Items.CRAB_HELMET_7539
                else -> return@craft
            }
            addItem(player, productId, 1)
            rewardXP(player, Skills.CRAFTING, 32.5)
            sendMessage(player, "You craft the crab into usable parts.")
        }
    }

    /**
     * Handles a crafting action.
     *
     * @param items  items array.
     * @param base base item id.
     * @param level required level.
     * @param amount amount.
     * @param action execution for each craft.
     */
    private fun craft(
        items: IntArray,
        base: Int,
        level: Int,
        amount: Int = 1,
        action: (player: Player, used: Item) -> Unit
    ) {
        require(amount > 0) { "Amount per craft must be at least 1." }

        onUseWith(IntType.ITEM, items, base) { player, used, _ ->
            if (getStatLevel(player, Skills.CRAFTING) < level) {
                sendMessage(player, "You need a Crafting level of at least $level to do this.")
                return@onUseWith true
            }

            val available = amountInInventory(player, used.id) / amount
            if (available < 1) {
                sendMessage(player, "You do not have enough ${getItemName(used.id).lowercase()} to craft this.")
                return@onUseWith true
            }

            repeat(available) {
                if (removeItem(player, Item(used.id, amount))) {
                    action(player, used.asItem())
                }
            }
            return@onUseWith true
        }
    }
}
