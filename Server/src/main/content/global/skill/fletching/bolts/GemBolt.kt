package content.global.skill.fletching.bolts

import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Items

/**
 * Represents the gem bolt data.
 */
enum class GemBolt(val base: Int, val gem: Int, val tip: Int, val product: Int, val level: Int, val experience: Double, val animation: Animation) {
    OPAL(Items.BRONZE_BOLTS_877, Items.OPAL_1609, Items.OPAL_BOLT_TIPS_45, Items.OPAL_BOLTS_879, 11, 1.6, Animation(Animations.CUT_OPAL_890)),
    JADE(Items.BLURITE_BOLTS_9139, Items.JADE_1611, Items.JADE_BOLT_TIPS_9187, Items.JADE_BOLTS_9335, 26, 2.4, Animation(Animations.CUT_JADE_891)),
    PEARL(Items.IRON_BOLTS_9140, Items.OYSTER_PEARL_411, Items.PEARL_BOLT_TIPS_46, Items.PEARL_BOLTS_880, 41, 3.2, Animation(Animations.CHISEL_OYSTER_PEARL_4470)),
    PEARLS(Items.IRON_BOLTS_9140, Items.OYSTER_PEARLS_413, Items.PEARL_BOLT_TIPS_46, Items.PEARL_BOLTS_880, 41, 3.2, Animation(Animations.CHISEL_OYSTER_PEARL_4470)),
    RED_TOPAZ(Items.STEEL_BOLTS_9141, Items.RED_TOPAZ_1613, Items.TOPAZ_BOLT_TIPS_9188, Items.TOPAZ_BOLTS_9336, 48, 3.9, Animation(Animations.CUT_TOPAZ_892)),
    SAPPHIRE(Items.MITHRIL_BOLTS_9142, Items.SAPPHIRE_1607, Items.SAPPHIRE_BOLT_TIPS_9189, Items.SAPPHIRE_BOLTS_9337, 56, 4.7, Animation(Animations.CUT_SAPPHIRE_888)),
    EMERALD(Items.MITHRIL_BOLTS_9142, Items.EMERALD_1605, Items.EMERALD_BOLT_TIPS_9190, Items.EMERALD_BOLTS_9338, 58, 5.5, Animation(Animations.CUT_EMERALD_889)),
    RUBY(Items.ADAMANT_BOLTS_9143, Items.RUBY_1603, Items.RUBY_BOLT_TIPS_9191, Items.RUBY_BOLTS_9339, 63, 6.3, Animation(Animations.CUT_RUBY_887)),
    DIAMOND(Items.ADAMANT_BOLTS_9143, Items.DIAMOND_1601, Items.DIAMOND_BOLT_TIPS_9192, Items.DIAMOND_BOLTS_9340, 65, 7.0, Animation(Animations.CUT_DIAMOND_886)),
    DRAGONSTONE(Items.RUNE_BOLTS_9144, Items.DRAGONSTONE_1615, Items.DRAGON_BOLT_TIPS_9193, Items.DRAGON_BOLTS_9341, 71, 8.2, Animation(Animations.CUT_DRAGONSTONE_885)),
    ONYX(Items.RUNE_BOLTS_9144, Items.ONYX_6573, Items.ONYX_BOLT_TIPS_9194, Items.ONYX_BOLTS_9342, 73, 9.4, Animation(Animations.CHISEL_ONYX_2717));

    companion object {
        val values = enumValues<GemBolt>()
        val product = values.associateBy { it.base }
        val gemToBolt = values.associateBy { it.gem }

        @JvmStatic
        fun forId(id: Int): GemBolt? = values().find { it.base == id || it.tip == id }
    }
}
