package core.plugin.type;

import core.plugin.Plugin;

/**
 * The type Manager plugin.
 */
public abstract class ManagerPlugin implements Plugin<Object> {

    /**
     * Tick.
     */
    public abstract void tick();

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }
}
