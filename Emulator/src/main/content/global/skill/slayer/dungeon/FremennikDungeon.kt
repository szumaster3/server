package content.global.skill.slayer.dungeon

import content.global.skill.agility.AgilityHandler
import core.api.getStatLevel
import core.api.impact
import core.api.sendMessage
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.map.zone.ZoneRestriction
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Animations

@Initializable
class FremennikDungeon :
    MapZone("fremennik", true, ZoneRestriction.CANNON),
    Plugin<Any?> {
    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any?,
    ): Any? {
        return true
    }

    override fun interact(
        entity: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (entity is Player && target is Scenery) {
            val dir = Direction.getLogicalDirection(entity.location, target.location)

            return when (target.id) {
                9326 -> handlePyrefiendArea(entity, target, dir)
                9321 -> handleNarrowCrevice(entity, target, dir)
                else -> super.interact(entity, target, option)
            }
        }
        return super.interact(entity, target, option)
    }

    private fun handlePyrefiendArea(
        player: Player,
        scenery: Scenery,
        direction: Direction,
    ): Boolean {
        if (getStatLevel(player, Skills.AGILITY) < 81) {
            sendMessage(player, "You need an agility level of at least 81 to do this.")
            return true
        }
        player.lock()
        GameWorld.Pulser.submit(
            object : Pulse(1, player) {
                var count = 0

                override fun pulse(): Boolean {
                    return when (++count) {
                        1 -> {
                            val start = scenery.location.transform(direction.opposite, 2)
                            player.walkingQueue.reset()
                            player.walkingQueue.addPath(start.x, start.y)
                            false
                        }

                        2 -> {
                            player.faceLocation(scenery.location)
                            false
                        }

                        3 -> {
                            val end = scenery.location.transform(direction, 1)
                            AgilityHandler.forceWalk(
                                player,
                                -1,
                                player.location,
                                end,
                                Animation.create(1995),
                                20,
                                0.0,
                                null,
                            )
                            false
                        }

                        4 -> {
                            handleAgilityFailure(player)
                            player.animate(Animation.create(Animations.JUMP_WEREWOLF_1603))
                            player.unlock()
                            true
                        }

                        else -> true
                    }
                }
            },
        )
        return true
    }

    private fun handleAgilityFailure(player: Player) {
        val fail = AgilityHandler.hasFailed(player, 20, 0.1)
        if (fail) {
            impact(player, RandomFunction.random(6), HitsplatType.NORMAL)
            sendMessage(player, "You trigger the trap as you jump over it.")
        }
    }

    private fun handleNarrowCrevice(
        player: Player,
        sceneryScenery: Scenery,
        dir: Direction,
    ): Boolean {
        if (getStatLevel(player, Skills.AGILITY) < 62) {
            sendMessage(player, "You need an agility level of at least 62 to do this.")
            return true
        }
        val end = sceneryScenery.location.transform(dir, 4)
        AgilityHandler.walk(
            player,
            -1,
            player.location,
            end,
            Animation.create(156),
            10.0,
            "You climb your way through the narrow crevice.",
        )
        return true
    }

    override fun configure() {
        register(ZoneBorders(2671, 9950, 2813, 10054))
    }
}
