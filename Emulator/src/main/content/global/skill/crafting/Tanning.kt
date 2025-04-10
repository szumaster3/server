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
enum class Tanning(
    val button: Int,
    val item: Int,
    val product: Int,
) {
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
        /**
         * Returns the [Tanning] enum entry corresponding to the given button id.
         *
         * @param id The button id that corresponds to a tanning option.
         * @return The corresponding [Tanning] enum entry, or null if no match is found.
         */
        @JvmStatic
        fun forId(id: Int): Tanning? = values().find { it.button == id }

        /**
         * Returns the [Tanning] enum entry corresponding to the given item id.
         *
         * @param id The id of the item required for tanning.
         * @return The corresponding [Tanning] enum entry, or null if no match is found.
         */
        @JvmStatic
        fun forItemId(id: Int): Tanning? = values().find { it.item == id }

        /**
         * Opens the tanning interface for the player.
         *
         * @param player The player opening the tanning interface.
         * @param npc The id of the NPC to interact with for tanning.
         */
        @JvmStatic
        fun open(
            player: Player,
            npc: Int,
        ) {
            player.interfaceManager.open(Component(Components.TANNER_324))
        }

        /**
         * Tans a specific amount of an item for the player.
         *
         * @param player The player performing the tanning action.
         * @param amount The number of items to tan.
         * @param def The [Tanning] enum entry that defines the tanning action.
         */
        @JvmStatic
        fun tan(
            player: Player,
            amount: Int,
            def: Tanning,
        ) {
            val availableAmount = minOf(amount, player.inventory.getAmount(Item(def.item)))
            if (availableAmount == 0) {
                sendMessage(player, "You don't have any ${getItemName(def.item).lowercase()} to tan.")
                return
            }

            val coinsRequired =
                when (def) {
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
                removeItem(
                    player,
                    Item(def.item, availableAmount),
                )
            ) {
                addItem(player, def.product, availableAmount)
                sendMessage(
                    player,
                    "The tanner tans ${
                        if (availableAmount > 1) {
                            "$availableAmount ${getItemName(def.item).lowercase()}s for you"
                        } else {
                            "${
                                getItemName(def.item).lowercase()
                            } for you"
                        }
                    }" + ".",
                )
                if (def == SOFT_LEATHER) {
                    finishDiaryTask(player, DiaryType.LUMBRIDGE, 1, 2)
                }
            } else {
                sendMessage(player, "You don't have enough coins to tan that many.")
            }
        }
    }
}