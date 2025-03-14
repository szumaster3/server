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

object Util {
    private val tens_names =
        arrayOf("", " ten", " twenty", " thirty", " forty", " fifty", " sixty", " seventy", " eighty", " ninety")

    val number_names: Array<String> =
        arrayOf(
            "",
            " one",
            " two",
            " three",
            " four",
            " five",
            " six",
            " seven",
            " eight",
            " nine",
            " ten",
            " eleven",
            " twelve",
            " thirteen",
            " fourteen",
            " fifteen",
            " sixteen",
            " seventeen",
            " eighteen",
            " nineteen",
        )

    val random: Random = Random()

    @JvmStatic
    fun convertToIntegerArray(primitiveArray: IntArray): Array<Int?> {
        val wrapperArray = arrayOfNulls<Int>(primitiveArray.size)
        for (i in primitiveArray.indices) {
            wrapperArray[i] = primitiveArray[i]
        }
        return wrapperArray
    }

    @JvmStatic
    fun parseLocation(locString: String): Location {
        val locTokens =
            Arrays
                .stream(locString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                .map { s: String -> s.toInt() }
                .collect(Collectors.toList())
        return Location(locTokens[0], locTokens[1], locTokens[2])
    }

    @JvmStatic
    fun getTimeUnitString(
        count: Long,
        singular: String,
        plural: String,
    ): String {
        return if (count == 1L) "$count $singular" else "$count $plural"
    }

    fun random(
        random: Random,
        i: Int,
    ): Int {
        return random.nextInt(i + 1)
    }

    fun random(i: Int): Int {
        return random(random, i)
    }

    @JvmOverloads
    fun randomDouble(random: Random = Util.random): Double {
        return random.nextDouble()
    }

    fun capitalize(name: String?): String? {
        if (name != null && name.length != 0) {
            val chars = name.toCharArray()
            chars[0] = chars[0].uppercaseChar()
            return String(chars)
        } else {
            return name
        }
    }

    fun enumToString(name: String): String? {
        var name = name
        name = name.lowercase(Locale.getDefault())
        name = name.replace("_".toRegex(), " ")
        return capitalize(name)
    }

    fun clamp(
        input: Double,
        min: Double,
        max: Double,
    ): Double {
        return kotlin.math.max(kotlin.math.min(input, max), min)
    }

    fun clamp(
        input: Int,
        min: Int,
        max: Int,
    ): Int {
        return kotlin.math
            .max(kotlin.math.min(input.toDouble(), max.toDouble()), min.toDouble())
            .toInt()
    }

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

    inline fun <T> findMatching(
        array: Array<T>,
        predicate: (T) -> Boolean,
    ): T? {
        return array.firstOrNull(predicate)
    }

    fun <T> getOrDefault(
        primary: T?,
        defaultValue: T,
    ): T {
        return primary ?: defaultValue
    }

    fun random(
        random: Random,
        min: Int,
        max: Int,
    ): Int {
        val n = abs((max - min).toDouble()).toInt()
        return (kotlin.math.min(min.toDouble(), max.toDouble()) + (if (n == 0) 0 else random(random, n))).toInt()
    }

    fun random(
        min: Int,
        max: Int,
    ): Int {
        return random(random, min, max)
    }

    fun getDistance(
        coordX1: Int,
        coordY1: Int,
        coordX2: Int,
        coordY2: Int,
    ): Int {
        val deltaX = coordX2 - coordX1
        val deltaY = coordY2 - coordY1
        return (sqrt(deltaX.toDouble().pow(2.0) + deltaY.toDouble().pow(2.0)).toInt())
    }

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

    private val DECIMAL_FORMAT = DecimalFormat(".###")

    fun format(number: Int): String {
        return NumberFormat.getNumberInstance().format(number.toLong())
    }

    fun format(number: Long): String {
        return NumberFormat.getNumberInstance().format(number)
    }

    fun format(number: Float): String {
        return NumberFormat.getNumberInstance().format(number.toDouble())
    }

    fun equals(
        a: Any?,
        b: Any,
    ): Boolean {
        return (a === b) || (a != null && a == b)
    }

    fun round(
        value: Double,
        places: Int,
    ): Double {
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
