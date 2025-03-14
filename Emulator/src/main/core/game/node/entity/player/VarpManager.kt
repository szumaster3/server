package core.game.node.entity.player

import org.json.simple.JSONArray
import org.json.simple.JSONObject

class VarpManager(
    val player: Player,
) {
    fun save(root: JSONObject) {
    }

    fun parse(data: JSONArray) {
        for (varpobj in data) {
            val vobj = varpobj as JSONObject
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
