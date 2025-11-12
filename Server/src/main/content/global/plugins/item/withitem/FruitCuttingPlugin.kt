package content.global.plugins.item.withitem

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Items

class FruitCuttingPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles cutting fruits.
         */

        onUseWith(IntType.ITEM, Items.KNIFE_946, *fruitsIDs) { player, _, with ->
            val fruitName = getItemName(with.id).lowercase()
            val sliceID = if (with.id == Items.TENTI_PINEAPPLE_1851) Items.PINEAPPLE_RING_2118 else with.id + 4
            val chunkID = if (with.id == Items.TENTI_PINEAPPLE_1851) Items.PINEAPPLE_CHUNKS_2116 else with.id + 2

            sendString(player, "Would you like to...", Components.SELECT_AN_OPTION_140, 4)
            sendItemOnInterface(player, Components.SELECT_AN_OPTION_140, 6, chunkID, 1)
            sendItemOnInterface(player, Components.SELECT_AN_OPTION_140, 5, sliceID, 1)
            sendString(player, "Slice the $fruitName", Components.SELECT_AN_OPTION_140, 2)
            sendString(player, "Dice the $fruitName", Components.SELECT_AN_OPTION_140, 3)
            openInterface(player, Components.SELECT_AN_OPTION_140)

            addDialogueAction(player) { _, button ->
                val productID = if (button == 2) sliceID else chunkID
                val productAmount = if (button == 2 && with.id in intArrayOf(Items.PINEAPPLE_2114, Items.TENTI_PINEAPPLE_1851)) 4 else 1
                val productName = if (button == 2) "slices" else "chunks"
                val product = Item(productID, productAmount)

                sendSkillDialogue(player) {
                    withItems(productID)
                    create { _, amount ->
                        runTask(player, 1, amount) {
                            if (amount < 1) return@runTask
                            if (removeItem(player, Item(with.id))) {
                                player.animate(Animation(Animations.CRAFT_KNIFE_5244))

                                if (!hasSpaceFor(player, product)) {
                                    sendMessage(player, "You don't have enough inventory space to do this.")
                                    return@runTask
                                }

                                player.inventory.add(product)
                                sendMessage(player, "You cut the $fruitName into $productName.")
                            }
                        }
                    }
                    calculateMaxAmount { amountInInventory(player, with.id) }
                }
            }
            return@onUseWith true
        }

        /*
         * Handles cutting the watermelon.
         */

        onUseWith(IntType.ITEM, Items.KNIFE_946, Items.WATERMELON_5982) { player, _, with ->
            val productID = Item(Items.WATERMELON_SLICE_5984, 3)
            if (!inInventory(player, Items.WATERMELON_5982)) return@onUseWith true
            sendSkillDialogue(player) {
                withItems(with.id)
                create { _, amount ->
                    runTask(player, 1, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(with.id))) {

                            if (!hasSpaceFor(player, productID)) {
                                sendMessage(player, "You don't have enough inventory space to do this.")
                                return@runTask
                            }

                            player.animate(Animation(Animations.CUT_WATERMELON_2269))
                            player.inventory.add(productID)
                            sendMessage(player, "You slice the watermelon into three slices.")
                        }
                    }
                }
                calculateMaxAmount { amountInInventory(player, with.id) }
            }
            return@onUseWith true
        }

        /*
         * Handles cutting the red banana.
         */

        onUseWith(IntType.ITEM, Items.KNIFE_946, Items.RED_BANANA_7572) { player, _, with ->
            val productID = Item(Items.SLICED_RED_BANANA_7574, 1)
            if (!inInventory(player, Items.RED_BANANA_7572)) return@onUseWith true
            sendSkillDialogue(player) {
                withItems(with.id)
                create { _, amount ->
                    runTask(player, 1, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(with.id))) {

                            if (!hasSpaceFor(player, productID)) {
                                sendMessage(player, "You don't have enough inventory space to do this.")
                                return@runTask
                            }

                            player.animate(Animation(3495))
                            player.inventory.add(productID)
                            sendMessage(player, "You cut the red banana into slices.")
                        }
                    }
                }
                calculateMaxAmount { amountInInventory(player, with.id) }
            }
            return@onUseWith true
        }
    }

    companion object {
        private val fruitsIDs = intArrayOf(
            Items.PINEAPPLE_2114,
            Items.TENTI_PINEAPPLE_1851,
            Items.BANANA_1963,
            Items.LEMON_2102,
            Items.LIME_2120,
            Items.ORANGE_2108
        )
    }
}