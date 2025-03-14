package content.global.activity.ttrail;

import core.game.world.map.Location;
import core.game.world.map.zone.ZoneBorders;
import core.plugin.Plugin;

/**
 * The type Map clue plugin.
 */
// TODO NOT IMPLEMENTED CLUES.
//
// Maps with Crates
//  [ ] - Clock Tower – South of Ardougne's Castle.
//  [ ] - Dark Knight's Fortress – Level 14 Wilderness. Search a crate in the southwest corner of the main room.
//  [ ] - Observatory – Southwest of Ardougne. Search the crate in the westernmost building near the 3 goblins.
//  [ ] - Varrock Lumberyard – Northeast of Varrock. Search the indicated crate.
// Maps with X (No Crates) With Buildings
//  [ ] - Chaos Altar – North of the Observatory. Dig next to the standards behind the altar.
//  [ ] - Brother Galahad's House – West of Seers' Village, across the river from the coal trucks.
//  [ ] - Miscellania – East of the castle.
//  [ ] - Necromancer's Tower – North of Tower of Life, south of Ardougne, near the Necromancer's tower. Fairy ring code DJP is nearby.
//  [ ] - Ranging Guild – East of the Ranging Guild.
// Without Buildings
//  [ ] - North Falador Rocks – East of Falador's walls, near some stones.
//  [ ] - Varrock West Mine – South-west of Varrock, near the mining spot.
//  [ ] - North Falador Statue – Just north of Falador, near a statue at a crossroads.
//  [ ] - Road to Rellekka – Between Seers' Village and Rellekka, near two oak trees.
//  [ ] - Crafting Guild Peninsula – West of the Crafting Guild.
//  [ ] - Picnic Tables – South of Legends' Guild, east of Ardougne.
//  [ ] - Volcanoes – Level 50 Wilderness, southeast of the Agility Dungeon and west of the Mage Arena. Dig between the three volcanoes.
//  [ ] - Rellekka Bridge – On the road between Rellekka and the Lighthouse.
// Maps with Fish
//  [ ] - Draynor – South of Draynor Village bank, by the fishing spot.
//  [ ] - Mort'ton – North of the Haunted Mine, along the path to Mort'ton (03.15N 31.03E).
//
public final class MapCluePlugin extends MapClueScroll {

    /**
     * Instantiates a new Map clue plugin.
     */
    public MapCluePlugin() {
        this(null, -1, null, -1, null);
    }

    /**
     * Instantiates a new Map clue plugin.
     *
     * @param name        the name
     * @param clueId      the clue id
     * @param level       the level
     * @param interfaceId the interface id
     * @param location    the location
     */
    public MapCluePlugin(String name, int clueId, ClueLevel level, int interfaceId, Location location) {
        super(name, clueId, level, interfaceId, location);
    }

    /**
     * Instantiates a new Map clue plugin.
     *
     * @param name        the name
     * @param clueId      the clue id
     * @param level       the level
     * @param interfaceId the interface id
     * @param location    the location
     * @param object      the object
     * @param borders     the borders
     */
    public MapCluePlugin(String name, int clueId, ClueLevel level, int interfaceId, Location location, int object, ZoneBorders... borders) {
        super(name, clueId, level, interfaceId, location, object, borders);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        register(new MapCluePlugin("mc-grubber-crate", 2743, ClueLevel.MEDIUM, 355, Location.create(2658, 3488, 0), 357, new ZoneBorders(2657, 3487, 2660, 3489)));
        register(new MapCluePlugin("ardy-crate", 2747, ClueLevel.MEDIUM, 361, Location.create(2565, 3248, 0), 354, new ZoneBorders(2564, 3247, 2566, 3249)));
        register(new MapCluePlugin("wildy-crate", 2773, ClueLevel.HARD, 359, Location.create(3026, 3628, 0), 354, new ZoneBorders(3025, 3627, 3027, 3629)));
        register(new MapCluePlugin("goblin-crate", 2774, ClueLevel.HARD, 358, Location.create(2457, 3182, 0), 18506, new ZoneBorders(2456, 3181, 2458, 3183)));
        register(new MapCluePlugin("miscellania-dig", 2776, ClueLevel.MEDIUM, 340, Location.create(2536, 3865, 0)));
        register(new MapCluePlugin("crafting-guild-dig", 2778, ClueLevel.MEDIUM, 352, Location.create(2906, 3294, 0)));
        register(new MapCluePlugin("wizard-tower-dig", 2745, ClueLevel.EASY, 356, Location.create(3110, 3152, 0)));
        register(new MapCluePlugin("east-dig", 2805, ClueLevel.EASY, 360, Location.create(2651, 3231, 0)));
        register(new MapCluePlugin("rimmington", 2780, ClueLevel.MEDIUM, 362, Location.create(2924, 3209, 0)));
        register(new MapCluePlugin("west-ardy", 2782, ClueLevel.HARD, 357, Location.create(2488, 3308, 0)));
        register(new MapCluePlugin("yanille", 2783, ClueLevel.HARD, 353, Location.create(2616, 3077, 0)));
        register(new MapCluePlugin("mcgrubber", 2785, ClueLevel.EASY, 354, Location.create(2611, 3481, 0)));
        register(new MapCluePlugin("falador-stone", 2786, ClueLevel.EASY, 351, Location.create(3043, 3399, 0)));
        register(new MapCluePlugin("champion's-guild", 2788, ClueLevel.EASY, 346, Location.create(3166, 3360, 0)));
        register(new MapCluePlugin("falador-statue", 2790, ClueLevel.EASY, 337, Location.create(2970, 3415, 0)));
        register(new MapCluePlugin("varrock-mine", 2792, ClueLevel.EASY, 347, Location.create(3289, 3373, 0)));
        register(new MapCluePlugin("khazard-altar", 2793, ClueLevel.MEDIUM, 342, Location.create(2455, 3230, 0)));
        register(new MapCluePlugin("light-house", 2794, ClueLevel.MEDIUM, 343, Location.create(2578, 3597, 0)));
        register(new MapCluePlugin("north-seers", 2796, ClueLevel.MEDIUM, 344, Location.create(2666, 3562, 0)));
        register(new MapCluePlugin("legends-guild", 2797, ClueLevel.HARD, 339, Location.create(2723, 3338, 0)));
        register(new MapCluePlugin("wilderness", 2799, ClueLevel.HARD, 338, Location.create(3021, 3911, 0)));
        register(new MapCluePlugin("draynor", 2801, ClueLevel.MEDIUM, 348, Location.create(3092, 3227, 0)));
        register(new MapCluePlugin("abandoned-mine", 2803, ClueLevel.MEDIUM, 341, Location.create(3434, 3266, 0)));
        return this;
    }

}
