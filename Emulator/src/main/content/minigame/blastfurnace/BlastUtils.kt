package content.minigame.blastfurnace

import core.game.world.map.Location
import org.rs.consts.Scenery

object BlastUtils {
    val ENTRANCE_LOC = Location(1940, 4958, 0)
    val EXIT_LOC = Location(2931, 10197, 0)
    val STAIRLOC_ENTRANCE = Location(2930, 10196)
    val STAIRLOC_EXIT = Location(1939, 4956)
    const val STAIR_ENTRANCE_ID = Scenery.STAIRS_9084
    const val STAIR_EXIT_ID = Scenery.STAIRS_9138
    const val BELT = Scenery.CONVEYOR_BELT_9100
    const val PEDALS = Scenery.PEDALS_9097
    val STOVE =
        intArrayOf(BFSceneryController.STOVE_COLD, BFSceneryController.STOVE_WARM, BFSceneryController.STOVE_HOT)
    const val PUMP = Scenery.PUMP_9090
    const val COKE = Scenery.COKE_9088
    const val TEMP_GAUGE = Scenery.TEMPERATURE_GAUGE_9089
    const val SINK = Scenery.SINK_9143
    val DISPENSER =
        intArrayOf(
            Scenery.BAR_DISPENSER_9093,
            Scenery.BAR_DISPENSER_9094,
            Scenery.BAR_DISPENSER_9095,
            Scenery.BAR_DISPENSER_9096,
        )
    const val SMITH_REQ = 60
    const val ENTRANCE_FEE = 2500
    const val FEE_ENTRANCE_DURATION = 1000
    const val COAL_LIMIT = 226
    const val ORE_LIMIT = 28
    const val BAR_LIMIT = 28
    const val COKE_LIMIT = 15
}
