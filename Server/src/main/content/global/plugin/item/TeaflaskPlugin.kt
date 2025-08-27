package content.global.plugin.item

import content.data.consumables.effects.NettleTeaEffect
import core.api.*
import core.game.global.action.DropListener
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Items

/**
 * Handles interaction with the tea flask item.
 */
class TeaflaskPlugin : InteractionListener {

    companion object {
        private val CUP_OF_TEA = intArrayOf(Items.CUP_OF_TEA_712, Items.CUP_OF_TEA_1978)
        private const val CHARGE_PER_CUP = 1000
        private const val MAX_CUPS = 5
        private const val MAX_CHARGE = CHARGE_PER_CUP * (MAX_CUPS + 1)
        private const val EMPTY_CHARGE = CHARGE_PER_CUP
    }

    override fun defineListeners() {
        on(Items.TEA_FLASK_10859, IntType.ITEM, "drink", "look-in", "drop") { player, node ->
            val flask = node as Item
            val charge = getCharge(flask).coerceIn(EMPTY_CHARGE, MAX_CHARGE)

            when (getUsedOption(player)) {
                "drink" -> drinkFromFlask(player, flask, charge)
                "look-in" -> lookInFlask(player, flask, charge)
                "drop" -> dropFlask(player, flask)
            }
            return@on true
        }

        onUseWith(IntType.ITEM, Items.TEA_FLASK_10859, Items.EMPTY_CUP_1980) { player, used, with ->
            val flask = used as Item
            val charge = getCharge(flask).coerceIn(EMPTY_CHARGE, MAX_CHARGE)
            fillCup(player, flask, with.asItem(), charge)
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, CUP_OF_TEA, Items.TEA_FLASK_10859) { player, used, with ->
            val flask = with as Item
            val charge = getCharge(flask).coerceIn(EMPTY_CHARGE, MAX_CHARGE)
            addCup(player, flask, used.asItem(), charge)
            return@onUseWith true
        }
    }

    private fun drinkFromFlask(player: Player, flask: Item, charge: Int) {
        if (charge <= EMPTY_CHARGE) {
            sendMessage(player, "The tea flask is empty.")
            return
        }
        lock(player, 1)
        animate(player, Animations.USE_TEA_FLASK_5827)
        setCharge(flask, (charge - CHARGE_PER_CUP).coerceAtLeast(EMPTY_CHARGE))
        NettleTeaEffect().activate(player)
        sendMessage(player, "You take a drink from the flask...")
        sendChat(player, "Ahh, tea is so refreshing.", 1)
    }

    private fun lookInFlask(player: Player, flask: Item, charge: Int) {
        val cups = ((charge - EMPTY_CHARGE) / CHARGE_PER_CUP).coerceIn(0, MAX_CUPS)
        val word = if (cups == 1) "cup" else "cups"
        sendItemDialogue(player, flask.id, "The tea flask holds: $cups $word of tea.")
    }

    private fun dropFlask(player: Player, flask: Item) {
        setCharge(flask, EMPTY_CHARGE)
        DropListener.drop(player, flask)
        sendMessage(player, "The contents of the tea flask fell out as you dropped it!")
    }

    private fun fillCup(player: Player, flask: Item, cup: Item, charge: Int) {
        if (charge <= EMPTY_CHARGE) {
            sendMessage(player, "The tea flask is empty.")
            return
        }
        setCharge(flask, (charge - CHARGE_PER_CUP).coerceAtLeast(EMPTY_CHARGE))
        replaceSlot(player, cup.slot, Item(Items.CUP_OF_TEA_712))
        sendMessage(player, "You fill the cup with tea.")
    }

    private fun addCup(player: Player, flask: Item, tea: Item, charge: Int) {
        if (charge >= MAX_CHARGE) {
            sendMessage(player, "The tea flask is already full.")
            return
        }
        replaceSlot(player, tea.slot, Item(Items.EMPTY_CUP_1980))
        setCharge(flask, (charge + CHARGE_PER_CUP).coerceAtMost(MAX_CHARGE))
        sendMessage(player, "You add the tea to the flask.")
    }
}
