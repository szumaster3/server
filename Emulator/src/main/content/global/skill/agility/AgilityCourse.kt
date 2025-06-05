package content.global.skill.agility

import core.game.interaction.OptionHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Plugin

/**
 * An abstract base class representing an agility course consisting of multiple obstacles.
 *
 * @author Emperor
 */
abstract class AgilityCourse(
    private val player: Player,
    size: Int,
    private val completionExperience: Double
) : OptionHandler() {

    private val obstaclesPassed = BooleanArray(size)

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        return if (arg == null) {
            configure()
            this
        } else {
            createInstance(arg as Player)
        }
    }

    override fun fireEvent(identifier: String, vararg args: Any?): Any? {
        return null
    }

    /**
     * Performs any course-specific configuration, such as registering obstacle interactions.
     */
    abstract fun configure()

    /**
     * Creates a new instance of the agility course for the given player.
     *
     * @param player the player
     * @return a new instance of the course
     */
    abstract fun createInstance(player: Player): AgilityCourse

    /**
     * Gets the active course instance for the specified player, creating one if necessary.
     *
     * @param player the player
     * @return the agility course instance
     */
    fun getCourse(player: Player): AgilityCourse {
        var course = player.getExtension<AgilityCourse>(AgilityCourse::class.java)
        if (course == null || course::class != this::class) {
            try {
                course = createInstance(player)
                player.addExtension(AgilityCourse::class.java, course)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return course
    }

    /**
     * Marks the course as completed if all obstacles were passed and awards experience.
     */
    fun finish() {
        if (isCompleted()) {
            player.skills.addExperience(Skills.AGILITY, completionExperience, true)
        }
        reset()
    }

    /**
     * Checks whether all obstacles in the course have been successfully completed.
     *
     * @return true if the course is complete, false otherwise
     */
    fun isCompleted(): Boolean = obstaclesPassed.all { it }

    /**
     * Resets all obstacle progress for this course.
     */
    fun reset() {
        obstaclesPassed.fill(false)
    }

    /**
     * Flags a specific obstacle as completed. If it's the final obstacle, the course is finished.
     *
     * @param index the index of the completed obstacle
     */
    fun flag(index: Int) {
        obstaclesPassed[index] = true
        if (index == obstaclesPassed.lastIndex) {
            finish()
        }
    }

    companion object {
        /**
         * Calculates the damage to take when failing an obstacle, based on the player's health.
         *
         * @param player the player
         * @return the damage amount
         */
        fun getHitAmount(player: Player): Int {
            val hit = player.skills.lifepoints / 12
            return maxOf(hit, 2)
        }
    }

    /**
     * Returns the player associated with this course.
     */
    fun getPlayer(): Player = player

    /**
     * Returns the array indicating which obstacles have been passed.
     */
    fun getPassedObstacles(): BooleanArray = obstaclesPassed
}
