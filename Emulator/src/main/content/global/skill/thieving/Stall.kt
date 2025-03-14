package content.global.skill.thieving

import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.tools.RandomFunction
import org.rs.consts.Items

enum class Stall(
    full: Array<Int>,
    empty: Array<Int>,
    var level: Int,
    var rewards: Array<Item>,
    var experience: Double,
    var delay: Int,
    var message: String,
) {
    VEGETABLE_STALL(
        full = arrayOf(org.rs.consts.Scenery.VEG_STALL_4706, org.rs.consts.Scenery.VEG_STALL_4708),
        empty = arrayOf(org.rs.consts.Scenery.MARKET_STALL_634),
        level = 2,
        rewards =
            arrayOf(
                Item(Items.ONION_1957, 1),
                Item(Items.CABBAGE_1965, 1),
                Item(Items.POTATO_1942, 1),
                Item(Items.TOMATO_1982, 1),
                Item(Items.GARLIC_1550, 1),
            ),
        experience = 10.0,
        delay = 4,
        message = "vegetables",
    ),
    BAKER_STALL(
        full =
            arrayOf(
                org.rs.consts.Scenery.BAKER_S_STALL_2561,
                org.rs.consts.Scenery.BAKERY_STALL_6163,
                org.rs.consts.Scenery.BAKER_S_STALL_34384,
            ),
        empty =
            arrayOf(
                org.rs.consts.Scenery.MARKET_STALL_634,
                org.rs.consts.Scenery.MARKET_STALL_6984,
                org.rs.consts.Scenery.MARKET_STALL_34381,
            ),
        level = 5,
        rewards = arrayOf(Item(Items.CAKE_1891, 1), Item(Items.BREAD_2309, 1), Item(Items.CHOCOLATE_SLICE_1901, 1)),
        experience = 16.0,
        delay = 4,
        message = "bread",
    ),
    CRAFTING_STALL(
        full = arrayOf(org.rs.consts.Scenery.CRAFTING_STALL_4874, org.rs.consts.Scenery.CRAFTING_STALL_6166),
        empty = arrayOf(org.rs.consts.Scenery.BAMBOO_DESK_4797, org.rs.consts.Scenery.MARKET_STALL_6984),
        level = 5,
        rewards =
            arrayOf(
                Item(Items.RING_MOULD_1592, 1),
                Item(Items.NECKLACE_MOULD_1597, 1),
                Item(Items.CHISEL_1755, 1),
            ),
        experience = 16.0,
        delay = 12,
        message = "tool",
    ),
    TEA_STALL(
        full = arrayOf(org.rs.consts.Scenery.TEA_STALL_635, org.rs.consts.Scenery.TEA_STALL_6574),
        empty = arrayOf(org.rs.consts.Scenery.MARKET_STALL_634, org.rs.consts.Scenery.MARKET_STALL_6573),
        level = 5,
        rewards = arrayOf(Item(Items.CUP_OF_TEA_712, 1)),
        experience = 16.0,
        delay = 12,
        message = "tea",
    ),
    SILK_STALL(
        full = arrayOf(org.rs.consts.Scenery.SILK_STALL_34383, org.rs.consts.Scenery.SILK_STALL_2560),
        empty = arrayOf(org.rs.consts.Scenery.MARKET_STALL_34381, org.rs.consts.Scenery.MARKET_STALL_634),
        level = 20,
        rewards = arrayOf(Item(Items.SILK_950, 1)),
        experience = 24.0,
        delay = 13,
        message = "silk",
    ),
    WINE_STALL(
        full = arrayOf(org.rs.consts.Scenery.MARKET_STALL_14011),
        empty = arrayOf(org.rs.consts.Scenery.MARKET_STALL_634),
        level = 22,
        rewards =
            arrayOf(
                Item(Items.JUG_1935, 1),
                Item(Items.JUG_OF_WATER_1937, 1),
                Item(Items.JUG_OF_WINE_1993, 1),
                Item(Items.BOTTLE_OF_WINE_7919, 1),
            ),
        experience = 27.0,
        delay = 27,
        message = "wine",
    ),
    MARKET_SEED_STALL(
        full = arrayOf(org.rs.consts.Scenery.SEED_STALL_7053),
        empty = arrayOf(org.rs.consts.Scenery.MARKET_STALL_634),
        level = 27,
        rewards =
            arrayOf(
                Item(Items.MARIGOLD_SEED_5096, 1),
                Item(Items.ROSEMARY_SEED_5097, 1),
                Item(Items.REDBERRY_SEED_5101, 1),
                Item(Items.POTATO_SEED_5318, 1),
                Item(Items.ONION_SEED_5319, 1),
                Item(Items.CABBAGE_SEED_5324, 1),
            ),
        experience = 10.0,
        delay = 19,
        message = "seeds",
    ),
    FUR_STALL(
        full =
            arrayOf(
                org.rs.consts.Scenery.FUR_STALL_34387,
                org.rs.consts.Scenery.FUR_STALL_2563,
                org.rs.consts.Scenery.FUR_STALL_4278,
            ),
        empty =
            arrayOf(
                org.rs.consts.Scenery.MARKET_STALL_34381,
                org.rs.consts.Scenery.MARKET_STALL_634,
                org.rs.consts.Scenery.MARKET_STALL_634,
            ),
        level = 35,
        rewards = arrayOf(Item(Items.FUR_6814, 1), Item(Items.GREY_WOLF_FUR_958, 1)),
        experience = 36.0,
        delay = 25,
        message = "fur",
    ),
    FISH_STALL(
        full =
            arrayOf(
                org.rs.consts.Scenery.FISH_STALL_4277,
                org.rs.consts.Scenery.FISH_STALL_4705,
                org.rs.consts.Scenery.FISH_STALL_4707,
            ),
        empty =
            arrayOf(
                org.rs.consts.Scenery.MARKET_STALL_634,
                org.rs.consts.Scenery.MARKET_STALL_634,
                org.rs.consts.Scenery.MARKET_STALL_634,
            ),
        level = 42,
        rewards = arrayOf(Item(Items.RAW_SALMON_331, 1), Item(Items.RAW_TUNA_359, 1), Item(Items.RAW_LOBSTER_377, 1)),
        experience = 42.0,
        delay = 27,
        message = "fish",
    ),
    CROSSBOW_STALL(
        full = arrayOf(org.rs.consts.Scenery.CROSSBOW_STALL_17031),
        empty = arrayOf(org.rs.consts.Scenery.MARKET_STALL_6984),
        level = 49,
        rewards =
            arrayOf(
                Item(Items.BRONZE_BOLTS_877, 3),
                Item(Items.BRONZE_LIMBS_9420, 1),
                Item(Items.WOODEN_STOCK_9440, 1),
            ),
        experience = 52.0,
        delay = 19,
        message = "crossbow parts",
    ),
    SILVER_STALL(
        full =
            arrayOf(
                org.rs.consts.Scenery.SILVER_STALL_2565,
                org.rs.consts.Scenery.SILVER_STALL_6164,
                org.rs.consts.Scenery.SILVER_STALL_34382,
            ),
        empty =
            arrayOf(
                org.rs.consts.Scenery.MARKET_STALL_634,
                org.rs.consts.Scenery.MARKET_STALL_6984,
                org.rs.consts.Scenery.MARKET_STALL_34381,
            ),
        level = 50,
        rewards = arrayOf(Item(Items.SILVER_ORE_442, 1)),
        experience = 54.0,
        delay = 50,
        message = "silver",
    ),
    SPICE_STALL(
        full = arrayOf(org.rs.consts.Scenery.SPICE_STALL_34386, org.rs.consts.Scenery.CRAFTING_STALL_6166),
        empty = arrayOf(org.rs.consts.Scenery.MARKET_STALL_34381, org.rs.consts.Scenery.MARKET_STALL_6984),
        level = 65,
        rewards = arrayOf(Item(Items.SPICE_2007, 1)),
        experience = 81.0,
        delay = 134,
        message = "spices",
    ),
    GEM_STALL(
        full =
            arrayOf(
                org.rs.consts.Scenery.GEM_STALL_2562,
                org.rs.consts.Scenery.GEM_STALL_6162,
                org.rs.consts.Scenery.GEM_STALL_34385,
            ),
        empty =
            arrayOf(
                org.rs.consts.Scenery.MARKET_STALL_634,
                org.rs.consts.Scenery.MARKET_STALL_6984,
                org.rs.consts.Scenery.MARKET_STALL_34381,
            ),
        level = 75,
        rewards =
            arrayOf(
                Item(Items.UNCUT_SAPPHIRE_1623, 1),
                Item(Items.EMERALD_1605, 1),
                Item(Items.RUBY_1603, 1),
                Item(Items.DIAMOND_1601, 1),
            ),
        experience = 160.0,
        delay = 300,
        message = "gems",
    ),
    SCIMITAR_STALL(
        full = arrayOf(org.rs.consts.Scenery.SCIMITAR_STALL_4878),
        empty = arrayOf(org.rs.consts.Scenery.BAMBOO_DESK_4797),
        level = 65,
        rewards = arrayOf(Item(Items.IRON_SCIMITAR_1323, 1)),
        experience = 100.0,
        delay = 134,
        message = "equipment",
    ),
    MAGIC_STALL(
        full = arrayOf(org.rs.consts.Scenery.MAGIC_STALL_4877),
        empty = arrayOf(org.rs.consts.Scenery.BAMBOO_DESK_4797),
        level = 65,
        rewards =
            arrayOf(
                Item(Items.AIR_RUNE_556, 1),
                Item(Items.EARTH_RUNE_557, 1),
                Item(Items.FIRE_RUNE_554, 1),
                Item(Items.WATER_RUNE_555, 1),
                Item(Items.LAW_RUNE_563, 1),
            ),
        experience = 100.0,
        delay = 134,
        message = "equipment",
    ),
    GENERAL_STALL(
        full = arrayOf(org.rs.consts.Scenery.GENERAL_STALL_4876),
        empty = arrayOf(org.rs.consts.Scenery.BAMBOO_DESK_4797),
        level = 5,
        rewards = arrayOf(Item(Items.EMPTY_POT_1931, 1), Item(Items.HAMMER_2347, 1), Item(Items.TINDERBOX_590, 1)),
        experience = 16.0,
        delay = 12,
        message = "goods",
    ),
    FOOD_STALL(
        full = arrayOf(org.rs.consts.Scenery.FOOD_STALL_4875),
        empty = arrayOf(org.rs.consts.Scenery.BAMBOO_DESK_4797),
        level = 5,
        rewards = arrayOf(Item(Items.BANANA_1963, 1)),
        experience = 16.0,
        delay = 12,
        message = "food",
    ),
    CANDLES(
        full = arrayOf(org.rs.consts.Scenery.CANDLES_19127),
        empty = arrayOf(org.rs.consts.Scenery.CANDLES_19127),
        level = 20,
        rewards = arrayOf(Item(Items.CANDLE_36, 1)),
        experience = 20.0,
        delay = 0,
        message = "candles",
    ),
    COUNTER(
        full = arrayOf(org.rs.consts.Scenery.COUNTER_2793),
        empty = arrayOf(org.rs.consts.Scenery.COUNTER_2791),
        level = 15,
        rewards = arrayOf(Item(Items.ROCK_CAKE_2379, 1)),
        experience = 6.5,
        delay = 6,
        message = "rocks",
    ),
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
        private val idMap = HashMap<Int, Stall>().apply {
            Stall.values().forEach { entry ->
                entry.fullIDs.forEach { id -> putIfAbsent(id, entry) }
            }
        }

        fun forScenery(scenery: Scenery): Stall? = idMap[scenery.id]
    }
}
