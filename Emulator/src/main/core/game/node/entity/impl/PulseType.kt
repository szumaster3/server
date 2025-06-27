package core.game.node.entity.impl

/**
 * Represents different types of pulses.
 */
enum class PulseType {
    /**
     * Standard pulse type.
     * This pulse can be interrupted or replaced by most other pulses.
     */
    STANDARD,

    /**
     * Strong pulse type.
     * This pulse type enforces itself as the only one that can run at the same time.
     */
    STRONG,

    /**
     * Custom pulse type 1.
     * A custom slot for extra tasks that should run alongside standard tasks.
     */
    CUSTOM_1,

    /**
     * Custom pulse type 2.
     * Another custom slot for additional tasks to run alongside standard tasks.
     */
    CUSTOM_2
}
