package core.game.world.update.flag.chunk

import core.game.node.scenery.Scenery
import core.game.world.update.flag.UpdateFlag
import core.net.packet.IoBuffer
import core.net.packet.out.ClearScenery
import core.net.packet.out.ConstructScenery

/**
 * The object update flag.
 * @author Emperor
 */
class ObjectUpdateFlag(
    /**
     * The object to update.
     */
    private val `object`: Scenery,
    /**
     * If we should remove the object.
     */
    private val remove: Boolean
) : UpdateFlag<Any?>(null) {
    override fun write(buffer: IoBuffer) {
        if (remove) {
            ClearScenery.write(buffer, `object`)
        } else {
            ConstructScenery.write(buffer, `object`)
        }
    }

    override fun data(): Int {
        return 0
    }

    override fun ordinal(): Int {
        return 0
    }
}