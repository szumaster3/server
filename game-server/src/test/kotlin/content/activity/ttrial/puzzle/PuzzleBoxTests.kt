package content.activity.ttrial.puzzle

import MockPlayer
import TestUtils
import content.global.activity.ttrail.plugin.PuzzleBoxPlugin
import core.api.getAttribute
import core.api.setAttribute
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PuzzleBoxTests {
    var mockPlayer: MockPlayer

    init {
        TestUtils.preTestSetup()
        mockPlayer = TestUtils.getMockPlayer("test")
    }

    @Test
    fun generateOnlySolvablePuzzles() {
        val puzzleListener = PuzzleBoxPlugin()
        val puzzleItems = (1..24).toList() + -1

        repeat(100) {
            val generatedPuzzle = puzzleListener.generateSolvablePuzzle(puzzleItems)
            Assertions.assertTrue(puzzleListener.isSolvable(generatedPuzzle), "Generated puzzle is not solvable: $generatedPuzzle")
            Assertions.assertTrue(generatedPuzzle.contains(-1), "Puzzle should contain empty slot (-1)")
            Assertions.assertEquals(25, generatedPuzzle.size, "Puzzle should have 25 pieces")
        }
    }

    @Test
    fun verifyKnownStateIsSolvable() {
        val puzzleListener = PuzzleBoxPlugin()
        val solvablePuzzle = (1..24).toList() + -1
        Assertions.assertTrue(puzzleListener.isSolvable(solvablePuzzle), "Puzzle should be solvable")
    }

    @Test
    fun generateUnsolvablePuzzles() {
        val puzzleListener = PuzzleBoxPlugin()
        val unsolvablePuzzle = (2..24).toList() + 1 + -1
        Assertions.assertFalse(puzzleListener.isSolvable(unsolvablePuzzle), "Puzzle should not be solvable")
    }

    @Test
    fun checkPuzzleStateSaving() {
        val puzzleListener = PuzzleBoxPlugin()
        val player = mockPlayer
        val key = "mm"
        val puzzleItems = (3904..3950 step 2).toList() + -1

        val shuffledPuzzle = puzzleListener.generateSolvablePuzzle(puzzleItems)
        puzzleListener.savePuzzleStateInSession(player, key, shuffledPuzzle)

        val loadedPuzzle = puzzleListener.loadPuzzleState(player, key)
        Assertions.assertEquals(shuffledPuzzle, loadedPuzzle, "Puzzle state should match the saved state")
    }

    @Test
    fun completePuzzleAndMarkAsDone() {
        val puzzleListener = PuzzleBoxPlugin()
        val player = mockPlayer
        val key = "mm"
        val puzzleItems = (3904..3950 step 2).toList() + -1
        val solution = (3904..3950 step 2).toList()

        val shuffledPuzzle = puzzleListener.generateSolvablePuzzle(puzzleItems)
        puzzleListener.savePuzzleStateInSession(player, key, shuffledPuzzle)

        puzzleListener.savePuzzleStateInSession(player, key, solution)
        setAttribute(player, "$key:puzzle:done", true)

        Assertions.assertTrue(getAttribute(player, "$key:puzzle:done", false), "Puzzle should be marked as completed")
    }
}
