package content.global.skill.runecrafting

import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Represents a combination rune in Runecrafting, which is a rune that combines two basic elements.
 *
 * @property rune The resulting rune item.
 * @property level The Runecrafting level required to craft the combination rune.
 * @property experience The base experience granted when crafting the rune.
 * @property altars The altars at which the combination rune can be crafted.
 * @constructor Creates a new combination rune with its requirements.
 */
enum class CombinationRune(
    val rune: Item,
    val level: Int,
    val experience: Double,
    val altars: Array<Altar>,
    vararg runes: Rune
) {

    /**
     * Combination of Air and Water runes.
     */
    MIST(Item(Items.MIST_RUNE_4695), 6, 8.0, arrayOf(Altar.WATER, Altar.AIR), Rune.AIR, Rune.WATER),

    /**
     * Combination of Air and Earth runes.
     */
    DUST(Item(Items.DUST_RUNE_4696), 10, 8.3, arrayOf(Altar.EARTH, Altar.AIR), Rune.AIR, Rune.EARTH),

    /**
     * Combination of Water and Earth runes.
     */
    MUD(Item(Items.MUD_RUNE_4698), 13, 9.3, arrayOf(Altar.EARTH, Altar.WATER), Rune.WATER, Rune.EARTH),

    /**
     * Combination of Air and Fire runes.
     */
    SMOKE(Item(Items.SMOKE_RUNE_4697), 15, 8.5, arrayOf(Altar.FIRE, Altar.AIR), Rune.AIR, Rune.FIRE),

    /**
     * Combination of Water and Fire runes.
     */
    STEAM(Item(Items.STEAM_RUNE_4694), 19, 9.3, arrayOf(Altar.WATER, Altar.FIRE), Rune.WATER, Rune.FIRE),

    /**
     * Combination of Earth and Fire runes.
     */
    LAVA(Item(Items.LAVA_RUNE_4699), 23, 10.0, arrayOf(Altar.FIRE, Altar.EARTH), Rune.EARTH, Rune.FIRE);

    /**
     * The base runes required to create this combination rune.
     */
    val runes: Array<Rune> = runes as Array<Rune>

    /**
     * Gets the higher XP value granted when crafting combination runes using binding necklaces or other methods.
     *
     * @return The boosted experience.
     */
    val highExperience: Double
        get() = if (experience % 1 == 0.0) experience + 5 else experience + 8

    companion object {

        /**
         * Retrieves the matching [CombinationRune] for a given [altar] and [item], typically a talisman or rune.
         *
         * @param altar The altar used to attempt crafting.
         * @param item The talisman or rune used in the combination.
         * @return The matching [CombinationRune], or null if no valid combination exists.
         */
        fun forAltar(altar: Altar, item: Item): CombinationRune? {
            for (rune in values()) {
                for (alt in rune.altars) {
                    if (alt == altar) {
                        val altarElement = alt.name
                        val talismanElement = if (item.name.contains("talisman"))
                            Talisman.forItem(item)!!.name
                        else
                            Rune.forItem(item)!!.name

                        if (altarElement == talismanElement) {
                            continue
                        }

                        for (r in rune.runes) {
                            if (r.name == talismanElement) {
                                return rune
                            }
                        }
                    }
                }
            }
            return null
        }
    }
}
