package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.*
import core.api.ui.setMinimapState
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.DARK_RED
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Scenery

class LunarIsleMineShortcut : InteractionListener {
    override fun defineListeners() {
        on(Scenery.CAVE_ENTRANCE_11399, IntType.SCENERY, "crawl-through") { player, _ ->
            if (player.location.x != 2335) {
                crawlingStart(player)
            } else {
                sendDialogueLines(
                    player,
                    "${DARK_RED}Warning:</col>",
                    "If you leave any items in the cave, they probably won't remain when",
                    "you return!",
                )
                addDialogueAction(player) { player, button ->
                    if (button == 4) {
                        crawlingStart(player)
                    }
                    return@addDialogueAction
                }
            }
            return@on true
        }
    }

    private fun crawlingStart(player: Player) {
        animate(player, CRAWL_START)
        openInterface(player, Components.FADE_TO_BLACK_115)
        setMinimapState(player, 2)
        queueScript(player, 2, QueueStrength.NORMAL) { stage: Int ->
            when (stage) {
                0 -> {
                    AgilityHandler.forceWalk(
                        player,
                        -1,
                        if (player.location.x != 2335) {
                            Location(2341, 10356, 2)
                        } else {
                            Location(
                                2335,
                                10345,
                                2,
                            )
                        },
                        if (player.location.x != 2335) {
                            Location(2335, 10345, 2)
                        } else {
                            Location(
                                2341,
                                10356,
                                2,
                            )
                        },
                        CRAWL_THROUGH,
                        40,
                        0.0,
                        null,
                    )
                    return@queueScript delayScript(player, 6)
                }

                1 -> {
                    openInterface(player, Components.FADE_FROM_BLACK_170)
                    setMinimapState(player, 0)
                    resetAnimator(player)
                    return@queueScript delayScript(player, 6)
                }

                2 -> return@queueScript stopExecuting(player)
                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    companion object {
        val CRAWL_START: Animation = Animation.create(Animations.HUMAN_STAY_IN_CRAWL_POSITION_845)
        val CRAWL_THROUGH: Animation = Animation.create(Animations.HUMAN_TURNS_INVISIBLE_2590)
    }
}
