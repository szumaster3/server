package content.global.handlers.iface.warning

import core.api.getVarbit
import core.api.sendMessage
import core.api.setVarbit
import core.api.ui.sendInterfaceConfig
import core.game.component.Component
import core.game.node.entity.player.Player
import core.tools.DARK_PURPLE
import org.rs.consts.Components

object WarningManager {
    @JvmStatic
    fun openWarning(
        player: Player,
        warning: Warnings,
    ) {
        player.interfaceManager.open(Component(warning.component))
        increment(player, warning.varbit)
    }

    @JvmStatic
    fun toggle(
        player: Player,
        componentId: Int,
    ) {
        val warning = Warnings.values().find { it.component == componentId } ?: return
        toggleWarning(player, warning)
    }

    @JvmStatic
    fun toggleWarning(
        player: Player,
        warning: Warnings,
    ) {
        warning.isDisabled = !warning.isDisabled
        setVarbit(player, warning.varbit, if (warning.isDisabled) 7 else 6, true)
        sendMessage(
            player,
            "You have toggled this warning screen " + (if (warning.isDisabled) "off" else "on") + ". You will " +
                (if (!warning.isDisabled) "see this interface again." else "no longer see this warning screen."),
        )
    }

    @JvmStatic
    fun increment(
        player: Player,
        varbitId: Int,
    ) {
        Warnings.values().find { it.component == varbitId }?.let { component ->
            val currentStatus = getVarbit(player, component.varbit)
            if (currentStatus < 6) {
                val newStatus = (currentStatus + 1).coerceAtMost(6)
                setVarbit(player, component.varbit, newStatus, true)
                player.debug(
                    "Component varbit: [$DARK_PURPLE$component</col>] increased to: [$DARK_PURPLE$newStatus</col>].",
                )
                if (newStatus == 6) {
                    val toggleButton = if (component.component == Components.WILDERNESS_WARNING_382) 26 else if (component.component == Components.CWS_WARNING_24_581) 19 else 21
                    sendInterfaceConfig(player, component.component, toggleButton, false)
                    sendMessage(player, "You can now disable this warning in the settings.")
                }
            }
        }
    }
}
