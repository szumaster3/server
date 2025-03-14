package content.region.misthalin.quest.romeo.handlers

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class RomeoNPC : AbstractNPC {
    constructor() : super(0, null, true)

    private constructor(id: Int, location: Location) : super(id, location, true)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return RomeoNPC(id, location)
    }

    override fun init() {
        isWalks = true
        super.init()
        isWalks = true
    }

    override fun tick() {
        super.tick()
    }

    override fun getIds(): IntArray {
        return ID
    }

    companion object {
        private val ID = intArrayOf(NPCs.ROMEO_639)
        private const val speakDelay = 0
    }
}
