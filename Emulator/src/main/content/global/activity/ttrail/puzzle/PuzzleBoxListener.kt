package content.global.activity.ttrail.puzzle

import core.api.IfaceSettingsBuilder
import core.api.getAttribute
import core.api.sendMessage
import core.api.setAttribute
import core.game.component.Component
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Scenery
import kotlin.math.abs

class PuzzleBoxListener : InteractionListener, InterfaceListener {

    private val puzzleSessionState = mutableMapOf<Player, MutableMap<String, List<Int>>>()

    override fun defineListeners() {

        /*
         * Handles tree clue puzzle.
         */

        on(Items.PUZZLE_BOX_3571, IntType.ITEM, "Open") { player, _ ->
            handlePuzzleOpen(player, "tree", (3643..3666).toList() + -1)
            return@on true
        }

        /*
         * Handles troll clue puzzle.
         */

        on(Items.PUZZLE_BOX_2795, IntType.ITEM, "Open") { player, _ ->
            handlePuzzleOpen(player, "troll", (2749..2772).toList() + -1)
            return@on true
        }


        /*
         * Handles castle clue puzzle.
         */

        on(Items.PUZZLE_BOX_3565, IntType.ITEM, "Open") { player, _ ->
            handlePuzzleOpen(player, "castle", (3619..3642).toList() + -1)
            return@on true
        }

        /*
         * Handles monkey madness puzzle.
         */

        on(Items.SPARE_CONTROLS_4002, IntType.ITEM, "View") { player, _ ->
            val puzzlePieces: Array<Item?>? = ((3904..3950 step 2).toList().map { Item(it) } + Item(-1)).toTypedArray()
            val settings = IfaceSettingsBuilder().build()
            player.packetDispatch.sendIfaceSettings(settings, 6, Components.TRAIL_PUZZLE_363, 0, 25)
            player.interfaceManager.open(Component(Components.TRAIL_PUZZLE_363))
            PacketRepository.send(
                ContainerPacket::class.java, ContainerContext(player, -1, -1, 140, puzzlePieces, 25, false)
            )
            return@on true
        }

        on(Scenery.REINITIALISATION_PANEL_4871, IntType.SCENERY, "Operate") { player, _ ->
            if (!getAttribute(player, "mm:puzzle:done", false)) {
                val settings = IfaceSettingsBuilder().enableAllOptions().build()
                player.packetDispatch.sendIfaceSettings(settings, 6, Components.TRAIL_PUZZLE_363, 0, 25)
                player.interfaceManager.open(Component(Components.TRAIL_PUZZLE_363))

                val key = "mm"
                val items = (3904..3950 step 2).toList() + -1
                if (loadPuzzleState(player, key) == null) {
                    val shuffled = items.shuffled()
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
            val key = listOf("mm", "tree", "troll", "castle").find {
                loadPuzzleState(player, it) != null
            } ?: return@on true

            val puzzle = loadPuzzleState(player, key)?.toMutableList() ?: return@on true
            val solution = when (key) {
                "mm" -> (3904..3950 step 2).toList()
                "tree" -> (3643..3666).toList()
                "troll" -> (2749..2772).toList()
                "castle" -> (3619..3642).toList()
                else -> emptyList()
            } + -1

            if (buttonID == 6 && slot in 0..24) {
                val offsets = listOf(-1, 1, -5, 5)
                val targetSlot = offsets
                    .map { findTargetSlot(puzzle, slot, it) }
                    .firstOrNull { it != -1 }

                if (targetSlot != null) {
                    puzzle[targetSlot] = puzzle[slot]
                    puzzle[slot] = -1

                    savePuzzleStateInSession(player, key, puzzle)
                    sendPuzzle(player, key)

                    if (puzzle == solution) {
                        setAttribute(player, "$key:puzzle:done", true)
                        savePuzzleStateInAttributes(player, key, puzzle)
                    }
                }

            } else if (buttonID == 0) {
                val solutionItems = solution.map { Item(it) }.toTypedArray()

                if (!getAttribute(player, "$key:puzzle:done", false)) {
                    PacketRepository.send(
                        ContainerPacket::class.java,
                        ContainerContext(player, -1, -1, 140, solutionItems, 25, false),
                    )
                }
            }

            return@on true
        }
    }


    /**
     * Opens a puzzle box, shuffles puzzle pieces, and saves the puzzle state for the player.
     *
     * @param player The player interacting with the puzzle.
     * @param key The identifier for the puzzle (e.g., "tree", "troll").
     * @param items The list of items representing the puzzle pieces.
     */
    private fun handlePuzzleOpen(player: Player, key: String, items: List<Int>) {
        if (!getAttribute(player, "$key:puzzle:done", false)) {
            val settings = IfaceSettingsBuilder().enableAllOptions().build()
            player.packetDispatch.sendIfaceSettings(settings, 6, Components.TRAIL_PUZZLE_363, 0, 25)
            player.interfaceManager.open(Component(Components.TRAIL_PUZZLE_363))

            if (loadPuzzleState(player, key) == null) {
                val shuffled = items.shuffled()

                val puzzleWithEmptyInBottomRight = shuffled.toMutableList()
                val emptySlotIndex = puzzleWithEmptyInBottomRight.indexOf(-1)

                if (emptySlotIndex != 24) {
                    puzzleWithEmptyInBottomRight[emptySlotIndex] = puzzleWithEmptyInBottomRight[24]
                    puzzleWithEmptyInBottomRight[24] = -1
                }

                savePuzzleStateInSession(player, key, puzzleWithEmptyInBottomRight)
            }

            sendPuzzle(player, key)
        } else {
            sendPuzzle(player, key)
        }
    }

    /**
     * Finds a valid target slot to move a puzzle piece to.
     *
     * @param puzzle The current puzzle state.
     * @param slot The index of the tile being moved.
     * @param offset The offset to check for valid movement.
     * @return The target slot index, or -1 if no valid target was found.
     */
    private fun findTargetSlot(puzzle: List<Int>, slot: Int, offset: Int): Int {
        val target = slot + offset
        return if (target in 0..24 && (abs(slot - target) == 1 || abs(slot - target) == 5) && puzzle[target] == -1) {
            target
        } else -1
    }

    /**
     * Saves the current puzzle state for a player in their session.
     *
     * @param player The player whose puzzle state is being saved.
     * @param key The identifier for the puzzle.
     * @param puzzle The puzzle state (list of tile IDs).
     */
    private fun savePuzzleStateInSession(player: Player, key: String, puzzle: List<Int>) {
        puzzleSessionState[player]?.put(key, puzzle) ?: run {
            puzzleSessionState[player] = mutableMapOf(key to puzzle)
        }
    }

    /**
     * Loads the puzzle state for a specific player and puzzle identifier.
     *
     * @param player The player whose puzzle state is being loaded.
     * @param key The identifier for the puzzle.
     * @return The puzzle state as a list of integers, or null if no state is found.
     */
    private fun loadPuzzleState(player: Player, key: String): List<Int>? {
        return puzzleSessionState[player]?.get(key)
    }

    /**
     * Saves the puzzle state to the player's attributes after solving the puzzle.
     *
     * @param player The player whose puzzle state is being saved.
     * @param key The identifier for the puzzle.
     * @param puzzle The completed puzzle state.
     */
    private fun savePuzzleStateInAttributes(player: Player, key: String, puzzle: List<Int>) {
        val encoded = puzzle.joinToString(",")
        setAttribute(player, "/save:$key:puzzle", encoded.hashCode())
        setAttribute(player, "/save:$key:puzzle:data", encoded)
    }

    /**
     * Sends the current puzzle state to the player for display.
     *
     * @param player The player to send the puzzle state to.
     * @param key The identifier for the puzzle.
     * @param data The puzzle state data (optional).
     */
    private fun sendPuzzle(player: Player, key: String, data: List<Int>? = null) {
        val puzzle = data ?: loadPuzzleState(player, key) ?: return
        val items = puzzle.map { Item(it) }.toTypedArray()
        PacketRepository.send(
            ContainerPacket::class.java,
            ContainerContext(player, -1, -1, 140, items, 25, false),
        )
    }
}
