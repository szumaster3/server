package content.region.misthalin.handlers.playersafety

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import org.rs.consts.Components

class PlayerSafetyInterface : InterfaceListener {
    companion object {
        /*
         * List of test questions for the player safety exam.
         */

        private val testQuestions =
            listOf(
                TestQuestion(Components.PLAYER_SAFETY_EXAM_697, 26, mapOf(4 to 37, 3 to 40, 5 to 43), 4),
                TestQuestion(Components.PLAYER_SAFETY_EXAM_699, 20, mapOf(4 to 31, 3 to 34), 4),
                TestQuestion(Components.PLAYER_SAFETY_EXAM_707, 20, mapOf(3 to 31, 4 to 35), 3),
                TestQuestion(Components.PLAYER_SAFETY_EXAM_710, 20, mapOf(9 to 31, 10 to 34), 9),
                TestQuestion(Components.PLAYER_SAFETY_EXAM_704, 26, mapOf(10 to 37, 12 to 43, 11 to 40), 10),
                TestQuestion(Components.PLAYER_SAFETY_EXAM_708, 29, mapOf(12 to 40, 13 to 43), 12),
                TestQuestion(Components.PLAYER_SAFETY_EXAM_696, 20, mapOf(4 to 31, 3 to 34), 4),
                TestQuestion(Components.PLAYER_SAFETY_EXAM_705, 26, mapOf(10 to 37, 12 to 43, 11 to 40), 10),
            )
    }

    /*
     * Tracks the current question number in the test.
     */

    private var testQuestionNumber = 0

    class TestQuestion(
        val interfaceId: Int,
        val baseChild: Int,
        val answers: Map<Int, Int>,
        val correctOption: Int,
    ) {
        /*
         * Displays the answer to the player based on the button clicked.
         */

        fun showAnswer(
            player: Player,
            button: Int,
        ) {
            setComponentVisibility(player, interfaceId, baseChild, false)
            answers[button]?.let { setComponentVisibility(player, interfaceId, it, false) }
        }
    }

    /*
     * Checks the player's answer and updates the next question.
     */

    private fun checkAnswer(
        player: Player,
        button: Int,
    ) {
        val question = testQuestions.getOrNull(testQuestionNumber) ?: return
        question.showAnswer(player, button)
        if (button == question.correctOption) testQuestionNumber++
    }

    /*
     * Finalizes the test for the player.
     */

    private fun completedTest(player: Player) {
        closeInterface(player)
        player.savedData.globalData.setTestStage(2)
        sendMessage(player, "Well done! You completed the exam.")
        sendDialogueLines(
            player,
            "Congratulations! The test has been completed. Hand the paper in to",
            "Professor Henry for marking.",
        )
    }

    /*
     * Updates the interface for the player with the test question.
     */

    fun update(player: Player) {
        closeInterface(player)
        testQuestions.getOrNull(testQuestionNumber)?.let {
            openInterface(player, it.interfaceId)
        }
    }

    override fun defineInterfaceListeners() {
        /*
         * Handles reset the question number.
         */

        onOpen(Components.PLAYER_SAFETY_EXAM_697) { _, _ ->
            testQuestionNumber = 0
            return@onOpen true
        }

        /*
         * Sets up listeners for each test question.
         */

        testQuestions.forEach { question ->
            on(question.interfaceId) { player, _, _, buttonID, _, _ ->
                if (buttonID in 0..35) {
                    checkAnswer(player, buttonID)
                } else {
                    update(player)

                    /*
                     * Completes the test if all questions have been answered.
                     */

                    if (testQuestionNumber >= testQuestions.size) {
                        completedTest(player)
                    }
                }
                return@on true
            }
        }
    }
}
