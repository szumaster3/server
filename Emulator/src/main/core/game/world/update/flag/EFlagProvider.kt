package core.game.world.update.flag

import core.api.logWithStack
import core.game.node.entity.Entity
import core.net.packet.IoBuffer
import core.tools.Log
import kotlin.reflect.KType

/**
 * Represents the type of entity the update flag applies to.
 */
enum class EFlagType {
    Player,
    NPC,
    ;

    companion object {
        /**
         * Gets the [EFlagType] based on the given [Entity].
         *
         * @param e The entity to evaluate.
         * @return [Player] if [e] is a player, otherwise [NPC].
         */
        @JvmStatic
        fun of(e: Entity): EFlagType = if (e is core.game.node.entity.player.Player) Player else NPC
    }
}

/**
 * Base class for entity update flag providers.
 *
 * @property revision The protocol revision this flag targets.
 * @property type The entity type this flag is for.
 * @property presenceFlag Bitmask indicating flag presence.
 * @property ordinal Unique ordinal index for flag order.
 * @property flag The actual [EntityFlag] this provider represents.
 */
open class EFlagProvider(
    val revision: Int,
    val type: EFlagType,
    val presenceFlag: Int,
    val ordinal: Int,
    val flag: EntityFlag,
) {
    /**
     * Writes the flag data to the provided [IoBuffer].
     * Override this in subclasses to implement actual serialization logic.
     *
     * @param buffer The buffer to write to.
     * @param context Optional context object used during serialization.
     */
    open fun writeTo(
        buffer: IoBuffer,
        context: Any?,
    ) {}

    /**
     * Writes the flag data with entity-specific context.
     *
     * @param buffer The buffer to write to.
     * @param context Optional context object used during serialization.
     * @param e The entity this flag applies to.
     */
    open fun writeToDynamic(
        buffer: IoBuffer,
        context: Any?,
        e: Entity,
    ) {
        writeTo(buffer, context)
    }

    /**
     * Logs an error if an invalid [context] type is passed to the flag.
     *
     * @param context The actual context object received.
     * @param expected The expected Kotlin type for the context.
     */
    fun logInvalidType(
        context: Any?,
        expected: KType,
    ) {
        logWithStack(
            this::class.java,
            Log.ERR,
            "Invalid context of type ${context?.let {
                it::class.java.simpleName
            } ?: "null"} passed to ${this::class.simpleName} flag which expects $expected.",
        )
    }
}