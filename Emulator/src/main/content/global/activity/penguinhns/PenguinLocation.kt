package content.global.activity.penguinhns

import core.game.world.map.Location
import org.rs.consts.NPCs

/**
 * Represents all possible penguins for the Penguin Hunter activity.
 */
enum class PenguinLocation(val id: Int, val hint: String, val location: List<Location>) {

    /*
     * Cactus's.
     */
    CACTUS_NARDAH(
        NPCs.CACTUS_8107, "located in the north of the Kharidian desert.", listOf(
        Location.create(3406, 2997, 0),
        Location.create(3259, 3052, 0),
    )),
    CACTUS_KHARDIAN_DESERT(
        NPCs.CACTUS_8107, "located in the south of the Kharidian desert.", listOf(
        Location.create(3310, 3157, 0),
        Location.create(3350, 3311, 0),
        Location.create(3279, 2906, 0)
    )),
    CACTUS_SOPHANEM(
        NPCs.CACTUS_8107, "located in a city of plagues within the desert.", listOf(
        Location.create(3275, 2798, 0)
    )),

    /*
     * Bushes.
     */
    BUSH_FREMENNIK(
        NPCs.BUSH_8105, "located between Fremennik and barbarians.", listOf(
        Location.create(2532, 3588, 0)
    )),
    BUSH_BANANA(
        NPCs.BUSH_8105, "located where banana smugglers dwell.", listOf(
        Location.create(2740, 3233, 0)
    )),
    BUSH_NORTH_ARDOUGNE(
        NPCs.BUSH_8105, "located north of Ardougne.", listOf(
        Location.create(2399, 3362, 0)
    )),
    BUSH_SOUTH_ARDOUGNE(
        NPCs.BUSH_8105, "located south of Ardougne.", listOf(
        Location.create(2456, 3092, 0),
        Location.create(2513, 3154, 0),
        Location.create(2440, 3206, 0),
        Location.create(2482, 3126, 0)
    )),
    BUSH_JUNGLE(
        NPCs.BUSH_8105, "located deep in the jungle.", listOf(
        Location.create(2832, 3053, 0),
        Location.create(2938, 2978, 0)
    )),
    BUSH_EAGLES(
        NPCs.BUSH_8105, "located where eagles fly.", listOf(
        Location.create(2326, 3516, 0)
    )),
    BUSH_TREE_GNOMES(
        NPCs.BUSH_8105, "located near a big tree surrounded by short people.", listOf(
        Location.create(2387, 3451, 0)
    )),
    BUSH_ASGARNIA(
        NPCs.BUSH_8105, "located in the kingdom of Asgarnia.", listOf(
        Location.create(2951, 3511, 0)
    )),
    BUSH_KANDARIN(
        NPCs.BUSH_8105, "located somewhere in the kingdom of Kandarin.", listOf(
        Location.create(2633, 3501, 0)
    )),
    BUSH_WIZARDS(
        NPCs.BUSH_8105, "located where wizards study.", listOf(
        Location.create(3112, 3149, 0)
    )),
    BUSH_BLOODSUCKERS(
        NPCs.BUSH_8105, "located where bloodsuckers rule.", listOf(
        Location.create(3457, 3387, 0)
    )),
    BUSH_MONKEYS(
        NPCs.BUSH_8105, "located where monkeys rule.", listOf(
        Location.create(2809, 2775, 0)
    )),
    BUSH_ETC(
        NPCs.BUSH_8105, "located on an island, etc.", listOf(
        Location.create(2533, 3868, 0)
    )),
    BUSH_FELDIP(
        NPCs.BUSH_8105, "located around Feldip Hills, near Gu'Tanoth.", listOf(
        Location.create(2606, 3005, 0),
        Location.create(2438, 3045, 0),
        Location.create(2340, 3064, 0)
    )),
    BUSH_BROTHERS(
        NPCs.BUSH_8105, "located on islands where brothers quarrel.", listOf(
        Location.create(2356, 3848, 0),
        Location.create(2352, 3834, 0)
    )),
    BUSH_WEREWOLVES(
        NPCs.BUSH_8105, "located near a town of werewolves.", listOf(
        Location.create(3600, 3485, 0)
    )),

    /*
     * Crates.
     */
    CRATE_PISCATORIS(
        NPCs.CRATE_8108, "located where fishers colonise.", listOf(
        Location.create(2322, 3658, 0)
    )),
    CRATE_MUSA_POINT(
        NPCs.CRATE_8108, "located where banana smugglers dwell.", listOf(
        Location.create(2870, 3158, 0)
    )),
    // CRATE_DRAGONTOOTH(NPCs.CRATE_8108, "located near the island of Dragontooth.", listOf(
    //     Location.create(3824, 3562, 0)
    // )),

    /*
     * Rocks.
     */
    ROCK_GUARD(
        NPCs.ROCK_8109, "located where the Imperial Guard train.", listOf(
        Location.create(2852, 3578, 0)
    )),
    ROCK_MISTHALIN(
        NPCs.ROCK_8109, "located in the kingdom of Misthalin.", listOf(
        Location.create(3356, 3416, 0)
    )),
    ROCK_OGRES(
        NPCs.ROCK_8109, "located near some ogres.", listOf(
        Location.create(2631, 2980, 0)
    )),
    ROCK_ASGARNIA(
        NPCs.ROCK_8109, "located in the Kingdom of Asgarnia.", listOf(
        Location.create(3013, 3501, 0)
    )),
    ROCK_FREMENNIK(
        NPCs.ROCK_8109, "located between Fremennik and barbarians.", listOf(
        Location.create(2532, 3630, 0),
        Location.create(2670, 3718, 0)
    )),
    ROCK_BROTHERS(
        NPCs.ROCK_8109, "located on islands where brothers quarrel.", listOf(
        Location.create(2312, 3814, 0),
        Location.create(2413, 3846, 0),
        Location.create(2357, 3797, 0)
    )),
    ROCK_CRESCENT(
        NPCs.ROCK_8109, "located on a large crescent island.", listOf(
        Location.create(2152, 3934, 0)
    )),
    ROCK_COAST(
        NPCs.ROCK_8109, "located near the coast, east of Ardougne.", listOf(
        Location.create(2733, 3282, 0)
    )),
    ROCK_BLOODSUCKERS(
        NPCs.ROCK_8109, "located near the coast, east of Ardougne.", listOf(
        Location.create(3544, 3437, 0)
    )),
    ROCK_WILDERNESS(
        NPCs.ROCK_8109, "located in the Wilderness.", listOf(
        Location.create(2990, 3828, 0),
        Location.create(3019, 3866, 0),
        Location.create(3108, 3838, 0),
        Location.create(3236, 3929, 0)
    )),
    ROCK_KELDAGRIM(
        NPCs.ROCK_8109, "located where dwarves dig deep.", listOf(
        Location.create(2909, 10210, 0)
    )),
    ROCK_ELVES(
        NPCs.ROCK_8109, "located near the pointy-eared ones.", listOf(
        Location.create(2296, 3271, 0)
    )),
    CRATE_MISTHALIN(
        NPCs.CRATE_8108, "located in the kingdom of Misthalin.", listOf(
        Location.create(3112, 3332, 0),
        Location.create(3305, 3508, 0)
    )),
    CRATE_WEREWOLVES(
        NPCs.CRATE_8108, "located near a town of werewolves.", listOf(
        Location.create(3637, 3486, 0)
    )),

    /*
     * Barrels.
     */
    BARREL_DORGESH_KAAN(
        NPCs.BARREL_8104, "located where the big-eyed goblins dwell.",listOf(
        Location.create(2732, 5325, 0)
    )),
    BARREL_ENTRANA(
        NPCs.BARREL_8104, "located where no weapons may go.", listOf(
        Location.create(2806, 3383, 0)
    )),
    BARREL_ARDOUGNE(
        NPCs.BARREL_8104, "located south of Ardougne.", listOf(
        Location.create(2662, 3152, 0)
    )),
    BARREL_PORT_PHASMATYS(
        NPCs.BARREL_8104, "located near the city of ghosts.", listOf(
        Location.create(3654, 3491, 0)
    )),
    BARREL_HARMLESS(
        NPCs.BARREL_8104, "located where pirates feel mostly harmless.", listOf(
        Location.create(3738, 2998, 0)
    )),
    BARREL_APE_ATOLL(
        NPCs.BARREL_8104, "located where monkeys rule.", listOf(
        Location.create(2752, 2699, 0)
    )),

    /*
     * Toadstools.
     */
    TOADSTOOL_MISTHALIN(
        NPCs.TOADSTOOL_8110, "located in the kingdom of Misthalin.", listOf(
        Location.create(3156, 3178, 0)
    )),
    TOADSTOOL_FAIRY(
        NPCs.TOADSTOOL_8110, "located in the fairy realm.", listOf(
        Location.create(2409, 4462, 0)
    )),
    TOADSTOOL_BLOODSUCKERS(
        NPCs.TOADSTOOL_8110, "located where bloodsuckers rule.", listOf(
        Location.create(3416, 3437, 0)
    )),
    TOADSTOOL_ELVES(
        NPCs.TOADSTOOL_8110, "located near the pointy-eared ones.", listOf(
        Location.create(2311, 3177, 0),
        Location.create(2219, 3227, 0),
        Location.create(2184, 3172, 0)
    ));

    companion object {
        private val locationMap = values()
            .flatMap { penguin -> penguin.location.map { it to penguin } }
            .toMap()

        fun forLocation(location: Location): PenguinLocation? = locationMap[location]
    }
}
