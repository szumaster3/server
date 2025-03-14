package core.api.utils

import core.game.world.map.Direction
import core.game.world.map.Location
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * A class representing a 2D vector with x and y components.
 *
 * @property x The x component of the vector.
 * @property y The y component of the vector.
 */
class Vector(
    val x: Double,
    val y: Double,
) {
    /**
     * Returns a new normalized vector (a unit vector) with the same direction as the current vector.
     * The normalized vector has a magnitude of 1.
     *
     * @return A normalized vector.
     */
    fun normalized(): Vector {
        val magnitude = magnitude()
        val xComponent = x / magnitude
        val yComponent = y / magnitude
        return Vector(xComponent, yComponent)
    }

    /**
     * Returns the magnitude (length) of the vector.
     * The magnitude is calculated as sqrt(x^2 + y^2).
     *
     * @return The magnitude of the vector.
     */
    fun magnitude(): Double {
        return sqrt(x.pow(2.0) + y.pow(2.0))
    }

    /**
     * Unary minus operator. Negates both components of the vector.
     *
     * @return A new vector with negated components.
     */
    operator fun Vector.unaryMinus() = Vector(-x, -y)

    /**
     * Multiplies the vector by a scalar value (Double).
     *
     * @param other The scalar value to multiply the vector with.
     * @return A new vector resulting from the multiplication.
     */
    operator fun times(other: Double): Vector {
        return Vector(this.x * other, this.y * other)
    }

    /**
     * Multiplies the vector by a scalar value (Int).
     *
     * @param other The scalar value to multiply the vector with.
     * @return A new vector resulting from the multiplication.
     */
    operator fun times(other: Int): Vector {
        return Vector(this.x * other, this.y * other)
    }

    /**
     * Adds another vector to the current vector.
     *
     * @param other The vector to add.
     * @return A new vector resulting from the addition.
     */
    operator fun plus(other: Vector): Vector {
        return Vector(this.x + other.x, this.y + other.y)
    }

    /**
     * Subtracts another vector from the current vector.
     *
     * @param other The vector to subtract.
     * @return A new vector resulting from the subtraction.
     */
    operator fun minus(other: Vector): Vector {
        return Vector(this.x - other.x, this.y - other.y)
    }

    /**
     * Returns a string representation of the vector in the form "{x, y}".
     *
     * @return A string representing the vector.
     */
    override fun toString(): String {
        return "{$x,$y}"
    }

    /**
     * Inverts the vector by negating both components.
     *
     * @return A new vector with the negated components.
     */
    fun invert(): Vector {
        return -this
    }

    /**
     * Converts the vector to a [Location] object. The x and y components are floored to integers,
     * and the plane is set to the given value (default is 0).
     *
     * @param plane The plane value to be used in the Location (default is 0).
     * @return A Location object representing the vector's position.
     */
    fun toLocation(plane: Int = 0): Location {
        return Location.create(floor(x).toInt(), floor(y).toInt(), plane)
    }

    /**
     * Converts the vector to a [Direction] based on its normalized direction.
     * The result is one of the 8 possible cardinal or diagonal directions.
     *
     * @return The corresponding direction based on the normalized vector's direction.
     */
    fun toDirection(): Direction {
        val norm = normalized()

        if (norm.x >= 0.85) {
            return Direction.EAST
        } else if (norm.x <= -0.85) {
            return Direction.WEST
        }

        if (norm.y > 0) {
            if (norm.y >= 0.85) return Direction.NORTH
            return if (norm.x > 0) Direction.NORTH_EAST else Direction.NORTH_WEST
        } else {
            if (norm.y <= -0.85) return Direction.SOUTH
            return if (norm.x > 0) Direction.SOUTH_EAST else Direction.SOUTH_WEST
        }
    }

    companion object {
        /**
         * Creates a vector representing the difference between two locations.
         *
         * @param from The starting location.
         * @param to The destination location.
         * @return A vector representing the difference between the two locations.
         */
        @JvmStatic
        fun betweenLocs(
            from: Location,
            to: Location,
        ): Vector {
            val xDiff = to.x - from.x
            val yDiff = to.y - from.y
            return Vector(xDiff.toDouble(), yDiff.toDouble())
        }

        /**
         * Derives a vector with equal components such that its magnitude is equal to the specified value.
         *
         * @param magnitude The desired magnitude of the vector.
         * @return A vector with equal x and y components that result in the specified magnitude.
         */
        @JvmStatic
        fun deriveWithEqualComponents(magnitude: Double): Vector {
            var sideLength = sqrt(magnitude.pow(2.0) / 2)
            return Vector(sideLength, sideLength)
        }
    }
}
