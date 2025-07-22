package core

import core.api.PersistWorld
import core.api.getItemName
import core.api.log
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.tools.Log
import core.tools.SystemLogger.logShutdown
import core.tools.SystemLogger.logStartup
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import javax.script.ScriptEngineManager

/**
 * Handles server-wide data persistence using JSON files.
 * Implements [PersistWorld] for parsing and saving stored data.
 */
class ServerStore : PersistWorld {
    /**
     * Parses JSON files in the server store directory and loads them into memory.
     */
    override fun parse() {
        logStartup("Parsing server store...")
        val dir = File(ServerConstants.STORE_PATH!!)
        if (!dir.exists()) {
            dir.mkdirs()
            return
        }

        var parser: JSONParser
        var reader: FileReader

        dir.listFiles()?.forEach { storeFile ->
            val key = storeFile.nameWithoutExtension

            reader = FileReader(storeFile)
            parser = JSONParser()

            try {
                val data = parser.parse(reader) as JSONObject
                fileMap[key] = data
                counter++
            } catch (e: Exception) {
                log(this::class.java, Log.ERR, "Failed parsing ${storeFile.name} - stack trace below.")
                e.printStackTrace()
                return@forEach
            }
        }

        logStartup("Initialized $counter store files.")
    }

    /**
     * Saves all stored JSON data to disk, formatting it for readability.
     */
    override fun save() {
        logShutdown("Saving server store...")
        val dir = File(ServerConstants.DATA_PATH + File.separator + "serverstore")
        if (!dir.exists()) {
            dir.mkdirs()
            return
        }

        val manager = ScriptEngineManager()
        val scriptEngine = manager.getEngineByName("JavaScript")

        fileMap.forEach { (name, data) ->
            val path = dir.absolutePath + File.separator + name + ".json"

            scriptEngine.put("jsonString", data.toJSONString())
            scriptEngine.eval("result = JSON.stringify(JSON.parse(jsonString), null, 2)")
            val prettyPrintedJson = scriptEngine["result"] as String

            FileWriter(path).use {
                it.write(prettyPrintedJson)
                it.flush()
                it.close()
            }
        }
    }

    companion object {
        /**
         * Stores parsed JSON data mapped by file name.
         */
        val fileMap = HashMap<String, JSONObject>()

        /**
         * Counter for the number of successfully loaded store files.
         */
        var counter = 0

        /**
         * Retrieves a JSON archive by name, creating a new one if it does not exist.
         */
        @JvmStatic
        fun getArchive(name: String): JSONObject = fileMap.getOrPut(name) { JSONObject() }

        /**
         * Stores a JSON archive under a given name.
         */
        fun setArchive(name: String, data: JSONObject) {
            fileMap[name] = data
        }

        /**
         * Clears all stored daily entries.
         */
        fun clearDailyEntries() {
            fileMap.keys.filter { it.lowercase().contains("daily") }.forEach { fileMap[it]?.clear() }
        }

        /**
         * Clears all stored weekly entries.
         */
        fun clearWeeklyEntries() {
            fileMap.keys.filter { it.lowercase().contains("weekly") }.forEach { fileMap[it]?.clear() }
        }

        /**
         * Retrieves an integer value from a JSON object.
         */
        @JvmStatic
        fun JSONObject.getInt(key: String, default: Int = 0): Int =
            when (val value = this[key]) {
                is Long -> value.toInt()
                is Double -> value.toInt()
                is Float -> value.toInt()
                is Int -> value
                else -> default
            }

        /**
         * Retrieves a string value from a JSON object.
         */
        @JvmStatic
        fun JSONObject.getString(key: String, default: String = "nothing"): String =
            this[key] as? String ?: default

        /**
         * Retrieves a long value from a JSON object.
         */
        @JvmStatic
        fun JSONObject.getLong(key: String, default: Long = 0L): Long =
            this[key] as? Long ?: default

        /**
         * Retrieves a boolean value from a JSON object.
         */
        @JvmStatic
        fun JSONObject.getBoolean(key: String, default: Boolean = false): Boolean =
            this[key] as? Boolean ?: default

        /**
         * Converts a list of integers into a JSONArray.
         */
        fun List<Int>.toJSONArray(): JSONArray = JSONArray().apply { this@toJSONArray.forEach { add(it) } }

        /**
         * Retrieves a list of a given type from a JSON object.
         */
        inline fun <reified T> JSONObject.getList(key: String): List<T> =
            (this[key] as? JSONArray)?.mapNotNull { it as? T } ?: emptyList()

        /**
         * Adds an item to a JSON array stored under a key in a JSON object.
         */
        fun JSONObject.addToList(key: String, value: Any) {
            val array = this.getOrPut(key) { JSONArray() } as JSONArray
            array.add(value)
        }

        /**
         * Generates a filename for storing NPC item data.
         */
        fun NPCItemFilename(npc: Int, item: Int, period: String = "daily"): String {
            val itemName = getItemName(item).lowercase().replace(" ", "-")
            val npcName = NPC(npc).name.lowercase()
            return "$period-$npcName-$itemName"
        }

        /**
         * Retrieves the JSON archive for an NPC item memory.
         */
        fun NPCItemMemory(npc: Int, item: Int, period: String = "daily"): JSONObject =
            getArchive(NPCItemFilename(npc, item, period))

        /**
         * Determines the available stock of an NPC item for a player.
         */
        fun getNPCItemStock(npc: Int, item: Int, limit: Int, player: Player, period: String = "daily"): Int {
            val itemMemory = NPCItemMemory(npc, item)
            val key = player.name
            var stock = limit - itemMemory.getInt(key)
            stock = maxOf(stock, 0)
            return stock
        }

        /**
         * Determines the maximum amount of an NPC item a player can acquire.
         */
        fun getNPCItemAmount(npc: Int, item: Int, limit: Int, player: Player, amount: Int, period: String = "daily"): Int {
            val stock = getNPCItemStock(npc, item, limit, player, period)
            var realamount = minOf(amount, stock)
            realamount = maxOf(realamount, 0)
            return realamount
        }

        /**
         * Adds a specified amount of an NPC item to a player's memory.
         */
        fun addNPCItemAmount(npc: Int, item: Int, limit: Int, player: Player, amount: Int, period: String = "daily") {
            val itemMemory = NPCItemMemory(npc, item, period)
            val key = player.name
            itemMemory[key] = maxOf(minOf(itemMemory.getInt(key) + amount, limit), 0)
        }
    }
}