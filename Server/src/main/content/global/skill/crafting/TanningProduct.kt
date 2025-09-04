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
enum class TanningProduct(val item: Int, val product: Int, val button: Int, val costPerItem: Int) {
    SOFT_LEATHER(Items.COWHIDE_1739, Items.LEATHER_1741, 1, 1),
    HARD_LEATHER(Items.COWHIDE_1739, Items.HARD_LEATHER_1743, 2, 3),
    SNAKESKIN(Items.SNAKE_HIDE_6287, Items.SNAKESKIN_6289, 3, 20),
    SNAKESKIN2(Items.SNAKE_HIDE_7801, Items.SNAKESKIN_6289, 4, 15),
    GREEN_DRAGONHIDE(Items.GREEN_DRAGONHIDE_1753, Items.GREEN_D_LEATHER_1745, 5, 20),
    BLUE_DRAGONHIDE(Items.BLUE_DRAGONHIDE_1751, Items.BLUE_D_LEATHER_2505, 6, 20),
    RED_DRAGONHIDE(Items.RED_DRAGONHIDE_1749, Items.RED_DRAGON_LEATHER_2507, 7, 20),
    BLACK_DRAGONHIDE(Items.BLACK_DRAGONHIDE_1747, Items.BLACK_D_LEATHER_2509, 8, 20);

    companion object {
        private val buttonMap = values().associateBy { it.button }

        fun forId(id: Int): TanningProduct? = buttonMap[id]

        fun open(player: Player, npc: Int) {
            player.interfaceManager.open(Component(Components.TANNER_324))
        }

        fun tan(player: Player, amount: Int, def: TanningProduct) {
            val availableAmount = minOf(amount, player.inventory.getAmount(Item(def.item)))
            if (availableAmount <= 0) {
                sendMessage(player, "You don't have any ${getItemName(def.item).lowercase()} to tan.")
                return
            }

            val coinsRequired = def.costPerItem * availableAmount
            if (!inInventory(player, Items.COINS_995, coinsRequired)) {
                sendMessage(player, "You don't have enough coins to tan that many.")
                return
            }

            val removed = removeItem(player, Item(Items.COINS_995, coinsRequired)) &&
                    removeItem(player, Item(def.item, availableAmount))
            if (!removed) {
                sendMessage(player, "You don't have enough coins to tan that many.")
                return
            }

            addItem(player, def.product, availableAmount)
            val itemName = getItemName(def.item).lowercase()
            sendMessage(player, "The tanner tans ${if (availableAmount > 1) "$availableAmount ${itemName}s" else itemName} for you.")

            if (def == SOFT_LEATHER) finishDiaryTask(player, DiaryType.LUMBRIDGE, 1, 2)
        }
    }
}