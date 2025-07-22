package content.region.kandarin.seers.quest.mcannon.plugin;

import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.map.zone.ZoneRestriction;
import core.plugin.Plugin;

/**
 * The type Dmc zone.
 */
public class DMCZone extends MapZone implements Plugin<Object> {

    /**
     * Instantiates a new Dmc zone.
     */
    public DMCZone() {
        super("DMC Zone", true, ZoneRestriction.CANNON);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ZoneBuilder.configure(this);
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public void configure() {

        registerRegion(11929);
        registerRegion(12185);
        registerRegion(12184);

        register(new ZoneBorders(2838, 3536, 2875, 3555));

        register(new ZoneBorders(3456, 9472, 3519, 9535, 0, true));

        register(new ZoneBorders(2995, 3465, 3022, 3509));

        register(new ZoneBorders(2690, 9934, 2831, 10050));

        registerRegion(11316);

        registerRegion(12086);

        registerRegion(9033);
    }

}
