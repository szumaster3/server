package content.minigame.barrows.plugin

import core.api.sendMessage
import core.api.setAttribute
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.net.packet.PacketRepository
import core.net.packet.context.DisplayModelContext
import core.net.packet.out.DisplayModel
import core.plugin.Plugin
import core.tools.RandomFunction
import shared.consts.Components
import java.util.*

/**
 * Represents a Barrows puzzle in the Barrows minigame.
 *
 * @property questionModels the model IDs for the puzzle questions
 * @property answerModels the model IDs for the puzzle answers, where the correct answer is encoded
 */
class BarrowsPuzzle private constructor(
    private val questionModels: IntArray,
    private vararg val answerModels: Int
) : ComponentPlugin() {

    /**
     * Creates a new instance of this puzzle with shuffled answer models.
     *
     * @return a new [BarrowsPuzzle] instance with answers shuffled
     */
    fun create(): BarrowsPuzzle {
        val answers = answerModels.copyOf(answerModels.size)
        val list: MutableList<Int> = ArrayList(20)
        for (answer in answers) {
            list.add(answer)
        }
        list.shuffle(Random())
        for (i in list.indices) {
            answers[i] = list[i]
        }
        return BarrowsPuzzle(questionModels, *answers)
    }

    /**
     * Registers this puzzle plugin with the component system.
     *
     * @param arg unused parameter
     * @return the plugin instance
     */
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.BARROWS_PUZZLE_25, this)
        return this
    }

    /**
     * Handles player interaction with the puzzle component.
     *
     * @param player the player interacting with the component
     * @param component the component instance
     * @param opcode the interaction opcode
     * @param button the button pressed
     * @param slot the component slot pressed
     * @param itemId the item id, if applicable
     * @return `true` if the action was handled, `false` otherwise
     */
    override fun handle(player: Player, component: Component, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        when (button) {
            2, 3, 5 -> {
                player.interfaceManager.close()
                val correct = player.getAttribute("puzzle:answers", IntArray(3))[if (button == 5) 2 else button - 2] shr 16 and 0xFF == 1
                if (!correct) {
                    sendMessage(player, "You got the puzzle wrong! You can hear the catacombs moving around you.")
                    BarrowsActivityPlugin.shuffleCatacombs(player)
                }
                setAttribute(player, "/save:barrow:solvedpuzzle", true)
                sendMessage(player, "You hear the doors' locking mechanism grind open.")
            }

            else -> return false
        }
        return true
    }

    companion object {
        /**
         * Predefined puzzle shape configurations.
         */
        val SHAPES =
            BarrowsPuzzle(
                intArrayOf(6734, 6735, 6736),
                getAnswerModel(6731, true),
                getAnswerModel(6732, false),
                getAnswerModel(6733, false),
            )
        private val LINES =
            BarrowsPuzzle(
                intArrayOf(6728, 6729, 6730),
                getAnswerModel(6725, true),
                getAnswerModel(6726, false),
                getAnswerModel(6727, false),
            )
        private val SQUARES =
            BarrowsPuzzle(
                intArrayOf(6722, 6723, 6724),
                getAnswerModel(6719, true),
                getAnswerModel(6720, false),
                getAnswerModel(6721, false),
            )
        private val TRIANGLE_CIRCLES =
            BarrowsPuzzle(
                intArrayOf(6716, 6717, 6718),
                getAnswerModel(6713, true),
                getAnswerModel(6714, false),
                getAnswerModel(6715, false),
            )
        private val COMPONENT = Component(25)

        /**
         * Opens a random Barrows puzzle for the player.
         * Ensures the new puzzle is different from the last one.
         *
         * @param player the player to open the puzzle for
         */
        fun open(player: Player) {
            var index = RandomFunction.random(4)
            if (index == player.getAttribute("puzzle:index", -1)) {
                index = (index + 1) % 4
            }
            open(player, index)
        }

        /**
         * Opens the specified Barrows puzzle for the player.
         *
         * @param player the player to open the puzzle for
         * @param index the index of the puzzle shape to open (0-3)
         */
        fun open(player: Player, index: Int) {
            var puzzle = SHAPES
            when (index) {
                1 -> puzzle = LINES
                2 -> puzzle = SQUARES
                3 -> puzzle = TRIANGLE_CIRCLES
            }
            puzzle = puzzle.create()
            setAttribute(player, "puzzle:index", index)
            setAttribute(player, "puzzle:answers", puzzle.answerModels)
            player.interfaceManager.open(COMPONENT)
            for (i in puzzle.questionModels.indices) {
                PacketRepository.send(
                    DisplayModel::class.java,
                    DisplayModelContext(player, DisplayModelContext.ModelType.MODEL, puzzle.questionModels[i], 0, 25, 6 + i),
                )
            }
            for (i in puzzle.answerModels.indices) {
                PacketRepository.send(
                    DisplayModel::class.java,
                    DisplayModelContext(player, DisplayModelContext.ModelType.MODEL, puzzle.answerModels[i] and 0xFFFF, 0, 25, 2 + i),
                )
            }
            PacketRepository.send(
                DisplayModel::class.java,
                DisplayModelContext(player, DisplayModelContext.ModelType.MODEL, puzzle.answerModels[2] and 0xFFFF, 0, 25, 5),
            )
        }

        /**
         * Encodes a puzzle answer model id with a correctness flag.
         *
         * @param modelId the model id
         * @param correct `true` if this is the correct answer, `false` otherwise
         * @return the encoded model integer
         */
        private fun getAnswerModel(modelId: Int, correct: Boolean): Int = modelId or ((if (correct) 1 else 0) shl 16)
    }
}