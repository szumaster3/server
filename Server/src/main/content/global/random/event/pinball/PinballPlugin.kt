package content.global.random.event.pinball

import content.data.GameAttributes
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.tools.BLUE
import shared.consts.Animations
import shared.consts.Components

/**
 * Handles pinball re interactions.
 * @author szu
 */
class PinballPlugin : InteractionListener, MapArea {
    init {
        PinballUtils.PINBALL_EVENT_MYSTERIOUS_OLD_MAN.init()
        PinballUtils.PINBALL_EVENT_MYSTERIOUS_OLD_MAN.isWalks = false
        PinballUtils.PINBALL_EVENT_MYSTERIOUS_OLD_MAN.isInvisible = false
        PinballUtils.PINBALL_EVENT_MYSTERIOUS_OLD_MAN.direction = Direction.EAST
    }

    override fun defineListeners() {
        on(PinballUtils.PINBALL_EVENT_SCENERY_IDs, IntType.SCENERY, "tag") { player, node ->
            var points = getVarbit(player, 2121)

            lock(player, 1)
            animate(player, Animations.HUMAN_MULTI_USE_832)

            fun updateScoreDisplay() =
                sendString(player, "Score: $points", Components.PINBALL_INTERFACE_263, 1)

            fun resetPinballTarget() {
                setAttribute(player, GameAttributes.RE_PINBALL_OBJ, (0..4).random())
                PinballUtils.replaceTag(player)
                PinballUtils.generateTag(player)
            }

            if (points >= 10) {
                sendPlainDialogue(player, true, "", "Congratulations - you can now leave the arena.")
                updateScoreDisplay()
                return@on true
            }

            if (node.id in PinballUtils.PINBALL_EVENT_WRONG_SCENERY_IDs) {
                points = 0
                setVarbit(player, 2121, points)
                updateScoreDisplay()

                sendUnclosablePlainDialogue(
                    player,
                    true,
                    "",
                    "Wrong post! Your score has been reset.",
                    "Tag the post with the ${BLUE}flashing rings</col>."
                )

                resetPinballTarget()
                return@on true
            }

            points += 1
            setVarbit(player, 2121, points)
            updateScoreDisplay()

            if (points < 10) {
                sendUnclosablePlainDialogue(player, true, "", "Well done! Now tag the next post.")
                resetPinballTarget()
            } else {
                PinballUtils.replaceTag(player)
                sendPlainDialogue(player, true, "", "Congratulations - you can now leave the arena.")
            }

            return@on true
        }

        on(PinballUtils.PINBALL_EVENT_CAVE_EXIT_SCENERY_ID, IntType.SCENERY, "exit") { player, _ ->
            if (getVarbit(player, PinballUtils.VARBIT_PINBALL_SCORE) >= 10) {
                PinballUtils.cleanup(player)
                PinballUtils.reward(player)
            } else {
                openDialogue(player, PinballGuardDialogue())
            }
            return@on true
        }

        on(PinballUtils.PINBALL_EVENT_GUARD_NPCs, IntType.NPC, "Talk-to") { player, npc ->
            openDialogue(player, PinballGuardDialogue(), npc)
            return@on true
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(PinballUtils.PINBALL_EVENT_ZONE_BORDERS)

    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        if (entity is Player) {
            val player = entity.asPlayer()
            if (getVarbit(player, PinballUtils.VARBIT_PINBALL_SCORE) >= 10) {
                player.dialogueInterpreter.sendPlainMessage(true, "", "Congratulations - you can now leave the arena.")
            }
        }
    }

    override fun getRestrictions(): Array<ZoneRestriction> = arrayOf(
        ZoneRestriction.CANNON,
        ZoneRestriction.FOLLOWERS,
        ZoneRestriction.FIRES,
    )

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            val player = entity.asPlayer()
            PinballUtils.cleanup(player)
        }
    }
}
