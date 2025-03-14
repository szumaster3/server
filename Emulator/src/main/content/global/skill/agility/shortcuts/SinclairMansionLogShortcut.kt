package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.Scenery
import org.rs.consts.Sounds
import kotlin.random.Random

class SinclairMansionLogShortcut : InteractionListener {
    override fun defineListeners() {
        on(sinclarLog, IntType.SCENERY, "walk-across") { player, node ->
            if (!hasLevelDyn(player, Skills.AGILITY, 48)) {
                sendMessage(player, "You need an agility level of at least 48 to do this.")
                return@on true
            }

            val start = player.location
            var failLand = Location.create(2718, 3592, 0)
            var fromSouth = false

            lock(player, 10)
            lockInteractions(player, 10)
            face(player, node)
            if (node.id == Scenery.LOG_BALANCE_9324) {
                fromSouth = true
                failLand = Location.create(2726, 3596, 0)
            }

            if (AgilityHandler.hasFailed(player, 48, 0.1)) {
                val failLocation = if (fromSouth) Location.create(2723, 3594, 0) else Location.create(2721, 3594, 0)
                AgilityHandler.forceWalk(player, -1, start, failLocation, failAnimation, 10, 0.0, null, 0)
                AgilityHandler.forceWalk(player, -1, failLocation, failLand, swimmingLoopAnimation, 15, 0.0, null, 1)
                submitIndividualPulse(
                    player,
                    object : Pulse(2) {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> {
                                    visualize(player, failAnimation, splashGraphics)
                                    playAudio(player, Sounds.WATERSPLASH_2496)
                                    teleport(player, failLocation)
                                    player.animator.forceAnimation(swimmingLoopAnimation)
                                }

                                1 ->
                                    AgilityHandler
                                        .fail(
                                            player,
                                            1,
                                            failLand,
                                            swimmingAnimation,
                                            Random.nextInt(1, 7),
                                            null,
                                        ).also { return true }
                            }
                            return false
                        }
                    },
                )
            } else {
                val end = if (fromSouth) start.transform(0, 4, 0) else start.transform(0, -4, 0)
                AgilityHandler.forceWalk(player, -1, start, end, logBalanceAnimation, 10, 0.0, null, 0).endAnimation =
                    Animation.RESET
                runTask(player, 4) {
                    if (node.id in sinclarLog &&
                        !player.achievementDiaryManager
                            .getDiary(DiaryType.SEERS_VILLAGE)!!
                            .isComplete(1, 0)
                    ) {
                        player.achievementDiaryManager
                            .getDiary(DiaryType.SEERS_VILLAGE)!!
                            .updateTask(player, 1, 0, true)
                    }
                }
            }
            return@on true
        }
    }

    companion object {
        private val sinclarLog = intArrayOf(Scenery.LOG_BALANCE_9322, Scenery.LOG_BALANCE_9324)
        private val logBalanceAnimation = Animation(Animations.BALANCE_WALK_ACROSS_LOG_9908)
        private val swimmingAnimation = Animation(Animations.SWIMMING_6988)
        private val swimmingLoopAnimation = Animation(Animations.SWIMMING_LOOP_6989)
        private val failAnimation = Animation(Animations.FALL_LOG_2582)
        private val splashGraphics = Graphics(68)
    }
}
