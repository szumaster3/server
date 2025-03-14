package content.region.morytania.handlers.tarnslair.traps

import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.Scenery

enum class FloorTrap(
    val trap: TrapScenery,
    val forceTo: Location,
    val location: Location,
    val pushTo: Location,
    val direction: Direction,
) {
    FLOOR1_TRAP_1(
        TrapScenery(Scenery.WALL_20920, Location(3195, 4557, 0)),
        Location(3196, 4559),
        Location(3196, 4557),
        Location(3196, 4556),
        Direction.NORTH,
    ),
    FLOOR1_TRAP_2(
        TrapScenery(Scenery.WALL_20920, Location(3195, 4558, 0)),
        Location(3196, 4556),
        Location(3196, 4558),
        Location(3196, 4559),
        Direction.SOUTH,
    ),
    FLOOR1_TRAP_3(
        TrapScenery(Scenery.WALL_20920, Location(3196, 4562, 0)),
        Location(3196, 4564),
        Location(3196, 4562),
        Location(3196, 4561),
        Direction.NORTH,
    ),
    FLOOR1_TRAP_4(
        TrapScenery(Scenery.WALL_20920, Location(3196, 4563, 0)),
        Location(3196, 4561),
        Location(3196, 4563),
        Location(3196, 4564),
        Direction.SOUTH,
    ),
    FLOOR2_TRAP_1(
        TrapScenery(Scenery.WALL_20920, Location(3146, 4588, 1)),
        Location(3148, 4589, 1),
        Location(3146, 4589, 1),
        Location(3145, 4589, 1),
        Direction.EAST,
    ),
    FLOOR2_TRAP_2(
        TrapScenery(Scenery.WALL_20920, Location(3147, 4588, 1)),
        Location(3145, 4589, 1),
        Location(3147, 4589, 1),
        Location(3148, 4589, 1),
        Direction.WEST,
    ),
    FLOOR2_TRAP_3(
        TrapScenery(Scenery.WALL_20920, Location(3151, 4589, 1)),
        Location(3153, 4589, 1),
        Location(3151, 4589, 1),
        Location(3150, 4589, 1),
        Direction.EAST,
    ),
    FLOOR2_TRAP_4(
        TrapScenery(Scenery.WALL_20920, Location(3152, 4589, 1)),
        Location(3150, 4589, 1),
        Location(3152, 4589, 1),
        Location(3153, 4589, 1),
        Direction.WEST,
    ),
    ;

    companion object {
        val trapMap: HashMap<Location, FloorTrap> = HashMap()

        init {
            for (trap in values()) {
                trapMap[trap.trap.location] = trap
            }
        }

        fun getFromCoords(
            x: Int,
            y: Int,
        ): FloorTrap? {
            return trapMap[Location(x, y)]
        }
    }
}

data class TrapScenery(
    val id: Int,
    val location: Location,
)
