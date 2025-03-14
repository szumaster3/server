package core.game.world.update.flag.context;

import core.cache.def.impl.AnimationDefinition;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.scenery.Scenery;

/**
 * The type Animation.
 */
public class Animation {

    /**
     * The constant RESET.
     */
    public static final Animation RESET = new Animation(-1, Priority.VERY_HIGH);

    private Priority priority;

    private int id;

    private final int delay;

    private AnimationDefinition definition;

    private Scenery scenery;

    /**
     * Instantiates a new Animation.
     *
     * @param id the id
     */
    public Animation(int id) {
        this(id, 0, Priority.MID);
    }

    /**
     * Create animation.
     *
     * @param id the id
     * @return the animation
     */
    public static Animation create(int id) {
        return new Animation(id, 0, Priority.MID);
    }

    /**
     * Instantiates a new Animation.
     *
     * @param id       the id
     * @param priority the priority
     */
    public Animation(int id, Priority priority) {
        this(id, 0, priority);
    }

    /**
     * Instantiates a new Animation.
     *
     * @param id    the id
     * @param delay the delay
     */
    public Animation(int id, int delay) {
        this(id, delay, Priority.MID);
    }

    /**
     * Instantiates a new Animation.
     *
     * @param id       the id
     * @param delay    the delay
     * @param priority the priority
     */
    public Animation(int id, int delay, Priority priority) {
        this.id = id;
        this.delay = delay;
        this.priority = priority;
    }

    /**
     * Gets definition.
     *
     * @return the definition
     */
    public AnimationDefinition getDefinition() {
        if (definition == null) {
            definition = AnimationDefinition.forId(id);
        }
        return definition;
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public int getDuration() {
        AnimationDefinition def = getDefinition();
        return def != null ? def.getDurationTicks() : 1;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets delay.
     *
     * @return the delay
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets priority.
     *
     * @return the priority
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Gets object.
     *
     * @return the object
     */
    public Scenery getObject() {
        return scenery;
    }

    /**
     * Sets object.
     *
     * @param scenery the scenery
     */
    public void setObject(Scenery scenery) {
        this.scenery = scenery;
    }

    /**
     * Sets priority.
     *
     * @param priority the priority
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Animation [priority=" + priority + ", id=" + id + "]";
    }
}