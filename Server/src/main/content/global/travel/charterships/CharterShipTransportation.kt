package content.global.travel.charterships

import core.api.*
import core.game.component.Component
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.Location
import core.game.world.repository.Repository.findNPC
import core.tools.StringUtils
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Utility for charter ship travel.
 */
object CharterShipTransportation {
    private const val MAX_SAFE_DISTANCE = 30
    // Charter map.
    val component = Component(Components.SAILING_TRANSPORT_WORLD_MAP_95)
    // Button map.
    private val BUTTON_MAP = Destination.values().associateBy { it.button }

    /**
     * Finds a destination by button id.
     */
    private fun forButton(button: Int) = BUTTON_MAP[button]

    /**
     * Finds nearest base within safe distance.
     */
    private fun findNearestBase(location: Location?): Destination? =
        Destination.values().find { it.base?.withinDistance(location!!, MAX_SAFE_DISTANCE) == true }

    /**
     * Opens charter interface.
     */
    fun open(player: Player) {
        findNearestBase(player.location)?.let { base ->
            getHiddenComponents(base).forEach { child ->
                sendInterfaceConfig(player, Components.SAILING_TRANSPORT_WORLD_MAP_95, child, true)
            }
            openInterface(player, component.id)
        }
    }

    /**
     * Handles button click on charter interface.
     */
    fun handle(player: Player, button: Int) {
        val destination = forButton(button) ?: return
        if (!destination.checkTravel(player)) return

        val cost = getCost(player, destination)
        closeInterface(player)

        findNPC(NPCs.TRADER_CREWMEMBER_4651)?.let {
            openDialogue(player, NPCs.TRADER_CREWMEMBER_4651, it, destination, cost)
        }
    }

    /**
     * Calculates cost.
     */
    private fun getCost(player: Player, destination: Destination): Int {
        val baseCost = destination.getCost(player, destination)
        return if (inEquipment(player, Items.RING_OF_CHAROSA_6465)) (baseCost * 0.5).toInt() else baseCost
    }

    /**
     * Gets interface components to hide depending on nearest base.
     */
    private fun getHiddenComponents(base: Destination): IntArray {
        val hidden = mutableListOf(
            Destination.CRANDOR.xChild, Destination.CRANDOR.nameChild,
            base.xChild, base.nameChild
        )
        when (base) {
            Destination.KARAMJA -> hidden += listOf(Destination.PORT_SARIM.xChild, Destination.PORT_SARIM.nameChild)
            Destination.PORT_SARIM -> hidden += listOf(Destination.KARAMJA.xChild, Destination.KARAMJA.nameChild)
            else -> {}
        }
        return hidden.toIntArray()
    }

    private fun loc(x: Int, y: Int, z: Int = 0) = Location.create(x, y, z)

    /**
     * Enum representing all possible charter destinations.
     */
    enum class Destination(
        val destination: Location,
        val button: Int,
        val costs: IntArray,
        val base: Location?,
        val xChild: Int,
        val nameChild: Int
    ) {
        CATHERBY(loc(2792, 3417, 1), 25, intArrayOf(480, 0, 480, 625, 1600, 3250, 1000, 1600, 3200, 3400), loc(2797, 3414), 3, 14),
        PORT_PHASMATYS(loc(3705, 3503, 1), 24, intArrayOf(3650, 3250, 1850, 0, 0, 0, 2050, 1850, 3200, 1100), loc(3702, 3502), 2, 13) {
            override fun checkTravel(player: Player) =
                requireQuest(player, Quests.PRIEST_IN_PERIL, "to go there.")
        },
        CRANDOR(loc(2792, 3417, 1), 32, intArrayOf(0, 480, 480, 925, 400, 3650, 1600, 400, 3200, 3800), null, 10, 21),
        BRIMHAVEN(loc(2763, 3238, 1), 28, intArrayOf(0, 480, 480, 925, 400, 3650, 1600, 400, 3200, 3800), loc(2760, 3238), 6, 17) {
            override fun getCost(player: Player, dest: Destination) =
                if (dest == PORT_KHAZARD && DiaryType.KARAMJA.hasRewardEquipment(player)) 15 else super.getCost(player, dest)
        },
        PORT_SARIM(loc(3038, 3189, 1), 30, intArrayOf(1600, 1000, 0, 325, 1280, 650, 1280, 400, 3200, 1400), loc(3039, 3193), 8, 19) {
            override fun getCost(player: Player, dest: Destination) =
                if (dest == KARAMJA && DiaryType.KARAMJA.hasRewardEquipment(player)) 15 else super.getCost(player, dest)
        },
        PORT_TYRAS(loc(2142, 3125, 1), 23, intArrayOf(3200, 3200, 3200, 1600, 3200, 3200, 3200, 3200, 0, 3200), loc(2143, 3122), 1, 12),
        KARAMJA(loc(2957, 3158, 1), 27, intArrayOf(200, 480, 0, 225, 400, 1850, 0, 200, 3200, 2000), loc(2954, 3156), 5, 16) {
            override fun getCost(player: Player, dest: Destination) =
                if (dest == PORT_SARIM && DiaryType.KARAMJA.hasRewardEquipment(player)) 15 else super.getCost(player, dest)
        },
        PORT_KHAZARD(loc(2674, 3141, 1), 29, intArrayOf(1600, 1000, 0, 325, 180, 650, 1280, 400, 3200, 1400), loc(2674, 3144), 7, 18) {
            override fun getCost(player: Player, dest: Destination) =
                if (dest == BRIMHAVEN && DiaryType.KARAMJA.hasRewardEquipment(player)) 15 else super.getCost(player, dest)
        },
        SHIPYARD(loc(3001, 3032, 0), 26, intArrayOf(400, 1600, 200, 225, 720, 1850, 400, 0, 3200, 900), loc(3001, 3032), 4, 15),
        OO_GLOG(loc(2623, 2857, 0), 33, intArrayOf(300, 3400, 2000, 550, 5000, 2800, 1400, 900, 3200, 0), loc(2622, 2857), 11, 22),
        MOS_LE_HARMLESS(loc(3668, 2931, 1), 31, intArrayOf(725, 625, 1025, 0, 1025, 0, 325, 275, 1600, 500), loc(3671, 2933), 9, 20);

        open fun getCost(player: Player, dest: Destination): Int {
            val current = findNearestBase(player.location) ?: return 0
            val costTable = values().filterNot { it == CRANDOR }
            return current.costs[costTable.indexOf(dest)]
        }

        open fun checkTravel(player: Player): Boolean = true

        fun sail(player: Player) {
            lock(player, 7)
            val start = player.location
            val destName = StringUtils.formatDisplayName(name)

            registerLogoutListener(player, "charter-pulse") { p ->
                p.location = start
            }

            queueScript(player, 1, QueueStrength.SOFT) { stage ->
                when (stage) {
                    0 -> {
                        playJingle(player, 171)
                        openOverlay(player, Components.FADE_TO_BLACK_115)
                        setMinimapState(player, 2)
                        teleport(player, destination, TeleportManager.TeleportType.INSTANT, 3)
                        return@queueScript delayScript(player, 5)
                    }

                    1 -> {
                        unlock(player)
                        closeInterface(player)
                        closeOverlay(player)
                        restoreTabs(player)
                        setMinimapState(player, 0)
                        sendMessage(player, "You pay the fare and sail to $destName.")
                        clearLogoutListener(player, "charter-pulse")
                        when(destination){
                            SHIPYARD.destination -> finishDiaryTask(player, DiaryType.KARAMJA, 1, 17)
                        }
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
        }
    }
}
