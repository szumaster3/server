package content.global.activity.tog

import core.api.TickListener
import core.api.addScenery
import core.api.getScenery
import core.game.world.map.Location
import shared.consts.Scenery

/**
 * Handles interaction for tog activity
 * @author ovenbreado
 */
class TearsOfGuthixListener : TickListener {
    companion object {
        var ticks = 0
        var globalWallState = intArrayOf(0, 0, 2, 1, 2, 1, 0, 0, 2, 1)
        val allWalls =
            arrayOf(
                // Blank
                Location(0, 0, 0),
                // Left Walls
                Location(3258, 9520, 2),
                Location(3261, 9516, 2),
                Location(3261, 9518, 2),
                Location(3257, 9514, 2),
                Location(3259, 9514, 2),
                // Right Walls
                Location(3257, 9520, 2),
                Location(3259, 9520, 2),
                Location(3261, 9517, 2),
                Location(3258, 9514, 2),
            )
    }

    override fun tick() {
        if (ticks++ > 10) {
            ticks = 0
        } else {
            return
        }
        val wallStates = intArrayOf(0, 0, 0, 1, 1, 1, 2, 2, 2)
        wallStates.shuffle()
        globalWallState = intArrayOf(0) + wallStates

        /*
         * Explanation: The walls are layered sceneries, which makes it rabidly fucked to change them.
         * What I did was to add the tears scenery first (essentially overriding the tears scenery),
         * then add the WEEPING_WALL_6660 right after it so that the interactions are still there.
         * this is how a layer is like:
         * 1 - WEEPING_WALL_6660 - No model, but holds the option "collect-from"
         * 2 - BLUE/GREEN/ABSENT - Model of the blue/green/absent waterfall.
         * 3 - WEEPING_WALL_6664 - The actual model, but not interactive.
         * 6661 - 6664 is left side, 6665 to 6668 is right side
         */
        wallStates.forEachIndexed { index, state ->
            val scenery = getScenery(allWalls[index + 1])!!
            val newSceneryId =
                if (state == 2) {
                    if (index + 1 <= 5) {
                        Scenery.GREEN_TEARS_6662
                    } else {
                        Scenery.GREEN_TEARS_6666
                    }
                } else if (state == 1) {
                    if (index + 1 <= 5) {
                        Scenery.BLUE_TEARS_6661
                    } else {
                        Scenery.BLUE_TEARS_6665
                    }
                } else {
                    if (index + 1 <= 5) {
                        Scenery.ABSENCE_OF_TEARS_6663
                    } else {
                        Scenery.ABSENCE_OF_TEARS_6667
                    }
                }
            addScenery(
                core.game.node.scenery.Scenery(
                    newSceneryId,
                    scenery.location,
                    4,
                    scenery.rotation,
                ),
            )
            addScenery(
                core.game.node.scenery
                    .Scenery(Scenery.WEEPING_WALL_6660, scenery.location, 0, scenery.rotation),
            )
        }
    }
}