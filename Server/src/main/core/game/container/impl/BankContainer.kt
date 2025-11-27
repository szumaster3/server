package core.game.container.impl

import core.ServerConstants
import core.api.*
import core.game.component.CloseEvent
import core.game.component.Component
import core.game.container.Container
import core.game.container.ContainerEvent
import core.game.container.ContainerType
import core.game.container.SortType
import core.game.container.access.InterfaceContainer.generateItems
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.config.ItemConfigParser
import core.game.world.GameWorld.settings
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import shared.consts.Components
import shared.consts.Vars

/**
 * Represents the bank container.
 * @author Emperor
 */
class BankContainer(private val player: Player) : Container(SIZE, ContainerType.ALWAYS_STACK, SortType.HASH) {

    /**
     * The bank listener.
     */
    private var listener: BankListener? = null

    /**
     * If the bank is open.
     */
    var isOpen: Boolean = false
        private set

    /**
     * The last x-amount entered.
     */
    var lastAmountX: Int = 50
        private set

    /**
     * The current tab index.
     */
    private var tabIndex = 10

    /**
     * The tab start indexes.
     */
    val tabStartSlot = IntArray(TAB_SIZE)

    /**
     * Construct a new `BankContainer` `Object`.
     * @param player The player reference.
     */
    init {
        register(BankListener(player).also { listener = it })
    }

    /**
     * Opens the bank interface for the player.
     */
    fun open() = open(player) {
        player.interfaceManager.openSingleTab(Component(BANK_V2_MAIN_SIDE))
        player.inventory.refresh()
        player.inventory.listeners.add(listener)
        player.packetDispatch.sendIfaceSettings(
            IfaceSettingsBuilder()
                .enableOptions(IntRange(0, 5))
                .enableExamine()
                .enableSlotSwitch()
                .build(),
            0, BANK_V2_MAIN_SIDE, 0, 27
        )
        setVarp(player, 1249, lastAmountX)
    }

    /**
     * Opens the bank interface for a player.
     */
    fun open(p: Player) = open(p) {
        refresh(listener)
        p.interfaceManager.openSingleTab(Component(BANK_V2_MAIN_SIDE))
        p.inventory.listeners.add(p.bank.listener)
        p.inventory.refresh()
        setVarp(p, 1249, lastAmountX)
        p.packetDispatch.sendIfaceSettings(1278, 73, BANK_V2_MAIN, 0, SIZE)
        p.packetDispatch.sendIfaceSettings(
            IfaceSettingsBuilder()
                .enableOptions(IntRange(0, 5))
                .enableExamine()
                .enableSlotSwitch()
                .build(),
            0, BANK_V2_MAIN_SIDE, 0, 27
        )
        p.packetDispatch.sendRunScript(1451, "")
    }

    /**
     * Internal helper to handle opening logic and restrictions.
     */
    private inline fun open(p: Player, extra: () -> Unit) {
        if (isOpen) return
        if (!p.bankPinManager.isUnlocked && !settings!!.isDevMode) {
            p.bankPinManager.openType(1)
            return
        }

        p.interfaceManager.openComponent(BANK_V2_MAIN)!!.closeEvent = CloseEvent { _: Player?, _: Component? ->
            this@BankContainer.close()
            true
        }

        super.refresh()
        p.inventory.refresh()
        extra()
        isOpen = true
    }

    /**
     * Opens the deposit box interface.
     */
    fun openDepositBox() {
        player.interfaceManager.open(Component(DEPOSIT_BOX))!!.closeEvent = CloseEvent { p: Player, _: Component? ->
            p.interfaceManager.openDefaultTabs()
            true
        }
        player.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6)
        refreshDepositBoxInterface()
    }

    /**
     * Refreshes deposit box interface buttons.
     */
    fun refreshDepositBoxInterface() {
        player.generateItems(DEPOSIT_BOX, 15, listOf("Deposit-X", "Deposit-All", "Deposit-10", "Deposit-5", "Deposit-1"), 5, 7)
    }

    /**
     * Closes the bank.
     */
    fun close() {
        isOpen = false
        player.inventory.listeners.remove(listener)
        player.interfaceManager.closeSingleTab()
        player.removeAttribute("search")
        player.packetDispatch.sendRunScript(571, "")
    }

    /**
     * Adds an item to the bank container.
     * @param slot The item slot.
     * @param amt The amount.
     */
    fun addItem(slot: Int, amt: Int) {
        var amount = amt
        if (slot < 0 || slot > player.inventory.capacity() || amount < 1) return
        val invItem = player.inventory[slot] ?: return

        if (!invItem.definition.getConfiguration(ItemConfigParser.BANKABLE, true)) {
            sendMessage(player, "A magical force prevents you from banking this item.")
            return
        }

        val maximum = player.inventory.getAmount(invItem)
        if (amount > maximum) amount = maximum

        val itemToRemove = Item(invItem.id, amount, invItem.charge)
        val unnote = !invItem.definition.isUnnoted()
        var add = if (unnote) Item(invItem.definition.getNoteId(), amount, invItem.charge) else itemToRemove
        if (unnote && !add.definition.isUnnoted()) add = itemToRemove

        val maxCount = getMaximumAdd(add)
        if (amount > maxCount) {
            add.amount = maxCount
            itemToRemove.amount = maxCount
            if (maxCount < 1) {
                player.packetDispatch.sendMessage("There is not enough space left in your bank.")
                return
            }
        }

        if (player.inventory.remove(itemToRemove, slot, false)) {
            var preferredSlot = -1
            if (tabIndex != 0 && tabIndex != 10 && !contains(add.id, 1)) {
                preferredSlot = tabStartSlot[tabIndex] + getItemsInTab(tabIndex)
                insert(freeSlot(), preferredSlot, false)
                increaseTabStartSlots(tabIndex)
            }
            add(add, true, preferredSlot)
            player.inventory.update()
        }
    }

    /**
     * Takes a item from the bank container and adds one to the inventory
     * container.
     * @param slot The slot.
     * @param amt The amount.
     */
    fun takeItem(slot: Int, amt: Int) {
        var amount = amt
        if (slot < 0 || slot > capacity() || amount <= 0) return
        val bankItem = get(slot) ?: return
        if (amount > bankItem.amount) amount = bankItem.amount
        val item = Item(bankItem.id, amount, bankItem.charge)

        val noteId = item.definition.getNoteId()
        var add = if (isNoteItems && noteId > 0) Item(noteId, amount, item.charge) else item
        val maxCount = player.inventory.getMaximumAdd(add)
        if (amount > maxCount) {
            item.amount = maxCount
            add.amount = maxCount
            if (maxCount < 1) {
                sendMessage(player, "Not enough space in your inventory.")
                return
            }
        }

        if (isNoteItems && noteId < 0) {
            sendMessage(player, "This item can't be withdrawn as a note.")
            add = item
        }

        if (remove(item, slot, false)) {
            player.inventory.add(add, false)
        }

        if (get(slot) == null) {
            val tabId = getTabByItemSlot(slot)
            decreaseTabStartSlots(tabId)
            shift()
        } else update()

        player.inventory.update()
    }

    /**
     * Updates the last x-amount entered.
     * @param amount The amount to set.
     */
    fun updateLastAmountX(amount: Int) {
        lastAmountX = amount
        setVarp(player, 1249, amount)
    }

    /**
     * Gets the tab the item slot is in.
     * @param itemSlot The item slot.
     * @return The tab index.
     */
    fun getTabByItemSlot(itemSlot: Int): Int {
        var low = 0
        var high = tabStartSlot.size - 1
        var result = 0

        while (low <= high) {
            val mid = (low + high) / 2
            if (itemSlot >= tabStartSlot[mid]) {
                result = mid
                low = mid + 1
            } else {
                high = mid - 1
            }
        }
        return result
    }

    /**
     * Increases a tab's start slot.
     * @param startId The start id.
     */
    fun increaseTabStartSlots(startId: Int) {
        for (i in startId + 1 until tabStartSlot.size) tabStartSlot[i]++
    }

    /**
     * Decreases a tab's start slot.
     * @param startId The start id.
     */
    fun decreaseTabStartSlots(startId: Int) {
        if (startId == 10) return
        for (i in startId + 1 until tabStartSlot.size) tabStartSlot[i]--
        if (getItemsInTab(startId) == 0) collapseTab(startId)
    }

    /**
     * Sends the bank space values on the interface.
     */
    fun sendBankSpace() {
        setVarc(player, 192, capacity() - freeSlots())
    }

    /**
     * Collapses a tab.
     * @param tabId The tab index.
     */
    fun collapseTab(tabId: Int) {
        val size = getItemsInTab(tabId)
        if (size <= 0) return

        val temp = Array<Item?>(size) { i -> get(tabStartSlot[tabId] + i) }
        for (i in 0 until size) replace(null, tabStartSlot[tabId] + i, false)
        shift()

        for (i in tabId until tabStartSlot.size - 1) {
            tabStartSlot[i] = tabStartSlot[i + 1] - size
        }
        tabStartSlot[10] -= size

        for (i in 0 until size) {
            val s = freeSlot()
            replace(temp[i], s, false)
        }
        refresh() //We only refresh once.
    }

    /**
     * Sets the tab configs.
     */
    fun setTabConfigurations() {
        for (i in 0..7) setVarbit(player, 4885 + i, getItemsInTab(i + 1))
    }

    /**
     * Gets the amount of items in one tab.
     * @param tabId The tab index.
     * @return The amount of items in this tab.
     */
    fun getItemsInTab(tabId: Int): Int = tabStartSlot[tabId + 1] - tabStartSlot[tabId]

    /**
     * Checks if the item can be added.
     * @param item the item.
     * @return `True` if so.
     */
    fun canAdd(item: Item): Boolean = item.definition.getConfiguration(ItemConfigParser.BANKABLE, true)

    var isNoteItems: Boolean
        /**
         * If items have to be noted.
         * @return If items have to be noted `true`.
         */
        get() = getVarbit(player, Vars.VARBIT_BANK_WITHDRAW_MODE_3958) == 1
        /**
         * Set if items have to be noted.
         * @param noteItems If items have to be noted `true`.
         */
        set(noteItems) {
            setVarbit(player, Vars.VARBIT_BANK_WITHDRAW_MODE_3958, if (noteItems) 1 else 0, true)
        }

    /**
     * Gets the tabIndex value.
     * @return The tabIndex.
     */
    fun getTabIndex(): Int = tabIndex

    /**
     * Sets the tabIndex value.
     * @param tabIndex The tabIndex to set.
     */
    fun setTabIndex(tabIndex: Int) {
        this.tabIndex = if (tabIndex == 0) 10 else tabIndex
        setVarbit(player, 4893, tabIndex + 1)
        setAttribute(player, "bank:lasttab", tabIndex)
    }

    var isInsertItems: Boolean
        /**
         * Gets the insert items value.
         * @return `True` if inserting items mode is enabled.
         */
        get() = getVarp(player, Vars.VARP_IFACE_BANK_INSERT_MODE_304) == 1
        /**
         * Sets the insert items value.
         * @param insertItems The insert items value.
         */
        set(insertItems) = setVarp(player, Vars.VARP_IFACE_BANK_INSERT_MODE_304, if (insertItems) 1 else 0)

    /**
     * Listens to the bank container.
     * @author Emperor
     */
    private inner class BankListener(private val p: Player) : ContainerListener {
        override fun update(c: Container?, event: ContainerEvent?) {
            if (event == null) return
            val isMain = c is BankContainer
            val iface = if (isMain) BANK_V2_MAIN else BANK_V2_MAIN_SIDE
            val containerType = if (isMain) MAIN_CONTAINER_TYPE else SIDE_CONTAINER_TYPE

            sendForUpdate(iface, containerType, event.items, *event.slots)
            refreshBankState()
        }

        override fun refresh(c: Container?) {
            val isMain = c is BankContainer
            val iface = if (isMain) BANK_V2_MAIN else BANK_V2_MAIN_SIDE
            val containerType = if (isMain) MAIN_CONTAINER_TYPE else SIDE_CONTAINER_TYPE
            val items = c?.toArray()?.copyOf() ?: emptyArray()
            val length = if (isMain) c?.capacity() ?: 0 else SIDE_DEFAULT_CAPACITY

            sendForRefresh(iface, containerType, items, length)
            refreshBankState()
        }

        private fun sendForUpdate(iface: Int, containerType: Int, items: Array<Item>, vararg slots: Int) {
            // constructor: (player, interfaceId, childId, containerId, items, split, vararg slots)
            PacketRepository.send(
                ContainerPacket::class.java,
                ContainerContext(p, iface, BANK_CHILD_ID, containerType, items, false, *slots)
            )
        }

        private fun sendForRefresh(iface: Int, containerType: Int, items: Array<Item>, length: Int) {
            // constructor: (player, interfaceId, childId, containerId, items, length, split)
            PacketRepository.send(
                ContainerPacket::class.java,
                ContainerContext(p, iface, BANK_CHILD_ID, containerType, items, length, false)
            )
        }

        private fun refreshBankState() {
            player.bank.setTabConfigurations()
            player.bank.sendBankSpace()
        }
    }

    companion object {
        /**
         * The deposit box interface id.
         */
        const val DEPOSIT_BOX = Components.BANK_DEPOSIT_BOX_11

        /**
         * The main bank interface id.
         */
        const val BANK_V2_MAIN = Components.BANK_V2_MAIN_762

        /**
         * The bank side interface id.
         */
        const val BANK_V2_MAIN_SIDE = Components.BANK_V2_SIDE_763

        /**
         * The bank child id.
         */
        private const val BANK_CHILD_ID = 64000

        /**
         * The bank container size.
         */
        val SIZE: Int = ServerConstants.BANK_SIZE

        /**
         * The maximum amount of bank tabs
         */
        const val TAB_SIZE: Int = 11

        /**
         * The main inv size.
         */
        private const val MAIN_CONTAINER_TYPE = 95

        /**
         * The side inv size.
         */
        private const val SIDE_CONTAINER_TYPE = 93

        /**
         * The inv size
         */
        private const val SIDE_DEFAULT_CAPACITY = 28

        /**
         * Gets the array index for a tab.
         *
         * @param tabId The tab id.
         * @return The array index.
         */
        fun getArrayIndex(tabId: Int): Int {
            if (tabId == 41 || tabId == 74) {
                return 10
            }
            var base = 39
            for (i in 1..9) {
                if (tabId == base) {
                    return i
                }
                base -= 2
            }
            return -1
        }
    }
}
