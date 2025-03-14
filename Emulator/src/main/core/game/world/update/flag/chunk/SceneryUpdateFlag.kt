package core.game.world.update.flag.chunk

import core.game.node.scenery.Scenery
import core.game.world.update.flag.UpdateFlag
import core.net.packet.IoBuffer
import core.net.packet.out.ClearScenery
import core.net.packet.out.ConstructScenery

class SceneryUpdateFlag(
    private val scenery: Scenery,
    private val remove: Boolean,
) : UpdateFlag<Any?>(null) {
    override fun write(buffer: IoBuffer) {
        if (remove) {
            ClearScenery.write(buffer, scenery)
        } else {
            ConstructScenery.write(buffer, scenery)
        }
    }

    override fun data(): Int = 0

    override fun ordinal(): Int = 0
}
