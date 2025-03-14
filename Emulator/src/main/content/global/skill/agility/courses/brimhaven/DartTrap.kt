package content.global.skill.agility.courses.brimhaven

import content.global.skill.agility.AgilityHandler
import core.game.node.entity.Entity
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.MovementHook
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.out.CameraViewPacket
import core.tools.RandomFunction

class DartTrap : MovementHook {
    override fun handle(
        e: Entity,
        l: Location,
    ): Boolean {
        val dir = e.direction
        val player = e as Player
        val start = l.transform(-dir.stepX, -dir.stepY, 0)
        e.lock(6)
        if (e.isPlayer()) {
            e.asPlayer().logoutListeners["dart-trap"] = { p: Player ->
                p.location = start
            }
        }
        val startProj = l.transform(dir.stepX * 5, dir.stepY * 5, 0)
        Pulser.submit(
            object : Pulse(2, e) {
                var failed: Boolean = false
                var count: Int = 0

                override fun pulse(): Boolean {
                    if (++count == 1) {
                        if (AgilityHandler.hasFailed(player, 40, 0.15).also { failed = it }) {
                            if (player.getSkills().getLevel(Skills.AGILITY) < 40) {
                                player.packetDispatch.sendMessage(
                                    "You need an agility of at least 40 to get past this trap!",
                                )
                            }
                            Projectile.create(startProj, l, 270, 0, 10, 46, 85, 5, 11).send()
                            delay = 3
                        } else {
                            PacketRepository.send(
                                CameraViewPacket::class.java,
                                CameraContext(
                                    player,
                                    CameraContext.CameraType.POSITION,
                                    startProj.x + (dir.stepX * 4),
                                    startProj.y + (dir.stepY * 4),
                                    350,
                                    1,
                                    100,
                                ),
                            )
                            PacketRepository.send(
                                CameraViewPacket::class.java,
                                CameraContext(player, CameraContext.CameraType.ROTATION, l.x, l.y, 350, 1, 100),
                            )
                            Projectile
                                .create(
                                    startProj,
                                    l.transform(-dir.stepX * 4, -dir.stepY * 4, 0),
                                    270,
                                    0,
                                    0,
                                    46,
                                    200,
                                    5,
                                    11,
                                ).send()
                        }
                    } else if (count == 2) {
                        if (failed) {
                            var hit = player.getSkills().lifepoints / 12
                            if (hit < 2) {
                                hit = 2
                            }
                            delay = 1
                            AgilityHandler
                                .failWalk(
                                    player,
                                    1,
                                    l,
                                    start,
                                    start,
                                    Animation.create(1114),
                                    10,
                                    hit,
                                    null,
                                ).direction = dir
                        } else {
                            if (dir.toInteger() % 2 != 0) {
                                val mod = if (dir == Direction.WEST) -1 else 1
                                PacketRepository.send(
                                    CameraViewPacket::class.java,
                                    CameraContext(
                                        player,
                                        CameraContext.CameraType.POSITION,
                                        l.x - (5 * mod),
                                        l.y - (5 * mod),
                                        400,
                                        8,
                                        6,
                                    ),
                                )
                                PacketRepository.send(
                                    CameraViewPacket::class.java,
                                    CameraContext(
                                        player,
                                        CameraContext.CameraType.ROTATION,
                                        l.x + (2 * mod),
                                        l.y,
                                        350,
                                        8,
                                        1,
                                    ),
                                )
                            } else {
                                val mod = if (dir == Direction.SOUTH) -1 else 1
                                PacketRepository.send(
                                    CameraViewPacket::class.java,
                                    CameraContext(
                                        player,
                                        CameraContext.CameraType.POSITION,
                                        l.x + (5 * mod),
                                        l.y - (5 * mod),
                                        400,
                                        8,
                                        6,
                                    ),
                                )
                                PacketRepository.send(
                                    CameraViewPacket::class.java,
                                    CameraContext(
                                        player,
                                        CameraContext.CameraType.ROTATION,
                                        l.x,
                                        l.y + (2 * mod),
                                        350,
                                        8,
                                        1,
                                    ),
                                )
                            }
                            player.lock(7)
                            player.animate(Animation(1110, 20))
                            delay = 5
                        }
                    } else if (count == 3) {
                        if (failed) {
                            player.packetDispatch.sendMessage(
                                "You were hit by some darts, something on them makes you feel dizzy!",
                            )
                            player.getSkills().updateLevel(Skills.AGILITY, -(2 + RandomFunction.randomize(2)), 0)
                            player.logoutListeners.remove("dart-trap")
                            return true
                        }
                        delay = 2
                        AgilityHandler.walk(
                            player,
                            -1,
                            l,
                            l.transform(dir.stepX shl 1, dir.stepY shl 1, 0),
                            null,
                            30.0,
                            null,
                        )
                    } else if (count == 4) {
                        PacketRepository.send(
                            CameraViewPacket::class.java,
                            CameraContext(player, CameraContext.CameraType.RESET, 0, 0, 0, 0, 0),
                        )
                        player.logoutListeners.remove("dart-trap")
                        return true
                    }
                    return false
                }
            },
        )
        return false
    }
}
