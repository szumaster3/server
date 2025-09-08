package content.global.skill.construction.decoration.costumeroom

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository
import core.net.packet.out.ContainerPacket
import shared.consts.Items

class StorageBoxInterface : InterfaceListener {
    private val INTERFACE = 467
    private val CONTAINER_COMPONENT = 164
    private val TOTAL_SLOTS = 30
    private val PAGE_SIZE = TOTAL_SLOTS - 1
    private val BUTTON_MORE = Items.MORE_10165
    private val BUTTON_BACK = Items.BACK_10166

    override fun defineInterfaceListeners() {
        on(INTERFACE) { player, _, _, buttonId, _, _ ->
            val typeName = getAttribute(player, "con:storage:type", null) as? String ?: return@on true
            val type = StorableType.valueOf(typeName.uppercase())
            handleStorageInteraction(player, buttonId, type)
            return@on true
        }
    }

    private fun getContainer(player: Player, type: StorableType) =
        player.getCostumeRoomState().getContainer(type)

    private fun getRelevantItems(type: StorableType): List<Storable> {
        val trailTiers = listOf(
            StorableType.LOW_LEVEL_TRAILS,
            StorableType.MED_LEVEL_TRAILS,
            StorableType.HIGH_LEVEL_TRAILS
        )
        val magicTiers = listOf(
            StorableType.ONE_SET_OF_ARMOUR,
            StorableType.TWO_SETS_OF_ARMOUR,
            StorableType.THREE_SETS_OF_ARMOUR,
            StorableType.FOUR_SETS_OF_ARMOUR,
            StorableType.FIVE_SETS_OF_ARMOUR,
            StorableType.SIX_SETS_OF_ARMOUR
        )
        val armourTiers = listOf(
            StorableType.TWO_SETS_ARMOUR_CASE,
            StorableType.FOUR_SETS_ARMOUR_CASE,
            StorableType.ALL_SETS_ARMOUR_CASE
        )

        return Storable.values().filter {
            when (type) {
                StorableType.LOW_LEVEL_TRAILS -> it.type == type
                StorableType.MED_LEVEL_TRAILS -> it.type in trailTiers.take(2)
                StorableType.HIGH_LEVEL_TRAILS -> it.type in trailTiers

                StorableType.ONE_SET_OF_ARMOUR -> it.type == StorableType.ONE_SET_OF_ARMOUR
                StorableType.TWO_SETS_OF_ARMOUR -> it.type in magicTiers.take(2)
                StorableType.THREE_SETS_OF_ARMOUR -> it.type in magicTiers.take(3)
                StorableType.FOUR_SETS_OF_ARMOUR -> it.type in magicTiers.take(4)
                StorableType.FIVE_SETS_OF_ARMOUR -> it.type in magicTiers.take(5)
                StorableType.SIX_SETS_OF_ARMOUR -> it.type in magicTiers
                StorableType.ALL_SETS_OF_ARMOUR -> it.type in magicTiers

                StorableType.TWO_SETS_ARMOUR_CASE -> it.type in armourTiers.take(1)
                StorableType.FOUR_SETS_ARMOUR_CASE -> it.type in armourTiers.take(2)
                StorableType.ALL_SETS_ARMOUR_CASE -> it.type in armourTiers

                else -> it.type == type
            }
        }
    }

    private fun handleStorageInteraction(player: Player, buttonId: Int, type: StorableType) {
        val container = getContainer(player, type)
        val allItems = getRelevantItems(type)
        val pageIndex = container.getPageIndex(type)

        val pageItems = allItems.drop(pageIndex * PAGE_SIZE).take(PAGE_SIZE)
        val slots = MutableList<Any?>(TOTAL_SLOTS) { null }

        var idx = 0
        pageItems.forEach {
            if (idx >= TOTAL_SLOTS) return@forEach
            slots[idx++] = it
        }

        val hasPrev = pageIndex > 0
        val hasNext = allItems.size > (pageIndex + 1) * PAGE_SIZE

        if (hasNext && idx < TOTAL_SLOTS) slots[idx++] = "MORE"
        if (hasPrev && idx < TOTAL_SLOTS) slots[idx] = "BACK"

        val slotIndex = when {
            buttonId in 56..(56 + (TOTAL_SLOTS - 1) * 2) step 2 -> (buttonId - 56) / 2
            buttonId in 165..223 step 2 -> (buttonId - 165) / 2
            else -> return
        }

        when (val clicked = slots.getOrNull(slotIndex)) {
            "MORE" -> {
                container.nextPage(type, allItems.size, PAGE_SIZE)
                openInterface(player, INTERFACE)
                renderPage(player, type)
            }
            "BACK" -> {
                container.prevPage(type)
                openInterface(player, INTERFACE)
                renderPage(player, type)
            }
            is Storable -> handleItemClick(player, clicked, type)
        }
    }

    private fun handleItemClick(player: Player, item: Storable, type: StorableType) {
        val container = getContainer(player, type)
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
        renderPage(player, type)
    }

    private fun renderPage(player: Player, type: StorableType) {
        val container = getContainer(player, type)
        val allItems = getRelevantItems(type)
        val stored = container.getItems(type).toSet()
        val pageIndex = container.getPageIndex(type)

        val pageItems = allItems.drop(pageIndex * PAGE_SIZE).take(PAGE_SIZE)
        val slots = MutableList<Any?>(TOTAL_SLOTS) { null }

        var idx = 0
        pageItems.forEach {
            if (idx >= TOTAL_SLOTS) return@forEach
            slots[idx++] = it
        }

        val hasPrev = pageIndex > 0
        val hasNext = allItems.size > (pageIndex + 1) * PAGE_SIZE

        if (hasNext && idx < TOTAL_SLOTS) slots[idx++] = "MORE"
        if (hasPrev && idx < TOTAL_SLOTS) slots[idx] = "BACK"

        val title = when {
            type.name.contains("TRAILS") -> "Treasure chest"
            type.name.contains("SET_OF_ARMOUR") -> "Magic wardrobe"
            type.name.contains("ARMOUR_CASE") -> "Armour case"
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
            OutgoingContext.Container(player, INTERFACE, CONTAINER_COMPONENT, TOTAL_SLOTS, itemsArray, false)
        )

        repeat(TOTAL_SLOTS) { i ->
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

    private fun openStorageInterface(player: Player, type: StorableType) {
        setAttribute(player, "con:storage:type", type.name)
        openInterface(player, INTERFACE)
        renderPage(player, type)
    }

    companion object {
        lateinit var instance: StorageBoxInterface
            private set

        fun openStorage(player: Player, type: StorableType) {
            instance.openStorageInterface(player, type)
        }
    }

    init {
        instance = this
    }
}
