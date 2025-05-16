package content.global.activity.ttrail.cryptic;

import content.global.activity.ttrail.ClueLevel;
import core.game.world.map.Location;
import core.game.world.map.zone.ZoneBorders;
import core.plugin.Plugin;
import org.rs.consts.Items;
import org.rs.consts.Scenery;

/**
 * The Cryptic clue plugin.
 */

public final class CrypticCluePlugin extends CrypticClueScroll {

    public CrypticCluePlugin() {
        this(null, -1, null, null, null);
    }

    public CrypticCluePlugin(String name, int clueId, ClueLevel level, String clueText, Location location) {
        super(name, clueId, level, clueText, location);
    }

    public CrypticCluePlugin(String name, int clueId, ClueLevel level, String clueText, Location location, int object, ZoneBorders... borders) {
        super(name, clueId, level, clueText, location, object, borders);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        register(new CrypticCluePlugin("etceteria-evergreen", Items.CLUE_SCROLL_10190, ClueLevel.HARD, "And so on, and so on, and so on. Walking from the land of many unimportant things leads to a choice of paths.", Location.create(2591, 3879, 0)));
        register(new CrypticCluePlugin("elemental-workshop-crates", Items.CLUE_SCROLL_10192, ClueLevel.HARD, "You have all of the elements available to solve this clue. Fortunately you do not have to go so far as to stand in a draft.", Location.create(2723, 9891, 0), Scenery.CRATE_18506));
        return this;
    }
}