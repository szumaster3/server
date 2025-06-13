package content.global.handlers.npc

import core.api.animate
import core.api.sendChat
import core.api.stopWalk
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.NPCs

@Initializable
class TownCrierNPC(
    id: Int = 0,
    location: Location? = null
) : AbstractNPC(id, location) {

    private val forceChatAnimation = listOf(
        "The Duke of Lumbridge needs a hand." to Animations.TOWN_CRIER_RING_BELL_6865,
        "The squirrels! The squirrels are coming! Noooo, get them out of my head!" to Animations.TOWN_CRIER_SCRATCHES_HEAD_6863
    )

    private var chatDelay = randomDelay()

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        TownCrierNPC(id, location)

    override fun getIds(): IntArray = ID

    override fun handleTickActions() {
        if (--chatDelay > 0) return

        if (RandomFunction.random(300) == 5) {
            val (chat, animation) = forceChatAnimation.random()
            stopWalk(this)
            animate(this, animation)
            sendChat(this, chat)
            chatDelay = randomDelay()
        } else {
            chatDelay = randomDelay()
        }
    }

    private fun randomDelay() = RandomFunction.random(50, 150)

    companion object {
        private val ID = intArrayOf(
            NPCs.TOWN_CRIER_6135,
            NPCs.TOWN_CRIER_6136,
            NPCs.TOWN_CRIER_6137,
            NPCs.TOWN_CRIER_6138,
            NPCs.TOWN_CRIER_6139,
        )
    }
}
