package content.region.kandarin.feldip.jiggig

import content.region.kandarin.feldip.jiggig.quest.zogre.plugin.ZogreUtils
import core.api.*
import core.api.utils.PlayerCamera
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders

class JiggigCave : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> =
        arrayOf(CHARRED_AREA)

    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        super.entityStep(entity, location, lastLocation)

        if (entity !is Player) return
        val player = entity

        if (!inBorders(player, CHARRED_AREA)) return
        if (getAttribute(player, ZogreUtils.CHARRED_AREA, false)) return

        stopWalk(player)
        lock(player, 4)
        playCharredAreaSequence(player)
    }

    private fun playCharredAreaSequence(player: Player) {
        submitWorldPulse(object : Pulse() {
            private var step = 0

            override fun pulse(): Boolean {
                when (step++) {
                    0 -> {
                        val message = "You enter this blackened, charred area — it looks like some sort of explosion has taken place."
                        player.dialogueInterpreter.sendPlainMessage(true, message)
                        sendMessage(player, message)
                    }

                    1 -> {
                        closeDialogue(player)
                        PlayerCamera(player).apply {
                            setPosition(2447, 9457, 400)
                            panTo(2441, 9459, 400, 100)
                        }
                    }

                    2 -> {
                        PlayerCamera(player).rotateTo(2441, 9459, 300, 10)
                    }

                    3 -> {
                        PlayerCamera(player).reset()
                        setAttribute(player, "/save${ZogreUtils.CHARRED_AREA}", true)
                        unlock(player)
                        return true
                    }
                }
                return false
            }
        })
    }

    companion object {
        private val CHARRED_AREA = ZoneBorders(2445, 9458, 2447, 9467)
    }
}
