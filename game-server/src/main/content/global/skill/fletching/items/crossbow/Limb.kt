package content.global.skill.fletching.items.crossbow

import org.rs.consts.Items

/**
 * Represents a types of limbs for crossbows.
 */
enum class Limb(val stock: Int, val limb: Int, val product: Int, val level: Int, val experience: Double, val animation: Int, ) {
    WOODEN_STOCK(Items.WOODEN_STOCK_9440, Items.BRONZE_LIMBS_9420, Items.BRONZE_CBOW_U_9454, 9, 12.0, 4436),
    OAK_STOCK(Items.OAK_STOCK_9442, Items.BLURITE_LIMBS_9422, Items.BLURITE_CBOW_U_9456, 24, 32.0, 4437),
    WILLOW_STOCK(Items.WILLOW_STOCK_9444, Items.IRON_LIMBS_9423, Items.IRON_CBOW_U_9457, 39, 44.0, 4438),
    TEAK_STOCK(Items.TEAK_STOCK_9446, Items.STEEL_LIMBS_9425, Items.STEEL_CBOW_U_9459, 46, 54.0, 4439),
    MAPLE_STOCK(Items.MAPLE_STOCK_9448, Items.MITHRIL_LIMBS_9427, Items.MITHRIL_CBOW_U_9461, 54, 64.0, 4440),
    MAHOGANY_STOCK(Items.MAHOGANY_STOCK_9450, Items.ADAMANTITE_LIMBS_9429, Items.ADAMANT_CBOW_U_9463, 61, 82.0, 4441),
    YEW_STOCK(Items.YEW_STOCK_9452, Items.RUNITE_LIMBS_9431, Items.RUNITE_CBOW_U_9465, 69, 100.0, 4442),
    ;

    companion object {
        val values = enumValues<Limb>()
        val product = values.associateBy { it.stock }
    }
}
