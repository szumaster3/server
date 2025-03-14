package core.game.world.update.flag.chunk

import core.game.world.update.flag.UpdateFlag
import core.game.world.update.flag.context.Animation
import core.net.packet.IoBuffer
import core.net.packet.out.AnimateObjectPacket

class AnimateSceneryUpdateFlag(
    context: Animation,
) : UpdateFlag<Animation>(context) {
    override fun write(buffer: IoBuffer) {
        AnimateObjectPacket.write(buffer, context)
    }

    override fun data(): Int = 0

    override fun ordinal(): Int = 0
}
