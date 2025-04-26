package content.global.skill.fletching

import content.global.skill.fletching.items.arrow.ArrowHead
import content.global.skill.fletching.items.arrow.BrutalArrow
import content.global.skill.fletching.items.bolts.Bolt
import content.global.skill.fletching.items.bolts.GemBolt
import content.global.skill.fletching.items.bow.Strings
import content.global.skill.fletching.items.crossbow.Limb
import content.global.skill.fletching.items.darts.Dart
import core.game.node.item.Item
import org.rs.consts.Items

object Fletching {
    val logMap = Item.values().associateTo(HashMap()) { it.id to it.items }

    const val arrowShaftId = Items.ARROW_SHAFT_52
    const val fletchedShaftId = Items.HEADLESS_ARROW_53
    const val fligtedOgreArrowId = Items.FLIGHTED_OGRE_ARROW_2865

    val featherIds = intArrayOf(Items.FEATHER_314, Items.STRIPY_FEATHER_10087, Items.RED_FEATHER_10088, Items.BLUE_FEATHER_10089, Items.YELLOW_FEATHER_10090, Items.ORANGE_FEATHER_10091)
    val stringIds = intArrayOf(Items.BOW_STRING_1777, Items.CROSSBOW_STRING_9438)
    val kebbitSpikeIds = intArrayOf(Items.KEBBIT_SPIKE_10105, Items.LONG_KEBBIT_SPIKE_10107)
    val gemIds = intArrayOf(Items.OYSTER_PEARL_411, Items.OYSTER_PEARLS_413, Items.OPAL_1609, Items.JADE_1611, Items.RED_TOPAZ_1613, Items.SAPPHIRE_1607, Items.EMERALD_1605, Items.RUBY_1603, Items.DIAMOND_1601, Items.DRAGONSTONE_1615, Items.ONYX_6573)

    val limbIds = Limb.values().map(Limb::limb).toIntArray()
    val stockIds = Limb.values().map(Limb::stock).toIntArray()
    val nailIds = BrutalArrow.values().map(BrutalArrow::base).toIntArray()
    val unfinishedArrows = ArrowHead.values().map { it.unfinished }.toIntArray()
    val unstrungBows = Strings.values().map(Strings::unfinished).toIntArray()
    val boltBaseIds = GemBolt.values().map { it.base }.toIntArray()
    val boltTipIds = GemBolt.values().map { it.tip }.toIntArray()

    @JvmStatic
    fun getEntries(id: Int): Array<FletchingItems>? = logMap[id]

    @JvmStatic
    fun isLog(id: Int): Boolean = logMap.containsKey(id)

    @JvmStatic
    fun isBolt(id: Int): Boolean = Bolt.product[id] != null

    @JvmStatic
    fun isDart(id: Int): Boolean = Dart.product[id] != null

    @JvmStatic
    fun getItems(id: Int): Array<core.game.node.item.Item>? = getEntries(id)?.map { Item(it.id) }?.toTypedArray()

    private enum class Item(
        val id: Int,
        vararg fletchingItems: FletchingItems,
    ) {
        STANDARD(
            Items.LOGS_1511,
            FletchingItems.ARROW_SHAFT,
            FletchingItems.SHORT_BOW,
            FletchingItems.LONG_BOW,
            FletchingItems.WOODEN_STOCK,
        ),
        ACHEY(
            Items.ACHEY_TREE_LOGS_2862,
            FletchingItems.OGRE_ARROW_SHAFT,
            FletchingItems.OGRE_COMPOSITE_BOW,
        ),
        OAK(
            Items.OAK_LOGS_1521,
            FletchingItems.OAK_SHORTBOW,
            FletchingItems.OAK_LONGBOW,
            FletchingItems.OAK_STOCK,
        ),
        WILLOW(
            Items.WILLOW_LOGS_1519,
            FletchingItems.WILLOW_SHORTBOW,
            FletchingItems.WILLOW_LONGBOW,
            FletchingItems.WILLOW_STOCK,
        ),
        MAPLE(
            Items.MAPLE_LOGS_1517,
            FletchingItems.MAPLE_SHORTBOW,
            FletchingItems.MAPLE_LONGBOW,
            FletchingItems.MAPLE_STOCK,
        ),
        YEW(
            Items.YEW_LOGS_1515,
            FletchingItems.YEW_SHORTBOW,
            FletchingItems.YEW_LONGBOW,
            FletchingItems.YEW_STOCK,
        ),
        MAGIC(
            Items.MAGIC_LOGS_1513,
            FletchingItems.MAGIC_SHORTBOW,
            FletchingItems.MAGIC_LONGBOW,
        ),
        TEAK(
            Items.TEAK_LOGS_6333,
            FletchingItems.TEAK_STOCK,
        ),
        MAHOGANY(
            Items.MAHOGANY_LOGS_6332,
            FletchingItems.MAHOGANY_STOCK,
        ),
        ;

        val items: Array<FletchingItems> = fletchingItems as Array<FletchingItems>
    }

    enum class FletchingItems(
        val id: Int,
        val experience: Double,
        val level: Int,
        val amount: Int,
    ) {
        ARROW_SHAFT(
            id = Items.ARROW_SHAFT_52,
            experience = 5.0,
            level = 1,
            amount = 15,
        ),
        SHORT_BOW(
            id = Items.SHORTBOW_U_50,
            experience = 5.0,
            level = 5,
            amount = 1,
        ),
        LONG_BOW(
            id = Items.LONGBOW_U_48,
            experience = 10.0,
            level = 10,
            amount = 1,
        ),
        WOODEN_STOCK(
            id = Items.WOODEN_STOCK_9440,
            experience = 6.0,
            level = 9,
            amount = 1,
        ),
        OGRE_ARROW_SHAFT(
            id = Items.OGRE_ARROW_SHAFT_2864,
            experience = 6.4,
            level = 5,
            amount = 4,
        ),

        // Authentic.
        OGRE_COMPOSITE_BOW(
            id = Items.COMP_OGRE_BOW_4827,
            experience = 40.0,
            level = 30,
            amount = 1,
        ),
        OAK_SHORTBOW(
            id = Items.OAK_SHORTBOW_U_54,
            experience = 16.5,
            level = 20,
            amount = 1,
        ),
        OAK_LONGBOW(
            id = Items.OAK_LONGBOW_U_56,
            experience = 25.0,
            level = 25,
            amount = 1,
        ),
        OAK_STOCK(
            id = Items.OAK_STOCK_9442,
            experience = 16.0,
            level = 24,
            amount = 1,
        ),
        WILLOW_SHORTBOW(
            id = Items.WILLOW_SHORTBOW_U_60,
            experience = 33.3,
            level = 35,
            amount = 1,
        ),
        WILLOW_LONGBOW(
            id = Items.WILLOW_LONGBOW_U_58,
            experience = 41.5,
            level = 40,
            amount = 1,
        ),
        WILLOW_STOCK(
            id = Items.WILLOW_STOCK_9444,
            experience = 22.0,
            level = 39,
            amount = 1,
        ),
        MAPLE_SHORTBOW(
            id = Items.MAPLE_SHORTBOW_U_64,
            experience = 50.0,
            level = 50,
            amount = 1,
        ),
        MAPLE_LONGBOW(
            id = Items.MAPLE_LONGBOW_U_62,
            experience = 58.3,
            level = 55,
            amount = 1,
        ),
        MAPLE_STOCK(
            id = Items.MAPLE_STOCK_9448,
            experience = 32.0,
            level = 54,
            amount = 1,
        ),
        YEW_SHORTBOW(
            id = Items.YEW_SHORTBOW_U_68,
            experience = 67.5,
            level = 65,
            amount = 1,
        ),
        YEW_LONGBOW(
            id = Items.YEW_LONGBOW_U_66,
            experience = 75.0,
            level = 70,
            amount = 1,
        ),
        YEW_STOCK(
            id = Items.YEW_STOCK_9452,
            experience = 50.0,
            level = 69,
            amount = 1,
        ),
        MAGIC_SHORTBOW(
            id = Items.MAGIC_SHORTBOW_U_72,
            experience = 83.3,
            level = 80,
            amount = 1,
        ),
        MAGIC_LONGBOW(
            id = Items.MAGIC_LONGBOW_U_70,
            experience = 91.5,
            level = 85,
            amount = 1,
        ),
        TEAK_STOCK(
            id = Items.TEAK_STOCK_9446,
            experience = 27.0,
            level = 46,
            amount = 1,
        ),
        MAHOGANY_STOCK(
            id = Items.MAHOGANY_STOCK_9450,
            experience = 41.0,
            level = 61,
            amount = 1,
        ),
    }
}
