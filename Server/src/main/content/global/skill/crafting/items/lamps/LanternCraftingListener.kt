package content.global.skill.crafting.items.lamps

import core.api.*
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.Items
import core.game.node.item.Item

@Initializable
class LanternCraftingListener : InteractionListener {

    override fun defineListeners() {

        /*
         * Handle: Candle lantern (4527) with candle (36) or black candle (38).
         */

        onUseWith(IntType.ITEM, Items.CANDLE_LANTERN_4527, Items.CANDLE_36, Items.BLACK_CANDLE_38) { player, used, with ->
            craftCandleLantern(player, used.asItem(), with.asItem())
            return@onUseWith true
        }

        /*
         * Handle: Oil lantern frame (4540) with oil lamp (4525).
         */

        onUseWith(IntType.ITEM, Items.OIL_LANTERN_FRAME_4540, Items.OIL_LAMP_4525) { player, used, with ->
            craftOilLantern(player, used.asItem(), with.asItem())
            return@onUseWith true
        }

        /*
         * Handle: Bullseye lantern (4544 / 4548) with:
         * - Lantern lens (4542)
         * - Emerald lens (9066)
         * - Sapphire gem (1607)
         */

        onUseWith(
            IntType.ITEM,
            intArrayOf(Items.BULLSEYE_LANTERN_4544, Items.BULLSEYE_LANTERN_4548),
            Items.LANTERN_LENS_4542, Items.EMERALD_LENS_9066, Items.SAPPHIRE_1607
        ) { player, used, with ->
            craftBullseyeLantern(player, used.asItem(), with.asItem())
            return@onUseWith true
        }
    }

    private fun craftCandleLantern(player: Player, used: Item, with: Item) {
        val result = when (with.id) {
            Items.CANDLE_36       -> Items.CANDLE_LANTERN_4529
            Items.BLACK_CANDLE_38 -> Items.CANDLE_LANTERN_4532
            else -> return
        }
        if(removeItem(player, used.id) && removeItem(player, with.id)) {
            addItem(player, result)
            sendMessage(player, "You place the unlit candle inside the lantern.")
        }
    }

    private fun craftOilLantern(player: Player, used: Item, with: Item) {
        if (with.id != Items.OIL_LAMP_4525) return

        if(removeItem(player, used.id) && removeItem(player, with.id)) {
            addItem(player, Items.OIL_LANTERN_4535)
            sendMessage(player, "You place the oil lamp inside its metal frame.")
        }
    }

    private fun craftBullseyeLantern(player: Player, used: Item, with: Item) {
        val result = when (with.id) {
            Items.LANTERN_LENS_4542 -> Items.BULLSEYE_LANTERN_4546
            Items.SAPPHIRE_1607     -> Items.SAPPHIRE_LANTERN_4700
            Items.EMERALD_LENS_9066 -> Items.EMERALD_LANTERN_9064
            else -> return
        }

        if(removeItem(player, used.id) && removeItem(player, with.id)) {
            addItem(player, result)
            sendMessage(player, "You fashion the lens onto the lantern.")
        }
    }
}
