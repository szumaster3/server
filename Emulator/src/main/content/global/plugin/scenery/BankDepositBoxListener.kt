package content.global.plugin.scenery

import core.api.dumpContainer
import core.api.getUsedOption
import core.api.interaction.restrictForIronman
import core.api.removeTabs
import core.api.sendString
import core.game.component.CloseEvent
import core.game.component.Component
import core.game.container.access.InterfaceContainer
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.game.world.GameWorld
import org.rs.consts.Components
import org.rs.consts.Scenery

class BankDepositBoxListener : InteractionListener {
    private fun openDepositBox(
        player: Player,
        node: Node,
        state: Int,
    ): Boolean {
        val option = getUsedOption(player)
        if (option == "Deposit-all") {
            dumpContainer(player, player.inventory)
            return true
        }

        restrictForIronman(player, IronmanMode.ULTIMATE) {
            player.interfaceManager.open(Component(Components.BANK_DEPOSIT_BOX_11))?.closeEvent =
                CloseEvent { p, _ ->
                    p.interfaceManager.openDefaultTabs()
                    return@CloseEvent true
                }
            sendString(
                player,
                "The Bank of " + GameWorld.settings!!.name + " - Deposit Box",
                Components.BANK_DEPOSIT_BOX_11,
                12,
            )
            removeTabs(player, 0, 1, 2, 3, 4, 5, 6)
            InterfaceContainer.generateItems(
                player,
                player.inventory.toArray(),
                arrayOf("Deposit-X", "Deposit-All", "Deposit-10", "Deposit-5", "Deposit-1"),
                Components.BANK_DEPOSIT_BOX_11,
                15,
                5,
                7,
            )
        }
        return true
    }

    override fun defineListeners() {
        defineInteraction(IntType.SCENERY, BANK_DEPOSIT_BOXES, "deposit", "deposit-all", handler = ::openDepositBox)
    }

    companion object {
        private val BANK_DEPOSIT_BOXES =
            intArrayOf(
                Scenery.BANK_DEPOSIT_BOX_9398,
                Scenery.BANK_DEPOSIT_BOX_20228,
                Scenery.BANK_DEPOSIT_BOX_25937,
                Scenery.BANK_DEPOSIT_BOX_26969,
                Scenery.BANK_DEPOSIT_BOX_34755,
                Scenery.BANK_DEPOSIT_BOX_36788,
                Scenery.BANK_DEPOSIT_BOX_39830,
                Scenery.DEPOSIT_TABLE_39533,
                Scenery.DEPOSIT_BOX_15985,
                Scenery.DEPOSIT_BOX_30044,
                Scenery.DEPOSIT_BOX_30045,
            )
    }
}
