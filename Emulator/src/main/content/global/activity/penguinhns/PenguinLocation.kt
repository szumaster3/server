package content.global.activity.penguinhns

import core.game.world.map.Location
import org.rs.consts.NPCs

/**
 * Represents all possible penguins for the Penguin Hunter activity.
 */
enum class PenguinLocation(
    val npcId: Int,
    val hint: String,
    vararg val locations: Location
) {
    CACTUS_NARDAH(NPCs.CACTUS_8107, "located in the north of the Kharidian desert.",
        Location.create(3406, 2997), Location.create(3259, 3052)),

    CACTUS_KHARDIAN_DESERT(NPCs.CACTUS_8107, "located in the south of the Kharidian desert.",
        Location.create(3310, 3157), Location.create(3350, 3311), Location.create(3279, 2906)),

    CACTUS_SOPHANEM(NPCs.CACTUS_8107, "located in a city of plagues within the desert.",
        Location.create(3275, 2798)),

    BUSH_FREMENNIK(NPCs.BUSH_8105, "located between Fremennik and barbarians.",
        Location.create(2532, 3588)),

    BUSH_BANANA(NPCs.BUSH_8105, "located where banana smugglers dwell.",
        Location.create(2740, 3233)),

    BUSH_NORTH_ARDOUGNE(NPCs.BUSH_8105, "located north of Ardougne.",
        Location.create(2399, 3362)),

    BUSH_SOUTH_ARDOUGNE(NPCs.BUSH_8105, "located south of Ardougne.",
        Location.create(2456, 3092), Location.create(2513, 3154), Location.create(2440, 3206), Location.create(2482, 3126)),

    BUSH_JUNGLE(NPCs.BUSH_8105, "located deep in the jungle.",
        Location.create(2832, 3053), Location.create(2938, 2978)),

    BUSH_EAGLES(NPCs.BUSH_8105, "located where eagles fly.",
        Location.create(2326, 3516)),

    BUSH_TREE_GNOMES(NPCs.BUSH_8105, "located near a big tree surrounded by short people.",
        Location.create(2387, 3451)),

    BUSH_ASGARNIA(NPCs.BUSH_8105, "located in the kingdom of Asgarnia.",
        Location.create(2951, 3511)),

    BUSH_KANDARIN(NPCs.BUSH_8105, "located somewhere in the kingdom of Kandarin.",
        Location.create(2633, 3501)),

    BUSH_WIZARDS(NPCs.BUSH_8105, "located where wizards study.",
        Location.create(3112, 3149)),

    BUSH_BLOODSUCKERS(NPCs.BUSH_8105, "located where bloodsuckers rule.",
        Location.create(3457, 3387)),

    BUSH_MONKEYS(NPCs.BUSH_8105, "located where monkeys rule.",
        Location.create(2809, 2775)),

    BUSH_ETC(NPCs.BUSH_8105, "located on an island, etc.",
        Location.create(2533, 3868)),

    BUSH_FELDIP(NPCs.BUSH_8105, "located around Feldip Hills, near Gu'Tanoth.",
        Location.create(2606, 3005), Location.create(2438, 3045), Location.create(2340, 3064)),

    BUSH_BROTHERS(NPCs.BUSH_8105, "located on islands where brothers quarrel.",
        Location.create(2356, 3848), Location.create(2352, 3834)),

    BUSH_WEREWOLVES(NPCs.BUSH_8105, "located near a town of werewolves.",
        Location.create(3600, 3485)),

    CRATE_PISCATORIS(NPCs.CRATE_8108, "located where fishers colonise.",
        Location.create(2322, 3658)),

    CRATE_MUSA_POINT(NPCs.CRATE_8108, "located where banana smugglers dwell.",
        Location.create(2870, 3158)),

    ROCK_GUARD(NPCs.ROCK_8109, "located where the Imperial Guard train.",
        Location.create(2852, 3578)),

    ROCK_MISTHALIN(NPCs.ROCK_8109, "located in the kingdom of Misthalin.",
        Location.create(3356, 3416)),

    ROCK_OGRES(NPCs.ROCK_8109, "located near some ogres.",
        Location.create(2631, 2980)),

    ROCK_ASGARNIA(NPCs.ROCK_8109, "located in the Kingdom of Asgarnia.",
        Location.create(3013, 3501)),

    ROCK_FREMENNIK(NPCs.ROCK_8109, "located between Fremennik and barbarians.",
        Location.create(2532, 3630), Location.create(2670, 3718)),

    ROCK_BROTHERS(NPCs.ROCK_8109, "located on islands where brothers quarrel.",
        Location.create(2312, 3814), Location.create(2413, 3846), Location.create(2357, 3797)),

    ROCK_CRESCENT(NPCs.ROCK_8109, "located on a large crescent island.",
        Location.create(2152, 3934)),

    ROCK_COAST(NPCs.ROCK_8109, "located near the coast, east of Ardougne.",
        Location.create(2733, 3282)),

    ROCK_BLOODSUCKERS(NPCs.ROCK_8109, "located near the coast, east of Ardougne.",
        Location.create(3544, 3437)),

    ROCK_WILDERNESS(NPCs.ROCK_8109, "located in the Wilderness.",
        Location.create(2990, 3828), Location.create(3019, 3866), Location.create(3108, 3838), Location.create(3236, 3929)),

    ROCK_KELDAGRIM(NPCs.ROCK_8109, "located where dwarves dig deep.",
        Location.create(2909, 10210)),

    ROCK_ELVES(NPCs.ROCK_8109, "located near the pointy-eared ones.",
        Location.create(2296, 3271)),

    CRATE_MISTHALIN(NPCs.CRATE_8108, "located in the kingdom of Misthalin.",
        Location.create(3112, 3332), Location.create(3305, 3508)),

    CRATE_WEREWOLVES(NPCs.CRATE_8108, "located near a town of werewolves.",
        Location.create(3637, 3486)),

    BARREL_DORGESH_KAAN(NPCs.BARREL_8104, "located where the big-eyed goblins dwell.",
        Location.create(2732, 5325)),

    BARREL_ENTRANA(NPCs.BARREL_8104, "located where no weapons may go.",
        Location.create(2806, 3383)),

    BARREL_ARDOUGNE(NPCs.BARREL_8104, "located south of Ardougne.",
        Location.create(2662, 3152)),

    BARREL_PORT_PHASMATYS(NPCs.BARREL_8104, "located near the city of ghosts.",
        Location.create(3654, 3491)),

    BARREL_HARMLESS(NPCs.BARREL_8104, "located where pirates feel mostly harmless.",
        Location.create(3738, 2998)),

    BARREL_APE_ATOLL(NPCs.BARREL_8104, "located where monkeys rule.",
        Location.create(2752, 2699)),

    TOADSTOOL_MISTHALIN(NPCs.TOADSTOOL_8110, "located in the kingdom of Misthalin.",
        Location.create(3156, 3178)),

    TOADSTOOL_FAIRY(NPCs.TOADSTOOL_8110, "located in the fairy realm.",
        Location.create(2409, 4462)),

    TOADSTOOL_BLOODSUCKERS(NPCs.TOADSTOOL_8110, "located where bloodsuckers rule.",
        Location.create(3416, 3437)),

    TOADSTOOL_ELVES(NPCs.TOADSTOOL_8110, "located near the pointy-eared ones.",
        Location.create(2311, 3177), Location.create(2219, 3227), Location.create(2184, 3172)),

    ;

    companion object {
        private val locationMap: Map<Location, PenguinLocation> = buildMap {
            for (penguin in values()) {
                for (location in penguin.locations) {
                    put(location, penguin)
                }
            }
        }

        fun forLocation(location: Location): PenguinLocation? = locationMap[location]
    }
}
