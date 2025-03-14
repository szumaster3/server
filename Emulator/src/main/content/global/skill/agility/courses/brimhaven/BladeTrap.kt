package content.global.skill.agility.courses.brimhaven

import content.global.skill.agility.AgilityHandler
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.task.MovementHook
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.update.flag.context.Animation

class BladeTrap : MovementHook {
    override fun handle(
        e: Entity,
        l: Location,
    ): Boolean {
        if (BrimhavenArena.sawBladeActive) {
            val dir = e.direction
            val player = e as Player
            val start = l.transform(-dir.stepX, -dir.stepY, 0)
            e.lock(5)
            if (e.isPlayer()) {
                e.asPlayer().logoutListeners["blade-trap"] = { p: Player ->
                    p.location = start
                    Unit
                }
            }
            Pulser.submit(
                object : Pulse(2, e) {
                    override fun pulse(): Boolean {
                        var direction = dir
                        var d = Direction.get(direction.toInteger() + 2 and 3)
                        if (getObject(player.location.transform(dir)) != null) {
                            val s = d
                            d = direction
                            direction = s
                        }
                        val loc = player.location
                        AgilityHandler
                            .failWalk(
                                player,
                                1,
                                loc,
                                loc.transform(direction),
                                loc.transform(direction),
                                Animation.create(846),
                                10,
                                3,
                                "You were hit by the saw blade!",
                            ).direction = d
                        player.logoutListeners.remove("blade-trap")
                        return true
                    }
                },
            )
            return false
        }
        return true
    }
}
