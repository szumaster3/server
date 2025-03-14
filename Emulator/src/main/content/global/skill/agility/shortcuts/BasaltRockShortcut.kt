package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.api.faceLocation
import core.api.lock
import core.api.sendMessage
import core.api.submitWorldPulse
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class BasaltRockShortcut : AgilityShortcut {
    constructor() : super(intArrayOf(), 0, 0.0, "")

    constructor(ids: IntArray?, level: Int, experience: Double, vararg options: String?) : super(
        ids,
        level,
        experience,
        *options,
    )

    override fun newInstance(arg: Any?): Plugin<Any> {
        configure(BasaltRockShortcut(intArrayOf(org.rs.consts.Scenery.BEACH_4550), 1, 0.0, "jump-to"))
        configure(BasaltRockShortcut(intArrayOf(org.rs.consts.Scenery.BASALT_ROCK_4551), 1, 0.0, "jump-across"))
        configure(BasaltRockShortcut(intArrayOf(org.rs.consts.Scenery.BASALT_ROCK_4552), 1, 0.0, "jump-across"))
        configure(BasaltRockShortcut(intArrayOf(org.rs.consts.Scenery.BASALT_ROCK_4553), 1, 0.0, "jump-across"))
        configure(BasaltRockShortcut(intArrayOf(org.rs.consts.Scenery.BASALT_ROCK_4554), 1, 0.0, "jump-across"))
        configure(BasaltRockShortcut(intArrayOf(org.rs.consts.Scenery.BASALT_ROCK_4555), 1, 0.0, "jump-across"))
        configure(BasaltRockShortcut(intArrayOf(org.rs.consts.Scenery.BASALT_ROCK_4556), 1, 0.0, "jump-across"))
        configure(BasaltRockShortcut(intArrayOf(org.rs.consts.Scenery.BASALT_ROCK_4557), 1, 0.0, "jump-across"))
        configure(BasaltRockShortcut(intArrayOf(org.rs.consts.Scenery.BASALT_ROCK_4558), 1, 0.0, "jump-across"))
        configure(BasaltRockShortcut(intArrayOf(org.rs.consts.Scenery.ROCKY_SHORE_4559), 1, 0.0, "jump-to"))
        return this
    }

    override fun getDestination(
        n: Node?,
        node: Node,
    ): Location? {
        val obj = node as Scenery
        when (obj.id) {
            4550 -> {
                return Location.create(2522, 3597, 0)
            }

            4551 -> {
                return Location.create(2522, 3595, 0)
            }

            4552 -> {
                return Location.create(2522, 3602, 0)
            }

            4553 -> {
                return Location.create(2522, 3600, 0)
            }

            4554 -> {
                return Location.create(2516, 3611, 0)
            }

            4555 -> {
                return Location.create(2518, 3611, 0)
            }

            4556 -> {
                return Location.create(2514, 3615, 0)
            }

            4557 -> {
                return Location.create(2514, 3613, 0)
            }

            4558 -> {
                return Location.create(2514, 3619, 0)
            }

            4559 -> {
                return Location.create(2514, 3617, 0)
            }

            else -> return super.getDestination(obj, node)
        }
    }

    override fun run(
        player: Player,
        obj: Scenery,
        option: String,
        failed: Boolean,
    ) {
        submitWorldPulse(
            object : Pulse(1, player) {
                override fun pulse(): Boolean {
                    when (obj.id) {
                        4550 -> {
                            if (player.location.y <= 3596) {
                                sendMessage(player, "I can't reach.")
                            } else {
                                lock(player, 3)
                                AgilityHandler.forceWalk(
                                    player,
                                    -1,
                                    Location.create(2522, 3597, 0),
                                    Location.create(2522, 3595, 0),
                                    Animation.create(769),
                                    20,
                                    0.0,
                                    null,
                                    1,
                                )
                            }
                            return true
                        }

                        4551 -> {
                            if (player.location == Location.create(2522, 3597, 0)) {
                                sendMessage(player, "I can't reach.")
                            } else {
                                lock(player, 3)
                                AgilityHandler.forceWalk(
                                    player,
                                    -1,
                                    Location.create(2522, 3595, 0),
                                    Location.create(2522, 3597, 0),
                                    Animation.create(769),
                                    20,
                                    0.0,
                                    null,
                                    1,
                                )
                            }
                            return true
                        }

                        4552 -> {
                            if (player.location == Location.create(2522, 3600, 0)) {
                                sendMessage(player, "I can't reach.")
                            } else {
                                lock(player, 3)
                                AgilityHandler.forceWalk(
                                    player,
                                    -1,
                                    Location.create(2522, 3602, 0),
                                    Location.create(2522, 3600, 0),
                                    Animation.create(769),
                                    20,
                                    0.0,
                                    null,
                                )
                            }
                            return true
                        }

                        4553 -> {
                            if (player.location == Location.create(2522, 3602, 0)) {
                                sendMessage(player, "I can't reach.")
                            } else {
                                lock(player, 3)
                                AgilityHandler.forceWalk(
                                    player,
                                    -1,
                                    Location.create(2522, 3600, 0),
                                    Location.create(2522, 3602, 0),
                                    Animation.create(769),
                                    20,
                                    0.0,
                                    null,
                                )
                            }
                            return true
                        }

                        4554 -> {
                            if (player.location == Location.create(2518, 3611, 0)) {
                                sendMessage(player, "I can't reach.")
                            } else {
                                lock(player, 3)
                                faceLocation(player, Location.create(2518, 3611, 0))
                                AgilityHandler.forceWalk(
                                    player,
                                    -1,
                                    Location.create(2516, 3611, 0),
                                    Location.create(2518, 3611, 0),
                                    Animation.create(769),
                                    20,
                                    0.0,
                                    null,
                                    1,
                                )
                            }
                            return true
                        }

                        4555 -> {
                            if (player.location == Location.create(2516, 3611, 0)) {
                                sendMessage(player, "I can't reach.")
                            } else {
                                lock(player, 3)
                                faceLocation(player, Location.create(2516, 3611, 0))
                                AgilityHandler.forceWalk(
                                    player,
                                    -1,
                                    Location.create(2518, 3611, 0),
                                    Location.create(2516, 3611, 0),
                                    Animation.create(769),
                                    20,
                                    0.0,
                                    null,
                                    1,
                                )
                            }
                            return true
                        }

                        4556 -> {
                            if (player.location == Location.create(2514, 3613, 0)) {
                                sendMessage(player, "I can't reach.")
                            } else {
                                lock(player, 3)
                                AgilityHandler.forceWalk(
                                    player,
                                    -1,
                                    Location.create(2514, 3615, 0),
                                    Location.create(2514, 3613, 0),
                                    Animation.create(769),
                                    20,
                                    0.0,
                                    null,
                                )
                            }
                            return true
                        }

                        4557 -> {
                            if (player.location == Location.create(2514, 3615, 0)) {
                                sendMessage(player, "I can't reach.")
                            } else {
                                lock(player, 3)
                                AgilityHandler.forceWalk(
                                    player,
                                    -1,
                                    Location.create(2514, 3613, 0),
                                    Location.create(2514, 3615, 0),
                                    Animation.create(769),
                                    20,
                                    0.0,
                                    null,
                                )
                            }
                            return true
                        }

                        4558 -> {
                            if (player.location == Location.create(2514, 3617, 0)) {
                                sendMessage(player, "I can't reach.")
                            } else {
                                lock(player, 3)
                                AgilityHandler.forceWalk(
                                    player,
                                    -1,
                                    Location.create(2514, 3619, 0),
                                    Location.create(2514, 3617, 0),
                                    Animation.create(769),
                                    20,
                                    0.0,
                                    null,
                                )
                            }
                            return true
                        }

                        4559 -> {
                            if (player.location.y >= 3618) {
                                sendMessage(player, "I can't reach.")
                            } else {
                                lock(player, 3)
                                AgilityHandler.forceWalk(
                                    player,
                                    -1,
                                    Location.create(2514, 3617, 0),
                                    Location.create(2514, 3619, 0),
                                    Animation.create(769),
                                    20,
                                    0.0,
                                    null,
                                    1,
                                )
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
