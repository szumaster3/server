package content.region.desert.quest.desertrescue

import core.game.node.entity.npc.AbstractNPC
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.NPCs

class MineSlaveNPC : AbstractNPC {
    private var delay = 0

    constructor() : super(0, null)

    constructor(id: Int, location: Location?) : super(id, location) {
        delay = ticks + RandomFunction.random(20, 100)
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return MineSlaveNPC(id, location)
    }

    override fun tick() {
        if (delay < ticks) {
            sendChat(CHATS[RandomFunction.random(CHATS.size)])
            delay = ticks + RandomFunction.random(20, 100)
        }
        super.tick()
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MALE_SLAVE_4975, NPCs.MALE_SLAVE_4976, NPCs.FEMALE_SLAVE_4977, NPCs.FEMALE_SLAVE_4978)
    }

    companion object {
        private val CHATS =
            arrayOf(
                "I'm sick of this place.",
                "What I wouldn't give for a good nights rest.",
                "I feel so weak I could faint.",
                "I didn't want to be a miner anyway.",
                "Ooh my back.",
                "I'm rich in experience, poor in wealth.",
                "I can' think straight, i'm so tired.",
            )
    }
}
