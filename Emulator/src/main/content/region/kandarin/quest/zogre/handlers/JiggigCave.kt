package content.region.kandarin.quest.zogre.handlers

import core.api.*
import core.api.MapArea
import core.api.ui.closeDialogue
import core.api.utils.PlayerCamera
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders

class JiggigCave : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2445, 9458, 2447, 9467))
    }

    override fun entityStep(
        entity: Entity,
        location: Location,
        lastLocation: Location,
    ) {
        super.entityStep(entity, location, lastLocation)
        if (entity is Player) {
            val player = entity.asPlayer()

            if (inBorders(player, CHARRED_AREA)) {
                stopWalk(entity)
                if (getAttribute(entity.asPlayer(), ZUtils.CHARRED_AREA, false)) {
                    return
                }

                lock(entity, 4)
                submitWorldPulse(
                    object : Pulse() {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> {
                                    entity.dialogueInterpreter.sendPlainMessage(
                                        true,
                                        "You enter this blackened, charred area -",
                                        "it looks like some sort of explosion has taken place.",
                                    )
                                    sendMessage(
                                        entity,
                                        "You enter this blackened, charred area - it looks like there's been an explosion!",
                                    )
                                }

                                1 -> {
                                    closeDialogue(entity)
                                    PlayerCamera(entity).setPosition(2447, 9457, 400)
                                    PlayerCamera(entity).panTo(2441, 9459, 400, 100)
                                }

                                2 -> {
                                    PlayerCamera(entity).rotateTo(2441, 9459, 300, 10)
                                }

                                3 -> {
                                    PlayerCamera(entity).reset()
                                    setAttribute(entity, "/save${ZUtils.CHARRED_AREA}", true)
                                    unlock(entity)
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }
        }
    }

    companion object {
        val CHARRED_AREA = ZoneBorders(2445, 9458, 2447, 9467)
    }
}
