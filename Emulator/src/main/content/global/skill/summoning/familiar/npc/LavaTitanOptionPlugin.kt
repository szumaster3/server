package content.global.skill.summoning.familiar.npc

import core.cache.def.impl.NPCDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.NPCs

/**
 * The type Lava titan option plugin.
 */
@Initializable
class LavaTitanOptionPlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        NPCDefinition.forId(NPCs.LAVA_TITAN_7341).handlers["option:interact"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        player.dialogueInterpreter.open(8700, node.asNpc())
        return true
    }
}
