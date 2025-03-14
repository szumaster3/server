package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomUtils
import org.rs.consts.Scenery
import org.rs.consts.Sounds

class ZanarisSqueezeShortcut : InteractionListener {
    override fun defineListeners() {
        on(Scenery.JUTTING_WALL_12127, IntType.SCENERY, "squeeze-past") { player, node ->
            handleShortcut(player, node.asScenery())
            return@on true
        }
    }

    private fun checkRequirements(
        player: Player,
        location: Location,
    ): Boolean {
        val agilityLevel = getStatLevel(player, Skills.AGILITY)
        val requiredLevel =
            when (location) {
                Location(2400, 4403) -> 46
                Location(2415, 4402), Location(2408, 4395) -> 66
                else -> return true
            }
        return if (agilityLevel < requiredLevel) {
            sendMessageWithDelay(player, "You need an agility level of $requiredLevel to negotiate this obstacle.", 1)
            false
        } else {
            true
        }
    }

    private fun calculateRotation(
        sceneryRotation: Int,
        direction: Direction,
    ): Int {
        return when {
            sceneryRotation == 3 && direction == Direction.SOUTH -> 1
            sceneryRotation == 3 && direction == Direction.NORTH -> 0
            direction == Direction.NORTH || (direction == Direction.SOUTH && sceneryRotation != 0) -> 1
            else -> 0
        }
    }

    private fun handleShortcut(
        player: Player,
        node: Node,
    ) {
        val scenery = node.asScenery()
        val direction = Direction.getLogicalDirection(player.location, scenery.location)
        val end = player.location.transform(direction.stepX * 2, direction.stepY * 2, 0)

        sendMessage(player, "You try to squeeze past.")

        if (!checkRequirements(player, scenery.location)) return

        playAudio(player, Sounds.SQUEEZE_THROUGH_ROCKS_1310, 1, 2)

        if (AgilityHandler.hasFailed(player, 10, 0.00300)) {
            handleFailure(player, direction, scenery.rotation, end)
        } else {
            AgilityHandler.walk(
                player,
                -1,
                player.location,
                end,
                Animation.create(157 - calculateRotation(scenery.rotation, direction)),
                0.0,
                null,
            )
        }
    }

    private fun handleFailure(
        player: Player,
        direction: Direction,
        sceneryRotation: Int,
        end: Location,
    ) {
        val forceChat = arrayOf("Arrgghhhh!", "Owww...", "Ahhh...")
        var repeat = 3
        var rand = RandomUtils.random(4)
        AgilityHandler.walk(
            player,
            -1,
            player.location,
            end,
            Animation.create(157 - calculateRotation(sceneryRotation, direction)),
            0.0,
            null,
        )
        lock(player, 4)
        GameWorld.Pulser.submit(
            object : Pulse(repeat, player) {
                override fun pulse(): Boolean {
                    runTask(player, repeat - 3, repeat) {
                        sendChat(player, forceChat[--repeat])
                        player.impactHandler.manualHit(
                            player,
                            rand,
                            ImpactHandler.HitsplatType.NORMAL,
                        )
                    }
                    AgilityHandler.fail(
                        player,
                        1,
                        end,
                        Animation.create(761 - calculateRotation(sceneryRotation, direction)),
                        -1,
                        null,
                    )
                    return true
                }
            },
        )
    }
}
