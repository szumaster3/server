package core.game.system

import core.game.node.entity.player.Player
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SystemConfig {
    private val configs: MutableMap<String, Any?> = HashMap()
    val betaUsers: List<String> = ArrayList(20)

    fun parse() {}

    private fun parseConfig(
        key: String,
        value: String?,
        dataType: String?,
    ) {
        if (dataType == null) {
            configs[key] = value
            return
        }
        if (value == null || value === "") {
            return
        }
        when (dataType) {
            "int" -> configs[key] = Integer.valueOf(value)
            "double" -> {}
            "boolean" -> {}
            "date" -> {
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                var parsed: Date? = null
                try {
                    parsed = format.parse(value)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                configs[key] = parsed
            }

            else -> configs[key] = value
        }
    }

    fun validLogin(player: Player?): Boolean {
        return true
    }

    val isDoubleExp: Boolean

        get() {
            val date = getConfig<Date?>("dxp", null) ?: return false
            return date.after(Date())
        }

    fun split(
        data: String,
        regex: String,
    ): List<String> {
        if (!data.contains(regex)) {
            val split: MutableList<String> = ArrayList(20)
            split.add(data)
            return split
        }
        val split: MutableList<String> = ArrayList(20)
        val tokens =
            data
                .trim { it <= ' ' }
                .split(regex.toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
        for (s in tokens) {
            split.add(s)
        }
        return split
    }

    fun isBetaUser(name: String): Boolean {
        return betaUsers.contains(name)
    }

    fun <T> getConfig(key: String): T? {
        return if (!configs.containsKey(key)) {
            null
        } else {
            configs[key] as T?
        }
    }

    fun <T> getConfig(
        string: String,
        fail: T,
    ): T {
        val `object` = configs[string]
        return if (`object` != null) {
            `object` as T
        } else {
            fail
        }
    }

    fun getConfigs(): Map<String, Any?> {
        return configs
    }
}
