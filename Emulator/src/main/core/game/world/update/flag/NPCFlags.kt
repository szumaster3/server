package core.game.world.update.flag

import core.ServerConstants
import core.game.node.entity.Entity
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.game.world.update.flag.context.HitMark
import core.net.packet.IoBuffer
import kotlin.reflect.typeOf

/**
 * Flags specific to NPC entities for update packets.
 *
 * @param p Presence flag bitmask
 * @param o Ordinal for ordering flags
 * @param f Corresponding entity flag type
 */
sealed class NPCFlags(
    p: Int,
    o: Int,
    f: EntityFlag,
) : EFlagProvider(ServerConstants.REVISION, EFlagType.NPC, p, o, f) {

    /**
     * Represents the primary hit update flag.
     * Expects [HitMark] context containing damage and hit type.
     */
    class PrimaryHit : NPCFlags(0x40, 0, EntityFlag.PrimaryHit) {
        override fun writeTo(
            buffer: IoBuffer,
            context: Any?,
        ) {
            if (context !is HitMark) {
                logInvalidType(context, typeOf<HitMark>())
                return
            }
            buffer.p1(context.damage)
            buffer.p1neg(context.type)

            val e = context.entity
            var ratio = 255
            val max = e.skills.maximumLifepoints
            if (e.skills.lifepoints < max) {
                ratio = e.skills.lifepoints * 255 / max
            }
            buffer.p1sub(ratio)
        }
    }

    /**
     * Represents the secondary hit update flag.
     * Expects [HitMark] context.
     */
    class SecondaryHit : NPCFlags(0x2, 1, EntityFlag.SecondaryHit) {
        override fun writeTo(
            buffer: IoBuffer,
            context: Any?,
        ) {
            if (context !is HitMark) {
                logInvalidType(context, typeOf<HitMark>())
                return
            }
            buffer.p1neg(context.damage)
            buffer.p1sub(context.type)
        }
    }

    /**
     * Represents animation update flag.
     * Expects [Animation] context.
     */
    class Animate : NPCFlags(0x10, 2, EntityFlag.Animate) {
        override fun writeTo(
            buffer: IoBuffer,
            context: Any?,
        ) {
            if (context !is Animation) {
                logInvalidType(context, typeOf<Animation>())
                return
            }
            buffer.p2(context.id)
            buffer.p1(context.delay)
        }
    }

    /**
     * Flag for facing another entity.
     * Context can be [Entity] or null.
     */
    class FaceEntity : NPCFlags(0x4, 3, EntityFlag.FaceEntity) {
        override fun writeTo(
            buffer: IoBuffer,
            context: Any?,
        ) {
            if (context !is Entity?) {
                logInvalidType(context, typeOf<Entity>())
                return
            }
            if (context == null) {
                buffer.p2add(-1)
            } else {
                buffer.p2add(context.clientIndex)
            }
        }
    }

    /**
     * Spot animation flag.
     * Expects [Graphics] context.
     */
    class SpotAnim : NPCFlags(0x80, 4, EntityFlag.SpotAnim) {
        override fun writeTo(
            buffer: IoBuffer,
            context: Any?,
        ) {
            if (context !is Graphics) {
                logInvalidType(context, typeOf<Graphics>())
                return
            }
            buffer.p2add(context.id)
            buffer.ip4((context.height shl 16) + context.delay)
        }
    }

    /**
     * Type swap flag.
     * Expects an [Int] context representing new NPC type ID.
     */
    class TypeSwap : NPCFlags(0x1, 5, EntityFlag.TypeSwap) {
        override fun writeTo(
            buffer: IoBuffer,
            context: Any?,
        ) {
            if (context !is Int) {
                logInvalidType(context, typeOf<Graphics>())
                return
            }
            buffer.ip2(context)
        }
    }

    /**
     * Force chat update flag.
     * Expects a [String] context containing the forced chat text.
     */
    class ForceChat : NPCFlags(0x20, 6, EntityFlag.ForceChat) {
        override fun writeTo(
            buffer: IoBuffer,
            context: Any?,
        ) {
            if (context !is String) {
                logInvalidType(context, typeOf<String>())
                return
            }
            buffer.putString(context)
        }
    }

    /**
     * Animation sequence flag.
     * Expects [Animation] context.
     */
    class AnimationSequence : NPCFlags(0x100, 7, EntityFlag.AnimSeq) {
        override fun writeTo(
            buffer: IoBuffer,
            context: Any?,
        ) {
            if (context !is Animation) {
                logInvalidType(context, typeOf<Animation>())
                return
            }
            buffer.p2(context.id)
            buffer.p1(context.delay)
        }
    }

    /**
     * Face location flag.
     * Expects [Location] context.
     */
    class FaceLocation : NPCFlags(0x200, 8, EntityFlag.FaceLocation) {
        override fun writeTo(
            buffer: IoBuffer,
            context: Any?,
        ) {
            if (context !is Location) {
                logInvalidType(context, typeOf<Location>())
                return
            }
            buffer.p2add((context.x shl 1) + 1)
            buffer.p2((context.y shl 1) + 1)
        }
    }
}
