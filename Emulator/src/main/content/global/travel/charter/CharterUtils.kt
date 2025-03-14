package content.global.travel.charter

import content.global.travel.charter.CharterUtils.Destination.values
import core.api.*
import core.api.quest.requireQuest
import core.api.ui.restoreTabs
import core.api.ui.setMinimapState
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.repository.Repository.findNPC
import core.tools.StringUtils
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

object CharterUtils {
    val component: Component = Component(Components.SAILING_TRANSPORT_WORLD_MAP_95)

    private fun forButton(button: Int): Destination? {
        for (destination in values()) {
            if (destination.button == button) {
                return destination
            }
        }
        return null
    }

    private fun getFromBase(location: Location?): Destination? {
        for (destination in values()) {
            if (destination.base == null) {
                continue
            }
            if (destination.base.getDistance(location) < 30) {
                return destination
            }
        }
        return null
    }

    fun open(player: Player) {
        val current = getFromBase(player.location)
        if (current != null) {
            val hiddenComponents = getHiddenComponents(player, current)
            for (component in hiddenComponents) {
                player.packetDispatch.sendInterfaceConfig(Components.SAILING_TRANSPORT_WORLD_MAP_95, component, true)
            }
            player.interfaceManager.open(component)
        }
    }

    fun handle(
        player: Player,
        button: Int,
    ) {
        val destination = forButton(button) ?: return
        if (!destination.checkTravel(player)) {
            return
        }
        val cost = getCost(player, destination)
        closeInterface(player)
        findNPC(NPCs.TRADER_CREWMEMBER_4651)?.let {
            openDialogue(
                player,
                NPCs.TRADER_CREWMEMBER_4651,
                it,
                destination,
                cost,
            )
        }
    }

    fun getCost(
        player: Player,
        destination: Destination,
    ): Int {
        val baseCost = destination.getCost(player, destination)
        return if (inEquipment(player, Items.RING_OF_CHAROSA_6465)) {
            (baseCost * 0.5).toInt()
        } else {
            baseCost
        }
    }

    fun getHiddenComponents(
        player: Player?,
        base: Destination,
    ): IntArray {
        val restrictions = arrayOf(Destination.CRANDOR)
        val childs: MutableList<Int> = ArrayList(20)
        for (destination in restrictions) {
            childs.add(destination.xChild)
            childs.add(destination.nameChild)
        }
        childs.add(base.xChild)
        childs.add(base.nameChild)
        if (base == Destination.KARAMJA) {
            childs.add(Destination.PORT_SARIM.xChild)
            childs.add(Destination.PORT_SARIM.nameChild)
        }
        if (base == Destination.PORT_SARIM) {
            childs.add(Destination.KARAMJA.xChild)
            childs.add(Destination.KARAMJA.nameChild)
        }
        val arrayChilds = IntArray(childs.size)
        for (i in arrayChilds.indices) {
            arrayChilds[i] = childs[i]
        }
        return arrayChilds
    }

    enum class Destination(
        val location: Location,
        val button: Int,
        val costs: IntArray,
        val base: Location?,
        vararg val components: Int,
    ) {
        CATHERBY(
            Location.create(2792, 3417, 1),
            25,
            intArrayOf(480, 0, 480, 625, 1600, 3250, 1000, 1600, 3200, 3400),
            Location.create(2797, 3414, 0),
            3,
            14,
        ),
        PORT_PHASMATYS(
            Location.create(3705, 3503, 1),
            24,
            intArrayOf(3650, 3250, 1850, 0, 0, 0, 2050, 1850, 3200, 1100),
            Location.create(3702, 3502, 0),
            2,
            13,
        ) {
            override fun checkTravel(player: Player?): Boolean {
                return requireQuest(player!!, Quests.PRIEST_IN_PERIL, "to go there.")
            }
        },
        CRANDOR(
            Location(2792, 3417, 1),
            32,
            intArrayOf(0, 480, 480, 925, 400, 3650, 1600, 400, 3200, 3800),
            null,
            10,
            21,
        ),
        BRIMHAVEN(
            Location.create(2763, 3238, 1),
            28,
            intArrayOf(0, 480, 480, 925, 400, 3650, 1600, 400, 3200, 3800),
            Location.create(2760, 3238, 0),
            6,
            17,
        ) {
            override fun getCost(
                player: Player,
                destination: Destination,
            ): Int {
                val hasGloves = DiaryType.KARAMJA.hasRewardEquipment(player)
                if (destination == PORT_KHAZARD && hasGloves) return 15
                return super.getCost(player, destination)
            }
        },
        PORT_SARIM(
            Location.create(3038, 3189, 1),
            30,
            intArrayOf(1600, 1000, 0, 325, 1280, 650, 1280, 400, 3200, 1400),
            Location.create(3039, 3193, 0),
            8,
            19,
        ) {
            override fun getCost(
                player: Player,
                destination: Destination,
            ): Int {
                val hasGloves = DiaryType.KARAMJA.hasRewardEquipment(player)
                if (destination == KARAMJA && hasGloves) return 15
                return super.getCost(player, destination)
            }
        },
        PORT_TYRAS(
            Location.create(2142, 3125, 1),
            23,
            intArrayOf(3200, 3200, 3200, 1600, 3200, 3200, 3200, 3200, 0, 3200),
            Location.create(2143, 3122, 0),
            1,
            12,
        ),
        KARAMJA(
            Location.create(2957, 3158, 1),
            27,
            intArrayOf(200, 480, 0, 225, 400, 1850, 0, 200, 3200, 2000),
            Location.create(2954, 3156, 0),
            5,
            16,
        ) {
            override fun getCost(
                player: Player,
                destination: Destination,
            ): Int {
                val hasGloves = DiaryType.KARAMJA.hasRewardEquipment(player)
                if (destination == PORT_SARIM && hasGloves) return 15
                return super.getCost(player, destination)
            }
        },
        PORT_KHAZARD(
            Location.create(2674, 3141, 1),
            29,
            intArrayOf(1600, 1000, 0, 325, 180, 650, 1280, 400, 3200, 1400),
            Location.create(2674, 3144, 0),
            7,
            18,
        ) {
            override fun getCost(
                player: Player,
                destination: Destination,
            ): Int {
                val hasGloves = DiaryType.KARAMJA.hasRewardEquipment(player)
                if (destination == BRIMHAVEN && hasGloves) return 15
                return super.getCost(player, destination)
            }
        },
        SHIPYARD(
            Location.create(3001, 3032, 0),
            26,
            intArrayOf(400, 1600, 200, 225, 720, 1850, 400, 0, 3200, 900),
            Location.create(3001, 3032, 0),
            4,
            15,
        ),
        OO_GLOG(
            Location.create(2623, 2857, 0),
            33,
            intArrayOf(300, 3400, 2000, 550, 5000, 2800, 1400, 900, 3200, 0),
            Location.create(2622, 2857, 0),
            11,
            22,
        ),
        MOS_LE_HARMLESS(
            Location.create(3668, 2931, 1),
            31,
            intArrayOf(725, 625, 1025, 0, 1025, 0, 325, 275, 1600, 500),
            Location.create(3671, 2933, 0),
            9,
            20,
        ),
        ;

        val xChild: Int = components[0]
        val nameChild: Int = components[1]

        open fun getCost(
            player: Player,
            destination: Destination,
        ): Int {
            val current = getFromBase(player.location) ?: return 0
            val costTable =
                arrayOf(
                    BRIMHAVEN,
                    CATHERBY,
                    KARAMJA,
                    MOS_LE_HARMLESS,
                    PORT_KHAZARD,
                    PORT_PHASMATYS,
                    PORT_SARIM,
                    SHIPYARD,
                    PORT_TYRAS,
                    OO_GLOG,
                )
            var index = 0
            for (i in costTable.indices) {
                if (costTable[i] == destination) {
                    index = i
                    break
                }
            }
            return current.costs[index]
        }

        open fun checkTravel(player: Player?): Boolean {
            return true
        }

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
                                sendMessage(
                                    player,
                                    "You pay the fare and sail to " + StringUtils.formatDisplayName(name) + ".",
                                )
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
}
