package content.minigame.duelarena.handlers

import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.RequestModule

class DuelRequestModule(
    private val staked: Boolean,
) : RequestModule {
    override fun open(
        player: Player?,
        target: Player?,
    ) {
        val session = DuelSession(player, target, staked)
        player!!.addExtension(DuelSession::class.java, session)
        target!!.addExtension(DuelSession::class.java, session)
        session.openRules()
    }
}
