package content.global.handlers.item.withitem

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.tools.START_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class FruitCuttingListener : InteractionListener {
    internal enum class Fruits(
        val fruit: Int,
        val chunk: Int,
        val slice: Item,
    ) {
        PINEAPPLE(Items.PINEAPPLE_2114, Items.PINEAPPLE_CHUNKS_2116, Item(Items.PINEAPPLE_RING_2118, 4)),
        BANANA(Items.BANANA_1963, -1, Item(Items.SLICED_BANANA_3162)),
        LEMON(Items.LEMON_2102, Items.LEMON_CHUNKS_2104, Item(Items.LEMON_SLICES_2106)),
        LIME(Items.LIME_2120, Items.LIME_CHUNKS_2122, Item(Items.LIME_SLICES_2124)),
        ORANGE(Items.ORANGE_2108, Items.ORANGE_CHUNKS_2110, Item(Items.ORANGE_SLICES_2112)),
        ;

        companion object {
            val productOfChunk = values().associate { it.fruit to it.chunk }
            val productOfSlice = values().associate { it.fruit to it.slice }
            val cutable = intArrayOf(PINEAPPLE.fruit, BANANA.fruit, LEMON.fruit, LIME.fruit, ORANGE.fruit)

            fun forChunkId(id: Int): Int {
                return productOfChunk[id]!!
            }

            fun forSliceId(id: Int): Item {
                return productOfSlice[id]!!
            }
        }
    }

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Fruits.cutable, Items.KNIFE_946) { player, used, _ ->
            openDialogue(player, FruitCuttingDialogue(used.id, Fruits.forChunkId(used.id), Fruits.forSliceId(used.id)))
            return@onUseWith true
        }
    }

    inner class FruitCuttingDialogue(
        val fruit: Int,
        val chunk: Int,
        val slice: Item,
    ) : DialogueFile() {
        private val anim = Animation(Animations.HUMAN_FRUIT_CUTTING_1192)

        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            when (stage) {
                START_DIALOGUE -> {
                    if (chunk == -1) {
                        if (removeItem(player!!, fruit) && addItem(player!!, slice.id)) {
                            animate(player!!, anim, true)
                            playAudio(player!!, Sounds.BANANA_SLICING_1261)
                            sendMessage(player!!, "You deftly chop the ${getItemName(fruit).lowercase()} into slices.")
                        }
                    } else {
                        sendString(player!!, "Would you like to...", 140, 4)
                        sendItemOnInterface(player!!, 140, 6, chunk, 1)
                        sendItemOnInterface(player!!, 140, 5, slice.id, 1)
                        sendString(player!!, "Slice the ${getItemName(fruit).lowercase()}", 140, 2)
                        sendString(player!!, "Dice the ${getItemName(fruit).lowercase()}", 140, 3)
                        openInterface(player!!, 140)
                        when (buttonID) {
                            1 -> {
                                closeInterface(player!!)
                                if (removeItem(player!!, fruit) && addItem(player!!, slice.id, slice.amount)) {
                                    animate(player!!, anim, true)
                                    sendMessage(player!!, "You cut the ${getItemName(fruit).lowercase()} into slices.")
                                }
                            }

                            2 -> {
                                closeInterface(player!!)
                                if (removeItem(player!!, fruit) && addItem(player!!, chunk)) {
                                    animate(player!!, anim, true)
                                    sendMessage(player!!, "You cut the ${getItemName(fruit).lowercase()} into chunks.")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
