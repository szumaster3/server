package content.minigame.pyramidplunder.handlers

import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class RechargeSceptre : UseWithHandler(9046, 9048, 9050) {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(4476, NPC_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        player.dialogueInterpreter.open(999876, event.usedItem)
        return true
    }
}
