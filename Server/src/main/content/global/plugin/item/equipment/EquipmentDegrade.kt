package content.global.plugin.item.equipment

import core.api.addItemOrDrop
import core.api.getNext
import core.api.isLast
import core.api.isNextLast
import core.game.node.entity.player.Player
import core.game.node.item.Item

/**
 * Handles degradation of equipment items that have limited durability or charges.
 */
class EquipmentDegrade {
    companion object StaticDegrade {
        var p: Player? = null
        val itemList = arrayListOf<Int>()
        val setList = arrayListOf<ArrayList<Int>>()
        val itemCharges = hashMapOf<Int, Int>()

        /**
         * Registers a single degradable item with its maximum number of charges.
         *
         * @param charges The maximum number of charges the item can have.
         * @param item The item ID.
         */
        fun register(
            charges: Int,
            item: Int,
        ) {
            itemList.add(item)
            itemCharges[item] = charges
        }

        /**
         * Registers a degradable item set (e.g., Barrows armour stages), applying the same charge to all.
         *
         * @param charges The maximum number of charges each item in the set can have.
         * @param items An array of item IDs representing degradation stages.
         */
        fun registerSet(
            charges: Int,
            items: Array<Int>,
        ) {
            setList.add(ArrayList(items.map { item -> item.also { register(charges, item) } }))
        }
    }

    /**
     * Checks whether the item with this ID is degradable.
     *
     * @return `true` if the item is degradable, `false` otherwise.
     */
    fun Int.canDegrade(): Boolean = itemList.contains(this)

    /**
     * Checks all equipped armour items (except weapon slot) and degrades them if necessary.
     *
     * @param player The player whose armour is being checked.
     */
    fun checkArmourDegrades(player: Player?) {
        p = player
        for (slot in 0..12) {
            if (slot == 3) continue
            player?.equipment?.get(slot)?.let { if (it.id.canDegrade()) it.degrade(slot) }
        }
    }

    /**
     * Checks the equipped weapon and degrades it if necessary.
     *
     * @param player The player whose weapon is being checked.
     */
    fun checkWeaponDegrades(player: Player?) {
        p = player
        player?.equipment?.get(3)?.let { if (it.id.canDegrade()) it.degrade(3) }
    }

    /**
     * Degrades the current item if its charges are depleted. Handles progression through degradation stages
     * and item replacement or removal based on the item set.
     *
     * @param slot The equipment slot this item is currently in.
     */
    fun Item.degrade(slot: Int) {
        val set = getDegradableSet(this.id)
        val charges = itemCharges.getOrElse(this.id) { 1000 }
        if (this.charge > charges) {
            charge = charges
        }
        this.charge--
        if (set?.indexOf(this.id) == 0 && !this.name.contains("100")) {
            charge = 0
        }
        if (this.charge <= 0) {
            val charges = itemCharges.getOrElse(this.id) { 1000 }
            if (set?.size!! > 2) {
                p?.equipment?.remove(this)
                p?.sendMessage("$name have degraded slightly!")
                if (set.isNextLast(this.id)) {
                    p?.let { addItemOrDrop(it, set.getNext(this.id)) }
                    p?.sendMessage("Your $name has broken.")
                } else {
                    p?.equipment?.add(Item(set.getNext(this.id), 1, charges), slot, false, false)
                    p?.equipment?.refresh()
                }
            } else if (set.size == 2) {
                if (set.isLast(this.id)) {
                    p?.equipment?.remove(this)
                    p?.sendMessage("Your $name degrades into dust.")
                } else {
                    p?.equipment?.remove(this)
                    p?.sendMessage("Your $name has degraded.")
                    p?.equipment?.add(Item(set.getNext(this.id), 1, charges), slot, false, false)
                    p?.equipment?.refresh()
                }
            }
        }
    }

    /**
     * Finds and returns the degradation set that includes the given item ID.
     *
     * @param item The item ID to find the set for.
     * @return The degradation set containing the item, or `null` if not found.
     */
    private fun getDegradableSet(item: Int): ArrayList<Int>? = setList.find { it.contains(item) }
}
