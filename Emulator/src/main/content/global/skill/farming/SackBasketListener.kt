package content.global.skill.farming

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items

class SackBasketListener : InteractionListener {
    private companion object {
        val fruit =
            arrayOf(
                Items.ORANGE_2108,
                Items.COOKING_APPLE_1955,
                Items.BANANA_1963,
                Items.STRAWBERRY_5504,
                Items.TOMATO_1982,
            )
        val produce = arrayOf(Items.POTATO_1942, Items.ONION_1957, Items.CABBAGE_1965)
    }

    override fun defineListeners() {
        on("fill", IntType.ITEM) { player, item ->
            if (item is Item) {
                tryFill(player, item)
            }
            return@on true
        }

        on("empty", IntType.ITEM) { player, item ->
            if (item is Item) {
                tryEmpty(player, item)
            }
            return@on true
        }

        on("remove-one", IntType.ITEM) { player, item ->
            if (item is Item) {
                tryTakeOne(player, item)
            }
            return@on true
        }
    }

    private fun tryFill(
        player: Player,
        item: Item,
    ) {
        if (item.id == Items.EMPTY_SACK_5418) {
            val hasProduce =
                player.inventory.contains(Items.POTATO_1942, 1) ||
                    player.inventory.contains(Items.ONION_1957, 1) ||
                    player.inventory.contains(Items.CABBAGE_1965, 1)

            if (!hasProduce) {
                sendMessage(player, "You don't have any potatoes, onions or cabbages.")
                return
            }
        }

        val containerID = item.id
        val appropriateProduce = getAppropriateProduce(player, containerID) ?: return
        val container = BasketsAndSacks.forId(containerID) ?: BasketsAndSacks.forId(appropriateProduce.id) ?: return
        val isLast = container.checkIsLast(containerID)
        val specific = container.checkWhich(containerID)
        val max = container.containers.size - 1

        if (isLast) {
            player.sendMessage("This is already full.")
            return
        }

        if (specific + appropriateProduce.amount > max) {
            appropriateProduce.amount = (max - specific)
        }

        if (player.inventory.remove(item) && player.inventory.remove(appropriateProduce)) {
            player.inventory.add(Item(container.containers[specific + appropriateProduce.amount]))
        }
    }

    private fun tryEmpty(
        player: Player,
        item: Item,
    ) {
        val container = BasketsAndSacks.forId(item.id) ?: return
        val emptyItem = if (produce.contains(container.produceID)) Items.EMPTY_SACK_5418 else Items.BASKET_5376
        val returnItem = Item(container.produceID, container.checkWhich(item.id) + 1)

        if (!hasSpaceFor(player, returnItem)) {
            sendMessage(player, "You don't have enough inventory space to do this.")
            return
        }

        if (removeItem(player, item)) {
            addItem(player, emptyItem)
            player.inventory.add(returnItem)
        } else {
            sendMessage(player, "You don't have any produce left.")
        }
    }

    private fun tryTakeOne(
        player: Player,
        item: Item,
    ) {
        val container = BasketsAndSacks.forId(item.id) ?: return
        val emptyItem = if (produce.contains(container.produceID)) Items.EMPTY_SACK_5418 else Items.BASKET_5376
        val isLast = container.checkIsFirst(item.id)
        val withdrawnItem = Item(container.produceID)

        if (!hasSpaceFor(player, withdrawnItem)) {
            sendMessage(player, "You don't have enough inventory space to do this.")
            return
        }

        if (removeItem(player, item)) {
            if (isLast) {
                addItem(player, emptyItem)
            } else {
                val it = Item(container.containers[container.checkWhich(item.id) - 1])
                player.inventory.add(it)
            }
            player.inventory.add(withdrawnItem)
        }
    }

    private fun getAppropriateProduce(
        player: Player,
        containerID: Int,
    ): Item? {
        val container = BasketsAndSacks.forId(containerID)
        val produce =
            if (container == null) {
                var selected = 0
                if (containerID == Items.EMPTY_SACK_5418) {
                    for (i in produce) {
                        if (inInventory(player, i, 1)) {
                            selected = i
                            break
                        }
                    }
                    selected
                } else {
                    for (i in fruit) {
                        if (inInventory(player, i, 1)) {
                            selected = i
                            break
                        }
                    }
                    selected
                }
            } else {
                container.produceID
            }

        return if (produce == 0) null else Item(produce, player.inventory.getAmount(produce))
    }
}
