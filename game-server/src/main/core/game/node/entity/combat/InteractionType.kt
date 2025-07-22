package core.game.node.entity.combat

/**
 * The interaction types for combat.
 *
 * @author Emperor
 */
enum class InteractionType {
    /**
     * The entity can hit while standing still.
     */
    STILL_INTERACT,

    /**
     * The entity can hit while following the victim.
     */
    MOVE_INTERACT,

    /**
     * The entity can't hit its target.
     */
    NO_INTERACT
}