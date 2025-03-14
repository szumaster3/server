package core.api

import core.game.node.item.Item
import java.util.*

/**
 * Converts an [IntProgression] to an [IntArray].
 *
 * @return A new [IntArray] containing the values of the [IntProgression].
 */
fun IntProgression.toIntArray(): IntArray {
    val result = IntArray((last - first) / step + 1)
    forEachIndexed { index, i -> result[index] = i }
    return result
}

/**
 * Converts an [Int] to an [Item] object.
 *
 * @return A new [Item] initialized with the given [Int].
 */
fun Int.asItem(): Item {
    return Item(this)
}

/**
 * Converts a collection of [IntArray] to a single [IntArray].
 *
 * @return A new [IntArray] that contains all the elements from the collection of [IntArray].
 */
fun Collection<IntArray>.toIntArray(): IntArray {
    val list = ArrayList<Int>()
    this.forEach { arr -> arr.forEach { list.add(it) } }
    return list.toIntArray()
}

/**
 * Checks if the specified [element] is the last element in the collection.
 *
 * @param element The element to check.
 * @return `true` if the [element] is the last in the collection, otherwise `false`.
 */
inline fun <reified T> Collection<T>.isLast(element: T): Boolean {
    return this.indexOf(element) == this.size - 1
}

/**
 * Gets the next element after the specified [element] in the collection.
 *
 * @param element The current element to check.
 * @return The next element if it exists, otherwise returns the same [element].
 */
inline fun <reified T> Collection<T>.getNext(element: T): T {
    val idx = this.indexOf(element)
    return if (idx < this.size - 1) {
        this.elementAt(idx + 1)
    } else {
        element
    }
}

/**
 * Checks if the next element after the specified [element] is the last element in the collection.
 *
 * @param element The element to check.
 * @return `true` if the next element is the last in the collection, otherwise `false`.
 */
inline fun <reified T> Collection<T>.isNextLast(element: T): Boolean {
    return this.isLast(this.getNext(element))
}

/**
 * Checks if the specified [element] is the last element in the array.
 *
 * @param element The element to check.
 * @return `true` if the [element] is the last in the array, otherwise `false`.
 */
fun IntArray.isLast(element: Int): Boolean {
    return this.indexOf(element) == this.size - 1
}

/**
 * Gets the next element after the specified [element] in the array.
 *
 * @param element The current element to check.
 * @return The next element if it exists, otherwise returns the same [element].
 */
fun IntArray.getNext(element: Int): Int {
    val idx = this.indexOf(element)
    return if (idx < this.size - 1) {
        this.elementAt(idx + 1)
    } else {
        element
    }
}

/**
 * Checks if the next element after the specified [element] is the last element in the array.
 *
 * @param element The element to check.
 * @return `true` if the next element is the last in the array, otherwise `false`.
 */
fun IntArray.isNextLast(element: Int): Boolean {
    return this.isLast(this.getNext(element))
}

/**
 * Attempts to pop an element from the [LinkedList], returning the default value if the list is empty.
 *
 * @param default The default value to return if the list is empty.
 * @return The popped element or the [default] value if the list is empty.
 */
fun <T> LinkedList<T>.tryPop(default: T?): T? {
    this.peek() ?: return default
    return this.pop()
}

/**
 * Attempts to parse an enum entry from a string [name].
 *
 * @param name The name of the enum entry to parse.
 * @return The enum entry if found, otherwise `null`.
 */
inline fun <reified E : Enum<E>> parseEnumEntry(name: String): E? {
    return try {
        enumValueOf<E>(name)
    } catch (e: Exception) {
        null
    }
}
