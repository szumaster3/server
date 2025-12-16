package content.global.travel.balloon.routes

import content.global.travel.balloon.routes.screens.impl.TaverleyRouteScreen
import core.game.node.entity.player.Player

/**
 * Represents the top and bottom positions of a balloon on an interface.
 */
data class BalloonBasePosition(
    val top: Int,
    val bottom: Int
)

/**
 * Represents a complete balloon route consisting of three sequential stages.
 *
 * Each stage has:
 * A sequence of button ids that must be clicked in order to progress.
 * An overlay rendering function that updates the interface for that stage.
 * A base position for the balloon top and bottom models.
 */
data class RouteData(
    val firstSequence: List<Int>,
    val secondSequence: List<Int>,
    val thirdSequence: List<Int>,
    val firstOverlay: (Player, Int) -> Unit,
    val secondOverlay: (Player, Int) -> Unit,
    val thirdOverlay: (Player, Int) -> Unit,
    val startPosition: List<BalloonBasePosition>
)

// Sequences map:
// Logs - 9
// Sandbag - 4
// Relax - 5
// Rope - 6
// Red rope - 10

object BalloonRoutes {

    // TODO: Decouple stage progress from hardcoded sequences.
    // https://runescape.wiki/w/Balloon_transport_system#Walkthrough


    val taverleyRoute = RouteData(
        firstSequence = listOf(9, 4, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 5, 5, 5, 5, 5, 5), // Aug 8, 2008
        secondSequence = listOf(9, 5, 5, 5, 5, 5, 5, 4, 5, 5, 5, 5, 5, 5, 5, 5),
        thirdSequence = listOf(5, 5, 5, 5, 5, 5, 10, 6, 5, 5, 5, 9, 5, 5, 5, 6),
        firstOverlay = TaverleyRouteScreen::firstStage,
        secondOverlay = TaverleyRouteScreen::secondStage,
        thirdOverlay = TaverleyRouteScreen::thirdStage,
        startPosition = listOf(
            BalloonBasePosition(top = 118, bottom = 99),  // first stage
            BalloonBasePosition(top = 120, bottom = 100), // second stage
            BalloonBasePosition(top = 179, bottom = 159)  // third stage
        )
    )

    val craftingGuildRoute = RouteData(
        firstSequence = emptyList(),
        secondSequence = emptyList(),
        thirdSequence = emptyList(),
        firstOverlay = { _, _ -> },
        secondOverlay = { _, _ -> },
        thirdOverlay = { _, _ -> },
        startPosition = listOf(
            BalloonBasePosition(top = 0, bottom = 0), // first stage
            BalloonBasePosition(top = 0, bottom = 0), // second stage
            BalloonBasePosition(top = 0, bottom = 0)  // third stage
        )
    )

    val varrockRoute = RouteData(
        firstSequence = emptyList(),
        secondSequence = emptyList(),
        thirdSequence = emptyList(),
        firstOverlay = { _, _ -> },
        secondOverlay = { _, _ -> },
        thirdOverlay = { _, _ -> },
        startPosition = listOf(
            BalloonBasePosition(top = 0, bottom = 0), // first stage
            BalloonBasePosition(top = 0, bottom = 0), // second stage
            BalloonBasePosition(top = 0, bottom = 0)  // third stage
        )
    )

    val castleWarsRoute = RouteData(
        firstSequence = emptyList(),
        secondSequence = emptyList(),
        thirdSequence = emptyList(),
        firstOverlay = { _, _ -> },
        secondOverlay = { _, _ -> },
        thirdOverlay = { _, _ -> },
        startPosition = listOf(
            BalloonBasePosition(top = 0, bottom = 0), // first stage
            BalloonBasePosition(top = 0, bottom = 0), // second stage
            BalloonBasePosition(top = 0, bottom = 0)  // third stage
        )
    )

    val grandTreeRoute = RouteData(
        firstSequence = emptyList(),
        secondSequence = emptyList(),
        thirdSequence = emptyList(),
        firstOverlay = { _, _ -> },
        secondOverlay = { _, _ -> },
        thirdOverlay = { _, _ -> },
        startPosition = listOf(
            BalloonBasePosition(top = 0, bottom = 0), // first stage
            BalloonBasePosition(top = 0, bottom = 0), // second stage
            BalloonBasePosition(top = 0, bottom = 0)  // third stage
        )
    )

    val routes: Map<Int, RouteData> = mapOf(
        1 to taverleyRoute,
        2 to craftingGuildRoute,
        3 to varrockRoute,
        4 to castleWarsRoute,
        5 to grandTreeRoute
    )
}
