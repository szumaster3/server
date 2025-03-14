package content.global.skill.cooking.handlers

import content.global.skill.cooking.CookingPulse
import core.api.getItemName
import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery

class PieCookingPulse(
    override val player: Player,
    override val scenery: Scenery,
    initial: Int,
    product: Int,
    amount: Int,
) : CookingPulse(player, scenery, initial, product, amount) {
    override fun checkRequirements(): Boolean {
        if (!scenery.name.lowercase().contains("range")) {
            sendMessage(player, "This can only be cooked on a range.")
            return false
        }
        return super.checkRequirements()
    }

    override fun getMessage(
        food: Item,
        product: Item,
        burned: Boolean,
    ): String {
        return if (burned) {
            "You accidentally burn the pie."
        } else {
            "You successfully bake a delicious " + getItemName(product.id).lowercase() + "."
        }
    }
}
