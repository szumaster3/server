package content.global.skill.agility.courses.brimhaven

import content.global.skill.agility.AgilityHandler
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.MovementHook
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.update.flag.context.Animation

class SpinningBlades : MovementHook {
    override fun handle(
        e: Entity,
        l: Location,
    ): Boolean {
        val dir = e.direction
        val player = e as Player
        val start = l.transform(-dir.stepX, -dir.stepY, 0)
        e.lock(5)
        if (e.isPlayer()) {
            e.logoutListeners["spin-blades"] = { p: Player ->
                p.location = start
                Unit
            }
        }
        Pulser.submit(
            object : Pulse(3, e) {
                override fun pulse(): Boolean {
                    sendObjectAnimation(player, l)
                    if (AgilityHandler.hasFailed(player, 40, 0.15)) {
                        if (player.getSkills().getLevel(Skills.AGILITY) < 40) {
                            player.packetDispatch.sendMessage(
                                "You need an agility of at least 40 to get past this trap!",
                            )
                        }
                        var hit = player.getSkills().lifepoints / 12
                        if (hit < 2) {
                            hit = 2
                        }
                        AgilityHandler
                            .failWalk(
                                player,
                                1,
                                player.location,
                                start,
                                start,
                                Animation.create(1114),
                                10,
                                hit,
                                "You were hit by the spinning blades.",
                            ).direction = dir
                    } else {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            l,
                            l.transform(dir.stepX shl 1, dir.stepY shl 1, 0),
                            Animation.create(1115),
                            20,
                            26.0,
                            null,
                        )
                    }
                    player.logoutListeners.remove("spin-blades")
                    return true
                }
            },
        )
        return false
    }

    companion object {
        private fun sendObjectAnimation(
            player: Player,
            l: Location,
        ) {
            var l = l
            if (l == Location.create(2778, 9579, 3)) {
                l = Location.create(2777, 9580, 3)
            } else if (l == Location.create(2783, 9574, 3)) {
                l = Location.create(2782, 9573, 3)
            } else if (l == Location.create(2778, 9557, 3)) {
                l = Location.create(2777, 9556, 3)
            }
            player.packetDispatch.sendSceneryAnimation(getObject(l), Animation.create(1107))
        }
    }
}
