package content.global.skill.runecrafting

import core.api.*
import core.game.global.action.DropListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items
import java.util.*

/**
 * Represents the different types of rune pouches for Runecrafting.
 *
 * @property pouch The item id of the pouch.
 * @property level Required Runecrafting level to use the pouch.
 * @property capacity Base essence capacity of the pouch.
 * @property totalCap Maximum combined capacity with upgrades.
 * @property uses Number of uses before decay.
 */
enum class RunePouch(val pouch: Int, val level: Int, private val capacity: Int, val totalCap: Int, val uses: Int) {
    SMALL(Items.SMALL_POUCH_5509, 1, 3, 3, 0),
    MEDIUM(Items.MEDIUM_POUCH_5510, 25, 6, 9, 45),
    LARGE(Items.LARGE_POUCH_5512, 50, 9, 18, 29),
    GIANT(Items.GIANT_POUCH_5514, 75, 12, 30, 10);

    /**
     * The decayed version of this pouch.
     */
    val decayedPouch = Item(pouch + 1)

    /**
     * Performs the selected action on this pouch.
     *
     * @param player The player using the pouch.
     * @param pouch The pouch item.
     * @param option Action type: fill, empty, check, drop.
     */
    fun action(player: Player, pouch: Item, option: String?) {
        if (pouch.charge == 1000 && getDecay(player) > 0) {
            resetDecay(player)
        }
        when (option) {
            "fill" -> fill(player, pouch)
            "empty" -> empty(player, pouch)
            "check" -> check(player, pouch)
            "drop" -> drop(player, pouch)
        }
    }

    /**
     * Fills the pouch with essence from the inventory.
     */
    fun fill(player: Player, pouch: Item) {
        if (isFull(pouch, player)) {
            return
        }

        if (!hasEssence(player)) {
            sendMessage(player, "You do not have any essence to fill your pouch with.")
            return
        }
        if (getStatLevel(player, Skills.RUNECRAFTING) < level) {
            sendMessage(
                player,
                "You need level $level Runecrafting to fill a " + name.lowercase(Locale.getDefault()) + " pouch.",
            )
            return
        }
        val essence = getEssence(player)
        if (!isValidEssence(pouch, essence, player)) {
            sendMessage(player, "You can only put " + getPouchEssenceName(pouch) + " in this pouch.")
        }
        val amount = getAddAmount(pouch, essence, player)
        addEssence(player, pouch, essence, amount)

        if (isFull(pouch, player)) {
        }
    }

    /**
     * Empties the essence from the pouch into the inventory.
     */
    fun empty(player: Player, pouch: Item) {
        if (isEmpty(pouch)) {
            sendMessage(player, "There are no essences in your pouch.")
            return
        }
        val essenceAmount = getEssence(pouch)
        var addAmount = essenceAmount

        if (player.inventory.freeSlots() < essenceAmount) {
            addAmount = essenceAmount - (essenceAmount - freeSlots(player))
        }
        val add = Item(getEssenceType(pouch).id, addAmount)

        if (!hasSpaceFor(player, add)) {
            sendMessage(player, "You do not have any more free space in your inventory.")
            return
        }
        if (player.inventory.add(add)) {
            incrementCharge(pouch, addAmount)

            if (essenceAmount != addAmount) {
                sendMessage(player, "You do not have any more free space in your inventory.")
            }
        }
    }

    /**
     * Checks for duplicate pouches in inventory/bank and removes extras.
     */
    fun checkDoubles(player: Player): Boolean {
        var hit = false
        for (item in values()) {
            if (player.inventory.getAmount(item.pouch) > 1 || player.bank.getAmount(item.pouch) > 1) {
                hit = true
                player.inventory.remove(Item(item.pouch, player.inventory.getAmount(item.pouch) - 1))
                player.bank.remove(Item(item.pouch, player.bank.getAmount(item.pouch)))
            }
        }
        return hit
    }

    /**
     * Sends a message with the amount of essence in the pouch.
     */
    fun check(player: Player, item: Item) {
        val amount = getEssence(item)
        sendMessage(
            player,
            if (amount == 0) {
                "There are no essences in this pouch."
            } else {
                "There " + (if (amount == 1) "is" else "are") + " " + amount + " " + getPouchEssenceName(
                    item,
                    amount,
                ) + " in this pouch."
            },
        )
    }

    /**
     * Drops the pouch and triggers any related effects.
     */
    fun drop(player: Player, item: Item) {
        onDrop(player, item)
        DropListener.drop(player, item)
    }

    /**
     * Called when a pouch is dropped to handle contents.
     */
    fun onDrop(player: Player, item: Item) {
        if (!isEmpty(item)) {
            resetCharge(item)
            sendMessage(player, "The contents of the pouch fell out as you dropped it!")
        }
    }

    /**
     * Adds essence to the pouch and updates its charge.
     */
    fun addEssence(player: Player, pouch: Item, essence: Item?, amount: Int) {
        val remove = Item(essence!!.id, amount)

        if (!player.inventory.containsItem(remove)) {
            return
        }

        if (player.inventory.remove(remove)) {
            var charge = getPouchCharge(pouch)
            if (charge == 1000) {
                charge = (if (isPureEssence(essence)) PURE_BASE else NORMAL_BASE)
                setCharge(pouch, charge)
            }
            if (isPureEssence(essence) && charge == NORMAL_BASE) {
                charge = PURE_BASE
            } else if (isNormalEssence(essence) && charge == PURE_BASE) {
                charge = NORMAL_BASE
            }
            charge -= amount
            setHash(pouch, charge)
            decay(player, pouch)
        }
    }

    /**
     * Handles decay for this pouch over use or time.
     */
    fun decay(player: Player, pouch: Item) {
        incrementDecay(player)

        if (getDecay(player) >= uses) {
            var message = ""
            if (!isDecayed(pouch)) {
                var decrementAmount = 0
                decrementAmount = if (decayAmount > getEssence(pouch)) {
                    decayAmount - (decayAmount - getEssence(pouch))
                } else {
                    getEssence(pouch) - (getEssence(pouch) - decayAmount)
                }
                incrementCharge(pouch, decrementAmount)
                message = "Your pouch has decayed through use."
                player.inventory.replace(Item(decayedPouch.id, pouch.amount, pouch.charge), pouch.slot)
            } else {
                message = "Your pouch has decayed beyond any further use."
                player.inventory.remove(pouch)
            }
            resetDecay(player)
            player.sendMessage(message)
        }
    }

    /**
     * Repairs a decayed pouch.
     */
    fun repair(player: Player, pouch: Item) {
        if (isDecayed(pouch)) {
            player.inventory.replace(Item(this.pouch, pouch.amount, pouch.charge), pouch.slot)
        }
        resetDecay(player)
    }

    /**
     * Increments the internal decay counter for this pouch.
     */
    fun incrementDecay(player: Player) {
        player.getSavedData().globalData.getRcDecays()[ordinal - 1]++
    }

    /**
     * Increments the pouches internal charge by a given amount.
     */
    fun incrementCharge(pouch: Item, chargeIncrement: Int) {
        setHash(pouch, getPouchCharge(pouch) + chargeIncrement)
    }

    /**
     * Decrements the pouches internal charge by a given amount.
     */
    fun decrementCharge(pouch: Item, chargeIncrement: Int) {
        setHash(pouch, getPouchCharge(pouch) - chargeIncrement)
    }

    /**
     * Sets the pouches internal charge.
     */
    fun setCharge(pouch: Item, charge: Int) {
        setHash(pouch, charge)
    }

    /**
     * Sets the pouches internal charge.
     */
    fun resetDecay(player: Player) {
        player.getSavedData().globalData.getRcDecays()[ordinal - 1] = 0
    }

    /**
     * Resets the pouches charge to full.
     */
    fun resetCharge(pouch: Item) {
        setHash(pouch, 1000)
    }

    /**
     * Sets the internal hash/charge for the pouch item.
     */
    fun setHash(pouch: Item, charge: Int) {
        pouch.charge = charge
    }

    /**
     * Gets the current decay count for this pouch.
     */
    fun getDecay(player: Player): Int = player.getSavedData().globalData.getRcDecay(ordinal - 1)

    /**
     * Gets the current charge of this pouch.
     */
    fun getPouchCharge(pouch: Item): Int = pouch.charge

    /**
     * Checks if the pouch is full of essence.
     */
    fun isFull(item: Item): Boolean = getEssence(item) >= getCapacity(pouch.asItem())

    /**
     * Calculates how much essence can be added to the pouch.
     */
    fun getAddAmount(pouch: Item, essence: Item?, player: Player): Int {
        val essyAmount = player.inventory.getAmount(essence)
        val pouchAmount = getEssence(pouch)
        val maxAdd = getCapacity(pouch) - pouchAmount
        if (essyAmount > maxAdd) {
            return maxAdd
        } else if (essyAmount <= maxAdd) {
            return essyAmount
        }
        return 0
    }

    /**
     * Checks if the essence is valid for this pouch type.
     */
    fun isValidEssence(pouch: Item, essence: Item?, player: Player?): Boolean {
        if (isEmpty(pouch)) {
            return true
        }
        return getPouchEssenceName(pouch) == getEssenceName(essence)
    }

    /**
     * Checks if the pouch is full and sends a message to the player.
     */
    fun isFull(item: Item, player: Player): Boolean {
        if (isFull(item)) {
            player.sendMessage("Your pouch is full.")
            return true
        }
        return false
    }

    /**
     * Gets the pouches essence name with pluralization.
     */
    fun getPouchEssenceName(item: Item, amount: Int): String = getPouchEssenceName(item) + (if (amount > 1) "s" else "")

    /**
     * Gets the type of essence in the pouch.
     */
    fun getPouchEssenceName(item: Item): String {
        val charge = getPouchCharge(item)
        return if (charge > NORMAL_BASE) "pure essence" else "normal essence"
    }

    /**
     * Gets the item type of essence contained in this pouch.
     */
    fun getEssenceInPouch(pouch: Item): Item = if (getPouchEssenceName(pouch) == "pure essence") {
        PURE_ESSENCE
    } else {
        NORMAL_ESSENCE
    }

    /**
     * Gets the name of the essence item.
     */
    fun getEssenceName(essence: Item?): String = if (essence!!.id == PURE_ESSENCE.id) {
        "pure essence"
    } else {
        "normal essence"
    }

    /**
     * Gets the base essence value depending on pouch type.
     */
    fun getEssenceBase(item: Item): Int = if (getPouchEssenceName(item) == "pure essence") PURE_BASE else NORMAL_BASE

    /**
     * Gets the base essence value depending on pouch type.
     */
    fun isPureEssencePouch(pouch: Item): Boolean = getPouchEssenceName(pouch) == "pure essence"

    /**
     * Gets true if the given essence item is pure.
     */
    fun isPureEssence(essence: Item?): Boolean = essence!!.id == PURE_ESSENCE.id

    /**
     * Gets true if the given essence item is normal.
     */
    fun isNormalEssence(essence: Item?): Boolean = essence!!.id == NORMAL_ESSENCE.id

    /**
     * Gets the essence item type for this pouch.
     */
    fun getEssenceType(pouch: Item): Item = if (getPouchEssenceName(pouch) == "pure essence") {
        PURE_ESSENCE
    } else {
        NORMAL_ESSENCE
    }

    /**
     * Gets an essence item from the inventory.
     */
    fun getEssence(player: Player): Item? {
        if (player.inventory.containsItem(PURE_ESSENCE)) {
            return PURE_ESSENCE
        } else if (player.inventory.containsItem(NORMAL_ESSENCE)) {
            return NORMAL_ESSENCE
        }
        return null
    }

    /**
     * Checks if the player has any essence to fill the pouch.
     */
    fun hasEssence(player: Player): Boolean =
        player.inventory.containsItem(PURE_ESSENCE) || player.inventory.containsItem(
            NORMAL_ESSENCE,
        )

    /**
     * Checks if the player has any essence to fill the pouch.
     */
    fun isEmpty(item: Item): Boolean = getEssence(item) <= 0

    /**
     * Gets the current amount of essence in the pouch.
     */
    fun getEssence(item: Item): Int {
        if (getPouchCharge(item) == 1000 || getPouchCharge(item) == 2000) {
            return 0
        }
        return getEssenceBase(item) - getPouchCharge(item)
    }

    /**
     * Gets the maximum capacity of the pouch.
     */
    fun getCapacity(pouch: Item): Int = capacity - (if (isDecayed(pouch)) decayAmount else 0)

    /**
     * Gets the amount the pouch decays per use.
     */
    val decayAmount: Int
        get() = if (this == GIANT) {
            3
        } else if (this == LARGE) {
            2
        } else {
            1
        }

    /**
     * Checks if this pouch has decayed.
     */
    fun isDecayed(pouch: Item): Boolean = pouch.id == decayedPouch.id

    /**
     * Checks if the pouch can decay over time.
     */
    val isDecayable: Boolean
        get() = this != SMALL

    /**
     * Gets true if the pouch has any decay or is decayed.
     */
    fun hasDecay(
        player: Player,
        pouch: Item,
    ): Boolean = getDecay(player) > 0 || isDecayed(pouch)

    companion object {
        private val PURE_ESSENCE = Item(Items.PURE_ESSENCE_7936)
        private val NORMAL_ESSENCE = Item(Items.RUNE_ESSENCE_1436)
        private const val PURE_BASE = 6000
        private const val NORMAL_BASE = 2000

        /**
         * Gets the [RunePouch] corresponding to a given item.
         */
        @JvmStatic
        fun forItem(pouch: Item): RunePouch? {
            for (p in values()) {
                if (p.pouch == pouch.id || (p != SMALL && p.decayedPouch.id == pouch.id)) {
                    return p
                }
            }
            return null
        }
    }
}
