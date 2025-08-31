package core.game.system.config

import com.google.gson.Gson
import com.google.gson.JsonArray
import core.ServerConstants
import core.api.log
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.repository.Repository
import core.tools.Log
import java.io.FileReader
import java.io.PrintWriter
import java.io.StringWriter
import java.nio.ByteBuffer

class GroundSpawnLoader {
    private val gson = Gson()

    fun load() {
        var count = 0
        FileReader(ServerConstants.CONFIG_PATH + "ground_spawns.json").use { reader ->
            val configs = gson.fromJson(reader, JsonArray::class.java)
            for (configElement in configs) {
                try {
                    val e = configElement.asJsonObject
                    val data = e.get("loc_data").asString.split("-")
                    val id = e.get("item_id").asInt
                    for (d in data) {
                        if (d.isEmpty()) continue
                        val tokens = d
                            .replace("{", "")
                            .replace("}", "")
                            .split(",")
                            .map { it.trim() }

                        val spawn = GroundSpawn(
                            tokens[4].toInt(),
                            Item(id, tokens[0].toInt()),
                            Location(tokens[1].toInt(), tokens[2].toInt(), tokens[3].toInt())
                        )
                        spawn.init()
                        count++
                    }
                } catch (ex: Exception) {
                    val sw = StringWriter()
                    val pw = PrintWriter(sw)
                    ex.printStackTrace(pw)
                    log(this::class.java, Log.ERR, "Error parsing config entry [$configElement]: [$sw]")
                }
            }
        }
        log(this::class.java, Log.DEBUG, "Initialized $count ground items.")
    }

    class GroundSpawn(
        var respawnRate: Int,
        item: Item,
        location: Location?,
    ) : GroundItem(item, location) {
        override fun toString(): String =
            "GroundSpawn [name=${getName()}, respawnRate=$respawnRate, loc=${getLocation()}]"

        fun save(buffer: ByteBuffer) {
            buffer.putInt(respawnRate)
            buffer.putShort(id.toShort())
            buffer.putInt(amount)
            buffer.putShort((getLocation().x and 0xFFFF).toShort())
                .putShort((getLocation().y and 0xFFFF).toShort())
                .put(getLocation().z.toByte())
        }

        fun init(): GroundItem = GroundItemManager.create(this)

        override fun isActive(): Boolean = true

        override fun isPrivate(): Boolean = false

        override fun isAutoSpawn(): Boolean = true

        override fun respawn() {
            GameWorld.Pulser.submit(
                object : Pulse(respawnDuration) {
                    override fun pulse(): Boolean {
                        GroundItemManager.create(this@GroundSpawn)
                        return true
                    }
                }
            )
        }

        fun setRespawnRate(min: Int, max: Int) {
            respawnRate = min or (max shl 16)
        }

        val respawnDuration: Int
            get() {
                val minimum = respawnRate and 0xFFFF
                val maximum = respawnRate shr 16 and 0xFFFF
                val playerRatio = ServerConstants.MAX_PLAYERS / Repository.players.size.toDouble()
                return (minimum + ((maximum - minimum) / playerRatio)).toInt()
            }
    }
}