package content.global.activity.trails.plugin

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import kotlin.math.absoluteValue
import shared.consts.Components
import shared.consts.Items
import shared.consts.Scenery

/**
 * Handles puzzle box interactions.
 */
class PuzzleBoxPlugin : InteractionListener, InterfaceListener {

    /**
     * Represents a puzzle box configuration.
     */
    data class PuzzleBox(val type: String, val id: Int, val tiles: List<Int>) {
        val fullSolution: List<Int> = tiles + -1
    }

    /**
     * All available puzzle boxes.
     */
    private val puzzles = listOf(
        PuzzleBox("troll", Items.PUZZLE_BOX_3571, (Items.SLIDING_PIECE_3643..Items.SLIDING_PIECE_3666).toList()),
        PuzzleBox("castle", Items.PUZZLE_BOX_2795, (Items.SLIDING_PIECE_2749..Items.SLIDING_PIECE_2772).toList()),
        PuzzleBox("tree", Items.PUZZLE_BOX_3565, (Items.SLIDING_PIECE_3619..Items.SLIDING_PIECE_3642).toList()),
        PuzzleBox("glider", Items.SPARE_CONTROLS_4002, (Items.SLIDING_BUTTON_3904..Items.SLIDING_BUTTON_3950 step 2).toList())
    )

    /**
     * Active puzzle sessions per player.
     */
    private val sessionState = mutableMapOf<Player, Pair<String, MutableList<Int>>>()

    /**
     * Finds a puzzle by type.
     */
    fun forType(type: String) = puzzles.firstOrNull { it.type == type }

    /**
     * Finds a puzzle by item id
     */
    fun forId(itemId: Int) = puzzles.firstOrNull { it.id == itemId }

    /**
     * Checks if player has completed a puzzle.
     */
    fun isComplete(player: Player, type: String): Boolean {
        val box = forType(type) ?: return false
        return getAttribute(player, "$type:puzzle:done", false) && inInventory(player, box.id, 1)
    }

    override fun defineListeners() {

        /*
         * Handle opening of puzzle boxes.
         */

        puzzles.forEach { box ->
            on(box.id, IntType.ITEM, "open", "view") { player, _ ->
                openPuzzle(player, box.type)
                return@on true
            }
        }

        /*
         * Handle monkey madness glider panel hint.
         */

        on(Scenery.REINITIALISATION_PANEL_4871, IntType.SCENERY, "Operate") { player, _ ->
            if (getAttribute(player, "glider:puzzle:done", false)) {
                sendMessage(player, "You have already solved the puzzle.")
                return@on true
            }

            val box = forType("glider") ?: return@on true
            openPuzzleInterface(player)
            sendPuzzle(player, "glider", box.fullSolution.toMutableList())
            return@on true
        }
    }

    override fun defineInterfaceListeners() {

        /*
         * Handle puzzle interface button interactions.
         */

        on(Components.TRAIL_PUZZLE_363) { player, _, _, buttonID, slot, _ ->
            val (type, puzzle) = sessionState[player] ?: return@on true
            val solution = forType(type)?.fullSolution ?: return@on true

            when (buttonID) {
                6 -> {
                    if (clickTile(puzzle, slot)) {
                        sessionState[player] = type to puzzle
                        sendPuzzle(player, type)
                        if (puzzle == solution) sendMessage(player, "Congratulations! You've solved the puzzle!")
                    }
                }
                0 -> sendPuzzle(player, type, solution.toMutableList())
            }
            return@on true
        }
    }

    /**
     * Initialize puzzles.
     */
    private fun openPuzzle(player: Player, type: String) {
        val box = forType(type) ?: return
        openPuzzleInterface(player)
        val puzzle = generatePuzzle(box.fullSolution)
        sessionState[player] = type to puzzle
        sendPuzzle(player, type)
    }

    /**
     * Opens the puzzle interface.
     */
    private fun openPuzzleInterface(player: Player) {
        val settings = IfaceSettingsBuilder().enableAllOptions().build()
        sendIfaceSettings(player, settings, 6, Components.TRAIL_PUZZLE_363, 0, 25)
        openInterface(player, Components.TRAIL_PUZZLE_363)
    }

    /**
     * Handles clicking a tile.
     */
    private fun clickTile(puzzle: MutableList<Int>, slot: Int): Boolean {
        val emptyIndex = puzzle.indexOf(-1)
        val rowDiff = slot / 5 - emptyIndex / 5
        val colDiff = slot % 5 - emptyIndex % 5
        if ((rowDiff.absoluteValue + colDiff.absoluteValue) == 1) {
            puzzle[emptyIndex] = puzzle[slot]
            puzzle[slot] = -1
            return true
        }
        return false
    }

    /**
     * Sends puzzle state to the client.
     */
    private fun sendPuzzle(player: Player, type: String, data: List<Int>? = null) {
        val puzzle = data ?: sessionState[player]?.second ?: return
        PacketRepository.send(
            ContainerPacket::class.java,
            ContainerContext(
                player, -1, -1, 140,
                puzzle.map { if (it != -1) Item(it) else null }.toTypedArray(),
                25, false
            )
        )
    }

    /**
     * Generates a randomized puzzle from a solved state.
     */
    fun generatePuzzle(solution: List<Int>): MutableList<Int> {
        val puzzle = solution.filter { it != -1 }.toMutableList().apply { add(-1) }
        var emptyIndex = puzzle.lastIndex

        repeat(200) {
            val emptyRow = emptyIndex / 5
            val emptyCol = emptyIndex % 5
            var moved = false
            while (!moved) {
                val move = (0..3).random()
                val target = when (move) {
                    0 -> emptyIndex - 5
                    1 -> emptyIndex + 5
                    2 -> emptyIndex - 1
                    else -> emptyIndex + 1
                }
                val targetRow = target / 5
                val targetCol = target % 5
                if (target in 0..24 && (targetRow == emptyRow || targetCol == emptyCol)) {
                    puzzle[emptyIndex] = puzzle[target]
                    puzzle[target] = -1
                    emptyIndex = target
                    moved = true
                }
            }
        }

        // Fix a potential swap issue in first row
        val idx4 = puzzle.indexOf(solution[3])
        val idx5 = puzzle.indexOf(solution[4])
        if (idx4 / 5 == idx5 / 5 + 1 && idx4 % 5 == idx5 % 5) {
            puzzle[idx4] = puzzle[idx5]
            puzzle[idx5] = solution[4]
        }

        // Ensure last tile is empty
        if (puzzle.last() != -1) {
            val emptyIdx = puzzle.indexOf(-1)
            puzzle[emptyIdx] = puzzle.last().also { puzzle[puzzle.lastIndex] = -1 }
        }

        return puzzle
    }

    /**
     * Saves a player's puzzle session.
     */
    fun saveSession(player: Player, type: String, puzzle: MutableList<Int>) {
        sessionState[player] = type to puzzle
    }

    /**
     * Loads a player's puzzle session.
     */
    fun loadSession(player: Player, type: String): MutableList<Int>? {
        val (sessionType, puzzle) = sessionState[player] ?: return null
        return if (sessionType == type) puzzle else null
    }
}
