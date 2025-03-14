package content.global.skill.crafting.items.armour.headdress

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items

class FeatherHeaddressListener : InteractionListener {
    private val featherIDs = FeatherHeaddress.values().map { it.base }.toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.ITEM, featherIDs, Items.COIF_1169) { player, used, with ->
            val item = FeatherHeaddress.forBase(used.id) ?: return@onUseWith false
            if (getStatLevel(player, Skills.CRAFTING) < 79) {
                sendMessage(player, "You need a crafting level of at least 79 in order to do this.")
            }

            if (!anyInInventory(player, item.base) || amountInInventory(player, item.base) < 20) {
                sendMessage(player, "You don't have required items in your inventory.")
            }

            if (removeItem(player, Item(item.base, 20))) {
                rewardXP(player, Skills.CRAFTING, 50.0)
                replaceSlot(player, with.asItem().slot, Item(item.product, 1))
                sendMessage(player, "You add the feathers to the coif to make a feathered headdress.")
            }
            return@onUseWith true
        }
    }
}
