package core.tools

import kotlin.random.Random

object RandomUtils {
    val random: Random = Random.Default

    fun random(
        random: Random,
        i: Int,
    ): Int {
        return random.nextInt(i + 1)
    }

    fun random(i: Int): Int {
        return random(random, i)
    }

    fun randomDouble(random: Random): Double {
        return random.nextDouble()
    }

    fun randomDouble(): Double {
        return randomDouble(random)
    }

    fun <T> randomChoice(options: List<T>): T = options.random()
}
