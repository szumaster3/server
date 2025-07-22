package content.data.consumables.effects

import core.api.getTimer
import core.api.removeTimer
import core.api.sendMessage
import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.system.timer.impl.Disease

/**
 * Reduces disease timer hits or cures disease on use.
 */
class CureDiseaseEffect : ConsumableEffect() {
    /**
     * Decreases disease hits; removes timer if cured and notifies player.
     */
    override fun activate(p: Player) {
        val existingTimer = getTimer<Disease>(p)
        if (existingTimer != null) {
            existingTimer.hitsLeft -= 9
            if (existingTimer.hitsLeft <= 0) {
                sendMessage(p, "The disease has been cured.")
                removeTimer<Disease>(p)
            } else {
                sendMessage(p, "You feel slightly better.")
            }
        }
    }
}