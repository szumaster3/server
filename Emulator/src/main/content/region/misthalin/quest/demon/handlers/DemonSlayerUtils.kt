package content.region.misthalin.quest.demon.handlers

import core.api.setAttribute
import core.game.node.entity.player.Player
import core.game.node.item.Item
import java.util.*

object DemonSlayerUtils {
    val INCANTATIONS = arrayOf("Carlem", "Aber", "Purchai", "Camerinthum", "Gabindo")
    @JvmField val SILVERLIGHT = Item(2402)
    @JvmField val FIRST_KEY = Item(2401)
    @JvmField val SECOND_KEY = Item(2400)
    @JvmField val THIRD_KEY = Item(2399)

    @JvmStatic
    fun getIncantation(player: Player): String {
        return player.getAttribute("demon-slayer:incantation", generateIncantation(player))
    }

    @JvmStatic
    fun generateIncantation(player: Player): String {
        val incantation = player.getAttribute("demon-slayer:incantation", generateIncantation())
        setAttribute(player, "/save:demon-slayer:incantation", incantation)
        return incantation
    }

    @JvmStatic
    private fun generateIncantation(): String {
        val incantations = ArrayList<String>(20)
        Collections.addAll(incantations, *INCANTATIONS)
        incantations.shuffle()
        return "${incantations[0]}... ${incantations[1]}... ${incantations[2]}... ${incantations[3]}... ${incantations[4]}"
    }
}
