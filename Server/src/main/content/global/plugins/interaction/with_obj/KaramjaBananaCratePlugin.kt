package content.global.plugins.interaction.with_obj

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery
import kotlin.math.min

class KaramjaBananaCratePlugin : InteractionListener {

    companion object {
        private const val CRATE_CAPACITY = 10
    }

    override fun defineListeners() {
        /*
         * Handles stash the karamjan rum into the crate.
         */

        onUseWith(IntType.SCENERY, Items.KARAMJAN_RUM_431, Scenery.CRATE_2072) { player, used, _ ->
            if (!player.savedData.globalData.isLuthasTask()) {
                sendMessage(player, "I don't know what goes in there.")
                return@onUseWith true
            }
            if (getAttribute(player, "stashed-rum", false)) {
                sendMessage(player, "There's already some rum in here...")
                return@onUseWith true
            }
            if (!removeItem(player, used)) {
                return@onUseWith false
            }
            lock(player, 2)
            animate(player, Animations.HUMAN_MULTI_USE_832)
            sendDialogue(player, "You stash the rum in the crate.")
            setAttribute(player, "/save:stashed-rum", true)
            return@onUseWith true
        }

        /*
         * Handles stash the bananas into the crate.
         */

        onUseWith(IntType.SCENERY, Items.BANANA_1963, Scenery.CRATE_2072) { player, used, _ ->
            if (!player.savedData.globalData.isLuthasTask()) {
                sendMessage(player, "I don't know what goes in there.")
                return@onUseWith true
            }
            val currNumBananasInCrate = player.savedData.globalData.karamjaBananas
            if (currNumBananasInCrate >= CRATE_CAPACITY) {
                sendMessage(player, "The crate is already full.")
                return@onUseWith true
            }
            if (!removeItem(player, used)) {
                return@onUseWith false
            }
            lock(player, 2)
            animate(player, Animations.HUMAN_MULTI_USE_832)
            sendDialogue(player, "You pack a banana into the crate.")
            player.savedData.globalData.setKaramjaBannanas(currNumBananasInCrate + 1)
            return@onUseWith true
        }

        /*
         * Handles search the crate interaction.
         */

        on(Scenery.CRATE_2072, IntType.SCENERY, "search") { player, _ ->
            if (!player.savedData.globalData.isLuthasTask()) {
                sendMessage(player, "I don't know what goes in there.")
                return@on true
            }
            val currNumBananasInCrate = player.savedData.globalData.karamjaBananas
            if (currNumBananasInCrate == 0) {
                sendMessage(player, "The crate is completely empty.")
                return@on true
            }
            if (currNumBananasInCrate >= CRATE_CAPACITY) {
                sendMessage(player, "The crate is full of bananas.")
                return@on true
            }
            sendMessage(
                player,
                "The crate has " + currNumBananasInCrate + " banana" + (if (currNumBananasInCrate > 1) "s" else "") +
                    " inside.",
            )
            return@on true
        }

        /*
         * Handles fill crate interaction.
         */

        on(Scenery.CRATE_2072, IntType.SCENERY, "fill") { player, _ ->
            if (!player.savedData.globalData.isLuthasTask()) {
                sendMessage(player, "I don't know what goes in there.")
                return@on true
            }
            val numBananas = amountInInventory(player, Items.BANANA_1963)
            val currNumBananasInCrate = player.savedData.globalData.karamjaBananas
            val bananasToBeAdded = min(numBananas, CRATE_CAPACITY - player.savedData.globalData.karamjaBananas)
            if (numBananas == 0) {
                return@on true
            }
            if (currNumBananasInCrate >= CRATE_CAPACITY) {
                sendMessage(player, "The crate is already full.")
                return@on true
            }
            if (!removeItem(player, Item(Items.BANANA_1963, bananasToBeAdded))) {
                return@on false
            }
            lock(player, 2)
            animate(player, Animations.HUMAN_MULTI_USE_832)
            sendMessage(player, "You pack all your bananas into the crate.")
            player.savedData.globalData.setKaramjaBannanas(
                player.savedData.globalData.karamjaBananas + bananasToBeAdded,
            )
            return@on true
        }
    }
}
