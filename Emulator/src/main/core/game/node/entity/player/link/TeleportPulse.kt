package core.game.node.entity.player.link

import core.api.removeAttribute
import core.api.setAttribute
import core.game.node.entity.Entity
import core.game.system.task.Pulse

internal abstract class TeleportPulse(
    private val entity: Entity,
) : Pulse() {
    abstract override fun pulse(): Boolean

    override fun start() {
        setAttribute(entity, "tele-pulse", this)
        super.start()
    }

    override fun stop() {
        removeAttribute(entity, "tele-pulse")
        super.stop()
    }
}
