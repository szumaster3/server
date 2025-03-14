package core.game.system.config

import core.ServerConstants
import core.api.log
import core.game.node.entity.player.Player
import core.tools.Log
import org.json.simple.JSONArray
import org.json.simple.parser.JSONParser
import java.io.FileReader

class ShopParser {
    val parser = JSONParser()
    var reader: FileReader? = null

    companion object {
        fun openUid(
            player: Player,
            uid: Int,
        ): Boolean {
            return true
        }
    }

    fun load() {
        var count = 0
        reader = FileReader(ServerConstants.CONFIG_PATH + "shops.json")
        val configlist = parser.parse(reader) as JSONArray
        for (config in configlist) {
            count++
        }

        log(this::class.java, Log.FINE, "Parsed $count shops.")
    }
}
