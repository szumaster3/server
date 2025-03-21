package core.game.system

/**
 * Enum representing the different states of the system.
 */
enum class SystemState {

    /**
     * The system is active and running normally.
     */
    ACTIVE,

    /**
     * The system is in an update state. During this state, certain actions might be restricted.
     */
    UPDATING,

    /**
     * The system is in a private state, possibly for development or testing purposes.
     */
    PRIVATE,

    /**
     * The system has been terminated and is no longer operational.
     */
    TERMINATED,
}