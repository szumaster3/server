package core.game.world.map.zone.impl

import core.ServerConstants
import core.api.sendMessage
import core.api.ui.restoreTabs
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalPlayers
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders

class ModeratorZone : MapZone("Moderator Zone", true) {
    override fun enter(entity: Entity): Boolean {
        if (entity !is Player) {
            return true
        }

        if ((!open && entity.details.rights !== Rights.ADMINISTRATOR)) {
            home(entity)
            return false
        }
        if (entity.details.rights === Rights.PLAYER_MODERATOR) {
            sendMessage(entity, toggleMessage)
        }
        return true
    }

    override fun leave(
        entity: Entity,
        logout: Boolean,
    ): Boolean {
        if (entity !is Player) {
            return true
        }
        restoreTabs(entity.asPlayer())
        return true
    }

    override fun interact(
        entity: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (entity is Player) {
        }
        return super.interact(entity, target, option)
    }

    override fun configure() {
        register(ZoneBorders(2840, 5204, 2853, 5224))
    }

    companion object {
        @JvmStatic var open: Boolean = true

        @JvmStatic val center: Location = Location.create(2846, 5213, 0)

        @JvmStatic fun toggle(
            player: Player,
            on: Boolean,
        ) {
            sendMessage(player, toggleMessage)
            if (!on) {
                for (p in getLocalPlayers(center)) {
                    if (p.details.rights === Rights.ADMINISTRATOR) {
                        continue
                    }
                    home(p)
                }
            }
        }

        @JvmStatic val toggleMessage: String =
            "The moderator room is currently " + (if (open) "available" else "not available") + " to player moderators."

        @JvmStatic fun home(player: Player) {
            player.teleporter.send(ServerConstants.HOME_LOCATION, TeleportType.NORMAL)
        }

        @JvmStatic fun teleport(player: Player) {
            player.teleporter.send(center, TeleportType.NORMAL)
        }
    }
}
