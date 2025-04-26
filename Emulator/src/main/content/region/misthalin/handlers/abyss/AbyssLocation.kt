package content.region.misthalin.handlers.abyss

import core.game.world.map.Location
import core.game.world.map.RegionManager
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Represents a location within the Abyss using polar coordinates.
 *
 * @property radius The radial distance from the Abyss center.
 * @property angle The angular coordinate around the Abyss center (in radians).
 */
class AbyssLocation(
    val radius: Double,
    val angle: Double,
) {
    /**
     * Moves the location closer to the Abyss center by a number of steps.
     *
     * @param steps The number of radius units to decrease.
     * @return A new [AbyssLocation] closer to the center.
     */
    fun attract(steps: Int = 1): AbyssLocation = AbyssLocation(radius - steps.toDouble(), angle)

    /**
     * Computes the obstacle segment ID based on the current angular position.
     *
     * @return An integer representing the segment (0-11) for obstacle configuration.
     */
    fun getSegment(): Int {
        val segments = 12
        val angleToCircle = angle * segments / (2 * Math.PI)
        val angleSegment = (angleToCircle + 0.5).toInt()

        val normalSegment = (9 - angleSegment).mod(12)
        return normalSegment
    }

    /**
     * Converts this [AbyssLocation] to an absolute [Location] based on the origin.
     *
     * @return The absolute [Location] represented by this [AbyssLocation].
     */
    fun toAbs(): Location {
        val x = (radius * cos(angle)).toInt()
        val y = (radius * sin(angle)).toInt()
        return origin.transform(x, y, 0)
    }

    /**
     * Checks if this [AbyssLocation] is valid for teleportation.
     *
     * A location is valid if teleportation is permitted at that spot
     * and there is no blocking object present.
     *
     * @return `true` if the location is valid; `false` otherwise.
     */
    fun isValid(): Boolean {
        val abs = toAbs()
        return (RegionManager.isTeleportPermitted(abs) && RegionManager.getObject(abs) == null)
    }

    companion object {
        val origin = Location(3039, 4832, 0)

        const val outerRadius = 25.1

        /**
         * Creates an [AbyssLocation] from an absolute [Location].
         *
         * Calculates the radius and angle relative to the [origin].
         *
         * @param loc The absolute [Location] to convert.
         * @return The corresponding [AbyssLocation].
         */
        fun fromAbs(loc: Location): AbyssLocation {
            val local = Location.getDelta(origin, loc)
            val radius = Math.sqrt((local.x * local.x + local.y * local.y).toDouble())
            val angle = Math.atan2(local.y.toDouble(), local.x.toDouble())
            return AbyssLocation(radius, angle)
        }

        /**
         * Generates a random [AbyssLocation] on the outer edge of the abyss.
         *
         * @return A new [AbyssLocation] with a random angle at the outer radius.
         */
        fun randomLocation(): AbyssLocation {
            val angle = Random.nextDouble() * 2 * Math.PI
            return AbyssLocation(outerRadius, angle)
        }
    }
}