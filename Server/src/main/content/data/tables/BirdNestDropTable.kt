package content.data.tables

import core.api.*
import core.game.node.entity.npc.drop.NPCDropTables
import core.game.node.entity.player.Player
import core.game.node.item.ChanceItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.tools.RandomFunction
import core.tools.StringUtils
import shared.consts.Items
import shared.consts.Sounds

/**
 * Represents possible bird nest drop tables.
 */
enum class BirdNestDropTable(
    /**
     * The bird nest item dropped on the ground.
     */
    val nest: ChanceItem,

    /**
     * Possible loot obtained when searching the nest.
     */
    vararg loot: ChanceItem
) {
    RED(
        ChanceItem(Items.BIRDS_NEST_5070, 1, 5),
        ChanceItem(Items.BIRDS_EGG_5076)
    ),
    GREEN(
        ChanceItem(Items.BIRDS_NEST_5071, 1, 5),
        ChanceItem(Items.BIRDS_EGG_5078)
    ),
    BLUE(
        ChanceItem(Items.BIRDS_NEST_5072, 1, 5),
        ChanceItem(Items.BIRDS_EGG_5077)
    ),
    SEED(
        ChanceItem(Items.BIRDS_NEST_5073, 1, 65),
        ChanceItem(Items.ACORN_5312, 1, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.APPLE_TREE_SEED_5283, 1, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.WILLOW_SEED_5313, 1, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.BANANA_TREE_SEED_5284, 1, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.ORANGE_TREE_SEED_5285, 1, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.CURRY_TREE_SEED_5286, 1, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.MAPLE_SEED_5314, 1, NPCDropTables.DROP_RATES[1]),
        ChanceItem(Items.PINEAPPLE_SEED_5287, 1, NPCDropTables.DROP_RATES[1]),
        ChanceItem(Items.PAPAYA_TREE_SEED_5288, 1, NPCDropTables.DROP_RATES[1]),
        ChanceItem(Items.YEW_SEED_5315, 1, NPCDropTables.DROP_RATES[2]),
        ChanceItem(Items.PALM_TREE_SEED_5289, 1, NPCDropTables.DROP_RATES[2]),
        ChanceItem(Items.CALQUAT_TREE_SEED_5290, 1, NPCDropTables.DROP_RATES[2]),
        ChanceItem(Items.SPIRIT_SEED_5317, 1, NPCDropTables.DROP_RATES[3]),
        ChanceItem(Items.MAGIC_SEED_5316, 1, NPCDropTables.DROP_RATES[3]),
    ),
    RING(
        ChanceItem(Items.BIRDS_NEST_5074, 1, 30),
        ChanceItem(Items.GOLD_RING_1635, 1, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.SAPPHIRE_RING_1637, 1, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.EMERALD_RING_1639, 1, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.RUBY_RING_1641, 1, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.DIAMOND_RING_1643, 1, NPCDropTables.DROP_RATES[2]),
    ),

    WYSON(
        ChanceItem(Items.BIRDS_NEST_7413, 1, 1),
        ChanceItem(Items.POTATO_SEED_5318, 14, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.SWEETCORN_SEED_5320, 3, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.TOMATO_SEED_5322, 6, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.CABBAGE_SEED_5324, 9, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.LIMPWURT_SEED_5100, 2, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.ONION_SEED_5319, 11, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.STRAWBERRY_SEED_5323, 3, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.WATERMELON_SEED_5321, 2, NPCDropTables.DROP_RATES[0]),
        ChanceItem(Items.ACORN_5312, 1, NPCDropTables.DROP_RATES[2]),
        ChanceItem(Items.RANARR_SEED_5295, 1, NPCDropTables.DROP_RATES[2]),
        ChanceItem(Items.WILLOW_SEED_5313, 1, NPCDropTables.DROP_RATES[2]),
        ChanceItem(Items.MAPLE_SEED_5314, 1, NPCDropTables.DROP_RATES[2]),
        ChanceItem(Items.YEW_SEED_5315, 1, NPCDropTables.DROP_RATES[2]),
        ChanceItem(Items.MAGIC_SEED_5316, 1, NPCDropTables.DROP_RATES[3]),
        ChanceItem(Items.SPIRIT_SEED_5317, 1, NPCDropTables.DROP_RATES[3]),
    ),
    RAVEN(
        ChanceItem(Items.BIRDS_NEST_11966, 1, 5),
        ChanceItem(Items.RAVEN_EGG_11964),
    );

    /**
     * Internal loot array used for random selection.
     */
    val loot: Array<ChanceItem> = loot as Array<ChanceItem>

    /**
     * Searches the bird nest and awards the appropriate loot to the player.
     *
     * @param player the player searching the nest
     * @param item the nest item being searched
     */
    fun search(player: Player, item: Item) {
        if (freeSlots(player) < 1) {
            sendMessage(player, "You don't have enough inventory space.")
            return
        }

        val reward = if (ordinal > 1 && this != WYSON) {
            loot.first()
        } else {
            RandomFunction.getChanceItem(loot)
        }

        val name = reward.name.lowercase()
        val article = if (StringUtils.isPlusN(name)) "an" else "a"

        lock(player, 1)
        addItem(player, reward.id)
        player.inventory.replace(EMPTY_NEST, item.slot)
        sendMessage(player, "You take $article $name out of the bird's nest.")
    }

    companion object {
        /**
         * Cached array of nest ChanceItems used for random rolling.
         */
        private val NESTS: Array<ChanceItem?> = arrayOfNulls(6)

        /**
         * Empty bird nest item left after searching.
         */
        private val EMPTY_NEST = Item(Items.BIRDS_NEST_5075)

        /**
         * Rolls and drops a random bird nest near the player.
         *
         * Plays the falling nest sound and sends a chat notification.
         */
        @JvmStatic
        fun drop(player: Player) {
            val nest = getRandomNest(node = false)
            playAudio(player, Sounds.CUCKOO_1_1997)

            nest?.let {
                GroundItemManager.create(it.nest, player)
            }

            player.packetDispatch.sendMessage(
                "<col=FF0000>A bird's nest falls out of the tree."
            )
        }

        /**
         * Returns a random bird nest type.
         *
         * @param node whether the source is a bird nest node (Wyson logic)
         * @return the rolled [BirdNestDropTable] or null if none matched
         */
        @JvmStatic
        fun getRandomNest(node: Boolean): BirdNestDropTable? {
            val rolled = RandomFunction.getChanceItem(NESTS)

            return values().firstOrNull { it.nest == rolled }?.let {
                when {
                    node && it == SEED -> WYSON
                    !node && it == WYSON -> SEED
                    else -> it
                }
            }
        }

        /**
         * Resolves a [BirdNestDropTable] by its nest item.
         *
         * @param nest the nest item
         * @return matching drop table or null if none exists
         */
        @JvmStatic
        fun forNest(nest: Item): BirdNestDropTable? =
            values().firstOrNull { it.nest.id == nest.id }

        init {
            values().forEachIndexed { index, table ->
                if (index < NESTS.size) {
                    NESTS[index] = table.nest
                }
            }
        }
    }
}
