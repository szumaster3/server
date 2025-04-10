package content.global.skill.runecrafting

import core.game.node.entity.combat.spell.Runes
import core.game.node.item.Item

/**
 * Represents a Rune in the RuneCrafting skill.
 *
 * Each rune has an associated [Item], a required level for use, experience gained from crafting,
 * and a set of multiples for crafting experience based on specific crafting actions.
 *
 * @property rune The [Item] representing the rune.
 * @property level The level required to craft or use the rune.
 * @property experience The base experience gained from crafting the rune.
 * @property multiple Additional multiples of experience gained based on crafting actions.
 */
enum class Rune(
    val rune: Item,
    val level: Int,
    val experience: Double,
    private vararg val multiple: Int,
) {
    /**
     * Air rune, requiring level 1 and providing 5 experience.
     */
    AIR(Runes.AIR_RUNE.transform(), 1, 5.0, 1, 11, 22, 33, 44, 55, 66, 77, 88, 99, 110),

    /**
     * Mind rune, requiring level 2 and providing 5.5 experience.
     */
    MIND(Runes.MIND_RUNE.transform(), 2, 5.5, 1, 14, 28, 42, 56, 70, 84, 98, 112),

    /**
     * Water rune, requiring level 5 and providing 6 experience.
     */
    WATER(Runes.WATER_RUNE.transform(), 5, 6.0, 1, 19, 38, 57, 76, 95, 114),

    /**
     * Earth rune, requiring level 9 and providing 6.5 experience.
     */
    EARTH(Runes.EARTH_RUNE.transform(), 9, 6.5, 1, 26, 52, 78, 104),

    /**
     * Fire rune, requiring level 14 and providing 7 experience.
     */
    FIRE(Runes.FIRE_RUNE.transform(), 14, 7.0, 1, 35, 70, 105),

    /**
     * Body rune, requiring level 20 and providing 7.5 experience.
     */
    BODY(Runes.BODY_RUNE.transform(), 20, 7.5, 1, 46, 92, 138),

    /**
     * Cosmic rune, requiring level 27 and providing 8 experience.
     */
    COSMIC(Runes.COSMIC_RUNE.transform(), 27, 8.0, 1, 59, 118),

    /**
     * Chaos rune, requiring level 35 and providing 8.5 experience.
     */
    CHAOS(Runes.CHAOS_RUNE.transform(), 35, 8.5, 1, 74, 148),

    /**
     * Astral rune, requiring level 40 and providing 8.7 experience.
     */
    ASTRAL(Runes.ASTRAL_RUNE.transform(), 40, 8.7, 1, 82, 164),

    /**
     * Nature rune, requiring level 44 and providing 9 experience.
     */
    NATURE(Runes.NATURE_RUNE.transform(), 44, 9.0, 1, 91, 182),

    /**
     * Law rune, requiring level 54 and providing 9.5 experience.
     */
    LAW(Runes.LAW_RUNE.transform(), 54, 9.5, 1, 110),

    /**
     * Death rune, requiring level 65 and providing 10 experience.
     */
    DEATH(Runes.DEATH_RUNE.transform(), 65, 10.0, 1, 131),

    /**
     * Blood rune, requiring level 77 and providing 10.5 experience.
     */
    BLOOD(Runes.BLOOD_RUNE.transform(), 77, 10.5, 1, 154),

    /**
     * Soul rune, requiring level 90 and providing 11 experience.
     */
    SOUL(Runes.SOUL_RUNE.transform(), 90, 11.0);

    /**
     * Retrieves the multiples of experience for this rune.
     *
     * @return An array of integers representing experience multiples, or `null` if there are no multiples.
     */
    fun getMultiple(): IntArray? = multiple

    /**
     * Determines if the rune is considered "normal" (i.e., part of the base set of runes).
     *
     * @return `true` if the rune is one of the basic runes (AIR, MIND, WATER, EARTH, FIRE, BODY), otherwise `false`.
     */
    val isNormal: Boolean
        get() = this == AIR || this == MIND || this == WATER || this == EARTH || this == FIRE || this == BODY

    /**
     * Determines if the rune has experience multiples.
     *
     * @return `true` if the rune has multiples, otherwise `false`.
     */
    fun isMultiple(): Boolean = getMultiple() != null

    companion object {

        /**
         * A map to look up the corresponding [Rune] by [Item] id.
         */
        private val itemToRune = HashMap<Int, Rune>()

        init {
            for (rune in values()) {
                itemToRune[rune.rune.id] = rune
            }
        }

        /**
         * Retrieves the corresponding [Rune] for the given [Item].
         *
         * @param item The [Item] to search for.
         * @return The matching [Rune], or `null` if no match is found.
         */
        @JvmStatic
        fun forItem(item: Item): Rune? = itemToRune[item.id]

        /**
         * Retrieves the corresponding [Rune] by its name.
         *
         * @param name The name of the rune.
         * @return The matching [Rune], or `null` if no match is found.
         */
        fun forName(name: String): Rune? = values().find { it.name == name }
    }
}
