package core.game.dialogue

import core.game.node.entity.Entity
import core.game.node.entity.player.Player

/**
 * Utility for sequential dialogue handling.
 *
 * Supports:
 * - [DialogueLine.SpeechLine]: Player or NPC dialogue (optional) facial expression.
 * - [DialogueLine.TextLine]: Simple dialogue message shown.
 * - [DialogueLine.OptionsLine]: Option selection with a callback.
 * - (Optional) invokes [onComplete] when the sequence finishes.
 */
object SequenceDialogue {

    /**
     * Sealed class for dialogue entries.
     */
    sealed class DialogueLine {
        /**
         * A standard speech line (player or NPCs).
         *
         * @param entity Entity speaking; null means player.
         * @param expression Facial animation.
         * @param messages Dialogue text lines.
         */
        data class SpeechLine(val entity: Entity?, val expression: FaceAnim?, val messages: List<String>) : DialogueLine()

        /**
         * A plain dialogue line directly sent to the player.
         */
        data class TextLine(val messages: List<String>) : DialogueLine()

        /**
         * An options line.
         *
         * @param title   Options title.
         * @param options List of option strings.
         * @param onSelect Callback invoked with selected index.
         */
        data class OptionsLine(val title: String, val options: List<String>, val onSelect: (Int) -> Unit) : DialogueLine()
    }

    /**
     * Sends a dialogue sequence to a player.
     *
     * @param player     Target player.
     * @param lines      List of dialogue entries in order.
     * @param onComplete Optional callback after sequence ends.
     */
    fun sendSequenceDialogue(
        player: Player,
        lines: List<DialogueLine>,
        onComplete: (() -> Unit)? = null
    ) {
        fun sendAt(i: Int) {
            if (i >= lines.size) {
                onComplete?.invoke()
                return
            }
            when (val entry = lines[i]) {
                is DialogueLine.SpeechLine -> {
                    if (entry.messages.isEmpty()) {
                        sendAt(i + 1)
                        return
                    }
                    val entity = entry.entity ?: player
                    val expr = entry.expression ?: FaceAnim.HALF_GUILTY
                    player.dialogueInterpreter.sendDialogues(entity, expr, *entry.messages.toTypedArray())
                    player.dialogueInterpreter.addAction { _, _ -> sendAt(i + 1) }
                }

                is DialogueLine.TextLine -> {
                    if (entry.messages.isEmpty()) {
                        sendAt(i + 1)
                        return
                    }
                    player.dialogueInterpreter.sendDialogue(*entry.messages.toTypedArray())
                    player.dialogueInterpreter.addAction { _, _ -> sendAt(i + 1) }
                }

                is DialogueLine.OptionsLine -> {
                    require(entry.options.size <= 5) { "OptionsLine at index $i has more than 5 options (now: ${entry.options.size})" }
                    val optionsToShow = entry.options.take(5)
                    player.dialogueInterpreter.sendOptions(entry.title, *optionsToShow.toTypedArray())
                    player.dialogueInterpreter.addAction { _, selected ->
                        val optionIndex = selected - 2
                        if (optionIndex in optionsToShow.indices) {
                            entry.onSelect(optionIndex + 1)
                        }
                        sendAt(i + 1)
                    }
                }

                else -> throw IllegalStateException("Unhandled DialogueLine type at index $i: ${entry::class.simpleName}")
            }
        }
        if (lines.isNotEmpty()) sendAt(0) else onComplete?.invoke()
    }

    /**
     * Overload: vararg lines without needing to build a list.
     */
    fun sendSequenceDialogue(
        player: Player, vararg lines: DialogueLine, onComplete: (() -> Unit)? = null
    ) = sendSequenceDialogue(player, lines.toList(), onComplete)

    /**
     * Build a player speech line.
     */
    fun playerLine(expression: FaceAnim?, vararg messages: String): DialogueLine.SpeechLine =
        DialogueLine.SpeechLine(null, expression, messages.toList())

    /**
     * Build a player speech line from a single multiline string.
     */
    fun playerLine(expression: FaceAnim?, message: String): DialogueLine.SpeechLine =
        DialogueLine.SpeechLine(null, expression, splitLines(message).toList())

    /**
     * Build a basic text dialogue line (no actor or animation).
     */
    fun dialogueLine(vararg messages: String): DialogueLine.TextLine =
        DialogueLine.TextLine(messages.toList())

    /**
     * Build a basic text dialogue line from a single multiline string.
     */
    fun dialogueLine(message: String): DialogueLine.TextLine =
        DialogueLine.TextLine(splitLines(message).toList())

    /**
     * Build an NPC speech line.
     */
    fun npcLine(npc: Entity, expression: FaceAnim?, vararg messages: String): DialogueLine.SpeechLine =
        DialogueLine.SpeechLine(npc, expression, messages.toList())

    /**
     * Build an NPC speech line from a single multiline string.
     */
    fun npcLine(npc: Entity, expression: FaceAnim?, message: String): DialogueLine.SpeechLine =
        DialogueLine.SpeechLine(npc, expression, splitLines(message).toList())

    /**
     * Build an options line with vararg.
     *
     * @param title    Prompt text.
     * @param options  Option labels.
     * @param onSelect Callback with selected index.
     */
    fun options(title: String, vararg options: String, onSelect: (Int) -> Unit): DialogueLine.OptionsLine =
        DialogueLine.OptionsLine(title, options.toList(), onSelect)
}
