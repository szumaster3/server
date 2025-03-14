package core.game.world.map.zone;

import core.api.StartupListener;
import core.game.world.map.zone.impl.*;

/**
 * The type Zone builder.
 */
public class ZoneBuilder implements StartupListener {

    @Override
    public void startup() {
        // Configures various predefined zones.
        configure(WildernessZone.getInstance());
        configure(MultiwayCombatZone.Companion.getInstance());
        configure(new ModeratorZone());
        configure(new DarkZone());
        configure(new KaramjaZone());
        configure(new BankZone());
        configure(new SnowZone());
    }

    /**
     * Configure.
     *
     * @param zone the zone
     */
    public static void configure(MapZone zone) {
        // Assigns a unique identifier to the zone.
        zone.setUid(zone.getName().hashCode());
        // Configures additional parameters specific to the zone.
        zone.configure();
    }
}
