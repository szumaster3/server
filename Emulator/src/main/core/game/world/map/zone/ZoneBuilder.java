package core.game.world.map.zone;

import core.api.StartupListener;
import core.game.world.map.zone.impl.*;


/**
 * Loads all the default zones.
 *
 * @author Emperor
 */
public class ZoneBuilder implements StartupListener {

    @Override
    public void startup() {
        configure(WildernessZone.Companion.getInstance());
        configure(MultiwayCombatZone.Companion.getInstance());
        configure(new ModeratorZone());
        configure(new DarkZone());
        configure(new KaramjaZone());
        configure(new BankZone());
        configure(new TutorialZone());
        configure(new SnowZone().getZone());
    }

    /**
     * Configures the map zone.
     *
     * @param zone The map zone.
     */
    public static void configure(MapZone zone) {
        zone.setUid(zone.getName().hashCode());
        zone.configure();
    }
}