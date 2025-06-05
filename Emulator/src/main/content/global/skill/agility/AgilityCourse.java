package content.global.skill.agility;

import core.game.interaction.OptionHandler;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.plugin.Plugin;

/**
 * An abstract base class representing an agility course consisting of multiple obstacles.
 *
 * @author Emperor
 */
public abstract class AgilityCourse extends OptionHandler {
    private final Player player;
    private final boolean[] obstaclesPassed;
    private final double completionExperience;

    /**
     * Constructs a new {@code AgilityCourse}.
     *
     * @param player               the player completing the course
     * @param size                 the number of obstacles in the course
     * @param completionExperience the amount of Agility experience awarded upon completion
     */
    public AgilityCourse(Player player, int size, double completionExperience) {
        this.player = player;
        this.obstaclesPassed = new boolean[size];
        this.completionExperience = completionExperience;
    }

    @Override
    public final Plugin<Object> newInstance(Object arg) throws Throwable {
        if (arg == null) {
            configure();
            return this;
        }
        return createInstance((Player) arg);
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Performs any course-specific configuration, such as registering obstacle interactions.
     */
    public abstract void configure();

    /**
     * Creates a new instance of the agility course for the given player.
     *
     * @param player the player
     * @return a new instance of the course
     */
    public abstract AgilityCourse createInstance(Player player);

    /**
     * Gets the active course instance for the specified player, creating one if necessary.
     *
     * @param player the player
     * @return the agility course instance
     */
    public AgilityCourse getCourse(Player player) {
        AgilityCourse course = player.getExtension(AgilityCourse.class);
        if (course == null || course.getClass() != getClass()) {
            try {
                player.addExtension(AgilityCourse.class, course = createInstance(player));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return course;
    }

    /**
     * Marks the course as completed if all obstacles were passed and awards experience.
     */
    public void finish() {
        if (isCompleted()) {
            player.getSkills().addExperience(Skills.AGILITY, completionExperience, true);
        }
        reset();
    }

    /**
     * Checks whether all obstacles in the course have been successfully completed.
     *
     * @return {@code true} if the course is complete, {@code false} otherwise
     */
    public boolean isCompleted() {
        for (boolean b : obstaclesPassed) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    /**
     * Resets all obstacle progress for this course.
     */
    public void reset() {
        for (int i = 0; i < obstaclesPassed.length; i++) {
            obstaclesPassed[i] = false;
        }
    }

    /**
     * Flags a specific obstacle as completed. If it's the final obstacle, the course is finished.
     *
     * @param index the index of the completed obstacle
     */
    public void flag(int index) {
        obstaclesPassed[index] = true;
        if (index == obstaclesPassed.length - 1) {
            finish();
        }
    }

    /**
     * Calculates the damage to take when failing an obstacle, based on the player's health.
     *
     * @param player the player
     * @return the damage amount
     */
    protected static int getHitAmount(Player player) {
        int hit = player.getSkills().getLifepoints() / 12;
        return Math.max(hit, 2);
    }

    /**
     * Returns the player associated with this course.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the array indicating which obstacles have been passed.
     *
     * @return a boolean array of passed obstacle flags
     */
    public boolean[] getPassedObstacles() {
        return obstaclesPassed;
    }
}