package content.minigame.mage_training.plugin

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
import shared.consts.Vars

open class MTAZone(name: String?, val items: Array<Item>) : MapZone(name.toString(), false, ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.FOLLOWERS) {

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
            type?.let {
                player.interfaceManager.openOverlay(it.overlay)
                update(player)
            }
            if (player.properties.spawnLocation == null) {
                player.properties.spawnLocation = Location(3363, 3302, 0)
            }
        }
        return super.enter(entity)
    }

    override fun canRequest(type: RequestType, player: Player, target: Player): Boolean {
        sendMessage(player, "You can't do that right now.")
        return false
    }

    override fun interact(e: Entity, target: Node, option: Option): Boolean {
        if (e is Player) {
            if (target.id == 10782) {
                type?.exit(e.asPlayer())
                return true
            }
        }
        return super.interact(e, target, option)
    }

    override fun teleport(e: Entity, type: Int, node: Node?): Boolean {
        if (e is Player) {
            if (type != -1) {
                e.asPlayer().sendMessage("You can't teleport out of the training arena!")
                return false
            }
        }
        return super.teleport(e, type, node)
    }

    override fun leave(entity: Entity, logout: Boolean): Boolean {
        if (entity is Player) {
            val player = entity.asPlayer() ?: return super.leave(entity, logout)
            if (logout) {
                player.location = Location(3363, 3302, 0)
            } else {
                player.properties.spawnLocation = player.getRespawnLocation()
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

    /**
     * Increment player points stored via varbits for the current MTA type.
     */
    fun incrementPoints(player: Player, index: Int, amount: Int) {
        Companion.incrementPoints(player, index, amount)
        Companion.updatePoints(player)
    }

    /**
     * Update points display for the player based on varbits.
     */
    fun updatePoints(player: Player) {
        Companion.updatePoints(player)
    }

    /**
     * Get points from varbit for player.
     */
    fun getPoints(player: Player, index: Int): Int {
        return Companion.getPoints(player, index)
    }

    /**
     * Update the overlay with current points.
     */
    open fun update(player: Player?) {
        val currentType = type ?: return
        player ?: return

        val points = getPoints(player, currentType.ordinal)
        sendString(player, points.toString(), currentType.overlay.id, 9)
    }

    companion object {
        /**
         * Map indexes of pizazz points to varbit ids.
         */
        val pizazzVarbitIds = intArrayOf(
            Vars.VARBIT_MTA_TELEKINETIC_POINTS_1485,
            Vars.VARBIT_MTA_ALCHEMIST_POINTS_1489,
            Vars.VARBIT_MTA_ENCHANTMENT_POINTS_1488,
            Vars.VARBIT_MTA_GRAVEYARD_POINTS_1486
        )

        fun incrementPoints(player: Player, index: Int, amount: Int) {
            val varbitId = pizazzVarbitIds[index]
            val current = getVarbit(player, varbitId)
            setVarbit(player, varbitId, current + amount)
        }

        fun decrementPoints(player: Player, index: Int, amount: Int) {
            val varbitId = pizazzVarbitIds[index]
            val current = getVarbit(player, varbitId)
            val newValue = (current - amount).coerceAtLeast(0)
            setVarbit(player, varbitId, newValue)
        }

        fun updatePoints(player: Player) {
            for ((i, varbitId) in pizazzVarbitIds.withIndex()) {
                setVarbit(player, varbitId, getPoints(player, i), true)
            }
        }

        fun getPoints(player: Player, index: Int): Int {
            val varbitId = pizazzVarbitIds[index]
            return getVarbit(player, varbitId)
        }

        fun getTotalPoints(player: Player): Int {
            return pizazzVarbitIds.indices.sumOf { getPoints(player, it) }
        }
    }
}
