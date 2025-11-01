package content.global.skill.runecrafting

import core.game.node.item.Item
import shared.consts.Items

/**
 * Represents a combination runes.
 */
enum class CombinationRune(
    val rune: Int,
    val level: Int,
    val experience: Double,
    val altars: Array<Altar>,
    vararg runes: Rune
) {
    MIST(Items.MIST_RUNE_4695, 6, 8.0, arrayOf(Altar.WATER, Altar.AIR), Rune.AIR, Rune.WATER),
    DUST(Items.DUST_RUNE_4696, 10, 8.3, arrayOf(Altar.EARTH, Altar.AIR), Rune.AIR, Rune.EARTH),
    MUD(Items.MUD_RUNE_4698, 13, 9.3, arrayOf(Altar.EARTH, Altar.WATER), Rune.WATER, Rune.EARTH),
    SMOKE(Items.SMOKE_RUNE_4697, 15, 8.5, arrayOf(Altar.FIRE, Altar.AIR), Rune.AIR, Rune.FIRE),
    STEAM(Items.STEAM_RUNE_4694, 19, 9.3, arrayOf(Altar.WATER, Altar.FIRE), Rune.WATER, Rune.FIRE),
    LAVA(Items.LAVA_RUNE_4699, 23, 10.0, arrayOf(Altar.FIRE, Altar.EARTH), Rune.EARTH, Rune.FIRE);

    /**
     * The base runes required to create this combination rune.
     */
    val runes: List<Rune> = runes.toList()

    /**
     * Gets the higher XP value granted.
     */
    val highExperience: Double
        get() = if (experience % 1 == 0.0) experience + 5 else experience + 8

    companion object {

        /**
         * Gets the matching [CombinationRune] for a given [altar] and [item].
         */
        fun forAltar(altar: Altar, item: Item): CombinationRune? {
            for (rune in values()) {
                for (alt in rune.altars) {
                    if (alt == altar) {
                        val altarElement = alt.name
                        val talismanElement = if (item.name.contains("talisman", ignoreCase = true)) {
                            Talisman.forItem(item)?.name
                        } else {
                            Rune.forItem(item)?.name
                        } ?: continue

                        if (altarElement == talismanElement) {
                            continue
                        }

                        if (rune.runes.any { it.name == talismanElement }) {
                            return rune
                        }
                    }
                }
            }
            return null
        }
    }
}