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
class ObjectUpdateFlag(private val scenery: Scenery?, private val remove: Boolean) : UpdateFlag<Scenery?>(scenery) {

    override fun write(buffer: IoBuffer) {
        if (remove) {
            ClearScenery.write(buffer, scenery!!)
        } else {
            ConstructScenery.write(buffer, scenery!!)
        }
    }

    override fun data(): Int {
        return 0
    }

    override fun ordinal(): Int {
        return 0
    }
}