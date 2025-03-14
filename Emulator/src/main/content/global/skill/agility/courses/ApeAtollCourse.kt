package content.global.skill.agility.courses

import content.global.skill.agility.AgilityCourse
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.node.Node
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Items

@Initializable
class ApeAtollCourse
    @JvmOverloads
    constructor(
        player: Player? = null,
    ) : AgilityCourse(player, 6, 450.0) {
        override fun createInstance(player: Player?): AgilityCourse {
            return ApeAtollCourse(player)
        }

        override fun handle(
            player: Player,
            node: Node?,
            option: String?,
        ): Boolean {
            getCourse(player)
            val n = node as Scenery
            when (n.id) {
                12568 -> {
                    jumpSteppingStone(player, node)
                    return true
                }

                12570 -> {
                    climbUpTropicalTree(player, node)
                    return true
                }

                12573 -> {
                    crossMonkeyBars(player, node)
                    return true
                }

                12576 -> {
                    climbUpSkullSlope(player, node)
                    return true
                }

                12578 -> {
                    swingRope(player, node)
                    return true
                }

                12618 -> {
                    climbDownTropicalTree(player, node)
                    return true
                }
            }
            return false
        }

        override fun configure() {
            SceneryDefinition.forId(12568).handlers["option:jump-to"] = this
            SceneryDefinition.forId(12570).handlers["option:climb"] = this
            SceneryDefinition.forId(12573).handlers["option:swing across"] = this
            SceneryDefinition.forId(12576).handlers["option:climb-up"] = this
            SceneryDefinition.forId(12578).handlers["option:swing"] = this
            SceneryDefinition.forId(12618).handlers["option:climb-down"] = this
        }

        private fun jumpSteppingStone(
            player: Player,
            scenery: Scenery,
        ) {
            if (!hasLevelDyn(player, Skills.AGILITY, 48)) {
                sendMessage(player, "You need an agility level of at least 48 to do this.")
            } else if (!inEquipment(player, Items.MONKEY_GREEGREE_4024)) {
                sendMessage(player, "You need to transform into a ninja monkey to use the ape atoll courses.")
            } else if (!player.location.withinDistance(scenery.asScenery().location, 2)) {
                return
            } else {
                player.lock(3)
                val toTile2 =
                    Location(
                        if (player.location.x ==
                            2755
                        ) {
                            2753
                        } else {
                            2755
                        },
                        2742,
                        scenery.asScenery().location.z,
                    )
                val waterTile = Location(2756, 2746, scenery.asScenery().location.z)
                sendMessage(player, "You jump to the stepping stone...")
                GameWorld.Pulser.submit(
                    object : Pulse(1) {
                        var count = 0
                        val fail =
                            content.global.skill.agility.AgilityHandler
                                .hasFailed(player, 48, 0.5)

                        override fun pulse(): Boolean {
                            when (count++) {
                                0 -> {
                                    animate(player, 3481)
                                }

                                1 -> {
                                    teleport(player, Location.create(2754, 2742, 0))
                                }

                                2 ->
                                    if (fail) {
                                        animate(player, 3489)
                                        sendMessage(player, "..And accidently fall to the water.")
                                        player.impactHandler.manualHit(player, 2, ImpactHandler.HitsplatType.NORMAL)
                                        forceMove(player, waterTile, waterTile, 25, 60, player.location.direction, 3489)
                                        teleport(player, location(2754, 2743, 0))
                                        resetAnimator(player)
                                        return false
                                    } else {
                                        animate(player, 3481)
                                        teleport(player, toTile2)
                                        rewardXP(player, Skills.AGILITY, 15.00)
                                        sendMessage(player, "..And made it carefully to the other side.")
                                        return true
                                    }
                            }
                            return false
                        }
                    },
                )
            }
        }

        private fun climbUpTropicalTree(
            player: Player,
            scenery: Scenery,
        ) {
            if (!hasLevelDyn(player, Skills.AGILITY, 48)) {
                sendMessage(player, "You need an agility level of at least 48 to do this.")
            } else if (!inEquipment(player, Items.MONKEY_GREEGREE_4024)) {
                sendMessage(player, "You need to transform into a ninja monkey to use the ape atoll courses.")
            } else {
                if (!player.location.withinDistance(scenery.asScenery().location, 2)) return
                player.lock(3)
                GameWorld.Pulser.submit(
                    object : Pulse(0) {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> animate(player, 3487)
                                3 -> {
                                    sendMessage(player, "You climb up the tree...")
                                    teleport(player, Location.create(2753, 2742, 2))
                                    rewardXP(player, Skills.AGILITY, 25.0)
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }
        }

        private fun climbUpSkullSlope(
            player: Player,
            scenery: Scenery,
        ) {
            if (!hasLevelDyn(player, Skills.AGILITY, 48)) {
                sendMessage(player, "You need an agility level of at least 48 to do this.")
            } else if (!inEquipment(player, Items.MONKEY_GREEGREE_4024)) {
                sendMessage(player, "You need to transform into a ninja monkey to use the ape atoll courses.")
            } else if (!player.location.withinDistance(scenery.asScenery().location, 2)) {
                return
            } else {
                if (player.location.x < 2747) return
                lock(player, 3)
                val toTile = location(2743, 2741, 0)
                player.walkingQueue.addPath(toTile.x, toTile.y, false)
                animate(player, 3485)
                GameWorld.Pulser.submit(
                    object : Pulse(0, player) {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                2 -> {
                                    sendMessage(player, "You climb up the skull slope.")
                                    forceMove(
                                        player,
                                        player.location.transform(toTile),
                                        toTile,
                                        25,
                                        60,
                                        player.location.direction,
                                        3489,
                                    )
                                    rewardXP(player, Skills.AGILITY, 45.00)
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }
        }

        private fun crossMonkeyBars(
            player: Player,
            scenery: Scenery,
        ) {
            if (!hasLevelDyn(player, Skills.AGILITY, 48)) {
                sendMessage(player, "You need an agility level of at least 48 to do this.")
            } else if (!inEquipment(player, Items.MONKEY_GREEGREE_4024)) {
                sendMessage(player, "You need to transform into a ninja monkey to use the ape atoll courses.")
            } else if (!player.location.withinDistance(scenery.asScenery().location, 2)) {
                return
            } else {
                player.lock(4)
                val toTile = location(2747, 2741, 0)
                val toTile2 = location(2747, 2741, 2)
                animate(player, Animation(3482))
                player.walkingQueue.addPath(toTile2.x, toTile2.y, false)
                sendMessage(player, "You jump to the monkey bars...")
                GameWorld.Pulser.submit(
                    object : Pulse(0) {
                        var count = 0

                        override fun pulse(): Boolean {
                            when (count++) {
                                3 -> {
                                    sendMessage(player, "..And made it carefully to the other side.")
                                    teleport(player, toTile)
                                    rewardXP(player, Skills.AGILITY, 35.00)
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }
        }

        private fun swingRope(
            player: Player,
            scenery: Scenery,
        ) {
            if (!hasLevelStat(player, Skills.AGILITY, 48)) {
                sendMessage(player, "You need an agility level of at least 48 to do this.")
            } else if (!inEquipment(player, Items.MONKEY_GREEGREE_4024)) {
                sendMessage(player, "You need to transform into a ninja monkey to use the ape atoll courses.")
            } else if (!player.location.withinDistance(scenery.asScenery().location, 2)) {
                return
            } else if (player.location.x == 2756) {
                return
            } else {
                player.lock(4)
                val toTile = location(2756, 2731, scenery.location.z)
                animate(player, 1388)
                animateScenery(player, scenery.asScenery(), 2231, true)
                player.walkingQueue.addPath(toTile.x, toTile.y, false)
                sendMessage(player, "You skillfully swing across.")
                GameWorld.Pulser.submit(
                    object : Pulse() {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                2 -> {
                                    teleport(player, toTile)
                                    rewardXP(player, Skills.AGILITY, 22.00)
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }
        }

        private fun climbDownTropicalTree(
            player: Player,
            scenery: Scenery,
        ) {
            if (!hasLevelDyn(player, Skills.AGILITY, 48)) {
                sendMessage(player, "You need an agility level of at least 48 to do this.")
            } else if (!inEquipment(player, Items.MONKEY_GREEGREE_4024)) {
                sendMessage(player, "You need to transform into a ninja monkey to use the ape atoll courses.")
            } else if (!player.location.withinDistance(scenery.asScenery().location, 2)) {
                return
            } else {
                player.lock()
                val toTile = location(2770, 2747, 0)
                val toTile2 = location(2770, 2747, 1)
                player.walkingQueue.addPath(toTile.x, toTile.y, false)
                player.walkingQueue.addPoint(2758, 2735, false)
                animate(player, 3494)
                GameWorld.Pulser.submit(
                    object : Pulse() {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                1 -> ForceMovement.run(player, player.location, toTile, Animation(3494))
                                9 -> {
                                    animate(player, 3488)
                                    sendMessage(player, "..And make it carefully to the end of it.")
                                    ForceMovement.run(player, toTile2)
                                    player.unlock()
                                    finish()
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
