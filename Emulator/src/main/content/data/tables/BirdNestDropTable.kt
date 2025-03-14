package content.data.tables

import core.api.*
import core.game.node.entity.npc.drop.NPCDropTables
import core.game.node.entity.player.Player
import core.game.node.item.ChanceItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.tools.RandomFunction
import core.tools.StringUtils
import org.rs.consts.Items
import org.rs.consts.Sounds

enum class BirdNestDropTable(
    val nest: ChanceItem,
    vararg loot: ChanceItem,
) {
    RED(
        ChanceItem(Items.BIRDS_NEST_5070, 1, 5),
        ChanceItem(Items.BIRDS_EGG_5076),
    ),
    GREEN(
        ChanceItem(Items.BIRDS_NEST_5071, 1, 5),
        ChanceItem(Items.BIRDS_EGG_5078),
    ),
    BLUE(
        ChanceItem(Items.BIRDS_NEST_5072, 1, 5),
        ChanceItem(Items.BIRDS_EGG_5077),
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
    ),
    ;

    val loot: Array<ChanceItem> = loot as Array<ChanceItem>

    fun search(
        player: Player,
        item: Item,
    ) {
        if (freeSlots(player) < 1) {
            sendMessage(player, "You don't have enough inventory space.")
            return
        }
        val loot = if (ordinal > 1 && this != WYSON) loot[0] else RandomFunction.getChanceItem(loot)
        val name = loot.name.lowercase()
        val input = (if (StringUtils.isPlusN(name)) "an" else "a") + " " + name

        lock(player, 1)
        addItem(player, loot.id)
        player.inventory.replace(EMPTY, item.slot)
        sendMessage(player, "You take $input out of the bird's nest.")
    }

    companion object {
        private val NESTS = arrayOfNulls<ChanceItem>(6)
        private val EMPTY = Item(Items.BIRDS_NEST_5075)

        @JvmStatic
        fun drop(player: Player) {
            val nest = getRandomNest(false)
            playAudio(player, Sounds.CUCKOO_1_1997)
            GroundItemManager.create(nest!!.nest, player)
            sendMessage(player, "<col=FF0000>A bird's nest falls out of the tree.")
        }

        @JvmStatic
        fun getRandomNest(node: Boolean): BirdNestDropTable? {
            val item = RandomFunction.getChanceItem(NESTS)
            for (n in values()) {
                if (n.nest == item) {
                    if (node && n == SEED) {
                        return WYSON
                    } else if (!node && n == WYSON) {
                        return SEED
                    }
                    return n
                }
            }
            return null
        }

        @JvmStatic
        fun forNest(nest: Item): BirdNestDropTable? {
            for (n in values()) {
                if (n.nest.id == nest.id) {
                    return n
                }
            }
            return null
        }

        init {
            for (i in NESTS.indices) {
                NESTS[i] = values()[i].nest
            }
        }
    }
}
