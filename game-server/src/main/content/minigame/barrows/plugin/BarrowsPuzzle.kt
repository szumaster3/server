package content.minigame.barrows.plugin

import core.api.sendMessage
import core.api.setAttribute
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository
import core.net.packet.out.DisplayModel
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Components
import java.util.*

class BarrowsPuzzle private constructor(
    private val questionModels: IntArray,
    private vararg val answerModels: Int,
) : ComponentPlugin() {
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

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.BARROWS_PUZZLE_25, this)
        return this
    }

    override fun handle(
        player: Player,
        component: Component,
        opcode: Int,
        button: Int,
        slot: Int,
        itemId: Int,
    ): Boolean {
        when (button) {
            2, 3, 5 -> {
                player.interfaceManager.close()
                val correct =
                    player.getAttribute(
                        "puzzle:answers",
                        IntArray(3),
                    )[if (button == 5) 2 else button - 2] shr 16 and 0xFF == 1
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

        fun open(player: Player) {
            var index = RandomFunction.random(4)
            if (index == player.getAttribute("puzzle:index", -1)) {
                index = (index + 1) % 4
            }
            open(player, index)
        }

        fun open(
            player: Player,
            index: Int,
        ) {
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
                    OutgoingContext.DisplayModel(
                        player,
                        OutgoingContext.DisplayModel.ModelType.MODEL,
                        puzzle.questionModels[i],
                        0,
                        25,
                        6 + i,
                    ),
                )
            }
            for (i in puzzle.answerModels.indices) {
                PacketRepository.send(
                    DisplayModel::class.java,
                    OutgoingContext.DisplayModel(
                        player,
                        OutgoingContext.DisplayModel.ModelType.MODEL,
                        puzzle.answerModels[i] and 0xFFFF,
                        0,
                        25,
                        2 + i,
                    ),
                )
            }
            PacketRepository.send(
                DisplayModel::class.java,
                OutgoingContext.DisplayModel(
                    player,
                    OutgoingContext.DisplayModel.ModelType.MODEL,
                    puzzle.answerModels[2] and 0xFFFF,
                    0,
                    25,
                    5,
                ),
            )
        }

        private fun getAnswerModel(
            modelId: Int,
            correct: Boolean,
        ): Int = modelId or ((if (correct) 1 else 0) shl 16)
    }
}
