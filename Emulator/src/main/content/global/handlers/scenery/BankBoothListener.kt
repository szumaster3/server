package content.global.handlers.scenery

import content.global.handlers.npc.BankerNPC
import core.ServerConstants
import core.api.*
import core.api.interaction.openBankAccount
import core.api.interaction.openGrandExchangeCollectionBox
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.repository.Repository
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class BankBoothListener : InteractionListener {
    /**
     * Locates a banker NPC adjacent to the given node by checking the four cardinal directions (north, east, south, west).
     *
     * @param node The node from which to search for a nearby banker.
     * @return The adjacent [BankerNPC] if found, or null otherwise.
     */
    private fun locateAdjacentBankerLinear(node: Node): NPC? {
        for (dir in arrayOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)) {
            Repository.findNPC(node.location.transform(dir))?.let { return it as? BankerNPC }
        }
        return null
    }

    /**
     * Locates a banker NPC around the given node within a square area of a given size.
     *
     * @param node The node from which to search for a nearby banker.
     * @param size The radius size (in tiles) to search around the node. Default is 1.
     * @return The nearby [BankerNPC] if found, or null otherwise.
     */
    private fun locateAdjacentBankerSquare(
        node: Node,
        size: Int = 1,
    ): NPC? {
        for (y in (node.location.y - size)..(node.location.y + size)) {
            for (x in (node.location.x - size)..(node.location.x + size)) {
                Repository.findNPC(Location(x, y))?.let { return it as? BankerNPC }
            }
        }
        return null
    }

    /**
     * Attempts to start a dialogue with a nearby banker, either by finding one linearly or within a square range.
     * If a banker supporting dialogue is found, it opens their dialogue; otherwise, defaults to the standard banker dialogue.
     *
     * @param player The player initiating the dialogue.
     * @param node The node from which the banker search begins.
     */
    private fun tryInvokeBankerDialogue(
        player: Player,
        node: Node,
    ) {
        (locateAdjacentBankerLinear(node) ?: locateAdjacentBankerSquare(node, 2))?.let {
            if (core.game.dialogue.DialogueInterpreter
                    .contains(it.id)
            ) {
                it.faceLocation(node.location)
                openDialogue(player, it.id, NPC(it.id, it.location))
            } else {
                openDialogue(player, NPCs.BANKER_494)
            }
        }
    }

    /**
     * Handles a quick bank booth interaction, directly opening the bank if allowed, or invoking a banker dialogue if restricted.
     *
     * @param player The player interacting with the bank booth.
     * @param node The bank booth node being interacted with.
     * @param state The interaction state, unused in this method.
     * @return Always returns true to indicate the interaction was processed.
     */
    private fun quickBankBoothUse(
        player: Player,
        node: Node,
        state: Int,
    ): Boolean {
        if (player.ironmanManager.checkRestriction(IronmanMode.ULTIMATE)) {
            return true
        }
        if (BankerNPC.checkLunarIsleRestriction(player, node)) {
            tryInvokeBankerDialogue(player, node)
            return true
        }

        openBankAccount(player)
        return true
    }

    /**
     * Handles a regular bank booth interaction, deciding between quick opening or starting banker dialogue based on server settings.
     *
     * @param player The player interacting with the bank booth.
     * @param node The bank booth node being interacted with.
     * @param state The interaction state, unused in this method.
     * @return Always returns true to indicate the interaction was processed.
     */
    private fun regularBankBoothUse(
        player: Player,
        node: Node,
        state: Int,
    ): Boolean {
        if (player.ironmanManager.checkRestriction(IronmanMode.ULTIMATE)) {
            return true
        }

        if (ServerConstants.BANK_BOOTH_QUICK_OPEN) {
            return quickBankBoothUse(player, node, state)
        }

        tryInvokeBankerDialogue(player, node)
        return true
    }

    /**
     * Handles a bank booth interaction specifically for accessing the Grand Exchange collection box.
     *
     * @param player The player interacting with the bank booth.
     * @param node The bank booth node being interacted with.
     * @param state The interaction state, unused in this method.
     * @return Always returns true to indicate the interaction was processed.
     */
    private fun collectBankBoothUse(
        player: Player,
        node: Node,
        state: Int,
    ): Boolean {
        if (BankerNPC.checkLunarIsleRestriction(player, node)) {
            tryInvokeBankerDialogue(player, node)
            return true
        }

        openGrandExchangeCollectionBox(player)
        return true
    }

    /**
     * Attempts to convert an item into its noted or unnoted form by using it on a bank booth.
     *
     * @param player The player attempting to convert the item.
     * @param used The item node being used.
     * @param with The bank booth node with which the item is being used.
     * @return Always returns true to indicate the interaction was processed.
     */
    private fun attemptToConvertItems(
        player: Player,
        used: Node,
        with: Node,
    ): Boolean {
        if (!hasOption(with, "use")) {
            return true
        }

        if (!ServerConstants.BANK_BOOTH_NOTE_UIM && player.ironmanManager.checkRestriction(IronmanMode.ULTIMATE)) {
            return true
        }

        if (BankerNPC.checkLunarIsleRestriction(player, with)) {
            tryInvokeBankerDialogue(player, with)
            return true
        }

        val item = used as Item

        if (item.noteChange != item.id) {
            if (item.definition.isUnnoted()) {
                val amount = amountInInventory(player, item.id)
                if (removeItem(player, Item(item.id, amount))) {
                    addItem(player, item.noteChange, amount)
                }
            } else {
                var amount = item.amount
                val freeSlotCount = freeSlots(player)

                if (amount > freeSlotCount && amount != freeSlotCount + 1) {
                    amount = freeSlotCount
                }

                if (removeItem(player, Item(item.id, amount))) {
                    addItem(player, item.noteChange, amount)
                }
            }

            return true
        }

        sendMessage(player, "This item can't be noted.")
        return true
    }

    override fun defineListeners() {
        defineInteraction(IntType.SCENERY, BANK_BOOTHS, "use-quickly", "bank", handler = ::quickBankBoothUse)
        defineInteraction(IntType.SCENERY, BANK_BOOTHS, "use", handler = ::regularBankBoothUse)
        defineInteraction(IntType.SCENERY, BANK_BOOTHS, "collect", handler = ::collectBankBoothUse)

        if (ServerConstants.BANK_BOOTH_NOTE_ENABLED) {
            onUseAnyWith(IntType.SCENERY, *BANK_BOOTHS, handler = ::attemptToConvertItems)
        }
    }

    companion object {
        val INOPERABLE_BANK_BOOTHS =
            intArrayOf(
                Scenery.BANK_BOOTH_12800,
                Scenery.BANK_BOOTH_12801,
                Scenery.BANK_BOOTH_36262,
                Scenery.BANK_BOOTH_35648,
            )
        val BANK_BOOTHS =
            intArrayOf(
                Scenery.BANK_BOOTH_2213,
                Scenery.BANK_BOOTH_2214,
                Scenery.BANK_BOOTH_3045,
                Scenery.BANK_BOOTH_5276,
                Scenery.BANK_BOOTH_6084,
                Scenery.BANK_BOOTH_10517,
                Scenery.BANK_BOOTH_11338,
                Scenery.BANK_BOOTH_11402,
                Scenery.BANK_BOOTH_11758,
                Scenery.BANK_BOOTH_12798,
                Scenery.BANK_BOOTH_12799,
                Scenery.BANK_BOOTH_14367,
                Scenery.BANK_BOOTH_14368,
                Scenery.BANK_BOOTH_16700,
                Scenery.BANK_BOOTH_18491,
                Scenery.BANK_BOOTH_19230,
                Scenery.BANK_BOOTH_20325,
                Scenery.BANK_BOOTH_20326,
                Scenery.BANK_BOOTH_20327,
                Scenery.BANK_BOOTH_20328,
                Scenery.BANK_BOOTH_22819,
                Scenery.BANK_BOOTH_24914,
                Scenery.BANK_BOOTH_25808,
                Scenery.BANK_BOOTH_26972,
                Scenery.BANK_BOOTH_29085,
                Scenery.BANK_BOOTH_30015,
                Scenery.BANK_BOOTH_30016,
                Scenery.BANK_BOOTH_34205,
                Scenery.BANK_BOOTH_34752,
                Scenery.BANK_BOOTH_35647,
                Scenery.BANK_BOOTH_36786,
                Scenery.BANK_BOOTH_37474,
            )
    }
}
