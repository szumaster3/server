package content.minigame.pest_control.plugin

import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import shared.consts.Components

/**
 * Represents the Pest Control lander zone.
 */
class PCLanderZone(private val activities: Array<PestControlActivityPlugin>) : MapZone("pest control lander", true, ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.FIRES, ZoneRestriction.FOLLOWERS, ZoneRestriction.CANNON) {

    override fun interact(e: Entity, target: Node, option: Option): Boolean {
        if (target is Scenery) {
            when (target.id) {
                14314 -> handleLeave(e, activities[0])
                25629 -> handleLeave(e, activities[1])
                25630 -> handleLeave(e, activities[2])
                else -> return false
            }
            return true
        }
        return false
    }

    private fun handleLeave(e: Entity, activity: PestControlActivityPlugin) {
        if (activity.waitingPlayers.contains(e)) {
            activity.waitingPlayers.remove(e)
            e.properties.teleportLocation = activity.leaveLocation
        }
    }

    override fun teleport(e: Entity, type: Int, node: Node?): Boolean {
        if (e is Player && type != -1) {
            for (activity in activities) {
                if (activity.waitingPlayers.contains(e)) {
                    e.packetDispatch.sendMessage("The knights don't appreciate you teleporting off their craft!")
                    return false
                }
            }
        }
        return super.teleport(e, type, node)
    }

    override fun leave(e: Entity, logout: Boolean): Boolean {
        if (e is Player) {
            e.interfaceManager.getComponent(Components.PEST_LANDER_OVERLAY_407)?.let {
                e.interfaceManager.closeOverlay()
            }

            for (activity in activities) {
                if (activity.waitingPlayers.remove(e)) {
                    if (logout) {
                        e.location = activity.leaveLocation
                    }
                    break
                }
            }
        }
        return super.leave(e, logout)
    }

    override fun configure() {
        val boatA = ZoneBorders(2659, 2637, 2664, 2664)
        val boatB = ZoneBorders(2637, 2641, 2642, 2648)
        val boatC = ZoneBorders(2631, 2648, 2636, 2655)

        register(boatA)
        register(boatB)
        register(boatC)
    }
}
