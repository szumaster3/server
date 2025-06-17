package core.game.global.action

import core.api.finishDiaryTask
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.Location

/**
 * Represents the special ladder interactions.
 */
enum class SpecialLadder(private val location: Location, private val destination: Location) : LadderAchievementCheck {
    ALKHARID_CRAFTING_DOWN(Location.create(3314, 3187, 1), Location.create(3310, 3187, 0)),
    ALKHARID_CRAFTING_UP(Location.create(3311, 3187, 0), Location.create(3314, 3187, 1)),
    ALKHARID_SOCRCERESS_DOWN(Location.create(3325, 3139, 1), Location.create(3325, 3143, 0)),
    ALKHARID_SOCRCERESS_UP(Location.create(3325, 3142, 0), Location.create(3325, 3139, 1)),
    ALKHARID_ZEKE_DOWN(Location.create(3284, 3190, 1), Location.create(3284, 3186, 0)),
    ALKHARID_ZEKE_UP(Location.create(3284, 3186, 0), Location.create(3284, 3190, 1)),
    BEAR_CAGE_DOWN(Location.create(3230, 3508, 0), Location.create(3229, 9904, 0)),
    BEAR_CAGE_UP(Location.create(3230, 9904, 0), Location.create(3231, 3504, 0)),
    BETTY_BASEMENT_CLIMB_UP(Location.create(3220, 4522, 0), Location.create(3015, 3260, 0)),
    BETTY_BASEMENT_CLIMB_DOWN(Location.create(3015, 3257, 0), Location.create(3219, 4522, 0)),
    BLACK_KNIGHTS_LADDER_DOWN(Location.create(3022, 3518, 1), Location.create(3022, 3517, 0)),
    BLACK_KNIGHTS_LADDER_UP(Location.create(3022, 3518, 0), Location.create(3022, 3517, 1)),
    BLACK_KNIGHTS_LADDER_DOWN_1(Location.create(3023, 3513, 2), Location.create(3023, 3514, 1)),
    BLACK_KNIGHTS_LADDER_UP_1(Location.create(3023, 3513, 1), Location.create(3023, 3514, 2)),
    BLACK_KNIGHTS_LADDER_DOWN_2(Location.create(3025, 3513, 2), Location.create(3025, 3514, 1)),
    BLACK_KNIGHTS_LADDER_UP_2(Location.create(3025, 3513, 1), Location.create(3025, 3514, 2)),
    BLACK_KNIGHTS_LADDER_DOWN_3(Location.create(3016, 3519, 2), Location.create(3016, 3518, 1)),
    BLACK_KNIGHTS_LADDER_UP_3(Location.create(3016, 3519, 1), Location.create(3016, 3518, 2)),
    BLACK_KNIGHTS_LADDER_DOWN_4(Location.create(3015, 3519, 1), Location.create(3015, 3518, 0)),
    BLACK_KNIGHTS_LADDER_UP_4(Location.create(3015, 3518, 0), Location.create(3015, 3518, 1)),
    BLACK_KNIGHTS_LADDER_DOWN_5(Location.create(3017, 3516, 2), Location.create(3017, 3515, 1)),
    BLACK_KNIGHTS_LADDER_UP_5(Location.create(3017, 3516, 1), Location.create(3017, 3515, 2)),
    BLACK_KNIGHTS_LADDER_DOWN_6(Location.create(3021, 3510, 1), Location.create(3022, 3510, 0)),
    BLACK_KNIGHTS_LADDER_UP_6(Location.create(3021, 3510, 0), Location.create(3022, 3510, 1)),
    BLACK_KNIGHTS_BASEMENT_LADDER_DOWN(Location.create(3016, 3519, 0), Location.create(1867, 4243, 0)),
    BLACK_KNIGHTS_BASEMENT_LADDER_UP(Location.create(1867, 4244, 0), Location.create(3016, 3518, 0)),
    BLACK_KNIGHT_CATACOMBS_LADDER_UP(Location.create(3017, 9922, 1), Location.create(1869, 4237, 0)),
    CATHERBY_BLACK_DRAGONS_LADDER_UP(Location.create(2841, 9824, 0), Location.create(2842, 3423, 0)),
    TRAVERLEY_DUNGEON_LADDER_UP(Location.create(2884, 9797, 0), Location.create(2884, 3398, 0)),
    CHAMPION_CHALLENGE_LADDER_UP(Location.create(3190, 9758, 0), Location.create(3191, 3355, 0)),
    CHAMPION_CHALLENGE_LADDER_DOWN(Location.create(3190, 3355, 0), Location.create(3189, 9758, 0)),
    CASTLEWARS_SARADOMIN_MAIN_FLOOR_STAIRS_DOWN(Location.create(2419, 3080, 1), Location.create(2419, 3077, 0)),
    CASTLEWARS_SARADOMIN_MAIN_FLOOR_STAIRS_UP(Location.create(2428, 3081, 1), Location.create(2430, 3080, 2)),
    CASTLEWARS_SARADOMIN_OUTER_WALL_STAIRS_DOWN(Location.create(2417, 3075, 0), Location.create(2417, 3078, 0)),
    CASTLEWARS_SARADOMIN_OUTER_WALL_STAIRS_UP(Location.create(2417, 3077, 0), Location.create(2416, 3075, 0)),
    CASTLEWARS_ZAMORAK_MAIN_FLOOR_STAIRS_UP(Location.create(2380, 3129, 0), Location.create(2379, 3127, 1)),
    CASTLEWARS_ZAMORAK_OUTER_WALL_STAIRS_UP(Location.create(2382, 3130, 0), Location.create(2383, 3132, 0)),
    CASTLEWARS_ZAMORAK_TOP_FLOOR_DOWN(Location.create(2374, 3133, 3), Location.create(2374, 3130, 2)),
    CASTLEWARS_ZAMORAK_OUTER_WALL_STAIRS_DOWN(Location.create(2382, 3132, 0), Location.create(2382, 3129, 0)),
    CLOCKTOWER_HIDDEN_LADDER(Location.create(2572, 9631, 0), Location.create(2572, 3230, 0)),
    CREATURE_CREATION_EXIT(Location.create(3038, 4375, 0), Location.create(2647, 3214, 0)),
    CREATURE_CREATION_ENTER(Location.create(2647, 3213, 0), Location.create(3038, 4376, 0)),
    DRAYNOR_SEWER_NORTHWEST_DOWN(Location.create(3084, 3272, 0), Location.create(3085, 9672, 0)),
    DRAYNOR_SEWER_NORTHWEST_UP(Location.create(3084, 9672, 0), Location.create(3084, 3271, 0)),
    DRAYNOR_SEWER_SOUTHEAST_DOWN(Location.create(3118, 3244, 0), Location.create(3118, 9643, 0)),
    DRAYNOR_SEWER_SOUTHEAST_UP(Location.create(3118, 9643, 0), Location.create(3118, 3243, 0)),
    ENTRANA_GLASSBLOWING_PIPE_HOUSE_DOWN(Location.create(2816, 3352, 1), Location.create(2817, 3352, 0)),
    EDGEVILLE_CLIMB_UP(Location.create(3097, 9867, 0), Location.create(3096, 3468, 0)),
    FALADOR_NEAR_MINING_STAIRS_F1_DOWN(Location.create(3019, 3343, 1), Location.create(3017, 3343, 0)),
    FALADOR_MINING_NORTH_UP(Location.create(3019, 9740, 0), Location.create(3019, 3341, 0)),
    FALADOR_MINING_EAST_UP(Location.create(3020, 9739, 0), Location.create(3021, 3339, 0)),
    FALADOR_MINING_SOUTH_UP(Location.create(3019, 9738, 0), Location.create(3019, 3337, 0)),
    FALADOR_MINING_WEST_UP(Location.create(3018, 9739, 0), Location.create(3017, 3339, 0)),
    FISHING_GUILD_HOUSE_UP(Location.create(2615, 3394, 0), Location.create(2615, 3395, 1)),
    FISHING_GUILD_HOUSE_DOWN(Location.create(2615, 3394, 1), Location.create(2615, 3395, 0)),
    FOG_ENTER(Location.create(3240, 3575, 0), Location.create(1675, 5599, 0)),
    FOG_LEAVE(Location.create(1673, 5598, 0), Location.create(3242, 3574, 0)),
    GEM_MINE(Location.create(2821, 2996, 0), Location.create(2838, 9387, 0)),
    GEM_MINE_UP(Location.create(2838, 9388, 0), Location.create(2820, 2996, 0)),
    GLARIAL_EXIT(Location.create(2556, 9844, 0), Location.create(2557, 3444, 0)),
    HADLEY_HOUSE_STAIRS_CLIMB_UP(Location.create(2517, 3429, 0), Location.create(2518, 3431, 1)),
    HAM_STORAGE_UP(Location.create(2567, 5185, 0), Location.create(3166, 9623, 0)),
    HAUNTED_MINE_LADDER_F0_DOWN(Location.create(3422, 9625, 0), Location.create(2783, 4569, 0)),
    HAUNTED_MINE_LADDER_F0_UP(Location.create(2782, 4569, 1), Location.create(3423, 9625, 0)),
    HAUNTED_MINE_LADDER_F1_DOWN(Location.create(2798, 4567, 0), Location.create(2733, 4503, 0)),
    HAUNTED_MINE_LADDER_F1_UP(Location.create(2734, 4503, 0), Location.create(2797, 4567, 0)),
    HAUNTED_MINE_LADDER_F2_DOWN(Location.create(2725, 4486, 0), Location.create(2789, 4487, 0)),
    HAUNTED_MINE_LADDER_F2_UP(Location.create(2789, 4486, 0), Location.create(2797, 4567, 0)),
    HEROES_GUILD_LADDER_UP(Location.create(2890, 3508, 1), Location.create(2890, 3507, 2)),
    HEROES_GUILD_STAIRCASE_UP(Location.create(2895, 3513, 0), Location.create(2897, 3513, 1)),
    HORROR_FROM_THE_DEEP_BASEMENT_UP(Location.create(2519, 4618, 1), Location.create(2510, 3644, 0)),
    HORROR_FROM_THE_DEEP_BASEMENT_AFTER_QUEST_UP(Location.create(2519, 9994, 1), Location.create(2510, 3644, 0)),
    HETTY_BASEMENT_CLIMB_UP(Location.create(3165, 4514, 0), Location.create(2967, 3201, 0)),
    HETTY_BASEMENT_CLIMB_DOWN(Location.create(2968, 3201, 0), Location.create(3165, 4515, 0)),
    JATIZSO_STAIRS_1_DOWN(Location.create(2388, 3804, 2), Location.create(2387, 3804, 0)),
    JATIZSO_STAIRS_1_UP(Location.create(2388, 3804, 0), Location.create(2387, 3804, 2)),
    JATIZSO_STAIRS_2_DOWN(Location.create(2388, 3793, 2), Location.create(2387, 3793, 0)),
    JATIZSO_STAIRS_2_UP(Location.create(2388, 3793, 0), Location.create(2387, 3793, 2)),
    JATIZSO_STAIRS_3_DOWN(Location.create(2410, 3823, 2), Location.create(2410, 3824, 0)),
    JATIZSO_STAIRS_3_UP(Location.create(2410, 3823, 0), Location.create(2410, 3824, 2)),
    JATIZSO_STAIRS_4_DOWN(Location.create(2421, 3823, 2), Location.create(2421, 3824, 0)),
    JATIZSO_STAIRS_4_UP(Location.create(2421, 3823, 0), Location.create(2421, 3824, 2)),
    JATIZSO_MINE_DOWN(Location.create(2397, 3812, 0), Location.create(2405, 10188, 0)),
    JATIZSO_MINE_UP(Location.create(2406, 10188, 0), Location.create(2397, 3811, 0)),
    JATIZSO_SHOUT_TOWER_DOWN(Location.create(2373, 3800, 2), Location.create(2374, 3800, 0)),
    JATIZSO_SHOUT_TOWER_UP(Location.create(2373, 3800, 0), Location.create(2374, 3800, 2)),
    KHARIDIAN_DESERT_SUMMONING_UP(Location.create(3299, 9318, 0), Location.create(3303, 2897, 0)),
    KHARIDIAN_DESERT_SUMMONING_DOWN(Location.create(3304, 2897, 0), Location.create(3299, 9317, 0)),
    KELDAGRIM_LIBRARY_UP(Location.create(2865, 10222, 0), Location.create(2865, 10224, 1)),
    LAVA_MAZE_EAST_UP(Location.create(3017, 10249, 0), Location.create(3069, 3855, 0)),
    LAVA_MAZE_KBD_UP(Location.create(3069, 10256, 0), Location.create(3017, 3848, 0)),
    LEGENDS_GUILD_STAIRCASE_UP(Location.create(2732, 3377, 0), Location.create(2732, 3380, 1)),
    LEGENDS_GUILD_STAIRCASE_DOWN(Location.create(2724, 3374, 0), Location.create(2725, 9775, 0)),
    LUMBRIDGE_LEARNING_THE_ROPES_DOWN(Location.create(3230, 3241, 0), Location.create(3290, 4936, 0)),
    LUMBRIDGE_LEARNING_THE_ROPES_UP(Location.create(3290, 4935, 0), Location.create(3230, 3240, 0)),
    MOVARIO_LADDER_UP(Location.create(2036, 4379, 0), Location.create(2502, 3255, 0)),
    MYREQUE_INN_BASEMENT_UP(Location.create(3477, 9846, 0), Location.create(3496, 3465, 0)),
    NEITIZNOT_STAIRS_UP(Location.create(2363, 3799, 0), Location.create(2364, 3799, 2)),
    NEITIZNOT_STAIRS_DOWN(Location.create(2363, 3799, 2), Location.create(2362, 3799, 0)),
    OBSERVATORY_STAIRS_CLIMB_DOWN(Location.create(2443, 3159, 1), Location.create(2444, 3162, 0)),
    OBSERVATORY_STAIRS_CLIMB_UP(Location.create(2444, 3159, 0), Location.create(2442, 3159, 1)),
    OURANIA_LADDER_CLIMB_UP(Location.create(3271, 4862, 0), Location.create(2452, 3230, 0)),
    OURANIA_LADDER_CLIMB_DOWN(Location.create(2452, 3231, 0), Location.create(3271, 4862, 0)),
    PATERDOMUS_TEMPLE_STAIRCASE_NORTH_UP(Location.create(3415, 3490, 0), Location.create(3415, 3491, 1)),
    PATERDOMUS_TEMPLE_STAIRCASE_NORTH_DOWN(Location.create(3415, 3491, 1), Location.create(3414, 3491, 0)),
    PATERDOMUS_TEMPLE_STAIRCASE_SOUTH_DOWN(Location.create(3415, 3486, 1), Location.create(3414, 3486, 0)),
    PORT_PHASMATYS_BAR_DOWN(Location.create(3681, 3498, 0), Location.create(3682, 9961, 0)),
    PORT_PHASMATYS_BAR_UP(Location.create(3682, 9962, 0), Location.create(3681, 3497, 0)),
    PIRATES_COVE_SHIP_CENTER_STAIRS_DOWN(Location.create(2225, 3807, 1), Location.create(2225, 3809, 0)),
    PIRATES_COVE_SHIP_CENTER_STAIRS_UP(Location.create(2225, 3808, 0), Location.create(2225, 3806, 1)),
    PIRATES_COVE_SHIP_NORTH_EAST_STAIRS_DOWN(Location.create(2227, 3806, 3), Location.create(2227, 3805, 2)),
    PIRATES_COVE_SHIP_NORTH_EAST_STAIRS_UP(Location.create(2227, 3806, 2), Location.create(2227, 3808, 3)),
    PIRATES_COVE_SHIP_NORTH_WEST_STAIRS_DOWN(Location.create(2221, 3806, 3), Location.create(2221, 3805, 2)),
    PIRATES_COVE_SHIP_NORTH_WEST_STAIRS_UP(Location.create(2221, 3806, 2), Location.create(2221, 3808, 3)),
    PIRATES_COVE_SHIP_SOUTH_EAST_STAIRS_DOWN(Location.create(2227, 3793, 3), Location.create(2227, 3795, 2)),
    PIRATES_COVE_SHIP_SOUTH_EAST_STAIRS_UP(Location.create(2227, 3794, 2), Location.create(2227, 3792, 3)),
    PIRATES_COVE_SHIP_SOUTH_LEFT_STAIRS_DOWN(Location.create(2221, 3792, 2), Location.create(2223, 3792, 1)),
    PIRATES_COVE_SHIP_SOUTH_LEFT_STAIRS_UP(Location.create(2222, 3792, 1), Location.create(2220, 3792, 2)),
    PIRATES_COVE_SHIP_SOUTH_RIGHT_STAIRS_DOWN(Location.create(2226, 3792, 2), Location.create(2225, 3792, 1)),
    PIRATES_COVE_SHIP_SOUTH_RIGHT_STAIRS_UP(Location.create(2226, 3792, 1), Location.create(2228, 3792, 2)),
    PIRATES_COVE_SHIP_SOUTH_WEST_STAIRS_DOWN(Location.create(2221, 3793, 3), Location.create(2221, 3795, 2)),
    PIRATES_COVE_SHIP_SOUTH_WEST_STAIRS_UP(Location.create(2221, 3794, 2), Location.create(2221, 3792, 3)),
    RAT_PITS_PORT_SARIM_LADDER_UP(Location.create(2962, 9651, 0), Location.create(3018, 3231, 0)),
    RAT_PITS_PORT_SARIM_LADDER_DOWN(Location.create(3018, 3232, 0), Location.create(2962, 9650, 0)) {
        override fun checkAchievement(player: Player) {
            finishDiaryTask(player, DiaryType.FALADOR, 1, 11)
        }
    },
    RAT_PITS_ARDOUGNE_LADDER_UP(Location.create(2661, 9641, 0), Location.create(2562, 3320, 0)),
    RAT_PITS_ARDOUGNE_LADDER_DOWN(Location.create(2561, 3320, 0), Location.create(2662, 9641, 0)),
    RAT_PITS_VARROCK_LADDER_UP(Location.create(2895, 5097, 0), Location.create(3268, 3400, 0)),
    RAT_PITS_VARROCK_LADDER_DOWN(Location.create(3267, 3400, 0), Location.create(2894, 5097, 0)),
    RAT_PITS_KELDAGRIM_LADDER_UP(Location.create(1943, 4703, 1), Location.create(2914, 10187, 0)),
    RAT_PITS_KELDAGRIM_LADDER_DOWN(Location.create(2914, 10188, 0), Location.create(1943, 4705, 1)),
    REVIVED_WATCHTOWER_LADDER_DOWN(Location.create(2933, 4711, 2), Location.create(2549, 3112, 1)),
    SEERS_VILLAGE_SPINNING_HOUSE_ROOFTOP_DOWN(Location.create(2715, 3472, 3), Location.create(2714, 3472, 1)),
    SEERS_VILLAGE_SPINNING_HOUSE_ROOFTOP_UP(Location.create(2715, 3472, 1), Location.create(2714, 3472, 3)) {
        override fun checkAchievement(player: Player) {
            finishDiaryTask(player, DiaryType.SEERS_VILLAGE, 1, 3)
        }
    },
    SHADOW_DUNGEON_UP(Location.create(2629, 5072, 0), Location.create(2548, 3421, 0)),
    SOPHANEM_ALTAR_LADDER_UP(Location.create(3279, 2771, 1), Location.create(3279, 2770, 3)),
    SOPHANEM_ALTAR_LADDER_DOWN(Location.create(3279, 2771, 3), Location.create(3279, 2772, 1)),
    SWENSEN_DOWN(Location.create(2644, 3657, 0), Location.create(2631, 10006, 0)),
    SWENSEN_UP(Location.create(2665, 10037, 0), Location.create(2649, 3661, 0)),
    TREE_GNOME_STRONGHOLD_WEST_BAR_STAIRS_DOWN(Location.create(2418, 3491, 1), Location.create(2419, 3491, 0)),
    TREE_GNOME_STRONGHOLD_WEST_BAR_STAIRS_UP(Location.create(2417, 3490, 0), Location.create(2418, 3492, 1)),
    TREE_GNOME_STRONGHOLD_SPINNING_WHEEL_STAIRS_UP(Location.create(2488, 3407, 0), Location.create(2489, 3409, 1)),
    TREE_GNOME_STRONGHOLD_GATE_STAIRS_UP(Location.create(2461, 3416, 0), Location.create(2460, 3417, 1)),
    TAVERLEY_DUNGEON_DOWN(Location.create(2884, 3397, 0), Location.create(2884, 9796, 0)),
    WATERBIRTH_ISLAND_DUNGEON_SUBLEVEL_2_WALLASALKI_3_LADDER_DOWN(Location.create(1799, 4387, 2), Location.create(1799, 4388, 1)),
    WATERBIRTH_LADDER_WAY_TO_LIGHTHOUSE_1_UP(Location.create(1932, 4378, 0), Location.create(1932, 4380, 2)),
    WATERBIRTH_LADDER_WAY_TO_LIGHTHOUSE_2_UP(Location.create(1961, 4391, 2), Location.create(1961, 4393, 3)),
    WATERBIRTH_LADDER_WAY_TO_LIGHTHOUSE_3_UP(Location.create(1975, 4408, 3), Location.create(2510, 3644, 0)),
    WEREWOLF_AGILITY_COURSE_LADDER_UP(Location.create(3549, 9864, 0), Location.create(3543, 3463, 0)),
    WITCH_HOUSE_TO_BASEMENT_DOWN(Location.create(2907, 3476, 0), Location.create(2906, 9876, 0)),
    WITCH_HOUSE_TO_BASEMENT_UP(Location.create(2907, 9876, 0), Location.create(2906, 3476, 0)),
    WIZARD_TOWER_LADDER_UP(Location.create(3103, 9576, 0), Location.create(3105, 3162, 0)),
    WHITE_WOLF_MOUNTAIN_FAKE_LADDER_1_UP(Location.create(2837, 9927, 0), Location.create(2837, 3527, 0)),
    WHITE_WOLF_MOUNTAIN_FAKE_LADDER_2_UP(Location.create(2823, 9930, 0), Location.create(2823, 3529, 0)),
    DRAYNOR_MANOR_LADDER_DOWN(Location.create(3092, 3362, 0), Location.create(3117, 9753, 0)),
    DRAYNOR_MANOR_LADDER_UP(Location.create(3117, 9754, 0), Location.create(3092, 3361, 0)),
    VARROCK_HOUSE_UP(Location.create(3230, 3383, 0), Location.create(3230, 3382, 1)),
    VARROCK_HOUSE_DOWN(Location.create(3230, 3383, 1), Location.create(3230, 3386, 0)),
    VARROCK_KING_RAT_LADDER_UP(Location.create(3268, 3379, 0), Location.create(3269, 3379, 1)),
    VARROCK_KING_RAT_LADDER_DOWN(Location.create(3268, 3379, 1), Location.create(3267, 3379, 0)),
    VARROCK_SEWERS_DOWN(Location.create(3237, 3458, 0), Location.create(3237, 9858, 0)),
    VARROCK_SEWERS_UP(Location.create(3237, 9858, 0), Location.create(3238, 3458, 0)),
    VARROCK_FUR_SHOP_DOWN(Location.create(3283, 3398, 1), Location.create(3283, 3397, 0)),
    VARROCK_FUR_SHOP_UP(Location.create(3283, 3398, 0), Location.create(3283, 3399, 1)),
    VARROCK_BEHIND_BAR_STAIRS_DOWN(Location.create(3231, 3401, 0), Location.create(3232, 9801, 0)),
    VARROCK_LOWE_STAIRS_UP(Location.create(3233, 3424, 0), Location.create(3232, 3424, 1)),
    ;

    companion object {

        /**
         * Map of ladder locations to their destinations.
         */
        @JvmStatic
        private val destinationMap = HashMap<Location, Location>()

        /**
         * Map of ladder locations to their [SpecialLadder] instances.
         */
        @JvmStatic
        private val ladderMap = HashMap<Location, SpecialLadder>()

        init {
            for (entry in values()) {
                destinationMap.putIfAbsent(entry.location, entry.destination)
                ladderMap.putIfAbsent(entry.location, entry)
            }
        }

        /**
         * Adds a ladder location and its destination.
         */
        @JvmStatic
        fun add(from: Location, to: Location) {
            destinationMap[from] = to
        }

        /**
         * Gets the destination for a given ladder location.
         */
        @JvmStatic
        fun getDestination(loc: Location): Location? = destinationMap[loc]

        /**
         * Gets the [SpecialLadder] for a location.
         */
        @JvmStatic
        fun getSpecialLadder(loc: Location): SpecialLadder? = ladderMap[loc]
    }

}
