package content.global.skill.crafting.items.armour

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items

/**
 * Handles crafting feathered headdresses by combining feathers with a coif.
 */
class FeatherHeaddressPlugin : InteractionListener {

    /** Array of all feather item IDs used in crafting. */
    private val featherIDs = FeatherHeaddress.values().map { it.base }.toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.ITEM, featherIDs, Items.COIF_1169) { player, used, _ ->

            val headdress = FeatherHeaddress.forBase(used.id) ?: return@onUseWith false

            if (getStatLevel(player, Skills.CRAFTING) < 79) {
                sendMessage(player, "You need a Crafting level of at least 79 to do this.")
                return@onUseWith true
            }

            val count = amountInInventory(player, headdress.base)
            if (count < 20) {
                sendMessage(player, "You need at least 20 ${getItemName(headdress.base).lowercase()} to craft this.")
                return@onUseWith true
            }

            if (removeItem(player, Item(headdress.base, 20))) {
                addItem(player, headdress.product, 1)
                rewardXP(player, Skills.CRAFTING, 50.0)
                sendMessage(player, "You add the feathers to the coif to make a feathered headdress.")
            }

            return@onUseWith true
        }
    }
}


/**
 * Represents a type of feather headdress.
 */
private enum class FeatherHeaddress(val base: Int, val product: Int) {

    FEATHER_HEADDRESS_BLUE(Items.BLUE_FEATHER_10089, Items.FEATHER_HEADDRESS_12210),
    FEATHER_HEADDRESS_ORANGE(Items.ORANGE_FEATHER_10091, Items.FEATHER_HEADDRESS_12222),
    FEATHER_HEADDRESS_RED(Items.RED_FEATHER_10088, Items.FEATHER_HEADDRESS_12216),
    FEATHER_HEADDRESS_STRIPY(Items.STRIPY_FEATHER_10087, Items.FEATHER_HEADDRESS_12219),
    FEATHER_HEADDRESS_YELLOW(Items.YELLOW_FEATHER_10090, Items.FEATHER_HEADDRESS_12213);

    companion object {
        /**
         * Map for fast lookup of feather headdress by base feather ID.
         */
        private val baseToHeaddressMap = values().associateBy { it.base }

        /**
         * Gets the corresponding [FeatherHeaddress] for a given feather item ID.
         *
         * @param baseId the feather item ID
         * @return the matching [FeatherHeaddress] or null if none exists
         */
        fun forBase(baseId: Int): FeatherHeaddress? = baseToHeaddressMap[baseId]
    }
}
