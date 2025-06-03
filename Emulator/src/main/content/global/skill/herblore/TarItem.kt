package content.global.skill.herblore

import org.rs.consts.Items

/**
 * Enum representing different types of herb tars used in Herblore.
 */
enum class TarItem(val ingredient: Int, val level: Int, val experience: Double, val product: Int, ) {
    GUAM_TAR(HerbItem.GUAM.product.id, 19, 30.0, Items.GUAM_TAR_10142),
    GROUND_GUAM_TAR(Items.GROUND_GUAM_6681, 19, 30.0, Items.GUAM_TAR_10142),
    MARRENTILL_TAR(HerbItem.MARRENTILL.product.id, 31, 42.5, Items.MARRENTILL_TAR_10143),
    TARROMIN_TAR(HerbItem.TARROMIN.product.id, 39, 55.0, Items.TARROMIN_TAR_10144),
    HARRALANDER_TAR(HerbItem.HARRALANDER.product.id, 44, 72.5, Items.HARRALANDER_TAR_10145),
    ;

    companion object {
        private val mapByIngredient = values().associateBy { it.ingredient }

        /**
         * Finds a [TarItem] by its ingredient ID.
         *
         * @param id The ingredient item ID to look up.
         * @return The corresponding [TarItem], or null if no match is found.
         */
        fun forId(id: Int): TarItem? = mapByIngredient[id]
    }
}
