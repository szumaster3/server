package core.game.world.map.zone.impl

import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction

/**
 * Represents a bank zone.
 *
 * @author Vexia
 */
class BankZone : MapZone(BANK_ZONES, true, ZoneRestriction.CANNON, ZoneRestriction.FIRES) {

    override fun configure() {
        register(VARROCK_WEST_BANK)
        register(VARROCK_EAST_BANK)
        register(FALADOR_EAST_BANK)
        register(FALADOR_WEST_BANK_0)
        register(FALADOR_WEST_BANK_1)
        register(DRAYNOR_VILLAGE_BANK)
        register(LUMBRIDGE_BANK)
        register(AL_KHARID_BANK)
        register(NARDAH_BANK)
        register(SOPHANEM_BANK)
        register(SHILO_VILLAGE_BANK_0)
        register(SHILO_VILLAGE_BANK_1)
        register(SHILO_VILLAGE_BANK_2)
        register(SHILO_VILLAGE_BANK_3)
        register(OOGLOG_BANK)
        register(CASTLE_WARS_BANK_CHEST)
        register(LLETYA_BANK)
        register(YANILLE_BANK)
        register(ARDOUGNE_EAST_NORTH_BANK)
        register(ARDOUGNE_EAST_SOUTH_BANK)
        register(FISHING_GUILD_BANK)
        register(GNOME_STRONGHOLD_BANK_0)
        register(PISCATORIS_BANK)
        register(NEITIZNOT_BANK)
        register(JATIZSO_BANK)
        register(ECTECERIA_BANK)
        register(CANIFIS_BANK)
        register(PORT_PHASMATYS_BANK)
        register(EDGEVILLE_BANK)
        register(BURGH_DE_ROTT_BANK)
        register(SEERS_VILLAGE_BANK_0)
        register(SEERS_VILLAGE_BANK_1)
        register(VOID_KNIGHTS_OUTPOST_BANK)
        register(MOS_LE_HARMLESS_BANK)
        register(KELDAGRIM_BANK)
        register(WARRIORS_GUILD_BANK)
        register(COOKS_GUILD_BANK)
        register(TUTORIAL_ISLAND_BANK_0)
        register(TUTORIAL_ISLAND_BANK_1)
        register(TUTORIAL_ISLAND_BANK_2)
        register(CATHERBY_BANK)
        register(GNOME_STRONGHOLD_BANK_1)
        register(GE_SQUARE_AREA_BANK)
    }

    companion object {
        @JvmStatic
        val instance = BankZone()
        val BANK_ZONES = "bank"

        val VARROCK_WEST_BANK = ZoneBorders(3179, 3432, 3194, 3446)
        val VARROCK_EAST_BANK = ZoneBorders(3250, 3416, 3257, 3423)
        val FALADOR_EAST_BANK = ZoneBorders(3009, 3353, 3018, 3358)
        val FALADOR_WEST_BANK_0 = ZoneBorders(2943, 3370, 2947, 3373)
        val FALADOR_WEST_BANK_1 = ZoneBorders(2943, 3368, 2949, 3369)
        val DRAYNOR_VILLAGE_BANK = ZoneBorders(3088, 3240, 3097, 3246)
        val LUMBRIDGE_BANK = ZoneBorders(3207, 3215, 3210, 3222, 2, true)
        val AL_KHARID_BANK = ZoneBorders(3269, 3161, 3272, 3173)
        val NARDAH_BANK = ZoneBorders(3427, 2889, 3430, 2894)
        val SOPHANEM_BANK = ZoneBorders(2794, 5159, 2805, 5172)
        val SHILO_VILLAGE_BANK_0 = ZoneBorders(2843, 2952, 2846, 2956)
        val SHILO_VILLAGE_BANK_1 = ZoneBorders(2850, 2951, 2854, 2957)
        val SHILO_VILLAGE_BANK_2 = ZoneBorders(2843, 2953, 2861, 2955)
        val SHILO_VILLAGE_BANK_3 = ZoneBorders(2858, 2952, 2861, 2956)
        val OOGLOG_BANK = ZoneBorders(2555, 2836, 2559, 2841)
        val CASTLE_WARS_BANK_CHEST = ZoneBorders(2437, 3081, 2447, 3098)
        val LLETYA_BANK = ZoneBorders(2350, 3161, 2356, 3166)
        val YANILLE_BANK = ZoneBorders(2609, 3088, 2614, 3097)
        val ARDOUGNE_EAST_NORTH_BANK = ZoneBorders(2612, 3330, 2621, 3335)
        val ARDOUGNE_EAST_SOUTH_BANK = ZoneBorders(2649, 3280, 2656, 3287)
        val FISHING_GUILD_BANK = ZoneBorders(2585, 3418, 2589, 3422)
        val GNOME_STRONGHOLD_BANK_0 = ZoneBorders(2431, 3478, 2487, 3521, 1, true)
        val PISCATORIS_BANK = ZoneBorders(2327, 3686, 2332, 3693)
        val NEITIZNOT_BANK = ZoneBorders(2334, 3805, 2339, 3808)
        val JATIZSO_BANK = ZoneBorders(2414, 3801, 2419, 3803)
        val ECTECERIA_BANK = ZoneBorders(2619, 3893, 2621, 3896)
        val CANIFIS_BANK = ZoneBorders(3509, 3474, 3516, 3483)
        val PORT_PHASMATYS_BANK = ZoneBorders(3686, 3461, 3699, 3471)
        val EDGEVILLE_BANK = ZoneBorders(3091, 3488, 3098, 3499)
        val BURGH_DE_ROTT_BANK = ZoneBorders(3494, 3210, 3500, 3213)
        val SEERS_VILLAGE_BANK_0 = ZoneBorders(2721, 3490, 2730, 3493)
        val SEERS_VILLAGE_BANK_1 = ZoneBorders(2724, 3487, 2727, 3489)
        val VOID_KNIGHTS_OUTPOST_BANK = ZoneBorders(2665, 2651, 2669, 2655)
        val MOS_LE_HARMLESS_BANK = ZoneBorders(3679, 2980, 3680, 2984)
        val KELDAGRIM_BANK = ZoneBorders(2834, 10206, 2841, 10215)
        val WARRIORS_GUILD_BANK = ZoneBorders(2843, 3537, 2848, 3545)
        val COOKS_GUILD_BANK = ZoneBorders(3144, 3450, 3148, 3453)
        val TUTORIAL_ISLAND_BANK_0 = ZoneBorders(3117, 3119, 3126, 3120)
        val TUTORIAL_ISLAND_BANK_1 = ZoneBorders(3119, 3121, 3124, 3122)
        val TUTORIAL_ISLAND_BANK_2 = ZoneBorders(3118, 3123, 3124, 3125)
        val CATHERBY_BANK = ZoneBorders(2806, 3438, 2812, 3442)
        val GNOME_STRONGHOLD_BANK_1 = ZoneBorders(2443, 3422, 2448, 3427, 1, true)
        val GE_SQUARE_AREA_BANK = ZoneBorders(3162, 3487, 3167, 3492)
    }
}
