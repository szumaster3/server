package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction

@Initializable
class MonkeyBarShortcut : AgilityShortcut {
    constructor() : super(intArrayOf(29375), 1, 14.0, "swing across")
    constructor(ids: IntArray?, level: Int, experience: Double, option: String?) : super(ids, level, experience, option)

    override fun newInstance(arg: Any?): Plugin<Any> {
        configure(MonkeyBarShortcut(intArrayOf(2321), 57, 20.0, "swing across"))
        return super.newInstance(arg)
    }

    override fun run(
        player: Player,
        scenery: Scenery,
        option: String,
        failed: Boolean,
    ) {
        player.lock(5)
        var direct = Direction.get((scenery.direction.toInteger() + 2) % 4)
        if (scenery.id == 29375) {
            if (direct == Direction.SOUTH && player.location.y < 9969) {
                direct = Direction.NORTH
            } else if (direct == Direction.NORTH && player.location.y >= 9969) {
                direct = Direction.SOUTH
            }
        } else if (scenery.id == 2321) {
            direct =
                if (player.location.y >= 9494) {
                    Direction.SOUTH
                } else {
                    Direction.NORTH
                }
        }

        val start = player.location
        val dir = direct
        var failed: Boolean = false
        ForceMovement.run(
            player,
            start.transform(dir),
            start.transform(dir.stepX shl 1, dir.stepY shl 1, 0),
            Animation.create(742),
            Animation.create(744),
        )
        player.logoutListeners["monkey-bar"] = { p: Player ->
            p.location = start
        }
        Pulser.submit(
            object : Pulse(2, player) {
                var count: Int = 0

                override fun pulse(): Boolean {
                    if (++count == 1) {
                        if (scenery.id == 2321 && (AgilityHandler.hasFailed(player, 57, 0.01).also { failed = it })) {
                            delay = 1
                            player.animate(Animation.create(743))
                            return false
                        }
                        delay = 4
                        AgilityHandler.walk(
                            player,
                            -1,
                            player.location.transform(dir),
                            player.location.transform(dir.stepX * 4, dir.stepY * 4, 0),
                            Animation.create(662),
                            0.0,
                            null,
                        )
                    } else if (count == 2) {
                        if (failed) {
                            player.appearance.setAnimations()
                            player.appearance.sync()
                            AgilityHandler.fail(
                                player,
                                2,
                                Location(2599, 9564, 0),
                                Animation.create(768),
                                RandomFunction.random(1, 3),
                                null,
                            )
                            player.logoutListeners.remove("monkey-bar")
                            return true
                        }
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            player.location,
                            player.location.transform(dir),
                            Animation.create(743),
                            10,
                            experience,
                            null,
                        )
                        player.logoutListeners.remove("monkey-bar")
                        return true
                    }
                    return false
                }
            },
        )
    }

    override fun getDestination(
        n: Node,
        node: Node,
    ): Location? {
        if (node.location == Location(2598, 9489, 0)) {
            return Location(2597, 9488, 0)
        } else if (node.location == Location(2598, 9494, 0)) {
            return Location(2597, 9495, 0)
        } else if (node.location == Location(2599, 9489, 0)) {
            return Location(2600, 9488, 0)
        }
        for (locations in MBAR_LOCATIONS) {
            if (n.location == locations[0]) {
                return locations[1]
            }
        }
        if (node.location == Location(2598, 9489, 0) || node.location == Location(2599, 9489, 0)) {
            return Location(2600, 9495, 0)
        }
        return null
    }

    companion object {
        private val MBAR_LOCATIONS =
            arrayOf(
                arrayOf(Location(3120, 9969, 0), Location.create(3121, 9969, 0)),
                arrayOf(Location(3119, 9969, 0), Location.create(3120, 9969, 0)),
                arrayOf(Location(3120, 9964, 0), Location.create(3121, 9964, 0)),
                arrayOf(Location.create(3120, 9963, 0), Location.create(3120, 9964, 0)),
                arrayOf(Location(2598, 9489, 0), Location(2597, 9488, 0)),
                arrayOf(Location(2598, 9489, 0), Location(2600, 9488, 0)),
                arrayOf(Location(2598, 9494, 0), Location(2597, 9495, 0)),
                arrayOf(Location(2599, 9494, 0), Location(2600, 9495, 0)),
            )
    }
}
