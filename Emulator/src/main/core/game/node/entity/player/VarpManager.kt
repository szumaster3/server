package core.game.node.entity.player

import org.json.simple.JSONArray
import org.json.simple.JSONObject

/**
 * Represents varp manager.
 */
class VarpManager(
    val player: Player,
) {
    fun save(root: JSONObject) {
    }
    /**
     * Parses and processes a JSON array containing varp data and updates the player's varp map.
     * The data structure is expected to contain varp objects with indices and bit arrays,
     * each containing a set of varbit values and offsets.
     *
     * @param data The JSON array containing the varp data to be parsed.
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
