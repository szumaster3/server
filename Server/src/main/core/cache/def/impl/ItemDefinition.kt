package core.cache.def.impl

import content.global.skill.summoning.familiar.BurdenBeast
import core.api.EquipmentSlot
import core.api.equipSlot
import core.api.log
import core.api.sendString
import core.cache.Cache.getData
import core.cache.Cache.getIndexCapacity
import core.cache.Archive
import core.cache.def.Definition
import core.game.container.Container
import core.game.interaction.OptionHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.item.ItemPlugin
import core.game.system.config.ItemConfigParser
import core.net.*
import core.tools.Log
import core.tools.StringUtils.isPlusN
import shared.consts.Components
import shared.consts.Items
import java.nio.ByteBuffer
import kotlin.math.round

/**
 * The type Item definition.
 */
class ItemDefinition : Definition<Item?>() {
    private var interfaceModelId = 0
    private var modelZoom = 0
    private var modelRotationX = 0
    private var modelRotationY = 0
    private var modelOffset1: Int = 0
    private var modelOffset2: Int = 0
    private var stackable = false
    private var value = 1
    private var membersOnly = false
    private var maleWornModelId1 = -1
    private var femaleWornModelId1 = -1
    private var maleWornModelId2 = -1
    private var femaleWornModelId2 = -1
    private var maleWornModelId3: Int = -1
    private var femaleWornModelId3: Int = -1
    private var maleWornModelId4: Int = -1
    private var femaleWornModelId4: Int = -1
    private var groundOptions: Array<String?>
    private var originalModelColors: ShortArray? = null
    private var modifiedModelColors: ShortArray? = null
    private var textureColour1: ShortArray? = null
    private var textureColour2: ShortArray? = null
    private var unknownArray1: ByteArray? = null
    private var unknownArray2: IntArray? = null
    private val unknownArray3: Array<IntArray>? = null
    private var unnoted = true
    private var colourEquip1 = -1
    private var colourEquip2 = 0
    private var noteId = -1
    private var noteTemplateId = -1
    private var stackIds: IntArray? = null
    private var stackAmounts: IntArray? = null
    private var teamId = 0
    private var lendId = -1
    private var lendTemplateId = -1
    private var recolourId: Int = -1
    private var recolourTemplateId: Int = -1
    private var equipId = 0
    private var itemRequirements: HashMap<Int, Int>? = null
    private var clientScriptData: HashMap<Int, Any>? = null
    private var itemType = 0

    /**
     * Transfer note definition.
     *
     * @param reference         the reference
     * @param templateReference the template reference
     */
    fun transferNoteDefinition(reference: ItemDefinition, templateReference: ItemDefinition) {
        membersOnly = reference.membersOnly
        interfaceModelId = templateReference.interfaceModelId
        originalModelColors = templateReference.originalModelColors
        name = reference.name
        modelOffset2 = templateReference.modelOffset2
        textureColour1 = templateReference.textureColour1
        value = reference.value
        modelRotationY = templateReference.modelRotationY
        stackable = true
        unnoted = false
        modifiedModelColors = templateReference.modifiedModelColors
        modelRotationX = templateReference.modelRotationX
        modelZoom = templateReference.modelZoom
        textureColour1 = templateReference.textureColour1
        handlers[ItemConfigParser.TRADEABLE] = true
    }

    /**
     * Transfer lend definition.
     *
     * @param reference         the reference
     * @param templateReference the template reference
     */
    fun transferLendDefinition(reference: ItemDefinition, templateReference: ItemDefinition) {
        femaleWornModelId1 = reference.femaleWornModelId1
        maleWornModelId2 = reference.maleWornModelId2
        membersOnly = reference.membersOnly
        interfaceModelId = templateReference.interfaceModelId
        textureColour2 = reference.textureColour2
        groundOptions = reference.groundOptions
        unknownArray1 = reference.unknownArray1
        modelRotationX = templateReference.modelRotationX
        modelRotationY = templateReference.modelRotationY
        originalModelColors = reference.originalModelColors
        name = reference.name
        maleWornModelId1 = reference.maleWornModelId1
        colourEquip1 = reference.colourEquip1
        teamId = reference.teamId
        modelOffset2 = templateReference.modelOffset2
        clientScriptData = reference.clientScriptData
        modifiedModelColors = reference.modifiedModelColors
        colourEquip2 = reference.colourEquip2
        modelOffset1 = templateReference.modelOffset1
        textureColour1 = reference.textureColour1
        value = 0
        modelZoom = templateReference.modelZoom
        options = arrayOfNulls(5)
        femaleWornModelId2 = reference.femaleWornModelId2
        if (reference.options != null) {
            options = reference.options.clone()
        }
    }

    /**
     * Transfer recolour definition.
     *
     * @param reference         the reference
     * @param templateReference the template reference
     */
    fun transferRecolourDefinition(reference: ItemDefinition, templateReference: ItemDefinition) {
        femaleWornModelId2 = reference.femaleWornModelId2
        options = arrayOfNulls(5)
        modelRotationY = templateReference.modelRotationY
        name = reference.name
        maleWornModelId1 = reference.maleWornModelId1
        modelOffset2 = templateReference.modelOffset2
        femaleWornModelId1 = reference.femaleWornModelId1
        maleWornModelId2 = reference.maleWornModelId2
        modelOffset1 = templateReference.modelOffset1
        unknownArray1 = reference.unknownArray1
        stackable = reference.stackable
        modelRotationX = templateReference.modelRotationX
        textureColour1 = reference.textureColour1
        colourEquip1 = reference.colourEquip1
        textureColour2 = reference.textureColour2
        modifiedModelColors = reference.modifiedModelColors
        modelZoom = templateReference.modelZoom
        colourEquip2 = reference.colourEquip2
        teamId = reference.teamId
        value = 0
        groundOptions = reference.groundOptions
        originalModelColors = reference.originalModelColors
        membersOnly = reference.membersOnly
        clientScriptData = reference.clientScriptData
        interfaceModelId = templateReference.interfaceModelId
        if (reference.options != null) {
            options = reference.options.clone()
        }
    }

    /**
     * Has requirement boolean.
     *
     * @param player  the player
     * @param wield   the wield
     * @param message the message
     * @return the boolean
     */
    fun hasRequirement(player: Player, wield: Boolean, message: Boolean): Boolean {
        val requirements = getConfiguration<Map<Int, Int>>(ItemConfigParser.REQUIREMENTS) ?: return true
        for (skill in requirements.keys) {
            if (skill < 0 || skill >= Skills.SKILL_NAME.size) {
                continue
            }
            val level = requirements[skill]!!
            if (player.getSkills().getStaticLevel(skill) < level) {
                if (message) {
                    val name = Skills.SKILL_NAME[skill]
                    player.packetDispatch.sendMessage(
                        "You need a" + (if (isPlusN(name)) "n " else " ") + name + " level of " + level + " to " + (if (wield) "wear " else "use ") + "this.",
                    )
                }
                return false
            }
        }
        return true
    }

    private val allowedNames = setOf("trousers", "tribal top", "woven top", "chompy bird hat", "cape")
    private val excludedKeywords = listOf(
        "(class", "camo ", "larupia", "kyatt", " stole", "moonclan", "villager ", "tribal", "spirit ", "gauntlets"
    )
    private val allowedSubstrings = listOf("satchel", "naval", " partyhat")

    /**
     * Checks if the item is allowed to be taken to Entrana.
     *
     * @return boolean
     */
    val isAllowed: Boolean
        get() {
            val name = getName().lowercase()
            val id = getId()

            if (id in permittedItems) return true
            if (id in bannedItems) return false
            if (name in allowedNames) return true
            if (excludedKeywords.any { it in name }) return false
            if (equipSlot(id) == EquipmentSlot.AMMO) return true
            if (allowedSubstrings.any { it in name }) return true
            if (name.startsWith("afro") || name.startsWith("ring") || name.startsWith("amulet")) return true

            val bonuses = getConfiguration<IntArray>(ItemConfigParser.BONUS)
            return bonuses?.all { it == 0 } ?: true
        }

    /**
     * Gets requirement.
     *
     * @param skillId the skill id
     * @return the requirement
     */
    fun getRequirement(skillId: Int): Int {
        val requirements = getConfiguration<Map<Int, Int>>(ItemConfigParser.REQUIREMENTS) ?: return 0
        val level = requirements[skillId]
        return level ?: 0
    }

    val renderAnimationId: Int
        /**
         * Gets render animation id.
         *
         * @return the render animation id
         */
        get() = getConfiguration(ItemConfigParser.RENDER_ANIM_ID, 1426)

    override fun getId(): Int = id

    override fun setId(id: Int) {
        this.id = id
    }

    /**
     * Gets interface model id.
     *
     * @return the interface model id
     */
    fun getInterfaceModelId(): Int = interfaceModelId

    /**
     * Sets interface model id.
     *
     * @param interfaceModelId the interface model id
     */
    fun setInterfaceModelId(interfaceModelId: Int) {
        this.interfaceModelId = interfaceModelId
    }

    override fun getName(): String = name

    override fun setName(name: String) {
        this.name = name
    }

    val isPlayerType: Boolean
        /**
         * Is player type boolean.
         *
         * @return the boolean
         */
        get() = itemType == 0

    /**
     * Gets model zoom.
     *
     * @return the model zoom
     */
    fun getModelZoom(): Int = modelZoom

    /**
     * Sets model zoom.
     *
     * @param modelZoom the model zoom
     */
    fun setModelZoom(modelZoom: Int) {
        this.modelZoom = modelZoom
    }

    /**
     * Gets model rotation x.
     *
     * @return the model rotation x
     */
    fun getModelRotationX(): Int = modelRotationX

    /**
     * Sets model rotation x.
     *
     * @param modelRotation1 the model rotation 1
     */
    fun setModelRotationX(modelRotation1: Int) {
        this.modelRotationX = modelRotation1
    }

    /**
     * Gets model rotation y.
     *
     * @return the model rotation y
     */
    fun getModelRotationY(): Int = modelRotationY

    /**
     * Sets model rotation y.
     *
     * @param modelRotation2 the model rotation 2
     */
    fun setModelRotationY(modelRotation2: Int) {
        this.modelRotationY = modelRotation2
    }

    /**
     * Is stackable boolean.
     *
     * @return the boolean
     */
    fun isStackable(): Boolean = stackable || !this.unnoted

    /**
     * Sets stackable.
     *
     * @param stackable the stackable
     */
    fun setStackable(stackable: Boolean) {
        this.stackable = stackable
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    fun getValue(): Int = value

    /**
     * Has shop currency value boolean.
     *
     * @param currency the currency
     * @return the boolean
     */
    fun hasShopCurrencyValue(currency: String?): Boolean = getHandlers().getOrDefault(currency, "0") !== "0"

    /**
     * Has shop currency value boolean.
     *
     * @param currency the currency
     * @return the boolean
     */
    fun hasShopCurrencyValue(currency: Int): Boolean = when (currency) {
        Items.COINS_995 -> isTradeable
        Items.TOKKUL_6529 -> hasShopCurrencyValue(ItemConfigParser.TOKKUL_PRICE)
        Items.ARCHERY_TICKET_1464 -> hasShopCurrencyValue(ItemConfigParser.ARCHERY_TICKET_PRICE)
        Items.CASTLE_WARS_TICKET_4067 -> hasShopCurrencyValue(ItemConfigParser.CASTLE_WARS_TICKET_PRICE)
        else -> false
    }

    val maxValue: Int
        /**
         * Gets max value.
         *
         * @return the max value
         */
        get() {
            if ((value * 1.05).toInt() <= 0) {
                return 1
            }
            return (value * 1.05).toInt()
        }

    val minValue: Int
        /**
         * Gets min value.
         *
         * @return the min value
         */
        get() {
            if ((value * .95).toInt() <= 0) {
                return 1
            }
            return (value * .95).toInt()
        }

    /**
     * Sets value.
     *
     * @param value the value
     */
    fun setValue(value: Int) {
        this.value = value
    }

    /**
     * Is members only boolean.
     *
     * @return the boolean
     */
    fun isMembersOnly(): Boolean = membersOnly

    /**
     * Sets members only.
     *
     * @param membersOnly the members only
     */
    fun setMembersOnly(membersOnly: Boolean) {
        this.membersOnly = membersOnly
    }

    /**
     * Gets male worn model id 1.
     *
     * @return the male worn model id 1
     */
    fun getMaleWornModelId1(): Int = maleWornModelId1

    /**
     * Sets male worn model id 1.
     *
     * @param maleWornModelId1 the male worn model id 1
     */
    fun setMaleWornModelId1(maleWornModelId1: Int) {
        this.maleWornModelId1 = maleWornModelId1
    }

    /**
     * Gets female worn model id 1.
     *
     * @return the female worn model id 1
     */
    fun getFemaleWornModelId1(): Int = femaleWornModelId1

    /**
     * Sets female worn model id 1.
     *
     * @param femaleWornModelId1 the female worn model id 1
     */
    fun setFemaleWornModelId1(femaleWornModelId1: Int) {
        this.femaleWornModelId1 = femaleWornModelId1
    }

    /**
     * Gets male worn model id 2.
     *
     * @return the male worn model id 2
     */
    fun getMaleWornModelId2(): Int = maleWornModelId2

    /**
     * Sets male worn model id 2.
     *
     * @param maleWornModelId2 the male worn model id 2
     */
    fun setMaleWornModelId2(maleWornModelId2: Int) {
        this.maleWornModelId2 = maleWornModelId2
    }

    /**
     * Gets female worn model id 2.
     *
     * @return the female worn model id 2
     */
    fun getFemaleWornModelId2(): Int = femaleWornModelId2

    /**
     * Sets female worn model id 2.
     *
     * @param femaleWornModelId2 the female worn model id 2
     */
    fun setFemaleWornModelId2(femaleWornModelId2: Int) {
        this.femaleWornModelId2 = femaleWornModelId2
    }

    val inventoryOptions: Array<String>
        /**
         * Get inventory options string.
         *
         * @return the string
         */
        get() = options

    /**
     * Sets inventory options.
     *
     * @param inventoryOptions the inventory options
     */
    fun setInventoryOptions(inventoryOptions: Array<String?>?) {
        this.options = inventoryOptions
    }

    /**
     * Get original model colors short.
     *
     * @return the short
     */
    fun getOriginalModelColors(): ShortArray? = originalModelColors

    /**
     * Sets original model colors.
     *
     * @param originalModelColors the original model colors
     */
    fun setOriginalModelColors(originalModelColors: ShortArray) {
        this.originalModelColors = originalModelColors
    }

    /**
     * Get modified model colors short.
     *
     * @return the short
     */
    fun getModifiedModelColors(): ShortArray? = modifiedModelColors

    /**
     * Sets modified model colors.
     *
     * @param modifiedModelColors the modified model colors
     */
    fun setModifiedModelColors(modifiedModelColors: ShortArray) {
        this.modifiedModelColors = modifiedModelColors
    }

    /**
     * Get texture colour 1 short.
     *
     * @return the short
     */
    fun getTextureColour1(): ShortArray? = textureColour1

    /**
     * Sets texture colour 1.
     *
     * @param textureColour1 the texture colour 1
     */
    fun setTextureColour1(textureColour1: ShortArray) {
        this.textureColour1 = textureColour1
    }

    /**
     * Get texture colour 2 short.
     *
     * @return the short
     */
    fun getTextureColour2(): ShortArray? = textureColour2

    /**
     * Sets texture colour 2.
     *
     * @param textureColour2 the texture colour 2
     */
    fun setTextureColour2(textureColour2: ShortArray) {
        this.textureColour2 = textureColour2
    }

    /**
     * Get unknown array 1 byte.
     *
     * @return the byte
     */
    fun getUnknownArray1(): ByteArray? = unknownArray1

    /**
     * Sets unknown array 1.
     *
     * @param unknownArray1 the unknown array 1
     */
    fun setUnknownArray1(unknownArray1: ByteArray) {
        this.unknownArray1 = unknownArray1
    }

    /**
     * Is unnoted boolean.
     *
     * @return the boolean
     */
    fun isUnnoted(): Boolean {
        return unnoted
    }

    /**
     * Sets isUnnoted.
     *
     * @param unnoted the isUnnoted
     */
    fun setUnnoted(unnoted: Boolean) {
        this.unnoted = unnoted
    }

    /**
     * Gets colour equip 1.
     *
     * @return the colour equip 1
     */
    fun getColourEquip1(): Int = colourEquip1

    /**
     * Sets colour equip 1.
     *
     * @param colourEquip1 the colour equip 1
     */
    fun setColourEquip1(colourEquip1: Int) {
        this.colourEquip1 = colourEquip1
    }

    /**
     * Gets colour equip 2.
     *
     * @return the colour equip 2
     */
    fun getColourEquip2(): Int = colourEquip2

    /**
     * Sets colour equip 2.
     *
     * @param colourEquip2 the colour equip 2
     */
    fun setColourEquip2(colourEquip2: Int) {
        this.colourEquip2 = colourEquip2
    }

    /**
     * Gets note id.
     *
     * @return the note id
     */
    fun getNoteId(): Int = noteId

    /**
     * Sets note id.
     *
     * @param noteId the note id
     */
    fun setNoteId(noteId: Int) {
        this.noteId = noteId
    }

    /**
     * Gets note template id.
     *
     * @return the note template id
     */
    fun getNoteTemplateId(): Int = noteTemplateId

    /**
     * Sets note template id.
     *
     * @param noteTemplateId the note template id
     */
    fun setNoteTemplateId(noteTemplateId: Int) {
        this.noteTemplateId = noteTemplateId
    }

    /**
     * Get stack ids int.
     *
     * @return the int
     */
    fun getStackIds(): IntArray? = stackIds

    /**
     * Sets stack ids.
     *
     * @param stackIds the stack ids
     */
    fun setStackIds(stackIds: IntArray?) {
        this.stackIds = stackIds
    }

    /**
     * Get stack amounts int.
     *
     * @return the int
     */
    fun getStackAmounts(): IntArray? = stackAmounts

    /**
     * Sets stack amounts.
     *
     * @param stackAmounts the stack amounts
     */
    fun setStackAmounts(stackAmounts: IntArray) {
        this.stackAmounts = stackAmounts
    }

    /**
     * Gets team id.
     *
     * @return the team id
     */
    fun getTeamId(): Int = teamId

    /**
     * Sets team id.
     *
     * @param teamId the team id
     */
    fun setTeamId(teamId: Int) {
        this.teamId = teamId
    }

    /**
     * Gets lend id.
     *
     * @return the lend id
     */
    fun getLendId(): Int = lendId

    /**
     * Sets lend id.
     *
     * @param lendId the lend id
     */
    fun setLendId(lendId: Int) {
        this.lendId = lendId
    }

    /**
     * Gets lend template id.
     *
     * @return the lend template id
     */
    fun getLendTemplateId(): Int = lendTemplateId

    /**
     * Sets lend template id.
     *
     * @param lendTemplateId the lend template id
     */
    fun setLendTemplateId(lendTemplateId: Int) {
        this.lendTemplateId = lendTemplateId
    }

    /**
     * Gets equip id.
     *
     * @return the equip id
     */
    fun getEquipId(): Int = equipId

    /**
     * Sets equip id.
     *
     * @param equipId the equip id
     */
    fun setEquipId(equipId: Int) {
        this.equipId = equipId
    }

    /**
     * Gets client script data.
     *
     * @return the client script data
     */
    fun getClientScriptData(): HashMap<Int, Any>? = clientScriptData

    /**
     * Sets client script data.
     *
     * @param clientScriptData the client script data
     */
    fun setClientScriptData(clientScriptData: HashMap<Int, Any>?) {
        this.clientScriptData = clientScriptData
    }

    /**
     * Gets alchemy value.
     *
     * @param highAlchemy the high alchemy
     * @return the alchemy value
     */
    fun getAlchemyValue(highAlchemy: Boolean): Int {
        if (!isUnnoted() && noteId > -1) {
            return forId(noteId).getAlchemyValue(highAlchemy)
        }
        if (highAlchemy) {
            return getConfiguration(ItemConfigParser.HIGH_ALCHEMY, round(value * 0.6).toInt())
        }
        return getConfiguration(ItemConfigParser.LOW_ALCHEMY, round(value * 0.4).toInt())
    }

    val isAlchemizable: Boolean
        /**
         * Is alchemizable boolean.
         *
         * @return the boolean
         */
        get() {
            if (!getConfiguration(ItemConfigParser.ALCHEMIZABLE, false)) {
                return false
            }
            return true
        }

    val isTradeable: Boolean
        /**
         * Is tradeable boolean.
         *
         * @return the boolean
         */
        get() {
            if (hasDestroyAction() && !getName().contains("impling jar")) {
                return false
            }
            if (!getConfiguration(ItemConfigParser.TRADEABLE, false)) {
                return false
            }
            return true
        }

    /**
     * Has action boolean.
     *
     * @param optionName the option name
     * @return the boolean
     */
    fun hasAction(optionName: String?): Boolean {
        if (options == null) {
            return false
        }
        for (action in options) {
            if (action == null) {
                continue
            }
            if (action.equals(optionName, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    /**
     * Has destroy action boolean.
     *
     * @return the boolean
     */
    fun hasDestroyAction(): Boolean = hasAction("destroy") || hasAction("dissolve")

    /**
     * Has wear action boolean.
     *
     * @return the boolean
     */
    fun hasWearAction(): Boolean {
        if (options == null) {
            return false
        }
        for (action in options) {
            if (action == null) {
                continue
            }
            if (action.equals("wield", ignoreCase = true) || action.equals(
                    "wear",
                    ignoreCase = true,
                ) || action.equals("equip", ignoreCase = true)
            ) {
                return true
            }
        }
        return false
    }

    /**
     * Has special bar boolean.
     *
     * @return the boolean
     */
    fun hasSpecialBar(): Boolean {
        if (clientScriptData == null) {
            return false
        }
        val specialBar = clientScriptData!![686]
        if (specialBar != null && specialBar is Int) {
            return specialBar == 1
        }
        return false
    }

    val questId: Int
        /**
         * Gets quest id.
         *
         * @return the quest id
         */
        get() {
            if (clientScriptData == null) {
                return -1
            }
            val questId = clientScriptData!![861]
            if (questId != null && questId is Int) {
                return questId
            }
            return -1
        }

    val archiveId: Int
        /**
         * Gets archive id.
         *
         * @return the archive id
         */
        get() = id ushr 8

    val fileId: Int
        /**
         * Gets file id.
         *
         * @return the file id
         */
        get() = 0xff and id

    /**
     * Instantiates a new Item definition.
     */
    init {
        groundOptions = arrayOf(null, null, "take", null, null)
        options = arrayOf(null, null, null, null, "drop")
    }

    /**
     * Has plugin boolean.
     *
     * @return the boolean
     */
    fun hasPlugin(): Boolean = itemPlugin != null

    var itemPlugin: ItemPlugin?
        /**
         * Gets item plugin.
         *
         * @return the item plugin
         */
        get() = getConfiguration<ItemPlugin>("wrapper", null)
        /**
         * Sets item plugin.
         *
         * @param plugin the plugin
         */
        set(plugin) {
            getHandlers()["wrapper"] = plugin
        }

    /**
     * Gets item type.
     *
     * @return the item type
     */
    fun getItemType(): Int = itemType

    /**
     * Sets item type.
     *
     * @param itemType the item type
     */
    fun setItemType(itemType: Int) {
        this.itemType = itemType
    }

    override fun getExamine(): String {
        examine = super.getExamine()
        if (!isUnnoted()) {
            examine = "Swap this note at any bank for the equivalent item."
        }
        return examine
    }

    companion object {
        /**
         * Gets definitions.
         *
         * @return the definitions
         */
        @JvmField
        val definitions: MutableMap<Int, ItemDefinition> = HashMap()
        private val OPTION_HANDLERS: MutableMap<String, OptionHandler?> = HashMap()

        /**
         * Parse.
         */
        fun parse() {
            for (itemId in 0 until getIndexCapacity(Archive.JS5_CONFIG_OBJ)) {
                val data = getData(Archive.JS5_CONFIG_OBJ, itemId ushr 8, itemId and 0xFF)

                if (data == null) {
                    definitions[itemId] = ItemDefinition()
                    continue
                }
                val def = decode(itemId, ByteBuffer.wrap(data))
                if (def == null) {
                    log(
                        ItemDefinition::class.java,
                        Log.ERR,
                        "Could not load item definitions for id $itemId - no definitions found!",
                    )
                    return
                }
                definitions[itemId] = def
            }
            defineTemplates()
        }

        /**
         * For id item definition.
         *
         * @param itemId the item id
         * @return the item definition
         */
        @JvmStatic
        fun forId(itemId: Int): ItemDefinition {
            val def = definitions[itemId]
            if (def != null) {
                return def
            }
            return ItemDefinition()
        }

        /**
         * Parse definition item definition.
         *
         * @param itemId the item id
         * @param buffer the buffer
         * @return the item definition
         */
        private fun decode(itemId: Int, buffer: ByteBuffer): ItemDefinition {
            val def = ItemDefinition()
            def.id = itemId
            while (true) {
                val opcode = buffer.g1()
                if (opcode == 0) break
                when (opcode) {
                    1 -> def.interfaceModelId = buffer.g2()
                    2 -> def.name = buffer.gjstr()
                    3 -> def.handlers["examine"] = buffer.gjstr()
                    4 -> def.modelZoom = buffer.g2()
                    5 -> def.modelRotationX = buffer.g2()
                    6 -> def.modelRotationY = buffer.g2()
                    7 -> {
                        def.modelOffset1 = buffer.g2()
                        if (def.modelOffset1 > 32767) def.modelOffset1 -= 65536
                    }

                    8 -> {
                        def.modelOffset2 = buffer.g2()
                        if (def.modelOffset2 > 32767) def.modelOffset2 -= 65536
                    }

                    10 -> {}
                    11 -> def.stackable = true
                    12 -> def.value = buffer.g4()
                    16 -> def.membersOnly = true
                    23 -> def.maleWornModelId1 = buffer.g2()
                    24 -> def.femaleWornModelId1 = buffer.g2()
                    25 -> def.maleWornModelId2 = buffer.g2()
                    26 -> def.femaleWornModelId2 = buffer.g2()
                    in 30..34 -> def.groundOptions[opcode - 30] = buffer.gjstr()
                    in 35..39 -> def.options[opcode - 35] = buffer.gjstr()
                    40 -> {
                        val length = buffer.g1()
                        def.originalModelColors = ShortArray(length)
                        def.modifiedModelColors = ShortArray(length)
                        for (i in 0 until length) {
                            def.originalModelColors!![i] = buffer.g2b().toShort()
                            def.modifiedModelColors!![i] = buffer.g2b().toShort()
                        }
                    }

                    41 -> {
                        val length = buffer.g1()
                        def.textureColour1 = ShortArray(length)
                        def.textureColour2 = ShortArray(length)
                        for (i in 0 until length) {
                            def.textureColour1!![i] = buffer.g2b().toShort()
                            def.textureColour2!![i] = buffer.g2b().toShort()
                        }
                    }

                    42 -> {
                        val length = buffer.g1()
                        def.unknownArray1 = ByteArray(length)
                        for (i in 0 until length) def.unknownArray1!![i] = buffer.get()
                    }

                    65 -> def.unnoted = true
                    78 -> def.colourEquip1 = buffer.g2()
                    79 -> def.colourEquip2 = buffer.g2()
                    90 -> def.maleWornModelId3 = buffer.g2()
                    91 -> def.femaleWornModelId3 = buffer.g2()
                    92 -> def.maleWornModelId4 = buffer.g2()
                    93 -> def.femaleWornModelId4 = buffer.g2()
                    95 -> buffer.g2()
                    96 -> def.itemType = buffer.g1()
                    97 -> def.noteId = buffer.g2()
                    98 -> def.noteTemplateId = buffer.g2()
                    in 100..109 -> {
                        if (def.stackIds == null) {
                            def.stackIds = IntArray(10)
                            def.stackAmounts = IntArray(10)
                        }
                        def.stackIds!![opcode - 100] = buffer.g2()
                        def.stackAmounts!![opcode - 100] = buffer.g2()
                    }

                    110, 111, 112 -> buffer.g2()
                    113, 114 -> buffer.g1()
                    115 -> def.teamId = buffer.g1()
                    121 -> def.lendId = buffer.g2()
                    122 -> def.lendTemplateId = buffer.g2()
                    125, 126 -> {
                        buffer.g1()
                        buffer.g1()
                        buffer.g1()
                    }

                    127, 128, 129, 130 -> {
                        buffer.g1()
                        buffer.g2()
                    }

                    249 -> {
                        val length = buffer.g1()
                        if (def.clientScriptData == null) def.clientScriptData = HashMap()
                        for (i in 0 until length) {
                            val isString = buffer.g1() == 1
                            val key = buffer.g3()
                            val value: Any = if (isString) buffer.gjstr() else buffer.g4()
                            def.clientScriptData!![key] = value
                        }
                    }

                    else -> break
                }
            }
            return def
        }

        /**
         * Define templates.
         */
        fun defineTemplates() {
            var equipId = 0
            for (i in 0 until getIndexCapacity(Archive.JS5_CONFIG_OBJ)) {
                val def = forId(i)
                if (def.noteTemplateId != -1) {
                    def.transferNoteDefinition(forId(def.noteId), forId(def.noteTemplateId))
                }
                if (def.lendTemplateId != -1) {
                    def.transferLendDefinition(forId(def.lendId), forId(def.lendTemplateId))
                }
                if (def.recolourTemplateId != -1) {
                    def.transferRecolourDefinition(forId(def.recolourId), forId(def.recolourTemplateId))
                }
                if (def != null && (def.maleWornModelId1 >= 0 || def.maleWornModelId2 >= 0)) {
                    def.equipId = equipId++
                }
            }
            forId(Items.DRAGON_CHAINBODY_2513).equipId = forId(Items.DRAGON_CHAINBODY_3140).equipId
        }

        /**
         * Checks if the player can enter Entrana.
         *
         * @param player the player who will be checked.
         * @return `true` can enter Entrana, `false` otherwise.
         */
        @JvmStatic
        fun canEnterEntrana(player: Player): Boolean {
            if (!checkContainer(player.inventory)) return false
            if (!checkContainer(player.equipment)) return false

            val familiar = player.familiarManager.familiar
            if (player.familiarManager.hasFamiliar() && familiar.isBurdenBeast) {
                val burden = familiar as BurdenBeast
                if (!checkContainer(burden.container)) return false
            }

            return true
        }

        private fun checkContainer(container: Container): Boolean {
            for (item in container.toArray()) {
                if (item != null && !item.definition.isAllowed) return false
            }
            return true
        }

        private val permittedItems = hashSetOf(
            Items.COMBAT_BRACELET1_11124,
            Items.COMBAT_BRACELET2_11122,
            Items.COMBAT_BRACELET3_11120,
            Items.COMBAT_BRACELET4_11118,
            Items.REGEN_BRACELET_11133,
            Items.BOOTS_OF_LIGHTNESS_88,
            Items.CLIMBING_BOOTS_3105,
            Items.BLUE_BERET_2633,
            Items.BLACK_BERET_2635,
            Items.WHITE_BERET_2637,
            Items.BROOMSTICK_14057,
            Items.SPOTTED_CAPE_10069,
            Items.SPOTTIER_CAPE_10071,
            Items.SARADOMIN_CAPE_2412,
            Items.ZAMORAK_CAPE_2414,
            Items.GUTHIX_CAPE_2413,
            Items.SARADOMIN_CLOAK_10446,
            Items.ZAMORAK_CLOAK_10450,
            Items.GUTHIX_CLOAK_10448,
            Items.TAN_CAVALIER_2639,
            Items.BLACK_CAVALIER_2643,
            Items.DARK_CAVALIER_2641,
            Items.DAVY_KEBBIT_HAT_12568,
            Items.FIXED_DEVICE_6082,
            Items.FLARED_TROUSERS_10394,
            Items.GIANTS_HAND_13666,
            Items.GNOME_SCARF_9470,
            Items.HOLY_BOOK_3840,
            Items.DAMAGED_BOOK_3839,
            Items.UNHOLY_BOOK_3842,
            Items.DAMAGED_BOOK_3841,
            Items.BOOK_OF_BALANCE_3844,
            Items.DAMAGED_BOOK_3843,
            Items.HAM_SHIRT_4298,
            Items.HAM_ROBE_4300,
            Items.HAM_HOOD_4302,
            Items.HAM_CLOAK_4304,
            Items.HAM_LOGO_4306,
            Items.GLOVES_4308,
            Items.BOOTS_4310,
            Items.ICE_GLOVES_1580,
            Items.MIND_HELMET_9733,
            Items.MONKS_ROBE_542,
            Items.MONKS_ROBE_544,
            Items.PRIEST_GOWN_426,
            Items.PRIEST_GOWN_428,
            Items.SHADE_ROBE_546,
            Items.SHADE_ROBE_548,
            Items.ZAMORAK_ROBE_1033,
            Items.ZAMORAK_ROBE_1035,
            Items.NURSE_HAT_6548,
            Items.OMNI_TIARA_13655,
            Items.PENANCE_GLOVES_10553,
            Items.PET_ROCK_3695,
            Items.SALVE_AMULETE_10588,
            Items.SKELETAL_BOOTS_6147,
            Items.SKELETAL_GLOVES_6153,
            Items.SKIRT_5048,
            Items.WARLOCK_TOP_14076,
            Items.WARLOCK_LEGS_14077,
            Items.WARLOCK_CLOAK_14081,
            Items.WIZARD_BOOTS_2579,
            Items.BONES_TO_BANANAS_8014,
            Items.BONES_TO_PEACHES_8015,
            Items.ENCHANT_SAPPHIRE_8016,
            Items.ENCHANT_EMERALD_8017,
            Items.ENCHANT_RUBY_8018,
            Items.ENCHANT_DIAMOND_8019,
            Items.ENCHANT_DRAGONSTN_8020,
            Items.ENCHANT_ONYX_8021,
            Items.ECTOPHIAL_4251
        )

        private val bannedItems = hashSetOf(
            Items.CAPE_OF_LEGENDS_1052,
            Items.FIRE_CAPE_6570,
            Items.AVAS_ATTRACTOR_10498,
            Items.AVAS_ACCUMULATOR_10499,
            Items.COOKING_GAUNTLETS_775,
            Items.CHAOS_GAUNTLETS_777,
            Items.GOLDSMITH_GAUNTLETS_776,
            Items.SILVER_SICKLE_2961,
            Items.SILVER_SICKLEB_2963,
            Items.SILVER_SICKLE_EMERALDB_13155,
            Items.KARAMJA_GLOVES_1_11136,
            Items.KARAMJA_GLOVES_2_11138,
            Items.KARAMJA_GLOVES_3_11140,
            Items.EXPLORERS_RING_1_13560,
            Items.EXPLORERS_RING_2_13561,
            Items.EXPLORERS_RING_3_13562,
            Items.FANCY_BOOTS_9005,
            Items.FIGHTING_BOOTS_9006,
            Items.VYREWATCH_TOP_9634,
            Items.VYREWATCH_LEGS_9636,
            Items.VYREWATCH_SHOES_9638,
            Items.BARB_TAIL_HARPOON_10129,
            Items.BUTTERFLY_NET_10010,
            Items.MIME_MASK_3057,
            Items.DWARF_CANNON_SET_11967,
            Items.CANNON_BARRELS_10,
            Items.CANNON_BASE_6,
            Items.CANNON_STAND_8,
            Items.CANNON_FURNACE_12,
            Items.HOLY_WATER_732,
            Items.ENCHANTED_WATER_TIARA_11969,
            Items.OMNI_TALISMAN_STAFF_13642,
            Items.FREMENNIK_SEA_BOOTS_1_14571,
            Items.FREMENNIK_SEA_BOOTS_2_14572,
            Items.FREMENNIK_SEA_BOOTS_3_14573,
            Items.ROBIN_HOOD_HAT_2581,
            Items.SAFETY_GLOVES_12629,
            Items.FALADOR_SHIELD_1_14577,
            Items.FALADOR_SHIELD_2_14578,
            Items.FALADOR_SHIELD_3_14579,
            Items.AGILE_TOP_14696,
            Items.AGILE_TOP_14697,
            Items.AGILE_LEGS_14698,
            Items.AGILE_LEGS_14699,
            Items.PROSYTE_HARNESS_M_9666,
            Items.INITIATE_HARNESS_M_9668,
            Items.PROSYTE_HARNESS_F_9670
        )

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
            if (def == null) {
                if (nodeId == 22937) {
                    log(
                        ItemDefinition::class.java,
                        Log.ERR,
                        "[ItemDefinition] No definition for item id $nodeId!",
                    )
                }
                return null
            }
            val handler = def.getConfiguration<OptionHandler>("option:$name")
            if (handler != null) {
                return handler
            }
            return OPTION_HANDLERS[name]
        }

        private val BONUS_NAMES = arrayOf(
            "Stab: ",
            "Slash: ",
            "Crush: ",
            "Magic: ",
            "Ranged: ",
            "Stab: ",
            "Slash: ",
            "Crush: ",
            "Magic: ",
            "Ranged: ",
            "Summoning: ",
            "Strength: ",
            "Prayer: ",
        )

        /**
         * Stats update.
         *
         * @param player the player
         */
        fun statsUpdate(player: Player) {
            if (!player.getAttribute("equip_stats_open", false)) {
                return
            }
            var index = 0
            val bonuses = player.properties.bonuses
            for (i in 36..49) {
                if (i == 47) {
                    continue
                }
                val bonus = bonuses[index]
                val bonusValue = if (bonus > -1) ("+$bonus") else bonus.toString()
                sendString(player, BONUS_NAMES[index++] + bonusValue, Components.EQUIP_SCREEN2_667, i)
            }
            sendString(player, "Attack bonus", Components.EQUIP_SCREEN2_667, 34)
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

        val optionHandlers: Map<String, OptionHandler?>
            /**
             * Gets option handlers.
             *
             * @return the option handlers
             */
            get() = OPTION_HANDLERS
    }
}
