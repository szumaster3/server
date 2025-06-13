package content.global.plugin.item.withitem

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Components
import org.rs.consts.Items
import kotlin.math.min

class CutFruitListener : InteractionListener {

    private val fruitID = intArrayOf(
        Items.PINEAPPLE_2114,
        Items.TENTI_PINEAPPLE_1851,
        Items.BANANA_1963,
        Items.LEMON_2102,
        Items.LIME_2120,
        Items.ORANGE_2108
    )

    override fun defineListeners() {
        onUseWith(IntType.ITEM, fruitID, Items.KNIFE_946) { player, used, _ ->
            val fruitName = getItemName(used.id).lowercase()
            val sliceID = if (used.id == Items.TENTI_PINEAPPLE_1851) Items.PINEAPPLE_RING_2118 else used.id + 4
            val chunkID = if (used.id == Items.TENTI_PINEAPPLE_1851) Items.PINEAPPLE_CHUNKS_2116 else used.id + 2

            sendString(player, "Would you like to...", Components.SELECT_AN_OPTION_140, 4)
            sendItemOnInterface(player, Components.SELECT_AN_OPTION_140, 6, chunkID, 1)
            sendItemOnInterface(player, Components.SELECT_AN_OPTION_140, 5, sliceID, 1)
            sendString(player, "Slice the $fruitName", Components.SELECT_AN_OPTION_140, 2)
            sendString(player, "Dice the $fruitName", Components.SELECT_AN_OPTION_140, 3)
            openInterface(player, Components.SELECT_AN_OPTION_140)

            addDialogueAction(player) { _, button ->
                sendSkillDialogue(player) {
                    val productID = if (button == 2) sliceID else chunkID
                    val productAmount = if (button == 2 && used.id in intArrayOf(Items.PINEAPPLE_2114, Items.TENTI_PINEAPPLE_1851)) 4 else 1
                    val productName = if (button == 2) "slices" else "chunks"

                    withItems(productID)

                    create { _, amount ->
                        runTask(player, 1, amount) {
                            if (amount < 1) return@runTask
                            if (removeItem(player, Item(used.id))) {
                                addItem(player, productID, productAmount, Container.INVENTORY)
                                sendMessage(player, "You cut the $fruitName into $productName.")
                            }
                        }
                    }

                    calculateMaxAmount {
                        val inv = amountInInventory(player, used.id)
                        min(inv, inv)
                    }
                }
            }
            return@onUseWith true
        }
    }
}