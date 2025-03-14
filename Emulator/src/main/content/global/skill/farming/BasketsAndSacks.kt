package content.global.skill.farming

import org.rs.consts.Items

enum class BasketsAndSacks(
    val produceID: Int,
    val baseContainer: Int,
    val capacity: Int,
) {
    POTATO(
        produceID = Items.POTATO_1942,
        baseContainer = Items.POTATOES1_5420,
        capacity = 10,
    ),
    ONION(
        produceID = Items.ONION_1957,
        baseContainer = Items.ONIONS1_5440,
        capacity = 10,
    ),
    CABBAGE(
        produceID = Items.CABBAGE_1965,
        baseContainer = Items.CABBAGES1_5460,
        capacity = 10,
    ),
    APPLE(
        produceID = Items.COOKING_APPLE_1955,
        baseContainer = Items.APPLES1_5378,
        capacity = 5,
    ),
    BANANA(
        produceID = Items.BANANA_1963,
        baseContainer = Items.BANANAS1_5408,
        capacity = 5,
    ),
    ORANGE(
        produceID = Items.ORANGE_2108,
        baseContainer = Items.ORANGES1_5388,
        capacity = 5,
    ),
    STRAWBERRY(
        produceID = Items.STRAWBERRY_5504,
        baseContainer = Items.STRAWBERRIES1_5398,
        capacity = 5,
    ),
    TOMATO(
        produceID = Items.TOMATO_1982,
        baseContainer = Items.TOMATOES1_5960,
        capacity = 5,
    ),
    ;

    val containers = ArrayList<Int>()

    companion object {
        private val map = HashMap<Int, BasketsAndSacks>()

        init {
            values().map { it.produceID to it }.toMap(BasketsAndSacks.Companion.map)
            for (b in values()) {
                for (i in 0 until b.capacity) {
                    BasketsAndSacks.Companion.map[b.baseContainer + (i * 2)] = b
                    b.containers.add(b.baseContainer + (i * 2))
                }
            }
        }

        @JvmStatic
        fun forId(itemId: Int): BasketsAndSacks? {
            return BasketsAndSacks.Companion.map[itemId]
        }
    }

    fun checkIsLast(containerID: Int): Boolean {
        return containerID == containers.last()
    }

    fun checkIsFirst(containerID: Int): Boolean {
        return containerID == containers.first()
    }

    fun checkWhich(containerID: Int): Int {
        return containers.indexOf(containerID)
    }
}
