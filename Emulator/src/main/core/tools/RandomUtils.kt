package core.tools

import kotlin.random.Random

/**
 * Utility object for generating random values.
 */
object RandomUtils {
    /**
     * Default random instance.
     */
    val random: Random = Random.Default

    /**
     * Returns a random integer between 0 and [i] (inclusive) using the provided [random].
     */
    fun random(random: Random, i: Int): Int = random.nextInt(i + 1)

    /**
     * Returns a random integer between 0 and [i] (inclusive) using the default [random].
     */
    fun random(i: Int): Int = random(random, i)

    /**
     * Returns a random double between 0.0 and 1.0 using the provided [random].
     */
    fun randomDouble(random: Random): Double = random.nextDouble()

    /**
     * Returns a random double between 0.0 and 1.0 using the default [random].
     */
    fun randomDouble(): Double = randomDouble(random)

    /**
     * Selects and returns a random element from the given [options] list.
     */
    fun <T> randomChoice(options: List<T>): T = options.random()
}
