package core.plugin.type;

import core.plugin.Plugin;

/**
 * The type Startup plugin.
 */
public abstract class StartupPlugin implements Plugin<Object> {

    /**
     * Run.
     */
    public abstract void run();
}
