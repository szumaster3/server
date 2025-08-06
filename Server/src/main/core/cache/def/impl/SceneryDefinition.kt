package core.cache.def.impl

import core.api.getVarp
import core.api.log
import core.cache.Cache.getData
import core.cache.Cache.getIndexCapacity
import core.cache.CacheIndex
import core.cache.buffer.read.BufferReader
import core.cache.def.Definition
import core.game.interaction.OptionHandler
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.GameWorld.prompt
import core.net.g1
import core.net.g2
import core.net.g4
import core.net.gjstr
import core.tools.Log
import java.nio.ByteBuffer

/**
 * The type Scenery definition.
 */
class SceneryDefinition : Definition<Scenery?>() {
    @JvmField var originalColors: ShortArray? = null
    @JvmField var childrenIds: IntArray? = null
    @JvmField var modelIds: IntArray? = null
    private var modelConfiguration: IntArray? = null
    @JvmField var anIntArray3833: IntArray? = null
    @JvmField val anInt3834: Int = 0
    @JvmField var anInt3835: Int = -1
    private val aByte3837: Byte = 0
    @JvmField var options = arrayOfNulls<String?>(5)
    @JvmField var anInt3838: Int = -1
    @JvmField var mirrored: Boolean
    @JvmField var contrast: Int
    @JvmField var modelSizeZ: Int
    @JvmField var anInt3844: Int
    @JvmField var aBoolean3845: Boolean = false
    private val aByte3847: Byte = 0
    private val aByte3849: Byte = 0
    @JvmField var anInt3850: Int = 0
    @JvmField var anInt3851: Int
    @JvmField var isBlocksLand: Boolean
    @JvmField var aBoolean3853: Boolean
    @JvmField var supportItems: Int
    @JvmField var isFirstBool: Boolean
    @JvmField var anInt3857: Int
    @JvmField var recolourPalette: ByteArray? = null
    @JvmField var anIntArray3859: IntArray? = null
    @JvmField var anInt3860: Int
    @JvmField var varbitID: Int
    @JvmField var modifiedColors: ShortArray? = null
    @JvmField var anInt3865: Int = 255
    @JvmField var aBoolean3866: Boolean = false
    @JvmField var delayShading: Boolean = false
    @JvmField var isProjectileClipped: Boolean
    @JvmField val anIntArray3869: IntArray?
    @JvmField var aBoolean3870: Boolean
    @JvmField var sizeY: Int
    @JvmField var isCastsShadow: Boolean = true
    @JvmField var membersOnly: Boolean
    @JvmField var thirdBoolean: Boolean
    @JvmField val anInt3875: Int
    @JvmField var addObjectCheck: Int
    @JvmField val anInt3877: Int
    @JvmField var brightness: Int
    @JvmField var solid: Int
    @JvmField val anInt3881: Int = 0
    @JvmField var anInt3882: Int
    @JvmField var offsetX: Int
    @JvmField var loader: Any? = null
    @JvmField var offsetZ: Int
    @JvmField var sizeX: Int
    @JvmField var aBoolean3891: Boolean
    @JvmField var offsetMultiplier: Int
    @JvmField var interactive: Int
    @JvmField var aBoolean3894: Boolean
    @JvmField var forceAnimation: Boolean
    @JvmField var anInt3896: Int = 0
    @JvmField var configId: Int
    private val aByteArray3899: ByteArray? = null
    @JvmField var anInt3900: Int
    @JvmField var modelSizeX: Int
    @JvmField var anInt3904: Int
    @JvmField var anInt3905: Int
    @JvmField var aBoolean3906: Boolean
    @JvmField var anIntArray3908: IntArray? = null
    @JvmField var contouredGround: Byte
    @JvmField var anInt3913: Int
    private val aByte3914: Byte
    @JvmField var offsetY: Int
    @JvmField val anIntArrayArray3916: Array<IntArray>? = null
    @JvmField var modelSizeY: Int
    @JvmField var modifiedTextureColours: ShortArray? = null
    @JvmField var originalTextureColours: ShortArray? = null
    @JvmField var anInt3921: Int
    private val aClass194_3922: Any? = null
    @JvmField var aBoolean3923: Boolean
    @JvmField var aBoolean3924: Boolean
    @JvmField var walkingFlag: Int
    private var hasHiddenOptions = false
    @JvmField var mapIcon: Short

    /**
     * Instantiates a new Scenery definition.
     */
    init {
        anInt3860 = -1
        varbitID = -1
        anInt3851 = -1
        anInt3844 = -1
        anInt3857 = -1
        anInt3882 = -1
        options = arrayOfNulls(5)
        anInt3875 = 0
        mirrored = false
        anIntArray3869 = null
        sizeY = 1
        thirdBoolean = false
        isProjectileClipped = true
        offsetX = 0
        forceAnimation = true
        contrast = 0
        aBoolean3870 = false
        offsetZ = 0
        aBoolean3853 = true
        isBlocksLand = false
        solid = 2
        supportItems = -1
        brightness = 0
        anInt3904 = 0
        sizeX = 1
        addObjectCheck = -1
        isFirstBool = false
        aBoolean3891 = false
        anInt3905 = 0
        name = "null"
        anInt3913 = -1
        aBoolean3906 = false
        membersOnly = false
        aByte3914 = 0.toByte()
        offsetY = 0
        anInt3900 = 0
        interactive = -1
        aBoolean3894 = false
        contouredGround = 0.toByte()
        anInt3921 = 0
        modelSizeX = 128
        configId = -1
        anInt3877 = 0
        walkingFlag = 0
        offsetMultiplier = 64
        aBoolean3923 = false
        aBoolean3924 = false
        modelSizeZ = 128
        modelSizeY = 128
        mapIcon = -1
    }

    /**
     * Configure object.
     */
    fun configureObject() {
        if (interactive == -1) {
            interactive = 0
            if (modelIds != null && (getModelConfiguration() == null || getModelConfiguration()!![0] == 10)) {
                interactive = 1
            }
            for (i in 0..4) {
                if (options[i] != null) {
                    interactive = 1
                    break
                }
            }
        }
        if (childrenIds != null) {
            for (i in childrenIds!!.indices) {
                val def = forId(childrenIds!![i])
                def.varbitID = varbitID
            }
        }
        if (supportItems == -1) {
            supportItems = if (solid == 0) 0 else 1
        }
        // Manual changes
        if (id == 31017) {
            sizeY = 2
            sizeX = sizeY
        }
        if (id == 29292) {
            isProjectileClipped = false
        }
    }

    /**
     * Has actions boolean.
     *
     * @return the boolean
     */
    fun hasActions(): Boolean {
        if (interactive > 0) {
            return true
        }
        if (childrenIds == null) {
            return hasOptions(false)
        }
        for (i in childrenIds!!.indices) {
            if (childrenIds!![i] != -1) {
                val def = forId(childrenIds!![i])
                if (def.hasOptions(false)) {
                    return true
                }
            }
        }
        return hasOptions(false)
    }

    /**
     * Gets child object.
     *
     * @param player the player
     * @return the child object
     */
    fun getChildObject(player: Player?): SceneryDefinition? {
        if (childrenIds == null || childrenIds!!.size < 1) {
            return this
        }
        var configValue = -1
        if (player != null) {
            if (varbitID != -1) {
                val def = VarbitDefinition.forSceneryId(varbitID)
                if (def != null) {
                    configValue = def.getValue(player)
                }
            } else if (configId != -1) {
                configValue = getVarp(player, configId)
            }
        } else {
            configValue = 0
        }
        val childDef = getChildObjectAtIndex(configValue)
        if (childDef != null) childDef.varbitID = this.varbitID
        return childDef
    }

    /**
     * Gets child object at index.
     *
     * @param index the index
     * @return the child object at index
     */
    fun getChildObjectAtIndex(index: Int): SceneryDefinition {
        if (childrenIds == null || childrenIds!!.isEmpty()) {
            return this
        }
        if (index < 0 || index >= childrenIds!!.size || childrenIds!![index] == -1) {
            val lastIndex = childrenIds!!.lastIndex
            val objectId = if (lastIndex >= 0) childrenIds!![lastIndex] else -1
            if (objectId != -1) {
                return forId(objectId)
            }
            return this
        }
        return forId(childrenIds!![index])
    }

    val configFile: VarbitDefinition?
        /**
         * Gets config file.
         *
         * @return the config file
         */
        get() {
            if (varbitID != -1) {
                return VarbitDefinition.forSceneryId(varbitID)
            }
            return null
        }

    /**
     * Sets option.
     *
     * @param slot   the slot
     * @param option the option
     */
    fun setOption(
        slot: Int,
        option: String?,
    ) {
        require(!(slot < 0 || slot >= options.size)) { ": $slot" }
        options[slot] = option
    }

    /**
     * Remove option.
     *
     * @param slot the slot
     */
    fun removeOption(slot: Int) {
        require(!(slot < 0 || slot >= options.size)) { "Wrong index: $slot" }
        options[slot] = null
    }

    /**
     * Print options.
     */
    fun printOptions() {
        println("Options for object $id:")
        for (i in options.indices) {
            println("Slot " + i + ": " + (if (options[i] != null) options[i] else "No options"))
        }
    }

    /**
     * Is mirrored boolean.
     *
     * @return the boolean
     */
    fun isMirrored(): Boolean = mirrored

    /**
     * Sets mirrored.
     *
     * @param mirrored the mirrored
     */
    fun setMirrored(mirrored: Boolean) {
        this.mirrored = mirrored
    }

    /**
     * Gets byte 3837.
     *
     * @return the byte 3837
     */
    fun getaByte3837(): Byte = aByte3837

    /**
     * Isa boolean 3845 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3845(): Boolean = aBoolean3845

    /**
     * Gets byte 3847.
     *
     * @return the byte 3847
     */
    fun getaByte3847(): Byte = aByte3847

    /**
     * Gets byte 3849.
     *
     * @return the byte 3849
     */
    fun getaByte3849(): Byte = aByte3849

    /**
     * Isa boolean 3853 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3853(): Boolean = aBoolean3853

    /**
     * Isa boolean 3866 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3866(): Boolean = aBoolean3866

    /**
     * Isa boolean 3867 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3867(): Boolean = delayShading

    /**
     * Isa boolean 3870 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3870(): Boolean = aBoolean3870

    /**
     * Isa boolean 3873 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3873(): Boolean = membersOnly

    /**
     * Isa boolean 3891 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3891(): Boolean = aBoolean3891

    /**
     * Isa boolean 3894 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3894(): Boolean = aBoolean3894

    /**
     * Isa boolean 3895 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3895(): Boolean = forceAnimation

    /**
     * Geta byte array 3899 byte.
     *
     * @return the byte
     */
    fun getaByteArray3899(): ByteArray? = aByteArray3899

    override fun getName(): String = name

    /**
     * Isa boolean 3906 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3906(): Boolean = aBoolean3906

    /**
     * Gets byte 3914.
     *
     * @return the byte 3914
     */
    fun getaByte3914(): Byte = aByte3914

    /**
     * Gets class 194 3922.
     *
     * @return the class 194 3922
     */
    fun getaClass194_3922(): Any? = aClass194_3922

    /**
     * Isa boolean 3923 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3923(): Boolean = aBoolean3923

    /**
     * Isa boolean 3924 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3924(): Boolean = aBoolean3924

    /**
     * Has action boolean.
     *
     * @param action the action
     * @return the boolean
     */
    fun hasAction(action: String?): Boolean {
        if (options == null) {
            return false
        }
        for (option in options) {
            if (option == null) {
                continue
            }
            if (option.equals(action, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    /**
     * Is has hidden options boolean.
     *
     * @return the boolean
     */
    fun isHasHiddenOptions(): Boolean = hasHiddenOptions

    /**
     * Sets has hidden options.
     *
     * @param hasHiddenOptions the has hidden options
     */
    fun setHasHiddenOptions(hasHiddenOptions: Boolean) {
        this.hasHiddenOptions = hasHiddenOptions
    }

    /**
     * Get model configuration int.
     *
     * @return the int
     */
    fun getModelConfiguration(): IntArray? = modelConfiguration

    /**
     * Sets model configuration.
     *
     * @param modelConfiguration the model configuration
     */
    fun setModelConfiguration(modelConfiguration: IntArray) {
        this.modelConfiguration = modelConfiguration
    }

    companion object {
        /**
         * Gets definitions.
         *
         * @return the definitions
         */
        val definitions: MutableMap<Int, SceneryDefinition> = HashMap()
        private val OPTION_HANDLERS: MutableMap<String, OptionHandler?> = HashMap()
        var anInt3832: Int = 0
        var anInt3836: Int = 0
        var anInt3842: Int = 0
        var anInt3843: Int = 0
        var anInt3846: Int = 0

        /**
         * The entry point of application.
         *
         * @param args the input arguments
         * @throws Throwable the throwable
         */
        @Throws(Throwable::class)
        @JvmStatic
        fun main(args: Array<String>) {
            prompt(false)
        }

        /**
         * Parse.
         *
         * @throws Throwable the throwable
         */
        @Throws(Throwable::class)
        fun parse() {
            for (objectId in 0 until getIndexCapacity(CacheIndex.SCENERY_CONFIGURATION)) {
                var data = getData(CacheIndex.SCENERY_CONFIGURATION, objectId ushr 8, objectId and 0xFF)
                if (data == null) {
                    definitions[objectId] = SceneryDefinition()
                    continue
                }
                val def = decode(objectId, ByteBuffer.wrap(data))
                definitions[objectId] = def
                data = null
            }
        }

        /**
         * For id scenery definition.
         *
         * @param objectId the object id
         * @return the scenery definition
         */
        @JvmStatic
        fun forId(objectId: Int): SceneryDefinition {
            var def = definitions[objectId]
            if (def != null) {
                return def
            }
            definitions[objectId] = SceneryDefinition().also { def = it }
            def!!.id = objectId
            return def!!
        }

        /**
         * Parses a [SceneryDefinition].
         *
         * @param objectId The id of the scenery object.
         * @param buffer The buffer containing the object data.
         * @return The parsed [SceneryDefinition].
         */
        private fun decode(objectId: Int, buffer: ByteBuffer): SceneryDefinition {
            val def = SceneryDefinition()
            def.id = objectId

            while (buffer.hasRemaining()) {
                val opcode = buffer.g1()

                when (opcode) {
                    0 -> break

                    1, 5 -> {
                        val length = buffer.g1()
                        if (def.modelIds == null) {
                            def.modelIds = IntArray(length)
                            if (opcode == 1) {
                                def.modelConfiguration = IntArray(length)
                            }
                            for (i in 0 until length) {
                                def.modelIds!![i] = buffer.g2()
                                if (opcode == 1) {
                                    def.modelConfiguration!![i] = buffer.g1()
                                }
                            }
                        } else {
                            buffer.position(buffer.position() + length * if (opcode == 1) 3 else 2)
                        }
                    }

                    2 -> def.name = buffer.gjstr()

                    14 -> def.sizeX = buffer.g1()
                    15 -> def.sizeY = buffer.g1()

                    17 -> {
                        def.isProjectileClipped = false
                        def.solid = 0
                    }

                    18 -> def.isProjectileClipped = false

                    19 -> def.interactive = buffer.g1()

                    21 -> def.contouredGround = 1.toByte()
                    22 -> def.delayShading = true
                    23 -> def.thirdBoolean = true

                    24 -> {
                        def.addObjectCheck = buffer.g2()
                        if (def.addObjectCheck == 65535) def.addObjectCheck = -1
                    }

                    27 -> def.solid = 1

                    28 -> def.offsetMultiplier = buffer.g1() shl 2

                    29 -> def.brightness = buffer.get().toInt()
                    39 -> def.contrast = buffer.get().toInt() * 5

                    in 30..34 -> {
                        val idx = opcode - 30
                        def.options[idx] = buffer.gjstr()
                        if (def.options[idx] == "Hidden") {
                            def.options[idx] = null
                            def.hasHiddenOptions = true
                        }
                    }

                    40 -> {
                        val length = buffer.g1()
                        def.originalColors = ShortArray(length)
                        def.modifiedColors = ShortArray(length)
                        repeat(length) {
                            def.originalColors!![it] = buffer.getShort()
                            def.modifiedColors!![it] = buffer.getShort()
                        }
                    }

                    41 -> {
                        val length = buffer.g1()
                        def.originalTextureColours = ShortArray(length)
                        def.modifiedTextureColours = ShortArray(length)
                        repeat(length) {
                            def.originalTextureColours!![it] = buffer.getShort()
                            def.modifiedTextureColours!![it] = buffer.getShort()
                        }
                    }

                    42 -> {
                        val length = buffer.g1()
                        def.recolourPalette = ByteArray(length)
                        repeat(length) { def.recolourPalette!![it] = buffer.get() }
                    }

                    60 -> def.mapIcon = buffer.getShort()

                    62 -> def.mirrored = true
                    64 -> def.isCastsShadow = false

                    65 -> def.modelSizeX = buffer.g2()
                    66 -> def.modelSizeZ = buffer.g2()
                    67 -> def.modelSizeY = buffer.g2()

                    68 -> buffer.getShort()

                    69 -> def.walkingFlag = buffer.g1()

                    70 -> def.offsetX = buffer.getShort().toInt() shl 2
                    71 -> def.offsetZ = buffer.getShort().toInt() shl 2
                    72 -> def.offsetY = buffer.getShort().toInt() shl 2

                    73 -> def.isBlocksLand = true
                    74 -> def.isFirstBool = true

                    75 -> def.supportItems = buffer.g1()

                    77, 92 -> {
                        def.varbitID = buffer.g2()
                        if (def.varbitID == 65535) def.varbitID = -1

                        def.configId = buffer.g2()
                        if (def.configId == 65535) def.configId = -1

                        var defaultId = -1
                        if (opcode == 92) {
                            defaultId = buffer.g2()
                            if (defaultId == 65535) defaultId = -1
                        }

                        val childrenAmount = buffer.g1()
                        def.childrenIds = IntArray(childrenAmount + 2)
                        for (index in 0..childrenAmount) {
                            def.childrenIds!![index] = buffer.g2()
                            if (def.childrenIds!![index] == 65535) def.childrenIds!![index] = -1
                        }
                        def.childrenIds!![childrenAmount + 1] = defaultId
                    }

                    78 -> {
                        def.anInt3860 = buffer.g2()
                        def.anInt3904 = buffer.g1()
                    }

                    79 -> {
                        def.anInt3900 = buffer.g2()
                        def.anInt3905 = buffer.g2()
                        def.anInt3904 = buffer.g1()
                        val length = buffer.g1()
                        def.anIntArray3859 = IntArray(length)
                        for (i in 0 until length) {
                            def.anIntArray3859!![i] = buffer.g2()
                        }
                    }

                    81 -> {
                        def.contouredGround = 2.toByte()
                        def.anInt3882 = 256 * buffer.g1()
                    }

                    82, 88 -> {}

                    89 -> def.forceAnimation = false
                    90 -> def.aBoolean3870 = true
                    91 -> def.membersOnly = true

                    93 -> {
                        def.contouredGround = 3.toByte()
                        def.anInt3882 = buffer.g2()
                    }

                    94 -> def.contouredGround = 4.toByte()
                    95 -> def.contouredGround = 5.toByte()

                    96, 97 -> {}

                    100 -> {
                        buffer.get()
                        buffer.getShort()
                    }

                    101 -> buffer.get()
                    102 -> buffer.getShort()

                    249 -> {
                        val length = buffer.g1()
                        repeat(length) {
                            val isString = buffer.g1() == 1
                            BufferReader.getMedium(buffer) // script id
                            if (isString) {
                                buffer.gjstr()
                            } else {
                                buffer.g4()
                            }
                        }
                    }

                    else -> {
                        log(SceneryDefinition::class.java, Log.ERR, "Unhandled object definition opcode: $opcode")
                        break
                    }
                }
            }

            def.configureObject()

            if (def.isFirstBool) {
                def.solid = 0
                def.isProjectileClipped = false
            }

            return def
        }

        /**
         * Gets option handler.
         *
         * @param nodeId the node id
         * @param name   the name
         * @return the option handler
         */
        @JvmStatic
        fun getOptionHandler(
            nodeId: Int,
            name: String,
        ): OptionHandler? {
            val def = forId(nodeId)
            val handler = def.getConfiguration<OptionHandler>("option:$name")
            if (handler != null) {
                return handler
            }
            return OPTION_HANDLERS[name]
        }

        /**
         * Sets option handler.
         *
         * @param name    the name
         * @param handler the handler
         * @return the option handler
         */
        @JvmStatic
        fun setOptionHandler(
            name: String,
            handler: OptionHandler?,
        ): Boolean = OPTION_HANDLERS.put(name, handler) != null

        /**
         * Gets container id.
         *
         * @param id the id
         * @return the container id
         */
        fun getContainerId(id: Int): Int = id ushr 8
    }
}
