package content.global.skill.crafting.items.armour

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items

class FeatherHeaddressMakePlugin : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.COIF_1169, *FeatherHeaddress.baseIds) { player, used, _ ->
            val item = FeatherHeaddress.forBase(used.id) ?: return@onUseWith false
            if (!hasLevelDyn(player, Skills.CRAFTING, 79)) {
                sendMessage(player, "You need a crafting level of at least 79 in order to do this.")
                return@onUseWith true
            }

            val available = amountInInventory(player, item.base) / 20
            if (available < 1) {
                sendMessage(player, "You don't have enough ${getItemName(item.base).lowercase()} to craft this.")
                return@onUseWith true
            }

            if (available == 1) {
                if (removeItem(player, Item(item.base, 20))) {
                    addItem(player, item.product, 1)
                    rewardXP(player, Skills.CRAFTING, 50.0)
                    sendMessage(player, "You add the feathers to the coif to make a feathered headdress.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(item.product)
                create { _, amount ->
                    if (amount < 1) return@create
                    runTask(player, delay = 1, repeatTimes = amount) {
                        if (removeItem(player, Item(item.base, 20))) {
                            addItem(player, item.product, 1)
                            rewardXP(player, Skills.CRAFTING, 50.0)
                            sendMessage(player, "You add the feathers to the coif to make ${if (amount > 1) "$amount feathered headdresses" else "a feathered headdress"}.")
                        } else {
                            sendMessage(player, "You don't have enough materials to continue crafting.")
                        }
                    }
                }
                calculateMaxAmount { available }
            }

            return@onUseWith true
        }
    }

}


private enum class FeatherHeaddress(val base: Int, val product: Int) {
    FEATHER_HEADDRESS_BLUE(Items.BLUE_FEATHER_10089, Items.FEATHER_HEADDRESS_12210),
    FEATHER_HEADDRESS_ORANGE(Items.ORANGE_FEATHER_10091, Items.FEATHER_HEADDRESS_12222),
    FEATHER_HEADDRESS_RED(Items.RED_FEATHER_10088, Items.FEATHER_HEADDRESS_12216),
    FEATHER_HEADDRESS_STRIPY(Items.STRIPY_FEATHER_10087, Items.FEATHER_HEADDRESS_12219),
    FEATHER_HEADDRESS_YELLOW(Items.YELLOW_FEATHER_10090, Items.FEATHER_HEADDRESS_12213);

    companion object {
        val baseToHeaddressMap = values().associateBy { it.base }
        val baseIds = values().map { it.base }.toIntArray()
        fun forBase(baseId: Int): FeatherHeaddress? = baseToHeaddressMap[baseId]
    }
}
