package content.minigame.pyramidplunder.handlers

import core.api.openDialogue
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.npc.NPC
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.NPCs

@Initializable
class SimonScepterHandler : UseWithHandler(9044, 9046, 9048, 9050) {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(NPCs.SIMON_TEMPLETON_3123, NPC_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        openDialogue(player, NPCs.SIMON_TEMPLETON_3123, event.usedWith as NPC, true, false, event.usedItem.id)
        return true
    }
}
