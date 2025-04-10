package content.global.skill.runecrafting

import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Represents the various elemental tiaras used in Runecrafting.
 *
 * @property item The [Item] instance representing the tiara.
 * @property experience The amount of bonus experience awarded when using the tiara during Runecrafting.
 */
enum class Tiara(
    val item: Item,
    val experience: Double,
) {
    AIR(Item(Items.AIR_TIARA_5527), 25.0),
    MIND(Item(Items.MIND_TIARA_5529), 27.5),
    WATER(Item(Items.WATER_TIARA_5531), 30.0),
    EARTH(Item(Items.EARTH_TIARA_5535), 32.5),
    FIRE(Item(Items.FIRE_TIARA_5537), 35.0),
    BODY(Item(Items.BODY_TIARA_5533), 37.5),
    COSMIC(Item(Items.COSMIC_TIARA_5539), 40.0),
    CHAOS(Item(Items.CHAOS_TIARA_5543), 43.5),
    NATURE(Item(Items.NATURE_TIARA_5541), 45.0),
    LAW(Item(Items.LAW_TIARA_5545), 47.5),
    DEATH(Item(Items.DEATH_TIARA_5547), 50.0),
    BLOOD(Item(Items.BLOOD_TIARA_5549), 52.5),
    ;

    /**
     * Gets the corresponding [Talisman] for this tiara, if one exists.
     *
     * The name of the tiara enum constant is matched against the names of the [Talisman] enum.
     */
    val talisman: Talisman?
        get() = Talisman.values().find { it.name == name }

    companion object {
        private val itemToTiara = HashMap<Int, Tiara>()

        init {
            for (tiara in values()) {
                itemToTiara[tiara.item.id] = tiara
            }
        }

        /**
         * Retrieves the [Tiara] enum constant for the given [Item].
         *
         * @param item The item to check.
         * @return The matching [Tiara], or `null` if no match is found.
         */
        @JvmStatic
        fun from(item: Item): Tiara? = itemToTiara[item.id]

        /**
         * Alternative method to retrieve the [Tiara] enum constant for the given [Item].
         *
         * This does the same as [from] and is kept for naming flexibility.
         *
         * @param item The item to check.
         * @return The matching [Tiara], or `null` if no match is found.
         */
        @JvmStatic
        fun forItem(item: Item): Tiara? = itemToTiara[item.id]
    }
}
