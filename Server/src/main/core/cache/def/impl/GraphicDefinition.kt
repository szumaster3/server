package core.cache.def.impl

import core.cache.Cache
import core.cache.CacheIndex
import core.net.g1
import core.net.g2
import core.net.g2s
import core.net.g4
import java.nio.ByteBuffer

/**
 * Represents a graphical definition used for rendering graphics.
 */
class GraphicDefinition {
    var modelID: Int = 0
    var animationID: Int = -1
    var sizeXY: Int = 128
    var sizeZ: Int = 128
    var rotation: Int = 0
    var ambient: Int = 0
    var originalModelColor: ShortArray? = null
    var modifiedModelColor: ShortArray? = null
    var originalTextureColor: ShortArray? = null
    var modifiedTextureColor: ShortArray? = null
    var castsShadow: Boolean = false
    var modelShadow: Int = 0
    var graphicsId: Int = 0
    var renderPriority: Byte = 0
    var shadowOpacity: Int = -1

    companion object {
        private val graphicDefinitions = mutableMapOf<Int, GraphicDefinition>()

        /**
         * Get graphic definition by id.
         */
        fun forId(gfxId: Int): GraphicDefinition = graphicDefinitions[gfxId] ?: run {
            val data = Cache.getData(CacheIndex.GRAPHICS, gfxId ushr 8, gfxId and 0xFF)
            GraphicDefinition().apply {
                graphicsId = gfxId
                data?.let { readValueLoop(ByteBuffer.wrap(it)) }
                graphicDefinitions[gfxId] = this
            }
        }
    }

    /**
     * Read graphic data from buffer.
     */
    private fun readValueLoop(buffer: ByteBuffer) {
        while (true) {
            val opcode = buffer.g1()
            if (opcode == 0) break
            decode(buffer, opcode)
        }
    }

    /**
     * Decode opcode and update definition.
     */
    private fun decode(buffer: ByteBuffer, opcode: Int) {
        when (opcode) {
            1 -> modelID = buffer.g2()
            2 -> animationID = buffer.g2()
            4 -> sizeXY = buffer.g2()
            5 -> sizeZ = buffer.g2()
            6 -> rotation = buffer.g2()
            7 -> ambient = buffer.g1()
            8 -> modelShadow = buffer.g1()
            10 -> castsShadow = true
            11 -> renderPriority = 1
            12 -> renderPriority = 4
            13 -> renderPriority = 5
            14 -> {
                renderPriority = 2
                shadowOpacity = buffer.g1() * 256
            }
            15 -> {
                renderPriority = 3
                shadowOpacity = buffer.g2()
            }
            16 -> {
                renderPriority = 3
                shadowOpacity = buffer.g4()
            }
            40 -> {
                val size = buffer.g1()
                originalModelColor = ShortArray(size)
                modifiedModelColor = ShortArray(size)
                for (i in 0 until size) {
                    originalModelColor!![i] = buffer.g2s().toShort()
                    modifiedModelColor!![i] = buffer.g2s().toShort()
                }
            }
            41 -> {
                val size = buffer.g1()
                originalTextureColor = ShortArray(size)
                modifiedTextureColor = ShortArray(size)
                for (i in 0 until size) {
                    originalTextureColor!![i] = buffer.g2().toShort()
                    modifiedTextureColor!![i] = buffer.g2s().toShort()
                }
            }
        }
    }
}