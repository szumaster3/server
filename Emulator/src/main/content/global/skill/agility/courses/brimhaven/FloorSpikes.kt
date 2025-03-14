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

class FloorSpikes : MovementHook {
    override fun handle(
        e: Entity,
        l: Location,
    ): Boolean {
        val dir = e.direction
        val player = e as Player
        val start = l.transform(-dir.stepX, -dir.stepY, 0)
        e.lock(5)
        if (e.isPlayer()) {
            e.logoutListeners["floor-spikes"] = { p: Player ->
                p.location = start
                Unit
            }
        }
        Pulser.submit(
            object : Pulse(3, e) {
                override fun pulse(): Boolean {
                    player.packetDispatch.sendSceneryAnimation(getObject(l), Animation.create(1111))
                    if (AgilityHandler.hasFailed(player, 20, 0.25)) {
                        if (player.getSkills().getLevel(Skills.AGILITY) < 20) {
                            player.packetDispatch.sendMessage(
                                "You need an agility of at least 20 to get past this trap!",
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
                                "You were hit by some floor spikes!",
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
                    player.logoutListeners.remove("floor-spikes")
                    return true
                }
            },
        )
        return false
    }
}
