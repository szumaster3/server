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
import shared.consts.Components
import shared.consts.Items
import shared.consts.Scenery
import kotlin.math.abs

/**
 * Plugin handling interactions and interface logic for Treasure Trails puzzle boxes.
 *
 * Supports puzzle management including loading, saving,
 * verifying completion, and generating solvable puzzles.
 *
 * Related to Monkey Madness quest puzzles.
 *
 * @author gabriel_aguido, szu
 */
class PuzzleBoxPlugin : InteractionListener, InterfaceListener {

    private val sessionState = mutableMapOf<Player, MutableMap<String, List<Int>>>()

    override fun defineListeners() {

        /*
         * Handles interaction with puzzle boxes.
         */

        PuzzleBox.values().forEach { box ->
            on(box.item.id, IntType.ITEM, "Open") { player, _ ->
                openPuzzle(player, box.key, box.fullSolution)
                return@on true
            }
        }


        /*
         * Handles quest puzzle (Monkey madness).
         */

        on(Items.SPARE_CONTROLS_4002, IntType.ITEM, "View") { player, _ ->
            openStaticPuzzle(player, (Items.SLIDING_BUTTON_3904..Items.SLIDING_BUTTON_3950 step 2).plus(-1))
            return@on true
        }


        /*
         * Handles interaction with panel (shows solution) puzzle (Monkey madness).
         */

        on(Scenery.REINITIALISATION_PANEL_4871, IntType.SCENERY, "Operate") { player, _ ->
            val key = "mm"
            if (player.getAttribute("$key:puzzle:done", false)) {
                player.sendMessage("You have already solved the puzzle.")
                return@on true
            }

            if (loadSession(player, key) == null) {
                val puzzle = generateSolvablePuzzle((Items.SLIDING_BUTTON_3904..Items.SLIDING_BUTTON_3950 step 2).plus(-1))
                saveSession(player, key, puzzle)
            }

            openPuzzleInterface(player)
            sendPuzzle(player, key)
            return@on true
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.TRAIL_PUZZLE_363) { player, _, _, buttonID, slot, _ ->
            val key = (PuzzleBox.values().map { it.key } + "mm").firstOrNull {
                loadSession(player, it) != null
            } ?: return@on true

            val puzzle = loadSession(player, key)?.toMutableList() ?: return@on true
            val solution = PuzzleBox.fromKey(key)?.fullSolution
                ?: if (key == "mm") (Items.SLIDING_BUTTON_3904..Items.SLIDING_BUTTON_3950 step 2).plus(-1)
                else return@on true

            when (buttonID) {
                6 -> moveTile(puzzle, slot)?.let {
                    saveSession(player, key, puzzle)
                    sendPuzzle(player, key)
                    if (puzzle == solution) {
                        player.setAttribute("$key:puzzle:done", true)
                        savePuzzleData(player, key, puzzle)
                        PuzzleBox.fromKey(key)?.let { setCharge(it.item, 1100) }
                    }
                }

                0 -> {
                    if (!player.getAttribute("$key:puzzle:done", false)) {
                        val solutionItems = solution.map { Item(it) }.toTypedArray()
                        sendPuzzleContainer(player, solutionItems)
                    }
                }
            }
            return@on true
        }
    }

    private fun openPuzzle(player: Player, key: String, solution: List<Int>) {
        val box = PuzzleBox.fromKey(key) ?: return
        val charge = getCharge(box.item) ?: 1000

        val puzzle = if (charge >= 1100) {
            solution
        } else {
            loadSession(player, key) ?: generateSolvablePuzzle(solution).also {
                saveSession(player, key, it)
            }
        }

        openPuzzleInterface(player)
        sendPuzzle(player, key, puzzle)
    }

    private fun moveTile(puzzle: MutableList<Int>, slot: Int): Boolean {
        val offsets = listOf(-1, 1, -5, 5)
        for (offset in offsets) {
            val target = slot + offset
            if (target !in 0..24) continue
            if ((offset == 1 || offset == -1) && slot / 5 != target / 5) continue
            if (puzzle[target] == -1) {
                puzzle[target] = puzzle[slot]
                puzzle[slot] = -1
                return true
            }
        }
        return false
    }

    private fun openPuzzleInterface(player: Player) {
        val settings = IfaceSettingsBuilder().enableAllOptions().build()
        player.packetDispatch.sendIfaceSettings(settings, 6, Components.TRAIL_PUZZLE_363, 0, 25)
        player.interfaceManager.open(Component(Components.TRAIL_PUZZLE_363))
    }

    private fun openStaticPuzzle(player: Player, items: List<Int>) {
        val settings = IfaceSettingsBuilder().build()
        player.packetDispatch.sendIfaceSettings(settings, 6, Components.TRAIL_PUZZLE_363, 0, 25)
        player.interfaceManager.open(Component(Components.TRAIL_PUZZLE_363))
        sendPuzzleContainer(player, items.map { Item(it) }.toTypedArray())
    }

    private fun sendPuzzle(player: Player, key: String, data: List<Int>? = null) {
        val puzzle = data ?: loadSession(player, key) ?: return
        sendPuzzleContainer(player, puzzle.map { Item(it) }.toTypedArray())
    }

    private fun sendPuzzleContainer(player: Player, items: Array<Item>) {
        PacketRepository.send(
            ContainerPacket::class.java,
            OutgoingContext.Container(player, -1, -1, 140, items, 25, false)
        )
    }

    fun savePuzzleData(player: Player, key: String, puzzle: List<Int>) {
        val data = puzzle.joinToString(",")
        setAttribute(player, "$key:puzzle", data.hashCode())
        setAttribute(player, "$key:puzzle:data", data)
    }

    fun isSolvable(puzzle: List<Int>): Boolean {
        val flat = puzzle.filter { it != -1 }
        val inversions = flat.indices.sumOf { i ->
            (i + 1 until flat.size).count { j -> flat[i] > flat[j] }
        }
        return inversions % 2 == 0
    }

    fun generateSolvablePuzzle(items: List<Int>): List<Int> =
        generateSequence { items.shuffled() }.first { isSolvable(it) }

    fun saveSession(player: Player, key: String, puzzle: List<Int>) {
        sessionState.getOrPut(player) { mutableMapOf() }[key] = puzzle
    }

    fun loadSession(player: Player, key: String): List<Int>? = sessionState[player]?.get(key)
}
