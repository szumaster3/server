package core.game.system.command

/**
 * Represents different privilege levels for players.
 */
enum class Privilege {
    /**
     * Standard player privilege level.
     */
    STANDARD,

    /**
     * Moderator privilege level with elevated permissions.
     */
    MODERATOR,

    /**
     * Administrator privilege level with full permissions.
     */
    ADMIN,
}
