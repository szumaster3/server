package core.game.ge

import core.ServerConstants
import core.api.StartupListener
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.FileReader

/**
 * Handles automatic stocking of Grand Exchange by
 * reading offers from a JSON file on server startup.
 */
class AutoStock : StartupListener {
    /**
     * Triggers the autostock process on server startup.
     */
    override fun startup() {
        autostock()
    }

    companion object {
        /**
         * Path to the auto-stock JSON file.
         */
        private val DB_PATH = "data${File.separator}eco${File.separator}autostock.json"

        /**
         * Loads offers from JSON and adds them to the Grand Exchange if enabled.
         */
        fun autostock() {
            if (ServerConstants.GE_AUTOSTOCK_ENABLED) {
                val parser = JSONParser()
                val botReader = FileReader(DB_PATH)
                val botSave = parser.parse(botReader) as JSONObject

                (botSave["offers"] as? JSONArray)?.forEach { offer ->
                    val o = offer as JSONObject
                    GrandExchange.addBotOffer(o["item"].toString().toInt(), o["qty"].toString().toInt())
                }
            }
        }
    }
}
