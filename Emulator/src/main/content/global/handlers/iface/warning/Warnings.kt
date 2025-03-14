package content.global.handlers.iface.warning

import org.rs.consts.Components

enum class Warnings(
    val varbit: Int,
    val component: Int,
    val buttonId: Int,
    var isDisabled: Boolean = false,
) {
    DAGANNOTH_KINGS_LADDER(3851, Components.CWS_WARNING_1_574, 50), // Complete
    LUMBRIDGE_SWAMP_CAVE_ROPE(3863, Components.CWS_WARNING_17_570, 51),
    STRONGHOLD_OF_SECURITY_LADDERS(3854, Components.CWS_WARNING_4_579, 52),
    FALADOR_MOLE_LAIR(3853, Components.CWS_WARNING_3_568, 53), // Complete
    DROPPED_ITEMS_IN_RANDOM_EVENTS(3856, Components.CWS_WARNING_6_566, 54),
    PLAYER_OWNED_HOUSES(3855, Components.CWS_WARNING_5_563, 55), // Complete
    CONTACT_DUNGEON_LADDER(3852, Components.CWS_WARNING_2_562, 56),
    ICY_PATH_AREA(3861, -1, 57), // https://runescape.wiki/w/Ice_Path
    HAM_TUNNEL_FROM_MILL(3864, Components.CWS_WARNING_19_571, 58),
    FAIRY_RING_TO_DORGESH_KAAN(3865, Components.CWS_WARNING_16_569, 59),
    LUMBRIDGE_CELLAR(3866, Components.CWS_WARNING_14_567, 60),
    MORT_MYRE(3870, Components.CWS_WARNING_20_580, 61),
    OBSERVATORY_STAIRS(3859, Components.CWS_WARNING_9_560, 62),
    SHANTAY_PASS(3860, Components.CWS_WARNING_10_565, 63),
    ELID_GENIE_CAVE(3867, Components.CWS_WARNING_18_577, 64),
    WATCHTOWER_SHAMAN_CAVE(3862, Components.CWS_WARNING_12_573, 65),
    TROLLHEIM_WILDERNESS_ENTRANCE(3858, Components.CWS_WARNING_13_572, 66),
    WILDERNESS_DITCH(3857, Components.WILDERNESS_WARNING_382, 67), // Complete
    DORGESH_KAAN_CITY_EXIT(3869, Components.CWS_WARNING_15_578, 68),
    DORGESH_KAAN_TUNNEL_TO_KALPHITES(3868, Components.CWS_WARNING_21_561, 69),
    RANGING_GUILD(3871, Components.CWS_WARNING_23_564, 70),// Complete
    DEATH_PLATEAU(3872, Components.CWS_WARNING_24_581, 71),
    // GOD_WARS(3946, Components.CWS_WARNING_25_600, 72), Wilderness component
    DUEL_ARENA(4132, Components.CWS_WARNING_26_627, 73),
    BOUNTY_AREA(4199, Components.BOUNTY_WARNING_657, 74),
    CHAOS_TUNNELS_EAST(4307, Components.CWS_WARNING_27_676, 75),
    CHAOS_TUNNELS_CENTRAL(4308, Components.CWS_WARNING_28_677, 76),
    CHAOS_TUNNELS_WEST(4309, Components.CWS_WARNING_29_678, 77),
    CORPOREAL_BEAST_DANGEROUS(5366, Components.CWS_WARNING_30_650, 78),
    CLAN_WARS_FFA_SAFETY(5294, -1, 79),
    CLAN_WARS_FFA_DANGEROUS(5295, Components.CWS_WARNING_8_576, 80),
    PVP_WORLDS(5296, -1, 81),
    ;

    companion object {
        @JvmStatic
        val values = enumValues<Warnings>()

        @JvmStatic
        val button = values.associateBy { it.varbit }
    }
}
