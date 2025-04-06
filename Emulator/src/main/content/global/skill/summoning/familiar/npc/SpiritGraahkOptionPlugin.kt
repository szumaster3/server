package content.global.skill.summoning.familiar.npc

import core.cache.def.impl.NPCDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.NPCs

/**
 * The type Spirit graahk option plugin.
 */
@Initializable
class SpiritGraahkOptionPlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        NPCDefinition.forId(NPCs.SPIRIT_GRAAHK_7364).handlers["option:interact"] = this
        NPCDefinition.forId(NPCs.SPIRIT_GRAAHK_7363).handlers["option:interact"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        player.dialogueInterpreter.open(NPCs.SPIRIT_GRAAHK_7363, node.asNpc())
        return true
    }
}
