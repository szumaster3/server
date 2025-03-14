package core.game.world

import core.ServerConstants
import org.json.simple.JSONObject
import java.io.FileInputStream
import java.io.IOException
import java.util.*

class GameSettings internal constructor(
    var name: String,
    var isBeta: Boolean,
    var isDevMode: Boolean,
    var isGui: Boolean,
    var worldId: Int,
    var countryIndex: Int,
    var activity: String,
    var isMembers: Boolean,
    var isPvp: Boolean,
    var isQuickChat: Boolean,
    var isLootshare: Boolean,
    var msAddress: String,
    var default_xp_rate: Double,
    var enable_default_clan: Boolean,
    var enable_bots: Boolean,
    var autostock_ge: Boolean,
    var allow_token_purchase: Boolean,
    var increased_door_time: Boolean,
    var enabled_botting: Boolean,
    var max_adv_bots: Int,
    var enable_doubling_money_scammers: Boolean,
    var wild_pvp_enabled: Boolean,
    var jad_practice_enabled: Boolean,
    var ge_announcement_limit: Int,
    var smartpathfinder_bfs: Boolean,
    var enable_castle_wars: Boolean,
    var message_model: Int,
    var message_string: String,
) {
    val isHosted: Boolean
        get() = !isDevMode

    override fun toString(): String {
        return "GameSettings [name=$name, debug=$isBeta, devMode=$isDevMode, gui=$isGui, worldId=$worldId]"
    }

    companion object {
        fun parse(data: JSONObject): GameSettings {
            val name = ServerConstants.SERVER_NAME
            val debug = data["debug"] as Boolean
            val dev = data["dev"] as Boolean
            val startGui = data["startGui"] as Boolean
            val worldId = data["worldID"].toString().toInt()
            val countryId = data["countryID"].toString().toInt()
            val activity = data["activity"].toString()
            val pvpWorld = data["pvpWorld"] as Boolean
            val msip = data["msip"].toString()
            val default_xp_rate = data["default_xp_rate"].toString().toDouble()
            val enable_default_clan = data["enable_default_clan"] as Boolean
            val enable_bots = data["enable_bots"] as Boolean
            val autostock_ge = data["autostock_ge"] as Boolean
            val increased_door_time = data["increased_door_time"] as? Boolean ?: false
            val enable_botting = data["botting_enabled"] as? Boolean ?: false
            val max_adv_bots = data["max_adv_bots"].toString().toIntOrNull() ?: 100
            val enable_doubling_money_scammers = data["enable_doubling_money_scammers"] as? Boolean ?: false
            val wild_pvp_enabled = data["wild_pvp_enabled"] as? Boolean ?: true
            val jad_practice_enabled = data["jad_practice_enabled"] as? Boolean ?: true
            val ge_announcement_limit = data["ge_announcement_limit"].toString().toInt()
            val smartpathfinder_bfs = data["smartpathfinder_bfs"] as? Boolean ?: false
            val enable_castle_wars = data["enable_castle_wars"] as? Boolean ?: false
            val allow_token_purchase = data["allow_token_purchase"] as Boolean
            val message_of_the_week_identifier = data["message_of_the_week_identifier"].toString().toInt()
            val message_of_the_week_text = data["message_of_the_week_text"].toString()

            return GameSettings(
                name,
                debug,
                dev,
                startGui,
                worldId,
                countryId,
                activity,
                true,
                pvpWorld,
                false,
                false,
                msip,
                default_xp_rate,
                enable_default_clan,
                enable_bots,
                autostock_ge,
                allow_token_purchase,
                increased_door_time,
                enable_botting,
                max_adv_bots,
                enable_doubling_money_scammers,
                wild_pvp_enabled,
                jad_practice_enabled,
                ge_announcement_limit,
                smartpathfinder_bfs,
                enable_castle_wars,
                message_of_the_week_identifier,
                message_of_the_week_text,
            )
        }

        private fun getProperties(path: String): Properties {
            val properties = Properties()
            try {
                FileInputStream(path).use { file -> properties.load(file) }
            } catch (e: IOException) {
                println("Warning: Could not find file at path: $path")
                e.printStackTrace()
            }
            return properties
        }
    }
}
