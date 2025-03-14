package content.global.activity.oldman

import core.tools.RandomFunction
import org.rs.consts.Items

enum class WomDelivery(
    val item: Int,
    val min: Int,
    val max: Int,
) {
    ANCHOVIES(
        item = Items.ANCHOVIES_319,
        min = 1,
        max = 15,
    ),

    BALL_OF_WOOL(
        item = Items.BALL_OF_WOOL_1759,
        min = 1,
        max = 15,
    ),

    BEER_GLASS(
        item = Items.BEER_GLASS_1919,
        min = 1,
        max = 15,
    ),

    BONES(
        item = Items.BONES_526,
        min = 1,
        max = 15,
    ),

    BREAD(
        item = Items.BREAD_2309,
        min = 1,
        max = 15,
    ),

    BRONZE_ARROW(
        item = Items.BRONZE_ARROW_882,
        min = 1,
        max = 15,
    ),

    BRONZE_BAR(
        item = Items.BRONZE_BAR_2349,
        min = 1,
        max = 15,
    ),

    BRONZE_DAGGER(
        item = Items.BRONZE_DAGGER_1205,
        min = 1,
        max = 15,
    ),

    BRONZE_AXE(
        item = Items.BRONZE_AXE_1351,
        min = 1,
        max = 15,
    ),

    BRONZE_FULL_HELM(
        item = Items.BRONZE_FULL_HELM_1155,
        min = 1,
        max = 15,
    ),

    BRONZE_KNIFE(
        item = Items.BRONZE_KNIFE_864,
        min = 1,
        max = 15,
    ),

    BRONZE_MACE(
        item = Items.BRONZE_MACE_1422,
        min = 1,
        max = 15,
    ),

    BRONZE_MEDIUM_HELM(
        item = Items.BRONZE_MED_HELM_1139,
        min = 1,
        max = 15,
    ),

    BRONZE_SPEAR(
        item = Items.BRONZE_SPEAR_1237,
        min = 1,
        max = 15,
    ),

    BRONZE_SWORD(
        item = Items.BRONZE_SWORD_1277,
        min = 1,
        max = 15,
    ),

    BRONZE_WARHAMMER(
        item = Items.BRONZE_WARHAMMER_1337,
        min = 1,
        max = 15,
    ),

    BRONZE_WIRE(
        item = Items.BRONZE_WIRE_1794,
        min = 1,
        max = 15,
    ),

    BEER(
        item = Items.BEER_1917,
        min = 1,
        max = 15,
    ),

    BOWSTRING(
        item = Items.BOW_STRING_1777,
        min = 1,
        max = 15,
    ),

    CADAVA_BERRIES(
        item = Items.CADAVA_BERRIES_753,
        min = 1,
        max = 15,
    ),

    COOKED_CHICKEN(
        item = Items.COOKED_CHICKEN_2140,
        min = 1,
        max = 15,
    ),

    COOKED_MEAT(
        item = Items.COOKED_MEAT_2142,
        min = 1,
        max = 15,
    ),

    COPPER_ORE(
        item = Items.COPPER_ORE_436,
        min = 1,
        max = 15,
    ),

    COWHIDE(
        item = Items.COWHIDE_1739,
        min = 1,
        max = 15,
    ),

    EGG(
        item = Items.EGG_1944,
        min = 1,
        max = 15,
    ),

    FEATHERS(
        item = Items.FEATHER_314,
        min = 1,
        max = 15,
    ),

    GRAIN(
        item = Items.GRAIN_1947,
        min = 1,
        max = 15,
    ),

    HEADLESS_ARROWS(
        item = Items.HEADLESS_ARROW_53,
        min = 1,
        max = 15,
    ),

    IRON_ARROWTIPS(
        item = Items.IRON_ARROWTIPS_40,
        min = 1,
        max = 15,
    ),

    IRON_MACE(
        item = Items.IRON_MACE_1420,
        min = 1,
        max = 15,
    ),

    IRON_ORE(
        item = Items.IRON_ORE_440,
        min = 1,
        max = 15,
    ),

    IRON_THROWING_KNIVES(
        item = Items.IRON_THROWNAXE_801,
        min = 1,
        max = 15,
    ),

    IRON_WARHAMMER(
        item = Items.IRON_WARHAMMER_1335,
        min = 1,
        max = 15,
    ),

    LEATHER_BOOTS(
        item = Items.LEATHER_BOOTS_1061,
        min = 1,
        max = 15,
    ),

    LEATHER_COWL(
        item = Items.LEATHER_COWL_1167,
        min = 1,
        max = 15,
    ),

    LEATHER_GLOVES(
        item = Items.LEATHER_GLOVES_1059,
        min = 1,
        max = 15,
    ),

    LOGS(
        item = Items.LOGS_1511,
        min = 1,
        max = 15,
    ),

    MOLTEN_GLASS(
        item = Items.MOLTEN_GLASS_1775,
        min = 1,
        max = 15,
    ),

    POT_OF_FLOUR(
        item = Items.POT_OF_FLOUR_1933,
        min = 1,
        max = 15,
    ),

    POTATOES(
        item = Items.POTATO_1942,
        min = 1,
        max = 15,
    ),

    RAW_ANCHOVIES(
        item = Items.RAW_ANCHOVIES_321,
        min = 1,
        max = 15,
    ),

    RAW_RAT_MEAT(
        item = Items.RAW_RAT_MEAT_2134,
        min = 1,
        max = 15,
    ),

    RUNE_ESSENCE(
        item = Items.RUNE_ESSENCE_1436,
        min = 1,
        max = 15,
    ),

    PURE_ESSENCE(
        item = Items.PURE_ESSENCE_7936,
        min = 1,
        max = 15,
    ),

    SHRIMP(
        item = Items.SHRIMPS_315,
        min = 1,
        max = 15,
    ),

    SILK(
        item = Items.SILK_950,
        min = 1,
        max = 15,
    ),

    SOFT_CLAY(
        item = Items.SOFT_CLAY_1761,
        min = 1,
        max = 15,
    ),
    ;

    val amount = RandomFunction.random(min, max)

    companion object {
        val deliveryMap = HashMap<Int, WomDelivery>()

        init {

            for (delivery in WomDelivery.values()) {
                deliveryMap[delivery.item] = delivery
            }

            for (amount in WomDelivery.values()) {
                deliveryMap[amount.item] = amount
            }
        }

        fun forId(task: Int): WomDelivery? {
            for (id in WomDelivery.values()) {
                if (id.item == task) {
                    return id
                }
            }
            return null
        }
    }
}
