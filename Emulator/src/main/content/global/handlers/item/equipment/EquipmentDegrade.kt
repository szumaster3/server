package content.global.handlers.item.equipment

import core.api.addItemOrDrop
import core.api.getNext
import core.api.isLast
import core.api.isNextLast
import core.game.node.entity.player.Player
import core.game.node.item.Item

class EquipmentDegrade {
    companion object StaticDegrade {
        var p: Player? = null
        val itemList = arrayListOf<Int>()
        val setList = arrayListOf<ArrayList<Int>>()
        val itemCharges = hashMapOf<Int, Int>()

        fun register(
            charges: Int,
            item: Int,
        ) {
            itemList.add(item)
            itemCharges[item] = charges
        }

        fun registerSet(
            charges: Int,
            items: Array<Int>,
        ) {
            setList.add(ArrayList(items.map { item -> item.also { register(charges, item) } }))
        }
    }

    fun Int.canDegrade(): Boolean {
        return itemList.contains(this)
    }

    fun checkArmourDegrades(player: Player?) {
        p = player
        for (slot in 0..12) {
            if (slot == 3) continue
            player?.equipment?.get(slot)?.let { if (it.id.canDegrade()) it.degrade(slot) }
        }
    }

    fun checkWeaponDegrades(player: Player?) {
        p = player
        player?.equipment?.get(3)?.let { if (it.id.canDegrade()) it.degrade(3) }
    }

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
                p?.sendMessages("$name have degraded slightly!")
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

    private fun getDegradableSet(item: Int): ArrayList<Int>? {
        return setList.find { it.contains(item) }
    }
}
