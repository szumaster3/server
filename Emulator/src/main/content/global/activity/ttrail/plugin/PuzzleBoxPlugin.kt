package content.global.activity.ttrail.plugin

import core.api.*
import core.game.component.Component
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository
import core.net.packet.out.ContainerPacket
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Scenery
import kotlin.math.abs

/**
 * Represents the Treasure trails puzzle boxes.
 *
 * Relations
 * - Monkey Madness quest
 *
 * @author gabriel_aguido, szu
 */
class PuzzleBoxPlugin : InteractionListener, InterfaceListener {

    private val puzzleSessionState = mutableMapOf<Player, MutableMap<String, List<Int>>>()

    override fun defineListeners() {

        /*
         * Handles interaction with puzzle boxes.
         */

        PuzzleBox.values().forEach { box ->
            on(box.item.id, IntType.ITEM, "Open") { player, _ ->
                handlePuzzleOpen(player, box.key, box.solutionTiles + -1)
                return@on true
            }
        }

        /*
         * Handles quest puzzle (Monkey madness).
         */

        on(Items.SPARE_CONTROLS_4002, IntType.ITEM, "View") { player, _ ->
            val puzzlePieces: Array<Item> = ((Items.SLIDING_BUTTON_3904..Items.SLIDING_BUTTON_3950 step 2).map { Item(it) } + Item(-1)).toTypedArray()
            val settings = IfaceSettingsBuilder().build()
            player.packetDispatch.sendIfaceSettings(settings, 6, Components.TRAIL_PUZZLE_363, 0, 25)
            player.interfaceManager.open(Component(Components.TRAIL_PUZZLE_363))
            PacketRepository.send(
                ContainerPacket::class.java,
                OutgoingContext.Container(player, -1, -1, 140, puzzlePieces, 25, false)
            )
            return@on true
        }

        /*
         * Handles interaction with panel (shows solution) puzzle (Monkey madness).
         */

        on(Scenery.REINITIALISATION_PANEL_4871, IntType.SCENERY, "Operate") { player, _ ->
            if (!getAttribute(player, "mm:puzzle:done", false)) {
                val settings = IfaceSettingsBuilder().enableAllOptions().build()
                player.packetDispatch.sendIfaceSettings(settings, 6, Components.TRAIL_PUZZLE_363, 0, 25)
                player.interfaceManager.open(Component(Components.TRAIL_PUZZLE_363))

                val key = "mm"
                val items = (Items.SLIDING_BUTTON_3904..Items.SLIDING_BUTTON_3950 step 2).toList() + -1
                if (loadPuzzleState(player, key) == null) {
                    val shuffled = generateSolvablePuzzle(items)
                    savePuzzleStateInSession(player, key, shuffled)
                }

                sendPuzzle(player, key)
            } else {
                sendMessage(player, "You have already solved the puzzle.")
            }
            return@on true
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.TRAIL_PUZZLE_363) { player, _, _, buttonID, slot, _ ->
            val key = (PuzzleBox.values().map { it.key } + "mm").find {
                loadPuzzleState(player, it) != null
            } ?: return@on true

            val puzzle = loadPuzzleState(player, key)?.toMutableList() ?: return@on true
            val solution = PuzzleBox.fromKey(key)?.fullSolution
                ?: if (key == "mm") (Items.SLIDING_BUTTON_3904..Items.SLIDING_BUTTON_3950 step 2).toList() + -1 else return@on true

            if (buttonID == 6 && slot in 0..24) {
                val offsets = listOf(-1, 1, -5, 5)
                val targetSlot = offsets.map { findTargetSlot(puzzle, slot, it) }.firstOrNull { it != -1 }

                if (targetSlot != null) {
                    puzzle[targetSlot] = puzzle[slot]
                    puzzle[slot] = -1

                    savePuzzleStateInSession(player, key, puzzle)
                    sendPuzzle(player, key)

                    if (puzzle == solution) {
                        setAttribute(player, "/save:$key:puzzle:done", true)
                        savePuzzleStateInAttributes(player, key, puzzle)
                        player.debug("[$key] Puzzle completed.")

                        PuzzleBox.fromKey(key)?.let { box ->
                            val item = box.item
                            setCharge(item, 1100)
                        }
                    }
                }
            } else if (buttonID == 0) {
                val solutionItems = solution.map { Item(it) }.toTypedArray()

                if (!getAttribute(player, "$key:puzzle:done", false)) {
                    PacketRepository.send(
                        ContainerPacket::class.java,
                        OutgoingContext.Container(player, -1, -1, 140, solutionItems, 25, false),
                    )
                }
            }

            return@on true
        }
    }

    /**
     * Opens and initializes the puzzle if not already completed or in progress.
     */
    private fun handlePuzzleOpen(player: Player, key: String, items: List<Int>) {
        val box = PuzzleBox.fromKey(key) ?: return
        val charge = getCharge(box.item) ?: 1000

        val puzzleData = if (charge >= 1100) {
            box.fullSolution
        } else {
            loadPuzzleState(player, key) ?: run {
                val shuffled = generateSolvablePuzzle(items)
                savePuzzleStateInSession(player, key, shuffled)
                shuffled
            }
        }

        val settings = IfaceSettingsBuilder().enableAllOptions().build()
        player.packetDispatch.sendIfaceSettings(settings, 6, Components.TRAIL_PUZZLE_363, 0, 25)
        player.interfaceManager.open(Component(Components.TRAIL_PUZZLE_363))
        sendPuzzle(player, key, puzzleData)
    }

    /**
     * Checks if the given move offset results in a valid adjacent empty slot.
     */
    private fun findTargetSlot(puzzle: List<Int>, slot: Int, offset: Int): Int {
        val target = slot + offset
        return if (target in 0..24 && (abs(slot - target) == 1 || abs(slot - target) == 5) && puzzle[target] == -1) {
            target
        } else -1
    }

    /**
     * Saves the current puzzle state in temporary session memory.
     */
    fun savePuzzleStateInSession(player: Player, key: String, puzzle: List<Int>) {
        puzzleSessionState[player]?.put(key, puzzle) ?: run {
            puzzleSessionState[player] = mutableMapOf(key to puzzle)
        }
    }

    /**
     * Loads the current puzzle state from temporary session memory.
     */
    fun loadPuzzleState(player: Player, key: String): List<Int>? {
        return puzzleSessionState[player]?.get(key)
    }

    /**
     * Saves the completed puzzle state in persistent player attributes.
     */
    private fun savePuzzleStateInAttributes(player: Player, key: String, puzzle: List<Int>) {
        val encoded = puzzle.joinToString(",")
        setAttribute(player, "$key:puzzle", encoded.hashCode())
        setAttribute(player, "$key:puzzle:data", encoded)
    }

    /**
     * Sends the current puzzle tile layout to the client interface.
     */
    private fun sendPuzzle(player: Player, key: String, data: List<Int>? = null) {
        val puzzle = data ?: loadPuzzleState(player, key) ?: return
        val items = puzzle.map { Item(it) }.toTypedArray()
        PacketRepository.send(
            ContainerPacket::class.java,
            OutgoingContext.Container(player, -1, -1, 140, items, 25, false),
        )
    }

    /**
     * Checks whether a given puzzle tile configuration is solvable.
     *
     * @return true if the tile configuration is solvable in a 5x5 grid.
     */
    fun isSolvable(puzzle: List<Int>): Boolean {
        val flat = puzzle.filter { it != -1 }
        val inversions = flat.indices.sumOf { i ->
            (i + 1 until flat.size).count { j -> flat[i] > flat[j] }
        }
        return inversions % 2 == 0
    }

    /**
     * Generates a shuffled puzzle tile list that is guaranteed to be solvable.
     *
     * @return a shuffled and solvable list of puzzle piece IDs.
     */
    fun generateSolvablePuzzle(items: List<Int>): List<Int> {
        while (true) {
            val shuffled = items.shuffled()
            if (isSolvable(shuffled)) {
                return shuffled
            }
        }
    }
}
