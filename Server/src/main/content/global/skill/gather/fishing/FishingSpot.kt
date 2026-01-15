package content.global.skill.gather.fishing

import shared.consts.NPCs

/**
 * Represents fishing spots and their available fishing options.
 */
enum class FishingSpot(
    vararg val options: FishingOption,
    val ids: IntArray
) {
    NetBait(
        FishingOption.SmallNet, FishingOption.Bait,
        ids = intArrayOf(NPCs.TUTORIAL_FISHING_SPOT_952, NPCs.FISHING_SPOT_316, NPCs.FISHING_SPOT_319, NPCs.FISHING_SPOT_320, NPCs.FISHING_SPOT_323, NPCs.FISHING_SPOT_325, NPCs.FISHING_SPOT_326, NPCs.FISHING_SPOT_327, NPCs.FISHING_SPOT_330, NPCs.FISHING_SPOT_332, NPCs.FISHING_SPOT_404, NPCs.FISHING_SPOT_1331, NPCs.FISHING_SPOT_2724, NPCs.FISHING_SPOT_4908, NPCs.FISHING_SPOT_7045)
    ),

    Cage(
        FishingOption.CrayfishCage,
        ids = intArrayOf(NPCs.FISHING_SPOT_6267, NPCs.FISHING_SPOT_6996, NPCs.FISHING_SPOT_7862, NPCs.FISHING_SPOT_7863, NPCs.FISHING_SPOT_7864)
    ),

    LureBait(
        FishingOption.Lure, FishingOption.PikeBait,
        ids = intArrayOf(NPCs.FISHING_SPOT_309, NPCs.FISHING_SPOT_310, NPCs.FISHING_SPOT_311, NPCs.FISHING_SPOT_314, NPCs.FISHING_SPOT_315, NPCs.FISHING_SPOT_317, NPCs.FISHING_SPOT_318, NPCs.FISHING_SPOT_328, NPCs.FISHING_SPOT_329, NPCs.FISHING_SPOT_331, NPCs.FISHING_SPOT_403, NPCs.FISHING_SPOT_927, NPCs.FISHING_SPOT_1189, NPCs.FISHING_SPOT_1190, NPCs.FISHING_SPOT_3019)
    ),

    CageHarpoon(
        FishingOption.LobsterCage, FishingOption.Harpoon,
        ids = intArrayOf(NPCs.FISHING_SPOT_312, NPCs.FISHING_SPOT_321, NPCs.FISHING_SPOT_324, NPCs.FISHING_SPOT_333, NPCs.FISHING_SPOT_405, NPCs.FISHING_SPOT_1332, NPCs.FISHING_SPOT_1399, NPCs.FISHING_SPOT_3804, NPCs.FISHING_SPOT_5470, NPCs.FISHING_SPOT_7046)
    ),

    NetHarpoon(
        FishingOption.BigNet, FishingOption.SharkHarpoon,
        ids = intArrayOf(NPCs.FISHING_SPOT_313, NPCs.FISHING_SPOT_322, NPCs.FISHING_SPOT_334, NPCs.FISHING_SPOT_406, NPCs.FISHING_SPOT_1191, NPCs.FISHING_SPOT_1333, NPCs.FISHING_SPOT_1405, NPCs.FISHING_SPOT_1406, NPCs.FISHING_SPOT_3574, NPCs.FISHING_SPOT_3575, NPCs.FISHING_SPOT_5471, NPCs.FISHING_SPOT_7044)
    ),

    HarpoonNet(
        FishingOption.Harpoon, FishingOption.MonkfishNet,
        ids = intArrayOf(NPCs.FISHING_SPOT_3848, NPCs.FISHING_SPOT_3849)
    ),

    EelsBait(
        FishingOption.MortMyreSwampBait,
        ids = intArrayOf(NPCs.FISHING_SPOT_1236, NPCs.FISHING_SPOT_1237)
    ),

    SwampNetBait(
        FishingOption.FrogspawnNet, FishingOption.LumbridgeSwampCavesBait,
        ids = intArrayOf(NPCs.FISHING_SPOT_2067, NPCs.FISHING_SPOT_2068)
    ),

    SpotKbwanji(
        FishingOption.KbwanjiNet,
        ids = intArrayOf(NPCs.FISHING_SPOT_1174)
    ),

    SpotKarambwan(
        FishingOption.KarambwanVes,
        ids = intArrayOf(NPCs.FISHING_SPOT_1177)
    ),

    LavaEelBait(
        FishingOption.OilyFishingRod,
        ids = intArrayOf(NPCs.FISHING_SPOT_800)
    ),

    FishingContestGiantCarp(
        FishingOption.GiantCarpRod,
        ids = intArrayOf(NPCs.FISHING_SPOT_233)
    ),

    FishingContestSardines(
        FishingOption.SardinesRod,
        ids = intArrayOf(NPCs.FISHING_SPOT_234)
    );

    private val optionsList: List<FishingOption> = options.toList()

    private val optionName: Map<String, FishingOption> =
        optionsList.associateBy { it.optionName }

    /**
     * Finds a [FishingOption] by its menu option text.
     *
     * @param op the menu option text
     * @return matching [FishingOption] or `null` if none exists
     */
    fun getOptionByName(op: String): FishingOption? =
        this.optionName[op.lowercase()]

    companion object {
        private val byNpcId: Map<Int, FishingSpot> =
            values().flatMap { spot -> spot.ids.asSequence().map { it to spot } }.toMap()

        /**
         * Returns the [FishingSpot] for the npc id.
         */
        fun forId(npcId: Int): FishingSpot? = byNpcId[npcId]

        /**
         * Returns all npc ids corresponding to fishing spots.
         */
        fun getAllIds(): IntArray =
            values().flatMap { it.ids.asSequence() }.toList().toIntArray()
    }
}