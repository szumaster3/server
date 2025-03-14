package core.game.world.update.flag

import core.game.node.entity.Entity
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.game.world.update.flag.context.HitMark
import core.net.packet.IoBuffer
import kotlin.reflect.typeOf

sealed class NPCFlags(
    p: Int,
    o: Int,
    f: EntityFlag,
) : EFlagProvider(530, EFlagType.NPC, p, o, f) {
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

    class AnimationSequence : NPCFlags(0x100, 7, EntityFlag.AnimSeq) {
        // TODO: Implement this flag's functionality.
    }

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
