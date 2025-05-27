package content.global.skill.summoning.items

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.rs.consts.Items

/**
 * Manages Summoning scrolls stored inside enchanted headgear items.
 */
object EnchantedHeadgearScrolls {
    private val playerHeadgearScrolls = mutableMapOf<Pair<Player, Int>, MutableMap<Int, Int>>()

    /**
     * Gets the count of a specific scroll stored in a headgear.
     */
    @JvmStatic
    fun getCurrentScrollCount(player: Player, chargedItemId: Int, scrollId: Int): Int {
        return playerHeadgearScrolls[Pair(player, chargedItemId)]?.get(scrollId) ?: 0
    }

    /**
     * Adds scrolls to a charged headgear.
     * @return `true` on success.
     */
    fun addScroll(player: Player, chargedItemId: Int, scrollId: Int, amount: Int): Boolean {
        val map = playerHeadgearScrolls.getOrPut(Pair(player, chargedItemId)) { mutableMapOf() }
        val current = map[scrollId] ?: 0
        map[scrollId] = current + amount
        return true
    }

    /**
     * Checks if the player headgear contains any scrolls.
     */
    fun hasScrolls(player: Player, chargedItemId: Int): Boolean {
        val key = Pair(player, chargedItemId)
        val scrolls = playerHeadgearScrolls[key]
        return !scrolls.isNullOrEmpty()
    }

    /**
     * Allowed scroll item ids for headgear.
     */
    val allowedScrollIDs = intArrayOf(
        Items.HOWL_SCROLL_12425, Items.DREADFOWL_STRIKE_SCROLL_12445, Items.SLIME_SPRAY_SCROLL_12459,
        Items.PESTER_SCROLL_12838, Items.ELECTRIC_LASH_SCROLL_12460, Items.FIREBALL_ASSAULT_SCROLL_12839,
        Items.SANDSTORM_SCROLL_12446, Items.VAMPIRE_TOUCH_SCROLL_12447, Items.BRONZE_BULL_RUSH_SCROLL_12461,
        Items.EVIL_FLAMES_SCROLL_12448, Items.PETRIFYING_GAZE_SCROLL_12458, Items.IRON_BULL_RUSH_SCROLL_12462,
        Items.ABYSSAL_DRAIN_SCROLL_12454, Items.DISSOLVE_SCROLL_12453, Items.AMBUSH_SCROLL_12836,
        Items.RENDING_SCROLL_12840, Items.DOOMSPHERE_SCROLL_12455, Items.DUST_CLOUD_SCROLL_12468,
        Items.STEEL_BULL_RUSH_SCROLL_12463, Items.POISONOUS_BLAST_SCROLL_12467, Items.MITH_BULL_RUSH_SCROLL_12464,
        Items.TOAD_BARK_SCROLL_12452, Items.FAMINE_SCROLL_12830, Items.ARCTIC_BLAST_SCROLL_12451,
        Items.RISE_FROM_THE_ASHES_SCROLL_14622, Items.CRUSHING_CLAW_SCROLL_12449, Items.MANTIS_STRIKE_SCROLL_12450,
        Items.INFERNO_SCROLL_12841, Items.ADDY_BULL_RUSH_SCROLL_12465, Items.DEADLY_CLAW_SCROLL_12831,
        Items.ACORN_MISSILE_SCROLL_12457, Items.SPIKE_SHOT_SCROLL_12456, Items.EBON_THUNDER_SCROLL_12837,
        Items.SWAMP_PLAGUE_SCROLL_12832, Items.RUNE_BULL_RUSH_SCROLL_12466, Items.BOIL_SCROLL_12833,
        Items.IRON_WITHIN_SCROLL_12828, Items.STEEL_OF_LEGENDS_SCROLL_12825
    )

    /**
     * Sends a message listing stored scrolls in the headgear.
     */
    fun checkHeadgear(player: Player, chargedItemId: Int) {
        val scrolls = playerHeadgearScrolls[Pair(player, chargedItemId)]

        if (scrolls.isNullOrEmpty()) {
            sendMessage(player, "Your headgear holds no scrolls.")
            return
        }


        val message = scrolls.entries.joinToString(separator = "\n") { (scrollId, amount) ->
            val scrollName = getItemName(scrollId)
            val plural = if (amount == 1) "charge" else "charges"
            "The item contains $amount $plural of the $scrollName."
        }
        sendMessage(player, message)
    }

    /**
     * Adds scrolls to headgear.
     */
    fun addScrollToChargedItem(
        chargedItemId: Int,
        scrollId: Int,
        amount: Int,
        player: Player
    ) {
        if (scrollId !in allowedScrollIDs) {
            sendMessage(player, "You cannot add this scroll to the headgear.")
            return
        }

        val headgear = EnchantedHeadgear.byCharged[chargedItemId]
        if (headgear == null) {
            sendMessage(player, "This item cannot hold scrolls.")
            return
        }

        val key = Pair(player, chargedItemId)
        val storedScrolls = playerHeadgearScrolls[key]

        if (storedScrolls != null && storedScrolls.isNotEmpty()) {
            val differentScrolls = storedScrolls.keys.filter { it != scrollId }
            if (differentScrolls.isNotEmpty()) {
                sendMessage(player, "Your headgear already contains different scrolls. Remove them first.")
                return
            }
        }

        val currentCount = getCurrentScrollCount(player, chargedItemId, scrollId)
        val freeSpace = headgear.scrollCapacity - currentCount

        if (freeSpace <= 0) {
            sendMessage(player, "You already have charged headgear.")
            return
        }

        var toAdd = amount
        if (toAdd > freeSpace) {
            toAdd = freeSpace
        }

        val scrollItem = Item(scrollId, toAdd)

        if (!player.inventory.containsItem(scrollItem)) {
            sendMessage(player, "You do not have enough scrolls to add.")
            return
        }

        if (!player.inventory.remove(scrollItem)) {
            sendMessage(player, "Failed to remove scrolls from your inventory.")
            return
        }

        if (!addScroll(player, chargedItemId, scrollId, toAdd)) {
            player.inventory.add(scrollItem)
            sendMessage(player, "Failed to add scrolls to your headgear.")
            return
        }
        sendMessage(player, "You charge the headgear with a scroll.")
    }

    /**
     * Removes all stored scrolls from headgear.
     */
    fun unchargeHeadgear(player: Player, chargedItemId: Int) {
        val headgear = EnchantedHeadgear.byCharged[chargedItemId] ?: run {
            sendMessage(player, "This item cannot be uncharged.")
            return
        }

        val key = Pair(player, chargedItemId)
        val scrolls = playerHeadgearScrolls[key]

        if (scrolls.isNullOrEmpty()) {
            sendMessage(player, "Your headgear has no scrolls to remove.")
            return
        }

        scrolls.forEach { (scrollId, amount) ->
            player.inventory.add(Item(scrollId, amount))
        }

        playerHeadgearScrolls.remove(key)

        var slot = -1
        for (i in 0 until player.inventory.itemCount()) {
            val item = player.inventory[i]
            if (item != null && item.id == chargedItemId) {
                slot = i
                break
            }
        }

        if (slot == -1) {
            sendMessage(player, "Could not find the charged headgear in your inventory.")
            return
        }

        player.inventory.replace(Item(headgear.enchantedItem.id), slot)

        sendMessages(
            player,
            "You remove the scrolls. You will need to use a Summoning scroll on it to charge the",
            "headgear up once more.",
        )
    }

    /**
     * Returns the first stored scroll id and amount for a player, or `null` if none.
     */
    @JvmStatic
    fun getStoredScroll(player: Player): Pair<Int, Int>? {
        for ((key, scrollMap) in playerHeadgearScrolls) {
            if (key.first == player && scrollMap.isNotEmpty()) {
                val entry = scrollMap.entries.first()
                return Pair(entry.key, entry.value)
            }
        }
        return null
    }

    /**
     * Consumes one scroll from charged headgear.
     * @return `true` if successful.
     */
    @JvmStatic
    fun removeScroll(player: Player, chargedItemId: Int, scrollId: Int): Boolean {
        val key = Pair(player, chargedItemId)
        val scrolls = playerHeadgearScrolls[key] ?: return false
        val current = scrolls[scrollId] ?: return false
        if (current <= 0) return false

        if (current == 1) {
            scrolls.remove(scrollId)
        } else {
            scrolls[scrollId] = current - 1
        }

        return true
    }

    /**
     * Returns the charged enchanted headgear item id the player is wearing, or null.
     */
    @JvmStatic
    fun getFromEquipment(player: Player): Int? {
        val headSlotItem = player.equipment[EquipmentSlot.HEAD.ordinal]
        return if (headSlotItem != null && EnchantedHeadgear.byCharged.containsKey(headSlotItem.id)) {
            headSlotItem.id
        } else null
    }

    /**
     * Gets total count of a scroll across inventory and charged headgear.
     */
    fun getTotalScrollCount(player: Player, scrollId: Int): Int {
        val inventoryCount = player.inventory.getAmount(scrollId)
        val equippedHeadgearId = EnchantedHeadgearScrolls.getFromEquipment(player)
        val headgearCount = if (equippedHeadgearId != null) {
            EnchantedHeadgearScrolls.getCurrentScrollCount(player, equippedHeadgearId, scrollId)
        } else {
            0
        }
        return inventoryCount + headgearCount
    }

    /**
     * Saves player charged scroll data to JSON.
     */
    fun save(player: Player, root: JSONObject) {
        val chargedArray = JSONArray()

        val filtered = playerHeadgearScrolls.filterKeys { it.first == player }

        for ((key, scrollMap) in filtered) {
            val chargedItemId = key.second
            for ((scrollId, amount) in scrollMap) {
                val obj = JSONObject()
                obj["chargedItemId"] = chargedItemId.toString()
                obj["scrollId"] = scrollId.toString()
                obj["amount"] = amount.toString()
                chargedArray.add(obj)
            }
        }

        root["chargedScrolls"] = chargedArray
    }

    /**
     * Loads player charged scroll data from JSON.
     */
    fun parse(player: Player, data: JSONArray) {
        playerHeadgearScrolls.entries.removeIf { it.key.first == player }

        for (e in data) {
            val obj = e as JSONObject
            val chargedItemId = obj["chargedItemId"].toString().toInt()
            val scrollId = obj["scrollId"].toString().toInt()
            val amount = obj["amount"].toString().toInt()

            val key = Pair(player, chargedItemId)
            val map = playerHeadgearScrolls.getOrPut(key) { mutableMapOf() }
            map[scrollId] = amount
        }
    }
}
