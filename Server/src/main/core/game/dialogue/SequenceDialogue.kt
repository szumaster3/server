package core.game.dialogue

import core.api.sendInputDialogue
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE

/**
 * Utility for sequential dialogue handling.
 *
 * Supports:
 * - [DialogueLine.SpeechLine]: Player or NPC dialogue (optional) facial expression.
 * - [DialogueLine.messageLine]: Simple dialogue message shown.
 * - [DialogueLine.ItemLine]: Item dialogue message shown.
 * - [DialogueLine.DoubleItemLine]: Double item dialogue message shown.
 * - [DialogueLine.OptionsLine]: Option selection with a callback.
 * - [DialogueLine.InputLine]: Input prompt with a callback, numeric or message input.
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
         * @param messages Dialogue message lines.
         */
        data class SpeechLine(
            val entity: Entity?,
            val expression: FaceAnim?,
            val messages: Array<out String>
        ) : DialogueLine()

        /**
         * A plain dialogue line directly sent to the player.
         *
         * @param messages Dialogue message lines.
         */
        data class MessageLine(val messages: Array<out String>) : DialogueLine() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as MessageLine

                return messages.contentEquals(other.messages)
            }

            override fun hashCode(): Int {
                return messages.contentHashCode()
            }
        }

        /**
         * An item-based dialogue line.
         *
         * @param itemId Item ID to show.
         * @param messages Dialogue message lines.
         */
        data class ItemLine(val itemId: Int, val messages: Array<out String>) : DialogueLine()

        /**
         * A double-item-based dialogue line.
         *
         * @param firstId First item ID to show.
         * @param secondId Second item ID to show.
         * @param messages Dialogue message lines.
         */
        data class DoubleItemLine(
            val firstId: Int,
            val secondId: Int,
            val messages: Array<out String>
        ) : DialogueLine()

        /**
         * An input line for string or numeric value input.
         *
         * @param numeric If true, numeric input is required; otherwise, string.
         * @param prompt Prompt message displayed to the player.
         * @param handler Callback receiving the entered value (String or Int).
         */
        data class InputLine(
            val numeric: Boolean,
            val prompt: String,
            val handler: (Any, (Boolean) -> Unit) -> Unit
        ) : DialogueLine()

        /**
         * An options line for selecting one choice.
         *
         * @param title Options title.
         * @param options List of option strings.
         * @param onSelect Callback invoked with selected index (1-based).
         */
        data class OptionsLine(
            val title: String,
            val options: Array<out String>,
            val onSelect: (Int) -> Unit
        ) : DialogueLine()
    }

    /**
     * Sends a dialogue sequence to a player.
     *
     * @param player Target player.
     * @param lines Dialogue lines to show.
     * @param onComplete (Optional) callback after sequence ends.
     */
    fun sendSequenceDialogue(
        player: Player,
        lines: List<DialogueLine>,
        onComplete: (() -> Unit)? = null
    ) {
        fun sendAt(i: Int) {
            if (i >= lines.size) {
                onComplete?.invoke()
                try {
                    val current = player.dialogueInterpreter.dialogue
                    if (current != null && current.file != null) {
                        current.file.stage = (END_DIALOGUE)
                    }
                } catch (_: Exception) {}
                player.dialogueInterpreter.close()
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
                is DialogueLine.MessageLine -> {
                    if (entry.messages.isEmpty()) {
                        sendAt(i + 1)
                        return
                    }
                    player.dialogueInterpreter.sendDialogue(*entry.messages)
                    player.dialogueInterpreter.addAction { _, _ -> sendAt(i + 1) }
                }
                is DialogueLine.ItemLine -> {
                    if (entry.messages.isEmpty() || entry.itemId <= 0) {
                        sendAt(i + 1)
                        return
                    }
                    player.dialogueInterpreter.sendItemMessage(entry.itemId, *entry.messages)
                    player.dialogueInterpreter.addAction { _, _ -> sendAt(i + 1) }
                }
                is DialogueLine.DoubleItemLine -> {
                    if (entry.messages.isEmpty() || entry.firstId <= 0 || entry.secondId <= 0) {
                        sendAt(i + 1)
                        return
                    }
                    player.dialogueInterpreter.sendDoubleItemMessage(
                        entry.firstId,
                        entry.secondId,
                        entry.messages.joinToString("\n")
                    )
                    player.dialogueInterpreter.addAction { _, _ -> sendAt(i + 1) }
                }
                is DialogueLine.InputLine -> {
                    val type = if (entry.numeric) InputType.NUMERIC else InputType.STRING_SHORT
                    sendInputDialogue(player, type, entry.prompt) { value ->
                        entry.handler(value) { continueDialogue ->
                            if (continueDialogue) sendAt(i + 1)
                            else {
                                onComplete?.invoke()
                                player.dialogueInterpreter.close()
                            }
                        }
                    }
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

        if (lines.isNotEmpty()) {
            sendAt(0)
        } else {
            onComplete?.invoke()
            player.dialogueInterpreter.close()
        }
    }

    /**
     * Sends a dialogue sequence using vararg syntax.
     *
     * @see SequenceDialogue
     */
    fun sendSequenceDialogue(
        player: Player,
        vararg lines: DialogueLine,
        onComplete: (() -> Unit)? = null
    ) = sendSequenceDialogue(player, listOf(*lines), onComplete)

    /**
     * Creates a player speech line with multiple message messages.
     *
     * @param expression Optional facial animation
     * @param messages Vararg lines of message
     * @throws IllegalArgumentException if no messages are provided
     */
    fun playerLine(expression: FaceAnim?, vararg messages: String) =
        DialogueLine.SpeechLine(null, expression, messages)

    /**
     * Creates a player speech line from a single multiline string.
     */
    fun playerLine(expression: FaceAnim?, message: String) =
        DialogueLine.SpeechLine(null, expression, splitLines(message))

    /**
     * Creates a plain message dialogue line (no actor or animation).
     */
    fun dialogueLine(vararg messages: String) = DialogueLine.MessageLine(messages)

    /**
     * Creates a plain message dialogue line from a multiline string.
     */
    fun dialogueLine(message: String) = DialogueLine.MessageLine(splitLines(message))

    /**
     * Creates an NPC speech line with multiple messages.
     *
     * @param npc NPC entity
     * @param expression Optional facial animation
     * @param messages Vararg lines of message
     * @throws IllegalArgumentException if no messages are provided
     */
    fun npcLine(npc: Entity, expression: FaceAnim?, vararg messages: String) =
        DialogueLine.SpeechLine(npc, expression, messages)

    /**
     * Creates an NPC speech line from a single multiline string.
     */
    fun npcLine(npc: Entity, expression: FaceAnim?, message: String) =
        DialogueLine.SpeechLine(npc, expression, splitLines(message))

    /**
     * Creates an NPC speech line from a single multiline string.
     */
    fun npcLine(npc: Any, expression: FaceAnim?, message: String): DialogueLine.SpeechLine {
        val npcEntity =
            when (npc) {
                is NPC -> npc
                is Int -> NPC(npc)
                else -> throw IllegalArgumentException("Invalid NPC type: ${npc::class.simpleName}")
            }
        return DialogueLine.SpeechLine(npcEntity, expression, splitLines(message))
    }

    /**
     * Creates an item dialogue line showing an item with messages.
     */
    fun itemLine(itemId: Int, vararg messages: String) = DialogueLine.ItemLine(itemId, messages)

    /**
     * Creates an item dialogue from a single multiline string.
     */
    fun itemLine(itemId: Int, message: String) = DialogueLine.ItemLine(itemId, splitLines(message))

    /**
     * Creates a double-item dialogue line with messages.
     */
    fun doubleItemLine(firstId: Int, secondId: Int, vararg messages: String) =
        DialogueLine.DoubleItemLine(firstId, secondId, messages)

    /**
     * Creates a double-item dialogue line from a single multiline string.
     */
    fun doubleItemLine(firstId: Int, secondId: Int, message: String) =
        DialogueLine.DoubleItemLine(firstId, secondId, splitLines(message))

    /**
     * Creates an input dialogue line with a prompt and a handler.
     */
    fun inputLine(numeric: Boolean, prompt: String, handler: (Any, (Boolean) -> Unit) -> Unit) =
        DialogueLine.InputLine(numeric, prompt, handler)

    /**
     * Creates an options dialogue with callback.
     *
     * @param title Displayed title
     * @param options Options (1-5)
     * @param onSelect Callback with selected index (1-based)
     */
    fun options(title: String, vararg options: String, onSelect: (Int) -> Unit) =
        DialogueLine.OptionsLine(title, options, onSelect)

    /** DSL-style builder for sequential dialogues. */
    fun dialogue(player: Player, block: DialogueBuilder.() -> Unit) {
        val builder = DialogueBuilder().apply(block)
        sendSequenceDialogue(player, builder.lines, builder.onComplete)
    }

    /** DSL builder for dialogue sequence. */
    class DialogueBuilder {
        val lines = mutableListOf<DialogueLine>()
        var onComplete: (() -> Unit)? = null

        fun message(vararg message: String) = lines.add(dialogueLine(*message))

        fun message(message: String) = lines.add(dialogueLine(*splitLines(message)))

        fun player(message: String) =
            lines.add(playerLine(FaceAnim.HALF_GUILTY, *splitLines(message)))

        fun player(expression: FaceAnim?, message: String) =
            lines.add(playerLine(expression, *splitLines(message)))

        fun player(expression: FaceAnim?, vararg message: String) =
            lines.add(playerLine(expression, *message))

        fun player(vararg message: String) = lines.add(playerLine(FaceAnim.HALF_GUILTY, *message))

        fun npc(npc: Entity, message: String) =
            lines.add(npcLine(npc, FaceAnim.HALF_GUILTY, *splitLines(message)))

        fun npc(npc: Entity, expression: FaceAnim?, message: String) =
            lines.add(npcLine(npc, expression, *splitLines(message)))

        fun npc(npc: Entity, expression: FaceAnim?, vararg message: String) =
            lines.add(npcLine(npc, expression, *message))

        fun npc(npcId: Int, expression: FaceAnim?, vararg message: String) {
            val split = message.flatMap { splitLines(it).toList() }.toTypedArray()
            lines.add(npcLine(NPC(npcId), expression, *split))
        }

        fun npc(npc: Entity, vararg message: String) =
            lines.add(npcLine(npc, FaceAnim.HALF_GUILTY, *message))

        fun npc(npc: Int, vararg message: String) =
            lines.add(npcLine(NPC(npc), FaceAnim.HALF_GUILTY, *message))

        fun npc(npc: Int, message: String) =
            lines.add(npcLine(NPC(npc), FaceAnim.HALF_GUILTY, *splitLines(message)))

        fun npc(npc: Any, expression: FaceAnim?, vararg message: String) {
            val npcEntity =
                when (npc) {
                    is NPC -> npc
                    is Int -> NPC(npc)
                    else -> throw IllegalArgumentException("Invalid NPC: ${npc::class.simpleName}.")
                }
            lines.add(npcLine(npcEntity, expression, *message))
        }

        fun item(itemId: Int, vararg message: String) = lines.add(itemLine(itemId, *message))

        fun item(itemId: Int, message: String) = lines.add(itemLine(itemId, *splitLines(message)))

        fun doubleItem(itemId1: Int, itemId2: Int, vararg message: String) =
            lines.add(doubleItemLine(itemId1, itemId2, *message))

        fun doubleItem(itemId1: Int, itemId2: Int, message: String) =
            lines.add(doubleItemLine(itemId1, itemId2, *splitLines(message)))

        fun input(numeric: Boolean, prompt: String, handler: (Any, (Boolean) -> Unit) -> Unit) =
            lines.add(inputLine(numeric, prompt, handler))

        fun options(title: String?, vararg options: String, onSelect: (Int) -> Unit) {
            val realTitle = title ?: "Select an Option"
            lines.add(SequenceDialogue.options(realTitle, *options, onSelect = onSelect))
        }

        fun end(callback: () -> Unit) {
            onComplete = callback
        }
    }
}
