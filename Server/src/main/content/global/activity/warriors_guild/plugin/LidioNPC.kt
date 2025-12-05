package content.global.activity.warriors_guild.plugin

import core.api.sendChat
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.NPCs

@Initializable
class LidioNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    private val dialogues = arrayOf(
        "Potatoes are filling and healthy too!",
        "Come try my lovely pizza or maybe some fish!",
        "Stew to fill the belly, on sale here!"
    )

    private var dialogueIndex = 0
    private val TICK_INTERVAL = 30
    private var tickCounter = RandomFunction.random(TICK_INTERVAL)
    private val PAUSE_AFTER_CYCLE = 204

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        LidioNPC(id, location)

    override fun handleTickActions() {
        super.handleTickActions()

        tickCounter++
        if (tickCounter < TICK_INTERVAL) return

        tickCounter = 0

        sendChat(this, dialogues[dialogueIndex])
        dialogueIndex++

        if (dialogueIndex >= dialogues.size) {
            dialogueIndex = 0
            tickCounter = -PAUSE_AFTER_CYCLE
        }
    }

    override fun getIds(): IntArray = NPC_IDS

    companion object {
        private val NPC_IDS = intArrayOf(NPCs.LIDIO_4293)
    }
}
