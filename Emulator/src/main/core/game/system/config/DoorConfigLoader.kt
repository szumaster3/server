package core.game.system.config

import core.ServerConstants
import core.api.log
import core.tools.Log
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader

class DoorConfigLoader {
    companion object {
        @JvmStatic val DOORS = hashMapOf<Int, Door>()

        @JvmStatic fun forId(id: Int): Door? {
            return DOORS[id]
        }
    }

    val parser = JSONParser()
    var reader: FileReader? = null

    fun load() {
        var count = 0
        reader = FileReader(ServerConstants.CONFIG_PATH + "door_configs.json")
        var configs = parser.parse(reader) as JSONArray
        for (config in configs) {
            val e = config as JSONObject
            val door = Door(e["id"].toString().toInt())
            door.replaceId = e["replaceId"].toString().toInt()
            door.isFence = e["fence"].toString().toBoolean()
            door.isMetal = e["metal"].toString().toBoolean()
            door.isAutoWalk = e["autowalk"]?.toString()?.toBoolean() ?: false
            door.questRequirement = e["questRequirement"]?.toString() ?: ""
            DOORS[door.id] = door
            val replacedDoor = Door(door.replaceId)
            replacedDoor.replaceId = door.id
            replacedDoor.isFence = door.isFence
            replacedDoor.isMetal = door.isMetal
            replacedDoor.isAutoWalk = door.isAutoWalk
            replacedDoor.questRequirement = door.questRequirement
            DOORS[door.replaceId] = replacedDoor
            count++
        }

        log(this::class.java, Log.FINE, "Parsed $count door configs.")
    }

    class Door(
        val id: Int,
    ) {
        var replaceId = 0
        var isFence = false
        var isAutoWalk = false
        var isMetal = false
        var questRequirement = ""
    }
}
