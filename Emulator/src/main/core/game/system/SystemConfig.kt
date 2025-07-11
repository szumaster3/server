package core.game.system

import core.game.node.entity.player.Player
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Stores and manages system configuration settings.
 */
class SystemConfig {
    private val configs: MutableMap<String, Any?> = HashMap()
    val betaUsers: List<String> = ArrayList(20)

    /**
     * Parses config data
     */
    fun parse() {}

    private fun parseConfig(key: String, value: String?, dataType: String?) {
        if (dataType == null) {
            configs[key] = value
            return
        }
        if (value.isNullOrEmpty()) return

        when (dataType) {
            "int" -> configs[key] = value.toIntOrNull()
            "double" -> {} // not implemented
            "boolean" -> {} // not implemented
            "date" -> {
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                configs[key] = try {
                    format.parse(value)
                } catch (e: ParseException) {
                    e.printStackTrace()
                    null
                }
            }
            else -> configs[key] = value
        }
    }

    /**
     * Checks if player login is valid (always true).
     */
    fun validLogin(player: Player?): Boolean = true

    /**
     * Check if double experience event is active.
     */
    val isDoubleExp: Boolean
        get() {
            val date = getConfig<Date?>("dxp", null) ?: return false
            return date.after(Date())
        }

    /**
     * Splits a string by [regex], returning a list.
     */
    fun split(data: String, regex: String): List<String> {
        if (!data.contains(regex)) return listOf(data)
        return data.trim().split(regex.toRegex()).filter { it.isNotEmpty() }
    }

    /**
     * Checks if a username is in beta users list.
     */
    fun isBetaUser(name: String): Boolean = betaUsers.contains(name)

    /**
     * Gets config value by [key], or null if missing.
     */
    fun <T> getConfig(key: String): T? = configs[key] as? T

    /**
     * Gets config value by [string], or returns [fail] if missing.
     */
    fun <T> getConfig(string: String, fail: T): T = configs[string] as? T ?: fail

    /**
     * Gets all config key-value pairs.
     */
    fun getConfigs(): Map<String, Any?> = configs
}
