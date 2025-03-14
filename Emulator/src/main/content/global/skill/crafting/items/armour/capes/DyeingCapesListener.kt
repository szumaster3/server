package content.global.skill.crafting.items.armour.capes

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import org.rs.consts.Items

class DyeingCapesListener : InteractionListener {
    companion object {
        private val DYES = Dyes.values().map { it.item.id }.toIntArray()
        private val CAPE = Capes.values().map { it.cape.id }.toIntArray()
    }

    override fun defineListeners() {
        onUseWith(IntType.ITEM, DYES, *CAPE) { player, used, with ->
            val product = Capes.forDye(used.id)

            product?.let {
                if (!inInventory(player, used.id)) {
                    sendMessage(player, "You don't have the required item to make this.")
                    return@onUseWith false
                }

                if (removeItem(player, used.id)) {
                    replaceSlot(player, with.index, it.cape)
                    addItem(player, Items.VIAL_229)
                    rewardXP(player, Skills.CRAFTING, 2.0)
                    sendMessage(player, "You dye the cape.")
                }
                return@onUseWith true
            }

            sendMessage(player, "This dye cannot be used with this cape.")
            return@onUseWith false
        }
    }
}
