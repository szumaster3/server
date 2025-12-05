package content.global.activity.warriors_guild.plugin

import core.api.sendChat
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.NPCs

@Initializable
class LillyNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    private val dialogues = arrayOf(
        "It's not an optical illusion, it just looks like one.",
        "Madness takes its toll. Please have exact change.",
        "If you don't care where you are, then you ain't lost.",
        "Don't look back, they might be gaining on you.",
        "Demons are a Ghouls best friend.",
        "Pink bananas... mmmmm.",
    )

    private var dialogueIndex = 0
    private val TICK_INTERVAL = 30
    private var tickCounter = RandomFunction.random(TICK_INTERVAL)
    private val PAUSE_AFTER_CYCLE = 204

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        LillyNPC(id, location)

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
        private val NPC_IDS = intArrayOf(NPCs.LILLY_4294)
    }
}
