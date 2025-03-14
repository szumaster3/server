package content.region.morytania.handlers.tarnslair.traps

import core.api.queueScript
import core.api.sendMessage
import core.api.stopExecuting
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class PassageHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        Passage.values().forEach { passage ->
            SceneryDefinition.forId(passage.passage.objectId).handlers["option:climb"] = this
            SceneryDefinition.forId(passage.passage.objectId).handlers["option:enter"] = this
        }
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val scenery = node as? Scenery ?: return false
        val passage = Passage.getAllPassages()[scenery.location]
        if (passage != null) {
            player.lock(2)
            queueScript(player, 1, QueueStrength.NORMAL) {
                player.teleport(passage.destination)
                return@queueScript stopExecuting(player)
            }
            return true
        } else {
            sendMessage(player, "You can't use this passage.")
            return false
        }
    }
}
