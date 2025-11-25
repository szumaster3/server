package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityHandler.extinguishLightOnWater
import content.global.skill.agility.AgilityShortcut
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import shared.consts.Animations
import shared.consts.Scenery
import shared.consts.Sounds
import kotlin.random.Random

/**
 * Represents the Log balance shortcut near Sinclair Mansion.
 */
@Initializable
class SinclairMansionLogShortcut :
    AgilityShortcut(intArrayOf(Scenery.LOG_BALANCE_9322, Scenery.LOG_BALANCE_9324), 48, 0.0, "walk-across") {

    override fun run(
        player: Player,
        scenery: core.game.node.scenery.Scenery,
        option: String,
        failed: Boolean,
    ) {
        val fromSouth = scenery.id == Scenery.LOG_BALANCE_9324
        val start = player.location
        val end = start.transform(0, if (fromSouth) 4 else -4, 0)

        val failLocation = Location(if (fromSouth) 2723 else 2721, 3594)
        val failLand = Location(if (fromSouth) 2726 else 2718, 3596)

        face(player, scenery)
        playAudio(player, Sounds.LOG_BALANCE_2470)

        if (failed) {
            handleFailure(player, start, failLocation, failLand)
        } else {
            AgilityHandler.forceWalk(player, -1, start, end, logBalanceAnimation, 10, 0.0, null, 1).endAnimation =
                Animation.RESET
            if (!player.achievementDiaryManager.getDiary(DiaryType.SEERS_VILLAGE)!!.isComplete(1, 0)) {
                player.achievementDiaryManager.getDiary(DiaryType.SEERS_VILLAGE)!!.updateTask(player, 1, 0, true)
            }
        }
    }

    private fun handleFailure(player: Player, start: Location, failLocation: Location, failLand: Location) {
        AgilityHandler.forceWalk(player, -1, start, failLocation, failAnimation, 10, 0.0, null, 0)
        AgilityHandler.forceWalk(player, -1, failLocation, failLand, swimmingLoopAnimation, 15, 0.0, null, 3)

        Pulser.submit(object : Pulse(1, player) {
            private var tick = 0
            override fun pulse(): Boolean {
                when (tick++) {
                    0 -> {
                        visualize(player, failAnimation, splashGraphics)
                        playAudio(player, Sounds.WATERSPLASH_2496)
                        extinguishLightOnWater(player)
                        teleport(player, failLocation)
                        animate(player, swimmingLoopAnimation)
                    }

                    1 -> {
                        AgilityHandler.fail(player, 1, failLand, swimmingAnimation, Random.nextInt(1, 7), null)
                        return true
                    }
                }
                return false
            }
        })
    }

    companion object {
        private val logBalanceAnimation = Animation(Animations.BALANCE_WALK_ACROSS_LOG_9908)
        private val swimmingAnimation = Animation(Animations.SWIMMING_6988)
        private val swimmingLoopAnimation = Animation(Animations.SWIMMING_LOOP_6989)
        private val failAnimation = Animation(Animations.FALL_LOG_2582)
        private val splashGraphics = Graphics(shared.consts.Graphics.WATER_SPLASH_68)
    }
}
