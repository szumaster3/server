package content.global.skill.construction.storage

import content.global.skill.construction.storage.box.BookcaseItem
import content.global.skill.construction.storage.box.CapeRackItem
import content.global.skill.construction.storage.box.FancyDressBoxItem
import content.global.skill.construction.storage.box.ToyBoxItem
import core.api.*
import core.api.item.allInInventory
import core.api.ui.sendInterfaceConfig
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items

private data class StorageInterface(
    val attribute: String,
    val validIds: List<Int>,
    val handler: (Int) -> Unit
)

class ItemStorageListener : InterfaceListener {
    override fun defineInterfaceListeners() {

        /*
         * Handles storage boxes.
         */

        on(INTERFACE) { player, _, _, buttonID, _, _ ->
            val interfaces =
                listOf(
                    StorageInterface("con:bookcase", BookcaseItem.values().map { it.labelId }) { labelId ->
                        val book = BookcaseItem.values().firstOrNull { it.labelId == labelId }
                        book?.let { addItem(player, it.takeId) }
                    },
                    StorageInterface("con:fancy-dress-box", FancyDressBoxItem.values().flatMap { listOf(it.labelId, it.iconId) }) { id ->
                        val item = FancyDressBoxItem.values().firstOrNull { it.labelId == id || it.iconId == id }
                        item?.let { handleFancyDressBox(player, it.ordinal) }
                    },
                    StorageInterface("con:cape-rack", CapeRackItem.values().flatMap { listOf(it.labelId, it.iconId) }) { id ->
                        val cape = CapeRackItem.values().firstOrNull { it.labelId == id || it.iconId == id }
                        cape?.let { handleCapeRack(player, it.ordinal) }
                    },
                    // TODO
                    StorageInterface("con:toybox", ToyBoxItem.values().map { it.displayId }) { id ->
                        val page = getAttribute(player, "con:toybox:page", 0) ?: 0
                        val itemsPerPage = 30
                        val offset = page * itemsPerPage
                        val visibleItems = ToyBoxItem.values().drop(offset).take(itemsPerPage)

                        val item = visibleItems.firstOrNull { it.displayId == id }

                        if (item != null) {
                            handleToyItem(player, item)
                        }
                        when (item) {
                            ToyBoxItem.More -> {
                                setAttribute(player, "con:toybox:page", page + 1)
                                handlePage(player)
                            }

                            ToyBoxItem.Back -> {
                                setAttribute(player, "toybox:page", maxOf(0, page - 1))
                                handlePage(player)
                            }

                            null -> player.debug("Invalid toybox button: [$id]")
                            else -> handleToyItem(player, item)
                        }
                    }
                )

            for (storage in interfaces) {
                if (getAttribute(player, storage.attribute, false)) {
                    if (buttonID in storage.validIds) {
                        storage.handler(buttonID)
                    } else {
                        player.debug("Invalid button ID [$buttonID] for ${storage.attribute}")
                    }
                    break
                }
            }
            return@on true
        }

        /*
         * Remove previous interaction attribute.
         */

        onClose(INTERFACE) { player, _ ->
            removeAttributes(
                player,
                "con:fancy-dress-box",
                "con:bookcase",
                "con:cape-rack",
                "con:toybox",
                "con:toybox:page"
            )
            return@onClose true
        }
    }


    private fun handleFancyDressBox(player: Player, setIndex: Int) {
        val item = FancyDressBoxItem.values().getOrNull(setIndex)

        if (item == null) {
            player.debug("Invalid [$item] selection.")
            return
        }

        val set = if (item == FancyDressBoxItem.RoyalFrogCostume) {
            if (player.isMale) {
                intArrayOf(Items.PRINCE_TUNIC_6184, Items.PRINCE_LEGGINGS_6185)
            } else {
                intArrayOf(Items.PRINCESS_BLOUSE_6186, Items.PRINCESS_SKIRT_6187)
            }
        } else {
            item.takeId
        }

        val labelId = item.labelId
        val iconId = item.iconId

        val key = "set:$setIndex"

        if (getAttribute(player, key, false)) {
            if (freeSlots(player) >= set.size) {
                set.forEach { addItem(player, it, 1) }
                removeAttribute(player, key)
                sendMessage(player, "You take the outfit from the wardrobe.")
                sendInterfaceConfig(player, INTERFACE, labelId, false)
                sendInterfaceConfig(player, INTERFACE, iconId + 1, false)
            } else {
                sendMessage(player, "You don't have enough inventory space.")
            }
        } else {
            if (allInInventory(player, *set)) {
                set.forEach { removeItem(player, Item(it, 1)) }
                setAttribute(player, "/save:$key", true)
                sendMessage(player, "You put your outfit into the wardrobe.")
                sendInterfaceConfig(player, INTERFACE, labelId, true)
                sendInterfaceConfig(player, INTERFACE, iconId + 1, true)
            } else {
                sendMessage(player, "That isn't currently stored in the fancy dress box.")
            }
        }
    }

    private fun handleCapeRack(player: Player, setIndex: Int) {
        val item =
            CapeRackItem.values().getOrNull(setIndex)
                ?: run {
                    player.debug("Invalid cape index: [$setIndex]")
                    return
                }

        val labelId = item.labelId
        val iconId = item.iconId

        val key = "cape:$setIndex"

        if (getAttribute(player, key, false)) {
            if (freeSlots(player) >= 1) {
                addItem(player, item.takeId.first(), 1)
                removeAttribute(player, key)
                sendMessage(player, "You take the cape from the rack.")
                sendInterfaceConfig(player, INTERFACE, labelId, false)
                sendInterfaceConfig(player, INTERFACE, iconId + 1, false)
            } else {
                sendMessage(player, "You don't have enough inventory space.")
            }
        } else {
            if (removeItem(player, item.takeId.first())) {
                setAttribute(player, key, true)
                sendMessage(player, "You put the cape into the rack.")
                sendInterfaceConfig(player, INTERFACE, labelId, true)
                sendInterfaceConfig(player, INTERFACE, iconId + 1, true)
            } else {
                sendMessage(player, "That isn't currently stored in the cape rack.")
            }
        }
    }

    private fun handleToyItem(player: Player, item: ToyBoxItem) {
        val key = "toybox:${item.name}"
        val displayId = item.displayId

        if (getAttribute(player, key, false)) {
            if (freeSlots(player) >= item.takeId.size) {
                item.takeId.forEach { addItem(player, it, 1) }
                removeAttribute(player, key)
                sendMessage(player, "You take the item from the toy box.")
                sendInterfaceConfig(player, INTERFACE, displayId, false)
            } else {
                sendMessage(player, "You don't have enough inventory space.")
            }
        } else {
            if (allInInventory(player, *item.takeId)) {
                item.takeId.forEach { removeItem(player, Item(it, 1)) }
                setAttribute(player, "/save:$key", true)
                sendMessage(player, "You put the item into the toy box.")
                sendInterfaceConfig(player, INTERFACE, displayId, true)
            } else {
                sendMessage(player, "That isn't currently stored in the toy box.")
            }
        }
    }

    private fun handlePage(player: Player) {
        val page = getAttribute(player, "con:toybox:page", 0)
        val itemsPerPage = 30
        val offset = page * itemsPerPage
        ToyBoxItem.values().forEachIndexed { index, item ->
            val visible = index in offset until offset + itemsPerPage
            sendInterfaceConfig(player, INTERFACE, item.displayId, visible)
        }
    }

    companion object {
        private const val INTERFACE = 467
    }
}
