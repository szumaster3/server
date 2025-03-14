package content.global.handlers.item

import core.api.*
import core.game.global.action.DropListener
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items

class SatchelListener : InteractionListener {
    companion object {
        val BASE_CHARGE_AMOUNT = 1000
        val SATCHEL_RESOURCES = intArrayOf(Items.CAKE_1891, Items.BANANA_1963, Items.TRIANGLE_SANDWICH_6962)
        val SATCHEL_IDS =
            intArrayOf(
                Items.PLAIN_SATCHEL_10877,
                Items.GREEN_SATCHEL_10878,
                Items.RED_SATCHEL_10879,
                Items.BLACK_SATCHEL_10880,
                Items.GOLD_SATCHEL_10881,
                Items.RUNE_SATCHEL_10882,
            )

        private val chargeItemMapping =
            mapOf(
                11816 to listOf(Items.CAKE_1891, Items.BANANA_1963, Items.TRIANGLE_SANDWICH_6962),
                9925 to listOf(Items.BANANA_1963, Items.TRIANGLE_SANDWICH_6962),
                9853 to listOf(Items.CAKE_1891, Items.TRIANGLE_SANDWICH_6962),
                7962 to listOf(Items.TRIANGLE_SANDWICH_6962),
                4854 to listOf(Items.CAKE_1891, Items.BANANA_1963),
                2963 to listOf(Items.BANANA_1963),
                2891 to listOf(Items.CAKE_1891),
            )
    }

    override fun defineListeners() {
        onUseWith(IntType.ITEM, SATCHEL_RESOURCES, *SATCHEL_IDS) { player, used, with ->
            add(player, used.asItem(), with.asItem())
            return@onUseWith true
        }

        on(SATCHEL_IDS, IntType.ITEM, "inspect", "empty", "drop") { player, node ->
            when (getUsedOption(player)) {
                "inspect" -> inspect(player, node.asItem())
                "empty" -> empty(player, node.asItem())
                "drop" -> drop(player, node.asItem())
                else -> player.debug("Something wrong with: ${node.asItem()}.")
            }
            return@on true
        }
    }

    private fun add(
        player: Player,
        used: Item,
        satchelItem: Item,
    ) {
        val chargesAmount = getCharge(satchelItem)
        val baseChargeAmount = BASE_CHARGE_AMOUNT

        if (isFull(chargesAmount, baseChargeAmount)) {
            sendMessage(player, "Your satchel is already full.")
            return
        }

        val itemId = used.id
        val itemName = getItemName(itemId).lowercase()
        val targetCharges = itemId + baseChargeAmount

        if (check(satchelItem, chargesAmount, targetCharges, itemId)) {
            sendMessage(player, "You already have a $itemName in there.")
            return
        }

        replaceSlot(player, used.slot, Item())
        adjustCharge(satchelItem, itemId)
        sendMessage(player, "You add a $itemName to the satchel.")
    }

    private fun isFull(
        charges: Int,
        base: Int,
    ) = charges >= 10816 + base

    private fun inspect(
        player: Player,
        item: Item,
    ) {
        val itemName = getItemName(item.id).lowercase().removePrefix("triangle ").trim()
        val itemIds = chargeItemMapping[getCharge(item)]?.distinct() ?: emptyList()
        val message =
            when (itemIds.size) {
                0 -> "Empty!"
                1 -> "one ${itemName[0]}"
                2 -> itemIds.joinToString(", ") { "one $itemName" }
                3 -> itemIds.joinToString(", ", limit = 2, truncated = "and one ${itemName[2]}")
                else -> throw IllegalStateException("Unexpected satchel content size.")
            }

        sendItemDialogue(player, item.id, "The ${getItemName(item.id)}!<br>(Containing: $message)")
    }

    private fun empty(
        player: Player,
        item: Item,
    ) {
        if (freeSlots(player) == 0) {
            sendMessage(player, "You don't have enough inventory space.")
            return
        }

        val satchelItems = chargeItemMapping[getCharge(item)]?.distinct() ?: emptyList()
        if (satchelItems.isEmpty()) {
            sendMessage(player, "It's already empty.")
            return
        }

        satchelItems.forEach { itemId -> addItem(player, itemId, 1) }
        setCharge(item, 1000)
    }

    private fun check(
        item: Item,
        charges: Int,
        targetCharge: Int,
        checkId: Int,
    ): Boolean {
        val potentialItems =
            setOf(
                Items.CAKE_1891 + targetCharge,
                Items.BANANA_1963 + targetCharge,
                Items.TRIANGLE_SANDWICH_6962 + targetCharge,
            )
        return item.isCharged && (charges == targetCharge || potentialItems.contains(checkId))
    }

    private fun drop(
        player: Player,
        satchel: Item,
    ) {
        setCharge(satchel, 1000)
        sendMessage(player, "The contents of the satchel fell out as you dropped it!")
        DropListener.drop(player, satchel)
    }
}
