package core.game.shops

import core.ServerConstants
import core.api.*
import core.api.item.itemDefinition
import core.game.component.Component
import core.game.container.Container
import core.game.container.ContainerEvent
import core.game.container.ContainerType
import core.game.event.ItemShopPurchaseEvent
import core.game.event.ItemShopSellEvent
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.shops.Shops.Companion.logShop
import core.game.system.config.ItemConfigParser
import core.game.world.GameWorld
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import org.rs.consts.Components
import org.rs.consts.Items
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * Represents a single item in a shop.
 *
 * @property itemId The id of the item.
 * @property amount The amount of the item in stock.
 * @property restockRate The number of ticks between restocks (Default to 100).
 */
data class ShopItem(
    var itemId: Int,
    var amount: Int,
    val restockRate: Int = 100,
)

/**
 * A listener for shop container updates, used to send interface updates to the player.
 *
 * @property player The player this listener is associated with.
 */
class ShopListener(
    val player: Player,
) : ContainerListener {
    var enabled = false

    /**
     * Called when the container changes.
     *
     * @param c The container being updated.
     * @param event The event describing what changed.
     */
    override fun update(
        c: Container?,
        event: ContainerEvent?,
    ) {
        PacketRepository.send(
            ContainerPacket::class.java,
            ContainerContext(player, -1, -1, 92, event!!.items, false, *event.slots),
        )
    }

    /**
     * Refreshes the entire container.
     *
     * @param c The container to refresh.
     */
    override fun refresh(c: Container?) {
        PacketRepository.send(
            ContainerPacket::class.java,
            ContainerContext(player, -1, -1, 92, c!!.toArray(), c.capacity(), false),
        )
    }
}

/**
 * Represents a shop that can be opened by players to buy and sell items.
 *
 * @property title The title of the shop shown in the interface.
 * @property stock The default stock items available in the shop.
 * @property general Whether the shop accepts any item from the player as sellable.
 * @property currency The item ID used as the shop's currency (default: coins).
 * @property highAlch Whether high alchemy pricing is applied.
 * @property forceShared Whether to force the shop to use a shared container across all players.
 */
class Shop(
    val title: String,
    val stock: Array<ShopItem>,
    val general: Boolean = false,
    val currency: Int = Items.COINS_995,
    val highAlch: Boolean = false,
    val forceShared: Boolean = false,
) {

    /**
     * Map of player UID or global key to stock containers.
     */
    val stockInstances = HashMap<Int, Container>()

    /**
     * Container for general (player-sellable) stock.
     */
    val playerStock = if (general) generalPlayerStock else Container(40, ContainerType.SHOP)

    /**
     * Map of players or global key to a flag indicating whether the shop should be restocked.
     */
    val needsUpdate = HashMap<Int, Boolean>()

    /**
     * Map of item ids to their restocking rates.
     */
    val restockRates = HashMap<Int, Int>()

    init {
        if (!getServerConfig().getBoolean(Shops.personalizedShops, false) || forceShared) {
            stockInstances[ServerConstants.SERVER_NAME.hashCode()] = generateStockContainer()
        }
    }

    /**
     * Opens the shop interface for the given player.
     *
     * @param player The player to open the shop for.
     */
    fun openFor(player: Player) {
        val cont = getContainer(player)
        sendString(player, title, 620, 22)
        setAttribute(player, "shop", this)
        setAttribute(player, "shop-cont", cont)
        openInterface(player, Components.SHOP_TEMPLATE_620)
        player.interfaceManager.openSingleTab(Component(Components.SHOP_TEMPLATE_SIDE_621))
        showTab(player, true)
        logShop("Opening shop [Title: $title, Player: ${player.username}]")
    }

    /**
     * Displays the shop tab (main or player stock).
     *
     * @param player The player viewing the shop.
     * @param main Whether to show the main stock (true) or the player stock (false).
     */
    fun showTab(
        player: Player,
        main: Boolean,
    ) {
        val cont = if (main) getAttribute<Container?>(player, "shop-cont", null) ?: return else playerStock

        if (!main) {
            cont.listeners.remove(listenerInstances[player.details.uid])
            playerStock.listeners.add(listenerInstances[player.details.uid])
        } else {
            playerStock.listeners.remove(listenerInstances[player.details.uid])
            cont.listeners.add(listenerInstances[player.details.uid])
        }

        val settings =
            IfaceSettingsBuilder()
                .enableOptions(0..9)
                .build()

        player.packetDispatch.sendIfaceSettings(
            settings,
            if (main) 23 else 24,
            Components.SHOP_TEMPLATE_620,
            0,
            cont.capacity(),
        )
        player.packetDispatch.sendRunScript(
            150,
            "IviiiIsssssssss",
            "",
            "",
            "",
            "",
            "Buy X",
            "Buy 10",
            "Buy 5",
            "Buy 1",
            "Value",
            -1,
            0,
            4,
            10,
            92,
            (620 shl 16) or if (main) 23 else 24,
        )
        player.packetDispatch.sendInterfaceConfig(620, 23, !main)
        player.packetDispatch.sendInterfaceConfig(620, 24, main)
        player.packetDispatch.sendInterfaceConfig(620, 29, !main)
        player.packetDispatch.sendInterfaceConfig(620, 25, main)
        player.packetDispatch.sendInterfaceConfig(620, 27, main)
        player.packetDispatch.sendInterfaceConfig(620, 26, false)

        if (!main) {
            playerStock.refresh()
        } else {
            cont.refresh()
        }

        setAttribute(player, "shop-main", main)
    }

    /**
     * Retrieves the appropriate shop container for a player.
     *
     * @param player The player for whom to retrieve the container.
     * @return The shop container.
     */
    fun getContainer(player: Player): Container {
        val container =
            if (getServerConfig().getBoolean(Shops.personalizedShops, false) && !forceShared) {
                stockInstances[player.details.uid] ?: generateStockContainer().also {
                    stockInstances[player.details.uid] = it
                }
            } else {
                stockInstances[ServerConstants.SERVER_NAME.hashCode()]
                    ?: generateStockContainer().also { stockInstances[ServerConstants.SERVER_NAME.hashCode()] = it }
            }

        val listener = listenerInstances[player.details.uid]

        if (listener != null && listener.player != player) {
            container.listeners.remove(listener)
        }

        if (listener == null || listener.player != player) {
            listenerInstances[player.details.uid] = ShopListener(player)
        }

        return container
    }

    /**
     * Generates a new container based on the initial stock defined in the shop.
     *
     * @return A newly populated stock container.
     */
    private fun generateStockContainer(): Container {
        val container = Container(40, ContainerType.SHOP)
        for (item in stock) {
            container.add(Item(item.itemId, item.amount))
            restockRates[item.itemId] = item.restockRate
        }

        return container
    }

    /**
     * Restocks all shop containers that have been flagged for update.
     * Adjusts item amount to match initial stock over time.
     */
    fun restock() {
        stockInstances.filter { needsUpdate[it.key] == true }.forEach { (player, cont) ->
            for (i in 0 until cont.capacity()) {
                if (cont[i] == null) continue
                if (stock.size < i + 1) break
                if (stock[i].restockRate == 0) continue
                if (GameWorld.ticks % stock[i].restockRate != 0) continue

                if (cont[i].amount < stock[i].amount) {
                    cont[i].amount++
                    cont.event.flag(i, cont[i])
                } else if (cont[i].amount > stock[i].amount) {
                    cont[i].amount--
                    cont.event.flag(i, cont[i])
                }
                if (cont[i].amount != stock[i].amount) needsUpdate[player] = true
            }
            cont.update()
        }
    }

    /**
     * Calculates the cost of buying an item from the shop.
     *
     * @param player The player attempting to buy the item.
     * @param slot The slot of the item within the container.
     * @return An [Item] representing the currency and price required to buy the item. Returns Item(-1, -1) if not found.
     */
    fun getBuyPrice(
        player: Player,
        slot: Int,
    ): Item {
        val isMainStock = getAttribute(player, "shop-main", true)
        val cont =
            if (isMainStock) getAttribute<Container?>(player, "shop-cont", null) ?: return Item(-1, -1) else playerStock
        val item = cont[slot]

        if (item == null) {
            player.sendMessage("That item doesn't appear to be there anymore. Please try again.")
            return Item(-1, -1)
        }

        val price =
            when (currency) {
                Items.TOKKUL_6529 -> item.definition.getConfiguration(ItemConfigParser.TOKKUL_PRICE, 1)
                Items.ARCHERY_TICKET_1464 -> item.definition.getConfiguration(ItemConfigParser.ARCHERY_TICKET_PRICE, 1)
                Items.CASTLE_WARS_TICKET_4067 ->
                    item.definition.getConfiguration(
                        ItemConfigParser.CASTLE_WARS_TICKET_PRICE,
                        1,
                    )

                else ->
                    getGPCost(
                        Item(item.id, 1),
                        if (isMainStock) stock[item.slot].amount else playerStock[slot].amount,
                        if (isMainStock) item.amount else playerStock[slot].amount,
                    )
            }

        return Item(currency, price)
    }

    /**
     * Calculates the price the shop will pay the player for selling an item.
     *
     * @param player The player attempting to sell the item.
     * @param slot The inventory slot of the item.
     * @return A [Pair] containing the target container (or null) and the price in currency as an [Item].
     *         Returns (null, Item(-1, -1)) if the item is invalid or unsellable.
     */
    fun getSellPrice(
        player: Player,
        slot: Int,
    ): Pair<Container?, Item> {
        val shopCont = getAttribute<Container?>(player, "shop-cont", null) ?: return Pair(null, Item(-1, -1))
        val item = player.inventory[slot]

        if (item == null) {
            player.debug("Inventory slot $slot does not contain an item!")
            player.sendMessage("That item doesn't seem to be there anymore. Please try again.")
            return Pair(null, Item(-1, -1))
        }

        val shopItemId =
            if (item.definition.isUnnoted()) {
                item.id
            } else {
                item.noteChange
            }
        var (isPlayerStock, shopSlot) = getStockSlot(shopItemId)

        val stockAmt =
            if (isPlayerStock) {
                0
            } else {
                if (shopSlot != -1) {
                    stock[shopSlot].amount
                } else {
                    0
                }
            }
        val currentAmt =
            if (isPlayerStock) {
                playerStock.getAmount(shopItemId)
            } else {
                if (shopSlot != -1) {
                    shopCont[shopSlot].amount
                } else {
                    isPlayerStock = true
                    0
                }
            }

        val price =
            when (currency) {
                Items.TOKKUL_6529 ->
                    (
                            item.definition.getConfiguration(
                                ItemConfigParser.TOKKUL_PRICE,
                                1,
                            ) / 10.0
                            ).toInt() // selling items authentically return 10x less tokkul (floored/truncated) than the item's shop price
                Items.ARCHERY_TICKET_1464 -> item.definition.getConfiguration(ItemConfigParser.ARCHERY_TICKET_PRICE, 1)
                Items.CASTLE_WARS_TICKET_4067 ->
                    item.definition.getConfiguration(
                        ItemConfigParser.CASTLE_WARS_TICKET_PRICE,
                        1,
                    )

                else -> getGPSell(Item(shopItemId, 1), stockAmt, currentAmt)
            }

        if (!general && stockAmt == 0 && shopSlot == -1) {
            return Pair(null, Item(-1, -1))
        }

        return Pair(if (isPlayerStock) playerStock else shopCont, Item(currency, price))
    }

    /**
     * Computes the adjusted buy price of an item based on current and stock amounts.
     *
     * @param item The item being priced.
     * @param stockAmount The original stock quantity of the item.
     * @param currentAmt The current amount of the item in the shop.
     * @return The computed GP cost for the item.
     */
    private fun getGPCost(
        item: Item,
        stockAmount: Int,
        currentAmt: Int,
    ): Int {
        var mod: Int
        mod =
            if (stockAmount == 0) {
                100
            } else if (currentAmt == 0) {
                130
            } else if (currentAmt >= stockAmount) {
                100
            } else {
                130 - (130 - 100) * currentAmt / stockAmount
            }
        if (mod < 1) mod = 1
        mod = max(100, min(130, mod))

        val price: Int = ceil(item.definition.value * mod.toDouble() / 100.0).toInt()

        return max(price, 1)
    }

    /**
     * Computes the amount of GP given to the player when selling an item.
     *
     * @param item The item being sold.
     * @param stockAmount The shop's desired stock quantity.
     * @param currentAmt The current quantity of the item in the shop.
     * @return The GP value the shop will give for this item (minimum 1).
     */
    private fun getGPSell(
        item: Item,
        stockAmount: Int,
        currentAmt: Int,
    ): Int {
        val base = item.definition.getAlchemyValue(highAlch)
        var overstock = currentAmt - stockAmount
        if (overstock < 0) {
            return base
        }
        if (overstock > 10) {
            overstock = 10
        }
        val price = (base - (item.definition.value * 0.03 * overstock)).roundToInt()
        if (price < 1) {
            return 1
        }
        return price
    }

    fun buy(
        player: Player,
        slot: Int,
        amount: Int,
    ): TransactionStatus {
        if (amount !in 1..Integer.MAX_VALUE) return TransactionStatus.Failure("Invalid amount: $amount")
        val isMainStock = getAttribute(player, "shop-main", false)
        if (!isMainStock && player.ironmanManager.isIronman) {
            sendDialogue(player, "As an ironman, you cannot buy from player stock in shops.")
            return TransactionStatus.Failure("Ironman buying from player stock")
        }
        val cont =
            if (isMainStock) {
                (
                        getAttribute<Container?>(player, "shop-cont", null)
                            ?: return TransactionStatus.Failure("Invalid shop-cont attr")
                        )
            } else {
                playerStock
            }
        val inStock = cont[slot]
        val item = Item(inStock.id, amount)
        if (inStock.amount < amount) {
            item.amount = inStock.amount
        }
        if (item.amount > player.inventory.getMaximumAdd(item)) {
            item.amount = player.inventory.getMaximumAdd(item)
        }
        if (inStock.amount == 0) {
            sendMessage(player, "There is no stock of that item at the moment.")
            return TransactionStatus.Failure("Shop item out of stock.")
        }
        if (isMainStock &&
            inStock.amount > stock[slot].amount &&
            (
                    !getServerConfig().getBoolean(
                        Shops.personalizedShops,
                        false,
                    ) ||
                            forceShared
                    ) &&
            player.ironmanManager.isIronman
        ) {
            sendDialogue(player, "As an ironman, you cannot buy overstocked items from shops.")
            return TransactionStatus.Failure("Ironman overstock purchase")
        }
        val cost = getBuyPrice(player, slot)
        if (cost.id == -1) {
            sendMessage(
                player,
                "This shop cannot sell that item.",
            ).also { return TransactionStatus.Failure("Shop cannot sell this item") }
        }
        if (currency == Items.COINS_995) {
            var amt = item.amount
            var inStockAmt = inStock.amount
            while (amt-- > 1) {
                cost.amount +=
                    getGPCost(
                        Item(item.id, 1),
                        if (isMainStock) stock[slot].amount else playerStock[slot].amount,
                        --inStockAmt,
                    )
            }
        } else {
            cost.amount = cost.amount * item.amount
        }
        if (inInventory(player, cost.id, cost.amount)) {
            if (removeItem(player, cost)) {
                if (item.amount == 0) {
                    item.amount = 1
                }
                if (!hasSpaceFor(player, item)) {
                    addItem(player, cost.id, cost.amount)
                    sendMessage(player, "You don't have enough inventory space to buy that many.")
                    return TransactionStatus.Failure("Not enough inventory space")
                }
                if (!isMainStock && cont[slot].amount - item.amount == 0) {
                    cont.remove(cont[slot], false)
                    cont.refresh()
                } else {
                    cont[slot].amount -= item.amount
                    cont.event.flag(slot, cont[slot])
                    cont.update()
                }
                addItem(player, item.id, item.amount)
                if (getServerConfig().getBoolean(Shops.personalizedShops, false)) {
                    needsUpdate[player.details.uid] = true
                } else {
                    needsUpdate[ServerConstants.SERVER_NAME.hashCode()] = true
                }
            }
        } else {
            when (currency) {
                Items.TOKKUL_6529 -> sendMessage(player, "You don't have enough tokkul to purchase that.")
                Items.ARCHERY_TICKET_1464 ->
                    sendMessage(
                        player,
                        "You only had enough money to buy some of the items you requested.",
                    )

                Items.CASTLE_WARS_TICKET_4067 ->
                    sendMessage(
                        player,
                        "You don't have enough castle wars tickets to purchase that.",
                    )

                else -> sendMessage(player, "You don't have enough money.")
            }
            return TransactionStatus.Failure("Not enough money in inventory")
        }
        player.dispatch(ItemShopPurchaseEvent(item.id, item.amount, cost))
        return TransactionStatus.Success()
    }

    fun sell(
        player: Player,
        slot: Int,
        amount: Int,
    ): TransactionStatus {
        if (amount !in 1..Integer.MAX_VALUE) return TransactionStatus.Failure("Invalid amount: $amount")
        val playerInventory = player.inventory[slot]
        if (playerInventory.id in intArrayOf(Items.COINS_995, Items.TOKKUL_6529, Items.ARCHERY_TICKET_1464)) {
            sendMessage(player, "You can't sell currency to a shop.")
            return TransactionStatus.Failure("Tried to sell currency - ${playerInventory.id}")
        }
        val item = Item(playerInventory.id, amount)
        val def = itemDefinition(item.id)

        if (def.hasDestroyAction()) {
            sendMessage(player, "You can't sell this item.")
            return TransactionStatus.Failure("Attempt to sell a destroyable - ${playerInventory.id}.")
        }

        if (!def.isTradeable) {
            sendMessage(player, "You can't sell this item.")
            return TransactionStatus.Failure("Attempt to sell an untradeable - ${playerInventory.id}.")
        }

        val (container, profit) = getSellPrice(player, slot)
        if (profit.amount == -1) {
            sendMessage(
                player,
                "This item can't be sold to this shop.",
            ).also {
                return TransactionStatus.Failure(
                    "Can't sell this item to this shop - ${playerInventory.id}, general: $general, price: $profit",
                )
            }
        }
        if (amount > player.inventory.getAmount(item.id)) {
            item.amount = player.inventory.getAmount(item.id)
        }

        val id = if (!item.definition.isUnnoted()) item.noteChange else item.id
        val (isPlayerStock, shopSlot) = getStockSlot(id)

        if (isPlayerStock && shopSlot == -1 && generalPlayerStock.freeSlots() == 0) {
            sendMessage(player, "The shop is too full to buy any more items")
            return TransactionStatus.Failure("Attempt to sell to full shop.")
        }

        if (currency == Items.COINS_995 && item.amount > 1) {
            var amt = item.amount
            var inStockAmt = container!![shopSlot]?.amount ?: playerStock.getAmount(id)
            while (amt-- > 1) {
                profit.amount +=
                    getGPSell(
                        Item(item.id, 1),
                        if (isPlayerStock) 0 else stock[shopSlot].amount,
                        ++inStockAmt,
                    )
            }
        } else {
            profit.amount = profit.amount * item.amount
        }

        if (removeItem(player, item)) {
            if (!hasSpaceFor(player, profit)) {
                sendMessage(player, "You don't have enough space to do that.")
                addItem(player, item.id, item.amount)
                return TransactionStatus.Failure("Did not have enough inventory space")
            }
            if (container == playerStock && getAttribute(player, "shop-main", false)) {
                showTab(player, false)
            } else if (!getAttribute(player, "shop-main", false) && container != playerStock) {
                showTab(player, true)
            }
            addItem(player, profit.id, profit.amount)
            if (!item.definition.isUnnoted()) {
                item.id = item.noteChange
            }
            container?.add(item)
            container?.refresh()
            if (getServerConfig().getBoolean(Shops.personalizedShops, false)) {
                needsUpdate[player.details.uid] = true
            } else {
                needsUpdate[ServerConstants.SERVER_NAME.hashCode()] = true
            }
        }

        player.dispatch(ItemShopSellEvent(item.id, item.amount, profit))
        return TransactionStatus.Success()
    }

    /**
     * Determines the stock slot and source (main stock or player stock) for a given item id.
     *
     * @param itemId The id of the item to locate in the stock.
     * @return A [Pair] where the first value is `true` if found in player stock, `false` if in main stock,
     *         and the second is the slot index where the item was found (or -1 if not found).
     */
    fun getStockSlot(itemId: Int): Pair<Boolean, Int> {
        var shopSlot: Int = -1
        var isPlayerStock = false
        val notechange = itemDefinition(itemId).noteId
        for ((stockSlot, shopItem) in stock.withIndex()) {
            if (shopItem.itemId == itemId || shopItem.itemId == notechange) {
                shopSlot = stockSlot
            }
        }
        if (shopSlot == -1) {
            for ((stockSlot, playerStockItem) in playerStock.toArray().withIndex()) {
                if (playerStockItem == null) continue
                if (playerStockItem.id == itemId || playerStockItem.id == notechange) {
                    shopSlot = stockSlot
                    isPlayerStock = true
                }
            }
        }

        if (shopSlot == -1) isPlayerStock = true
        return Pair(isPlayerStock, shopSlot)
    }

    companion object {
        /**
         * Shared container used by general stores for storing player-sold items.
         * This is a globally shared inventory across all general stores.
         */
        val generalPlayerStock = Container(40, ContainerType.SHOP)

        /**
         * A map of shop IDs to their corresponding [ShopListener] instances.
         * Used to track active listeners per shop.
         */
        val listenerInstances = HashMap<Int, ShopListener>()
    }

    /**
     * Represents the result of a shop transaction.
     */
    sealed class TransactionStatus {

        /**
         * Indicates that the transaction was completed successfully.
         */
        class Success : TransactionStatus()

        /**
         * Indicates that the transaction failed.
         *
         * @param reason The reason why the transaction could not be completed.
         */
        class Failure(
            val reason: String,
        ) : TransactionStatus()
    }
}
