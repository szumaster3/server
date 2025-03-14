package content.global.skill.crafting.items.armour.leather.dragon

import core.api.amountInInventory
import core.api.skill.sendSkillDialogue
import core.api.submitIndividualPulse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class DragonLeatherCraftingListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, DRAGON_LEATHER, Items.NEEDLE_1733) { player, used, with ->
            val index = IntArray(3)

            if (used.id == Items.GREEN_D_LEATHER_1745) {
                index[0] = DragonLeather.GREEN_D_HIDE_BODY.product
                index[1] = DragonLeather.GREEN_D_HIDE_VAMBS.product
                index[2] = DragonLeather.GREEN_D_HIDE_CHAPS.product
            }
            if (used.id == Items.BLUE_D_LEATHER_2505) {
                index[0] = DragonLeather.BLUE_D_HIDE_BODY.product
                index[1] = DragonLeather.BLUE_D_HIDE_VAMBS.product
                index[2] = DragonLeather.BLUE_D_HIDE_CHAPS.product
            }
            if (used.id == Items.RED_DRAGON_LEATHER_2507) {
                index[0] = DragonLeather.RED_D_HIDE_BODY.product
                index[1] = DragonLeather.RED_D_HIDE_VAMBS.product
                index[2] = DragonLeather.RED_D_HIDE_CHAPS.product
            }
            if (used.id == Items.BLACK_D_LEATHER_2509) {
                index[0] = DragonLeather.BLACK_D_HIDE_BODY.product
                index[1] = DragonLeather.BLACK_D_HIDE_VAMBS.product
                index[2] = DragonLeather.BLACK_D_HIDE_CHAPS.product
            }

            sendSkillDialogue(player) {
                withItems(*index)
                create { id, amount ->
                    val item = DragonLeather.forId(id)
                    submitIndividualPulse(
                        entity = player,
                        pulse = DragonLeatherCraftingPulse(player, null, item!!, amount),
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
        val DRAGON_LEATHER =
            intArrayOf(
                Items.GREEN_D_LEATHER_1745,
                Items.BLUE_D_LEATHER_2505,
                Items.RED_DRAGON_LEATHER_2507,
                Items.BLACK_D_LEATHER_2509,
            )
    }
}
