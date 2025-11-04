package content.region.karamja.quest.roots.npc

import core.api.sendGraphics
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.NPCs

class HoracioNPC(
    id: Int = NPCs.HORACIO_845,
    location: Location? = null
) : AbstractNPC(id, location) {
    var target: Player? = null

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = HoracioNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.HORACIO_845)

    private val forceChat = arrayOf(
        "Where's my weedkiller?",
        "Ow! That's stings!",
        "Get it off me!",
    )

    private var tickDelay = RandomFunction.random(5)
    private val TICK_INTERVAL = 3

    override fun handleTickActions() {
        super.handleTickActions()

        tickDelay++
        if (tickDelay < TICK_INTERVAL) return
        tickDelay = 0

        if (RandomFunction.roll(2)) {
            sendGraphics(1171, this.location)
            this.asNpc().animate(Animation(3635))
            core.api.sendChat(this, forceChat.random())
        }
    }

    init {
        isWalks = false
        isRespawn = false
        isNeverWalks = true
    }
}