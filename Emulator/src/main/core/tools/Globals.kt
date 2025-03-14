package core.tools

const val BLACK = "<col=000000>"
const val RED = "<col=ff0000>"
const val ORANGE = "<col=ff6600>"
const val YELLOW = "<col=ffff00>"
const val GREEN = "<col=66ff33>"
const val BLUE = "<col=0000ff>"
const val PURPLE = "<col=cc00ff>"
const val WHITE = "<col=ffffff>"

const val DARK_RED = "<col=8A0808>"
const val DARK_ORANGE = "<col=ab7000>"
const val DARK_YELLOW = "<col=a6a300>"
const val DARK_GREEN = "<col=007d0c>"
const val DARK_BLUE = "<col=08088A>"
const val DARK_PURPLE = "<col=734a75>"

private val pattern = Regex("%[0-9a-fA-F]{6}")

private val testData =
    arrayOf(
        "This is a string with no colors.",
        "This %R is a string with one color.",
        "This %R %G %B is a string with multiple colors.",
        "This %ffffff is an arbitrary hex string.",
    )

fun colorize(line: String): String {
    return line
        .replace("%BK", BLACK)
        .replace("%R", RED)
        .replace("%O", ORANGE)
        .replace("%Y", YELLOW)
        .replace("%G", GREEN)
        .replace("%B", BLUE)
        .replace("%P", PURPLE)
        .replace("%W", WHITE)
        .replace("%DR", DARK_RED)
        .replace("%DO", DARK_ORANGE)
        .replace("%DY", DARK_YELLOW)
        .replace("%DG", DARK_GREEN)
        .replace("%DB", DARK_BLUE)
        .replace("%DP", DARK_PURPLE)
        .replace(pattern) { matchResult -> "<col=${matchResult.value.substring(1)}>" }
        .append("</col>") + " "
}

fun colorize(
    line: String,
    hexColor: String,
): String {
    return line.prepend("<col=$hexColor>").append("</col>")
}

fun String.append(line: String): String {
    return this + line
}

fun String.prepend(line: String): String {
    return line + this
}

fun String.shuffle(): String {
    var new = ""
    val old = this.split("").toMutableList()
    for (i in this.indices) {
        val c = old.random()
        new += c.toString()
        old.remove(c)
    }
    return new
}

fun prependArticle(noun: String): String {
    if (noun == null) return noun
    val exceptions = hashMapOf("unicorn" to "a", "herb" to "an", "hour" to "an")
    if (exceptions.contains(noun.lowercase())) {
        return "${exceptions[noun.lowercase()]} $noun"
    }
    return when (noun[0]) {
        'a', 'e', 'i', 'o', 'u' -> "an $noun"
        else -> "a $noun"
    }
}
