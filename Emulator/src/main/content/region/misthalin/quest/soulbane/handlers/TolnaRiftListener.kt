package content.region.misthalin.quest.soulbane.handlers

import core.api.*
import core.api.utils.PlayerCamera
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class TolnaRiftListener : InteractionListener {
    private val SCENERY_RIFT =
        intArrayOf(
            13967,
            13969,
            3971,
            13972,
            13973,
            13974,
            13975,
            13976,
            13977,
            13978,
            13979,
            13980,
            13968,
            13981,
            13982,
            13983,
            13985,
            13986,
            13987,
            13988,
            13989,
            13990,
            13991,
            13992,
            13993,
        )

    override fun defineListeners() {
        on(SCENERY_RIFT, SCENERY, "enter") { player, _ ->
            sendMessage(player, "You can't just jump down there.")
            return@on true
        }

        onUseWith(SCENERY, Items.ROPE_954, Scenery.RIFT_13975) { player, _, _ ->
            if (!removeItem(player, Item(Items.ROPE_954, 1), Container.INVENTORY)) {
                sendMessage(player, "Nothing interesting happens.")
            } else {
                setVarbit(player, ASoulsBaneUtils.VARBIT_TOLNA_RIFT_ROPE, 1, true)
                setAttribute(player, "/save:${ASoulsBaneUtils.ATTRIBUTE_TOLNA_RIFT_ACCESS}", true)
            }
            return@onUseWith true
        }

        on(Scenery.RIFT_13970, SCENERY, "enter") { player, node ->
            if (getAttribute(player, ASoulsBaneUtils.ATTRIBUTE_TOLNA_RIFT_ACCESS, false)) {
                lock(player, 6)
                face(player, node.asScenery())
                PlayerCamera(player).reset()
                submitIndividualPulse(
                    player,
                    object : Pulse(1) {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> {
                                    PlayerCamera(player).rotateTo(3309, 3455, 400, 200)
                                    animate(player, ASoulsBaneUtils.ANIMATION_TOLNA_RIFT_CRAWL)
                                }

                                5 -> teleport(player, location(3297, 9824, 0))
                                6 -> {
                                    PlayerCamera(player).reset()
                                    resetAnimator(player)
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }
            return@on true
        }

        on(Scenery.ROPE_13999, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.USE_LADDER_828), Location(3309, 3452, 0))
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.RIFT_13970), "enter") { _, _ ->
            return@setDest Location.create(3309, 3452, 0)
        }
    }
}
