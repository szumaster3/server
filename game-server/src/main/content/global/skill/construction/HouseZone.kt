package content.global.skill.construction

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.RegionManager.forId
import core.game.world.map.RegionManager.removeRegion
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneRestriction

class HouseZone(
    private val house: HouseManager,
) : MapZone("poh-zone$house", true, ZoneRestriction.RANDOM_EVENTS) {
    private var previousRegion = -1
    private var previousDungeon = -1
    private val restrictedItems = (7668..7737)

    override fun configure() {
        unregisterOldRegions()
        registerRegion(house.houseRegion.id)
        if (house.dungeonRegion != null) {
            registerRegion(house.dungeonRegion.id)
        }
    }

    private fun unregisterOldRegions() {
        if (previousRegion != -1) {
            unregisterRegion(previousRegion)
        }
        if (previousDungeon != -1) {
            unregisterRegion(previousDungeon)
        }
    }

    override fun enter(e: Entity): Boolean {
        if (e is Player) {
            val player = e.asPlayer()
            if (house == player.houseManager) {
                previousRegion = house.houseRegion.id
                if (house.dungeonRegion != null) previousDungeon = house.dungeonRegion.id
            }
            registerLogoutListener(e, "house-logout") { p: Player ->
                p.location = house.location.exitLocation
            }
        }
        return super.enter(e)
    }

    override fun death(
        e: Entity,
        killer: Entity,
    ): Boolean {
        if (e is Player) {
            val player = e.asPlayer()
            HouseManager.leave(player)
        }
        return true
    }

    override fun leave(
        e: Entity,
        logout: Boolean,
    ): Boolean {
        if (e is Player) {
            val player = e.asPlayer()
            if (house == player.houseManager) {
                val items = restrictedItems.toIntArray()
                if (anyInInventory(player, *items)) {
                    player.inventory.removeAll(items)
                }

                house.expelGuests(player)
                val toRemove = previousRegion
                val dungRemove = previousDungeon
                clearLogoutListener(player, "house-logout")
                submitWorldPulse(
                    object : Pulse(2) {
                        override fun pulse(): Boolean {
                            val r = forId(toRemove)
                            val dr = if (dungRemove != -1) forId(dungRemove) else null
                            removeRegion(toRemove)
                            unregisterRegion(toRemove)
                            r.isActive = false
                            if (dungRemove != -1) {
                                removeRegion(dungRemove)
                                unregisterRegion(dungRemove)
                                dr!!.isActive = false
                            }
                            return true
                        }
                    },
                )
            }
            return true
        }
        return true
    }
}
