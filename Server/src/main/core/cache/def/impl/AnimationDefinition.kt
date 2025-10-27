package core.cache.def.impl

import core.cache.Cache
import core.cache.Archive
import core.net.g1
import core.net.g2
import core.net.g3
import java.nio.ByteBuffer

/**
 * Represents an animation definition.
 */
class AnimationDefinition {
    var frames: IntArray? = null
    var duration: IntArray? = null
    var hasSoundEffect = false
    var priority = 5
    var rightHandItem = -1

    private var maxLoops = 99
    private var priorityOverride = -1
    private var leftHandItem = -1
    private var handledSounds: Array<IntArray?>? = null
    private var interleaveOrder: BooleanArray? = null
    private var expressionFrames: IntArray? = null
    private var isForcedPriority = false
    private var replayMode = 2
    private var priorityBackup = -1
    private var loopOffset = -1

    companion object {
        private val animationDefinition = mutableMapOf<Int, AnimationDefinition>()

        /**
         * Get animation by id.
         * @param emoteId Animation id
         * @return AnimationDefinition or null if missing
         */
        @JvmStatic
        fun forId(emoteId: Int): AnimationDefinition? = try {
            animationDefinition[emoteId] ?: run {
                val data = Cache.getData(Archive.JS5_CONFIG_SEQ, emoteId ushr 7, emoteId and 0x7f)
                val def = AnimationDefinition()
                data?.let { def.readValueLoop(ByteBuffer.wrap(it)) }
                def.changeValues()
                animationDefinition[emoteId] = def
                def
            }
        } catch (_: Throwable) { null }

        /** All loaded animations */
        fun getDefinition(): Map<Int, AnimationDefinition> = animationDefinition
    }

    /**
     * Read animation data from buffer.
     */
    private fun readValueLoop(buffer: ByteBuffer) {
        while (true) {
            val opcode = buffer.g1()
            if (opcode == 0) break
            readValues(buffer, opcode)
        }
    }

    /**
     * Total animation duration in ms.
     */
    fun getDuration(): Int = duration?.filter { it <= 100 }?.sumOf { it * 20 } ?: 0

    /**
     * Total animation cycles.
     */
    fun getCycles(): Int = duration?.sum() ?: 0

    /**
     * Duration in game ticks.
     */
    fun getDurationTicks(): Int = maxOf(getDuration() / 600, 1)

    /**
     * Read values from buffer by opcode.
     */
    private fun readValues(buffer: ByteBuffer, opcode: Int) {
        when (opcode) {
            1 -> {
                val len = buffer.g2()
                duration = IntArray(len) { buffer.g2() }
                frames = IntArray(len) { buffer.g2() }
                frames?.forEachIndexed { i, v -> frames!![i] = (buffer.g2() shl 16) + v }
            }
            2 -> loopOffset = buffer.g2()
            3 -> {
                interleaveOrder = BooleanArray(256)
                val len = buffer.g1()
                repeat(len) { interleaveOrder!![buffer.g1()] = true }
            }
            4 -> isForcedPriority = true
            5 -> priority = buffer.g1()
            6 -> leftHandItem = buffer.g2()
            7 -> rightHandItem = buffer.g2()
            8 -> maxLoops = buffer.g1()
            9 -> priorityOverride = buffer.g1()
            10 -> priorityBackup = buffer.g1()
            11 -> replayMode = buffer.g1()
            12 -> {
                val len = buffer.g1()
                expressionFrames = IntArray(len) { buffer.g2() }
                expressionFrames?.forEachIndexed { i, v -> expressionFrames!![i] = (buffer.g2() shl 16) + v }
            }
            13 -> {
                val len = buffer.g2()
                handledSounds = Array(len) { null }
                repeat(len) { idx ->
                    val size = buffer.g1()
                    if (size > 0) {
                        handledSounds!![idx] = IntArray(size).apply {
                            this[0] = buffer.g3()
                            for (j in 1 until size) this[j] = buffer.g2()
                        }
                    }
                }
            }
            14 -> hasSoundEffect = true
        }
    }

    /**
     * Fix up default values after reading.
     */
    fun changeValues() {
        if (priorityOverride == -1) priorityOverride = if (interleaveOrder == null) 0 else 2
        if (priorityBackup == -1) priorityBackup = if (interleaveOrder == null) 0 else 2
    }
}