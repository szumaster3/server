package core.game.system.config

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import core.ServerConstants
import core.api.log
import core.game.node.entity.npc.NPC
import core.game.world.map.Direction
import core.game.world.map.Location
import core.tools.Log
import java.io.File
import java.io.FileReader

class NPCSpawner {
    private val gson = Gson()

    /*fun load() {
        var count = 0
        val spawnsDir = File(ServerConstants.CONFIG_PATH + "npc_spawns/")

        if (!spawnsDir.exists() || !spawnsDir.isDirectory) {
            log(this::class.java, Log.WARN, "No region spawn directory found at ${spawnsDir.path}")
            return
        }

        spawnsDir.listFiles { file -> file.extension.equals("json", ignoreCase = true) }?.forEach { file ->
            FileReader(file).use { reader ->
                val configs = gson.fromJson(reader, JsonArray::class.java)
                for (configElement in configs) {
                    val e = configElement.asJsonObject
                    val locData = e.get("loc_data").asString
                    val data = locData.split("-").map { it.trim() }.filter { it.isNotEmpty() }
                    val id = e.get("npc_id").asInt

                    for (d in data) {
                        val cleaned = d.removePrefix("{").removeSuffix("}")
                        val tokens = cleaned.split(",").map { it.trim() }
                        if (tokens.size < 5) continue

                        val npc = NPC.create(
                            id,
                            Location.create(tokens[0].toInt(), tokens[1].toInt(), tokens[2].toInt())
                        )
                        npc.isWalks = tokens[3] == "1"
                        npc.direction = Direction.values()[tokens[4].toInt()]
                        npc.setAttribute("spawned:npc", true)

                        if (npc.definition.getConfiguration("facing_booth", false)) {
                            npc.setAttribute("facing_booth", true)
                        }
                        npc.init()
                        count++
                    }
                }
            }
        }

        log(this::class.java, Log.DEBUG, "Spawned $count NPC spawns from ${spawnsDir.listFiles()?.size ?: 0} region files.")
    }
    */

    fun load() {
        var count = 0
        FileReader(ServerConstants.CONFIG_PATH + "npc_spawns.json").use { reader ->
            val configs = gson.fromJson(reader, JsonArray::class.java)
            for (configElement: JsonElement in configs) {
                val e = configElement.asJsonObject
                val locData = e.get("loc_data").asString
                val data = locData.split("-").map { it.trim() }.filter { it.isNotEmpty() }
                val id = e.get("npc_id").asInt

                for (d in data) {
                    val cleaned = d.removePrefix("{").removeSuffix("}")
                    val tokens = cleaned.split(",").map { it.trim() }

                    if (tokens.size < 5) {
                        continue
                    }

                    val npc = NPC.create(
                        id,
                        Location.create(
                            tokens[0].toInt(),
                            tokens[1].toInt(),
                            tokens[2].toInt()
                        )
                    )
                    npc.isWalks = tokens[3] == "1"
                    npc.direction = Direction.values()[tokens[4].toInt()]
                    npc.setAttribute("spawned:npc", true)

                    if (npc.definition.getConfiguration("facing_booth", false)) {
                        npc.setAttribute("facing_booth", true)
                    }
                    npc.init()
                    count++
                }
            }
        }

        log(this::class.java, Log.DEBUG, "Spawned $count NPC spawns.")
    }
}
