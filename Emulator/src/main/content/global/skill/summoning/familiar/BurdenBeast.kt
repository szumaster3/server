package content.global.skill.summoning.familiar

import content.global.skill.summoning.SummoningPouch
import core.api.sendMessage
import core.game.component.CloseEvent
import core.game.component.Component
import core.game.container.Container
import core.game.container.access.InterfaceContainer
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.config.ItemConfigParser
import org.rs.consts.Components
import org.rs.consts.Items

/**
 * A familiar that can carry items on behalf of the player.
 */
abstract class BurdenBeast : Familiar {

    /**
     * The container used to store items carried by the familiar.
     */
    val container: Container

    /**
     * Primary constructor for initializing the burden beast with all properties.
     *
     * @param owner The owning player.
     * @param id The NPC id of the familiar.
     * @param ticks The lifetime of the familiar in ticks.
     * @param pouchId The id of the summoning pouch used to summon this familiar.
     * @param specialCost The special move cost.
     * @param containerSize The capacity of the storage container.
     * @param attackStyle The familiar's attack style.
     */
    constructor(
        owner: Player, id: Int, ticks: Int, pouchId: Int, specialCost: Int, containerSize: Int, attackStyle: Int
    ) : super(owner, id, ticks, pouchId, specialCost, attackStyle) {
        this.container = Container(containerSize).register(BurdenContainerListener(owner))
    }

    /**
     * Secondary constructor that defaults to a defensive attack style.
     */
    constructor(owner: Player, id: Int, ticks: Int, pouchId: Int, specialCost: Int, containerSize: Int) : this(
        owner, id, ticks, pouchId, specialCost, containerSize, WeaponInterface.STYLE_DEFENSIVE
    )

    /**
     * Dismisses the familiar and drops all stored items on the ground.
     */
    override fun dismiss() {
        if (owner.interfaceManager.hasMainComponent(Components.LORE_BANK_671)) {
            owner.interfaceManager.close()
        }
        for (item in container.toArray()) {
            item?.let {
                GroundItemManager.create(GroundItem(it, location, 500, owner))
            }
        }
        container.clear()
        super.dismiss()
    }

    /**
     * @return True since this is a burden beast.
     */
    override fun isBurdenBeast(): Boolean = true

    /**
     * @return True, as burden beasts are immune to poison.
     */
    override fun isPoisonImmune(): Boolean = true

    /**
     * Checks whether the given item is allowed to be stored in this familiar.
     *
     * @param owner The owning player.
     * @param item The item to check.
     * @return `True` if the item is allowed, `false` otherwise.
     */
    fun isAllowed(owner: Player, item: Item): Boolean {
        if (item.value > 50_000) {
            sendMessage(owner, "This item is too valuable to trust to this familiar.")
            return false
        }
        if (!item.definition.isTradeable) {
            sendMessage(owner, "You can't trade this item, not even to your familiar.")
            return false
        }
        val pouch = SummoningPouch.get(pouchId)
        val isEssence = item.id == Items.RUNE_ESSENCE_1436 || item.id == Items.PURE_ESSENCE_7936
        if ((!pouch.abyssal && isEssence) || !item.definition.getConfiguration(ItemConfigParser.BANKABLE, true)) {
            sendMessage(owner, "You can't store ${item.name.lowercase()} in this familiar.")
            return false
        }
        if (pouch.abyssal) {
            if (!item.name.lowercase().contains("essence")) {
                sendMessage(owner, "You can only give unnoted essence to this familiar.")
                return false
            }
            if (item.id == Items.RUNE_ESSENCE_1437 || item.id == Items.PURE_ESSENCE_7937) {
                sendMessage(owner, "You can't give noted essence to this familiar.")
                return false
            }
        }
        return true
    }

    /**
     * Transfers an item to or from the familiar's storage container.
     *
     * @param item The item to transfer.
     * @param amount The quantity to transfer.
     * @param withdraw True to withdraw from the familiar, false to store into it.
     */
    fun transfer(item: Item?, amount: Int, withdraw: Boolean) {
        if (this is Forager && !withdraw) {
            sendMessage(owner, "You can't store your items in this familiar.")
            return
        }
        if (item == null || owner == null) return

        if (!withdraw && !isAllowed(owner, Item(item.id, if (item.definition.isStackable()) amount else 1))) {
            return
        }

        val to = if (withdraw) owner.inventory else container
        val from = if (withdraw) container else owner.inventory
        var actualAmount = amount.coerceAtMost(from.getAmount(item)).coerceAtMost(to.getMaximumAdd(item))

        if (actualAmount < 1) {
            if (withdraw) {
                sendMessage(owner, "Not enough space in your inventory.")
            } else {
                sendMessage(owner, "Your familiar can't carry any more items.")
            }
            return
        }

        if (!item.definition.isStackable() && item.slot > -1) {
            from.replace(null, item.slot)
            to.add(Item(item.id, 1))
            actualAmount--
        }

        if (actualAmount > 0) {
            val transferItem = Item(item.id, actualAmount)
            if (from.remove(transferItem)) {
                to.add(transferItem)
            }
        }
    }

    /**
     * Withdraws all items from the familiar's container into the player's inventory.
     */
    fun withdrawAll() {
        for (i in 0 until container.capacity()) {
            val item = container[i] ?: continue
            val max = owner.inventory.getMaximumAdd(item)
            if (item.amount > max) {
                val reduced = Item(item.id, max)
                container.remove(reduced, false)
                owner.inventory.add(reduced, false)
            } else {
                container.replace(null, i, false)
                owner.inventory.add(item, false)
            }
        }
        container.update()
        owner.inventory.update()
    }

    /**
     * Opens the burden beast interface allowing item transfers.
     */
    fun openInterface() {
        if (container.itemCount() == 0 && this is Forager) {
            sendMessage(owner, "Your familiar is not carrying any items that you can withdraw.")
            return
        }
        owner.interfaceManager.open(Component(Components.LORE_BANK_671))?.closeEvent = CloseEvent { player, _ ->
            player.interfaceManager.closeSingleTab()
            return@CloseEvent true
        }

        container.shift()
        owner.interfaceManager.openSingleTab(Component(Components.LORE_BANK_SIDE_665))
        InterfaceContainer.generateItems(
            owner,
            owner.inventory.toArray(),
            arrayOf("Examine", "Store-X", "Store-All", "Store-10", "Store-5", "Store-1"),
            Components.LORE_BANK_SIDE_665,
            0,
            7,
            4,
            93
        )
        InterfaceContainer.generateItems(
            owner,
            container.toArray(),
            arrayOf("Examine", "Withdraw-X", "Withdraw-All", "Withdraw-10", "Withdraw-5", "Withdraw-1"),
            Components.LORE_BANK_671,
            27,
            5,
            6,
            30
        )
        container.refresh()
    }

}