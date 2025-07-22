package core.api

/**
 * Interface for classes that need to be updated every game tick.
 *
 * This interface is intended for non-player, world-wide tick events. Implementing this interface
 * allows a class to define a `tick()` function that will be called each tick.
 *
 * **Important Notes:**
 * - Do **not** reference non-static, class-local variables in the `tick()` function.
 * - `TickListener` is typically used for world events (e.g., fishing spot rotations,
 *   grand exchange updates, puro puro randomization).
 * - If you need player or entity-specific updates, consider using an [core.game.event.EventHook]
 *   with a [core.game.event.TickEvent].
 */
interface TickListener : ContentInterface {
    /**
     * Called every game tick to update world events or other tick-based logic.
     */
    fun tick()
}
