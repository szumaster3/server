package core.cache.def.impl

import core.api.log
import core.cache.Cache.getArchiveCapacity
import core.cache.Cache.getData
import core.cache.CacheArchive
import core.cache.CacheIndex
import core.game.world.GameWorld.prompt
import core.net.g1
import core.net.g2
import core.net.g2s
import core.tools.Log
import java.lang.reflect.Modifier
import java.nio.ByteBuffer

/**
 * The render animation definition.
 */
class RenderAnimationDefinition {
    var turn180Animation: Int
    var anInt951: Int = -1
    var anInt952: Int
    var turnCWAnimation: Int = -1
    var anInt954: Int
    var anInt955: Int
    var anInt956: Int = 0
    var anInt957: Int
    var anInt958: Int
    var anIntArray959: IntArray? = null
    var anInt960: Int
    var anInt961: Int = 0
    var anInt962: Int
    var walkAnimationId: Int
    var anInt964: Int
    var anInt965: Int = 0
    var anInt966: Int
    var standAnimationIds: IntArray? = null
    var anInt969: Int = 0
    var anIntArray971: IntArray? = null
    var standAnimationId: Int
    var anInt973: Int = 0
    var anInt974: Int
    var anInt975: Int = 0
    var runAnimationId: Int
    var anInt977: Int
    var aBoolean978: Boolean = true
    var anIntArrayArray979: Array<IntArray?>? = null
    var anInt980: Int = 0
    var turnCCWAnimation: Int
    var anInt983: Int
    var anInt985: Int
    var anInt986: Int
    var anInt987: Int
    var anInt988: Int = 0
    var anInt989: Int
    var anInt990: Int
    var anInt992: Int = 0
    var anInt993: Int = 0
    var anInt994: Int = 0

    private fun parse(buffer: ByteBuffer) {
        while (true) {
            val opcode = buffer.g1()
            if (opcode == 0) break
            decode(buffer, opcode)
        }
    }

    private fun decode(buffer: ByteBuffer, opcode: Int) {
        when (opcode) {
            1 -> {
                standAnimationId = buffer.g2()
                walkAnimationId = buffer.g2()
                if (standAnimationId == 0xFFFF) standAnimationId = -1
                if (walkAnimationId == 0xFFFF) walkAnimationId = -1
            }
            2 -> anInt974 = buffer.g2()
            3 -> anInt987 = buffer.g2()
            4 -> anInt986 = buffer.g2()
            5 -> anInt977 = buffer.g2()
            6 -> runAnimationId = buffer.g2()
            7 -> anInt960 = buffer.g2()
            8 -> anInt985 = buffer.g2()
            9 -> anInt957 = buffer.g2()
            26 -> {
                anInt973 = buffer.g1() * 4
                anInt975 = buffer.g1() * 4
            }
            27 -> {
                if (anIntArrayArray979 == null) anIntArrayArray979 = arrayOfNulls(12)
                val index = buffer.g1()
                anIntArrayArray979!![index] = IntArray(6) { buffer.g2s() }
            }
            28 -> {
                anIntArray971 = IntArray(12) {
                    val value = buffer.g1()
                    if (value == 255) -1 else value
                }
            }
            29 -> anInt992 = buffer.g1()
            30 -> anInt980 = buffer.g2()
            31 -> anInt988 = buffer.g1()
            32 -> anInt961 = buffer.g2()
            33 -> anInt956 = buffer.g2s()
            34 -> anInt993 = buffer.g1()
            35 -> anInt969 = buffer.g2()
            36 -> anInt965 = buffer.g2s()
            37 -> anInt951 = buffer.g1()
            38 -> anInt958 = buffer.g2()
            39 -> anInt954 = buffer.g2()
            40 -> turn180Animation = buffer.g2()
            41 -> turnCCWAnimation = buffer.g2()
            42 -> turnCWAnimation = buffer.g2()
            43 -> anInt955 = buffer.g2()
            44 -> anInt983 = buffer.g2()
            45 -> anInt964 = buffer.g2()
            46 -> anInt952 = buffer.g2()
            47 -> anInt966 = buffer.g2()
            48 -> anInt989 = buffer.g2()
            49 -> anInt990 = buffer.g2()
            50 -> anInt962 = buffer.g2()
            51 -> aBoolean978 = false
            52 -> {
                val len = buffer.g1()
                anIntArray959 = IntArray(len)
                standAnimationIds = IntArray(len)
                for (i in 0 until len) {
                    standAnimationIds!![i] = buffer.g2()
                    anIntArray959!![i] = buffer.g1()
                    anInt994 += anIntArray959!![i]
                }
            }
            53 -> aBoolean978 = false
            54 -> {
                buffer.g1()
                buffer.g1()
            }
            55 -> {
                buffer.g1() // index
                buffer.g2() // value
            }
            56 -> {
                buffer.g1() // index
                repeat(3) { buffer.g2() } // 3 values
            }
        }
    }

    init {
        anInt957 = -1
        anInt954 = -1
        anInt960 = -1
        anInt958 = -1
        turn180Animation = -1
        standAnimationId = -1
        anInt952 = -1
        anInt983 = -1
        anInt985 = -1
        anInt962 = -1
        anInt966 = -1
        anInt977 = -1
        runAnimationId = -1
        turnCCWAnimation = -1
        anInt987 = -1
        anInt964 = -1
        walkAnimationId = -1
        anInt986 = -1
        anInt955 = -1
        anInt989 = -1
        anInt974 = -1
        anInt990 = -1
    }

    companion object {
        fun forId(animId: Int): RenderAnimationDefinition? {
            if (animId == -1) return null
            val data = getData(CacheIndex.CONFIGURATION, CacheArchive.BAS_TYPE, animId)
            val defs = RenderAnimationDefinition()
            if (data != null) {
                defs.parse(ByteBuffer.wrap(data))
            } else {
                log(
                    RenderAnimationDefinition::class.java,
                    Log.ERR,
                    "No definitions found for render animation $animId, size=" +
                            getArchiveCapacity(CacheIndex.CONFIGURATION, CacheArchive.BAS_TYPE) + "!",
                )
            }
            return defs
        }

        @Throws(Throwable::class)
        @JvmStatic
        fun main(args: Array<String>) {
            prompt(false)
            val def = forId(1426)
            for (f in def!!.javaClass.declaredFields) {
                if (!Modifier.isStatic(f.modifiers)) {
                    if (f.type.isArray) {
                        val obj = f[def]
                        if (obj != null) {
                            val length = java.lang.reflect.Array.getLength(obj)
                            print(f.name + ", [")
                            for (i in 0 until length) {
                                print(java.lang.reflect.Array.get(obj, i).toString() + if (i < length - 1) ", " else "]")
                            }
                        }
                    }
                }
            }
        }
    }
}
