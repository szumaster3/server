package content.global.plugin.iface.warning

import core.api.getVarbit
import core.api.sendMessage
import core.api.setVarbit
import core.api.ui.sendInterfaceConfig
import core.game.component.Component
import core.game.node.entity.player.Player
import core.tools.DARK_PURPLE
import org.rs.consts.Components

/**
 * Handles warning screen logic, including display, tracking, and toggling.
 */
object WarningManager {

    /**
     * Opens a warning interface if not disabled by the player.
     *
     * @param player The player receiving the warning.
     * @param warning The warning to display.
     */
    @JvmStatic
    fun openWarning(player: Player, warning: Warnings) {
        if (isDisabled(player, warning)) {
            return
        }

        player.interfaceManager.open(Component(warning.component))
        increment(player, warning.varbit)
    }

    /**
     * Toggles a warning on/off using its component ID.
     *
     * @param player The player toggling the warning.
     * @param componentId The component ID linked to the warning.
     */
    @JvmStatic
    fun toggle(player: Player, componentId: Int) {
        val warning = Warnings.values().find { it.component == componentId } ?: return
        toggleWarning(player, warning)
    }

    /**
     * Toggles the warning state based on current varbit.
     *
     * @param player The player toggling the warning.
     * @param warning The warning to toggle.
     */
    @JvmStatic
    fun toggleWarning(player: Player, warning: Warnings) {
        val current = getVarbit(player, warning.varbit)
        if (current == 6) {
            setVarbit(player, warning.varbit, 7, true)
            sendMessage(player, "You have toggled this warning screen off. You will no longer see this warning screen.")
        } else {
            setVarbit(player, warning.varbit, 6, true)
            sendMessage(player, "You have toggled this warning screen on. You will see this interface again.")
        }
    }

    /**
     * Increments the usage counter of a warning and enables toggle option at threshold.
     *
     * @param player The player who triggered the warning.
     * @param varbitId The varbit ID associated with the warning.
     */
    @JvmStatic
    fun increment(player: Player, varbitId: Int) {
        Warnings.values().find { it.varbit == varbitId }?.let { warning ->
            val currentStatus = getVarbit(player, warning.varbit)
            if (currentStatus < 6) {
                val newStatus = (currentStatus + 1).coerceAtMost(6)
                setVarbit(player, warning.varbit, newStatus, true)
                player.debug("Component varbit: [$DARK_PURPLE$warning</col>] increased to: [$DARK_PURPLE$newStatus</col>].")
                if (newStatus == 6) {
                    enableToggleButton(player, warning)
                    sendMessage(player, "You can now disable this warning in the settings.")
                }
            } else if (currentStatus == 6) {
                enableToggleButton(player, warning)
            }
        }
    }

    /**
     * Enables the toggle button on the warning interface once threshold is reached.
     */
    private fun enableToggleButton(player: Player, warning: Warnings) {
        val toggleButton = when (warning.component) {
            Components.WILDERNESS_WARNING_382 -> 26
            Components.CWS_WARNING_24_581 -> 19
            else -> 21
        }
        sendInterfaceConfig(player, warning.component, toggleButton, false)
    }

    /**
     * Checks if the warning is disabled for the player.
     */
    @JvmStatic
    fun isDisabled(player: Player, warning: Warnings): Boolean {
        return getVarbit(player, warning.varbit) == 7
    }
}
