package core.game.dialogue

import java.util.*
import kotlin.math.ceil

object DialogueUtils {
    val tagRegex = "<([A-Za-z0-9=/]+)>".toRegex()

    fun removeMatches(
        message: String,
        regex: Regex,
    ): String {
        return regex.replace(message, "")
    }
}

/**
 * Automatically split a single continuous string into multiple comma-separated lines.
 *
 * Should this not work out for any reason, you should fall back to standard npc and player methods for dialogue.
 */
fun splitLines(
    message: String,
    perLineLimit: Int = 54,
): Array<String> {
    var lines =
        Array(
            ceil(DialogueUtils.removeMatches(message, DialogueUtils.tagRegex).length / perLineLimit.toFloat()).toInt(),
        ) { "" }

    if (lines.size == 1) {
        lines[0] = message
        return lines
    }

    val tokenQueue = LinkedList(message.split(" "))
    var index = 0
    val line = StringBuilder()
    var accumulator = 0

    val openTags = LinkedList<String>()

    fun pushLine() {
        if (line.isEmpty()) return

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

        if (lines.size == index) lines = lines.plus(line.toString()) else lines[index] = line.toString()
        index++
        line.clear()
        accumulator = 0

        for (tag in openTags) line.append(tag)
        openTags.clear()
    }

    while (!tokenQueue.isEmpty()) {
        val shouldSpace = DialogueUtils.removeMatches(line.toString(), DialogueUtils.tagRegex).isNotEmpty()
        accumulator += DialogueUtils.removeMatches(tokenQueue.peek(), DialogueUtils.tagRegex).length

        if (shouldSpace) accumulator += 1
        if (accumulator > perLineLimit) {
            pushLine()
            continue
        }

        if (shouldSpace) line.append(" ")
        line.append(tokenQueue.pop())
    }

    pushLine()

    if (lines.size > 4) {
        lines[3] = lines[3] + "<br>" + lines[4]
        lines = lines.sliceArray(0..3)
    }

    return lines
}
