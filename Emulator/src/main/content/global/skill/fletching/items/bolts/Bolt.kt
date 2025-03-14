package content.global.skill.fletching.items.bolts

import org.rs.consts.Items

/**
 * Represents types of bolts.
 *
 * @property unfinished The unfinished bolt item id.
 * @property finished   The finished bolt item id.
 * @property level      The required level to fletch the bolt.
 * @property experience The experience gained from crafting the bolt.
 */
enum class Bolt(
    val unfinished: Int,
    val finished: Int,
    val level: Int,
    val experience: Double,
) {
    BRONZE_BOLT(
        unfinished = Items.BRONZE_BOLTS_UNF_9375,
        finished = Items.BRONZE_BOLTS_877,
        level = 9,
        experience = 0.5,
    ),
    BLURITE_BOLT(
        unfinished = Items.BLURITE_BOLTS_UNF_9376,
        finished = Items.BLURITE_BOLTS_9139,
        level = 24,
        experience = 1.0,
    ),
    IRON_BOLT(
        unfinished = Items.IRON_BOLTS_UNF_9377,
        finished = Items.IRON_BOLTS_9140,
        level = 39,
        experience = 1.5,
    ),
    SILVER_BOLT(
        unfinished = Items.SILVER_BOLTS_UNF_9382,
        finished = Items.SILVER_BOLTS_9145,
        level = 43,
        experience = 2.5,
    ),
    STEEL_BOLT(
        unfinished = Items.STEEL_BOLTS_UNF_9378,
        finished = Items.STEEL_BOLTS_9141,
        level = 46,
        experience = 3.5,
    ),
    MITHRIL_BOLT(
        unfinished = Items.MITHRIL_BOLTS_UNF_9379,
        finished = Items.MITHRIL_BOLTS_9142,
        level = 54,
        experience = 5.0,
    ),
    BROAD_BOLT(
        unfinished = Items.BROAD_BOLTS_UNF_13279,
        finished = Items.BROAD_TIPPED_BOLTS_13280,
        level = 55,
        experience = 3.0,
    ),
    ADAMANT_BOLT(
        unfinished = Items.ADAMANT_BOLTS_UNF_9380,
        finished = Items.ADAMANT_BOLTS_9143,
        level = 61,
        experience = 7.0,
    ),
    RUNITE_BOLT(
        unfinished = Items.RUNITE_BOLTS_UNF_9381,
        finished = Items.RUNE_BOLTS_9144,
        level = 69,
        experience = 10.0,
    ),
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
