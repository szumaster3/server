package core.game.node.entity.player.link

import core.api.removeAttribute
import core.api.setAttribute
import core.game.node.entity.Entity
import core.game.system.task.Pulse

/**
 * Abstract class representing a teleportation pulse that operates on an entity.
 *
 * @property entity The entity that this pulse operates on.
 */
internal abstract class TeleportPulse(
    private val entity: Entity,
) : Pulse() {

    /**
     * Abstract method that defines the logic of the pulse.
     *
     * @return A boolean indicating whether the pulse has completed or should continue.
     */
    abstract override fun pulse(): Boolean

    /**
     * Starts the teleport pulse by setting the entity attribute.
     */
    override fun start() {
        // Set the teleport pulse attribute to indicate that this pulse is active
        setAttribute(entity, "tele-pulse", this)
        super.start()
    }

    /**
     * Stops the teleport pulse by removing the entity attribute.
     */
    override fun stop() {
        removeAttribute(entity, "tele-pulse")
        super.stop()
    }
}
