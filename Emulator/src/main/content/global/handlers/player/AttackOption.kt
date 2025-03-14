package content.global.handlers.player

import core.cache.def.impl.NPCDefinition
import core.game.interaction.Option
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class AttackOption : OptionHandler() {
    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        player.attack(node)
        return true
    }

    override fun isWalk(): Boolean {
        return false
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        Option._P_ATTACK.setHandler(this)
        NPCDefinition.setOptionHandler("attack", this)
        return this
    }

    override fun isDelayed(player: Player): Boolean {
        return false
    }
}
