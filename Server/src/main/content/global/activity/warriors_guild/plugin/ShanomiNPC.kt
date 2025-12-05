package content.global.activity.warriors_guild.plugin

import core.api.sendChat
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.NPCs

@Initializable
class ShanomiNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    // 8.8 seconds between each line and a 1-minute pause after the last one.
    private val dialogues = arrayOf(
        "Think not dishonestly.",
        "The Way in training is.",
        "Acquainted with every art become.",
        "Ways of all professions know you.",
        "Gain and loss between you must distinguish.",
        "Judgment and understanding for everything develop you must.",
        "Those things which cannot be seen, perceive them.",
        "Trifles pay attention even to.",
        "Do nothing which is of no use.",
        "Way of the Warrior this is.",
    )


    private var dialogueIndex = 0
    private val TICK_INTERVAL = 30
    private var tickCounter = RandomFunction.random(TICK_INTERVAL)
    private val PAUSE_AFTER_CYCLE = 204

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        ShanomiNPC(id, location)

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
        private val NPC_IDS = intArrayOf(NPCs.SHANOMI_4290)
    }
}
