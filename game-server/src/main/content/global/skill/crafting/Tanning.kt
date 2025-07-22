package content.global.skill.crafting

import core.api.*
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import org.rs.consts.Components
import org.rs.consts.Items

/**
 * Represents the different types of tanning.
 */
enum class Tanning(val button: Int, val item: Int, val product: Int, ) {
    /**
     * Tanning soft leather from cowhide.
     */
    SOFT_LEATHER(
        button = 1,
        item = Items.COWHIDE_1739,
        product = Items.LEATHER_1741,
    ),

    /**
     * Tanning hard leather from cowhide.
     */
    HARD_LEATHER(
        button = 2,
        item = Items.COWHIDE_1739,
        product = Items.HARD_LEATHER_1743,
    ),

    /**
     * Tanning snakeskin from snake hide.
     */
    SNAKESKIN(
        button = 3,
        item = Items.SNAKE_HIDE_6287,
        product = Items.SNAKESKIN_6289,
    ),

    /**
     * Tanning snakeskin from a different type of snake hide.
     */
    SNAKESKIN2(
        button = 4,
        item = Items.SNAKE_HIDE_7801,
        product = Items.SNAKESKIN_6289,
    ),

    /**
     * Tanning green dragonhide into green dragon leather.
     */
    GREENDHIDE(
        button = 5,
        item = Items.GREEN_DRAGONHIDE_1753,
        product = Items.GREEN_D_LEATHER_1745,
    ),

    /**
     * Tanning blue dragonhide into blue dragon leather.
     */
    BLUEDHIDE(
        button = 6,
        item = Items.BLUE_DRAGONHIDE_1751,
        product = Items.BLUE_D_LEATHER_2505,
    ),

    /**
     * Tanning red dragonhide into red dragon leather.
     */
    REDDHIDE(
        button = 7,
        item = Items.RED_DRAGONHIDE_1749,
        product = Items.RED_DRAGON_LEATHER_2507,
    ),

    /**
     * Tanning black dragonhide into black dragon leather.
     */
    BLACKDHIDE(
        button = 8,
        item = Items.BLACK_DRAGONHIDE_1747,
        product = Items.BLACK_D_LEATHER_2509,
    ),
    ;

    companion object {
        private val buttonMap: Map<Int, Tanning> = values().associateBy { it.button }
        private val itemMap: Map<Int, Tanning> = values().associateBy { it.item }

        /**
         * Gets [Tanning] by button id or `null` if none.
         */
        @JvmStatic
        fun forId(id: Int): Tanning? = buttonMap[id]

        /**
         * Gets [Tanning] by item id or `null` if none.
         */
        @JvmStatic
        fun forItemId(id: Int): Tanning? = itemMap[id]

        /**
         * Opens tanning interface for the player.
         */
        @JvmStatic
        fun open(player: Player, npc: Int) {
            player.interfaceManager.open(Component(Components.TANNER_324))
        }

        /**
         * Tans a given amount of items for the player, checking inventory and coins.
         */
        @JvmStatic
        fun tan(player: Player, amount: Int, def: Tanning) {
            val availableAmount = minOf(amount, player.inventory.getAmount(Item(def.item)))
            if (availableAmount == 0) {
                sendMessage(player, "You don't have any ${getItemName(def.item).lowercase()} to tan.")
                return
            }

            val coinsRequired = when (def) {
                SOFT_LEATHER -> 1
                HARD_LEATHER -> 3
                SNAKESKIN -> 20
                SNAKESKIN2 -> 15
                else -> 20
            }

            if (!inInventory(player, Items.COINS_995, coinsRequired * availableAmount)) {
                sendMessage(player, "You don't have enough coins to tan that many.")
                return
            }

            if (removeItem(player, Item(Items.COINS_995, coinsRequired * availableAmount)) &&
                removeItem(player, Item(def.item, availableAmount))) {
                addItem(player, def.product, availableAmount)
                sendMessage(player, "The tanner tans ${if (availableAmount > 1) "$availableAmount ${getItemName(def.item).lowercase()}s" else getItemName(def.item).lowercase()} for you.")
                if (def == SOFT_LEATHER) finishDiaryTask(player, DiaryType.LUMBRIDGE, 1, 2)
            } else {
                sendMessage(player, "You don't have enough coins to tan that many.")
            }
        }
    }
}