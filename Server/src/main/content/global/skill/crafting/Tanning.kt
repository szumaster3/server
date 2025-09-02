package content.global.skill.crafting

import core.api.*
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import shared.consts.Components
import shared.consts.Items

/**
 * Represents the different types of tanning.
 */
enum class Tanning(val item: Int, val product: Int, val button: Int) {
    SOFT_LEATHER(Items.COWHIDE_1739, Items.LEATHER_1741, 1),
    HARD_LEATHER(Items.COWHIDE_1739, Items.HARD_LEATHER_1743, 2),
    SNAKESKIN(Items.SNAKE_HIDE_6287, Items.SNAKESKIN_6289, 3),
    SNAKESKIN2(Items.SNAKE_HIDE_7801, Items.SNAKESKIN_6289, 4),
    GREEN_DRAGONHIDE(Items.GREEN_DRAGONHIDE_1753, Items.GREEN_D_LEATHER_1745, 5),
    BLUE_DRAGONHIDE(Items.BLUE_DRAGONHIDE_1751, Items.BLUE_D_LEATHER_2505, 6),
    RED_DRAGONHIDE(Items.RED_DRAGONHIDE_1749, Items.RED_DRAGON_LEATHER_2507, 7),
    BLACK_DRAGONHIDE(Items.BLACK_DRAGONHIDE_1747, Items.BLACK_D_LEATHER_2509, 8), ;

    companion object {
        private val buttonMap: Map<Int, Tanning> = values().associateBy { it.button }

        /**
         * Gets [Tanning] by button id or `null` if none.
         */
        @JvmStatic
        fun forId(id: Int): Tanning? = buttonMap[id]

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

            if (removeItem(player, Item(Items.COINS_995, coinsRequired * availableAmount)) && removeItem(
                    player,
                    Item(def.item, availableAmount)
                )
            ) {
                addItem(player, def.product, availableAmount)
                sendMessage(
                    player, "The tanner tans ${
                        if (availableAmount > 1) "$availableAmount ${getItemName(def.item).lowercase()}s" else getItemName(
                            def.item
                        ).lowercase()
                    } for you."
                )
                if (def == SOFT_LEATHER) finishDiaryTask(player, DiaryType.LUMBRIDGE, 1, 2)
            } else {
                sendMessage(player, "You don't have enough coins to tan that many.")
            }
        }
    }
}