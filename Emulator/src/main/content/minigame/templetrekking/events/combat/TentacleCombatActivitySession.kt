package content.minigame.templetrekking.events.combat

import content.minigame.templetrekking.TempleTrekking
import core.api.MapArea
import core.api.getAttribute
import core.api.getRegionBorders
import core.api.registerLogoutListener
import core.api.setAttribute
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.build.DynamicRegion
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction

class TentacleCombatActivitySession(
    val activity: TentacleCombatActivity? = null,
) : MapArea {
    constructor(region: DynamicRegion, activity: TentacleCombatActivity) : this(activity) {
        this.region = region
        this.base = region.baseLocation
    }

    lateinit var region: DynamicRegion
    lateinit var base: Location
    var isActive = false
    var inactiveTicks = 0
    var player: ArrayList<Player> = ArrayList()

    fun start(player: Player) {
        isActive = true
        setAttribute(player, TempleTrekking.templeTrekkingCombatSession, this)
        registerLogoutListener(player, TempleTrekking.logout) {
            val session =
                player.getAttribute<TentacleCombatActivitySession?>(TempleTrekking.templeTrekkingCombatSession, null)
                    ?: return@registerLogoutListener
            player.properties.teleportLocation = getAttribute(player, TempleTrekking.baseLocation, null)
            session.player.remove(player)
        }
        zone.register(getRegionBorders(region.id))
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(getRegionBorders(TempleTrekking.tentacleCombatEventRegion))
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.CANNON, ZoneRestriction.FIRES, ZoneRestriction.RANDOM_EVENTS)
    }
}
