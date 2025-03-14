package core.game.node.entity.impl;

/**
 * Enum representing different types of pulses that can be used within the game system.
 * Pulses are used to control the execution of tasks within the game, with different
 * types enforcing varying levels of priority or behavior.
 */
public enum PulseType {

    /**
     * Standard pulse type. This pulse can be interrupted or replaced by most other pulses.
     * Typically used for regular game tasks that do not require high priority.
     */
    STANDARD,

    /**
     * Strong pulse type. This pulse type enforces itself as the only one that can run at the
     * same time. It cannot be interrupted or replaced by other pulses.
     */
    STRONG,

    /**
     * Custom pulse type 1. A custom slot for extra tasks that should run alongside standard tasks.
     * Typically used for special tasks that do not interfere with standard pulses.
     */
    CUSTOM_1,

    /**
     * Custom pulse type 2. Another custom slot for additional tasks to run alongside standard tasks.
     * Provides flexibility for implementing specific functionality without conflicting with other pulses.
     */
    CUSTOM_2
}
