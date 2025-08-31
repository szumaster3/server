package core.game.system.config

import com.google.gson.Gson
import com.google.gson.JsonArray
import core.ServerConstants
import core.api.log
import core.game.component.ComponentDefinition
import core.tools.Log
import java.io.FileReader

class InterfaceConfigParser {
    private val gson = Gson()

    fun load() {
        var count = 0
        FileReader(ServerConstants.CONFIG_PATH + "interface_configs.json").use { reader ->
            val configList = gson.fromJson(reader, JsonArray::class.java)
            for (element in configList) {
                val e = element.asJsonObject
                val id = e.get("id").asInt

                val interfaceType = e.get("interfaceType").asString
                val walkable = e.get("walkable").asString
                val tabIndex = e.get("tabIndex").asString

                if (ComponentDefinition.definitions.containsKey(id)) {
                    ComponentDefinition.definitions[id]!!.parse(interfaceType, walkable, tabIndex)
                }

                ComponentDefinition.put(
                    id,
                    ComponentDefinition().parse(interfaceType, walkable, tabIndex)
                )

                count++
            }
        }
        log(this::class.java, Log.DEBUG, "Parsed $count interface configs.")
    }
}
