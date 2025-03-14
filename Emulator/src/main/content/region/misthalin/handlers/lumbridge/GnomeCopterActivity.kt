package content.region.misthalin.handlers.lumbridge

import core.api.removeAttribute
import core.api.setAttribute
import core.game.activity.ActivityPlugin
import core.game.container.impl.EquipmentContainer
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable

@Initializable
class GnomeCopterActivity : ActivityPlugin("Gnome copters", false, false, true) {
    private val usedLandingPads = BooleanArray(4)

    override fun newInstance(p: Player?): ActivityPlugin {
        return this
    }

    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (target is Scenery) {
            val `object` = target
            if (`object`.id == 30032) {
                flyGnomeCopter(e as Player, `object`)
                return true
            }
            if (`object`.id == 30036) {
                GnomeCopterSign.ENTRANCE.read((e as Player))
                return true
            }
        } else if (target is Item && e.getAttribute("gc:flying", false)) {
            (e as Player).packetDispatch.sendMessage("You can't do this right now.")
            return true
        }
        return false
    }

    override fun leave(
        e: Entity,
        logout: Boolean,
    ): Boolean {
        if (logout && e.getAttribute("gc:flying", false)) {
            e.location = spawnLocation
            (e as Player).equipment.remove(COPTER_ITEM)
        }
        return super.leave(e, logout)
    }

    private fun flyGnomeCopter(
        player: Player,
        scenery: Scenery,
    ) {
        if (player.location != scenery.location.transform(0, 1, 0)) {
            return
        }
        if (scenery.charge == 88) {
            player.packetDispatch.sendMessage("Someone else is already using this gnomecopter.")
            return
        }
        if (player.equipment[EquipmentContainer.SLOT_HAT] != null) {
            player.packetDispatch.sendMessage("You can't wear a hat on a Gnomecopter.")
            return
        }
        if (player.equipment[EquipmentContainer.SLOT_CAPE] != null) {
            player.packetDispatch.sendMessage("You can't wear a cape on a Gnomecopter.")
            return
        }
        if (player.equipment[3] != null || player.equipment[5] != null) {
            player.packetDispatch.sendMessage("You need to have your hands free to use this.")
            return
        }
        if (!player.inventory.containsItem(Item(12843))) {
            player.packetDispatch.sendMessage("You need to have gnomecopter ticket to use this.")
            return
        }
        setAttribute(player, "gc:flying", true)
        player.lock()
        player.inventory.remove(Item(12843))
        player.packetDispatch.sendMessage("The gnomecopter accepts the ticket and sets off for Castle Wars.")
        player.faceLocation(player.location.transform(0, 3, 0))
        scenery.charge = 88
        Pulser.submit(
            object : Pulse(1, player) {
                var stage: Int = 0

                override fun pulse(): Boolean {
                    if (++stage == 1) {
                        player.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6, 7, 11)
                        ForceMovement.run(
                            player,
                            player.location,
                            scenery.location,
                            ForceMovement.WALK_ANIMATION,
                            Animation(8955),
                            Direction.NORTH,
                            8,
                        )
                        player.lock()
                    } else if (stage == 3) {
                        player.equipment.replace(COPTER_ITEM, 3)
                        player.visualize(Animation.create(8956), Graphics.create(1578))
                        player.appearance.standAnimation = 8964
                        player.appearance.walkAnimation = 8961
                        player.appearance.runAnimation = 8963
                        player.appearance.turn180 = 8963
                        player.appearance.turn90ccw = 8963
                        player.appearance.turn90cw = 8963
                        player.appearance.standTurnAnimation = 8963
                    } else if (stage == 4) {
                        scenery.charge = 88
                        player.packetDispatch.sendSceneryAnimation(scenery, Animation(5))
                    } else if (stage == 16) {
                        player.walkingQueue.reset()
                        player.walkingQueue.addPath(scenery.location.x, scenery.location.y + 16, true)
                        Graphics.send(Graphics.create(1579), scenery.location)
                    } else if (stage == 20) {
                        scenery.charge = 1000
                        player.packetDispatch.sendSceneryAnimation(scenery, Animation(8941))
                    } else if (stage == 33) {
                        player.unlock()
                        landGnomeCopter(player)
                        return true
                    }
                    return false
                }
            },
        )
    }

    private fun landGnomeCopter(player: Player) {
        var index = 0
        index = 0
        while (index < usedLandingPads.size) {
            if (!usedLandingPads[index]) {
                break
            }
            index++
        }
        usedLandingPads[index] = true
        player.lock()
        val pad = index
        player.direction = Direction.SOUTH
        player.properties.teleportLocation = Location.create(3162, 3352, 0)
        Pulser.submit(
            object : Pulse(1, player) {
                var stage: Int = 0
                var tick: Int = 0

                override fun pulse(): Boolean {
                    if (++stage == 1) {
                        player.walkingQueue.reset()
                        player.walkingQueue.addPath(3162, 3348, true)
                        player.walkingQueue.addPath(3161 - (pad shl 1), 3336, true)
                        tick = stage + player.walkingQueue.queue.size
                    } else if (stage == tick) {
                        player.animate(Animation.create(8957))
                    } else if (stage == tick + 14) {
                        SceneryBuilder.add(Scenery(30034, player.location), 6)
                        player.equipment.replace(null, 3)
                        ForceMovement.run(
                            player,
                            player.location,
                            player.location.transform(0, -1, 0),
                            Animation(8959),
                            8,
                        )
                        player.lock(2)
                    } else if (stage == tick + 15) {
                        player.unlock()
                        player.interfaceManager.restoreTabs()
                        player.interfaceManager.openDefaultTabs()
                        usedLandingPads[pad] = false
                        removeAttribute(player, "gc:flying")
                        return true
                    }
                    return false
                }
            },
        )
    }

    override fun getSpawnLocation(): Location {
        return Location.create(3161, 3337, 0)
    }

    override fun configure() {
        register(ZoneBorders(3154, 3330, 3171, 3353))
    }

    companion object {
        private val COPTER_ITEM = Item(12842)
    }
}
