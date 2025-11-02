package core.game.system.command.sets

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import core.api.log
import core.api.sendMessage
import core.cache.Cache
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.command.CommandPlugin
import core.game.system.command.Privilege
import core.plugin.Initializable
import core.tools.Log
import java.io.File

@Initializable
class SpawnCommandSet : CommandSet(Privilege.ADMIN) {

    companion object {
        fun readJsonArrayFromFile(filePath: String, gson: Gson): JsonArray {
            val file = File(filePath)
            return if (file.exists()) {
                gson.fromJson(file.readText(), JsonArray::class.java) ?: JsonArray()
            } else {
                JsonArray()
            }
        }

        fun writeJsonArrayToFile(filePath: String, jsonArray: JsonArray, gson: Gson) {
            val file = File(filePath)
            file.parentFile.mkdirs()
            file.writeText(gson.toJson(jsonArray))
        }
    }
    override fun defineCommands() {
        val npcLocations = mutableMapOf<Int, MutableList<String>>()

        define(name = "npc", privilege = Privilege.ADMIN, usage = "Spawns a npc with the given ID") { player, args ->
            if (args.size < 2) {
                reject(player, "syntax error: id (optional) direction")
                return@define
            }

            val npcId = CommandPlugin.toInteger(args[1])

            val npc =
                NPC.create(npcId, player.location).apply {
                    setAttribute("spawned:npc", true)
                    isRespawn = false
                    direction = player.direction
                    init()
                    isWalks = args.size > 2
                }

            val locData = "{${npc.location.x},${npc.location.y},${npc.location.z},${if (npc.isWalks) "1" else "0"},${npc.direction.ordinal}}-"

            npcLocations.getOrPut(npcId) { mutableListOf() }.let { locations ->
                if (!locations.contains(locData)) {
                    locations.add(locData)
                }
            }

            val filePath = "data/logs/npc_spawns.json"
            val gson: Gson = GsonBuilder().setPrettyPrinting().create()

            try {
                val npcLogEntry =
                    JsonObject().apply {
                        addProperty("npc_id", npcId.toString())
                        addProperty("loc_data", npcLocations[npcId]?.joinToString("") { it })
                    }

                val existingData = readJsonArrayFromFile(filePath, gson)
                existingData.add(npcLogEntry)
                writeJsonArrayToFile(filePath, existingData, gson)

                sendMessage(player, "NPC data saved to [${core.tools.DARK_RED}$filePath</col>].")
            } catch (e: Exception) {
                reject(player, "An error occurred while logging NPC data: ${e.message}")
            }
        }

        define(name = "item") { player, args ->
            if (args.size < 2) {
                reject(player, "You must specify an item ID")
                return@define
            }
            val id = args[1].toIntOrNull() ?: return@define
            var amount = (args.getOrNull(2) ?: "1").toInt()
            if (id > Cache.getItemDefinitionsSize()) {
                reject(player, "Item ID '$id' out of range.")
                return@define
            }
            val item = Item(id, amount)
            val max = player.inventory.getMaximumAdd(item)
            if (amount > max) {
                amount = max
            }
            item.setAmount(amount)
            player.inventory.add(item)
        }

        define(
            name = "object",
            privilege = Privilege.MODERATOR,
            usage = "Spawn object with given ID at the player's location",
        ) { player, args ->
            if (args.size < 2) {
                reject(player, "syntax error: id (optional) type rotation or rotation")
                return@define
            }
            val scenery =
                if (args.size > 3) {
                    Scenery(
                        CommandPlugin.toInteger(
                            args[1],
                        ),
                        player.location,
                        CommandPlugin.toInteger(args[2]),
                        CommandPlugin.toInteger(args[3]),
                    )
                } else if (args.size == 3) {
                    Scenery(
                        CommandPlugin.toInteger(args[1]),
                        player.location,
                        CommandPlugin.toInteger(args[2]),
                    )
                } else {
                    Scenery(CommandPlugin.toInteger(args[1]), player.location)
                }
            SceneryBuilder.add(scenery)
            log(this::class.java, Log.FINE, "object = $scenery")
        }

        define(
            name = "objectgrid",
            privilege = Privilege.MODERATOR,
            usage = "::objectgrid",
            description = "Defines an object grid",
        ) { player, args ->
            if (args.size != 5) {
                reject(player, "Usage: objectgrid beginId endId type rotation")
                return@define
            }
            val beginId = args[1].toIntOrNull() ?: return@define
            val endId = args[2].toIntOrNull() ?: return@define
            val type = args[3].toIntOrNull() ?: return@define
            val rotation = args[4].toIntOrNull() ?: return@define
            for (i in 0..10) {
                SceneryBuilder.add(
                    Scenery(
                        29447 + i,
                        player.location.transform(i, -1, 0),
                    ),
                )
            }
            for (i in beginId..endId) {
                val j = i - beginId
                val scenery =
                    Scenery(
                        i,
                        player.location.transform(j % 10, j / 10, 0),
                        type,
                        rotation,
                    )
                SceneryBuilder.add(scenery)
                if (j % 10 == 0) {
                    SceneryBuilder.add(
                        Scenery(
                            29447 + (j / 10) % 10,
                            player.location.transform(-1, j / 10, 0),
                        ),
                    )
                }
            }
        }
    }
}
