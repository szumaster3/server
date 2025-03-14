package core.game.world.update.flag.context

import core.game.world.map.Direction
import core.game.world.map.Location

data class ForceMoveCtx(
    val start: Location, // The starting point of the move
    val dest: Location, // The endpoint of the move
    val startArrive: Int, // Indicates when the move starts
    val destArrive: Int, // Indicates when the move ends
    val direction: Direction, // Specifies the direction of the move
)
