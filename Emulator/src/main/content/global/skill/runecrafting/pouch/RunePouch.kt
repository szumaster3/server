package content.global.skill.runecrafting.pouch

import core.api.*
import core.game.global.action.DropListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import java.util.*

enum class RunePouch(
    val pouch: Int,
    val level: Int,
    private val capacity: Int,
    val totalCap: Int,
    val uses: Int,
) {
    SMALL(Items.SMALL_POUCH_5509, 1, 3, 3, 0),
    MEDIUM(Items.MEDIUM_POUCH_5510, 25, 6, 9, 45),
    LARGE(Items.LARGE_POUCH_5512, 50, 9, 18, 29),
    GIANT(Items.GIANT_POUCH_5514, 75, 12, 30, 10),
    ;

    val decayedPouch = Item(pouch + 1)

    fun action(
        player: Player,
        pouch: Item,
        option: String?,
    ) {
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

    fun fill(
        player: Player,
        pouch: Item,
    ) {
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
                "You need level " + level + " Runecrafting to fill a " +
                    name.lowercase(
                        Locale.getDefault(),
                    ) + " pouch.",
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

    fun empty(
        player: Player,
        pouch: Item,
    ) {
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

    fun check(
        player: Player,
        item: Item,
    ) {
        val amount = getEssence(item)
        sendMessage(
            player,
            if (amount ==
                0
            ) {
                "There are no essences in this pouch."
            } else {
                "There " + (if (amount == 1) "is" else "are") + " " +
                    amount +
                    " " +
                    getPouchEssenceName(
                        item,
                        amount,
                    ) + " in this pouch."
            },
        )
    }

    fun drop(
        player: Player,
        item: Item,
    ) {
        onDrop(player, item)
        DropListener.drop(player, item)
    }

    fun onDrop(
        player: Player,
        item: Item,
    ) {
        if (!isEmpty(item)) {
            resetCharge(item)
            sendMessage(player, "The contents of the pouch fell out as you dropped it!")
        }
    }

    fun addEssence(
        player: Player,
        pouch: Item,
        essence: Item?,
        amount: Int,
    ) {
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

    fun decay(
        player: Player,
        pouch: Item,
    ) {
        incrementDecay(player)

        if (getDecay(player) >= uses) {
            var message = ""
            if (!isDecayed(pouch)) {
                var decrementAmount = 0
                decrementAmount =
                    if (decayAmount > getEssence(pouch)) {
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

    fun repair(
        player: Player,
        pouch: Item,
    ) {
        if (isDecayed(pouch)) {
            player.inventory.replace(Item(this.pouch, pouch.amount, pouch.charge), pouch.slot)
        }
        resetDecay(player)
    }

    fun incrementDecay(player: Player) {
        player.getSavedData().globalData.getRcDecays()[ordinal - 1]++
    }

    fun incrementCharge(
        pouch: Item,
        chargeIncrement: Int,
    ) {
        setHash(pouch, getPouchCharge(pouch) + chargeIncrement)
    }

    fun decrementCharge(
        pouch: Item,
        chargeIncrement: Int,
    ) {
        setHash(pouch, getPouchCharge(pouch) - chargeIncrement)
    }

    fun setCharge(
        pouch: Item,
        charge: Int,
    ) {
        setHash(pouch, charge)
    }

    fun resetDecay(player: Player) {
        player.getSavedData().globalData.getRcDecays()[ordinal - 1] = 0
    }

    fun resetCharge(pouch: Item) {
        setHash(pouch, 1000)
    }

    fun setHash(
        pouch: Item,
        charge: Int,
    ) {
        pouch.charge = charge
    }

    fun getDecay(player: Player): Int {
        return player.getSavedData().globalData.getRcDecay(ordinal - 1)
    }

    fun getPouchCharge(pouch: Item): Int {
        return pouch.charge
    }

    fun isFull(item: Item): Boolean {
        return getEssence(item) >= getCapacity(pouch.asItem())
    }

    fun getAddAmount(
        pouch: Item,
        essence: Item?,
        player: Player,
    ): Int {
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

    fun isValidEssence(
        pouch: Item,
        essence: Item?,
        player: Player?,
    ): Boolean {
        if (isEmpty(pouch)) {
            return true
        }
        return getPouchEssenceName(pouch) == getEssenceName(essence)
    }

    fun isFull(
        item: Item,
        player: Player,
    ): Boolean {
        if (isFull(item)) {
            player.sendMessage("Your pouch is full.")
            return true
        }
        return false
    }

    fun getPouchEssenceName(
        item: Item,
        amount: Int,
    ): String {
        return getPouchEssenceName(item) + (if (amount > 1) "s" else "")
    }

    fun getPouchEssenceName(item: Item): String {
        val charge = getPouchCharge(item)
        return if (charge > NORMAL_BASE) "pure essence" else "normal essence"
    }

    fun getEssenceInPouch(pouch: Item): Item {
        return if (getPouchEssenceName(pouch) == "pure essence") PURE_ESSENCE else NORMAL_ESSENCE
    }

    fun getEssenceName(essence: Item?): String {
        return if (essence!!.id == PURE_ESSENCE.id) "pure essence" else "normal essence"
    }

    fun getEssenceBase(item: Item): Int {
        return if (getPouchEssenceName(item) == "pure essence") PURE_BASE else NORMAL_BASE
    }

    fun isPureEssencePouch(pouch: Item): Boolean {
        return getPouchEssenceName(pouch) == "pure essence"
    }

    fun isPureEssence(essence: Item?): Boolean {
        return essence!!.id == PURE_ESSENCE.id
    }

    fun isNormalEssence(essence: Item?): Boolean {
        return essence!!.id == NORMAL_ESSENCE.id
    }

    fun getEssenceType(pouch: Item): Item {
        return if (getPouchEssenceName(pouch) == "pure essence") PURE_ESSENCE else NORMAL_ESSENCE
    }

    fun getEssence(player: Player): Item? {
        if (player.inventory.containsItem(PURE_ESSENCE)) {
            return PURE_ESSENCE
        } else if (player.inventory.containsItem(NORMAL_ESSENCE)) {
            return NORMAL_ESSENCE
        }
        return null
    }

    fun hasEssence(player: Player): Boolean {
        return player.inventory.containsItem(PURE_ESSENCE) || player.inventory.containsItem(NORMAL_ESSENCE)
    }

    fun isEmpty(item: Item): Boolean {
        return getEssence(item) <= 0
    }

    fun getEssence(item: Item): Int {
        if (getPouchCharge(item) == 1000 || getPouchCharge(item) == 2000) {
            return 0
        }
        return getEssenceBase(item) - getPouchCharge(item)
    }

    fun getCapacity(pouch: Item): Int {
        return capacity - (if (isDecayed(pouch)) decayAmount else 0)
    }

    val decayAmount: Int
        get() =
            if (this == GIANT) {
                3
            } else if (this == LARGE) {
                2
            } else {
                1
            }

    fun isDecayed(pouch: Item): Boolean {
        return pouch.id == decayedPouch.id
    }

    val isDecayable: Boolean
        get() = this != SMALL

    fun hasDecay(
        player: Player,
        pouch: Item,
    ): Boolean {
        return getDecay(player) > 0 || isDecayed(pouch)
    }

    companion object {
        private val PURE_ESSENCE = Item(7936)
        private val NORMAL_ESSENCE = Item(1436)
        private const val PURE_BASE = 6000
        private const val NORMAL_BASE = 2000

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
