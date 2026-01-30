package content.global.skill.cooking

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.event.ResourceProducedEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items

class DoughMakingPlugin : InteractionListener {

    companion object {
        private val waterContainerMap = hashMapOf(
            Items.BUCKET_OF_WATER_1929 to Items.BUCKET_1925,
            Items.BOWL_OF_WATER_1921 to Items.BOWL_1923,
            Items.JUG_OF_WATER_1937 to Items.JUG_1935
        )
    }

    override fun defineListeners() {
        onUseWith(IntType.ITEM, waterContainerMap.keys.toIntArray(), Items.POT_OF_FLOUR_1933) { player, used, with ->

            val tutorialStage = getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)

            if (tutorialStage in 18..19) {
                val bucket = used.asItem().id
                val pot = with.asItem().id

                if (player.inventory.containsItems(Item(bucket), Item(pot))) {
                    removeItem(player, Item(bucket))
                    removeItem(player, Item(pot))

                    addItem(player, Items.BREAD_DOUGH_2307)
                    player.dispatch(ResourceProducedEvent(Items.BREAD_DOUGH_2307, 1, player))
                    val emptyWaterContainerId = waterContainerMap[bucket]!!
                    addItem(player, emptyWaterContainerId)
                    addItem(player, Items.EMPTY_POT_1931)
                    sendMessage(player, "You mix the flour and the water to make some bread.")
                } else {
                    sendMessage(player, "You need a bucket of water and a pot of flour to make bread dough.")
                }
                return@onUseWith true
            }
            openDialogue(player, DoughMakeDialogue(used.asItem(), with.asItem()))
            return@onUseWith true
        }
    }

    private class DoughMakeDialogue(
        private val waterContainer: Item,
        private val flourContainer: Item
    ) : DialogueFile() {

        companion object {
            private const val STAGE_PRESENT_OPTIONS = 0
            private const val STAGE_PROCESS_OPTION = 1

            private val PRODUCTS = listOf(
                intArrayOf(Items.BREAD_DOUGH_2307, 1),
                intArrayOf(Items.PASTRY_DOUGH_1953, 1),
                intArrayOf(Items.PIZZA_BASE_2283, 35),
                intArrayOf(Items.PITTA_DOUGH_1863, 58),
            )
        }

        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                STAGE_PRESENT_OPTIONS -> {
                    sendOptions(
                        player!!,
                        "What do you wish to make?",
                        *PRODUCTS.map { getItemName(it[0]) }.toTypedArray()
                    )
                    stage++
                }

                STAGE_PROCESS_OPTION -> runTask(player!!, 1) {
                    end()

                    val (itemId, level) = PRODUCTS[buttonID - 1]
                    val name = getItemName(itemId)

                    if (!hasLevelDyn(player!!, Skills.COOKING, level)) {
                        sendDialogue(player!!, "You need a Cooking level of at least $level to make ${name.lowercase()}.")
                        return@runTask
                    }

                    if (freeSlots(player!!) < 1) {
                        sendMessage(player!!, "Not enough space in your inventory.")
                        return@runTask
                    }

                    if (removeItem(player!!, waterContainer) && removeItem(player!!, flourContainer)) {
                        addItem(player!!, itemId)
                        player!!.dispatch(ResourceProducedEvent(itemId, 1, player!!))

                        val emptyWaterContainerId = waterContainerMap[waterContainer.id]!!
                        addItem(player!!, emptyWaterContainerId)
                        addItem(player!!, Items.EMPTY_POT_1931)

                        sendMessage(player!!, "You mix the flour and the water to make some ${name.lowercase()}.")
                    }
                }
            }
        }
    }
}
