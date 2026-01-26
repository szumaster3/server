package content.global.skill.construction

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.tools.Log
import shared.consts.Components
import shared.consts.Items

class BuildInterface : InterfaceListener {

    override fun defineInterfaceListeners() {

        on(Components.POH_BUILD_FURNITURE_396) { player, _, _, button, slot, itemId ->
            if (button == BUILD_BUTTON) {
                handleBuildFurniture(player, slot, itemId)
                return@on true
            }

            return@on false
        }

        on(Components.POH_HOUSE_OPTIONS_398) { player, _, _, button, _, _ ->
            return@on handleHouseOptions(player, button)
        }

        onOpen(Components.POH_BUILD_SCREEN_402) { player, _ ->
            val coins = amountInInventory(player, Items.COINS_995)

            COINS_VALUE_TO_CHILD
                .filter { coins >= it.first }
                .forEach { (amount, childIds) ->
                    childIds.forEach { childId ->
                        sendString(player, core.tools.YELLOW + "$amount coins", Components.POH_BUILD_SCREEN_402, childId)
                    }
                }

            return@onOpen true
        }

        on(Components.POH_BUILD_SCREEN_402) { player, _, _, button, _, _ ->
            val index = button - BASE_BUTTON_ID
            log(javaClass, Log.FINE, "BuildRoom Interface Index: [$index]")

            RoomProperties.values().getOrNull(index)?.let {
                openDialogue(player, "con:room", it)
            }

            return@on true
        }
    }

    private fun handleBuildFurniture(player: Player, slot: Int, itemId: Int) {
        closeInterface(player)

        val hotspot = player.getAttribute<Hotspot>("con:hotspot")
        val scenery = player.getAttribute<Scenery>("con:hsobject")

        if (hotspot == null && hotspot?.hotspot != BuildHotspot.FLATPACK) {
            log(javaClass, Log.ERR, "Construction failed: hotspot=[$hotspot], scenery=[$scenery]")
            return
        }

        val index = decorationIndex(slot)
        val decorations = hotspot?.hotspot?.decorations ?: return

        if (index !in decorations.indices) {
            log(javaClass, Log.ERR, "Invalid decoration index [$index/${decorations.size}]")
            return
        }

        val debug = player.isStaff
        val decoration = getDecoration(hotspot, decorations, index, itemId)

        if (hotspot.hotspot == BuildHotspot.FLATPACK) {
            if (debug || checkRequirements(player, decoration)) {
                BuildingUtils.createFlatpack(player, decoration, debug)
            }
            return
        }

        if (!debug && !checkRequirements(player, decoration)) return

        scenery?.let {
            BuildingUtils.buildDecoration(player, hotspot, decoration, it)
        }
    }

    private fun handleHouseOptions(player: Player, button: Int): Boolean = when (button) {
        BUILD_MODE_ON -> {
            player.houseManager.toggleBuildingMode(player, true)
            true
        }

        BUILD_MODE_OFF -> {
            player.houseManager.toggleBuildingMode(player, false)
            true
        }

        EXPEL_GUESTS -> {
            player.houseManager.expelGuests(player)
            true
        }

        LEAVE_HOUSE -> {
            if (!player.houseManager.isInHouse(player)) {
                sendMessage(player, "You can't do this outside of your house.")
            } else {
                HouseManager.leave(player)
            }
            true
        }

        else -> false
    }

    private fun decorationIndex(slot: Int): Int = ((slot and 1) * 4) + (slot shr 1)

    private fun getDecoration(
        hotspot: Hotspot, decorations: Array<Decoration>, index: Int, itemId: Int
    ): Decoration = if (hotspot.hotspot == BuildHotspot.FLATPACK) Decoration.forInterfaceItemId(itemId)
    else decorations[index]

    private fun checkRequirements(player: Player, deco: Decoration): Boolean {
        if (getStatLevel(player, Skills.CONSTRUCTION) < deco.level) {
            sendMessage(player, "You need a Construction level of ${deco.level} to build that.")
            return false
        }

        if (!player.inventory.containsItems(*deco.items)) {
            sendMessage(player, "You don't have the right materials.")
            return false
        }

        for (tool in deco.tools) {
            if (tool == BuildingUtils.WATERING_CAN) {
                if (!hasWateringCan(player)) {
                    sendMessage(player, "You need a watering can to plant this.")
                    return false
                }
                continue
            }

            if (!inInventory(player, tool, 1)) {
                val name = ItemDefinition.forId(tool).name
                sendMessage(player, "You need a $name to build this.")
                return false
            }
        }

        return true
    }

    private fun hasWateringCan(player: Player): Boolean = (0..7).any {
        inInventory(player, BuildingUtils.WATERING_CAN - it, 1)
    }

    companion object {
        // POH_BUILD_FURNITURE_396
        private const val BUILD_BUTTON = 132

        // POH_HOUSE_OPTIONS_398
        private const val BUILD_MODE_ON = 14
        private const val BUILD_MODE_OFF = 1
        private const val EXPEL_GUESTS = 15
        private const val LEAVE_HOUSE = 13

        // POH_BUILD_SCREEN_402
        private const val BASE_BUTTON_ID = 160
        private val COINS_VALUE_TO_CHILD = arrayOf(
            1000    to listOf(138, 139),
            5000    to listOf(140, 141),
            10000   to listOf(142, 143, 144),
            25000   to listOf(145, 146, 147),
            50000   to listOf(148, 149, 150),
            100000  to listOf(151),
            75000   to listOf(152),
            150000  to listOf(153, 154),
            7500    to listOf(155, 156, 157),
            250000  to listOf(159)
        )
    }
}
