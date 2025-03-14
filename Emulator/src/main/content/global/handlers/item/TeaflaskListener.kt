package content.global.handlers.item

import content.data.consumables.effects.NettleTeaEffect
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items

class TeaflaskListener : InteractionListener {
    val CUP_OF_TEA = intArrayOf(Items.CUP_OF_TEA_712, Items.CUP_OF_TEA_1978)

    override fun defineListeners() {
        on(Items.TEA_FLASK_10859, IntType.ITEM, "drink", "look-in") { player, node ->
            val item = node as Item
            val charges = getCharge(item)

            when (getUsedOption(player)) {
                "drink" -> {
                    if (charges <= 1000) {
                        sendMessage(player, "The tea flask is empty.")
                    } else {
                        lock(player, 1)
                        animate(player, Animations.USE_TEA_FLASK_5827)
                        adjustCharge(item, -1000)
                        NettleTeaEffect().activate(player)
                        sendMessage(player, "You take a drink from the flask...")
                        sendChat(player, "Ahh, tea is so refreshing.", 1)
                    }
                }

                "look-in" -> {
                    val chargeAmount =
                        when (item.charge) {
                            6000 -> 5
                            5000 -> 4
                            4000 -> 3
                            3000 -> 2
                            2000 -> 1
                            1000 -> 0
                            else -> 0
                        }
                    sendItemDialogue(
                        player,
                        item.id,
                        "The tea flask holds: $chargeAmount ${if (chargeAmount > 1 || chargeAmount == 0) "cups" else "cup"} of tea.",
                    )
                }
            }
            return@on true
        }

        onUseWith(IntType.ITEM, Items.TEA_FLASK_10859, Items.EMPTY_CUP_1980) { player, used, with ->
            val item = used as Item
            val charges = getCharge(item)

            if (charges == 1000) {
                sendMessage(player, "The tea flask is empty.")
            } else {
                setCharge(item, charges - 1000)
                replaceSlot(player, with.asItem().slot, Item(Items.CUP_OF_TEA_712))
                sendMessage(player, "You fill the cup with tea.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, CUP_OF_TEA, Items.TEA_FLASK_10859) { player, used, with ->
            val item = with as Item
            val charges = getCharge(item)

            if (charges >= 6000) {
                sendMessage(player, "The tea flask is already full.")
            } else {
                replaceSlot(player, used.asItem().slot, Item(Items.EMPTY_CUP_1980))
                adjustCharge(item, 1000)
                sendMessage(player, "You add the tea to the flask.")
            }
            return@onUseWith true
        }
    }
}
