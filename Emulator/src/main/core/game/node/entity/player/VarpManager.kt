package core.game.node.entity.player

import org.json.simple.JSONArray
import org.json.simple.JSONObject

/**
 * Represents varp manager.
 */
class VarpManager(val player: Player) {

    /**
     * Saves varp data into the provided JSON root object.
     */
    fun save(root: JSONObject) {
        // Empty
    }

    /**
     * Parses varp data from a JSON array and updates the player's varp map.
     *
     * @param data JSON array of varp entries.
     */
    fun parse(data: JSONArray) {
        for (varpObj in data) {
            val vobj = varpObj as JSONObject
            val index = vobj["index"].toString().toInt()
            val bits = vobj["bitArray"] as JSONArray
            var total = 0
            for (vbit in bits) {
                val varbit = vbit as JSONObject
                val offset = varbit["offset"].toString().toInt()
                val value = varbit["value"].toString().toInt()
                total = total or (value shl offset)
            }
            player.varpMap[index] = total
            player.saveVarp[index] = true
        }
    }
}