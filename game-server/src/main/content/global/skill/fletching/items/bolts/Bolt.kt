package content.global.skill.fletching.items.bolts

import org.rs.consts.Items

/**
 * Represents types of bolts.
 */
enum class Bolt(val unfinished: Int, val finished: Int, val level: Int, val experience: Double) {
    BRONZE_BOLT(Items.BRONZE_BOLTS_UNF_9375, Items.BRONZE_BOLTS_877, 9, 0.5),
    BLURITE_BOLT(Items.BLURITE_BOLTS_UNF_9376, Items.BLURITE_BOLTS_9139, 24, 1.0),
    IRON_BOLT(Items.IRON_BOLTS_UNF_9377, Items.IRON_BOLTS_9140, 39, 1.5),
    SILVER_BOLT(Items.SILVER_BOLTS_UNF_9382, Items.SILVER_BOLTS_9145, 43, 2.5),
    STEEL_BOLT(Items.STEEL_BOLTS_UNF_9378, Items.STEEL_BOLTS_9141, 46, 3.5),
    MITHRIL_BOLT(Items.MITHRIL_BOLTS_UNF_9379, Items.MITHRIL_BOLTS_9142, 54, 5.0),
    BROAD_BOLT(Items.BROAD_BOLTS_UNF_13279, Items.BROAD_TIPPED_BOLTS_13280, 55, 3.0),
    ADAMANT_BOLT(Items.ADAMANT_BOLTS_UNF_9380, Items.ADAMANT_BOLTS_9143, 61, 7.0),
    RUNITE_BOLT(Items.RUNITE_BOLTS_UNF_9381, Items.RUNE_BOLTS_9144, 69, 10.0),
    ;

    companion object {
        val product = enumValues<Bolt>().associateBy { it.unfinished }

        /**
         * Checks if the given item id matches an unfinished bolt.
         *
         * @param id The item id to check.
         * @return `true` if the item id matches a bolt, otherwise `false`.
         */
        fun isBolt(id: Int): Boolean = product.containsKey(id)
    }
}
