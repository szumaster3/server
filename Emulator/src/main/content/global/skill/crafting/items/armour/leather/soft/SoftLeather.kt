package content.global.skill.crafting.items.armour.leather.soft

import core.api.openInterface
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Components
import org.rs.consts.Items

/**
 * Represents the different types of soft leather armour.
 */
enum class SoftLeather(
    val button: Int,
    val level: Int,
    val experience: Double,
    val product: Item,
) {
    /**
     * Crafting Leather Body.
     */
    ARMOUR(
        button = 28,
        level = 14,
        experience = 25.0,
        product = Item(Items.LEATHER_BODY_1129),
    ),

    /**
     * Crafting Leather Gloves.
     */
    GLOVES(
        button = 29,
        level = 1,
        experience = 13.8,
        product = Item(Items.LEATHER_GLOVES_1059),
    ),

    /**
     * Crafting Leather Boots.
     */
    BOOTS(
        button = 30,
        level = 7,
        experience = 16.3,
        product = Item(Items.LEATHER_BOOTS_1061),
    ),

    /**
     * Crafting Leather Vambraces.
     */
    VAMBRACES(
        button = 31,
        level = 11,
        experience = 22.0,
        product = Item(Items.LEATHER_VAMBRACES_1063),
    ),

    /**
     * Crafting Leather Chaps.
     */
    CHAPS(
        button = 32,
        level = 18,
        experience = 27.0,
        product = Item(Items.LEATHER_CHAPS_1095),
    ),

    /**
     * Crafting Coif.
     */
    COIF(
        button = 33,
        level = 38,
        experience = 37.0,
        product = Item(Items.COIF_1169),
    ),

    /**
     * Crafting Leather Cowl.
     */
    COWL(
        button = 34,
        level = 9,
        experience = 18.5,
        product = Item(Items.LEATHER_COWL_1167),
    ),
    ;

    companion object {
        /**
         * Opens the soft leather crafting interface for the player.
         *
         * @param player The player who will receive the crafting interface.
         */
        @JvmStatic
        fun openCraftingInterface(player: Player) {
            openInterface(player, Components.LEATHER_CRAFTING_154)
        }

        /**
         * Returns the [SoftLeather] enum entry corresponding to the given button id.
         *
         * @param button The button id used for interaction.
         * @return The corresponding [SoftLeather] enum entry, or null if no match is found.
         */
        @JvmStatic
        fun forButton(button: Int): SoftLeather? = values().find { it.button == button }
    }
}
