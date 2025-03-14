package content.global.handlers.player

import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.interaction.Option
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class FollowOption : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        Option._P_FOLLOW.setHandler(this)
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val target = node as Player
        player.pulseManager.run(
            object : MovementPulse(player, target, DestinationFlag.FOLLOW_ENTITY) {
                override fun pulse(): Boolean {
                    player.face(target)
                    return false
                }

                override fun stop() {
                    super.stop()
                    mover.face(null)
                }
            },
            PulseType.STANDARD,
        )
        return true
    }
}
