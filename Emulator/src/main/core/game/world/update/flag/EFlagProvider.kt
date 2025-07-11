package core.game.world.update.flag

import core.api.logWithStack
import core.game.node.entity.Entity
import core.net.packet.IoBuffer
import core.tools.Log
import kotlin.reflect.KType

/** Entity types for update flags. */
enum class EFlagType {
    Player,
    NPC;

    companion object {
        /** Returns [Player] if entity is player, otherwise [NPC]. */
        @JvmStatic
        fun of(e: Entity): EFlagType = if (e is core.game.node.entity.player.Player) Player else NPC
    }
}

/** Base provider for entity update flags. */
open class EFlagProvider(
    val revision: Int,
    val type: EFlagType,
    val presenceFlag: Int,
    val ordinal: Int,
    val flag: EntityFlag,
) {
    /** Writes flag data to [buffer]. Override to implement serialization. */
    open fun writeTo(buffer: IoBuffer, context: Any?) {}

    /** Writes flag data with entity context; defaults to [writeTo]. */
    open fun writeToDynamic(buffer: IoBuffer, context: Any?, e: Entity) {
        writeTo(buffer, context)
    }

    /** Logs error on invalid [context] type passed to the flag. */
    fun logInvalidType(context: Any?, expected: KType) {
        logWithStack(
            this::class.java,
            Log.ERR,
            "Invalid context of type ${context?.let { it::class.java.simpleName } ?: "null"} " +
                    "passed to ${this::class.simpleName} flag which expects $expected."
        )
    }
}
