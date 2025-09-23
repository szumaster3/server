package content.activity.ttrial.puzzle

import TestUtils
import core.api.getAttribute
import core.api.setAttribute
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import content.global.activity.ttrail.plugin.PuzzleBoxPlugin

class PuzzleBoxTests {
    private val p = TestUtils.getMockPlayer("puzzleSessionTest")
    private val plugin = PuzzleBoxPlugin()

    init {
        TestUtils.preTestSetup()
    }

    @Test fun generateOnlySolvablePuzzles() {
        val puzzleItems = (1..24).toList() + -1
        repeat(100) {
            val generatedPuzzle = plugin.generateSolvablePuzzle(puzzleItems)
            Assertions.assertTrue(plugin.isSolvable(generatedPuzzle), "Generated puzzle is not solvable: $generatedPuzzle")
            Assertions.assertTrue(generatedPuzzle.contains(-1), "Puzzle should contain empty slot (-1)")
            Assertions.assertEquals(25, generatedPuzzle.size, "Puzzle should have 25 pieces")
        }
    }

    @Test fun verifyKnownStateIsSolvable() {
        val solvablePuzzle = (1..24).toList() + -1
        Assertions.assertTrue(plugin.isSolvable(solvablePuzzle), "Puzzle should be solvable")
    }

    @Test fun generateUnsolvablePuzzles() {
        val unsolvablePuzzle = (2..24).toList() + 1 + -1
        Assertions.assertFalse(plugin.isSolvable(unsolvablePuzzle), "Puzzle should not be solvable")
    }

    @Test fun checkPuzzleStateSaving() {
        val key = "mm"
        val puzzleItems = (3904..3950 step 2).toList() + -1

        val shuffledPuzzle = plugin.generateSolvablePuzzle(puzzleItems)
        plugin.saveSession(p, key, shuffledPuzzle)

        val loadedPuzzle = plugin.loadSession(p, key)
        Assertions.assertEquals(shuffledPuzzle, loadedPuzzle, "Puzzle state should match the saved state")
    }

    @Test fun completePuzzleAndMarkAsDone() {
        val p = p
        val key = "mm"
        val puzzleItems = (3904..3950 step 2).toList() + -1
        val solution = (3904..3950 step 2).toList() + listOf(-1)

        val shuffledPuzzle = plugin.generateSolvablePuzzle(puzzleItems)
        plugin.saveSession(p, key, shuffledPuzzle)

        plugin.savePuzzleData(p, key, solution)

        setAttribute(p, "$key:puzzle:done", true)

        Assertions.assertTrue(getAttribute(p, "$key:puzzle:done", false), "Puzzle should be marked as completed")
        Assertions.assertEquals(solution.joinToString(",").hashCode(), getAttribute(p, "$key:puzzle", 0))
        Assertions.assertEquals(solution.joinToString(","), getAttribute(p, "$key:puzzle:data", ""))
    }
}
