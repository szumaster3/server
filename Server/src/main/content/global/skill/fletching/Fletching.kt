package content.global.skill.fletching

import content.global.skill.fletching.arrows.ArrowHead
import content.global.skill.fletching.arrows.BrutalArrow
import content.global.skill.fletching.bolts.GemBolt
import content.global.skill.fletching.bow.Strings
import content.global.skill.fletching.crossbow.Limb
import core.game.node.item.Item
import shared.consts.Items

object Fletching {
    data class FletchData(val id: Int, val exp: Double, val level: Int, val amount: Int, val logId: Int)

    private val fletchTable = listOf(
        // Standard logs.
        FletchData(Items.ARROW_SHAFT_52, 5.0, 1, 15, Items.LOGS_1511),
        FletchData(Items.SHORTBOW_U_50, 5.0, 5, 1, Items.LOGS_1511),
        FletchData(Items.LONGBOW_U_48, 10.0, 10, 1, Items.LOGS_1511),
        FletchData(Items.WOODEN_STOCK_9440, 6.0, 9, 1, Items.LOGS_1511),
        // Achey.
        FletchData(Items.OGRE_ARROW_SHAFT_2864, 6.4, 5, 4, Items.ACHEY_TREE_LOGS_2862),
        FletchData(Items.COMP_OGRE_BOW_4827, 40.0, 30, 1, Items.ACHEY_TREE_LOGS_2862),
        // Oak.
        FletchData(Items.OAK_SHORTBOW_U_54, 16.5, 20, 1, Items.OAK_LOGS_1521),
        FletchData(Items.OAK_LONGBOW_U_56, 25.0, 25, 1, Items.OAK_LOGS_1521),
        FletchData(Items.OAK_STOCK_9442, 16.0, 24, 1, Items.OAK_LOGS_1521),
        // Willow.
        FletchData(Items.WILLOW_SHORTBOW_U_60, 33.3, 35, 1, Items.WILLOW_LOGS_1519),
        FletchData(Items.WILLOW_LONGBOW_U_58, 41.5, 40, 1, Items.WILLOW_LOGS_1519),
        FletchData(Items.WILLOW_STOCK_9444, 22.0, 39, 1, Items.WILLOW_LOGS_1519),
        // Maple.
        FletchData(Items.MAPLE_SHORTBOW_U_64, 50.0, 50, 1, Items.MAPLE_LOGS_1517),
        FletchData(Items.MAPLE_LONGBOW_U_62, 58.3, 55, 1, Items.MAPLE_LOGS_1517),
        FletchData(Items.MAPLE_STOCK_9448, 32.0, 54, 1, Items.MAPLE_LOGS_1517),
        // Yew.
        FletchData(Items.YEW_SHORTBOW_U_68, 67.5, 65, 1, Items.YEW_LOGS_1515),
        FletchData(Items.YEW_LONGBOW_U_66, 75.0, 70, 1, Items.YEW_LOGS_1515),
        FletchData(Items.YEW_STOCK_9452, 50.0, 69, 1, Items.YEW_LOGS_1515),
        // Magic.
        FletchData(Items.MAGIC_SHORTBOW_U_72, 83.3, 80, 1, Items.MAGIC_LOGS_1513),
        FletchData(Items.MAGIC_LONGBOW_U_70, 91.5, 85, 1, Items.MAGIC_LOGS_1513),
        // Teak.
        FletchData(Items.TEAK_STOCK_9446, 27.0, 46, 1, Items.TEAK_LOGS_6333),
        // Mahogany.
        FletchData(Items.MAHOGANY_STOCK_9450, 41.0, 61, 1, Items.MAHOGANY_LOGS_6332)
    )

    private val LOGS_MAP: Map<Int, List<FletchData>> = fletchTable.groupBy { it.logId }

    const val ARROW_SHAFT = Items.ARROW_SHAFT_52
    const val HEADLESS_ARROW = Items.HEADLESS_ARROW_53
    const val FLIGHTED_OGRE_ARROW = Items.FLIGHTED_OGRE_ARROW_2865

    val featherIds = intArrayOf(Items.FEATHER_314, Items.STRIPY_FEATHER_10087, Items.RED_FEATHER_10088, Items.BLUE_FEATHER_10089, Items.YELLOW_FEATHER_10090, Items.ORANGE_FEATHER_10091)
    val stringIds = intArrayOf(Items.BOW_STRING_1777, Items.CROSSBOW_STRING_9438)
    val kebbitSpikeIds = intArrayOf(Items.KEBBIT_SPIKE_10105, Items.LONG_KEBBIT_SPIKE_10107)
    val gemIds = intArrayOf(Items.OYSTER_PEARL_411, Items.OYSTER_PEARLS_413, Items.OPAL_1609, Items.JADE_1611, Items.RED_TOPAZ_1613, Items.SAPPHIRE_1607, Items.EMERALD_1605, Items.RUBY_1603, Items.DIAMOND_1601, Items.DRAGONSTONE_1615, Items.ONYX_6573)
    val limbIds = Limb.values().map(Limb::limb).toIntArray()
    val stockIds = Limb.values().map(Limb::stock).toIntArray()
    val nailIds = BrutalArrow.values().map(BrutalArrow::base).toIntArray()
    val unfinishedArrows = ArrowHead.values().map(ArrowHead::unfinished).toIntArray()
    val unstrungBows = Strings.values().map(Strings::unfinished).toIntArray()
    val boltBaseIds = GemBolt.values().map(GemBolt::base).toIntArray()
    val boltTipIds = GemBolt.values().map(GemBolt::tip).toIntArray()

    fun getEntries(id: Int) = LOGS_MAP[id]
    fun getItems(id: Int) = getEntries(id)?.map { Item(it.id) }?.toTypedArray()
}
