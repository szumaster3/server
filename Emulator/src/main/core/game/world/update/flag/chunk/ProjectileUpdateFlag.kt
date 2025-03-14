package core.game.world.update.flag.chunk

import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.world.update.flag.UpdateFlag
import core.net.packet.IoBuffer

class ProjectileUpdateFlag(
    context: Projectile,
) : UpdateFlag<Projectile>(context) {
    override fun write(buffer: IoBuffer) {
        val projectile = context
        val start = projectile.sourceLocation
        val target = projectile.victim
        val end = if (projectile.isLocationBased) projectile.endLocation else target?.location

        buffer
            .put(16)
            .put((start.chunkOffsetX shl 4) or (start.chunkOffsetY and 0x7))
            .put(end!!.x - start.x)
            .put(end.y - start.y)
            .putShort(
                when (target) {
                    null -> -1
                    is Player -> -(target.index + 1)
                    else -> (target.index + 1)
                },
            ).putShort(projectile.projectileId)
            .put(projectile.startHeight)
            .put(projectile.endHeight)
            .putShort(projectile.startDelay)
            .putShort(projectile.speed)
            .put(projectile.angle)
            .put(projectile.distance)
    }

    override fun data(): Int = 0

    override fun ordinal(): Int = 2
}
