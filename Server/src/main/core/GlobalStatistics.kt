package core

import com.google.gson.JsonObject
import core.ServerStore.Companion.getInt

object GlobalStatistics {
    @JvmStatic
    fun incrementDeathCount() {
        val archive = getDailyDeathArchive()
        archive.addProperty("players", archive.getInt("players") + 1)
    }

    @JvmStatic
    fun incrementDailyCowDeaths() {
        val archive = getDailyDeathArchive()
        archive.addProperty("lumbridge-cows", archive.getInt("lumbridge-cows") + 1)
    }

    @JvmStatic
    fun incrementGuardPickpockets() {
        val archive = getGuardPickpocketArchive()
        archive.addProperty("count", archive.getInt("count") + 1)
    }

    @JvmStatic
    fun incrementStealCakes() {
        val archive = getBakersStallArchive()
        archive.addProperty("amount", archive.getInt("amount") + 1)
    }

    @JvmStatic
    fun getDailyGuardPickpockets(): Int = getGuardPickpocketArchive().getInt("count")

    @JvmStatic
    fun getDailyBakersStallThefts(): Int = getBakersStallArchive().getInt("amount")

    @JvmStatic
    fun getDailyDeaths(): Int = getDailyDeathArchive().getInt("players")

    @JvmStatic
    fun getDailyCowDeaths(): Int = getDailyDeathArchive().getInt("lumbridge-cows")

    @JvmStatic
    private fun getDailyDeathArchive(): JsonObject = ServerStore.getArchive("daily-deaths-global")

    @JvmStatic
    private fun getGuardPickpocketArchive(): JsonObject = ServerStore.getArchive("daily-guard-pickpockets")

    @JvmStatic
    private fun getBakersStallArchive(): JsonObject = ServerStore.getArchive("daily-thieving-bakers-stall")
}