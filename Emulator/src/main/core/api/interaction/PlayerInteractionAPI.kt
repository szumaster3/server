package core.api.interaction

import content.global.handlers.iface.ge.StockMarket
import core.api.*
import core.api.interaction.SecondaryBankAccountActivationResult.*
import core.game.ge.ExchangeHistory
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Restricts an action for a player based on their Ironman mode.
 *
 * @param player The player to check the restriction for.
 * @param restriction The [IronmanMode] that restricts access to the action.
 * @param action The action to execute if the restriction is not applied.
 */
fun restrictForIronman(
    player: Player,
    restriction: IronmanMode,
    action: () -> Unit,
) {
    if (!player.ironmanManager.checkRestriction(restriction)) {
        action()
    }
}

/**
 * Checks if the player has any awaiting Grand Exchange collections.
 *
 * @param player The player whose Grand Exchange records are being checked.
 * @return true if there is at least one offer with an item awaiting collection, otherwise false.
 */
fun hasAwaitingGrandExchangeCollections(player: Player): Boolean {
    val records = ExchangeHistory.getInstance(player)

    for (record in records.offerRecords) {
        val offer = records.getOffer(record)

        return offer != null && offer.withdraw[0] != null
    }

    return false
}

/**
 * Opens the Grand Exchange collection box for the player, with restrictions based on Ironman mode.
 *
 * @param player The player who is attempting to open the Grand Exchange collection box.
 */
fun openGrandExchangeCollectionBox(player: Player) {
    restrictForIronman(player, IronmanMode.ULTIMATE) {
        ExchangeHistory.getInstance(player).openCollectionBox()
    }
}

/**
 * Opens the player's bank account interface, with restrictions based on Ironman mode.
 *
 * @param player The player who is attempting to open the bank account.
 */
fun openBankAccount(player: Player) {
    restrictForIronman(player, IronmanMode.ULTIMATE) {
        player.bank.open()
    }
}

/**
 * Opens the player's deposit box interface, with restrictions based on Ironman mode.
 *
 * @param player The player who is attempting to open the deposit box.
 */
fun openDepositBox(player: Player) {
    restrictForIronman(player, IronmanMode.ULTIMATE) {
        player.bank.openDepositBox()
    }
}

/**
 * Opens the player's bank pin settings, with restrictions based on Ironman mode.
 *
 * @param player The player who is attempting to open the bank pin settings.
 */
fun openBankPinSettings(player: Player) {
    restrictForIronman(player, IronmanMode.ULTIMATE) {
        player.bankPinManager.openSettings()
    }
}

/**
 * Toggles the player's bank account between primary and secondary accounts, with restrictions based on Ironman mode.
 *
 * @param player The player who is attempting to toggle their bank account.
 */
fun toggleBankAccount(player: Player) {
    restrictForIronman(player, IronmanMode.ULTIMATE) {
        if (!hasActivatedSecondaryBankAccount(player)) {
            return@restrictForIronman
        }

        player.useSecondaryBank = !player.useSecondaryBank
    }
}

/**
 * Returns the name of the player's bank account (primary or secondary), with an option to invert the result.
 *
 * @param player The player whose bank account name is being retrieved.
 * @param invert If true, inverts the result between primary and secondary.
 * @return The name of the player's bank account ("primary" or "secondary").
 */
fun getBankAccountName(
    player: Player,
    invert: Boolean = false,
): String {
    return if (isUsingSecondaryBankAccount(player)) {
        if (invert) "primary" else "secondary"
    } else {
        if (invert) "secondary" else "primary"
    }
}

/**
 * Activates the player's secondary bank account if they meet the required conditions.
 *
 * @param player The player who is attempting to activate their secondary bank account.
 * @return The result of the secondary bank account activation.
 */
fun activateSecondaryBankAccount(player: Player): SecondaryBankAccountActivationResult {
    if (hasIronmanRestriction(player, IronmanMode.ULTIMATE)) {
        return SecondaryBankAccountActivationResult.INTERNAL_FAILURE
    }

    if (hasActivatedSecondaryBankAccount(player)) {
        return SecondaryBankAccountActivationResult.ALREADY_ACTIVE
    }

    val cost = 10000
    val coinsInInventory = amountInInventory(player, Items.COINS_995)
    val coinsInBank = amountInBank(player, Items.COINS_995)
    val coinsTotal = coinsInInventory + coinsInBank

    if (cost > coinsTotal) {
        return SecondaryBankAccountActivationResult.NOT_ENOUGH_MONEY
    }

    val operationResult =
        if (cost > coinsInInventory) {
            val amountToTakeFromBank = cost - coinsInInventory

            removeItem(player, Item(Items.COINS_995, coinsInInventory), Container.INVENTORY) &&
                removeItem(
                    player,
                    Item(Items.COINS_995, amountToTakeFromBank),
                    Container.BANK,
                )
        } else {
            removeItem(player, Item(Items.COINS_995, cost))
        }

    return if (operationResult) {
        setAttribute(player, "/save:UnlockedSecondaryBank", true)
        SecondaryBankAccountActivationResult.SUCCESS
    } else {
        sendMessage(player, "$cost;$coinsInInventory;$coinsInBank;$coinsTotal")
        SecondaryBankAccountActivationResult.INTERNAL_FAILURE
    }
}

/**
 * Represents the result of an attempt to activate a secondary bank account.
 *
 * This enum is used to indicate the outcome of a player's attempt to activate their secondary bank account.
 * The possible results are:
 * - [SUCCESS]: The secondary bank account was successfully activated.
 * - [ALREADY_ACTIVE]: The player already has an active secondary bank account.
 * - [NOT_ENOUGH_MONEY]: The player does not have enough money to activate the secondary bank account.
 * - [INTERNAL_FAILURE]: An internal failure occurred during the activation process.
 */
enum class SecondaryBankAccountActivationResult {
    /**
     * The secondary bank account was successfully activated.
     */
    SUCCESS,

    /**
     * The player already has an active secondary bank account.
     */
    ALREADY_ACTIVE,

    /**
     * The player does not have enough money to activate the secondary bank account.
     */
    NOT_ENOUGH_MONEY,

    /**
     * An internal failure occurred during the activation process.
     */
    INTERNAL_FAILURE,
}

/**
 * Checks if the player has activated their secondary bank account.
 *
 * @param player The player to check the secondary bank account activation for.
 * @return True if the secondary bank account is activated, otherwise false.
 */
fun hasActivatedSecondaryBankAccount(player: Player): Boolean {
    return getAttribute(player, "UnlockedSecondaryBank", false)
}

/**
 * Checks if the player is using their secondary bank account.
 *
 * @param player The player to check the usage of their secondary bank account.
 * @return True if the player is using the secondary bank account, otherwise false.
 */
fun isUsingSecondaryBankAccount(player: Player): Boolean {
    return player.useSecondaryBank
}

/**
 * Opens the Grand Exchange interface for the player, with restrictions based on their Ironman mode.
 *
 * @param player The player who is attempting to access the Grand Exchange.
 */
fun openGrandExchange(player: Player) {
    restrictForIronman(player, IronmanMode.ULTIMATE) {
        StockMarket.openFor(player)
    }
}

private class PlayerInteractionAPI
