package core.cache.def.impl

import core.api.getVarbit
import core.cache.Cache.getData
import core.cache.CacheIndex
import core.game.node.entity.player.Player
import core.net.g1
import core.net.g2
import java.nio.ByteBuffer

/**
 * Represents a varbit definition used for bit-wise variable storage.
 *
 * @property id The varbit id.
 * @property varpId The related varp id.
 * @property startBit The starting bit index.
 * @property endBit The ending bit index.
 */
class VarbitDefinition {
    val id: Int
    var varpId: Int = 0
    var startBit: Int = 0
    var endBit: Int = 0
        private set

    constructor(id: Int) {
        this.id = id
    }

    constructor(varpId: Int, id: Int, startBit: Int, endBit: Int) {
        this.varpId = varpId
        this.id = id
        this.startBit = startBit
        this.endBit = endBit
    }

    /**
     * Gets the value of this varbit for the given player.
     *
     * @param player The player instance.
     * @return The value of the varbit.
     */
    fun getValue(player: Player): Int = getVarbit(player, id)

    /**
     * Calculates the bit mask from startBit to endBit.
     */
    val mask: Int
        get() {
            var mask = 0
            for (i in startBit..endBit) mask = mask or (1 shl (i - startBit))
            return mask
        }

    override fun toString(): String = "VarbitDefinition(id=$id, varpId=$varpId, startBit=$startBit, endBit=$endBit)"

    companion object {
        private val MAPPING: MutableMap<Int, VarbitDefinition> = HashMap()
        private val BITS = IntArray(32)

        init {
            var flag = 2
            for (i in 0..31) {
                BITS[i] = flag - 1
                flag += flag
            }
        }

        /**
         * Gets the varbit definition for a scenery id.
         */
        @JvmStatic
        fun forSceneryId(id: Int): VarbitDefinition = forId(id)

        /**
         * Gets the varbit definition for an NPC id.
         */
        fun forNpcId(id: Int): VarbitDefinition = forId(id)

        /**
         * Gets the varbit definition for an item id.
         */
        fun forItemId(id: Int): VarbitDefinition = forId(id)

        /**
         * Gets the varbit definition for the given id.
         *
         * @param id The varbit id.
         * @return The varbit definition.
         */
        fun forId(id: Int): VarbitDefinition {
            MAPPING[id]?.let { return it }

            val def = VarbitDefinition(id)
            val bs = getData(CacheIndex.VAR_BIT, id ushr 10, id and 0x3FF)
            if (bs != null) {
                val buffer = ByteBuffer.wrap(bs)
                var opcode: Int
                while ((buffer.g1().also { opcode = it }) != 0) {
                    if (opcode == 1) {
                        def.varpId = buffer.g2()
                        def.startBit = buffer.g1()
                        def.endBit = buffer.g1()
                    }
                }
            }
            MAPPING[id] = def
            return def
        }

        /**
         * Creates and registers a new VarbitDefinition.
         *
         * @param varpId The varp id.
         * @param varbitId The varbit id.
         * @param startBit The start bit index.
         * @param endBit The end bit index.
         */
        fun create(varpId: Int, varbitId: Int, startBit: Int, endBit: Int) {
            val def = VarbitDefinition(varpId, varbitId, startBit, endBit)
            MAPPING[varbitId] = def
        }

        /**
         * Gets all varbit definitions.
         */
        val mapping: Map<Int, VarbitDefinition>
            get() = MAPPING
    }
}
