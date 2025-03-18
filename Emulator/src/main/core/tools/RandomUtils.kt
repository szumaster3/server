package core.tools

import kotlin.random.Random

object RandomUtils {
    val random: Random = Random.Default

    fun random(
        random: Random,
        i: Int,
    ): Int = random.nextInt(i + 1)

    fun random(i: Int): Int = random(random, i)

    fun randomDouble(random: Random): Double = random.nextDouble()

    fun randomDouble(): Double = randomDouble(random)

    fun <T> randomChoice(options: List<T>): T = options.random()
}
