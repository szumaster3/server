package content.global.handlers.scenery

import core.api.*
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.ImpactHandler
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class HayBaleListener : InteractionListener {
    override fun defineListeners() {
        on(HAY, IntType.SCENERY, "search") { player, node ->
            val rand = RandomFunction.random(50)
            lock(player, 2)
            animate(player, Animations.HUMAN_BURYING_BONES_827)
            sendMessage(player, "You search the " + node.name.lowercase() + "...")
            if (freeSlots(player) == 0) {
                sendDialogue(player, "You don't have enough inventory space.")
                return@on true
            }

            when (rand) {
                1 -> {
                    addItemOrDrop(player, Items.NEEDLE_1733)
                    sendPlayerDialogue(
                        player,
                        "Wow! A needle! Now what are the chances of finding that?",
                        FaceAnim.HALF_GUILTY,
                    )
                }

                25 -> {
                    sendDialogue(player, "Ow! There's something sharp in there!")
                    impact(player, 1, ImpactHandler.HitsplatType.NORMAL)
                }

                else -> {
                    sendMessage(player, "You find nothing of interest.")
                }
            }

            return@on true
        }

        onUseWith(IntType.SCENERY, Items.EMPTY_SACK_5418, Scenery.HAY_BALE_8713) { player, used, with ->
            replaceSlot(player, used.asItem().slot, Item(Items.HAY_SACK_6057, 1))
            sendMessage(player, "You fill the sack with straw.")
            return@onUseWith true
        }
    }

    companion object {
        val HAY =
            intArrayOf(
                Scenery.HAY_BALES_298,
                Scenery.HAY_BALES_299,
                Scenery.HAYSTACK_300,
                Scenery.HAY_BALE_304,
                Scenery.HAY_BALE_36892,
                Scenery.HAY_BALE_36893,
                Scenery.HAY_BALES_36894,
                Scenery.HAY_BALES_36895,
                Scenery.HAY_BALES_36896,
                Scenery.HAY_BALES_36897,
                Scenery.HAY_BALES_36898,
                Scenery.HAY_BALES_36899,
            )
    }
}
