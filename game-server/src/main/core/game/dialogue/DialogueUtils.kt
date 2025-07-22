package core.game.dialogue

import java.util.*
import kotlin.math.ceil

/**
 * A utility object that provides helper functions for working with dialogues.
 */
object DialogueUtils {
    /**
     * Regular expression pattern used to identify tags in a dialogue message.
     */
    val tagRegex = "<([A-Za-z0-9=/]+)>".toRegex()

    /**
     * Removes all matches of a given regular expression from a message.
     *
     * @param message The input message to process.
     * @param regex The regular expression pattern to match and remove.
     * @return The processed message with all matches removed.
     */
    fun removeMatches(
        message: String,
        regex: Regex,
    ): String = regex.replace(message, "")
}

/**
 * Automatically splits a long string into multiple comma-separated lines based on a character limit per line.
 *
 * @param message The message to split into lines.
 * @param perLineLimit The maximum number of characters allowed per line (default is 54).
 * @return An array of strings, each representing a line of dialogue.
 */
fun splitLines(
    message: String,
    perLineLimit: Int = 54,
): Array<String> {
    // Calculate the number of lines required to split the message, considering tag removal.
    var lines =
        Array(
            ceil(DialogueUtils.removeMatches(message, DialogueUtils.tagRegex).length / perLineLimit.toFloat()).toInt(),
        ) { "" }

    // If the message fits within a single line, return it as is.
    if (lines.size == 1) {
        lines[0] = message
        return lines
    }

    // Tokenize the message by spaces and initialize necessary variables.
    val tokenQueue = LinkedList(message.split(" "))
    var index = 0
    val line = StringBuilder()
    var accumulator = 0

    // List to keep track of open tags to prevent tag mismatches.
    val openTags = LinkedList<String>()

    /**
     * Helper function to push the current line to the lines array.
     */
    fun pushLine() {
        if (line.isEmpty()) return

        // Process tags in the current line to ensure they are properly opened/closed.
        for (lineTag in DialogueUtils.tagRegex.findAll(line)) {
            if (lineTag.value[1] == '/') {
                for (openTag in openTags.descendingIterator()) {
                    val lineTagName = lineTag.value.substring(2, lineTag.value.length - 1)
                    val openTagName = openTag.substring(1, lineTag.value.length - 2)
                    if (lineTagName == openTagName) {
                        openTags.remove(openTag)
                        break
                    }
                }
            } else {
                openTags.add(lineTag.value)
            }
        }

        // Add the line to the lines array and increment the line index.
        if (lines.size == index) lines = lines.plus(line.toString()) else lines[index] = line.toString()
        index++
        line.clear()
        accumulator = 0

        // Add any remaining open tags to the new line.
        for (tag in openTags) line.append(tag)
        openTags.clear()
    }

    // Process each token in the message.
    while (!tokenQueue.isEmpty()) {
        val shouldSpace = DialogueUtils.removeMatches(line.toString(), DialogueUtils.tagRegex).isNotEmpty()
        accumulator += DialogueUtils.removeMatches(tokenQueue.peek(), DialogueUtils.tagRegex).length

        // Add space between words if necessary.
        if (shouldSpace) accumulator += 1
        if (accumulator > perLineLimit) {
            pushLine()
            continue
        }

        // Append the current token to the line.
        if (shouldSpace) line.append(" ")
        line.append(tokenQueue.pop())
    }

    // Push the final line.
    pushLine()

    // Combine lines if there are more than 4, creating a scrollable message format.
    if (lines.size > 4) {
        lines[3] = lines[3] + "<br>" + lines[4]
        lines = lines.sliceArray(0..3)
    }

    return lines
}
