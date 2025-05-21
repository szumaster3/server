package content.global.activity.penguinhns

import core.ServerStore.Companion.toJSONArray
import core.api.log
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.tools.Log
import org.json.simple.JSONArray
import org.json.simple.JSONObject

/**
 * Manages the Penguin Hunter activity state.
 */
class PenguinManager {
    companion object {

        /**
         * List of ordinal identifiers representing currently spawned penguins.
         */
        var penguins: MutableList<Int> = ArrayList()

        /**
         * List of NPC instances representing penguins currently spawned in the world.
         */
        var npcs = ArrayList<NPC>()

        /**
         * Instance responsible for spawning penguins.
         */
        val spawner = PenguinSpawner()

        /**
         * Mapping from penguin ordinal to a JSON array of player usernames
         * who have tagged that penguin.
         */
        var tagMapping: MutableMap<Int, JSONArray> = HashMap()

        /**
         * Registers that a player has tagged a penguin at a given location.
         *
         * @param player The player who tagged the penguin.
         * @param location The location of the penguin that was tagged.
         */
        fun registerTag(
            player: Player,
            location: Location,
        ) {
            val ordinal = Penguin.forLocation(location)?.ordinal ?: -1
            val list = tagMapping[ordinal] ?: JSONArray()

            list.add(player.username.lowercase())
            tagMapping[ordinal] = list
            updateStoreFile()
        }

        /**
         * Checks whether a player has already tagged the penguin at the given location.
         *
         * @param player The player to check.
         * @param location The location of the penguin.
         * @return `True` if the player has tagged this penguin, `false` otherwise.
         */
        fun hasTagged(
            player: Player,
            location: Location,
        ): Boolean {
            val ordinal = Penguin.forLocation(location)?.ordinal
            return tagMapping[ordinal]?.contains(player.username.lowercase()) ?: false
        }

        /**
         * Updates the persistent storage file with the current tag mappings.
         */
        private fun updateStoreFile() {
            val jsonTags = JSONArray()
            tagMapping.filter { it.value.isNotEmpty() }.forEach { (ordinal, taggers) ->
                log(this::class.java, Log.FINE, "$ordinal - ${taggers.first()}")

                val tag = JSONObject()
                tag["ordinal"] = ordinal
                tag["taggers"] = taggers
                jsonTags.add(tag)
            }

            PenguinHNSEvent.getStoreFile()["tag-mapping"] = jsonTags
        }
    }

    /**
     * Rebuilds the internal variables based on the saved store file.
     *
     * - If no penguins are currently spawned, spawns 10 penguins and initializes tag mappings.
     * - Otherwise, loads the spawned penguins and tag mappings from persistent storage.
     */
    fun rebuildVars() {
        if (!PenguinHNSEvent.getStoreFile().containsKey("spawned-penguins")) {
            penguins = spawner.spawnPenguins(10)
            PenguinHNSEvent.getStoreFile()["spawned-penguins"] = penguins.toJSONArray()
            tagMapping.clear()

            for (p in penguins) {
                tagMapping[p] = JSONArray()
            }
            updateStoreFile()
        } else {
            val spawnedOrdinals =
                (PenguinHNSEvent.getStoreFile()["spawned-penguins"] as JSONArray).map {
                    it
                        .toString()
                        .toInt()
                }
            spawner.spawnPenguins(spawnedOrdinals)
            val storedTags =
                (PenguinHNSEvent.getStoreFile()["tag-mapping"] as? JSONArray)
                    ?.associate { jRaw ->
                        val jObj = jRaw as JSONObject
                        jObj["ordinal"].toString().toInt() to (jObj["taggers"] as JSONArray)
                    }?.toMutableMap() ?: HashMap()

            tagMapping = storedTags
            penguins = spawnedOrdinals.toMutableList()
        }
    }
}
