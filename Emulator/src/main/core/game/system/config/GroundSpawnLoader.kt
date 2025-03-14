package core.game.system.config

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
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader
import java.io.PrintWriter
import java.io.StringWriter
import java.nio.ByteBuffer

class GroundSpawnLoader {
    val parser = JSONParser()
    var reader: FileReader? = null

    fun load() {
        var count = 0
        reader = FileReader(ServerConstants.CONFIG_PATH + "ground_spawns.json")
        var configs = parser.parse(reader) as JSONArray
        for (config in configs) {
            try {
                val e = config as JSONObject
                val data = e["loc_data"].toString().split("-")
                val id = e["item_id"].toString().toInt()
                for (d in data) {
                    if (d.isNullOrEmpty()) continue
                    val tokens =
                        d
                            .replace("{", "")
                            .replace("}", "")
                            .split(",".toRegex())
                            .toTypedArray()
                    val spawn =
                        GroundSpawn(
                            tokens[4].toInt(),
                            Item(id, tokens[0].toInt()),
                            Location(
                                Integer.valueOf(tokens[1]),
                                Integer.valueOf(tokens[2]),
                                Integer.valueOf(tokens[3]),
                            ),
                        )
                    spawn.init()
                    count++
                }
            } catch (e: Exception) {
                val sw = StringWriter()
                val pw = PrintWriter(sw)
                e.printStackTrace(pw)
                log(this::class.java, Log.ERR, "Error parsing config entry $config: $sw")
            }
        }

        log(this::class.java, Log.FINE, "Initialized $count ground items.")
    }

    class GroundSpawn(
        var respawnRate: Int,
        item: Item,
        location: Location?,
    ) : GroundItem(item, location) {
        override fun toString(): String {
            return "GroundSpawn [name=" + getName() + ", respawnRate=" + respawnRate + ", loc=" + getLocation() + "]"
        }

        fun save(buffer: ByteBuffer) {
            buffer.putInt(respawnRate)
            buffer.putShort(id.toShort())
            buffer.putInt(amount)
            buffer
                .putShort((getLocation().x and 0xFFFF).toShort())
                .putShort((getLocation().y and 0xFFFF).toShort())
                .put(getLocation().z.toByte())
        }

        fun init(): GroundItem {
            return GroundItemManager.create(this)
        }

        override fun isActive(): Boolean {
            return true
        }

        override fun isPrivate(): Boolean {
            return false
        }

        override fun isAutoSpawn(): Boolean {
            return true
        }

        override fun respawn() {
            GameWorld.Pulser.submit(
                object : Pulse(respawnDuration) {
                    override fun pulse(): Boolean {
                        GroundItemManager.create(this@GroundSpawn)
                        return true
                    }
                },
            )
        }

        fun setRespawnRate(
            min: Int,
            max: Int,
        ) {
            respawnRate = min or max shl 16
        }

        val respawnDuration: Int
            get() {
                val minimum = respawnRate and 0xFFFF
                val maximum = respawnRate shr 16 and 0xFFFF
                val playerRatio = ServerConstants.MAX_PLAYERS / Repository.players.size.toDouble()
                return (minimum + (maximum - minimum) / playerRatio).toInt()
            }
    }
}
