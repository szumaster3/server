package core.game.ge

import core.ServerConstants
import core.api.StartupListener
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.FileReader

class AutoStock : StartupListener {
    override fun startup() {
        autostock()
    }

    companion object {
        private val DB_PATH = "data" + File.separator + "eco" + File.separator + "autostock.json"

        fun autostock() {
            if (ServerConstants.GE_AUTOSTOCK_ENABLED) {
                val parser = JSONParser()

                val botReader: FileReader = FileReader(DB_PATH)
                val botSave = parser.parse(botReader) as JSONObject

                if (botSave.containsKey("offers")) {
                    val offers = botSave["offers"] as JSONArray

                    for (offer in offers) {
                        val o = offer as JSONObject
                        GrandExchange.addBotOffer(o["item"].toString().toInt(), o["qty"].toString().toInt())
                    }
                }
            }
        }
    }
}
