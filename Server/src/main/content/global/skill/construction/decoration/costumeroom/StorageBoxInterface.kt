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
 * the costume room storage.
 */
class StorageBoxInterface : InterfaceListener {

    companion object {
        private const val INTERFACE = 467
        private const val COMPONENT = 164
        private const val SIZE = 30
        private const val PAGE_SIZE = SIZE - 1
        private const val BUTTON_MORE = Items.MORE_10165
        private const val BUTTON_BACK = Items.BACK_10166

        lateinit var instance: StorageBoxInterface
            private set

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
            val type = StorableType.valueOf(typeName)
            handleStorageInteraction(player, buttonId, type)
            true
        }
    }

    private fun getStorageContainer(player: Player, type: StorableType) =
        player.getCostumeRoomState().getContainer(type)

    private fun handleStorageInteraction(player: Player, buttonId: Int, type: StorableType) {
        val container = getStorageContainer(player, type)
        val tier = container.getTier(type)

        val allItems = StorableRepository.getItems(type, tier)
        val pageIndex = container.getPageIndex(type)

        val pageItems = allItems.drop(pageIndex * PAGE_SIZE).take(PAGE_SIZE)
        val slots = MutableList<Any?>(SIZE) { null }

        var idx = 0
        pageItems.forEach { if (idx < SIZE) slots[idx++] = it }

        if (pageIndex > 0 && idx < SIZE) slots[idx++] = "BACK"
        if ((pageIndex + 1) * PAGE_SIZE < allItems.size && idx < SIZE) slots[idx++] = "MORE"

        val slotIndex = when {
            buttonId in 56..(56 + (SIZE - 1) * 2) step 2 -> (buttonId - 56) / 2
            buttonId in 165..223 step 2 -> (buttonId - 165) / 2
            else -> return
        }

        when (val clicked = slots.getOrNull(slotIndex)) {
            "MORE" -> {
                container.nextPage(type, allItems.size, PAGE_SIZE)
                updateStorageInterface(player, type)
            }

            "BACK" -> {
                container.prevPage(type)
                updateStorageInterface(player, type)
            }

            is Storable -> processItemTransaction(player, clicked, type)
        }
    }

    private fun processItemTransaction(player: Player, item: Storable, type: StorableType) {
        val container = getStorageContainer(player, type)
        val storedItems = container.getItems(type).toSet()
        val actualId = item.takeIds.firstOrNull() ?: item.displayId

        if (actualId in storedItems) {
            if (freeSlots(player) <= 0) {
                sendMessage(player, "You don't have enough inventory space.")
                return
            }

            sendMessage(player, "You take the item from the ${boxName(type)}.")
            addItem(player, actualId, 1)
            container.withdraw(type, item)
        } else {
            if (!player.inventory.contains(actualId, 1)) {
                sendMessage(player, "You don't have that item in your inventory.")
                return
            }

            sendMessage(player, "You put the item into the box.")
            removeItem(player, Item(actualId))
            container.addItem(type, actualId)
        }

        updateStorageInterface(player, type)
    }

    private fun updateStorageInterface(player: Player, type: StorableType) {
        val container = getStorageContainer(player, type)
        val tier = container.getTier(type)

        val allItems = StorableRepository.getItems(type, tier)
        val stored = container.getItems(type).toSet()
        val pageIndex = container.getPageIndex(type)

        val pageItems = allItems.drop(pageIndex * PAGE_SIZE).take(PAGE_SIZE)
        val slots = MutableList<Any?>(SIZE) { null }

        var idx = 0
        pageItems.forEach { if (idx < SIZE) slots[idx++] = it }

        if (pageIndex > 0 && idx < SIZE) slots[idx++] = "BACK"
        if ((pageIndex + 1) * PAGE_SIZE < allItems.size && idx < SIZE) slots[idx++] = "MORE"

        sendString(player, boxTitle(player, type), INTERFACE, 225)

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

    private fun boxName(type: StorableType) = when (type) {
        StorableType.TRAILS -> "Treasure chest"
        StorableType.ARMOUR -> "Magic wardrobe"
        StorableType.ARMOUR_CASE -> "Armour case"
        StorableType.BOOK -> "Bookcase"
        StorableType.CAPE -> "Cape rack"
        StorableType.FANCY -> "Fancy dress box"
        StorableType.TOY -> "Toy box"
    }

    private fun boxTitle(player: Player, type: StorableType): String {
        if (type == StorableType.TRAILS) {
            val tier = getStorageContainer(player, type).getTier(type)
            return when (tier) {
                1 -> "Low-level Treasure Trail rewards"
                2 -> "Medium-level Treasure Trail rewards"
                3 -> "High-level Treasure Trail rewards"
                else -> "Low-level Treasure Trail rewards"
            }
        }
        return boxName(type)
    }
}
