package content.global.skill.cooking.handlers

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.event.ResourceProducedEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items

class DoughListener : InteractionListener {
    companion object {
        private val waterContainerMap =
            hashMapOf(
                Items.BUCKET_OF_WATER_1929 to Items.BUCKET_1925,
                Items.BOWL_OF_WATER_1921 to Items.BOWL_1923,
                Items.JUG_OF_WATER_1937 to Items.JUG_1935,
            )
    }

    override fun defineListeners() {
        onUseWith(
            IntType.ITEM,
            waterContainerMap.keys.toIntArray(),
            Items.POT_OF_FLOUR_1933,
        ) { player, used, with ->
            openDialogue(player, DoughMakeDialogue(used.asItem(), with.asItem()))
            return@onUseWith true
        }
    }

    private class DoughMakeDialogue(
        private val waterContainer: Item,
        private val flourContainer: Item,
    ) : DialogueFile() {
        companion object {
            private const val STAGE_PRESENT_OPTIONS = 0
            private const val STAGE_PROCESS_OPTION = 1

            private enum class DoughProduct(
                val itemId: Int,
                val itemName: String,
                val requirements: Int,
            ) {
                BREAD_DOUGH(Items.BREAD_DOUGH_2307, getItemName(Items.BREAD_DOUGH_2307), 1),
                PASTRY_DOUGH(Items.PASTRY_DOUGH_1953, getItemName(Items.PASTRY_DOUGH_1953), 1),
                PIZZA_DOUGH(Items.PIZZA_BASE_2283, getItemName(Items.PIZZA_BASE_2283), 35),
                PITTA_DOUGH(Items.PITTA_DOUGH_1863, getItemName(Items.PITTA_DOUGH_1863), 58),
            }
        }

        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            when (stage) {
                STAGE_PRESENT_OPTIONS -> {
                    player!!.dialogueInterpreter.sendOptions(
                        "What do you wish to make?",
                        *DoughProduct.values().map { it.itemName }.toTypedArray(),
                    )
                    stage++
                }

                STAGE_PROCESS_OPTION ->
                    runTask(player!!, 1) {
                        end()
                        val product = DoughProduct.values()[buttonID - 1]

                        if (hasLevelDyn(player!!, Skills.COOKING, product.requirements)) {
                            if (freeSlots(player!!) < 1) {
                                sendMessage(player!!, "Not enough space in your inventory.")
                                return@runTask
                            }
                            if (removeItem(player!!, waterContainer) && removeItem(player!!, flourContainer)) {
                                addItem(player!!, product.itemId)
                                player!!.dispatch(ResourceProducedEvent(product.itemId, 1, player!!))
                                val emptyWaterContainerId = waterContainerMap[waterContainer.id]!!
                                addItem(player!!, emptyWaterContainerId)
                                addItem(player!!, Items.EMPTY_POT_1931)
                                sendMessage(
                                    player!!,
                                    "You mix the flour and the water to make some ${product.itemName.lowercase()}.",
                                )
                            }
                        } else {
                            sendDialogue(
                                player!!,
                                "You need a Cooking level of at least ${product.requirements} to make ${product.itemName.lowercase()}.",
                            )
                        }
                    }
            }
        }
    }
}
