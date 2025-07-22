package core.game.consumable

import core.api.playAudio
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Sounds

/**
 * Represents a consumable potion with multiple doses.
 *
 * @param ids Array of potion dose item IDs.
 * @param effect Optional effect applied on consumption.
 * @param messages Optional custom messages per dose.
 */
open class Potion(
    private val ids: IntArray?,
    private val effect: ConsumableEffect?,
    private vararg val messages: String?,
) : Drink(ids, effect, *messages) {

    /**
     * Consumes a dose of the potion, replaces it with the next dose or vial.
     */
    override fun consume(item: Item, player: Player) {
        executeConsumptionActions(player)

        val nextItemId = getNextItemId(item.id)
        val replacementItem = if (nextItemId != -1) Item(nextItemId) else Item(VIAL)
        player.inventory.replace(replacementItem, item.slot)

        val initialLifePoints = player.getSkills().lifepoints
        effect?.activate(player)
        sendMessages(player, item)

        sendHealingMessage(player, initialLifePoints)
    }

    /**
     * Sends drinking animation and sound.
     */
    override fun executeConsumptionActions(player: Player) {
        player.animate(animation)
        playAudio(player, Sounds.LIQUID_2401, 1, 1)
    }

    /**
     * Sends default or custom messages after drinking.
     */
    private fun sendMessages(player: Player, item: Item) {
        if (messages.isEmpty()) sendDefaultMessages(player, item)
        else sendCustomMessages(player, messages)
    }

    /**
     * Sends default messages about potion drinking and remaining doses.
     */
    override fun sendDefaultMessages(player: Player, item: Item) {
        val consumedDoses = ids.indexOf(item.id) + 1
        val dosesLeft = ids.size - consumedDoses

        player.packetDispatch.sendMessage("You drink some of your ${getFormattedName(item)}.")

        val dosesMessage = when {
            dosesLeft > 1 -> "You have $dosesLeft doses of potion left."
            dosesLeft == 1 -> "You have 1 dose of potion left."
            else -> "You have finished your potion."
        }
        player.packetDispatch.sendMessage(dosesMessage)
    }

    /**
     * Gets the number of doses left in the potion item.
     *
     * @param potion The potion item.
     * @return Number of doses left.
     */
    fun getDose(potion: Item): Int {
        val index = ids.indexOf(potion.id)
        return if (index != -1) ids.size - index else potion.name.replace("[^\\d.]".toRegex(), "").toInt()
    }

    override fun getIds(): IntArray = ids

    override fun getEffect(): ConsumableEffect = effect ?: throw IllegalArgumentException("Effect cannot be null.")

    companion object {
        private const val VIAL = Items.VIAL_229
    }
}
