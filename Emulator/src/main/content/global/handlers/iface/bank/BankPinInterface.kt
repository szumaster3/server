package content.global.handlers.iface.bank

import core.game.interaction.InterfaceListener
import org.rs.consts.Components

class BankPinInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.BANKPIN_SETTINGS_14) { player, _, _, buttonID, _, _ ->
            val manager = player.bankPinManager
            when (buttonID) {
                60, 62 ->
                    if (!manager.hasPin()) {
                        manager.toggleConfirmInterface(buttonID == 60)
                    } else {
                        manager.setChangingState(1)
                        manager.openPin()
                    }

                63 -> manager.toggleConfirmInterface(true)
                65 ->
                    manager.cancelPin(
                        "The PIN has been cancelled",
                        "and will NOT be set.",
                        "",
                        "You still do not have a Bank",
                        "PIN.",
                    )

                89, 91 -> manager.handleConfirmInterface(buttonID)
                61, 64 -> manager.switchRecovery()
            }
            return@on true
        }

        on(Components.BANKPIN_MAIN_13) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                30 -> {}
                else -> player.bankPinManager.updateTempPin(buttonID - 1)
            }
            return@on true
        }
    }
}
