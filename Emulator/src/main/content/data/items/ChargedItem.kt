package content.data.items

import core.api.toIntArray
import core.cache.def.impl.ItemDefinition
import org.rs.consts.Items

/**
 * Enum class representing various charged items and their respective item IDs for different charge levels.
 * Each enum value corresponds to an item that can have multiple charge levels, and the associated IDs for each level are defined.
 */
enum class ChargedItem(
    val ids: IntArray,
) {
    /**
     * Amulet of Glory with different charge levels.
     */
    AMULET_OF_GLORY(ids = (Items.AMULET_OF_GLORY4_1712 downTo Items.AMULET_OF_GLORY_1704 step 2).toIntArray()),

    /**
     * Ring of Duelling with different charge levels.
     */
    RING_OF_DUELLING(ids = (Items.RING_OF_DUELLING8_2552..Items.RING_OF_DUELLING1_2566 step 2).toIntArray()),

    /**
     * Games Necklace with different charge levels.
     */
    GAMES_NECKLACE(ids = (Items.GAMES_NECKLACE8_3853..Items.GAMES_NECKLACE1_3867 step 2).toIntArray()),

    /**
     * Broodoo Shield A with different charge levels.
     */
    BROODOO_SHIELDA(ids = (Items.BROODOO_SHIELD_10_6215..Items.BROODOO_SHIELD_6235 step 2).toIntArray()),

    /**
     * Broodoo Shield B with different charge levels.
     */
    BROODOO_SHIELDB(ids = (Items.BROODOO_SHIELD_10_6237..Items.BROODOO_SHIELD_6257 step 2).toIntArray()),

    /**
     * Broodoo Shield C with different charge levels.
     */
    BROODOO_SHIELDC(ids = (Items.BROODOO_SHIELD_10_6259..Items.BROODOO_SHIELD_6279 step 2).toIntArray()),

    /**
     * Rod of Ivandis with different charge levels.
     */
    ROD_OF_IVANDIS(ids = (Items.ROD_OF_IVANDIS10_7639..Items.ROD_OF_IVANDIS1_7648).toIntArray()),

    /**
     * Black Mask with different charge levels.
     */
    BLACK_MASK(ids = (Items.BLACK_MASK_10_8901..Items.BLACK_MASK_8921 step 2).toIntArray()),

    /**
     * Amulet of Glory T with different charge levels.
     */
    AMULET_OF_GLORYT(ids = (Items.AMULET_OF_GLORYT4_10354..Items.AMULET_OF_GLORYT_10362 step 2).toIntArray()),

    /**
     * Castlewar Brace with different charge levels.
     */
    CASTLEWAR_BRACE(ids = (Items.CASTLEWAR_BRACE3_11079..Items.CASTLEWAR_BRACE1_11083 step 2).toIntArray()),

    /**
     * Forinthry Brace with different charge levels.
     */
    FORINTHRY_BRACE(ids = (Items.FORINTHRY_BRACE5_11095..Items.FORINTHRY_BRACE1_11103 step 2).toIntArray()),

    /**
     * Skills Necklace with different charge levels.
     */
    SKILLS_NECKLACE(ids = (Items.SKILLS_NECKLACE4_11105..Items.SKILLS_NECKLACE_11113 step 2).toIntArray()),

    /**
     * Combat Bracelet with different charge levels.
     */
    COMBAT_BRACELET(ids = (Items.COMBAT_BRACELET4_11118..Items.COMBAT_BRACELET_11126 step 2).toIntArray()),

    /**
     * Digsite Pendant with different charge levels.
     */
    DIGSITE_PENDANT(ids = (Items.DIGSITE_PENDANT_5_11194 downTo Items.DIGSITE_PENDANT_1_11190).toIntArray()),

    /**
     * Void Seal with different charge levels.
     */
    VOID_SEAL(ids = (Items.VOID_SEAL8_11666..Items.VOID_SEAL1_11673).toIntArray()),

    /**
     * Amulet of Farming with different charge levels.
     */
    AMULET_OF_FARMING(ids = (Items.AMULET_OF_FARMING8_12622 downTo Items.AMULET_OF_FARMING1_12608 step 2).toIntArray()),

    /**
     * Ivandis Flail with different charge levels.
     */
    IVANDIS_FLAIL(ids = (Items.IVANDIS_FLAIL_30_13117..Items.IVANDIS_FLAIL_1_13146).toIntArray()),

    /**
     * Ring of Slaying with different charge levels.
     */
    RING_OF_SLAYING(ids = (Items.RING_OF_SLAYING8_13281..Items.RING_OF_SLAYING1_13288).toIntArray()),
    ;

    /**
     * Returns the ID corresponding to the item at the specified charge level.
     * If the charge is invalid (less than 1 or greater than the maximum charge), the closest valid ID will be returned.
     *
     * @param charge The charge level (1-based index).
     * @return The item ID corresponding to the specified charge level.
     */
    fun forCharge(charge: Int): Int {
        return ids[
            maxCharge() -
                if (charge < 1) {
                    1 + maxCharge() - ids.size
                } else if (charge > maxCharge()) {
                    maxCharge()
                } else {
                    charge
                },
        ]
    }

    /**
     * Returns the maximum charge level for this charged item.
     *
     * @return The maximum charge level.
     */
    fun maxCharge(): Int = maxCharges[ordinal]

    companion object {
        private val CHARGE_REGEX = Regex("""\(\D?(\d+)\)""")
        private val idMap = HashMap<Int, ChargedItem>()
        private val maxCharges = IntArray(values().size)

        init {
            values().forEach { chargedItem ->
                maxCharges[chargedItem.ordinal] = getMaxCharge(chargedItem)
                chargedItem.ids.forEach { idMap[it] = chargedItem }
            }
        }

        /**
         * Returns the maximum charge level for the specified charged item.
         *
         * @param chargedItem The charged item.
         * @return The maximum charge level.
         */
        @JvmStatic
        private fun getMaxCharge(chargedItem: ChargedItem): Int {
            return CHARGE_REGEX
                .find(ItemDefinition.forId(chargedItem.ids.first()).name)!!
                .groups[1]!!
                .value
                .toInt()
        }

        /**
         * Checks if an item ID is a valid charged item.
         *
         * @param id The item ID.
         * @return True if the item ID corresponds to a charged item, false otherwise.
         */
        @JvmStatic
        fun contains(id: Int): Boolean = idMap.containsKey(id)

        /**
         * Returns the charged item associated with the specified item ID.
         *
         * @param id The item ID.
         * @return The charged item corresponding to the specified ID, or null if no match is found.
         */
        @JvmStatic
        fun forId(id: Int): ChargedItem? = idMap[id]

        /**
         * Returns the charge level for the specified item ID.
         *
         * @param id The item ID.
         * @return The charge level, or null if the item is not a charged item.
         */
        @JvmStatic
        fun getCharge(id: Int): Int? {
            val chargedItem = forId(id) ?: return null
            return chargedItem.maxCharge() - chargedItem.ids.indexOf(id)
        }
    }
}
