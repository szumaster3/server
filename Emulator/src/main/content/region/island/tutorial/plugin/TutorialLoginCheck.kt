package content.region.island.tutorial.plugin

import content.data.GameAttributes
import core.api.LoginListener
import core.api.getAttribute
import core.api.setAttribute
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld

class TutorialLoginCheck : LoginListener {
    override fun login(player: Player) {
        if (!getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)) {
            setVarbit(player, 4895, 2)
            if (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0) == 0 &&
                (player.skills.totalLevel > 33 || player.bank.itemCount() > 0 || player.inventory.itemCount() > 0)
            ) {
                setAttribute(player, "/save:${GameAttributes.TUTORIAL_COMPLETE}", true)
                setVarbit(player, 4895, 0)
                return
            }
            GameWorld.Pulser.submit(
                object : Pulse(2) {
                    override fun pulse(): Boolean {
                        TutorialStage.load(player, getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0), true)
                        return true
                    }
                },
            )
        }
    }
}
