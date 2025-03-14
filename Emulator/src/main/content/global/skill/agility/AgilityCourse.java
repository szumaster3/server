package content.global.skill.agility;

import core.game.interaction.OptionHandler;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.plugin.Plugin;

/**
 * The type Agility course.
 */
public abstract class AgilityCourse extends OptionHandler {
    private final Player player;
    private final boolean[] obstaclesPassed;
    private final double completionExperience;

    /**
     * Instantiates a new Agility course.
     *
     * @param player               the player
     * @param size                 the size
     * @param completionExperience the completion experience
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
     * Configure.
     */
    public abstract void configure();

    /**
     * Create instance agility course.
     *
     * @param player the player
     * @return the agility course
     */
    public abstract AgilityCourse createInstance(Player player);

    /**
     * Gets course.
     *
     * @param player the player
     * @return the course
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
     * Finish.
     */
    public void finish() {
        if (isCompleted()) {
            player.getSkills().addExperience(Skills.AGILITY, completionExperience, true);
        }
        reset();
    }

    /**
     * Is completed boolean.
     *
     * @return the boolean
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
     * Reset.
     */
    public void reset() {
        for (int i = 0; i < obstaclesPassed.length; i++) {
            obstaclesPassed[i] = false;
        }
    }

    /**
     * Flag.
     *
     * @param index the index
     */
    public void flag(int index) {
        obstaclesPassed[index] = true;
        if (index == obstaclesPassed.length - 1) {
            finish();
        }
    }

    /**
     * Gets hit amount.
     *
     * @param player the player
     * @return the hit amount
     */
    protected static int getHitAmount(Player player) {
        int hit = player.getSkills().getLifepoints() / 12;
        if (hit < 2) {
            hit = 2;
        }
        return hit;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get passed obstacles boolean [ ].
     *
     * @return the boolean [ ]
     */
    public boolean[] getPassedObstacles() {
        return obstaclesPassed;
    }

}