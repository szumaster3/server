package core.game.node.entity.state;

import core.game.node.entity.Entity;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;

import java.nio.ByteBuffer;

/**
 * The type State pulse.
 */
public abstract class StatePulse extends Pulse {

    /**
     * The Entity.
     */
    protected final Entity entity;

    /**
     * Instantiates a new State pulse.
     *
     * @param entity the entity
     * @param ticks  the ticks
     */
    public StatePulse(Entity entity, int ticks) {
        super(ticks, entity);
        super.stop();
        this.entity = entity;
    }

    /**
     * Is save required boolean.
     *
     * @return the boolean
     */
    public abstract boolean isSaveRequired();

    /**
     * Save.
     *
     * @param buffer the buffer
     */
    public abstract void save(ByteBuffer buffer);

    /**
     * Parse state pulse.
     *
     * @param entity the entity
     * @param buffer the buffer
     * @return the state pulse
     */
    public abstract StatePulse parse(Entity entity, ByteBuffer buffer);

    /**
     * Create state pulse.
     *
     * @param entity the entity
     * @param args   the args
     * @return the state pulse
     */
    public abstract StatePulse create(Entity entity, Object... args);

    /**
     * Can run boolean.
     *
     * @param entity the entity
     * @return the boolean
     */
    public boolean canRun(Entity entity) {
        return true;
    }

    /**
     * Remove.
     */
    public void remove() {

    }

    public void run() {
        if (isRunning()) {
            return;
        }
        restart();
        start();
        GameWorld.getPulser().submit(this);
    }

}