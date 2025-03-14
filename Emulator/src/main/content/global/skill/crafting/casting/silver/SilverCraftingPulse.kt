package content.global.skill.crafting.casting.silver

import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class SilverCraftingPulse(
    val player: Player,
    val product: Silver,
    val furnace: Scenery,
    var amount: Int,
) : Pulse() {
    override fun pulse(): Boolean {
        if (amount < 1) return true

        if (!inInventory(player, product.required) || !inInventory(player, Items.SILVER_BAR_2355)) {
            return true
        }

        animate(player, Animations.HUMAN_FURNACE_SMELT_3243)
        playAudio(player, Sounds.FURNACE_2725)
        if (removeItem(player, Items.SILVER_BAR_2355, Container.INVENTORY)) {
            addItem(player, product.product, product.amount)
            rewardXP(player, Skills.CRAFTING, product.experience)

            player.dispatch(
                ResourceProducedEvent(
                    itemId = product.product,
                    amount = product.amount,
                    source = furnace,
                    original = Items.SILVER_BAR_2355,
                ),
            )
        } else {
            return true
        }

        amount--
        delay = 5

        return false
    }
}
