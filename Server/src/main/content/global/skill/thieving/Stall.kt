package content.global.skill.thieving

import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.Scenery as Objects

enum class Stall(full: Array<Int>, empty: Array<Int>, var level: Int, var rewards: Array<Item>, var experience: Double, var delay: Int, var message: String, ) {
    VEGETABLE_STALL(arrayOf(Objects.VEG_STALL_4706, Objects.VEG_STALL_4708), arrayOf(Objects.MARKET_STALL_634), 2, arrayOf(Item(Items.ONION_1957, 1), Item(Items.CABBAGE_1965, 1), Item(Items.POTATO_1942, 1), Item(Items.TOMATO_1982, 1), Item(Items.GARLIC_1550, 1)), 10.0, 4, "vegetables",),
    BAKER_STALL(arrayOf(Objects.BAKER_S_STALL_2561, Objects.BAKERY_STALL_6163, Objects.BAKER_S_STALL_34384,), arrayOf(Objects.MARKET_STALL_634, Objects.MARKET_STALL_6984, Objects.MARKET_STALL_34381,), 5, arrayOf(Item(Items.CAKE_1891, 1), Item(Items.BREAD_2309, 1), Item(Items.CHOCOLATE_SLICE_1901, 1)), 16.0, 4, "bread",),
    CRAFTING_STALL(arrayOf(Objects.CRAFTING_STALL_4874, Objects.CRAFTING_STALL_6166), arrayOf(Objects.BAMBOO_DESK_4797, Objects.MARKET_STALL_6984), 5, arrayOf(Item(Items.RING_MOULD_1592, 1), Item(Items.NECKLACE_MOULD_1597, 1), Item(Items.CHISEL_1755, 1),), 16.0, 12, "tool",),
    TEA_STALL(arrayOf(Objects.TEA_STALL_635, Objects.TEA_STALL_6574), arrayOf(Objects.MARKET_STALL_634, Objects.MARKET_STALL_6573), 5, arrayOf(Item(Items.CUP_OF_TEA_712, 1)), 16.0, 12, "tea",),
    SILK_STALL(arrayOf(Objects.SILK_STALL_34383, Objects.SILK_STALL_2560), arrayOf(Objects.MARKET_STALL_34381, Objects.MARKET_STALL_634), 20, arrayOf(Item(Items.SILK_950, 1)), 24.0, 13, "silk",),
    WINE_STALL(arrayOf(Objects.MARKET_STALL_14011), arrayOf(Objects.MARKET_STALL_634), 22, arrayOf(Item(Items.JUG_1935, 1), Item(Items.JUG_OF_WATER_1937, 1), Item(Items.JUG_OF_WINE_1993, 1), Item(Items.BOTTLE_OF_WINE_7919, 1),), 27.0, 27, "wine",),
    MARKET_SEED_STALL(arrayOf(Objects.SEED_STALL_7053), arrayOf(Objects.MARKET_STALL_634), 27, arrayOf(Item(Items.MARIGOLD_SEED_5096, 1), Item(Items.ROSEMARY_SEED_5097, 1), Item(Items.REDBERRY_SEED_5101, 1), Item(Items.POTATO_SEED_5318, 1), Item(Items.ONION_SEED_5319, 1), Item(Items.CABBAGE_SEED_5324, 1),), 10.0, 19, "seeds",),
    FUR_STALL(arrayOf(Objects.FUR_STALL_34387, Objects.FUR_STALL_2563, Objects.FUR_STALL_4278,), arrayOf(Objects.MARKET_STALL_34381, Objects.MARKET_STALL_634, Objects.MARKET_STALL_634,), 35, arrayOf(Item(Items.FUR_6814, 1), Item(Items.GREY_WOLF_FUR_958, 1)), 36.0, 25, "fur",),
    FISH_STALL(arrayOf(Objects.FISH_STALL_4277, Objects.FISH_STALL_4705, Objects.FISH_STALL_4707,), arrayOf(Objects.MARKET_STALL_634, Objects.MARKET_STALL_634, Objects.MARKET_STALL_634,), 42, arrayOf(Item(Items.RAW_SALMON_331, 1), Item(Items.RAW_TUNA_359, 1), Item(Items.RAW_LOBSTER_377, 1)), 42.0, 27, "fish",),
    CROSSBOW_STALL(arrayOf(Objects.CROSSBOW_STALL_17031), arrayOf(Objects.MARKET_STALL_6984), 49, arrayOf(Item(Items.BRONZE_BOLTS_877, 3), Item(Items.BRONZE_LIMBS_9420, 1), Item(Items.WOODEN_STOCK_9440, 1),), 52.0, 19, "crossbow parts",),
    SILVER_STALL(arrayOf(Objects.SILVER_STALL_2565, Objects.SILVER_STALL_6164, Objects.SILVER_STALL_34382,), arrayOf(Objects.MARKET_STALL_634, Objects.MARKET_STALL_6984, Objects.MARKET_STALL_34381,), 50, arrayOf(Item(Items.SILVER_ORE_442, 1)), 54.0, 50, "silver",),
    SPICE_STALL(arrayOf(Objects.SPICE_STALL_34386, Objects.CRAFTING_STALL_6166), arrayOf(Objects.MARKET_STALL_34381, Objects.MARKET_STALL_6984), 65, arrayOf(Item(Items.SPICE_2007, 1)), 81.0, 134, "spices",),
    GEM_STALL(arrayOf(Objects.GEM_STALL_2562, Objects.GEM_STALL_6162, Objects.GEM_STALL_34385,), arrayOf(Objects.MARKET_STALL_634, Objects.MARKET_STALL_6984, Objects.MARKET_STALL_34381,), 75, arrayOf(Item(Items.UNCUT_SAPPHIRE_1623, 1), Item(Items.EMERALD_1605, 1), Item(Items.RUBY_1603, 1), Item(Items.DIAMOND_1601, 1),), 160.0, 300, "gems",),
    SCIMITAR_STALL(arrayOf(Objects.SCIMITAR_STALL_4878), arrayOf(Objects.BAMBOO_DESK_4797), 65, arrayOf(Item(Items.IRON_SCIMITAR_1323, 1)), 100.0, 134, "equipment",),
    MAGIC_STALL(arrayOf(Objects.MAGIC_STALL_4877), arrayOf(Objects.BAMBOO_DESK_4797), 65, arrayOf(Item(Items.AIR_RUNE_556, 1), Item(Items.EARTH_RUNE_557, 1), Item(Items.FIRE_RUNE_554, 1), Item(Items.WATER_RUNE_555, 1), Item(Items.LAW_RUNE_563, 1),), 100.0, 134, "equipment",),
    GENERAL_STALL(arrayOf(Objects.GENERAL_STALL_4876), arrayOf(Objects.BAMBOO_DESK_4797), 5, arrayOf(Item(Items.EMPTY_POT_1931, 1), Item(Items.HAMMER_2347, 1), Item(Items.TINDERBOX_590, 1)), 16.0, 12, "goods",),
    FOOD_STALL(arrayOf(Objects.FOOD_STALL_4875), arrayOf(Objects.BAMBOO_DESK_4797), 5, arrayOf(Item(Items.BANANA_1963, 1)), 16.0, 12, "food",),
    CANDLES(arrayOf(Objects.CANDLES_19127), arrayOf(Objects.CANDLES_19127), 20, arrayOf(Item(Items.CANDLE_36, 1)), 20.0, 0, "candles",),
    COUNTER(arrayOf(Objects.COUNTER_2793), arrayOf(Objects.COUNTER_2791), 15, arrayOf(Item(Items.ROCK_CAKE_2379, 1)), 6.5, 12, "rocks",),
    ;

    var fullIDs: List<Int> = ArrayList(listOf(*full))
    var empty_ids: List<Int> = ArrayList(listOf(*empty))

    fun getEmpty(id: Int): Int {
        val fullIndex = fullIDs.indexOf(id)
        return empty_ids[fullIndex]
    }

    val randomLoot: Item
        get() = rewards[RandomFunction.random(rewards.size)]

    companion object {
        private val idMap =
            HashMap<Int, Stall>().apply {
                Stall.values().forEach { entry ->
                    entry.fullIDs.forEach { id -> putIfAbsent(id, entry) }
                }
            }

        fun forScenery(scenery: Scenery): Stall? = idMap[scenery.id]
    }
}
