package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
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
        on(sinclairLogs, IntType.SCENERY, "walk-across") { player, node ->
            if (!hasLevelDyn(player, Skills.AGILITY, 48)) {
                player.sendMessage("You need an agility level of at least 48 to do this.")
                return@on true
            }

            val fromSouth = node.id == Scenery.LOG_BALANCE_9324
            val start = player.location
            val end = start.transform(0, if (fromSouth) 4 else -4, 0)

            var fail = AgilityHandler.hasFailed(player, 48, 0.1)
            val failLocation = Location(if (fromSouth) 2723 else 2721, 3594)
            val failLand = Location(if (fromSouth) 2726 else 2718, 3596)

            lock(player, 10)
            lockInteractions(player, 10)
            face(player, node)
            playAudio(player, Sounds.LOG_BALANCE_2470)

            if (fail) {
                handleFailure(player, start, failLocation, failLand)
            } else {
                AgilityHandler.forceWalk(player, -1, start, end, logBalanceAnimation, 10, 0.0, null, 1).endAnimation = Animation.RESET
                if (!player.achievementDiaryManager.getDiary(DiaryType.SEERS_VILLAGE)!!.isComplete(1, 0)) {
                    player.achievementDiaryManager.getDiary(DiaryType.SEERS_VILLAGE)!!.updateTask(player, 1, 0, true)
                }
            }

            return@on true
        }
    }

    private fun handleFailure(player: Player, start: Location, failLocation: Location, failLand: Location) {
        AgilityHandler.forceWalk(player, -1, start, failLocation, failAnimation, 10, 0.0, null, 0)
        AgilityHandler.forceWalk(player, -1, failLocation, failLand, swimmingLoopAnimation, 15, 0.0, null, 1)

        submitIndividualPulse(player, object : Pulse(2) {
            var tick = 0
            override fun pulse(): Boolean {
                when (tick++) {
                    0 -> {
                        visualize(player, failAnimation, splashGraphics)
                        playAudio(player, Sounds.WATERSPLASH_2496)
                        teleport(player, failLocation)
                        player.animator.forceAnimation(swimmingLoopAnimation)
                    }

                    1 -> {
                        AgilityHandler.fail(
                            player, 1, failLand, swimmingAnimation, Random.nextInt(1, 7), null
                        )
                        return true
                    }
                }
                return false
            }
        })
    }

    companion object {
        private val sinclairLogs = intArrayOf(Scenery.LOG_BALANCE_9322, Scenery.LOG_BALANCE_9324)
        private val logBalanceAnimation = Animation(Animations.BALANCE_WALK_ACROSS_LOG_9908)
        private val swimmingAnimation = Animation(Animations.SWIMMING_6988)
        private val swimmingLoopAnimation = Animation(Animations.SWIMMING_LOOP_6989)
        private val failAnimation = Animation(Animations.FALL_LOG_2582)
        private val splashGraphics = Graphics(org.rs.consts.Graphics.WATER_SPLASH_68)
    }
}
