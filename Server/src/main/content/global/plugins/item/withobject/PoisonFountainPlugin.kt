package content.global.plugins.item.withobject

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery

class PoisonFountainPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles use the poisoned fish food on fountain.
         */

        onUseWith(IntType.SCENERY, Items.POISONED_FISH_FOOD_274, Scenery.FOUNTAIN_153) { player, used, _ ->
            if (getAttribute(player, "piranhas-killed", false)) {
                sendMessage(player, "The piranhas are dead already.")
                return@onUseWith true
            }
            if (!removeItem(player, used)) {
                return@onUseWith false
            }
            lock(player, 3)
            animate(player, Animations.HUMAN_PICKPOCKETING_881)
            sendMessage(player, "You pour the poisoned fish food into the fountain.")
            setAttribute(player, "/save:piranhas-killed", true)
            sendMessage(player, "The piranhas start eating the food...", 1)
            sendMessage(player, "... then die and float to the surface.", 2)
            return@onUseWith true
        }
    }
}
