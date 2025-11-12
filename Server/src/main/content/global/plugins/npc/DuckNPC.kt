package content.global.plugins.npc

import core.api.sendChat
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.NPCs

@Initializable
class DuckNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    private val forceChat = arrayOf("Eep!", "Quack!")
    private var tickDelay = RandomFunction.random(30)
    private val TICK_INTERVAL = 30

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = DuckNPC(id, location)

    override fun handleTickActions() {
        super.handleTickActions()

        tickDelay++
        if (tickDelay < TICK_INTERVAL) return
        tickDelay = 0

        if (RandomFunction.roll(2)) {
            sendChat(this, forceChat.random())
        }
    }

    override fun getIds(): IntArray = ID

    companion object {
        private val ID = intArrayOf(NPCs.DUCK_46, NPCs.DUCK_2693, NPCs.DUCK_6113)
    }
}
