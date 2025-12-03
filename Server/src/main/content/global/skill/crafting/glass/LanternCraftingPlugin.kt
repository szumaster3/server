package content.global.skill.crafting.glass

import core.api.addItem
import core.api.addItemOrDrop
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Items

/**
 * Handles lantern crafting interactions.
 */
class LanternCraftingPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles interaction between a candle lantern and a candle (regular or black).
         * Crafts a lit or black candle lantern depending on the candle used.
         */

        onUseWith(IntType.ITEM, Items.CANDLE_LANTERN_4527, Items.CANDLE_36, Items.BLACK_CANDLE_38) { player, used, with ->
            craftCandleLantern(player, used.asItem(), with.asItem())
            return@onUseWith true
        }

        /*
         * Handles interaction between an oil lantern frame and an oil lamp.
         * Combines them into a functional oil lantern.
         */

        onUseWith(IntType.ITEM, Items.OIL_LANTERN_FRAME_4540, Items.OIL_LAMP_4525) { player, used, with ->
            craftOilLantern(player, used.asItem(), with.asItem())
            return@onUseWith true
        }

        /*
         * Handles interaction for Bullseye lanterns and sapphire lanterns with:
         * - Lantern lens
         * - Emerald lens
         * - Sapphire gem
         * Also handles swapping sapphire for lens.
         */

        onUseWith(
            IntType.ITEM,
            intArrayOf(Items.BULLSEYE_LANTERN_4544, Items.BULLSEYE_LANTERN_4548, Items.SAPPHIRE_LANTERN_4701),
            Items.LANTERN_LENS_4542, Items.EMERALD_LENS_9066, Items.SAPPHIRE_1607
        ) { player, used, with ->

            when {
                /*
                 * Placing a lens or gem onto a base bullseye lantern.
                 */
                used.id in arrayOf(Items.BULLSEYE_LANTERN_4544, Items.BULLSEYE_LANTERN_4548) -> {
                    val result = when (with.id) {
                        Items.LANTERN_LENS_4542 -> Items.BULLSEYE_LANTERN_4546
                        Items.SAPPHIRE_1607     -> Items.SAPPHIRE_LANTERN_4700
                        Items.EMERALD_LENS_9066 -> Items.EMERALD_LANTERN_9064
                        else -> return@onUseWith true
                    }
                    if (removeItem(player, used.id) && removeItem(player, with.id)) {
                        addItemOrDrop(player, result)
                        sendMessage(player, "You fashion the lens or gem onto the lantern.")
                    }
                }

                /*
                 * Swapping a sapphire for a lens on the lantern.
                 */
                used.id == Items.SAPPHIRE_LANTERN_4701 && with.id == Items.LANTERN_LENS_4542 -> {
                    if (removeItem(player, used.id) && removeItem(player, with.id)) {
                        addItemOrDrop(player, Items.BULLSEYE_LANTERN_4548)
                        addItemOrDrop(player, Items.SAPPHIRE_1607)
                        sendMessage(player, "You swap the lantern's sapphire for a lens.")
                    }
                }
            }

            return@onUseWith true
        }

        /*
         * Prevents combining a sapphire or lens with a lit bullseye lantern.
         */

        onUseWith(ITEM, Items.SAPPHIRE_1607, Items.BULLSEYE_LANTERN_4549, Items.BULLSEYE_LANTERN_4550, Items.LANTERN_LENS_4542) { player, _, _ ->
            sendMessage(player, "The lantern is too hot to do that while it is lit.")
            return@onUseWith true
        }
    }

    /*
     * Crafts a candle lantern by combining a lantern with a candle.
     * Produces either a lit or black candle lantern.
     */
    private fun craftCandleLantern(player: Player, used: Item, with: Item) {
        val result = when (with.id) {
            Items.CANDLE_36       -> Items.CANDLE_LANTERN_4529
            Items.BLACK_CANDLE_38 -> Items.CANDLE_LANTERN_4532
            else -> return
        }
        if (removeItem(player, used.id) && removeItem(player, with.id)) {
            addItem(player, result)
            sendMessage(player, "You place the unlit candle inside the lantern.")
        }
    }

    /*
     * Crafts an oil lantern by combining the frame with an oil lamp.
     */
    private fun craftOilLantern(player: Player, used: Item, with: Item) {
        if (with.id != Items.OIL_LAMP_4525) return
        if (removeItem(player, used.id) && removeItem(player, with.id)) {
            addItem(player, Items.OIL_LANTERN_4535)
            sendMessage(player, "You place the oil lamp inside its metal frame.")
        }
    }
}
