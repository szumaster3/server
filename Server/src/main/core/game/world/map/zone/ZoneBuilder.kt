package core.game.world.map.zone

import core.api.StartupListener
import core.game.world.map.zone.impl.*

/**
 * Loads all the default zones.
 *
 * @author Emperor
 */
class ZoneBuilder : StartupListener {
    override fun startup() {
        configure(WildernessZone.instance)
        configure(MultiwayCombatZone.instance)
        configure(ModeratorZone())
        configure(DarkZone())
        configure(KaramjaZone())
        configure(BankZone())
        configure(TutorialZone())
        configure(SnowZone().zone)
    }

    companion object {
        /**
         * Configures the map zone.
         *
         * @param zone The map zone.
         */
        @JvmStatic
        fun configure(zone: MapZone) {
            zone.setUid(zone.name.hashCode())
            zone.configure()
        }
    }
}