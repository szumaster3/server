package content.region.misc.handlers.tutorial

import core.api.*
import core.game.component.Component
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders

class Tutorial :
    MapArea,
    LogoutListener {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(
            getRegionBorders(12079),
            getRegionBorders(12180),
            getRegionBorders(12592),
            getRegionBorders(12436),
            getRegionBorders(12335),
            getRegionBorders(12336),
        )
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            val p = entity.asPlayer()
            if (getAttribute(p, TutorialStage.TUTORIAL_STAGE, 0) != 72) {
                lockTeleport(p)
            } else {
                p.locks.unlockTeleport()
            }
            if (getAttribute(p, TutorialStage.TUTORIAL_STAGE, -1) >= 72) {
                Component.setUnclosable(
                    p,
                    p.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "You have almost completed the tutorial!",
                        "",
                        "Just click on the first spell, Home Teleport, in your Magic",
                        "Spellbook. This spell doesn't require any runes, but can only",
                        "be cast once every 30 minutes.",
                    ),
                )
            }
        }
    }

    override fun entityStep(
        entity: Entity,
        location: Location,
        lastLocation: Location,
    ) {
        super.entityStep(entity, location, lastLocation)
        if (entity is Player) {
            val player = entity.asPlayer()
            val stage = getAttribute(player, TutorialStage.TUTORIAL_STAGE, -1)

            if (stage >= 72) {
                player.locks.unlockTeleport()
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "You have almost completed the tutorial!",
                        "",
                        "Just click on the first spell, Home Teleport, in your Magic",
                        "Spellbook. This spell doesn't require any runes, but can only",
                        "be cast once every 30 minutes.",
                    ),
                )
            }
        }
    }

    override fun logout(player: Player) {
        if (getAttribute(player, TutorialStage.TUTORIAL_STAGE, -1) >= 72) {
            Component.setUnclosable(
                player,
                player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                    "You have almost completed the tutorial!",
                    "",
                    "Just click on the first spell, Home Teleport, in your Magic",
                    "Spellbook. This spell doesn't require any runes, but can only",
                    "be cast once every 30 minutes.",
                ),
            )
        }
    }
}
