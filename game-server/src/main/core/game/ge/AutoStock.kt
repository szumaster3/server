package core.game.ge

import core.api.getItemName
import core.game.world.GameWorld
import core.tools.SystemLogger
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.FileReader

/**
 * Handles automatic stocking of Grand Exchange by
 * reading offers from a JSON file on server startup.
 */
class AutoStock {

    companion object {
        private val DB_PATH = "data${File.separator}eco${File.separator}ge_autostock.json"

        fun autostock() {
            if (!GameWorld.settings!!.autostock_ge) {
                SystemLogger.logGE("AutoStock is disabled in server settings.")
                return
            }

            SystemLogger.logGE("Loading AutoStock offers from $DB_PATH")

            val parser = JSONParser()
            val botReader = FileReader(DB_PATH)
            val botSave = parser.parse(botReader) as JSONObject

            val offers = botSave["offers"] as? JSONArray ?: run {
                SystemLogger.logGE("No offers found in AutoStock JSON.")
                return
            }

            var count = 0
            offers.forEach { offer ->
                val o = offer as JSONObject
                val item = o["item_id"].toString().toInt()
                val amount = o["amount"].toString().toInt()
                if (GrandExchange.addBotOffer(item, amount)) {
                    SystemLogger.logGE("AutoStocked $amount x ${getItemName(item)} [ID: $item]")
                    count++
                } else {
                    SystemLogger.logGE("Skipped item $item - not tradeable or failed to add.")
                }
            }

            SystemLogger.logGE("AutoStock completed. Total items stocked: $count")
        }
    }
}
