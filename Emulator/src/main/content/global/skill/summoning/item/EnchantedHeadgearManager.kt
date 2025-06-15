package content.global.skill.summoning.item

import core.api.*
import core.game.container.Container
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.json.simple.JSONArray
import org.json.simple.JSONObject

class EnchantedHeadgearManager(val player: Player) {

    val enchantedGear: MutableMap<Int, ChargedHeadgear> = mutableMapOf()

    fun addScrolls(chargedItemId: Int, itemId: Int, amount: Int) {
        val chargedGear: ChargedHeadgear = enchantedGear.getOrPut(chargedItemId) {
            val capacity = EnchantedHeadgear.byCharged[chargedItemId]?.scrollCapacity ?: 50
            ChargedHeadgear(chargedItemId, Container(capacity))
        }

        val freeSlots = chargedGear.container.freeSlots()
        if (freeSlots <= 0) {
            sendMessage(player, "You already have charged headgear.")
            return
        }

        val toAdd = amount.coerceAtMost(freeSlots)
        chargedGear.container.add(Item(itemId, toAdd))
    }

    fun withdrawScrolls(chargedItemId: Int, itemId: Int, amount: Int) {
        val chargedGear = enchantedGear[chargedItemId] ?: return


        val containedAmount = chargedGear.container.getAmount(itemId)
        if (containedAmount < amount) return

        chargedGear.container.remove(Item(itemId, amount))
        chargedGear.container.shift()
        addItem(player, itemId, amount)
    }

    fun withdrawAllScrolls(chargedItemId: Int) {
        val chargedGear = enchantedGear[chargedItemId] ?: return
        val scrolls = chargedGear.container.toArray().filterNotNull()

        scrolls.forEach { item ->
            chargedGear.container.remove(item)
            addItem(player, item.id, item.amount)
        }

        chargedGear.container.shift()
        sendMessages(player, "You remove the scrolls. You will need to use a Summoning scroll on it to charge the", "headgear up once more.")
    }

    fun hasScrolls(chargedItemId: Int): Boolean {
        val chargedGear = enchantedGear[chargedItemId] ?: return false
        return chargedGear.container.toArray().any { it != null }
    }

    fun clear() {
        enchantedGear.clear()
    }

    fun save(root: JSONObject) {
        val arr = JSONArray()
        enchantedGear.forEach { (_, chargedGear) ->
            val obj = JSONObject().apply {
                put("chargedItemId", chargedGear.chargedItemId.toString())
                put("scrolls", JSONArray().apply {
                    chargedGear.container.toArray().forEach { item ->
                        if (item == null) return@forEach
                        add(JSONObject().apply {
                            put("id", item.id.toString())
                            put("amount", item.amount.toString())
                        })
                    }
                })
            }
            arr.add(obj)
        }
        root["enchanted_headgear_scrolls"] = arr
    }

    fun parse(data: JSONArray) {
        enchantedGear.clear()

        data.forEach { e ->
            val obj = e as JSONObject
            val chargedItemId = obj["chargedItemId"].toString().toInt()
            val scrollsArr = obj["scrolls"] as JSONArray
            val capacity = EnchantedHeadgear.byCharged[chargedItemId]?.scrollCapacity ?: 50
            val container = Container(capacity)

            scrollsArr.forEach { s ->
                val scrollObj = s as JSONObject
                val id = scrollObj["id"].toString().toInt()
                val amount = scrollObj["amount"].toString().toInt()
                container.add(Item(id, amount))
            }

            enchantedGear[chargedItemId] = ChargedHeadgear(chargedItemId, container)
        }
    }

    fun getCurrentScrollCount(chargedItemId: Int, scrollId: Int): Int {
        val chargedGear = enchantedGear[chargedItemId] ?: return 0
        return chargedGear.container.getAmount(scrollId)
    }

    fun getTotalScrollCount(scrollId: Int): Int {
        val inventoryCount = player.inventory.getAmount(scrollId)
        val equippedHeadgearId = getFromEquipment()
        val headgearCount = if (equippedHeadgearId != null) {
            getCurrentScrollCount(equippedHeadgearId, scrollId)
        } else 0
        return inventoryCount + headgearCount
    }

    fun getFromEquipment(): Int? {
        val headSlotItem = player.equipment[EquipmentSlot.HEAD.ordinal]
        return if (headSlotItem != null && EnchantedHeadgear.byCharged.containsKey(headSlotItem.id)) {
            headSlotItem.id
        } else null
    }

    fun removeScroll(chargedItemId: Int, scrollId: Int): Boolean {
        val chargedGear = enchantedGear[chargedItemId] ?: return false
        val container = chargedGear.container
        val amount = container.getAmount(scrollId)
        if (amount <= 0) return false
        container.remove(Item(scrollId, 1))
        container.shift()
        return true
    }

    fun checkHeadgear(chargedItemId: Int) {
        val chargedGear = enchantedGear[chargedItemId]
        if (chargedGear == null || chargedGear.container.toArray().all { it == null }) {
            sendMessage(player, "Your headgear holds no scrolls.")
            return
        }
        val message = chargedGear.container.toArray()
            .filterNotNull()
            .joinToString("\n") { item ->
                val scrollName = getItemName(item.id)
                val plural = if (item.amount == 1) "charge" else "charges"
                "The item contains ${item.amount} $plural of the $scrollName."
            }
        sendMessage(player, message)
    }

    data class ChargedHeadgear(
        val chargedItemId: Int,
        val container: Container,
    )
}