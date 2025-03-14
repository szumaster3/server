package content.global.skill.agility.courses.brimhaven

import content.global.skill.agility.AgilityHandler
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class BrimhavenCourse : OptionHandler() {
    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val scenery = node as Scenery
        var dir = scenery.direction
        var start = player.location
        when (scenery.id) {
            3566 -> {
                start = scenery.location.transform(dir.stepX shl 1, dir.stepY shl 1, 0)
                val end = scenery.location.transform(-dir.stepX * 3, -dir.stepY * 3, 0)
                if (AgilityHandler.hasFailed(player, 1, 0.1)) {
                    AgilityHandler
                        .failWalk(
                            player,
                            2,
                            start,
                            start.transform(-dir.stepX, -dir.stepY, 0),
                            player.location.transform(0, 0, -3),
                            Animation.create(1105),
                            26,
                            getHitAmount(player),
                            "You missed the rope!",
                        ).endAnimation = Animation.RESET
                    return true
                }
                AgilityHandler.forceWalk(player, -1, start, end, Animation.create(751), 25, getExp(player, 20.0), null)
                Pulser.submit(
                    object : Pulse(1, player) {
                        var finish: Boolean = false

                        override fun pulse(): Boolean {
                            if (!finish) {
                                player.packetDispatch.sendSceneryAnimation(scenery, Animation.create(1052))
                                finish = true
                                return false
                            }
                            player.packetDispatch.sendSceneryAnimation(scenery, Animation.create(-1))
                            return true
                        }
                    },
                )
                return true
            }

            3578 -> {
                handlePillarObstacle(player, scenery)
                return true
            }

            3565 -> {
                dir = Direction.getLogicalDirection(player.location, scenery.location)
                start = player.location
                if (AgilityHandler.hasFailed(player, 1, 0.05)) {
                    val end = start.transform(dir)
                    AgilityHandler.failWalk(
                        player,
                        2,
                        start,
                        end,
                        end,
                        Animation.create(1106),
                        15,
                        getHitAmount(player),
                        "You lost your balance!",
                    )
                    AgilityHandler.forceWalk(
                        player,
                        -1,
                        end,
                        player.location,
                        ForceMovement.WALK_ANIMATION,
                        10,
                        0.0,
                        null,
                        4,
                    )
                    return true
                }
                AgilityHandler.forceWalk(
                    player,
                    -1,
                    start,
                    start.transform(dir.stepX * 3, dir.stepY * 3, 0),
                    Animation.create(1252),
                    6,
                    getExp(player, 8.0),
                    null,
                )
                return true
            }

            3553, 3557 -> {
                handleLogBalance(player, scenery)
                return true
            }

            3570, 3571, 3572 -> {
                handlePlankObstacle(player, scenery)
                return true
            }

            3559, 3561 -> {
                handleBalancingLedge(player, scenery)
                return true
            }

            3564 -> {
                handleMonkeyBars(player, scenery)
                return true
            }

            3551 -> {
                handleBalancingRope(player, scenery)
                return true
            }

            3583 -> {
                handleHandHolds(player, scenery)
                return true
            }
        }
        return false
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(3566).handlers["option:swing-on"] = this
        SceneryDefinition.forId(3578).handlers["option:jump-on"] = this
        SceneryDefinition.forId(3565).handlers["option:climb-over"] = this
        SceneryDefinition.forId(3553).handlers["option:walk-on"] = this
        SceneryDefinition.forId(3557).handlers["option:walk-on"] = this
        SceneryDefinition.forId(3570).handlers["option:walk-on"] = this
        SceneryDefinition.forId(3571).handlers["option:walk-on"] = this
        SceneryDefinition.forId(3572).handlers["option:walk-on"] = this
        SceneryDefinition.forId(3559).handlers["option:walk-across"] = this
        SceneryDefinition.forId(3561).handlers["option:walk-across"] = this
        SceneryDefinition.forId(3564).handlers["option:swing-across"] = this
        SceneryDefinition.forId(3551).handlers["option:walk-on"] = this
        SceneryDefinition.forId(3583).handlers["option:climb-across"] = this
        return this
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location? {
        val scenery = n as Scenery
        val dir = scenery.direction
        when (scenery.id) {
            3566 -> return n.getLocation().transform(dir.stepX shl 1, dir.stepY shl 1, 0)
            3564 -> {
                if (n.getLocation().x == 2771 && n.getLocation().y == 9570) {
                    return Location.create(2772, 9569, 3)
                }
                if (n.getLocation().x == 2771 && n.getLocation().y == 9577) {
                    return Location.create(2772, 9578, 3)
                }
                if (n.getLocation().x == 2793 && n.getLocation().y == 9566) {
                    return Location.create(2794, 9567, 3)
                }
                if (n.getLocation().x == 2793 && n.getLocation().y == 9559) {
                    return Location.create(2794, 9558, 3)
                }
                if (n.getLocation().x == 2781 && n.getLocation().y == 9545) {
                    return Location.create(2782, 9546, 3)
                }
                if (n.getLocation().x == 2774 && n.getLocation().y == 9545) {
                    return Location.create(2773, 9546, 3)
                }
            }

            3551 -> return n.getLocation()
        }
        return null
    }

    companion object {
        private fun handleHandHolds(
            player: Player,
            scenery: Scenery,
        ) {
            if (player.getSkills().getLevel(Skills.AGILITY) < 20) {
                player.packetDispatch.sendMessage("You need an agility of at least 20 to get past this obstacle!")
                return
            }
            var mod = 0
            var direction = scenery.direction
            val x = scenery.location.x
            val y = scenery.location.y
            if ((x == 2785 && y == 9544) || (x == 2759 && y == 9566) || (x == 2792 && y == 9592)) {
                mod = 4
                direction = Direction.get(direction.toInteger() + 2 and 3)
            }
            val m = mod
            player.lock(5)
            val dir = direction
            val faceDirection = Direction.get(scenery.direction.toInteger() - 1 and 3)
            val start = player.location
            player.appearance.setAnimations(Animation.create(1118 + m))
            player.appearance.sync()
            AgilityHandler.climb(player, -1, Animation(1117 + m), start.transform(dir), 0.0, null)
            player.logoutListeners["brimcourse"] = { p: Player ->
                p.location = start
                Unit
            }
            Pulser.submit(
                object : Pulse(3, player) {
                    var last: Location = start.transform(dir)
                    var count: Int = 0

                    override fun pulse(): Boolean {
                        if (++count == 1) {
                            if (AgilityHandler.hasFailed(player, 1, 0.15)) {
                                player.appearance.setAnimations()
                                player.appearance.sync()
                                AgilityHandler.fail(
                                    player,
                                    2,
                                    last.transform(0, 0, -3),
                                    Animation.create(1119 + m),
                                    getHitAmount(player),
                                    "You missed a hand hold!",
                                )
                                player.logoutListeners.remove("brimcourse")
                                return true
                            }
                        } else if (count == 6) {
                            player.appearance.setAnimations()
                            player.appearance.sync()
                            AgilityHandler
                                .forceWalk(
                                    player,
                                    -1,
                                    last,
                                    last.transform(dir),
                                    Animation.create(1120 + m),
                                    5,
                                    getExp(player, 22.0),
                                    null,
                                ).direction = faceDirection
                            player.logoutListeners.remove("brimcourse")
                            return true
                        }
                        player.logoutListeners.remove("brimcourse")
                        AgilityHandler
                            .forceWalk(
                                player,
                                -1,
                                last,
                                last.transform(dir).also { last = it },
                                Animation.create(1118 + m),
                                5,
                                0.0,
                                null,
                            ).direction = faceDirection
                        return false
                    }
                },
            )
        }

        private fun handleBalancingRope(
            player: Player,
            scenery: Scenery,
        ) {
            val dir = scenery.direction
            val failed = AgilityHandler.hasFailed(player, 1, 0.1)
            if (failed) {
                val end = player.location.transform(dir)
                AgilityHandler.forceWalk(player, -1, player.location, end, Animation.create(762), 10, 0.0, null)
                Pulser.submit(
                    object : Pulse(2, player) {
                        override fun pulse(): Boolean {
                            val d = Direction.get((dir.toInteger() + 3) % 4)
                            AgilityHandler.fail(
                                player,
                                2,
                                player.location.transform(d.stepX shl 1, d.stepY shl 1, -3),
                                Animation.create(764),
                                getHitAmount(player),
                                "You lost your balance!",
                            )
                            return true
                        }
                    },
                )
                return
            }
            AgilityHandler.walk(
                player,
                -1,
                player.location,
                player.location.transform(dir.stepX * 7, dir.stepY * 7, 0),
                Animation.create(155),
                getExp(player, 10.0),
                null,
            )
        }

        private fun handleMonkeyBars(
            player: Player,
            scenery: Scenery,
        ) {
            player.lock(5)
            val dir = Direction.get((scenery.direction.toInteger() + 2) % 4)
            val start = player.location
            ForceMovement.run(
                player,
                start.transform(dir),
                start.transform(dir.stepX shl 1, dir.stepY shl 1, 0),
                Animation.create(742),
                Animation.create(744),
            )
            player.logoutListeners["brimcourse"] = { p: Player ->
                p.location = start
                Unit
            }
            Pulser.submit(
                object : Pulse(2, player) {
                    var failed: Boolean = false
                    var count: Int = 0

                    override fun pulse(): Boolean {
                        if (++count == 1) {
                            if (AgilityHandler.hasFailed(player, 1, 0.15).also { failed = it }) {
                                delay = 1
                                player.animate(Animation.create(743))
                                return false
                            }
                            delay = 7
                            AgilityHandler.walk(
                                player,
                                -1,
                                player.location.transform(dir),
                                player.location.transform(dir.stepX * 7, dir.stepY * 7, 0),
                                Animation.create(662),
                                0.0,
                                null,
                            )
                        } else if (count == 2 && failed) {
                            player.appearance.setAnimations()
                            player.appearance.sync()
                            AgilityHandler.fail(
                                player,
                                2,
                                player.location.transform(0, 0, -3),
                                Animation.create(768),
                                getHitAmount(player),
                                "You missed a hand hold!",
                            )
                            player.logoutListeners.remove("brimcourse")
                            return true
                        } else {
                            player.logoutListeners.remove("brimcourse")
                            AgilityHandler.forceWalk(
                                player,
                                -1,
                                player.location,
                                player.location.transform(dir),
                                Animation.create(743),
                                10,
                                getExp(player, 14.0),
                                null,
                            )
                            return true
                        }
                        return false
                    }
                },
            )
        }

        private fun handleBalancingLedge(
            player: Player,
            scenery: Scenery,
        ) {
            val diff = if (scenery.id == 3561) 0 else 1
            val start = player.location
            val dir = Direction.getLogicalDirection(start, scenery.location)
            var end = scenery.location
            var xp = 0.0
            if (AgilityHandler.hasFailed(player, 1, 0.15)) {
                player.lock(3)
                Pulser.submit(
                    object : Pulse(2, player) {
                        override fun pulse(): Boolean {
                            val d = Direction.get((dir.toInteger() + 1) % 4)
                            AgilityHandler.fail(
                                player,
                                1,
                                player.location.transform(d.stepX, d.stepY, -3),
                                Animation.create(761 - diff),
                                getHitAmount(player),
                                "You lost your balance!",
                            )
                            return true
                        }
                    },
                )
            } else {
                xp = 16.0
                end = scenery.location.transform(dir.stepX * 6, dir.stepY * 6, 0)
            }
            AgilityHandler.walk(
                player,
                -1,
                player.location,
                end,
                Animation.create(157 - diff),
                getExp(player, xp),
                null,
            )
        }

        private fun handlePlankObstacle(
            player: Player,
            scenery: Scenery,
        ) {
            val dir = Direction.getLogicalDirection(player.location, scenery.location)
            val start = player.location
            val end = start.transform(dir.stepX * 7, dir.stepY * 7, 0)
            player.faceLocation(end)
            if (scenery.charge == 500) {
                player.lock(7)
                AgilityHandler.walk(
                    player,
                    -1,
                    start,
                    start.transform(dir.stepX * 2, dir.stepY * 2, 0),
                    Animation.create(1426),
                    0.0,
                    null,
                )
                Pulser.submit(
                    object : Pulse(3) {
                        var finish: Boolean = false

                        override fun pulse(): Boolean {
                            if (!finish) {
                                delay = 2
                                AgilityHandler.fail(
                                    player,
                                    1,
                                    player.location.transform(0, 0, -3),
                                    Animation.create(189),
                                    getHitAmount(player),
                                    "You stepped on a broken piece of plank!",
                                )
                                finish = true
                                return false
                            }
                            AgilityHandler.walk(player, -1, player.location, start.transform(dir), null, 0.0, null)
                            return true
                        }
                    },
                )
                return
            }
            AgilityHandler.walk(player, -1, start, end, Animation.create(1426), getExp(player, 6.0), null)
        }

        private fun handlePillarObstacle(
            player: Player,
            scenery: Scenery,
        ) {
            val dir = Direction.getLogicalDirection(player.location, scenery.location)
            AgilityHandler.forceWalk(
                player,
                -1,
                player.location,
                scenery.location,
                Animation.create(741),
                10,
                0.0,
                null,
            )
            val start = player.location
            player.logoutListeners["brimcourse"] = { p: Player ->
                p.location = start
                Unit
            }
            player.lock(12)
            Pulser.submit(
                object : Pulse(2, player) {
                    var count: Int = 0

                    override fun pulse(): Boolean {
                        if (count == 0 && AgilityHandler.hasFailed(player, 1, 0.15)) {
                            val d = Direction.get((dir.toInteger() + 3) % 4)
                            player.unlock()
                            player.lock(2)
                            AgilityHandler.fail(
                                player,
                                2,
                                player.location.transform(d.stepX shl 1, d.stepY shl 1, -3),
                                Animation.create(764),
                                getHitAmount(player),
                                "You lost your balance!",
                            )
                            player.logoutListeners.remove("brimcourse")
                            return true
                        }
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            player.location,
                            player.location.transform(dir),
                            Animation.create(741),
                            10,
                            if (count == 5) getExp(player, 18.0) else 0.0,
                            null,
                        )
                        player.logoutListeners.remove("brimcourse")
                        return ++count == 6
                    }
                },
            )
        }

        private fun handleLogBalance(
            player: Player,
            scenery: Scenery,
        ) {
            val dir = Direction.getLogicalDirection(player.location, scenery.location)
            val failed = AgilityHandler.hasFailed(player, 1, 0.1)
            if (failed) {
                val end = player.location.transform(dir)
                AgilityHandler.forceWalk(player, -1, player.location, end, Animation.create(762), 10, 0.0, null)
                Pulser.submit(
                    object : Pulse(2, player) {
                        override fun pulse(): Boolean {
                            val d = Direction.get((dir.toInteger() + 3) % 4)
                            AgilityHandler.fail(
                                player,
                                2,
                                player.location.transform(d.stepX shl 1, d.stepY shl 1, -3),
                                Animation.create(764),
                                getHitAmount(player),
                                "You lost your balance!",
                            )
                            return true
                        }
                    },
                )
                return
            }
            AgilityHandler.walk(
                player,
                -1,
                player.location,
                player.location.transform(dir.stepX * 7, dir.stepY * 7, 0),
                Animation.create(155),
                getExp(player, 12.0),
                null,
            )
        }

        private fun getExp(
            player: Player,
            exp: Double,
        ): Double {
            return if (player.achievementDiaryManager.karamjaGlove > 1) exp + (exp * 0.10) else exp
        }

        private fun getHitAmount(player: Player): Int {
            var hit = player.getSkills().lifepoints / 12
            if (hit < 2) {
                hit = 2
            }
            return hit
        }
    }
}
