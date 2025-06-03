package core.game.world.update.flag.chunk

import core.game.world.update.flag.UpdateFlag
import core.game.world.update.flag.context.Animation
import core.net.packet.IoBuffer
import core.net.packet.out.AnimateObjectPacket

/**
 * The animate object update flag.
 * @author Emperor
 */
class AnimateObjectUpdateFlag(context: Animation?) : UpdateFlag<Animation?>(context) {

    override fun write(buffer: IoBuffer) {
        AnimateObjectPacket.write(buffer, context!!)
    }

    override fun data(): Int {
        return 0
    }

    override fun ordinal(): Int {
        return 0
    }
}