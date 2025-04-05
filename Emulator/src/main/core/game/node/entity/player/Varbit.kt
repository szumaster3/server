package core.game.node.entity.player

/**
 * Represents a Varbit (variable bit) used to store a specific value in a player's state.
 *
 * @property value The current value of the varbit.
 * @property offset The offset or position of the varbit.
 */
class Varbit(
    var value: Int,
    val offset: Int,
)
