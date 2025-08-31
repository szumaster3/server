package core.game.system.config

import com.google.gson.Gson
import com.google.gson.JsonArray
import core.ServerConstants
import core.api.log
import core.cache.def.impl.DataMap
import core.game.node.entity.player.link.music.MusicEntry
import core.game.node.entity.player.link.music.MusicZone
import core.game.world.map.RegionManager
import core.game.world.map.zone.ZoneBorders
import core.tools.Log
import java.io.FileReader

/**
 * Loads music configuration data from JSON.
*/
class MusicConfigLoader {
    private val gson = Gson()

    fun load() {
        val songs = DataMap.get(1351)
        val names = DataMap.get(1345)

        songs.dataStore.forEach { (indexAny, songIdAny) ->
            val index = indexAny as? Int ?: return@forEach
            val songId = songIdAny as? Int ?: return@forEach
            val songName = names?.getString(index)
            val entry = MusicEntry(songId, songName, index)
            MusicEntry.getSongs().putIfAbsent(songId, entry)
        }

        val filePath = ServerConstants.CONFIG_PATH + "music_configs.json"
        FileReader(filePath).use { reader ->
            val configs = gson.fromJson(reader, JsonArray::class.java)
            var count = 0

            for (element in configs) {
                val config = element.asJsonObject
                val musicId = config.get("id")?.asInt ?: continue
                val bordersString = config.get("borders")?.asString ?: continue

                val borderStrings = bordersString.split("-").filter { it.isNotBlank() }

                for (borderRaw in borderStrings) {
                    val cleaned = borderRaw.replace("{", "").replace("}", "")
                    val tokens = cleaned.split(",")
                    if (tokens.size < 4) continue

                    val zoneBorders = ZoneBorders(
                        tokens[0].toInt(),
                        tokens[1].toInt(),
                        tokens[2].toInt(),
                        tokens[3].toInt()
                    )

                    if ('[' in borderRaw) {
                        val exceptionsRaw = tokens.drop(4).joinToString(",") {
                            if (!it.contains("]~") && !it.contains("[")) "$it," else it
                        }

                        val exceptions = exceptionsRaw.split("~").mapNotNull { ex ->
                            val cleanedEx = ex.replace("[", "").replace("]", "")
                            val exTokens = cleanedEx.split(",")
                            if (exTokens.size < 4) null else exTokens
                        }

                        exceptions.forEach { exTokens ->
                            zoneBorders.addException(
                                ZoneBorders(
                                    exTokens[0].toInt(),
                                    exTokens[1].toInt(),
                                    exTokens[2].toInt(),
                                    exTokens[3].toInt()
                                )
                            )
                        }
                    }

                    val zone = MusicZone(musicId, zoneBorders)
                    zoneBorders.regionIds
                        .filterNotNull()
                        .forEach { regionId ->
                            RegionManager.forId(regionId).musicZones.add(zone)
                        }
                }
                count++
            }
            log(this::class.java, Log.DEBUG, "Parsed $count music configs.")
        }
    }
}
