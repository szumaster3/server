package content.region.misthalin.varrock.quest.dslay.plugin

import core.api.setAttribute
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Items

/**
 * Utility for the Demon Slayer quest.
 */
object DemonSlayerUtils {

    /**
     * Possible incantations for the quest.
     */
    private val INCANTATIONS = arrayOf("Carlem", "Aber", "Purchai", "Camerinthum", "Gabindo")

    /**
     * The Silverlight sword item.
     */
    @JvmField val SILVERLIGHT = Item(Items.SILVERLIGHT_2402, 1)

    /**
     * Keys required to obtain Silverlight.
     */
    @JvmField val FIRST_KEY = Item(Items.SILVERLIGHT_KEY_2401, 1)
    @JvmField val SECOND_KEY = Item(Items.SILVERLIGHT_KEY_2400, 1)
    @JvmField val THIRD_KEY = Item(Items.SILVERLIGHT_KEY_2399, 1)

    /**
     * Gets the incantation for the quest.
     * @param player The player to get the incantation for.
     * @return The incantation string.
     */
    @JvmStatic
    fun getIncantation(player: Player): String {
        return player.getAttribute("demon-slayer:incantation") ?: generateIncantation(player)
    }

    /**
     * Generates a new incantation.
     * @param player The player to assign the incantation to.
     * @return A new incantation.
     */
    @JvmStatic
    fun generateIncantation(player: Player): String {
        val incantation = generateNewIncantation()
        setAttribute(player, "/save:demon-slayer:incantation", incantation)
        return incantation
    }

    /**
     * Generates a randomized incantation.
     * @return A incantation.
     */
    private fun generateNewIncantation(): String {
        val shuffled = INCANTATIONS.toMutableList().apply { shuffle() }
        return shuffled.take(5).joinToString(" ") { "$it..." }
    }
}
