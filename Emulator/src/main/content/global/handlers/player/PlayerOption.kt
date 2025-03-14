package content.global.handlers.player

import core.game.bots.AIPlayer
import core.game.bots.AIRepository
import core.game.interaction.DestinationFlag
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.MovementPulse
import core.game.interaction.Option.*
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.RequestType

class PlayerOption : InteractionListener {
    override fun defineListeners() {
        on(IntType.PLAYER, _P_ATTACK.name) { player, node ->
            player.attack(node)
            return@on true
        }

        on(IntType.PLAYER, _P_TRADE.name) { player, node ->
            player.requestManager.request((node as Player), RequestType.TRADE)
            return@on true
        }

        on(IntType.PLAYER, _P_ASSIST.name) { player, node ->
            if (node is AIPlayer) AIRepository.sendBotInfo(player, node)
            player.requestManager.request((node as Player), RequestType.ASSIST)
            return@on true
        }

        on(IntType.PLAYER, _P_FOLLOW.name) { player, node ->
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
            return@on true
        }
    }
}
