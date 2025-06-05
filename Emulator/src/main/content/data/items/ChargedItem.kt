package content.data.items

import core.api.toIntArray
import core.cache.def.impl.ItemDefinition
import org.rs.consts.Items

/**
 * Represents various charged items and their respective item IDs for different charge levels.
 * @author RiL
 */
enum class ChargedItem(val ids: IntArray, ) {
    AMULET_OF_GLORY(ids = (Items.AMULET_OF_GLORY4_1712 downTo Items.AMULET_OF_GLORY_1704 step 2).toIntArray()),
    RING_OF_DUELLING(ids = (Items.RING_OF_DUELLING8_2552..Items.RING_OF_DUELLING1_2566 step 2).toIntArray()),
    GAMES_NECKLACE(ids = (Items.GAMES_NECKLACE8_3853..Items.GAMES_NECKLACE1_3867 step 2).toIntArray()),
    BROODOO_SHIELDA(ids = (Items.BROODOO_SHIELD_10_6215..Items.BROODOO_SHIELD_6235 step 2).toIntArray()),
    BROODOO_SHIELDB(ids = (Items.BROODOO_SHIELD_10_6237..Items.BROODOO_SHIELD_6257 step 2).toIntArray()),
    BROODOO_SHIELDC(ids = (Items.BROODOO_SHIELD_10_6259..Items.BROODOO_SHIELD_6279 step 2).toIntArray()),
    ROD_OF_IVANDIS(ids = (Items.ROD_OF_IVANDIS10_7639..Items.ROD_OF_IVANDIS1_7648).toIntArray()),
    BLACK_MASK(ids = (Items.BLACK_MASK_10_8901..Items.BLACK_MASK_8921 step 2).toIntArray()),
    AMULET_OF_GLORYT(ids = (Items.AMULET_OF_GLORYT4_10354..Items.AMULET_OF_GLORYT_10362 step 2).toIntArray()),
    CASTLEWAR_BRACE(ids = (Items.CASTLEWAR_BRACE3_11079..Items.CASTLEWAR_BRACE1_11083 step 2).toIntArray()),
    FORINTHRY_BRACE(ids = (Items.FORINTHRY_BRACE5_11095..Items.FORINTHRY_BRACE1_11103 step 2).toIntArray()),
    SKILLS_NECKLACE(ids = (Items.SKILLS_NECKLACE4_11105..Items.SKILLS_NECKLACE_11113 step 2).toIntArray()),
    COMBAT_BRACELET(ids = (Items.COMBAT_BRACELET4_11118..Items.COMBAT_BRACELET_11126 step 2).toIntArray()),
    DIGSITE_PENDANT(ids = (Items.DIGSITE_PENDANT_5_11194 downTo Items.DIGSITE_PENDANT_1_11190).toIntArray()),
    VOID_SEAL(ids = (Items.VOID_SEAL8_11666..Items.VOID_SEAL1_11673).toIntArray()),
    AMULET_OF_FARMING(ids = (Items.AMULET_OF_FARMING8_12622 downTo Items.AMULET_OF_FARMING1_12608 step 2).toIntArray()),
    IVANDIS_FLAIL(ids = (Items.IVANDIS_FLAIL_30_13117..Items.IVANDIS_FLAIL_1_13146).toIntArray()),
    RING_OF_SLAYING(ids = (Items.RING_OF_SLAYING8_13281..Items.RING_OF_SLAYING1_13288).toIntArray()),
    ;

    /**
     * Returns the id to the item at the specified charge level.
     *
     * @param charge The charge level (1-based index).
     * @return The item ID corresponding to the specified charge level.
     */
    fun forCharge(charge: Int): Int =
        ids[
            maxCharge() -
                if (charge < 1) {
                    1 + maxCharge() - ids.size
                } else if (charge > maxCharge()) {
                    maxCharge()
                } else {
                    charge
                },
        ]

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
        private fun getMaxCharge(chargedItem: ChargedItem): Int =
            CHARGE_REGEX
                .find(ItemDefinition.forId(chargedItem.ids.first()).name)!!
                .groups[1]!!
                .value
                .toInt()

        /**
         * Checks if an item ID is a valid-charged item.
         *
         * @param id The item id.
         * @return True if the item id to a charged item, false otherwise.
         */
        @JvmStatic
        fun contains(id: Int): Boolean = idMap.containsKey(id)

        /**
         * Returns the charged item for given id.
         *
         * @param id The item id.
         * @return The charged item for the specified id, or `null` if no match is found.
         */
        @JvmStatic
        fun forId(id: Int): ChargedItem? = idMap[id]

        /**
         * Returns the charge level for the specified item id.
         *
         * @param id The item ID.
         * @return The charge level, or `null` if the item is not a charged item.
         */
        @JvmStatic
        fun getCharge(id: Int): Int? {
            val chargedItem = forId(id) ?: return null
            return chargedItem.maxCharge() - chargedItem.ids.indexOf(id)
        }
    }
}
