package core.game.world.update.flag.chunk

import core.game.world.map.Location
import core.game.world.update.flag.UpdateFlag
import core.game.world.update.flag.context.Graphics
import core.net.packet.IoBuffer

class GraphicUpdateFlag(
    context: Graphics,
    private val location: Location,
) : UpdateFlag<Graphics>(context) {
    override fun write(buffer: IoBuffer) {
        buffer.put(17)
        buffer.put((location.chunkOffsetX shl 4) or (location.chunkOffsetY and 0x7))
        buffer.putShort(context.id)
        buffer.put(context.height)
        buffer.putShort(context.delay)
    }

    override fun data(): Int = 0

    override fun ordinal(): Int = 3
}
