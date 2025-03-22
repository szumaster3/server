package content.global.skill.runecrafting

import core.game.node.entity.combat.spell.Runes
import core.game.node.item.Item

enum class Rune(
    val rune: Item,
    val level: Int,
    val experience: Double,
    private vararg val multiple: Int,
) {
    AIR(Runes.AIR_RUNE.transform(), 1, 5.0, 1, 11, 22, 33, 44, 55, 66, 77, 88, 99, 110),
    MIND(Runes.MIND_RUNE.transform(), 2, 5.5, 1, 14, 28, 42, 56, 70, 84, 98, 112),
    WATER(Runes.WATER_RUNE.transform(), 5, 6.0, 1, 19, 38, 57, 76, 95, 114),
    EARTH(Runes.EARTH_RUNE.transform(), 9, 6.5, 1, 26, 52, 78, 104),
    FIRE(Runes.FIRE_RUNE.transform(), 14, 7.0, 1, 35, 70, 105),
    BODY(Runes.BODY_RUNE.transform(), 20, 7.5, 1, 46, 92, 138),
    COSMIC(Runes.COSMIC_RUNE.transform(), 27, 8.0, 1, 59, 118),
    CHAOS(Runes.CHAOS_RUNE.transform(), 35, 8.5, 1, 74, 148),
    ASTRAL(Runes.ASTRAL_RUNE.transform(), 40, 8.7, 1, 82, 164),
    NATURE(Runes.NATURE_RUNE.transform(), 44, 9.0, 1, 91, 182),
    LAW(Runes.LAW_RUNE.transform(), 54, 9.5, 1, 110),
    DEATH(Runes.DEATH_RUNE.transform(), 65, 10.0, 1, 131),
    ARMADYL(Runes.ARMADYL_RUNE.transform(), 72, 10.0, 7, 8, 9, 10),
    BLOOD(Runes.BLOOD_RUNE.transform(), 77, 10.5, 1, 154),
    SOUL(Runes.SOUL_RUNE.transform(), 90, 11.0),
    ;

    fun getMultiple(): IntArray? = multiple

    val isNormal: Boolean
        get() = this == AIR || this == MIND || this == WATER || this == EARTH || this == FIRE || this == BODY

    fun isMultiple(): Boolean = getMultiple() != null

    companion object {
        private val itemToRune = HashMap<Int, Rune>()

        init {
            for (rune in values()) {
                itemToRune[rune.rune.id] = rune
            }
        }

        @JvmStatic
        fun forItem(item: Item): Rune? = itemToRune[item.id]

        fun forName(name: String): Rune? = values().find { it.name == name }
    }
}
