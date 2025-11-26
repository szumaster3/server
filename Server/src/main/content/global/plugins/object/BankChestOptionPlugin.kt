package content.global.plugins.`object`

import core.api.openBankAccount
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Scenery

private val BANK_CHESTS = intArrayOf(Scenery.BANK_CHEST_3194, Scenery.BANK_CHEST_4483, Scenery.BANK_CHEST_10562, Scenery.BANK_CHEST_14382, Scenery.BANK_CHEST_16695, Scenery.BANK_CHEST_16696, Scenery.BANK_CHEST_21301, Scenery.BANK_CHEST_27662, Scenery.BANK_CHEST_27663)

class BankChestOptionPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Bank chests scenery interaction.
         */

        defineInteraction(IntType.SCENERY, BANK_CHESTS, "bank", "use", "open") { player, _, _ ->
            openBankAccount(player)
            return@defineInteraction true
        }
    }
}
