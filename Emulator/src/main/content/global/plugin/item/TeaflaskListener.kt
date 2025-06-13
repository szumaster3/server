package content.global.plugin.item

import content.data.consumables.effects.NettleTeaEffect
import core.api.*
import core.game.global.action.DropListener
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items

/**
 * Handles interaction with tea flask item.
 */
class TeaflaskListener : InteractionListener {

    private val CUP_OF_TEA = intArrayOf(Items.CUP_OF_TEA_712, Items.CUP_OF_TEA_1978)
    private val CHARGE_PER_CUP = 1000
    private val MAX_CUPS = 5
    private val MAX_CHARGE = CHARGE_PER_CUP * (MAX_CUPS + 1) // offset
    private val EMPTY_CHARGE = CHARGE_PER_CUP

    override fun defineListeners() {

        /*
         * Handles interaction with tea flask (drink, look-in).
         */

        on(Items.TEA_FLASK_10859, IntType.ITEM, "drink", "look-in", "drop") { player, node ->
            val flask = node as Item
            val rawCharge = getCharge(flask)
            val actualCharge = (rawCharge - CHARGE_PER_CUP).coerceAtLeast(0)

            when (getUsedOption(player)) {
                "drink" -> {
                    if (actualCharge < CHARGE_PER_CUP) {
                        sendMessage(player, "The tea flask is empty.")
                        return@on true
                    }

                    lock(player, 1)
                    animate(player, Animations.USE_TEA_FLASK_5827)
                    setCharge(flask, rawCharge - CHARGE_PER_CUP)
                    NettleTeaEffect().activate(player)
                    sendMessage(player, "You take a drink from the flask...")
                    sendChat(player, "Ahh, tea is so refreshing.", 1)
                }

                "look-in" -> {
                    val cupCount = (actualCharge / CHARGE_PER_CUP).coerceIn(0, MAX_CUPS)
                    val cupWord = if (cupCount == 1) "cup" else "cups"
                    sendItemDialogue(player, flask.id, "The tea flask holds: $cupCount $cupWord of tea.")
                }
                "drop" -> {
                    setCharge(flask, EMPTY_CHARGE)
                    sendMessage(player, "The contents of the teaflask fell out as you dropped it!")
                    DropListener.drop(player, flask)
                }
            }

            return@on true
        }

        /*
         * Handles filling empty cups with tea.
         */

        onUseWith(IntType.ITEM, Items.TEA_FLASK_10859, Items.EMPTY_CUP_1980) { player, used, with ->
            val flask = used as Item
            val baseCharge = getCharge(flask)
            val actualCharge = (baseCharge - CHARGE_PER_CUP).coerceAtLeast(0)

            if (actualCharge < CHARGE_PER_CUP) {
                sendMessage(player, "The tea flask is empty.")
                return@onUseWith true
            }

            setCharge(flask, baseCharge - CHARGE_PER_CUP)
            replaceSlot(player, with.asItem().slot, Item(Items.CUP_OF_TEA_712))
            sendMessage(player, "You fill the cup with tea.")
            return@onUseWith true
        }

        /*
         * Handles adding tea to flask.
         */

        onUseWith(IntType.ITEM, CUP_OF_TEA, Items.TEA_FLASK_10859) { player, used, with ->
            val flask = with as Item
            val baseCharge = getCharge(flask)

            if (baseCharge >= MAX_CHARGE) {
                sendMessage(player, "The tea flask is already full.")
                return@onUseWith true
            }

            replaceSlot(player, used.asItem().slot, Item(Items.EMPTY_CUP_1980, 1))
            setCharge(flask, baseCharge + CHARGE_PER_CUP)
            sendMessage(player, "You add the tea to the flask.")
            return@onUseWith true
        }
    }
}
