package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.api.getStatLevel
import core.api.impact
import core.api.sendChat
import core.api.sendMessage
import core.api.utils.Vector
import core.game.node.entity.combat.ImpactHandler
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
import org.rs.consts.Animations

@Initializable
class RockClimbShortcut :
    AgilityShortcut(intArrayOf(9335, 9336, 2231, 26327, 26328, 26324, 26323, 19849, 9296, 9297), 1, 0.0, "climb") {
    override fun run(
        player: Player,
        scenery: Scenery,
        option: String,
        failed: Boolean,
    ) {
        val scalingAnim = Animation(Animations.CLIMB_DOWN_B_740)
        val req = determineRequirement(player.location)

        when (scenery.id) {
            2231 -> handleClimbShortcut(player, scenery, scalingAnim)
            19849 -> handleEaglesPeakShortcut(player, scalingAnim)
            9335 ->
                ForceMovement
                    .run(
                        player,
                        player.location,
                        Location.create(3427, 3478, 0),
                        scalingAnim,
                        scalingAnim,
                        Direction.WEST,
                        13,
                    ).endAnimation = Animation.RESET

            9336 ->
                ForceMovement
                    .run(
                        player,
                        player.location,
                        Location.create(3424, 3476, 0),
                        scalingAnim,
                        scalingAnim,
                        Direction.WEST,
                        13,
                    ).endAnimation = Animation.RESET

            in arrayOf(26327, 26328, 26324, 26323) -> handleGodWarsShortcut(player, scenery, scalingAnim)
            in arrayOf(9297, 9296) -> handleArandarShortcut(player, scenery, scalingAnim, req)
        }
    }

    private fun determineRequirement(location: Location): Int {
        return when (location) {
            Location(2346, 3300, 0), Location(2344, 3294, 0) -> 59
            Location(2338, 3281, 0), Location(2338, 3286, 0) -> 85
            Location(2332, 3252, 0), Location(2338, 3253, 0) -> 68
            else -> 0
        }
    }

    private fun handleClimbShortcut(
        player: Player,
        scenery: Scenery,
        scalingAnim: Animation,
    ) {
        val hitpoints = getStatLevel(player, Skills.HITPOINTS)
        val damage = hitpoints / 10.0
        val direction = if (player.location.x == 2795) Direction.WEST else Direction.EAST
        val fail = AgilityHandler.hasFailed(player, 1, 0.053)
        val targetLocation = scenery.location.transform(if (direction == Direction.WEST) -3 else 3, 0, 0)
        if (getStatLevel(player, Skills.AGILITY) >= 15) {
            Pulser.submit(
                object : Pulse(0, player) {
                    override fun pulse(): Boolean {
                        if (fail) {
                            sendChat(player, "Ouch")
                            sendMessage(player, "You fall and hurt yourself.")
                            impact(player, damage.toInt(), ImpactHandler.HitsplatType.NORMAL)
                            impact(player, damage.toInt(), ImpactHandler.HitsplatType.NORMAL)
                            ForceMovement
                                .run(
                                    player,
                                    scenery.location,
                                    scenery.location.transform(1, 0, 0),
                                    scalingAnim,
                                    scalingAnim,
                                    Direction.WEST,
                                    13,
                                ).endAnimation = Animation.RESET
                        } else {
                            ForceMovement
                                .run(
                                    player,
                                    scenery.location,
                                    targetLocation,
                                    scalingAnim,
                                    scalingAnim,
                                    Direction.WEST,
                                    13,
                                ).endAnimation = Animation.RESET
                        }
                        return true
                    }
                },
            )
        } else {
            sendMessage(player, "You need an agility level of at least 15 to do this.")
        }
    }

    private fun handleEaglesPeakShortcut(
        player: Player,
        scalingAnim: Animation,
    ) {
        if (getStatLevel(player, Skills.AGILITY) >= 25) {
            val targetLocation =
                if (player.location.x <= 2322) Location.create(2324, 3497, 0) else Location.create(2322, 3502, 0)
            ForceMovement
                .run(
                    player,
                    player.location,
                    targetLocation,
                    scalingAnim,
                    scalingAnim,
                    Direction.SOUTH,
                    13,
                ).endAnimation = Animation.RESET
        } else {
            sendMessage(player, "You need an agility level of at least 25 to do this.")
        }
    }

    private fun handleGodWarsShortcut(
        player: Player,
        scenery: Scenery,
        scalingAnim: Animation,
    ) {
        val dir = Vector.betweenLocs(player.location, scenery.location).toDirection()
        if (getStatLevel(player, Skills.AGILITY) >= 60) {
            val targetLocation =
                when (scenery.id) {
                    26327 -> Location.create(2942, 3768, 0)
                    26328 -> Location.create(2950, 3767, 0)
                    26324 -> Location.create(2928, 3757, 0)
                    26323 -> Location.create(2927, 3761, 0)
                    else -> return
                }
            ForceMovement.run(player, player.location, targetLocation, scalingAnim, scalingAnim, dir, 13).endAnimation =
                Animation.RESET
        } else {
            sendMessage(player, "You need an agility level of at least 60 to do this.")
        }
    }

    private fun handleArandarShortcut(
        player: Player,
        scenery: Scenery,
        scalingAnim: Animation,
        req: Int,
    ) {
        val dir = Vector.betweenLocs(player.location, scenery.location).toDirection()
        if (getStatLevel(player, Skills.AGILITY) >= req) {
            val targetLocation =
                when (scenery.id) {
                    9297 -> Location.create(2346, 3300, 0)
                    9296 -> Location.create(2344, 3294, 0)
                    else -> return
                }
            ForceMovement.run(player, player.location, targetLocation, scalingAnim, scalingAnim, dir, 13).endAnimation =
                Animation.RESET
        } else {
            sendMessage(player, "You need an agility level of at least $req to do this.")
        }
    }
}
