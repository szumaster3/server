package content.activity.ttrail

import TestUtils
import content.global.activity.trails.plugin.PuzzleBoxPlugin
import core.api.addItem
import core.api.setAttribute
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PuzzleBoxPluginTests {
    private val plugin = PuzzleBoxPlugin()

    init {
        TestUtils.preTestSetup()
    }

    @Test fun generateOnlySolvablePuzzles() {
        val solution = (1..24).toList() + -1
        repeat(50) {
            val puzzle = plugin.generatePuzzle(solution)
            Assertions.assertEquals(25, puzzle.size, "Puzzle should have 25 pieces")
            Assertions.assertTrue(puzzle.contains(-1), "Puzzle must contain the empty slot (-1)")
        }
    }

    @Test fun saveAndLoadSession() {
        TestUtils.getMockPlayer("puzzle-save-load").use { p ->
            val type = "castle"
            val solution = plugin.forType(type)!!.fullSolution.toMutableList()
            val shuffled = plugin.generatePuzzle(solution)

            plugin.saveSession(p, type, shuffled)
            val loaded = plugin.loadSession(p, type)

            Assertions.assertEquals(shuffled, loaded, "Loaded puzzle should match saved session")
        }
    }

    @Test fun markPuzzleComplete() {
        TestUtils.getMockPlayer("puzzle-complete").use { p ->
            val type = "castle"
            val box = plugin.forType(type)!!
            val solution = box.fullSolution

            plugin.saveSession(p, type, solution.toMutableList())
            addItem(p, box.id)
            setAttribute(p, "$type:puzzle:done", true)

            Assertions.assertTrue(
                plugin.isComplete(p, type),
                "Puzzle should be marked as completed and player should have the item"
            )
        }
    }

    @Test fun randomPuzzleSelection() {
        TestUtils.getMockPlayer("puzzle-random").use { _ ->
            val allIds = listOf(
                plugin.forType("troll")!!.id,
                plugin.forType("castle")!!.id,
                plugin.forType("tree")!!.id,
                plugin.forType("glider")!!.id
            )
            val randomId = allIds.random()
            val box = plugin.forId(randomId)
            Assertions.assertNotNull(box, "Random puzzle ID must correspond to a Puzzle")
            Assertions.assertEquals(25, box!!.fullSolution.size, "Puzzle must have 25 tiles")
        }
    }
}
