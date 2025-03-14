package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class LogBalanceShortcut : AgilityShortcut {
    private var start: Location
    private var end: Location

    constructor(
        ids: IntArray,
        level: Int,
        experience: Double,
        start: Location,
        end: Location,
        vararg options: String,
    ) : super(ids, level, experience, *options) {
        this.start = start
        this.end = end
    }

    constructor() : this(intArrayOf(), 0, 0.0, Location(0, 0, 0), Location(0, 0, 0))

    override fun newInstance(arg: Any?): Plugin<Any> {
        configure(
            LogBalanceShortcut(
                intArrayOf(2296),
                20,
                5.0,
                Location(2598, 3477, 0),
                Location.create(2603, 3477, 0),
                "walk-across",
            ),
        )
        configure(
            LogBalanceShortcut(
                intArrayOf(2332),
                1,
                1.0,
                Location.create(2910, 3049, 0),
                Location.create(2906, 3049, 0),
                "cross",
            ),
        )
        configure(
            LogBalanceShortcut(
                intArrayOf(3933),
                45,
                1.0,
                Location.create(2290, 3232, 0),
                Location.create(2290, 3239, 0),
                "cross",
            ),
        )
        configure(
            LogBalanceShortcut(
                intArrayOf(3932),
                45,
                1.0,
                Location.create(2258, 3250, 0),
                Location.create(2264, 3250, 0),
                "cross",
            ),
        )
        configure(
            LogBalanceShortcut(
                intArrayOf(3931),
                45,
                1.0,
                Location.create(2202, 3237, 0),
                Location.create(2196, 3237, 0),
                "cross",
            ),
        )
        return this
    }

    override fun run(
        player: Player,
        scenery: Scenery,
        option: String,
        failed: Boolean,
    ) {
        val distanceToStart = player.location.getDistance(start)
        val distanceToEnd = player.location.getDistance(end)
        val destination =
            when {
                distanceToStart < distanceToEnd -> end
                distanceToStart > distanceToEnd -> start
                else -> end
            }
        AgilityHandler.walk(player, -1, player.location, destination, Animation.create(155), experience, null)
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location {
        return if (node.location.getDistance(start) < node.location.getDistance(end)) start else end
    }
}
