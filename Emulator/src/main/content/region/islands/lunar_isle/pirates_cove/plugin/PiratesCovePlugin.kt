package content.region.islands.lunar_isle.pirates_cove.plugin

import core.api.addItem
import core.api.freeSlots
import core.api.replaceScenery
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class PiratesCovePlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles looting the rotten apple, cooking apple, or swamp tar from barrels.
         */

        on(FULL_BARREL, IntType.SCENERY, "take-from") { player, node ->
            if (freeSlots(player) < 1) {
                sendMessage(player, "Not enough space in your inventory.")
                return@on true
            }

            val scenery = node.asScenery()
            if (scenery.charge >= 0) {
                player.animate(Animation(Animations.MULTI_TAKE_832))

                when (node.id) {
                    Scenery.BARREL_16884 -> addItem(player, Items.ROTTEN_APPLE_1984)
                    Scenery.BARREL_16885 -> addItem(player, Items.COOKING_APPLE_1955)
                    Scenery.TAR_BARREL_16860 -> addItem(player, Items.SWAMP_TAR_1939)
                }

                if (RandomFunction.random(1, 3) == 1) {
                    sendMessage(player, "The barrel became empty!")
                    when (node.id) {
                        Scenery.TAR_BARREL_16860 -> replaceScenery(scenery, Scenery.TAR_BARREL_16688, 40)
                        Scenery.BARREL_16884, Scenery.BARREL_16885 -> replaceScenery(scenery, EMPTY_BARREL, 40)
                    }
                    scenery.charge = 0
                }
            }
            return@on true
        }
    }

    companion object {
        private val FULL_BARREL = intArrayOf(Scenery.TAR_BARREL_16860, Scenery.BARREL_16884, Scenery.BARREL_16885)
        private const val EMPTY_BARREL = Scenery.BARREL_16886
    }
}
