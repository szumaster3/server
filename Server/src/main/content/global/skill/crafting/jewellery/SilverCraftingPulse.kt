package content.global.skill.crafting.jewellery

import core.api.*
import core.game.dialogue.InputType
import core.game.event.ResourceProducedEvent
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import shared.consts.*

class SilverCraftingPulse(val player: Player, val product: SilverProduct, val furnace: core.game.node.scenery.Scenery, var amount: Int) : Pulse() {

    override fun pulse(): Boolean {
        if (!clockReady(player, Clocks.SKILLING)) return false
        delayClock(player, Clocks.SKILLING, 5)
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
        return false
    }
}