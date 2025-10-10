package core.game.node.entity.player.link

import core.api.*
import core.game.component.Component
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.WarningActions.getDitchLocations
import core.tools.DARK_PURPLE
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Scenery
import shared.consts.Vars

class WarningManager : InteractionListener, InterfaceListener {

    private val cwsWarnings = mapOf(
        Components.CWS_WARNING_24_581 to Vars.VARBIT_CWS_WARNING_24_3872,
        Components.CWS_WARNING_26_627 to Vars.VARBIT_CWS_WARNING_26_4132
    )

    private fun handleInterfaceInteraction(player: Player, warning: Warnings, buttonId: Int) {
        closeOverlay(player)
        closeInterface(player)
        when (buttonId) {
            17 -> confirmWarning(player, warning)
            18 -> executeWarning(player, warning.component)
            20, 28 -> toggleWarning(player, warning.component)
        }
    }

    private fun confirmWarning(player: Player, warning: Warnings) {
        warning.action(player)
        incrementWarning(player, warning.component)
    }

    private fun executeWarning(player: Player, component: Int) {
        when (component) {
            Components.WILDERNESS_WARNING_382 -> handleWildernessInteraction(player)
            else -> handleClanWarsInteraction(player, component)
        }
    }

    private fun handleWildernessInteraction(player: Player) {
        player.interfaceManager.close()
        player.getAttribute<core.game.node.scenery.Scenery>("wildy_ditch")
            ?.let { WarningActions.handleWildernessJump(player) }
        incrementWarning(player, Components.WILDERNESS_WARNING_382)
    }

    private fun handleClanWarsInteraction(player: Player, component: Int) {
        val varbit = cwsWarnings[component]
        if (varbit != null && getVarbit(player, varbit) >= 6) {
            toggleWarning(player, component)
        } else {
            incrementWarning(player, component)
        }
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

    override fun defineListeners() {
        /*
         * Generic scenery interactions.
         */

        SCENERY_WARNINGS.forEach { (sceneryId, warning) ->
            on(sceneryId, IntType.SCENERY, "go-through", "climb", "open", "cross", "climb-down", "climb-up") { player, node ->
                // Special case for Mort Myre double gate.
                if ((sceneryId == 3506 || sceneryId == 3507) && player.location.y < 3458) {
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
            if (!isWarningDisabled(player, Warnings.CORPOREAL_BEAST_DANGEROUS) && player.location.x < 2974) {
                openInterface(player, Components.CWS_WARNING_30_650)
            } else {
                val offset = if (player.location.x > 2970) -4 else 4
                player.properties.teleportLocation = player.location.transform(offset, 0, 0)
            }
            return@on true
        }
    }

    override fun defineInterfaceListeners() {
        /*
         * Handles special clan war warning.
         */

        onOpen(Components.CWS_WARNING_24_581) { player, component ->
            incrementWarning(player, component.id)
            return@onOpen true
        }

        /*
         * Generic warning button handlers.
         */

        Warnings.values.forEach { warning ->
            if (warning.component != -1) {
                on(warning.component) { player, _, _, buttonId, _, _ ->
                    handleInterfaceInteraction(player, warning, buttonId)
                    return@on true
                }
            }
        }
    }

    companion object {
        private val SCENERY_WARNINGS = listOf(
            Scenery.STAIRS_25432 to Warnings.OBSERVATORY_STAIRS,
            Scenery.CLIMBING_ROPE_5946 to Warnings.LUMBRIDGE_SWAMP_CAVE_ROPE,
            Scenery.DARK_HOLE_5947 to Warnings.LUMBRIDGE_SWAMP_CAVE_ROPE,
            Scenery.GATE_3506 to Warnings.MORT_MYRE,
            Scenery.GATE_3507 to Warnings.MORT_MYRE,
            Scenery.TOWER_LADDER_2511 to Warnings.RANGING_GUILD
        )

        @JvmStatic
        fun openWarningInterface(player: Player, warning: Warnings) {
            if (isWarningDisabled(player, warning)) return
            player.interfaceManager.open(Component(warning.component))
            incrementWarning(player, warning.varbit)
        }

        @JvmStatic
        fun isWarningDisabled(player: Player, warning: Warnings): Boolean =
            getVarbit(player, warning.varbit) == 7

        fun incrementWarning(player: Player, varbitId: Int) {
            val warning = Warnings.values.find { it.varbit == varbitId } ?: return
            val current = getVarbit(player, warning.varbit)
            val next = (current + 1).coerceAtMost(6)

            if (current < 6) {
                setVarbit(player, warning.varbit, next, true)
                player.debug("Component varbit [$DARK_PURPLE$warning</col>] increased to [$DARK_PURPLE$next</col>].")
                if (next == 6) {
                    enableToggleButton(player, warning)
                    sendMessage(player, "You can now disable this warning in the settings.")
                }
            } else if (current == 6) {
                enableToggleButton(player, warning)
            }
        }

        fun toggleWarning(player: Player, componentId: Int) {
            Warnings.values.find { it.component == componentId }?.let { toggleWarningState(player, it) }
        }

        private fun toggleWarningState(player: Player, warning: Warnings) {
            val current = getVarbit(player, warning.varbit)
            val (newValue, message) = if (current == 6) {
                7 to "You have toggled this warning screen off. You will no longer see it."
            } else {
                6 to "You have toggled this warning screen on. You will see this interface again."
            }
            setVarbit(player, warning.varbit, newValue, true)
            sendMessage(player, message)
        }

        private fun enableToggleButton(player: Player, warning: Warnings) {
            val toggleButton = when (warning.component) {
                Components.WILDERNESS_WARNING_382 -> 26
                Components.CWS_WARNING_24_581 -> 19
                else -> 21
            }
            sendInterfaceConfig(player, warning.component, toggleButton, false)
        }
    }
}
