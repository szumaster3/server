package content.global.skill.construction.decoration.costumeroom

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import shared.consts.Items

/**
 * Handles the interface and interactions for
 * the Construction Costume Room storage boxes.
 */
class StorageBoxInterface : InterfaceListener {
    companion object {
        private const val INTERFACE = 467
        private const val COMPONENT = 164
        private const val SIZE = 30
        private const val PAGE_SIZE = SIZE - 1
        private const val BUTTON_MORE = Items.MORE_10165
        private const val BUTTON_BACK = Items.BACK_10166

        /**
         * Singleton instance of the storage interface.
         */
        lateinit var instance: StorageBoxInterface
            private set

        /**
         * Opens the storage box.
         *
         * @param player The player.
         * @param type The type of storage to open.
         */
        fun openStorage(player: Player, type: StorableType) {
            instance.openStorageForType(player, type)
        }
    }

    init {
        instance = this
    }

    override fun defineInterfaceListeners() {
        on(INTERFACE) { player, _, _, buttonId, _, _ ->
            val typeName = getAttribute(player, "con:storage:type", null) as? String ?: return@on true
            val type = StorableType.valueOf(typeName.uppercase())
            handleStorageInteraction(player, buttonId, type)
            return@on true
        }
    }

    /**
     * Gets the storage container.
     */
    private fun getStorageContainer(player: Player, type: StorableType) =
        player.getCostumeRoomState().getContainer(type)

    /**
     * Handles interaction with storage interface.
     */
    private fun handleStorageInteraction(player: Player, buttonId: Int, type: StorableType) {
        val container = getStorageContainer(player, type)
        val allItems = StorableRepository.getRelevantItems(type)
        val pageIndex = container.getPageIndex(type)

        val pageItems = allItems.drop(pageIndex * PAGE_SIZE).take(PAGE_SIZE)
        val slots = MutableList<Any?>(SIZE) { null }

        var idx = 0
        pageItems.forEach {
            if (idx >= SIZE) return@forEach
            slots[idx++] = it
        }

        val hasPrev = pageIndex > 0
        val hasNext = allItems.size > (pageIndex + 1) * PAGE_SIZE

        if (hasNext && idx < SIZE) slots[idx++] = "MORE"
        if (hasPrev && idx < SIZE) slots[idx] = "BACK"

        val slotIndex = when {
            buttonId in 56..(56 + (SIZE - 1) * 2) step 2 -> (buttonId - 56) / 2
            buttonId in 165..223 step 2 -> (buttonId - 165) / 2
            else -> return
        }

        when (val clicked = slots.getOrNull(slotIndex)) {
            "MORE" -> {
                container.nextPage(type, allItems.size, PAGE_SIZE)
                openInterface(player, INTERFACE)
                updateStorageInterface(player, type)
            }
            "BACK" -> {
                container.prevPage(type)
                openInterface(player, INTERFACE)
                updateStorageInterface(player, type)
            }
            is Storable -> processItemTransaction(player, clicked, type)
        }
    }

    /**
     * Handles taking or depositing an item from/to the storage.
     */
    private fun processItemTransaction(player: Player, item: Storable, type: StorableType) {
        val container = getStorageContainer(player, type)
        val storedItems = container.getItems(type).toSet()
        val actualId = item.takeIds.firstOrNull() ?: item.displayId

        if (actualId in storedItems) {
            if (freeSlots(player) <= 0) {
                sendMessage(player, "You don't have enough inventory space.")
                return
            }
            val boxName = when {
                type.name.contains("TRAILS") -> "Treasure chest"
                type.name.contains("SET_OF_ARMOUR") -> "Magic wardrobe"
                type.name.contains("ARMOUR_CASE") -> "Armour case"
                else -> type.name.lowercase()
            }
            sendMessage(player, "You take the item from the $boxName box.")
            addItem(player, actualId, 1)
            container.withdraw(type, item)
        } else {
            if (player.inventory.contains(actualId, 1)) {
                sendMessage(player, "You put the item into the box.")
                removeItem(player, Item(actualId))
                container.addItem(type, actualId)
            } else {
                sendMessage(player, "You don't have that item in your inventory.")
            }
        }
        updateStorageInterface(player, type)
    }

    private fun updateStorageInterface(player: Player, type: StorableType) {
        val container = getStorageContainer(player, type)
        val allItems = StorableRepository.getRelevantItems(type)
        val stored = container.getItems(type).toSet()
        val pageIndex = container.getPageIndex(type)

        val pageItems = allItems.drop(pageIndex * PAGE_SIZE).take(PAGE_SIZE)
        val slots = MutableList<Any?>(SIZE) { null }

        var idx = 0
        pageItems.forEach {
            if (idx >= SIZE) return@forEach
            slots[idx++] = it
        }

        val hasPrev = pageIndex > 0
        val hasNext = allItems.size > (pageIndex + 1) * PAGE_SIZE

        if (hasNext && idx < SIZE) slots[idx++] = "MORE"
        if (hasPrev && idx < SIZE) slots[idx] = "BACK"

        val title = when {
            type.name.contains("TRAILS") -> "Treasure chest"
            type.name.contains("SET_OF_ARMOUR") -> "Magic wardrobe"
            type.name.contains("ARMOUR_CASE") -> "Armour case"
            type.name.contains("BOOK") -> "Bookcase"
            else -> type.name.lowercase().replaceFirstChar(Char::titlecase) + " box"
        }
        sendString(player, title, INTERFACE, 225)

        val itemsArray = slots.mapNotNull {
            when (it) {
                is Storable -> Item(it.displayId)
                "MORE" -> Item(BUTTON_MORE)
                "BACK" -> Item(BUTTON_BACK)
                else -> null
            }
        }.toTypedArray()

        PacketRepository.send(
            ContainerPacket::class.java,
            ContainerContext(player, INTERFACE, COMPONENT, SIZE, itemsArray, false)
        )

        repeat(SIZE) { i ->
            val nameComponent = 55 + i * 2
            val iconComponent = 165 + i * 2
            val hiddenIconComponent = 166 + i * 2

            val obj = slots[i]
            val (name, hidden) = when (obj) {
                is Storable -> getItemName(obj.displayId) to (obj.displayId !in stored)
                "MORE" -> "More..." to false
                "BACK" -> "Back..." to false
                else -> "" to true
            }

            sendString(player, name, INTERFACE, nameComponent)
            sendInterfaceConfig(player, INTERFACE, nameComponent, false)

            if (obj is Storable) {
                sendInterfaceConfig(player, INTERFACE, iconComponent, hidden)
                sendInterfaceConfig(player, INTERFACE, hiddenIconComponent, !hidden)
            } else {
                sendInterfaceConfig(player, INTERFACE, iconComponent, true)
                sendInterfaceConfig(player, INTERFACE, hiddenIconComponent, true)
            }
        }
    }

    private fun openStorageForType(player: Player, type: StorableType) {
        setAttribute(player, "con:storage:type", type.name)
        openInterface(player, INTERFACE)
        updateStorageInterface(player, type)
    }
}
