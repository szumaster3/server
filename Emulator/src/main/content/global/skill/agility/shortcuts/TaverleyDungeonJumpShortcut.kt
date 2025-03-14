package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Scenery
import org.rs.consts.Sounds
import kotlin.random.Random

class TaverleyDungeonJumpShortcut : InteractionListener {
    override fun defineListeners() {
        on(Scenery.STRANGE_FLOOR_9294, IntType.SCENERY, "jump-over") { player, node ->
            if (!hasLevelDyn(player, Skills.AGILITY, 80)) {
                sendMessage(player, "You need an agility level of at least 80 to do this.")
                return@on true
            }
            lock(player, 6)
            AgilityHandler.forceWalk(
                player,
                -1,
                if (player.location.x >= 2880) Location.create(2881, 9813, 0) else Location.create(2877, 9813, 0),
                if (player.location.x >= 2880) Location.create(2877, 9813, 0) else Location.create(2881, 9813, 0),
                Animation(1995),
                13,
                0.0,
                null,
                0,
            )
            submitIndividualPulse(
                player,
                object : Pulse(1, player) {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> player.animator.forceAnimation(Animation(Animations.RUNNING_OSRS_STYLE_1995))
                            1 -> {
                                playAudio(player, Sounds.JUMP_2461)
                                player.animator.forceAnimation(Animation(Animations.JUMP_WEREWOLF_1603))
                                if (AgilityHandler.hasFailed(player, 1, 0.1)) {
                                    playAudio(player, Sounds.FLOOR_SPIKES_1383)
                                    playAudio(player, Sounds.JUMP_BLADES_2464)
                                    animateScenery(node.asScenery(), 1111)
                                    AgilityHandler.fail(
                                        player,
                                        0,
                                        if (player.location.x >= 2880) {
                                            Location.create(
                                                2877,
                                                9813,
                                                0,
                                            )
                                        } else {
                                            Location.create(2881, 9813, 0)
                                        },
                                        Animation(1603),
                                        Random.nextInt(1, 7),
                                        null,
                                    )
                                    sendMessage(player, "You trigger the trap as you jump over it.")
                                    return true
                                }
                            }
                        }
                        return false
                    }
                },
            )
            return@on true
        }
    }
}
