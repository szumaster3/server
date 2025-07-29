package core.game.system.config

import core.ServerConstants
import core.api.log
import core.tools.Log
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader

class XteaParser {
    companion object {
        val REGION_XTEA = HashMap<Int, IntArray>()
        private val DEFAULT_REGION_KEYS = intArrayOf(0, 0, 0, 0)

        fun getRegionXTEA(regionId: Int): IntArray = REGION_XTEA.getOrDefault(regionId, DEFAULT_REGION_KEYS)
    }

    val parser = JSONParser()
    var reader: FileReader? = null

    fun load() {
        var count = 0
        reader = FileReader(ServerConstants.CONFIG_PATH + "xteas.json")
        val obj = parser.parse(reader) as JSONObject
        val configList = obj["xteas"] as JSONArray
        for (config in configList) {
            val e = config as JSONObject
            val id = e["regionId"].toString().toInt()
            val keys = e["keys"].toString().split(",").map(String::toInt)
            REGION_XTEA[id] = intArrayOf(keys[0], keys[1], keys[2], keys[3])
            count++
        }

        log(this::class.java, Log.DEBUG, "Parsed $count region keys.")
    }
}
