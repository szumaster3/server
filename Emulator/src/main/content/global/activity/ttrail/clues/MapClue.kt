package content.global.activity.ttrail.clues

import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.scrolls.MapClueScroll
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.plugin.Plugin
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Scenery

/**
 * Represents the emote clues.
 */

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
class MapClue : MapClueScroll {

    /**
     * Instantiates a new Map clue plugin.
     */
    @JvmOverloads
    constructor(name: String? = null, clueId: Int = -1, level: ClueLevel? = null, interfaceId: Int = -1, location: Location? = null) : super(name, clueId, level, interfaceId, location, 0)

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
    constructor(name: String, clueId: Int, level: ClueLevel, interfaceId: Int, location: Location?, `object`: Int, vararg borders: ZoneBorders?) : super(name, clueId, level, interfaceId, location, `object`, *borders.filterNotNull().toTypedArray())

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        register(MapClue("mc-grubber-crate", Items.CLUE_SCROLL_2743, ClueLevel.MEDIUM, Components.TRAIL_MAP19_355, Location.create(2658, 3488, 0), Scenery.CRATE_357, ZoneBorders(2657, 3487, 2660, 3489)))
        register(MapClue("ardy-crate", Items.CLUE_SCROLL_2747, ClueLevel.MEDIUM, Components.TRAIL_MAP25_361, Location.create(2565, 3248, 0), Scenery.CRATE_354, ZoneBorders(2564, 3247, 2566, 3249)))
        register(MapClue("wildy-crate", Items.CLUE_SCROLL_2773, ClueLevel.HARD, Components.TRAIL_MAP23_359, Location.create(3026, 3628, 0), Scenery.CRATE_354, ZoneBorders(3025, 3627, 3027, 3629)))
        register(MapClue("goblin-crate", Items.CLUE_SCROLL_2774, ClueLevel.HARD, Components.TRAIL_MAP22_358, Location.create(2457, 3182, 0), Scenery.CRATE_18506, ZoneBorders(2456, 3181, 2458, 3183)))
        register(MapClue("miscellania-dig", Items.CLUE_SCROLL_2776, ClueLevel.MEDIUM, Components.TRAIL_MAP04_340, Location.create(2536, 3865, 0)))
        register(MapClue("crafting-guild-dig", Items.CLUE_SCROLL_2778, ClueLevel.MEDIUM, Components.TRAIL_MAP16_352, Location.create(2906, 3294, 0)))
        register(MapClue("wizard-tower-dig", Items.CLUE_SCROLL_2745, ClueLevel.EASY, Components.TRAIL_MAP20_356, Location.create(3110, 3152, 0)))
        register(MapClue("east-dig", Items.CLUE_SCROLL_2805, ClueLevel.EASY, Components.TRAIL_MAP24_360, Location.create(2651, 3231, 0)))
        register(MapClue("rimmington", Items.CLUE_SCROLL_2780, ClueLevel.MEDIUM, Components.TRAIL_MAP26_362, Location.create(2924, 3209, 0)))
        register(MapClue("west-ardy", Items.CLUE_SCROLL_2782, ClueLevel.HARD, Components.TRAIL_MAP21_357, Location.create(2488, 3308, 0)))
        register(MapClue("yanille", Items.CLUE_SCROLL_2783, ClueLevel.HARD, Components.TRAIL_MAP17_353, Location.create(2616, 3077, 0)))
        register(MapClue("mcgrubber", Items.CLUE_SCROLL_2785, ClueLevel.EASY, Components.TRAIL_MAP18_354, Location.create(2611, 3481, 0)))
        register(MapClue("falador-stone", Items.CLUE_SCROLL_2786, ClueLevel.EASY, Components.TRAIL_MAP15_351, Location.create(3043, 3399, 0)))
        register(MapClue("champion's-guild", Items.CLUE_SCROLL_2788, ClueLevel.EASY, Components.TRAIL_MAP10_346, Location.create(3166, 3360, 0)))
        register(MapClue("falador-statue", Items.CLUE_SCROLL_2790, ClueLevel.EASY, Components.TRAIL_MAP01_337, Location.create(2970, 3415, 0)))
        register(MapClue("varrock-mine", Items.CLUE_SCROLL_2792, ClueLevel.EASY, Components.TRAIL_MAP11_347, Location.create(3289, 3373, 0)))
        register(MapClue("khazard-altar", Items.CLUE_SCROLL_2793, ClueLevel.MEDIUM, Components.TRAIL_MAP06_342, Location.create(2455, 3230, 0)))
        register(MapClue("light-house", Items.CLUE_SCROLL_2794, ClueLevel.MEDIUM, Components.TRAIL_MAP07_343, Location.create(2578, 3597, 0)))
        register(MapClue("north-seers", Items.CLUE_SCROLL_2796, ClueLevel.MEDIUM, Components.TRAIL_MAP08_344, Location.create(2666, 3562, 0)))
        register(MapClue("legends-guild", Items.CLUE_SCROLL_2797, ClueLevel.HARD, Components.TRAIL_MAP03_339, Location.create(2723, 3338, 0)))
        register(MapClue("wilderness", Items.CLUE_SCROLL_2799, ClueLevel.HARD, Components.TRAIL_MAP02_338, Location.create(3021, 3911, 0)))
        register(MapClue("draynor", Items.CLUE_SCROLL_2801, ClueLevel.MEDIUM, Components.TRAIL_MAP12_348, Location.create(3092, 3227, 0)))
        register(MapClue("abandoned-mine", Items.CLUE_SCROLL_2803, ClueLevel.MEDIUM, Components.TRAIL_MAP05_341, Location.create(3434, 3266, 0)))
        return this
    }
}
