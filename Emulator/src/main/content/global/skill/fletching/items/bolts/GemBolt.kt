package content.global.skill.fletching.items.bolts

import org.rs.consts.Items

/**
 * Represents the gem bolt data.
 *
 * @property base       The base item id used to create.
 * @property gem        The gem id used for fletch.
 * @property tip        The tip id used for the gem bolt.
 * @property product    The product created from the base, gem, and tip.
 * @property level      The required level to create the gem bolt.
 * @property experience The experience gained from creating the bolt.
 */
enum class GemBolt(
    val base: Int,
    val gem: Int,
    val tip: Int,
    val product: Int,
    val level: Int,
    val experience: Double,
) {
    OPAL(
        base = Items.BRONZE_BOLTS_877,
        gem = Items.OPAL_1609,
        tip = Items.OPAL_BOLT_TIPS_45,
        product = Items.OPAL_BOLTS_879,
        level = 11,
        experience = 1.6,
    ),
    PEARL(
        base = Items.IRON_BOLTS_9140,
        gem = Items.OYSTER_PEARL_411,
        tip = Items.PEARL_BOLT_TIPS_46,
        product = Items.PEARL_BOLTS_880,
        level = 41,
        experience = 3.2,
    ),
    PEARLS(
        base = Items.IRON_BOLTS_9140,
        gem = Items.OYSTER_PEARLS_413,
        tip = Items.PEARL_BOLT_TIPS_46,
        product = Items.PEARL_BOLTS_880,
        level = 41,
        experience = 3.2,
    ),
    JADE(
        base = Items.BLURITE_BOLTS_9139,
        gem = Items.JADE_1611,
        tip = Items.JADE_BOLT_TIPS_9187,
        product = Items.JADE_BOLTS_9335,
        level = 26,
        experience = 2.4,
    ),
    RED_TOPAZ(
        base = Items.STEEL_BOLTS_9141,
        gem = Items.RED_TOPAZ_1613,
        tip = Items.TOPAZ_BOLT_TIPS_9188,
        product = Items.TOPAZ_BOLTS_9336,
        level = 48,
        experience = 3.9,
    ),
    SAPPHIRE(
        base = Items.MITHRIL_BOLTS_9142,
        gem = Items.SAPPHIRE_1607,
        tip = Items.SAPPHIRE_BOLT_TIPS_9189,
        product = Items.SAPPHIRE_BOLTS_9337,
        level = 56,
        experience = 4.7,
    ),
    EMERALD(
        base = Items.MITHRIL_BOLTS_9142,
        gem = Items.EMERALD_1605,
        tip = Items.EMERALD_BOLT_TIPS_9190,
        product = Items.EMERALD_BOLTS_9338,
        level = 58,
        experience = 5.5,
    ),
    RUBY(
        base = Items.ADAMANT_BOLTS_9143,
        gem = Items.RUBY_1603,
        tip = Items.RUBY_BOLT_TIPS_9191,
        product = Items.RUBY_BOLTS_9339,
        level = 63,
        experience = 6.3,
    ),
    DIAMOND(
        base = Items.ADAMANT_BOLTS_9143,
        gem = Items.DIAMOND_1601,
        tip = Items.DIAMOND_BOLT_TIPS_9192,
        product = Items.DIAMOND_BOLTS_9340,
        level = 65,
        experience = 7.0,
    ),
    DRAGONSTONE(
        base = Items.RUNE_BOLTS_9144,
        gem = Items.DRAGONSTONE_1615,
        tip = Items.DRAGON_BOLT_TIPS_9193,
        product = Items.DRAGON_BOLTS_9341,
        level = 71,
        experience = 8.2,
    ),
    ONYX(
        base = Items.RUNE_BOLTS_9144,
        gem = Items.ONYX_6573,
        tip = Items.ONYX_BOLT_TIPS_9194,
        product = Items.ONYX_BOLTS_9342,
        level = 73,
        experience = 9.4,
    ),
    ;

    companion object {
        val values = enumValues<GemBolt>()
        val product = values.associateBy { it.base }

        @JvmStatic
        fun forId(id: Int): GemBolt? {
            return values().find { it.base == id || it.tip == id }
        }

        @JvmStatic
        fun getGemId(gemBolt: GemBolt): Int {
            return gemBolt.gem
        }

        @JvmStatic
        fun getTipId(gemBolt: GemBolt): Int {
            return gemBolt.tip
        }
    }
}
