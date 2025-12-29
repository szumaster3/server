package custom.guild.warehouse

import kotlin.math.pow

object GuildWarehouseLevel {

    fun capacityForLevel(level: Int): Int {
        if (level <= 0) return 0
        return 25 + ((level - 1) * 25)
    }

    fun goldCostForLevel(level: Int): Long {
        return when(level) {
            1 -> 1_500_000
            2 -> 3_000_000
            3 -> 6_000_000
            4 -> 12_000_000
            else -> (12_000_000L * 2.0.pow((level - 4).toDouble())).toLong()
        }
    }
}
