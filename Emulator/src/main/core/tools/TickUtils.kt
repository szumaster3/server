package core.tools

const val tick = 600
const val second = 1000
const val cycle = 20

fun secondsToTicks(seconds: Int): Int {
    val seconds = seconds * second
    return seconds / tick
}

fun ticksToSeconds(ticks: Int): Int {
    val ticksMs = ticks * tick
    return ticksMs / 1000
}

fun cyclesToTicks(cycles: Int): Int {
    val cyclesPerTick = tick / cycle
    return kotlin.math.ceil(cycles / cyclesPerTick.toDouble()).toInt()
}

fun ticksToCycles(ticks: Int): Int {
    return ticks * (tick / cycle)
}

fun minutesToTicks(minutes: Int): Int {
    val minutesMs = minutes * 60 * 1000
    return minutesMs / tick
}

fun minutesToTicks(minutes: Double): Int {
    val minutesMs = (minutes * 60 * 1000).toInt()
    return minutesMs / tick
}

fun ticksToMinutes(ticks: Int): Int {
    val ticksMs = ticks * tick
    return ticksMs / 1000 / 60
}

const val ticksPerSecond = second / tick
const val ticksPerMinute = 60 * ticksPerSecond
