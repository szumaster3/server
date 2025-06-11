package content.global.travel.charter

import content.global.travel.charter.CharterUtils.Destination.values
import core.api.*
import core.api.quest.requireQuest
import core.api.ui.*
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.repository.Repository.findNPC
import org.rs.consts.*

/**
 * Utility object providing functionality for charter ship travel.
 */
object CharterUtils {
    private const val MAX_SAFE_DISTANCE = 30

    /**
     * Represents the sailing map interface.
     */
    val component = Component(Components.SAILING_TRANSPORT_WORLD_MAP_95)

    /**
     * Finds a destination matching the provided button id.
     *
     * @param button The interface button ID clicked by the player.
     * @return The matching [Destination] or `null` if none matches.
     */
    private fun forButton(button: Int) = values().find { it.button == button }

    /**
     * Finds the nearest charter base destination to the given location,
     * within a maximum safe distance.
     *
     * @param location The location to compare against all charter bases.
     * @return The nearest [Destination] base if within [MAX_SAFE_DISTANCE], or `null` otherwise.
     */
    private fun findNearestBase(location: Location?): Destination? =
        values().find { it.base?.withinDistance(location, MAX_SAFE_DISTANCE) == true }

    /**
     * Opens the charter travel interface.
     *
     * @param player The player opening the charter interface.
     */
    fun open(player: Player) {
        findNearestBase(player.location)?.let { base ->
            getHiddenComponents(base).forEach {
                player.packetDispatch.sendInterfaceConfig(Components.SAILING_TRANSPORT_WORLD_MAP_95, it, true)
            }
            player.interfaceManager.open(component)
        }
    }

    /**
     * Handles button click events within the charter interface.
     *
     * @param player The player interacting with the interface.
     * @param button The button id clicked.
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
     * Calculates the travel cost for the player to the specified destination,
     * applying any discounts if the player has the Ring of Charos equipped.
     *
     * @param player The player traveling.
     * @param destination The chosen travel destination.
     * @return The final travel cost in coins.
     */
    private fun getCost(player: Player, destination: Destination): Int {
        val baseCost = destination.getCost(player, destination)
        return if (inEquipment(player, Items.RING_OF_CHAROSA_6465)) (baseCost * 0.5).toInt() else baseCost
    }

    /**
     * Gets which interface components should be hidden
     * depending on the player's current charter base.
     *
     * @param base The nearest charter [Destination] base.
     * @return Array of component IDs to hide.
     */
    private fun getHiddenComponents(base: Destination): IntArray =
        buildList {
            arrayOf(Destination.CRANDOR).forEach {
                add(it.xChild)
                add(it.nameChild)
            }
            add(base.xChild)
            add(base.nameChild)
            when (base) {
                Destination.KARAMJA -> addBoth(Destination.PORT_SARIM)
                Destination.PORT_SARIM -> addBoth(Destination.KARAMJA)
                else -> {}
            }
        }.toIntArray()

    private fun MutableList<Int>.addBoth(dest: Destination) {
        add(dest.xChild)
        add(dest.nameChild)
    }

    /**
     * Represents a charter ship travel destination.
     *
     * @property location The in-game location to teleport the player to.
     * @property button The button ID corresponding to this destination in the interface.
     * @property costs An array of travel costs from various bases.
     * @property base The base location associated with this destination, if any.
     * @property xChild The component ID for the X coordinate display in the interface.
     * @property nameChild The component ID for the name display in the interface.
     */
    enum class Destination(
        val location: Location,
        val button: Int,
        val costs: IntArray,
        val base: Location?,
        val xChild: Int,
        val nameChild: Int
    ) {
        CATHERBY(loc(2792, 3417, 1), 25, intArrayOf(480, 0, 480, 625, 1600, 3250, 1000, 1600, 3200, 3400), loc(2797, 3414), 3, 14),
        PORT_PHASMATYS(loc(3705, 3503, 1), 24, intArrayOf(3650, 3250, 1850, 0, 0, 0, 2050, 1850, 3200, 1100), loc(3702, 3502), 2, 13) {
            override fun checkTravel(player: Player) = requireQuest(player, Quests.PRIEST_IN_PERIL, "to go there.")
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

        /**
         * Gets the travel cost from the player's nearest base to the given destination.
         *
         * @param player The player traveling.
         * @param dest The destination to calculate cost for.
         * @return The travel cost in coins.
         */
        open fun getCost(player: Player, dest: Destination): Int {
            val current = findNearestBase(player.location) ?: return 0
            val costTable = values().filterNot { it == CRANDOR }
            return current.costs[costTable.indexOf(dest)]
        }

        /**
         * Checks if the player meets the requirements to travel to this destination.
         *
         * @param player The player attempting travel.
         * @return `true` if travel is allowed, `false` otherwise.
         */
        open fun checkTravel(player: Player): Boolean = true

        /**
         * Initiates the sailing process.
         *
         * @param player The player sailing.
         */
        fun sail(player: Player) {
            player.lock(7)
            val start = player.location
            Pulser.submit(
                object : Pulse(1) {
                    var count: Int = 0

                    override fun pulse(): Boolean {
                        when (count++) {
                            0 -> openOverlay(player, Components.FADE_TO_BLACK_115)
                            2 -> setMinimapState(player, 2)
                            3 -> teleport(player, location, TeleportManager.TeleportType.INSTANT)
                            5 -> {
                                player.unlock()
                                closeInterface(player)
                                closeOverlay(player)
                                restoreTabs(player)
                                setMinimapState(player, 0)
                                sendMessage(player, "You pay the fare and sail to $name.")
                                if (start.withinDistance(Location.create(3001, 3032, 0))) {
                                    finishDiaryTask(player, DiaryType.KARAMJA, 1, 17)
                                }
                                return true
                            }
                        }
                        return false
                    }
                },
            )
        }
    }

    /**
     * Helper to create a Location
     */
    private fun loc(x: Int, y: Int, z: Int = 0) = Location.create(x, y, z)
}
