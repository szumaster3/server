package core.game.world.update.flag

import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.*
import core.net.packet.IoBuffer
import core.tools.StringUtils
import java.nio.charset.StandardCharsets
import kotlin.math.max
import kotlin.reflect.typeOf

sealed class PlayerFlags(
    p: Int,
    o: Int,
    f: EntityFlag,
) : EFlagProvider(530, EFlagType.Player, p, o, f) {
    class Chat : PlayerFlags(0x80, 0, EntityFlag.Chat) {
        override fun writeTo(
            buffer: IoBuffer,
            context: Any?,
        ) {
            if (context !is ChatMessage) {
                logInvalidType(context, typeOf<ChatMessage>())
                return
            }
            buffer.ip2(context.effects)
            if (context.isQuickChat) {
                buffer.p1(4)
            } else {
                buffer.p1(context.chatIcon)
            }
            val chatBuf = ByteArray(256)
            chatBuf[0] = context.text.length.toByte()
            val offset =
                1 +
                    StringUtils.encryptPlayerChat(
                        chatBuf,
                        0,
                        1,
                        context.text.length,
                        context.text.toByteArray(StandardCharsets.UTF_8),
                    )
            buffer.p1(offset + 1)
            buffer.putReverse(chatBuf, 0, offset)
        }
    }

    class PrimaryHit : PlayerFlags(0x1, 1, EntityFlag.PrimaryHit) {
        override fun writeTo(
            buffer: IoBuffer,
            context: Any?,
        ) {
            if (context !is HitMark) {
                logInvalidType(context, typeOf<HitMark>())
                return
            }
            buffer.psmarts(context.damage)
            buffer.p1add(context.type)

            var ratio = 255
            val e = context.entity
            val max = e.skills.maximumLifepoints
            if (max > e.skills.lifepoints) {
                ratio = e.skills.lifepoints * 255 / max
            }
            buffer.p1sub(ratio)
        }
    }

    class Animate : PlayerFlags(0x8, 2, EntityFlag.Animate) {
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

    class Appearance : PlayerFlags(0x4, 3, EntityFlag.Appearance) {
        override fun writeTo(
            buffer: IoBuffer,
            context: Any?,
        ) {
            if (context !is Player) {
                logInvalidType(context, typeOf<Player>())
                return
            }
            val appearance = context.appearance
            appearance.prepareBodyData(context)
            var settings = appearance.gender.toByte().toInt()
            val nonPvp = context.skullManager.isWilderness && context.skullManager.isWildernessDisabled
            if (context.size() > 1) {
                settings += (context.size() - 1) shl 3
            }
            if (nonPvp) {
                settings += 0x4 // flag the player as non-PvP
            }

            buffer.p1(0) // stand-in for size.
            val startPos = buffer.toByteBuffer().position()

            buffer.p1(settings)
            buffer.p1(appearance.skullIcon)
            buffer.p1(appearance.headIcon)
            val npcId = appearance.npcId
            if (npcId == -1) {
                val parts = appearance.bodyParts
                for (i in 0 until 12) {
                    if (parts[i] == 0) {
                        buffer.p1(0)
                    } else {
                        buffer.p2(parts[i])
                    }
                }
            } else {
                buffer.p2(-1)
                buffer.p2(npcId)
                buffer.p1(255)
            }
            arrayOf(
                appearance.hair,
                appearance.torso,
                appearance.legs,
                appearance.feet,
                appearance.skin,
            ).forEach { part ->
                buffer.p1(part.color)
            }
            buffer.p2(appearance.renderAnimation)
            buffer.p8(StringUtils.stringToLong(context.username))
            if (nonPvp) {
                buffer.p1(context.properties.currentCombatLevel) // with summoning
                buffer.p2(context.skills.getTotalLevel())
            } else {
                // combat level calculations
                if ((
                        GameWorld.settings!!.isPvp ||
                            (GameWorld.settings!!.wild_pvp_enabled && context.skullManager.isWilderness)
                    ) &&
                    !context.familiarManager.isUsingSummoning
                ) {
                    buffer.p1(context.properties.combatLevel)
                    buffer.p1(context.properties.combatLevel + context.familiarManager.summoningCombatLevel)
                } else {
                    buffer.p1(context.properties.currentCombatLevel)
                    buffer.p1(context.properties.currentCombatLevel)
                }
                if (GameWorld.settings!!.isPvp) {
                    buffer.p1(context.skullManager.level)
                } else {
                    buffer.p1(-1) // combat range
                }
            }
            buffer.p1(0) // sound radius
            buffer.psizeadd(buffer.toByteBuffer().position() - startPos)
        }
    }

    class FaceEntity : PlayerFlags(0x2, 4, EntityFlag.FaceEntity) {
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

    class ForceMove : PlayerFlags(0x400, 5, EntityFlag.ForceMove) {
        override fun writeToDynamic(
            buffer: IoBuffer,
            context: Any?,
            e: Entity,
        ) {
            if (context !is ForceMoveCtx) {
                logInvalidType(context, typeOf<ForceMoveCtx>())
                return
            }
            if (e !is Player) {
                logInvalidType(context, typeOf<Player>())
                return
            }
            val l = e.playerFlags.lastSceneGraph
            buffer.p1neg(context.start.getSceneX(l))
            buffer.p1(context.start.getSceneY(l))
            buffer.p1add(context.dest.getSceneX(l))
            buffer.p1(context.dest.getSceneY(l))
            buffer.ip2(context.startArrive)
            buffer.ip2(max(context.startArrive + 1, context.startArrive + context.destArrive))
            buffer.p1neg(context.direction.toInteger())
        }
    }

    class ForceChat : PlayerFlags(0x20, 6, EntityFlag.ForceChat) {
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

    class SecondaryHit : PlayerFlags(0x200, 7, EntityFlag.SecondaryHit) {
        override fun writeTo(
            buffer: IoBuffer,
            context: Any?,
        ) {
            if (context !is HitMark) {
                logInvalidType(context, typeOf<HitMark>())
                return
            }
            buffer.psmarts(context.damage)
            buffer.p1sub(context.type)
        }
    }

    class AnimationSequence : PlayerFlags(0x800, 8, EntityFlag.AnimSeq) {
        // TODO: Implement animation sequence handling
    }

    class SpotAnim : PlayerFlags(0x100, 9, EntityFlag.SpotAnim) {
        override fun writeTo(
            buffer: IoBuffer,
            context: Any?,
        ) {
            if (context !is Graphics) {
                logInvalidType(context, typeOf<Graphics>())
                return
            }
            buffer.ip2(context.id)
            buffer.mp4((context.height shl 16) + context.delay)
        }
    }

    class FaceLocation : PlayerFlags(0x40, 10, EntityFlag.FaceLocation) {
        override fun writeTo(
            buffer: IoBuffer,
            context: Any?,
        ) {
            if (context !is Location) {
                logInvalidType(context, typeOf<Location>())
                return
            }
            buffer.p2((context.x shl 1) + 1)
            buffer.ip2add((context.y shl 1) + 1)
        }
    }
}
