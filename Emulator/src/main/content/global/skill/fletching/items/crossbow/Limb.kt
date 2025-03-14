package content.global.skill.fletching.items.crossbow

import org.rs.consts.Items

/**
 * Represents a types of limbs for crossbows.
 *
 * @property stock      The item id for the stock.
 * @property limb       The item id for the limb.
 * @property product    The item id for the finished product.
 * @property level      The required level to fletch.
 * @property experience The experience gained from fletch.
 * @property animation  The animation id used during the pulse.
 */
enum class Limb(
    val stock: Int,
    val limb: Int,
    val product: Int,
    val level: Int,
    val experience: Double,
    val animation: Int,
) {
    WOODEN_STOCK(
        stock = Items.WOODEN_STOCK_9440,
        limb = Items.BRONZE_LIMBS_9420,
        product = Items.BRONZE_CBOW_U_9454,
        level = 9,
        experience = 12.0,
        animation = 4436,
    ),
    OAK_STOCK(
        stock = Items.OAK_STOCK_9442,
        limb = Items.BLURITE_LIMBS_9422,
        product = Items.BLURITE_CBOW_U_9456,
        level = 24,
        experience = 32.0,
        animation = 4437,
    ),
    WILLOW_STOCK(
        stock = Items.WILLOW_STOCK_9444,
        limb = Items.IRON_LIMBS_9423,
        product = Items.IRON_CBOW_U_9457,
        level = 39,
        experience = 44.0,
        animation = 4438,
    ),
    TEAK_STOCK(
        stock = Items.TEAK_STOCK_9446,
        limb = Items.STEEL_LIMBS_9425,
        product = Items.STEEL_CBOW_U_9459,
        level = 46,
        experience = 54.0,
        animation = 4439,
    ),
    MAPLE_STOCK(
        stock = Items.MAPLE_STOCK_9448,
        limb = Items.MITHRIL_LIMBS_9427,
        product = Items.MITHRIL_CBOW_U_9461,
        level = 54,
        experience = 64.0,
        animation = 4440,
    ),
    MAHOGANY_STOCK(
        stock = Items.MAHOGANY_STOCK_9450,
        limb = Items.ADAMANTITE_LIMBS_9429,
        product = Items.ADAMANT_CBOW_U_9463,
        level = 61,
        experience = 82.0,
        animation = 4441,
    ),
    YEW_STOCK(
        stock = Items.YEW_STOCK_9452,
        limb = Items.RUNITE_LIMBS_9431,
        product = Items.RUNITE_CBOW_U_9465,
        level = 69,
        experience = 100.0,
        animation = 4442,
    ),
    ;

    companion object {
        val values = enumValues<Limb>()
        val product = values.associateBy { it.stock }
    }
}
