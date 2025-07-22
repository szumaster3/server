package core.game.container

import core.api.Container

/**
 * The sort type of the container.
 *
 * @author Emperor
 */
data class Slot(
    /**
     * Sort by item id (default).
     */
    val id: Int,
    /**
     * Sort by item identification hash (bank).
     */
    val type: Container,
)
