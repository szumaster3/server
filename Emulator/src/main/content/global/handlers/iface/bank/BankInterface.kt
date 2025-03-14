package content.global.handlers.iface.bank

import content.global.dialogue.BankDepositDialogue
import content.global.dialogue.BankHelpDialogue
import core.ServerConstants
import core.api.*
import core.api.interaction.openBankAccount
import core.game.component.Component
import core.game.container.Container
import core.game.dialogue.InputType
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import org.rs.consts.Components
import org.rs.consts.Items

/**
 * Handles player interaction with the bank interface.
 *
 * @author vddCore
 */
class BankInterface : InterfaceListener {
    companion object {
        private const val MAIN_BUTTON_CLOSE = 10
        private const val MAIN_BUTTON_INSERT_MODE = 14
        private const val MAIN_BUTTON_NOTE_MODE = 16
        private const val MAIN_BUTTON_BOB_DEPOSIT = 18
        private const val MAIN_BUTTON_SEARCH_BANK = 20
        private const val MAIN_BUTTON_HELP = 23

        private const val MENU_ELEMENT = 73
        private const val OP_AMOUNT_ONE = 155
        private const val OP_AMOUNT_FIVE = 196
        private const val OP_AMOUNT_TEN = 124
        private const val OP_AMOUNT_LAST_X = 199
        private const val OP_AMOUNT_X = 234
        private const val OP_AMOUNT_ALL = 168
        private const val OP_AMOUNT_ALL_BUT_ONE = 166
        private const val OP_EXAMINE = 9

        private const val BANK_TAB_1 = 41
        private const val BANK_TAB_2 = 39
        private const val BANK_TAB_3 = 37
        private const val BANK_TAB_4 = 35
        private const val BANK_TAB_5 = 33
        private const val BANK_TAB_6 = 31
        private const val BANK_TAB_7 = 29
        private const val BANK_TAB_8 = 27
        private const val BANK_TAB_9 = 25

        private val BANK_TABS =
            intArrayOf(
                BANK_TAB_1,
                BANK_TAB_2,
                BANK_TAB_3,
                BANK_TAB_4,
                BANK_TAB_5,
                BANK_TAB_6,
                BANK_TAB_7,
                BANK_TAB_8,
                BANK_TAB_9,
            )

        private const val OP_SET_TAB = 155
        private const val OP_COLLAPSE_TAB = 196

        private const val THRESHOLD_TO_DISPLAY_EXACT_QUANTITY_ON_EXAMINE = 100000

        /**
         * Handles the input dialogue for entering an item transfer amount.
         *
         * @param player The player performing the action.
         * @param slot The slot of the item in the inventory or bank.
         * @param withdraw Whether the item is being withdrawn from the bank.
         * @param after An optional action to perform after the transfer.
         */
        @JvmStatic
        fun transferX(
            player: Player,
            slot: Int,
            withdraw: Boolean,
            after: (() -> Unit)? = null,
        ) {
            sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:") { value ->
                val number = Integer.parseInt(value.toString())
                if (withdraw) {
                    player.bank.takeItem(slot, number)
                } else {
                    player.bank.addItem(slot, number)
                }
                player.bank.updateLastAmountX(number)
                after?.let { it() }
            }
        }
    }

    /**
     * Handles the event when the bank interface is opened.
     *
     * @param player The player opening the bank.
     * @param component The component representing the bank interface.
     * @return `false` to allow the interface to be opened.
     */
    private fun onBankInterfaceOpen(
        player: Player,
        component: Component,
    ): Boolean {
        val settings =
            IfaceSettingsBuilder()
                .enableAllOptions()
                .enableSlotSwitch()
                .setInterfaceEventsDepth(2)
                .build()

        player.packetDispatch.sendIfaceSettings(
            settings,
            73,
            Components.BANK_V2_MAIN_762,
            0,
            ServerConstants.BANK_SIZE,
        )

        resetSearch(player)
        return false
    }

    /**
     * Handles tab interactions within the bank interface.
     *
     * @param player The player interacting with the bank.
     * @param component The component representing the bank interface.
     * @param opcode The operation code of the interaction.
     * @param buttonID The button ID within the interface.
     * @param slot The slot in the bank.
     * @param itemID The item ID being interacted with.
     * @return `true` if the interaction was handled successfully.
     */
    private fun handleTabInteraction(
        player: Player,
        component: Component,
        opcode: Int,
        buttonID: Int,
        slot: Int,
        itemID: Int,
    ): Boolean {
        resetSearch(player)
        val clickedTabIndex = -((buttonID - 41) / 2)

        when (opcode) {
            OP_SET_TAB -> {
                if (player.bank.getTabIndex() != clickedTabIndex) {
                    player.bank.setTabIndex(clickedTabIndex)
                }
            }

            OP_COLLAPSE_TAB -> {
                player.bank.collapseTab(clickedTabIndex)
            }
        }

        return true
    }

    /**
     * Handles menu interactions within the bank.
     *
     * @param player The player interacting with the menu.
     * @param component The component representing the bank menu.
     * @param opcode The operation code of the interaction.
     * @param buttonID The button ID within the interface.
     * @param slot The slot in the bank.
     * @param itemID The item ID being interacted with.
     * @return `true` if the interaction was handled successfully.
     */
    private fun handleBankMenu(
        player: Player,
        component: Component,
        opcode: Int,
        buttonID: Int,
        slot: Int,
        itemID: Int,
    ): Boolean {
        val item = player.bank.get(slot) ?: return true
        resetSearch(player)

        when (opcode) {
            OP_AMOUNT_ONE -> player.bank.takeItem(slot, 1)
            OP_AMOUNT_FIVE -> player.bank.takeItem(slot, 5)
            OP_AMOUNT_TEN -> player.bank.takeItem(slot, 10)
            OP_AMOUNT_LAST_X -> player.bank.takeItem(slot, player.bank.lastAmountX)
            OP_AMOUNT_X -> transferX(player, slot, true)
            OP_AMOUNT_ALL -> player.bank.takeItem(slot, player.bank.getAmount(item))
            OP_AMOUNT_ALL_BUT_ONE -> player.bank.takeItem(slot, player.bank.getAmount(item) - 1)
            OP_EXAMINE -> {
                var examineText = item.definition.examine
                val id = item.definition.id
                val bank = player.bank
                if (isCoinOverrideNeeded(id, bank)) {
                    examineText = "" + bank.getAmount(id) + " x " + item.definition.name + "."
                }
                sendMessage(player, examineText)
            }

            else -> player.debug("Unknown bank menu opcode $opcode")
        }

        return true
    }

    /**
     * Handles menu interactions within the bank.
     *
     * @param player The player interacting with the menu.
     * @param component The component representing the bank menu.
     * @param opcode The operation code of the interaction.
     * @param buttonID The button ID within the interface.
     * @param slot The slot in the bank.
     * @param itemID The item ID being interacted with.
     * @return `true` if the interaction was handled successfully.
     */
    private fun handleInventoryMenu(
        player: Player,
        component: Component,
        opcode: Int,
        buttonID: Int,
        slot: Int,
        itemID: Int,
    ): Boolean {
        val item = player.inventory.get(slot) ?: return true
        resetSearch(player)

        when (opcode) {
            OP_AMOUNT_ONE -> player.bank.addItem(slot, 1)
            OP_AMOUNT_FIVE -> player.bank.addItem(slot, 5)
            OP_AMOUNT_TEN -> player.bank.addItem(slot, 10)
            OP_AMOUNT_LAST_X -> player.bank.addItem(slot, player.bank.lastAmountX)
            OP_AMOUNT_X -> transferX(player, slot, false)
            OP_AMOUNT_ALL -> player.bank.addItem(slot, player.inventory.getAmount(item))
            OP_EXAMINE -> {
                var examineText = item.definition.examine
                val id = item.definition.id
                val inventory = player.inventory
                if (isCoinOverrideNeeded(id, inventory)) {
                    examineText = "" + inventory.getAmount(id) + " x " + item.definition.name + "."
                }
                sendMessage(player, examineText)
            }

            else -> player.debug("Unknown inventory menu opcode $opcode")
        }

        return true
    }

    override fun defineInterfaceListeners() {
        onOpen(Components.BANK_V2_MAIN_762, ::onBankInterfaceOpen)

        on(Components.BANK_V2_MAIN_762) { player, component, opcode, buttonID, slot, itemID ->
            when (buttonID) {
                MAIN_BUTTON_HELP -> openDialogue(player, BankHelpDialogue())
                MAIN_BUTTON_BOB_DEPOSIT -> openDialogue(player, BankDepositDialogue())
                MAIN_BUTTON_INSERT_MODE -> player.bank.isInsertItems = !player.bank.isInsertItems
                MAIN_BUTTON_NOTE_MODE -> player.bank.isNoteItems = !player.bank.isNoteItems
                MAIN_BUTTON_SEARCH_BANK -> setAttribute(player, "search", true)
                MENU_ELEMENT -> handleBankMenu(player, component, opcode, buttonID, slot, itemID)
                in BANK_TABS -> handleTabInteraction(player, component, opcode, buttonID, slot, itemID)
            }

            return@on true
        }

        on(Components.BANK_V2_HELP_767) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                MAIN_BUTTON_CLOSE -> openBankAccount(player)
            }

            return@on true
        }

        on(Components.BANK_V2_SIDE_763, ::handleInventoryMenu)
    }

    /**
     * Checks if the coin override is needed when examining an item.
     *
     * @param id The item ID.
     * @param container The container holding the item.
     * @return `true` if the override is needed, otherwise `false`.
     */
    private fun isCoinOverrideNeeded(
        id: Int,
        container: Container,
    ): Boolean {
        val amount = container.getAmount(id)
        // Only COINS_995 are obtainable and bankable by player.
        return id == Items.COINS_995 && amount >= THRESHOLD_TO_DISPLAY_EXACT_QUANTITY_ON_EXAMINE
    }

    /**
     * Resets the bank search mode.
     *
     * @param player The player whose search mode should be reset.
     */
    private fun resetSearch(player: Player) {
        val lastTab = getAttribute(player, "bank:lasttab", 0)
        player.bank.setTabIndex(lastTab)
        setVarc(player, 190, 1)
    }
}
