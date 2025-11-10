package core.game.node.entity.player.link

import core.api.*
import core.game.component.Component
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.tools.DARK_PURPLE
import shared.consts.Components
import shared.consts.Scenery
import shared.consts.Vars

class WarningManager : InteractionListener, InterfaceListener {

    override fun defineListeners() {
        /*
         * Generic scenery interactions.
         */

        SCENERY_WARNINGS.forEach { (sceneryId, warning) ->
            on(sceneryId, IntType.SCENERY, "go-through", "climb", "open", "cross", "climb-down", "climb-up") { player, node ->
                if ((sceneryId == Scenery.GATE_3506 || sceneryId == Scenery.GATE_3507) && player.location.y < 3458) {
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                } else {
                    handleSceneryInteraction(player, warning, node.asScenery())
                }
                return@on true
            }
        }

        /*
         * Handles corporeal beast passage (ODD).
         */

        on(Scenery.PASSAGE_37929, IntType.SCENERY, "go-through") { player, _ ->
            val offset = if (player.location.x > 2917) -4 else 4
            player.properties.teleportLocation = player.location.transform(offset, 0, 0)
            return@on true
        }

        on(Scenery.PASSAGE_38811, IntType.SCENERY, "go-through") { player, _ ->
            val warning = Warnings.CORPOREAL_BEAST_DANGEROUS
            if (!isWarningDisabled(player, warning) && player.location.x < 2974) {
                openWarningInterface(player, warning)
            } else {
                val offset = if (player.location.x > 2970) -4 else 4
                player.properties.teleportLocation = player.location.transform(offset, 0, 0)
            }
            return@on true
        }
    }

    override fun defineInterfaceListeners() {

        /*
         * Generic warning button handlers.
         */

        Warnings.values.forEach { warning ->
            if (warning.component != -1) {
                on(warning.component) { player, _, _, buttonId, _, _ ->
                    handleInterfaceButton(player, warning, buttonId)
                    return@on true
                }
            }
        }

    }

    private fun handleInterfaceButton(player: Player, warning: Warnings, buttonId: Int) {
        closeOverlay(player)
        closeInterface(player)
        when (buttonId) {
            17 -> confirmWarning(player, warning)
            18 -> warning.action(player)
            19, 20, 28 -> toggleWarning(player, warning)
        }
    }

    private fun confirmWarning(player: Player, warning: Warnings) {
        warning.action(player)
        incrementWarning(player, warning)
    }

    private fun handleSceneryInteraction(player: Player, warning: Warnings, node: core.game.node.scenery.Scenery) {
        when (warning) {
            Warnings.MORT_MYRE -> player.setAttribute("myre_gate", node)
            Warnings.WILDERNESS_DITCH -> player.setAttribute("wildy_ditch", node)
            else -> {}
        }

        if (isWarningDisabled(player, warning)) {
            warning.action(player)
        } else {
            openWarningInterface(player, warning)
        }
    }

    companion object {

        private val SCENERY_WARNINGS = listOf(
            Scenery.STAIRS_25432 to Warnings.OBSERVATORY_STAIRS,
            Scenery.DARK_HOLE_5947 to Warnings.LUMBRIDGE_SWAMP_CAVE_ROPE,
            Scenery.GATE_3506 to Warnings.MORT_MYRE,
            Scenery.GATE_3507 to Warnings.MORT_MYRE,
            Scenery.TOWER_LADDER_2511 to Warnings.RANGING_GUILD
        )

        @JvmStatic
        fun openWarningInterface(player: Player, warning: Warnings) {
            if (isWarningDisabled(player, warning)) return
            player.interfaceManager.open(Component(warning.component))
            incrementWarning(player, warning)
        }

        @JvmStatic
        fun isWarningDisabled(player: Player, warning: Warnings): Boolean =
            getVarbit(player, warning.varbit) == 7

        private fun isWarningUnlocked(player: Player, warning: Warnings): Boolean =
            getVarbit(player, warning.varbit) >= 6

        fun incrementWarning(player: Player, warning: Warnings) {
            val current = getVarbit(player, warning.varbit)
            val next = (current + 1).coerceAtMost(6)

            if (current < 6) {
                setVarbit(player, warning.varbit, next, true)
                player.debug("Component varbit [$DARK_PURPLE$warning</col>] increased to [$DARK_PURPLE$next</col>].")
                if (next == 6) {
                    enableToggleButton(player, warning)
                    sendMessage(player, "You can now toggle this warning in settings.")
                }
            } else if (current == 6) {
                enableToggleButton(player, warning)
            }
        }

        fun toggleWarning(player: Player, warning: Warnings) {
            val current = getVarbit(player, warning.varbit)
            val (newValue, message) = if (current == 7) {
                6 to "You have toggled this warning screen on. You will see this interface again."
            } else {
                7 to "You have toggled this warning screen off. You will no longer see it."
            }

            setVarbit(player, warning.varbit, newValue, true)
            sendMessage(player, message)
        }

        private fun enableToggleButton(player: Player, warning: Warnings) {
            val toggleButtonId = when (warning.component) {
                Components.WILDERNESS_WARNING_382 -> 26
                else -> 21
            }
            sendInterfaceConfig(player, warning.component, toggleButtonId, false)
        }
    }
}
