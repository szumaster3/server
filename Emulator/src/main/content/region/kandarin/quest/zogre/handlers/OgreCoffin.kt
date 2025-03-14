package content.region.kandarin.quest.zogre.handlers

import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import org.rs.consts.Items
import org.rs.consts.Scenery

enum class OgreCoffin(
    val sceneryId: IntArray,
    val table: WeightBasedTable,
) {
    OGRE_COFFIN(
        intArrayOf(
            Scenery.OGRE_COFFIN_6848,
            Scenery.OGRE_COFFIN_6850,
        ),
        WeightBasedTable
            .create(
                WeightedItem(Items.COINS_995, 6, 20, 15.0),
                WeightedItem(Items.BRONZE_AXE_1351, 1, 1, 3.0),
                WeightedItem(Items.IRON_AXE_1349, 1, 1, 3.0),
                WeightedItem(Items.STEEL_AXE_1353, 1, 1, 3.0),
                WeightedItem(Items.BRONZE_PICKAXE_1265, 1, 1, 3.0),
                WeightedItem(Items.IRON_PICKAXE_1267, 1, 1, 3.0),
                WeightedItem(Items.STEEL_PICKAXE_1269, 1, 1, 3.0),
                WeightedItem(Items.BRONZE_DAGGER_1205, 1, 1, 3.0),
                WeightedItem(Items.IRON_DAGGER_1203, 1, 1, 3.0),
                WeightedItem(Items.STEEL_DAGGER_1207, 1, 1, 3.0),
                WeightedItem(Items.BRONZE_NAILS_4819, 1, 21, 2.0),
                WeightedItem(Items.IRON_NAILS_4820, 1, 18, 2.0),
                WeightedItem(Items.STEEL_NAILS_1539, 1, 13, 2.0),
                WeightedItem(Items.BLACK_NAILS_4821, 1, 13, 2.0),
                WeightedItem(Items.KNIFE_946, 1, 1, 3.0),
                WeightedItem(Items.RUSTY_SWORD_686, 1, 1, 4.0),
                WeightedItem(Items.LEATHER_BODY_1129, 1, 1, 4.0),
                WeightedItem(Items.DAMAGED_ARMOUR_697, 1, 1, 4.0),
                WeightedItem(Items.TINDERBOX_590, 1, 1, 2.0),
                WeightedItem(Items.BUTTONS_688, 1, 1, 2.0),
                WeightedItem(Items.UNCUT_OPAL_1625, 1, 1, 3.0),
                WeightedItem(Items.UNCUT_JADE_1627, 1, 1, 3.0),
                WeightedItem(Items.GRIMY_LANTADYME_2485, 1, 1, 0.125),
                WeightedItem(Items.GRIMY_DWARF_WEED_217, 1, 1, 0.125),
                WeightedItem(Items.ZOGRE_BONES_4812, 1, 1, 64.0),
                WeightedItem(Items.FAYRG_BONES_4830, 1, 1, 14.0),
                WeightedItem(Items.RAURG_BONES_4832, 1, 1, 10.0),
                WeightedItem(Items.OURG_BONES_4834, 1, 1, 5.0),
            ).insertEasyClue(1.0),
    ),
    ;

    companion object {
        val coffinMap = HashMap<Int, WeightBasedTable>()

        init {
            OgreCoffin.values().forEach {
                it.sceneryId.forEach { id -> coffinMap[id] = it.table }
            }
        }

        fun forId(id: Int): WeightBasedTable? {
            return coffinMap[id]
        }
    }
}
