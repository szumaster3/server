package content.region.kandarin.handlers.npc

import core.api.interaction.openNpcShop
import core.cache.def.impl.NPCDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.NPCs

@Initializable
class RasoloNPC : OptionHandler() {

    companion object {
        private const val NPC_ID = NPCs.RASOLO_1972
    }

    override fun newInstance(arg: Any?): Plugin<Any>? {
        NPCDefinition.forId(NPC_ID).handlers["option:talk-to"] = this
        NPCDefinition.forId(NPC_ID).handlers["option:trade"] = this
        return null
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        return when (option) {
            "trade" -> {
                openNpcShop(player, NPC_ID)
                true
            }
            "talk-to" -> {
                player.dialogueInterpreter.open(NPC_ID)
                true
            }
            else -> false
        }
    }
}
