package content.global.skill.cooking.handlers

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class AppleMushListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.COOKING_APPLE_1955, Scenery.APPLE_BARREL_7403) { player, _, _ ->
            if (getStatLevel(player, Skills.COOKING) < 14) {
                sendMessage(player, "You need a cooking level of 14 in order to do that.")
                return@onUseWith false
            }
            if (!inInventory(player, Items.BUCKET_1925)) {
                sendMessage(player, "You need a bucket to do that.")
                return@onUseWith false
            }
            if (!inInventory(player, Items.COOKING_APPLE_1955, 4)) {
                sendMessage(player, "You need at least 4 cooking apples.")
                return@onUseWith false
            }

            forceWalk(player, START_LOCATION, "smart")
            if (player.location == START_LOCATION) {
                removeItem(player, Item(Items.COOKING_APPLE_1955, 4))
                makePulp(player)
            }
            return@onUseWith true
        }
    }

    private fun makePulp(player: Player) {
        submitIndividualPulse(
            player,
            object : Pulse(1) {
                private var counter = 0

                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> {
                            animate(player, JUMP_ON_BARREL)
                            forceMove(player, START_LOCATION, BARREL_LOCATION, 0, 5, null, -1)
                            replaceScenery(
                                core.game.node.scenery.Scenery(
                                    EMPTY_BARREL,
                                    BARREL_LOCATION,
                                ),
                                APPLE_BARREL,
                                -1,
                            )
                        }

                        3 ->
                            replaceScenery(
                                core.game.node.scenery.Scenery(
                                    APPLE_BARREL,
                                    BARREL_LOCATION,
                                ),
                                APPLE_MUSH_BARREL,
                                -1,
                            )

                        5 -> forceMove(player, BARREL_LOCATION, END_LOCATION, 0, 20, Direction.SOUTH, -1)
                        7 -> resetAnimator(player)
                        8 -> forceWalk(player, TAP_LOCATION, "smart")
                        11 -> {
                            face(player, TAP_SCENERY_LOCATION)
                            animate(player, Animations.HUMAN_WITHDRAW_833)
                            player.inventory.add(APPLE_MUSH)
                            replaceScenery(
                                core.game.node.scenery.Scenery(
                                    APPLE_MUSH_BARREL,
                                    BARREL_LOCATION,
                                ),
                                EMPTY_BARREL,
                                -1,
                            )
                            sendMessage(player, "You fill the bucket with apple mush.")
                        }
                    }
                    return false
                }
            },
        )
    }

    companion object {
        private val START_LOCATION = Location.create(2914, 10193, 1)
        private val BARREL_LOCATION = Location(2914, 10192, 1)
        private val END_LOCATION = Location.create(2914, 10191, 1)
        private val TAP_LOCATION = Location.create(2916, 10193, 1)
        private val TAP_SCENERY_LOCATION = Location(2915, 10193, 1)
        private val APPLE_MUSH = Item(Items.APPLE_MUSH_5992, 1)
        private const val JUMP_ON_BARREL = Animations.JUMPING_ON_APPLE_MUSH_2306
        private const val APPLE_MUSH_BARREL = Scenery.APPLE_BARREL_7404
        private const val EMPTY_BARREL = Scenery.APPLE_BARREL_7403
        private const val APPLE_BARREL = Scenery.BARREL_16885
    }
}
