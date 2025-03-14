package content.global.skill.crafting.items.armour.leather.studded

import core.api.amountInInventory
import core.api.skill.sendSkillDialogue
import core.api.submitIndividualPulse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items

class StuddedArmourListener : InteractionListener {
    private val leatherItem = StuddedArmour.values().map { it.leather }.toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.ITEM, leatherItem, Items.STEEL_STUDS_2370) { player, used, _ ->
            val item = StuddedArmour.forId(used.id) ?: return@onUseWith true

            sendSkillDialogue(player) {
                withItems(item.product)
                create { _, amount ->
                    submitIndividualPulse(
                        entity = player,
                        pulse = StuddedArmourPulse(player, Item(item.leather), item, amount),
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
