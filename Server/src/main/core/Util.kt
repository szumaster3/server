package core

import core.game.world.map.Location
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.stream.Collectors
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * A collection of general utility functions used across the server.
 */
object Util {

    /**
     * Number names for multiples of ten.
     */
    private val tens_names = arrayOf("", " ten", " twenty", " thirty", " forty", " fifty", " sixty", " seventy", " eighty", " ninety")

    /**
     * Number names for numbers zero to nineteen.
     */
    val number_names: Array<String> = arrayOf("", " one", " two", " three", " four", " five", " six", " seven", " eight", " nine", " ten", " eleven", " twelve", " thirteen", " fourteen", " fifteen", " sixteen", " seventeen", " eighteen", " nineteen")

    /**
     * Shared random instance.
     */
    val random: Random = Random()

    /**
     * Converts a primitive [IntArray] to an [Array] of nullable [Int].
     */
    @JvmStatic
    fun convertToIntegerArray(primitiveArray: IntArray): Array<Int?> {
        val wrapperArray = arrayOfNulls<Int>(primitiveArray.size)
        for (i in primitiveArray.indices) {
            wrapperArray[i] = primitiveArray[i]
        }
        return wrapperArray
    }

    /**
     * Parses a [Location] from a string in the format "x,y,z".
     *
     * @author Ceikry
     */
    @JvmStatic
    fun parseLocation(locString: String): Location {
        val locTokens =
            Arrays
                .stream(locString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                .map { s: String -> s.toInt() }
                .collect(Collectors.toList())
        return Location(locTokens[0], locTokens[1], locTokens[2])
    }

    /**
     * Returns a random integer between 0 and [i], inclusive.
     */
    fun random(random: Random, i: Int): Int = random.nextInt(i + 1)

    /**
     * Returns a random integer between 0 and [i], inclusive, using the default random.
     */
    fun random(i: Int): Int = random(random, i)

    /**
     * Returns a random [Double] between 0.0 and 1.0.
     */
    @JvmOverloads
    fun randomDouble(random: Random = Util.random): Double = random.nextDouble()

    /**
     * Formats an item [name] with an article ("a"/"an") or "the" if [definite] is true.
     */
    fun formatItemName(name: String, definite: Boolean = false): String {
        val lower = name.lowercase()
        return if (definite) {
            "the $lower"
        } else {
            val firstChar = lower.firstOrNull()
            val article = if (firstChar != null && firstChar in listOf('a', 'e', 'i', 'o', 'u')) "an" else "a"
            "$article $lower"
        }
    }

    /**
     * Capitalizes the first character of the given [name].
     */
    fun capitalize(name: String?): String? {
        if (name != null && name.length != 0) {
            val chars = name.toCharArray()
            chars[0] = chars[0].uppercaseChar()
            return String(chars)
        } else {
            return name
        }
    }

    /**
     * Converts an enum-style [name] (e.g., "SOME_ENUM") to a readable string (e.g., "Some enum").
     */
    fun enumToString(name: String): String? {
        var name = name
        name = name.lowercase(Locale.getDefault())
        name = name.replace("_".toRegex(), " ")
        return capitalize(name)
    }

    /**
     * Clamps [input] to the range of [min] and [max] for [Double] values.
     */
    fun clamp(input: Double, min: Double, max: Double): Double = kotlin.math.max(kotlin.math.min(input, max), min)

    /**
     * Clamps [input] to the range of [min] and [max] for [Int] values.
     */
    fun clamp(input: Int, min: Int, max: Int): Int =
        kotlin.math
            .max(kotlin.math.min(input.toDouble(), max.toDouble()), min.toDouble())
            .toInt()

    /**
     * Returns the timestamp (ms) of the next midnight based on [currentTime].
     */
    fun nextMidnight(currentTime: Long): Long {
        val date = Date()
        val cal = Calendar.getInstance()

        date.time = currentTime
        cal.time = date
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        cal.add(Calendar.HOUR_OF_DAY, 24)

        return cal.time.time
    }

    /**
     * Finds the first matching element in [array] that satisfies [predicate].
     */
    inline fun <T> findMatching(
        array: Array<T>,
        predicate: (T) -> Boolean,
    ): T? = array.firstOrNull(predicate)

    /**
     * Returns [primary] if not null; otherwise returns [defaultValue].
     */
    fun <T> getOrDefault(
        primary: T?,
        defaultValue: T,
    ): T = primary ?: defaultValue

    /**
     * Returns a random integer between [min] and [max], inclusive.
     */
    fun random(random: Random, min: Int, max: Int): Int {
        val n = abs((max - min).toDouble()).toInt()
        return (kotlin.math.min(min.toDouble(), max.toDouble()) + (if (n == 0) 0 else random(random, n))).toInt()
    }

    /**
     * Returns a random integer between [min] and [max], inclusive, using the default random.
     */
    fun random(min: Int, max: Int): Int = random(random, min, max)

    /**
     * Calculates the Euclidean distance between two points.
     */
    fun getDistance(coordX1: Int, coordY1: Int, coordX2: Int, coordY2: Int): Int {
        val deltaX = coordX2 - coordX1
        val deltaY = coordY2 - coordY1
        return (sqrt(deltaX.toDouble().pow(2.0) + deltaY.toDouble().pow(2.0)).toInt())
    }

    /**
     * Converts numbers less than 1000 to their English word representation.
     */
    private fun convertLessThanOneThousand(number: Int): String {
        var number = number
        var soFar: String

        if (number % 100 < 20) {
            soFar = number_names[number % 100]
            number /= 100
        } else {
            soFar = number_names[number % 10]
            number /= 10

            soFar = tens_names[number % 10] + soFar
            number /= 10
        }
        if (number == 0) {
            return soFar
        }
        return number_names[number] + " hundred" + soFar
    }

    /**
     * Converts [number] into its full English word representation.
     */
    fun convert(number: Int): String {
        if (number == 0) {
            return "zero"
        }

        var snumber = number.toLong().toString()

        val mask = "000000000000"
        val df = DecimalFormat(mask)
        snumber = df.format(number.toLong())

        val billions = snumber.substring(0, 3).toInt()
        val millions = snumber.substring(3, 6).toInt()
        val hundredThousands = snumber.substring(6, 9).toInt()
        val thousands = snumber.substring(9, 12).toInt()
        val tradBillions =
            when (billions) {
                0 -> ""
                1 -> convertLessThanOneThousand(billions) + " billion "
                else -> convertLessThanOneThousand(billions) + " billion "
            }
        var result = tradBillions
        val tradMillions =
            when (millions) {
                0 -> ""
                1 -> convertLessThanOneThousand(millions) + " million "
                else -> convertLessThanOneThousand(millions) + " million "
            }
        result = result + tradMillions
        val tradHundredThousands =
            when (hundredThousands) {
                0 -> ""
                1 -> "one thousand "
                else -> convertLessThanOneThousand(hundredThousands) + " thousand "
            }
        result = result + tradHundredThousands
        val tradThousand = convertLessThanOneThousand(thousands)
        result = result + tradThousand

        return result.replace("^\\s+".toRegex(), "").replace("\\b\\s{2,}\\b".toRegex(), " ")
    }

    /**
     * Formats an [Int] to include commas.
     */
    fun format(number: Int): String = NumberFormat.getNumberInstance().format(number.toLong())

    /**
     * Formats a [Long] to include commas.
     */
    fun format(number: Long): String = NumberFormat.getNumberInstance().format(number)

    /**
     * Formats a [Float] to include commas.
     */
    fun format(number: Float): String = NumberFormat.getNumberInstance().format(number.toDouble())

    /**
     * Compares two objects [a] and [b] for equality, handling nulls safely.
     */
    fun equals(a: Any?, b: Any): Boolean = (a === b) || (a != null && a == b)

    /**
     * Rounds a [Double] to [places] decimal places.
     */
    fun round(value: Double, places: Int): Double {
        require(places >= 0)
        try {
            var bd = BigDecimal(value)
            bd = bd.setScale(places, RoundingMode.HALF_UP)
            return bd.toDouble()
        } catch (e: NumberFormatException) {
            return -1.0
        }
    }
}
