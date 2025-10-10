package core.game.node.entity.player.link

import core.api.getVarbit
import core.api.setVarbit
import core.api.teleport
import core.game.node.entity.player.Player
import core.game.world.map.Location
import shared.consts.Components
import shared.consts.Vars

/**
 * Represents all configurable warnings.
 */
enum class Warnings(
    val varbit: Int,
    val component: Int,
    val buttonId: Int,
    val action: (Player) -> Unit
) {

    WILDERNESS_DITCH(
        Vars.VARBIT_WILDERNESS_WARNING_382_3857,
        Components.WILDERNESS_WARNING_382,
        67,
        { WarningActions::handleWildernessJump }
    ),

    CORPOREAL_BEAST_DANGEROUS(
        Vars.VARBIT_CORPOREAL_BEAST_DANGEROUS_5366,
        Components.CWS_WARNING_30_650,
        78,
        WarningActions::handleCorporalBeast
    ),

    BOUNTY_AREA(
        Vars.VARBIT_BOUNTY_WARNING_4199,
        Components.BOUNTY_WARNING_657,
        74,
        {}
    ),

    DEATH_PLATEAU(
        Vars.VARBIT_CWS_WARNING_24_3872,
        Components.CWS_WARNING_24_581,
        71,
        {}
    ),

    DUEL_ARENA(
        Vars.VARBIT_CWS_WARNING_26_4132,
        Components.CWS_WARNING_26_627,
        73,
        {}
    ),

    CLAN_WARS_FFA_SAFETY(
        Vars.VARBIT_CLAN_WARS_FFA_SAFETY_5294,
        -1,
        79,
        {}
    ),

    CLAN_WARS_FFA_DANGEROUS(
        Vars.VARBIT_CLAN_WARS_FFA_DANGEROUS_5295,
        Components.CWS_WARNING_8_576,
        80,
        {}
    ),

    PVP_WORLDS(
        Vars.VARBIT_PVP_WORLDS_5296,
        -1,
        81,
        {}
    ),

    SHANTAY_PASS(
        Vars.VARBIT_CWS_WARNING_10_3860,
        Components.CWS_WARNING_10_565,
        63,
        WarningActions::handleShantayPass
    ),

    FAIRY_RING_TO_DORGESH_KAAN(
        Vars.VARBIT_CWS_WARNING_15_3865,
        Components.CWS_WARNING_15_578,
        59,
        WarningActions::handleFairyRing
    ),

    MORT_MYRE(
        Vars.VARBIT_CWS_WARNING_20_3870,
        Components.CWS_WARNING_20_580,
        61,
        WarningActions::handleMortMyreGate
    ),

    DORGESH_KAAN_CITY_EXIT(
        Vars.VARBIT_CWS_WARNING_16_3869,
        Components.CWS_WARNING_16_569,
        68,
        {}
    ),

    RANGING_GUILD(
        Vars.VARBIT_CWS_WARNING_23_3871,
        Components.CWS_WARNING_23_564,
        70,
        WarningActions::handleRangingGuild
    ),

    DAGANNOTH_KINGS_LADDER(
        Vars.VARBIT_CWS_WARNING_1_3851,
        Components.CWS_WARNING_1_574,
        50,
        { teleport(it, Location.create(2899, 4449, 0)) }
    ),

    CONTACT_DUNGEON_LADDER(
        Vars.VARBIT_CWS_WARNING_2_3852,
        Components.CWS_WARNING_2_562,
        56,
        {}
    ),

    FALADOR_MOLE_LAIR(
        Vars.VARBIT_CWS_WARNING_3_3853,
        Components.CWS_WARNING_3_568,
        53,
        WarningActions::handleMoleTunnel
    ),

    STRONGHOLD_OF_SECURITY_LADDERS(
        Vars.VARBIT_CWS_WARNING_4_3854,
        Components.CWS_WARNING_4_579,
        52,
        WarningActions::handleStrongholdLadder
    ),

    WATCHTOWER_SHAMAN_CAVE(
        Vars.VARBIT_CWS_WARNING_12_3862,
        Components.CWS_WARNING_12_573,
        65,
        WarningActions::handleWatchtower
    ),

    LUMBRIDGE_SWAMP_CAVE_ROPE(
        Vars.VARBIT_CWS_WARNING_17_3863,
        Components.CWS_WARNING_17_570,
        51,
        WarningActions::handleSwampCave
    ),

    HAM_TUNNEL_FROM_MILL(
        Vars.VARBIT_CWS_WARNING_19_3864,
        Components.CWS_WARNING_19_571,
        58,
        {}
    ),

    ELID_GENIE_CAVE(
        Vars.VARBIT_CWS_WARNING_18_3867,
        Components.CWS_WARNING_18_577,
        64,
        {}
    ),

    DORGESH_KAAN_TUNNEL_TO_KALPHITES(
        Vars.VARBIT_CWS_WARNING_21_3868,
        Components.CWS_WARNING_21_561,
        69,
        {}
    ),

    TROLLHEIM_WILDERNESS_ENTRANCE(
        Vars.VARBIT_CWS_WARNING_13_3858,
        Components.CWS_WARNING_13_572,
        66,
        {}
    ),

    OBSERVATORY_STAIRS(
        Vars.VARBIT_CWS_WARNING_9_3859,
        Components.CWS_WARNING_9_560,
        62,
        { teleport(it, Location(2355, 9394, 0)) }
    ),

    LUMBRIDGE_CELLAR(
        Vars.VARBIT_CWS_WARNING_14_3866,
        Components.CWS_WARNING_14_567,
        60,
        {}
    ),

    PLAYER_OWNED_HOUSES(
        Vars.VARBIT_CWS_WARNING_5_3855,
        Components.CWS_WARNING_5_563,
        55,
        { it.houseManager.toggleBuildingMode(it, true) }
    ),

    DROPPED_ITEMS_IN_RANDOM_EVENTS(
        Vars.VARBIT_CWS_WARNING_6_3856,
        Components.CWS_WARNING_6_566,
        54,
        {}
    ),

    ICY_PATH_AREA(
        Vars.VARBIT_ICY_PATH_AREA_3861,
        -1,
        57,
        {}
    );

    companion object {
        val values = enumValues<Warnings>()
        val byVarbit = values.associateBy { it.varbit }
    }

    fun trigger(player: Player) {
        val disabled = getVarbit(player, varbit) == 1
        if (disabled) {
            action(player)
        } else {
            WarningManager.openWarningInterface(player, this)
        }
    }

    fun setDisabled(player: Player, disabled: Boolean) {
        setVarbit(player, varbit, if (disabled) 1 else 0)
    }
}
