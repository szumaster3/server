package content.region.kandarin.handlers.barbtraining.smithing

import core.api.amountInInventory
import core.api.asItem
import core.api.skill.sendSkillDialogue
import core.api.submitIndividualPulse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Scenery

class BarbSmithingListener : InteractionListener {
    private val bars = BarbarianWeapon.values().map { it.requiredBar }.toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, bars, Scenery.BARBARIAN_ANVIL_25349) { player, used, _ ->
            val weapon = BarbarianWeapon.product[used.id] ?: return@onUseWith true

            sendSkillDialogue(player) {
                withItems(weapon.spearId.asItem(), weapon.hastaId.asItem())
                create { id, amount ->
                    submitIndividualPulse(
                        entity = player,
                        pulse = BarbSmithingPulse(player, weapon, amount, id)
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
