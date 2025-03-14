package core.game.world.update

import core.api.logWithStack
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.world.update.flag.EFlagProvider
import core.game.world.update.flag.EFlagType
import core.game.world.update.flag.EntityFlag
import core.game.world.update.flag.EntityFlags
import core.game.world.update.flag.context.HitMark
import core.net.packet.IoBuffer
import core.tools.Log
import java.util.concurrent.atomic.AtomicBoolean

class UpdateMasks(
    val owner: Entity,
) {
    var appearanceStamp: Long = 0

    private val type = EFlagType.of(owner) // The flag type of the entity
    private val updating = AtomicBoolean() // Flag to track if updates are in progress
    private var presenceFlags = 0 // The presence flags indicating which updates are active
    private var syncedPresenceFlags = 0 // Synced presence flags for synchronized updates
    private val elements = arrayOfNulls<MaskElement?>(SIZE) // Array holding dynamic update elements
    private val syncedElements = arrayOfNulls<MaskElement?>(SIZE) // Array holding synced update elements

    // Data class for storing the encoder and context of each flag element
    private data class MaskElement(
        val encoder: EFlagProvider,
        val context: Any?,
    )

    @JvmOverloads
    fun register(
        flag: EntityFlag,
        context: Any?,
        sync: Boolean = false,
    ): Boolean {
        var synced = sync
        val provider =
            EntityFlags.getFlagFor(type, flag) ?: run {
                logWithStack(
                    this::class.java,
                    Log.ERR,
                    "Tried to use flag ${flag.name} which is not available for ${type.name} in this revision.",
                )
                return false
            }

        if (updating.get()) return false

        // Handle appearance updates
        if (flag == EntityFlag.Appearance) {
            appearanceStamp = System.currentTimeMillis()
            synced = true
        }

        // Register synced flags if applicable
        if (synced) {
            syncedElements[provider.ordinal] = MaskElement(provider, context)
            syncedPresenceFlags = syncedPresenceFlags or provider.presenceFlag
        }

        // Register normal flags
        elements[provider.ordinal] = MaskElement(provider, context)
        presenceFlags = presenceFlags or provider.presenceFlag

        return true
    }

    fun unregisterSynced(ordinal: Int): Boolean {
        if (syncedElements[ordinal] != null) {
            syncedPresenceFlags = syncedPresenceFlags and syncedElements[ordinal]!!.encoder.presenceFlag.inv()
            syncedElements[ordinal] = null
            return true
        }
        return false
    }

    fun write(
        p: Player?,
        e: Entity?,
        buffer: IoBuffer,
    ) {
        var maskData = presenceFlags
        if (maskData >= 0x100) {
            maskData = maskData or if (e is Player) 0x10 else 0x8
            buffer.put(maskData).put(maskData shr 8)
        } else {
            buffer.put(maskData)
        }
        // Write each mask element
        for (i in elements.indices) {
            val element = elements[i]
            element?.encoder?.writeToDynamic(buffer, element.context, p!!)
        }
    }

    fun writeSynced(
        p: Player?,
        e: Entity?,
        buffer: IoBuffer,
        appearance: Boolean,
    ) {
        var maskData = presenceFlags
        var synced = syncedPresenceFlags
        var appearanceFlag = EntityFlags.getPresenceFlag(type, EntityFlag.Appearance)
        if (!appearance && synced and appearanceFlag != 0) {
            synced = synced and appearanceFlag.inv()
        }
        maskData = maskData or synced
        if (maskData >= 0x100) {
            maskData = maskData or if (e is Player) 0x10 else 0x8
            buffer.put(maskData).put(maskData shr 8)
        } else {
            buffer.put(maskData)
        }
        for (i in elements.indices) {
            var element = elements[i]
            if (element == null) {
                element = syncedElements[i]
                if (!appearance && element != null && element.encoder.flag == EntityFlag.Appearance) {
                    continue
                }
            }
            element?.encoder?.writeToDynamic(buffer, element.context, p!!)
        }
    }

    fun prepare(entity: Entity) {
        val handler = entity.impactHandler
        for (i in 0..1) {
            if (handler.impactQueue.peek() == null) {
                break
            }
            val impact = handler.impactQueue.poll()
            registerHitUpdate(entity, impact, i == 1)
        }
        updating.set(true)
    }

    private fun registerHitUpdate(
        e: Entity,
        impact: ImpactHandler.Impact,
        secondary: Boolean,
    ): HitMark {
        val mark = HitMark(impact.amount, impact.type.ordinal, e)
        register(if (secondary) EntityFlag.SecondaryHit else EntityFlag.PrimaryHit, mark)
        return mark
    }

    fun reset() {
        for (i in elements.indices) {
            elements[i] = null
        }
        presenceFlags = 0
        updating.set(false)
    }

    fun isUpdating(): Boolean {
        return updating.get()
    }

    val isUpdateRequired: Boolean
        get() = presenceFlags != 0

    fun hasSynced(): Boolean {
        return syncedPresenceFlags != 0
    }

    companion object {
        const val SIZE = 11
    }
}
