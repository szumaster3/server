package content.global.activity.creation

import core.api.*
import core.game.global.action.DropListener
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Items

class SatchelPlugin : InteractionListener {

    companion object {
        const val BASE_CHARGE_AMOUNT = 1000

        private val FOOD_ITEMS = listOf(
            Items.CAKE_1891,
            Items.BANANA_1963,
            Items.TRIANGLE_SANDWICH_6962
        )

        val SATCHEL_IDS = intArrayOf(
            Items.PLAIN_SATCHEL_10877,
            Items.GREEN_SATCHEL_10878,
            Items.RED_SATCHEL_10879,
            Items.BLACK_SATCHEL_10880,
            Items.GOLD_SATCHEL_10881,
            Items.RUNE_SATCHEL_10882
        )

        /**
         * charge = BASE
         * -        + slot0
         * -        + (slot1 << 8)
         * -        + (slot2 << 16)
         */

        fun decodeList(charge: Int): MutableList<Int> {
            val raw = charge - BASE_CHARGE_AMOUNT
            if (raw < 0) return mutableListOf()

            val a = raw and 0xFF
            val b = (raw shr 8) and 0xFF
            val c = (raw shr 16) and 0xFF

            return listOf(a, b, c).filter { it != 0 }.toMutableList()
        }

        fun encodeList(into: Item, items: List<Int>) {
            var encoded = 0
            if (items.isNotEmpty()) encoded = encoded or (items[0] and 0xFF)
            if (items.size > 1) encoded = encoded or ((items[1] and 0xFF) shl 8)
            if (items.size > 2) encoded = encoded or ((items[2] and 0xFF) shl 16)

            setCharge(into, BASE_CHARGE_AMOUNT + encoded)
        }

    }

    override fun defineListeners() {

        /*
         * Handles adding food to satchel.
         */

        onUseWith(IntType.ITEM, FOOD_ITEMS.toIntArray(), *SATCHEL_IDS) { player, used, with ->
            add(player, used.asItem(), with.asItem())
            return@onUseWith true
        }

        /*
         * Handles satchel interaction options.
         */

        on(SATCHEL_IDS, IntType.ITEM, "inspect", "empty", "drop") { player, node ->
            val item = node.asItem()
            when (getUsedOption(player)) {
                "inspect" -> inspect(player, item)
                "empty" -> empty(player, item)
                "drop" -> drop(player, item)
            }
            return@on true
        }
    }

    private fun add(player: Player, food: Item, satchel: Item) {
        val list = decodeList(getCharge(satchel))

        if (list.contains(food.id)) {
            sendMessage(player, "You already have a ${formatName(food.id)} in there.")
            return
        }

        if (list.size >= 3) {
            sendMessage(player, "Your satchel is already full.")
            return
        }

        list.add(food.id)
        encodeList(satchel, list)

        replaceSlot(player, food.slot, Item())
        sendMessage(player, "You add a ${formatName(food.id)} to the satchel.")
    }

    private fun inspect(player: Player, item: Item) {
        val list = decodeList(getCharge(item))

        if (list.isEmpty()) {
            player.dialogueInterpreter.sendItemMessage(
                item.id,
                "The ${getItemName(item.id)}!",
                "(Containing: Empty!)"
            )
            return
        }

        val names = list.map { "one ${formatName(it)}" }

        val msg = when (names.size) {
            1 -> names[0]
            2 -> names.joinToString(", ")
            3 -> "${names[0]}, ${names[1]} and <br>${names[2]}"
            else -> ""
        }

        player.dialogueInterpreter.sendItemMessage(
            item.id,
            "The ${getItemName(item.id)}!",
            "(Containing: $msg)"
        )
    }

    private fun empty(player: Player, item: Item) {
        val list = decodeList(getCharge(item))

        if (list.isEmpty()) {
            sendMessage(player, "It's already empty.")
            return
        }

        if (freeSlots(player) < list.size) {
            sendMessage(player, "You don't have enough inventory space.")
            return
        }

        list.forEach { addItem(player, it, 1) }

        encodeList(item, emptyList())
        sendMessage(player, "You empty the contents of the satchel.")
    }

    private fun drop(player: Player, satchel: Item) {
        val list = decodeList(getCharge(satchel))

        list.forEach { DropListener.drop(player, Item(it)) }

        encodeList(satchel, emptyList())
        DropListener.drop(player, satchel)

        sendMessage(player, "The contents of the satchel fell out as you dropped it!")
    }

    private fun formatName(itemId: Int): String {
        return getItemName(itemId).lowercase().removePrefix("triangle ").trim()
    }
}
