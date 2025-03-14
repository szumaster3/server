package content.global.skill.crafting.items.armour.yakhide

import core.api.amountInInventory
import core.api.skill.sendSkillDialogue
import core.api.submitIndividualPulse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items

class YakArmourCraftingListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.CURED_YAK_HIDE_10820, Items.NEEDLE_1733) { player, used, _ ->
            sendSkillDialogue(player) {
                withItems(BODY, LEGS)
                create { index, amount ->
                    submitIndividualPulse(
                        entity = player,
                        pulse =
                            YakArmourCraftingPulse(
                                player,
                                if (index == 1) BODY else LEGS,
                                index,
                                amount,
                            ),
                    )
                }

                calculateMaxAmount {
                    amountInInventory(player, used.id)
                }
            }
            return@onUseWith true
        }
    }

    companion object {
        private val BODY = Item(Items.YAK_HIDE_ARMOUR_10822)
        private val LEGS = Item(Items.YAK_HIDE_ARMOUR_10824)
    }
}
