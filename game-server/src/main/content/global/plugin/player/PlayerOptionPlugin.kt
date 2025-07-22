package content.global.plugin.player

import core.game.bots.AIPlayer
import core.game.bots.AIRepository
import core.game.interaction.*
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.RequestType

class PlayerOptionPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles attacking the other player.
         */

        on(IntType.PLAYER, Option.Companion.P_ATTACK.name) { player, node ->
            player.attack(node)
            return@on true
        }

        /*
         * Handles trade with the other player.
         */

        on(IntType.PLAYER, Option.Companion.P_TRADE.name) { player, node ->
            player.requestManager.request((node as Player), RequestType.TRADE)
            return@on true
        }

        /*
         * Handles assists with the other player.
         */

        on(IntType.PLAYER, Option.Companion.P_ASSIST.name) { player, node ->
            if (node is AIPlayer) AIRepository.sendBotInfo(player, node)
            player.requestManager.request((node as Player), RequestType.ASSIST)
            return@on true
        }

        /*
         * Handles following the player.
         */

        on(IntType.PLAYER, Option.Companion.P_FOLLOW.name) { player, node ->
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
