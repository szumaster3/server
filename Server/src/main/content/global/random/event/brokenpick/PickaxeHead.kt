package content.global.random.event.brokenpick

import shared.consts.Items

/**
 * Represents types of pickaxe heads and their resulting pickaxe items.
 */
enum class PickaxeHead(val head: Int, val pickaxe: Int) {
    BRONZE(Items.BRONZE_PICK_HEAD_480, Items.BRONZE_PICKAXE_1265),
    IRON(Items.IRON_PICK_HEAD_482, Items.IRON_PICKAXE_1267),
    STEEL(Items.STEEL_PICK_HEAD_484, Items.STEEL_PICKAXE_1269),
    MITHRIL(Items.MITHRIL_PICK_HEAD_486, Items.MITHRIL_PICKAXE_1273),
    ADAMANT(Items.ADAMANT_PICK_HEAD_488, Items.ADAMANT_PICKAXE_1271),
    RUNE(Items.RUNE_PICK_HEAD_490, Items.RUNE_PICKAXE_1275);

    companion object {
        private val productMap: Map<Int, PickaxeHead> = values().associateBy { it.head }

        /**
         * Returns the [PickaxeHead] corresponding to the given head id.
         * @param headId the head item id.
         * @return the matching PickaxeHead, or null if not found
         */
        fun fromHeadId(headId: Int): PickaxeHead? = productMap[headId]

        /**
         * Returns a map of head ids to their [PickaxeHead] enums.
         */
        fun getProductMap(): Map<Int, PickaxeHead> = productMap
    }
}