package core.game.world.update.flag

import core.api.logWithStack
import core.game.node.entity.Entity
import core.net.packet.IoBuffer
import core.tools.Log
import kotlin.reflect.KType

enum class EFlagType {
    Player,
    NPC,
    ;

    companion object {
        @JvmStatic
        fun of(e: Entity): EFlagType {
            return if (e is core.game.node.entity.player.Player) Player else NPC
        }
    }
}

open class EFlagProvider(
    val revision: Int,
    val type: EFlagType,
    val presenceFlag: Int,
    val ordinal: Int,
    val flag: EntityFlag,
) {
    open fun writeTo(
        buffer: IoBuffer,
        context: Any?,
    ) {}

    open fun writeToDynamic(
        buffer: IoBuffer,
        context: Any?,
        e: Entity,
    ) {
        writeTo(buffer, context)
    }

    fun logInvalidType(
        context: Any?,
        expected: KType,
    ) {
        logWithStack(
            this::class.java,
            Log.ERR,
            "Invalid context of type ${context?.let { it::class.java.simpleName } ?: "null"} passed to ${this::class.simpleName} flag which expects $expected.",
        )
    }
}
