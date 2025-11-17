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
import shared.consts.Items
import shared.consts.Scenery as Objects

@Initializable
class ApeAtollCourse : AgilityCourse {

    constructor() : super(null, 6, 450.0)
    constructor(player: Player?) : super(player, 6, 450.0)

    override fun createInstance(player: Player): AgilityCourse = ApeAtollCourse(player)

    override fun handle(player: Player, node: Node?, option: String?): Boolean {
        val scenery = node as? Scenery ?: return false
        getCourse(player)
        return when (scenery.id) {
            Objects.STEPPING_STONE_12568 -> jumpSteppingStone(player, scenery)
            Objects.TROPICAL_TREE_12570  -> climbUpTropicalTree(player, scenery)
            Objects.MONKEYBARS_12573     -> crossMonkeyBars(player, scenery)
            Objects.SKULL_SLOPE_12576    -> climbUpSkullSlope(player, scenery)
            Objects.ROPE_12578           -> swingRope(player, scenery)
            Objects.TROPICAL_TREE_12618  -> climbDownTropicalTree(player, scenery)
            else -> false
        }
    }

    override fun configure() {
        SceneryDefinition.forId(Objects.STEPPING_STONE_12568).handlers["option:jump-to"]      = this
        SceneryDefinition.forId(Objects.TROPICAL_TREE_12570 ).handlers["option:climb"]        = this
        SceneryDefinition.forId(Objects.MONKEYBARS_12573    ).handlers["option:swing across"] = this
        SceneryDefinition.forId(Objects.SKULL_SLOPE_12576   ).handlers["option:climb-up"]     = this
        SceneryDefinition.forId(Objects.ROPE_12578          ).handlers["option:swing"]        = this
        SceneryDefinition.forId(Objects.TROPICAL_TREE_12618 ).handlers["option:climb-down"]   = this
    }

    private fun checkRequirements(player: Player, level: Int = 48): Boolean {
        if (!hasLevelDyn(player, Skills.AGILITY, level)) {
            sendMessage(player, "You need an Agility level of at least $level to do this.")
            return false
        }
        if (!inEquipment(player, Items.MONKEY_GREEGREE_4024)) {
            sendMessage(player, "You need to transform into a ninja monkey to use the ape atoll courses.")
            return false
        }
        return true
    }

    private fun jumpSteppingStone(player: Player, scenery: Scenery): Boolean {
        if (!checkRequirements(player) || !player.location.withinDistance(scenery.location, 2)) return true
        player.walkingQueue.reset()

        player.lock(3)
        val toTile = Location(if (player.location.x == 2755) 2753 else 2755, 2742, scenery.location.z)
        val waterTile = Location(2756, 2746, scenery.location.z)
        sendMessage(player, "You jump to the stepping stone...")

        GameWorld.Pulser.submit(
            object : Pulse(1) {
                var pulseCount = 0
                val fail = content.global.skill.agility.AgilityHandler.hasFailed(player, 48, 0.5)

                override fun pulse(): Boolean {
                    when (pulseCount++) {
                        0 -> animate(player, 3481)
                        1 -> teleport(player, Location.create(2754, 2742, 0))
                        2 -> {
                            if (fail) {
                                animate(player, 3489)
                                sendMessage(player, "...And accidentally fall to the water.")
                                player.impactHandler.manualHit(player, 2, ImpactHandler.HitsplatType.NORMAL)
                                forceMove(player, waterTile, waterTile, 25, 60, player.location.direction, 3489)
                                teleport(player, Location.create(2754, 2743, 0))
                                resetAnimator(player)
                            } else {
                                animate(player, 3481)
                                teleport(player, toTile)
                                rewardXP(player, Skills.AGILITY, 15.0)
                                sendMessage(player, "..And made it carefully to the other side.")
                            }
                            return true
                        }
                    }
                    return false
                }
            }
        )
        return true
    }

    private fun climbUpTropicalTree(player: Player, scenery: Scenery): Boolean {
        if (!checkRequirements(player) || !player.location.withinDistance(scenery.location, 2)) return true
        player.walkingQueue.reset()

        player.lock(3)
        GameWorld.Pulser.submit(
            object : Pulse(0, player) {
                var pulseCounter = 0

                override fun pulse(): Boolean {
                    when (pulseCounter++) {
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
            }
        )
        return true
    }

    private fun climbUpSkullSlope(player: Player, scenery: Scenery): Boolean {
        if (!checkRequirements(player) || !player.location.withinDistance(scenery.location, 2)) return true
        if (player.location.x < 2747) return true
        player.walkingQueue.reset()

        lock(player, 3)
        val toTile = Location.create(2743, 2741, 0)
        player.walkingQueue.addPath(toTile.x, toTile.y, false)
        animate(player, 3485)

        GameWorld.Pulser.submit(
            object : Pulse(0, player) {
                var pulseCounter = 0

                override fun pulse(): Boolean {
                    if (pulseCounter++ == 2) {
                        sendMessage(player, "You climb up the skull slope.")
                        forceMove(
                            player,
                            player.location.transform(toTile),
                            toTile,
                            25,
                            60,
                            player.location.direction,
                            3489
                        )
                        rewardXP(player, Skills.AGILITY, 45.0)
                        return true
                    }
                    return false
                }
            }
        )
        return true
    }

    private fun crossMonkeyBars(player: Player, scenery: Scenery): Boolean {
        if (!checkRequirements(player) || !player.location.withinDistance(scenery.location, 2)) return true
        player.walkingQueue.reset()

        player.lock(4)
        val toTile = Location.create(2747, 2741, 0)
        val toTile2 = Location.create(2747, 2741, 2)
        animate(player, Animation(3482))
        player.walkingQueue.addPath(toTile2.x, toTile2.y, false)
        sendMessage(player, "You jump to the monkey bars...")

        GameWorld.Pulser.submit(
            object : Pulse(0, player) {
                var pulseCounter = 0

                override fun pulse(): Boolean {
                    if (pulseCounter++ == 3) {
                        sendMessage(player, "..And made it carefully to the other side.")
                        teleport(player, toTile)
                        rewardXP(player, Skills.AGILITY, 35.0)
                        return true
                    }
                    return false
                }
            }
        )
        return true
    }

    private fun swingRope(player: Player, scenery: Scenery): Boolean {
        if (!checkRequirements(player) || !player.location.withinDistance(scenery.location, 2)) return true
        if (player.location.x == 2756) return true
        player.walkingQueue.reset()

        player.lock(4)
        val toTile = Location.create(2756, 2731, scenery.location.z)
        animate(player, 1388)
        animateScenery(player, scenery, 2231, true)
        player.walkingQueue.addPath(toTile.x, toTile.y, false)
        sendMessage(player, "You skillfully swing across.")

        GameWorld.Pulser.submit(
            object : Pulse(0, player) {
                var pulseCounter = 0

                override fun pulse(): Boolean {
                    if (pulseCounter++ == 2) {
                        teleport(player, toTile)
                        rewardXP(player, Skills.AGILITY, 22.0)
                        return true
                    }
                    return false
                }
            }
        )
        return true
    }

    private fun climbDownTropicalTree(player: Player, scenery: Scenery): Boolean {
        if (!checkRequirements(player) || !player.location.withinDistance(scenery.location, 2)) return true
        player.walkingQueue.reset()

        player.lock()
        val toTile = Location.create(2770, 2747, 0)
        val toTile2 = Location.create(2770, 2747, 1)
        player.walkingQueue.addPath(toTile.x, toTile.y, false)
        player.walkingQueue.addPoint(2758, 2735, false)
        animate(player, 3494)

        GameWorld.Pulser.submit(
            object : Pulse(0, player) {
                var pulseCounter = 0

                override fun pulse(): Boolean {
                    when (pulseCounter++) {
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
            }
        )
        return true
    }
}
