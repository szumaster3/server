package content.global.skill.crafting.items.armour

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items

class WarbonnetsCraftingPlugin : InteractionListener {
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
                addItem(player, item.product, 1)
                sendMessage(player, "You add the feathers to the coif to make a feathered headdress.")
            }
            return@onUseWith true
        }
    }
}


/**
 * Represents the feather headdress.
 */
private enum class FeatherHeaddress(val base: Int, val product: Int) {
    FEATHER_HEADDRESS_BLUE(Items.BLUE_FEATHER_10089, Items.FEATHER_HEADDRESS_12210),
    FEATHER_HEADDRESS_ORANGE(Items.ORANGE_FEATHER_10091, Items.FEATHER_HEADDRESS_12222),
    FEATHER_HEADDRESS_RED(Items.RED_FEATHER_10088, Items.FEATHER_HEADDRESS_12216),
    FEATHER_HEADDRESS_STRIPY(Items.STRIPY_FEATHER_10087, Items.FEATHER_HEADDRESS_12219),
    FEATHER_HEADDRESS_YELLOW(Items.YELLOW_FEATHER_10090, Items.FEATHER_HEADDRESS_12213);

    companion object {
        private val baseToHeaddressMap = HashMap<Int, FeatherHeaddress>()

        init {
            for (headdress in values()) {
                baseToHeaddressMap[headdress.base] = headdress
            }
        }

        /**
         * For base feather headdress.
         *
         * @param baseId the base id
         * @return the feather headdress
         */
        fun forBase(baseId: Int): FeatherHeaddress? {
            return baseToHeaddressMap[baseId]
        }
    }
}