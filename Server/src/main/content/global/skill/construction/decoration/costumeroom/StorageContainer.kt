package content.global.skill.construction.decoration.costumeroom

import com.google.gson.JsonArray
import com.google.gson.JsonObject

class StorageContainer {
    private val stored = mutableMapOf<StorableType, MutableList<Int>>()
    private val currentPage = mutableMapOf<StorableType, Int>()
    private val tiers = mutableMapOf<StorableType, Int>()

    fun addItem(type: StorableType, id: Int) =
        stored.getOrPut(type) { mutableListOf() }.add(id)

    fun withdraw(type: StorableType, item: Storable) {
        val id = item.takeIds.firstOrNull() ?: item.displayId
        stored[type]?.remove(id)
    }

    fun contains(type: StorableType, id: Int) =
        id in (stored[type] ?: emptyList())

    fun getItems(type: StorableType): List<Int> =
        stored[type]?.toList() ?: emptyList()

    fun getTier(type: StorableType): Int =
        tiers.getOrDefault(type, 0)

    fun setTier(type: StorableType, tier: Int) {
        tiers[type] = tier.coerceAtLeast(0)
        currentPage[type] = 0
    }

    fun getPageIndex(type: StorableType): Int =
        currentPage.getOrDefault(type, 0)

    fun nextPage(type: StorableType, totalItems: Int, pageSize: Int) {
        currentPage[type] =
            (getPageIndex(type) + 1).takeIf { it * pageSize < totalItems }
                ?: getPageIndex(type)
    }

    fun prevPage(type: StorableType) {
        currentPage[type] = (getPageIndex(type) - 1).coerceAtLeast(0)
    }

    fun toJson(): JsonObject =
        JsonObject().apply {

            add("items", JsonObject().apply {
                stored.forEach { (type, list) ->
                    add(type.name.lowercase(), JsonArray().apply {
                        list.forEach(::add)
                    })
                }
            })

            add("tiers", JsonObject().apply {
                tiers.forEach { (type, tier) ->
                    addProperty(type.name.lowercase(), tier)
                }
            })
        }

    companion object {
        fun fromJson(json: JsonObject) =
            StorageContainer().apply {

                json.getAsJsonObject("items")?.entrySet()?.forEach { (key, value) ->
                    val type = StorableType.valueOf(key.uppercase())
                    stored[type] =
                        value.asJsonArray.map { it.asInt }.toMutableList()
                }

                json.getAsJsonObject("tiers")?.entrySet()?.forEach { (key, value) ->
                    val type = StorableType.valueOf(key.uppercase())
                    tiers[type] = value.asInt
                }
            }
    }
}
