package core.game.node.entity.combat.spell

import org.rs.consts.Items

enum class CombinationRune(
    var id: Int,
    vararg var types: Runes,
) {
    LAVA_RUNE(Items.LAVA_RUNE_4699, Runes.FIRE_RUNE, Runes.EARTH_RUNE),
    STEAM_RUNE(Items.STEAM_RUNE_4694, Runes.FIRE_RUNE, Runes.WATER_RUNE),
    MIST_RUNE(Items.MIST_RUNE_4695, Runes.WATER_RUNE, Runes.AIR_RUNE),
    DUST_RUNE(Items.DUST_RUNE_4696, Runes.AIR_RUNE, Runes.EARTH_RUNE),
    SMOKE_RUNE(Items.SMOKE_RUNE_4697, Runes.FIRE_RUNE, Runes.AIR_RUNE),
    MUD_RUNE(Items.MUD_RUNE_4698, Runes.EARTH_RUNE, Runes.WATER_RUNE),
    ELEMENTAL_RUNE(Items.ELEMENTAL_RUNE_12850, Runes.AIR_RUNE, Runes.WATER_RUNE, Runes.EARTH_RUNE, Runes.FIRE_RUNE),
    CATALYTIC_RUNE(
        Items.CATALYTIC_RUNE_12851,
        Runes.MIND_RUNE,
        Runes.CHAOS_RUNE,
        Runes.DEATH_RUNE,
        Runes.BLOOD_RUNE,
        Runes.SOUL_RUNE,
        Runes.ASTRAL_RUNE,
    ),
    ;

    companion object {
        @JvmStatic
        fun eligibleFor(rune: Runes): List<CombinationRune> {
            val runes: MutableList<CombinationRune> = ArrayList(20)
            for (r in values()) {
                for (ru in r.types) {
                    if (ru == rune) {
                        runes.add(r)
                    }
                }
            }
            return runes
        }
    }
}
