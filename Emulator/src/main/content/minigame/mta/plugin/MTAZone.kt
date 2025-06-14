package content.minigame.mta.plugin

import core.ServerConstants
import core.api.*
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.RequestType
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneRestriction
import core.game.world.map.zone.ZoneType

open class MTAZone(
    name: String?,
    val items: Array<Item>,
) : MapZone(name, false, ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.FOLLOWERS) {
    var type: MTAType? = null

    init {
        zoneType = ZoneType.SAFE.id
    }

    override fun enter(entity: Entity): Boolean {
        if (entity is Player) {
            val player = entity.asPlayer() ?: return true
            if (type == null) {
                type = MTAType.forZone(this)
            }
            if (type != null) {
                player.interfaceManager.openOverlay(type!!.overlay)
                update(player)
            }
            player.properties.spawnLocation = Location(3363, 3302, 0)
        }
        return super.enter(entity)
    }

    override fun canRequest(
        type: RequestType,
        player: Player,
        target: Player,
    ): Boolean {
        sendDialogue(player, "You can't do that right now.")
        return false
    }

    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (e is Player) {
            if (target.id == 10782) {
                type!!.exit(e.asPlayer())
                return true
            }
        }
        return super.interact(e, target, option)
    }

    override fun teleport(
        e: Entity,
        type: Int,
        node: Node,
    ): Boolean {
        if (e is Player) {
            if (type != -1) {
                e.asPlayer().sendMessage("You can't teleport out of the training arena!")
                return false
            }
        }
        return super.teleport(e, type, node)
    }

    @Suppress("deprecation")
    override fun leave(
        entity: Entity,
        logout: Boolean,
    ): Boolean {
        if (entity is Player) {
            val player = entity.asPlayer()
            if (logout) {
                player.location = Location(3363, 3302, 0)
            } else {
                player.properties.spawnLocation = ServerConstants.HOME_LOCATION
            }
            cleanItems(player)
            closeOverlay(player)
        }
        return super.leave(entity, logout)
    }

    override fun configure() {}

    private fun cleanItems(player: Player) {
        items.forEach { item ->
            removeAll(player, item.id, Container.INVENTORY)
            removeAll(player, item.id, Container.EQUIPMENT)
        }
    }

    fun incrementPoints(
        player: Player,
        index: Int,
        amount: Int,
    ) {
        player.getSavedData().activityData.incrementPizazz(index, amount)
        update(player)
    }

    open fun update(player: Player?) {
        if (type == null) {
            return
        }
        type?.let {
            sendString(
                player!!,
                player
                    .getSavedData()
                    .activityData
                    .getPizazzPoints(it.ordinal)
                    .toString(),
                it.overlay.id,
                9,
            )
        }
    }
}
