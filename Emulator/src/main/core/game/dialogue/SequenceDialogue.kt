package core.game.dialogue

import core.game.dialogue.SequenceDialogue.DialogueLine
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player

/**
 * Utility for sequential dialogue handling.
 *
 * Supports:
 * - [DialogueLine.SpeechLine]: Player or NPC dialogue (optional) facial expression.
 * - [DialogueLine.TextLine]: Simple dialogue message shown.
 * - [DialogueLine.ItemLine]: Item dialogue message shown.
 * - [DialogueLine.DoubleItemLine]: Double item dialogue message shown.
 * - [DialogueLine.OptionsLine]: Option selection with a callback.
 * - (Optional) invokes [onComplete] when the sequence finishes.
 */
object SequenceDialogue {

    private const val OPTION_INDEX_OFFSET = 2

    /**
     * Represents a single dialogue entry in a sequence.
     */
    sealed class DialogueLine {
        /**
         * A standard speech line (player or NPCs).
         *
         * @param entity Entity speaking; null means player.
         * @param expression (Optional) Facial animation.
         * @param messages Dialogue text lines.
         */
        data class SpeechLine(val entity: Entity?, val expression: FaceAnim?, val messages: Array<out String>) :
            DialogueLine()

        /**
         * A plain dialogue line directly sent to the player.
         *
         * @param messages Dialogue text lines.
         */
        data class TextLine(val messages: Array<out String>) : DialogueLine()

        /**
         * An item-based dialogue line.
         *
         * @param itemId Item ID to show.
         * @param messages Dialogue text lines.
         */
        data class ItemLine(val itemId: Int, val messages: Array<out String>) : DialogueLine()

        /**
         * A double-item-based dialogue line.
         *
         * @param firstId First item ID to show.
         * @param secondId Second item ID to show.
         * @param messages Dialogue text lines.
         */
        data class DoubleItemLine(val firstId: Int, val secondId: Int, val messages: Array<out String>) : DialogueLine()

        /**
         * An options line for selecting one choice.
         *
         * @param title   Options title.
         * @param options List of option strings.
         * @param onSelect Callback invoked with selected index (1-based).
         */
        data class OptionsLine(val title: String, val options: Array<out String>, val onSelect: (Int) -> Unit) :
            DialogueLine()
    }

    /**
     * Sends a dialogue sequence to a player.
     *
     * @param player     Target player.
     * @param lines      Dialogue lines to show.
     * @param onComplete (Optional) callback after sequence ends.
     */
    fun sendSequenceDialogue(
        player: Player, lines: List<DialogueLine>, onComplete: (() -> Unit)? = null
    ) {
        fun sendAt(i: Int) {
            if (i >= lines.size) {
                onComplete?.invoke()
                return
            }

            val entry = lines[i]
            when (entry) {
                is DialogueLine.SpeechLine -> {
                    if (entry.messages.isEmpty()) {
                        sendAt(i + 1)
                        return
                    }
                    val entity = entry.entity ?: player
                    val expr = entry.expression ?: FaceAnim.HALF_GUILTY
                    player.dialogueInterpreter.sendDialogues(entity, expr, *entry.messages)
                    player.dialogueInterpreter.addAction { _, _ -> sendAt(i + 1) }
                }

                is DialogueLine.TextLine -> {
                    if (entry.messages.isEmpty()) {
                        sendAt(i + 1)
                        return
                    }
                    player.dialogueInterpreter.sendDialogue(*entry.messages)
                    player.dialogueInterpreter.addAction { _, _ -> sendAt(i + 1) }
                }

                is DialogueLine.ItemLine -> {
                    if (entry.messages.isEmpty()) {
                        sendAt(i + 1)
                        return
                    }
                    player.dialogueInterpreter.sendItemMessage(entry.itemId, *entry.messages)
                    player.dialogueInterpreter.addAction { _, _ -> sendAt(i + 1) }
                }

                is DialogueLine.DoubleItemLine -> {
                    if (entry.messages.isEmpty()) {
                        sendAt(i + 1)
                        return
                    }
                    if (entry.firstId <= 0 || entry.secondId <= 0) {
                        throw IllegalArgumentException("Invalid DoubleItemLine item IDs: firstId=${entry.firstId}, secondId=${entry.secondId}")
                    }
                    player.dialogueInterpreter.sendDoubleItemMessage(
                        entry.firstId, entry.secondId, entry.messages.joinToString("\n")
                    )
                    player.dialogueInterpreter.addAction { _, _ -> sendAt(i + 1) }
                }

                is DialogueLine.OptionsLine -> {
                    require(entry.options.size <= 5) {
                        "OptionsLine at index $i has more than 5 options (was: ${entry.options.size})"
                    }
                    player.dialogueInterpreter.sendOptions(entry.title, *entry.options)
                    player.dialogueInterpreter.addAction { _, selected ->
                        val optionIndex = selected - OPTION_INDEX_OFFSET + 1
                        if (optionIndex in 1..entry.options.size) {
                            entry.onSelect(optionIndex)
                        }
                        sendAt(i + 1)
                    }
                }
            }
        }

        if (lines.isNotEmpty()) sendAt(0) else onComplete?.invoke()
    }

    /**
     * Sends a dialogue sequence using vararg syntax.
     * @see SequenceDialogue
     */
    fun sendSequenceDialogue(
        player: Player, vararg lines: DialogueLine, onComplete: (() -> Unit)? = null
    ) = sendSequenceDialogue(player, listOf(*lines), onComplete)

    /**
     * Creates a player speech line with multiple text messages.
     *
     * @param expression Optional facial animation
     * @param messages Vararg lines of text
     * @throws IllegalArgumentException if no messages are provided
     */
    fun playerLine(expression: FaceAnim?, vararg messages: String): DialogueLine.SpeechLine {
        require(messages.isNotEmpty()) { "Player dialogue must not be empty." }
        return DialogueLine.SpeechLine(null, expression, messages)
    }

    /**
     * Creates a player speech line from a single multiline string.
     */
    fun playerLine(expression: FaceAnim?, message: String): DialogueLine.SpeechLine =
        DialogueLine.SpeechLine(null, expression, splitLines(message))

    /**
     * Creates a plain text dialogue line (no actor or animation).
     */
    fun dialogueLine(vararg messages: String): DialogueLine.TextLine {
        require(messages.isNotEmpty()) { "Dialogue must not be empty." }
        return DialogueLine.TextLine(messages)
    }

    /**
     * Creates a plain text dialogue line from a multiline string.
     */
    fun dialogueLine(message: String): DialogueLine.TextLine = DialogueLine.TextLine(splitLines(message))

    /**
     * Creates an NPC speech line with multiple messages.
     *
     * @param npc NPC entity
     * @param expression Optional facial animation
     * @param messages Vararg lines of text
     * @throws IllegalArgumentException if no messages are provided
     */
    fun npcLine(npc: Entity, expression: FaceAnim?, vararg messages: String): DialogueLine.SpeechLine {
        require(messages.isNotEmpty()) { "Empty dialogue line. At least one message is required." }
        return DialogueLine.SpeechLine(npc, expression, messages)
    }

    /**
     * Creates an NPC speech line from a single multiline string.
     */
    fun npcLine(npc: Entity, expression: FaceAnim?, message: String): DialogueLine.SpeechLine {
        require(message.isEmpty()) { "Invalid NPC dialogue: cannot be empty." }
        return DialogueLine.SpeechLine(npc, expression, splitLines(message))
    }

    /**
     * Creates an item dialogue line showing an item with messages.
     */
    fun itemLine(itemId: Int, vararg messages: String): DialogueLine.ItemLine {
        require(itemId > 0) { "Item ID must be positive and non-zero." }
        require(messages.isNotEmpty()) { "Empty item line. At least one message is required." }
        return DialogueLine.ItemLine(itemId, messages)
    }

    /**
     * Creates an item dialogue from a single multiline string.
     */
    fun itemLine(itemId: Int, message: String): DialogueLine.ItemLine {
        require(itemId > 0) { "Item ID must be positive and non-zero." }
        return DialogueLine.ItemLine(itemId, splitLines(message))
    }

    /**
     * Creates a double-item dialogue line with messages.
     */
    fun doubleItemLine(firstItem: Int, secondItem: Int, vararg messages: String): DialogueLine.DoubleItemLine {
        require(firstItem > 0) { "First item ID must be positive and non-zero." }
        require(secondItem > 0) { "Second item ID must be positive and non-zero." }
        require(messages.isNotEmpty()) { "Empty dialogue line. At least one message is required." }
        return DialogueLine.DoubleItemLine(firstItem, secondItem, messages)
    }

    /**
     * Creates a double-item dialogue line from a single multiline string.
     */
    fun doubleItemLine(firstId: Int, secondId: Int, message: String): DialogueLine.DoubleItemLine {
        require(firstId > 0) { "First item ID must be positive and non-zero." }
        require(secondId > 0) { "Second item ID must be positive and non-zero." }
        return DialogueLine.DoubleItemLine(firstId, secondId, splitLines(message))
    }

    /**
     * Creates an options dialogue with callback.
     *
     * @param title Displayed title
     * @param options Options (1-5)
     * @param onSelect Callback with selected index (1-based)
     */
    fun options(title: String, vararg options: String, onSelect: (Int) -> Unit): DialogueLine.OptionsLine {
        require(options.isNotEmpty()) { "Options must not be empty." }
        require(options.size in 1..5) { "Options value between 1 and 5, got [${options.size}]." }
        return DialogueLine.OptionsLine(title, options, onSelect)
    }

    /**
     * DSL-style builder for sequential dialogues.
     */
    fun dialogue(player: Player, block: DialogueBuilder.() -> Unit) {
        val builder = DialogueBuilder().apply(block)
        sendSequenceDialogue(player, builder.lines, builder.onComplete)
    }

    /**
     * DSL builder for dialogue sequence.
     */
    class DialogueBuilder {
        val lines = mutableListOf<DialogueLine>()
        var onComplete: (() -> Unit)? = null

        fun message(vararg text: String) {
            lines += dialogueLine(*text)
        }

        fun message(text: String) {
            lines += dialogueLine(text)
        }

        fun player(text: String) {
            lines += playerLine(FaceAnim.HALF_GUILTY, text)
        }

        fun player(expression: FaceAnim?, text: String) {
            lines += playerLine(expression, text)
        }

        fun player(expression: FaceAnim?, vararg text: String) {
            lines += playerLine(expression, *text)
        }

        fun player(vararg text: String) {
            lines += playerLine(FaceAnim.HALF_GUILTY, *text)
        }

        fun npc(npc: Entity, text: String) {
            lines += npcLine(npc, FaceAnim.HALF_GUILTY, text)
        }

        fun npc(npc: Entity, expression: FaceAnim?, vararg text: String) {
            lines += npcLine(npc, expression, *text)
        }

        fun npc(npc: Entity, expression: FaceAnim?, text: String) {
            lines += npcLine(npc, expression, text)
        }

        fun npc(npcId: Int, expression: FaceAnim?, vararg text: String) {
            lines += npcLine(NPC(npcId), expression, *text)
        }

        fun npc(npc: Entity, vararg text: String) {
            lines += npcLine(npc, FaceAnim.HALF_GUILTY, *text)
        }

        fun item(itemId: Int, vararg text: String) {
            lines += itemLine(itemId, *text)
        }

        fun item(itemId: Int, text: String) {
            lines += itemLine(itemId, text)
        }

        fun doubleItem(itemId1: Int, itemId2: Int, vararg text: String) {
            lines += doubleItemLine(itemId1, itemId2, *text)
        }

        fun doubleItem(itemId1: Int, itemId2: Int, text: String) {
            lines += doubleItemLine(itemId1, itemId2, text)
        }

        fun options(title: String?, vararg options: String, onSelect: (Int) -> Unit) {
            val realTitle = title ?: "Select an Option"
            lines += SequenceDialogue.options(realTitle, *options, onSelect = onSelect)
        }

        fun end(callback: () -> Unit) {
            onComplete = callback
        }
    }
}
