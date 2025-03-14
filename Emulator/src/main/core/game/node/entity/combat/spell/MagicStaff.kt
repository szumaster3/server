package core.game.node.entity.combat.spell

import org.rs.consts.Items

enum class MagicStaff(
    val runeId: Int,
    vararg val staves: Int,
) {
    FIRE_RUNE(
        Items.FIRE_RUNE_554,
        Items.STAFF_OF_FIRE_1387,
        Items.FIRE_BATTLESTAFF_1393,
        Items.MYSTIC_FIRE_STAFF_1401,
        Items.LAVA_BATTLESTAFF_3053,
        Items.MYSTIC_LAVA_STAFF_3054,
        Items.LAVA_BATTLESTAFF_3055,
        Items.MYSTIC_LAVA_STAFF_3056,
        Items.STEAM_BATTLESTAFF_11736,
        Items.MYSTIC_STEAM_STAFF_11738,
    ),
    WATER_RUNE(
        Items.WATER_RUNE_555,
        Items.STAFF_OF_WATER_1383,
        Items.WATER_BATTLESTAFF_1395,
        Items.MYSTIC_WATER_STAFF_1403,
        Items.MYSTIC_MUD_STAFF_6563,
        Items.STEAM_BATTLESTAFF_11736,
        Items.MYSTIC_STEAM_STAFF_11738,
        Items.MUD_BATTLESTAFF_6562,
    ),
    AIR_RUNE(Items.AIR_RUNE_556, Items.STAFF_OF_AIR_1381, Items.AIR_BATTLESTAFF_1397, Items.MYSTIC_AIR_STAFF_1405),
    EARTH_RUNE(
        Items.EARTH_RUNE_557,
        Items.LAVA_BATTLESTAFF_3053,
        Items.MYSTIC_LAVA_STAFF_3054,
        Items.LAVA_BATTLESTAFF_3055,
        Items.MYSTIC_LAVA_STAFF_3056,
        Items.STAFF_OF_EARTH_1385,
        Items.EARTH_BATTLESTAFF_1399,
        Items.MYSTIC_EARTH_STAFF_1407,
        Items.MYSTIC_MUD_STAFF_6563,
        Items.MUD_BATTLESTAFF_6562,
    ),
    ;

    companion object {
        private val MAGIC_STAVES: MutableMap<Int, MagicStaff> = HashMap()

        init {
            for (m in values()) {
                MAGIC_STAVES[m.runeId] = m
            }
        }

        @JvmStatic
        fun forId(runeId: Int): MagicStaff? {
            return MAGIC_STAVES[runeId]
        }
    }
}
