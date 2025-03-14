package content.global.handlers.item.withobject

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.system.task.Pulse
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class SandSourceListener : InteractionListener {
    companion object {
        private val SANDPITS = intArrayOf(Scenery.SAND_PIT_2645, Scenery.SAND_PIT_4373, Scenery.SANDPIT_10814)
        private val SAND_PILE = intArrayOf(Scenery.SAND_2977, Scenery.SAND_2978, Scenery.SAND_2979)
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.BUCKET_1925, *SANDPITS) { player, used, _ ->
            val numEmptyBuckets = amountInInventory(player, used.id)

            var animationTrigger = 0
            runTask(player, 2, numEmptyBuckets) {
                if (removeItem(player, used)) {
                    if (animationTrigger % 2 == 0) {
                        animate(player, Animations.FILL_BUCKET_SAND_895)
                    }
                    sendMessage(player, "You fill the bucket with sand.")
                    addItem(player, Items.BUCKET_OF_SAND_1783)
                }
                animationTrigger++
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.BUCKET_1925, *SAND_PILE) { player, used, with ->
            val numEmptyBuckets = amountInInventory(player, used.id)
            var animationTrigger = 0
            runTask(player, 2, numEmptyBuckets) {
                if (removeItem(player, used)) {
                    if (animationTrigger % 2 == 0) {
                        animate(player, Animations.FILL_BUCKET_SAND_895)
                    }
                    sendMessage(player, "You fill the bucket with sand.")

                    if (with.id == 2979) {
                        removeScenery(with.asScenery())
                        submitWorldPulse(
                            object : Pulse(75) {
                                override fun pulse(): Boolean {
                                    addScenery(
                                        if (inBorders(player, getRegionBorders(11310))) {
                                            with.id - 2
                                        } else {
                                            with.id - 1
                                        },
                                        with.location,
                                        with.direction.ordinal,
                                    )
                                    return true
                                }
                            },
                        )
                    } else {
                        replaceScenery(with.asScenery(), with.id + 1, 75)
                    }
                    addItem(player, Items.BUCKET_OF_SAND_1783)
                }
                animationTrigger++
            }
            return@onUseWith true
        }
        on(SAND_PILE, IntType.SCENERY, "look") { player, node ->
            sendMessage(player, sceneryDefinition(node.id).examine.toString())
            return@on true
        }
    }
}
