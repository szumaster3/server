package content.global.skill.crafting.items.armour.leather.hard

import core.api.amountInInventory
import core.api.getStatLevel
import core.api.sendDialogue
import core.api.skill.sendSkillDialogue
import core.api.submitIndividualPulse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import org.rs.consts.Items

class HardLeatherCraftingListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.HARD_LEATHER_1743, Items.NEEDLE_1733) { player, used, _ ->
            if (getStatLevel(player, Skills.CRAFTING) < 28) {
                sendDialogue(player, "You need a crafting level of " + 28 + " to make a hard leather body.")
                return@onUseWith false
            }
            sendSkillDialogue(player) {
                withItems(Items.HARDLEATHER_BODY_1131)

                create { _, amount ->
                    submitIndividualPulse(
                        entity = player,
                        pulse = HardLeatherCraftingPulse(player, used.asItem(), amount),
                    )
                }
                calculateMaxAmount {
                    amountInInventory(player, used.id)
                }
            }
            return@onUseWith true
        }
    }
}
