package content.global.skill.construction.decoration.costumeroom

import com.google.gson.JsonObject
import core.game.node.entity.player.Player

class StorageState(val player: Player) {

    enum class ContainerGroup {
        BOOK, CAPE, FANCY, TOY, TRAILS, ARMOUR, ARMOUR_CASE, ;

        companion object {
            fun fromType(type: StorableType) = when (type) {
                StorableType.BOOK -> BOOK
                StorableType.CAPE -> CAPE
                StorableType.FANCY -> FANCY
                StorableType.TOY -> TOY
                in listOf(StorableType.LOW_LEVEL_TRAILS, StorableType.MED_LEVEL_TRAILS, StorableType.HIGH_LEVEL_TRAILS) -> TRAILS
                in listOf(StorableType.ONE_SET_OF_ARMOUR, StorableType.TWO_SETS_OF_ARMOUR, StorableType.THREE_SETS_OF_ARMOUR, StorableType.FOUR_SETS_OF_ARMOUR, StorableType.FIVE_SETS_OF_ARMOUR, StorableType.SIX_SETS_OF_ARMOUR, StorableType.ALL_SETS_OF_ARMOUR) -> ARMOUR
                else -> ARMOUR_CASE
            }
        }
    }

    private val containers = ContainerGroup.values().associateWith { StorageContainer() }.toMutableMap()

    fun getContainer(type: StorableType): StorageContainer = containers.getValue(ContainerGroup.fromType(type))

    fun toJson(): JsonObject = JsonObject().apply {
        add("containers", JsonObject().apply {
            containers.forEach { (group, container) ->
                add(group.name.lowercase(), container.toJson())
            }
        })
    }

    fun readJson(data: JsonObject) {
        data.getAsJsonObject("containers")?.let { json ->
            ContainerGroup.values().forEach { group ->
                json.getAsJsonObject(group.name.lowercase())?.let {
                    containers[group] = StorageContainer.fromJson(it)
                }
            }
        }
    }
}
