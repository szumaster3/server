package content.minigame.allfiredup.plugin

import core.api.getVarbit
import core.api.log
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.tools.Log
import org.rs.consts.Vars

/**
 * Represents a beacon in the All Fired Up minigame.
 */
enum class AFUBeacon(val title: String, val fmLevel: Int, val varbit: Int, val location: Location, val experience: Double, val keeper: Int = 0) {
    RIVER_SALVE("", 43, Vars.VARBIT_BEACON_RIVER_SALVE_5146, Location.create(3396, 3464, 0), 216.2, 8065),
    RAG_AND_BONE("", 43, Vars.VARBIT_BEACON_RAG_AND_BONE_5147, Location.create(3343, 3510, 0), 235.8, 8066),
    JOLLY_BOAR("", 48, Vars.VARBIT_BEACON_JOLLY_BOAR_5148, Location.create(3278, 3525, 0), 193.8, 8067),
    NORTH_VARROCK_CASTLE("", 53, Vars.VARBIT_BEACON_NORTH_VARROCK_CASTLE_5149, Location.create(3236, 3527, 0), 178.5, 8068),
    GRAND_EXCHANGE("", 59, Vars.VARBIT_BEACON_GRAND_EXCHANGE_5150, Location.create(3170, 3536, 0), 194.3, 8069),
    EDGEVILLE("", 62, Vars.VARBIT_BEACON_EDGEVILLE_5151, Location.create(3087, 3516, 0), 86.7, 8070),
    MONASTERY("", 68, Vars.VARBIT_BEACON_MONASTERY_5152, Location.create(3034, 3518, 0), 224.4, 8071),
    GOBLIN_VILLAGE("", 72, Vars.VARBIT_BEACON_GOBLIN_VILLAGE_5153, Location.create(2968, 3516, 0), 194.8, 8072),
    BURTHORPE("", 76, Vars.VARBIT_BEACON_BURTHORPE_5154, Location.create(2940, 3565, 0), 195.3, 8073),
    DEATH_PLATEAU("", 79, Vars.VARBIT_BEACON_DEATH_PLATEAU_5155, Location.create(2944, 3622, 0), 249.9, 8074),
    TROLLHEIM("", 83, Vars.VARBIT_BEACON_TROLLHEIM_5156, Location.create(2939, 3680, 0), 201.0, 8075),
    GWD("", 87, Vars.VARBIT_BEACON_GWD_5157, Location.create(2937, 3773, 0), 255.0, 8076),
    TEMPLE("", 89, Vars.VARBIT_BEACON_TEMPLE_5158, Location.create(2946, 3836, 0), 198.9),
    PLATEAU("", 92, Vars.VARBIT_BEACON_PLATEAU_5159, Location.create(2964, 3931, 0), 147.9),
    ;

    companion object {
        fun forLocation(location: Location): AFUBeacon {
            for (beacon in values()) {
                if (beacon.location == location) return beacon
            }
            return RIVER_SALVE.also {
                log(this::class.java, Log.WARN, "Unhandled Beacon Location [$location].")
            }
        }

        fun resetAllBeacons(player: Player) {
            for (beacon in values()) {
                setVarbit(player, beacon.varbit, 0)
            }
        }
    }

    fun light(player: Player) {
        setVarbit(player, varbit, 2)
    }

    fun diminish(player: Player) {
        setVarbit(player, varbit, 3)
    }

    fun extinguish(player: Player) {
        setVarbit(player, varbit, 0)
    }

    fun lightGnomish(player: Player) {
        setVarbit(player, varbit, 4)
    }

    fun fillWithLogs(player: Player) {
        setVarbit(player, varbit, 1, true)
    }

    fun getState(player: Player): BeaconState = BeaconState.values()[getVarbit(player, varbit)]

}
