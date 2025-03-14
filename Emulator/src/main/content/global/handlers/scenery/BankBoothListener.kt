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
    private fun locateAdjacentBankerLinear(node: Node): NPC? {
        for (dir in arrayOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)) {
            Repository.findNPC(node.location.transform(dir))?.let { return it as? BankerNPC }
        }
        return null
    }

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
            if (item.definition.isUnnoted) {
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
