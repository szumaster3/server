package content.global.plugin.iface.bank

import core.api.*
import core.api.openBankAccount
import core.api.openGrandExchangeCollectionBox
import core.api.restrictForIronman
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.link.IronmanMode
import core.game.node.item.Item
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

class OuraniaBankPlugin : InteractionListener, InterfaceListener {

    override fun defineListeners() {

        /*
         * Handles interaction with zmi banker.
         */

        on(NPCs.ENIOLA_6362, IntType.NPC, "bank", "collect") { player, _ ->
            val option = getUsedOption(player)
            restrictForIronman(player, IronmanMode.ULTIMATE) {
                when (option) {
                    "bank" -> setAttribute(player, "zmi:bankaction", "open")
                    "collect" -> setAttribute(player, "zmi:bankaction", "collect")
                    else -> return@restrictForIronman
                }
                openInterface(player, Components.BANK_CHARGE_ZMI_619)
            }
            return@on true
        }
    }

    override fun defineInterfaceListeners() {

        /*
         * Handles rune payment when accessing the ZMI bank interface.
         */

        on(Components.BANK_CHARGE_ZMI_619) { player, _, _, buttonID, _, _ ->
            val runeItemId = BUTTON_TO_RUNE[buttonID] ?: return@on true

            if (!removeItem(player, Item(runeItemId, 20))) {
                sendNPCDialogue(player, NPCs.ENIOLA_6362, "You need at least 20 of that rune type to access your bank.", FaceAnim.NEUTRAL)
                return@on true
            }

            when (getAttribute(player, "zmi:bankaction", "")) {
                "open" -> openBankAccount(player)
                "collect" -> openGrandExchangeCollectionBox(player)
            }
            return@on true
        }
    }

    companion object {
        private val BUTTON_TO_RUNE = mapOf(
            28 to Items.AIR_RUNE_556,
            29 to Items.MIND_RUNE_558,
            30 to Items.WATER_RUNE_555,
            31 to Items.EARTH_RUNE_557,
            32 to Items.FIRE_RUNE_554,
            33 to Items.BODY_RUNE_559,
            34 to Items.COSMIC_RUNE_564,
            35 to Items.CHAOS_RUNE_562,
            36 to Items.ASTRAL_RUNE_9075,
            37 to Items.LAW_RUNE_563,
            38 to Items.DEATH_RUNE_560,
            39 to Items.BLOOD_RUNE_565,
            40 to Items.NATURE_RUNE_561,
            41 to Items.SOUL_RUNE_566,
        )
    }
}