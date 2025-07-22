package content.region.misthalin.varrock.quest.dslay.plugin

import core.api.setAttribute
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items
import java.util.*

object DemonSlayerUtils {
    private val INCANTATIONS = arrayOf("Carlem", "Aber", "Purchai", "Camerinthum", "Gabindo")

    @JvmField val SILVERLIGHT = Item(Items.SILVERLIGHT_2402)
    @JvmField val FIRST_KEY = Item(Items.SILVERLIGHT_KEY_2401)
    @JvmField val SECOND_KEY = Item(Items.SILVERLIGHT_KEY_2400)
    @JvmField val THIRD_KEY = Item(Items.SILVERLIGHT_KEY_2399)

    @JvmStatic
    fun getIncantation(player: Player): String = player.getAttribute("demon-slayer:incantation", generateIncantation(player))

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
