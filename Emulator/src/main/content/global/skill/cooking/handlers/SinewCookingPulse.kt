package content.global.skill.cooking.handlers

import content.global.skill.cooking.CookingPulse
import content.global.skill.cooking.data.CookableItem
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery

class SinewCookingPulse(
    player: Player?,
    scenery: Scenery?,
    initial: Int,
    product: Int,
    amount: Int,
) : CookingPulse(player!!, scenery!!, initial, product, amount) {
    override fun checkRequirements(): Boolean {
        properties = CookableItem.SINEW
        return super.checkRequirements()
    }

    override fun isBurned(
        player: Player,
        scenery: Scenery,
        food: Int,
    ): Boolean {
        return false
    }

    override fun getMessage(
        food: Item,
        product: Item,
        burned: Boolean,
    ): String {
        return "You dry a piece of beef and extract the sinew."
    }
}
