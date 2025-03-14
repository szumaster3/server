package content.global.skill.gathering.fishing

import org.rs.consts.NPCs

enum class FishingSpot(
    val ids: IntArray,
    vararg val options: FishingOption,
) {
    NET_BAIT(
        intArrayOf(
            NPCs.TUTORIAL_FISHING_SPOT_952,
            NPCs.FISHING_SPOT_316,
            NPCs.FISHING_SPOT_319,
            NPCs.FISHING_SPOT_320,
            NPCs.FISHING_SPOT_323,
            NPCs.FISHING_SPOT_325,
            NPCs.FISHING_SPOT_326,
            NPCs.FISHING_SPOT_327,
            NPCs.FISHING_SPOT_330,
            NPCs.FISHING_SPOT_332,
            NPCs.FISHING_SPOT_404,
            NPCs.FISHING_SPOT_1331,
            NPCs.FISHING_SPOT_2724,
            NPCs.FISHING_SPOT_4908,
            NPCs.FISHING_SPOT_7045,
        ),
        FishingOption.SMALL_NET,
        FishingOption.BAIT,
    ),
    CAGE(
        intArrayOf(
            NPCs.FISHING_SPOT_6267,
            NPCs.FISHING_SPOT_6996,
            NPCs.FISHING_SPOT_7862,
            NPCs.FISHING_SPOT_7863,
            NPCs.FISHING_SPOT_7864,
        ),
        FishingOption.CRAYFISH_CAGE,
    ),
    LURE_BAIT(
        intArrayOf(
            NPCs.FISHING_SPOT_309,
            NPCs.FISHING_SPOT_310,
            NPCs.FISHING_SPOT_311,
            NPCs.FISHING_SPOT_314,
            NPCs.FISHING_SPOT_315,
            NPCs.FISHING_SPOT_317,
            NPCs.FISHING_SPOT_318,
            NPCs.FISHING_SPOT_328,
            NPCs.FISHING_SPOT_329,
            NPCs.FISHING_SPOT_331,
            NPCs.FISHING_SPOT_403,
            NPCs.FISHING_SPOT_927,
            NPCs.FISHING_SPOT_1189,
            NPCs.FISHING_SPOT_1190,
            NPCs.FISHING_SPOT_3019,
        ),
        FishingOption.LURE,
        FishingOption.PIKE_BAIT,
    ),
    CAGE_HARPOON(
        intArrayOf(
            NPCs.FISHING_SPOT_312,
            NPCs.FISHING_SPOT_321,
            NPCs.FISHING_SPOT_324,
            NPCs.FISHING_SPOT_333,
            NPCs.FISHING_SPOT_405,
            NPCs.FISHING_SPOT_1332,
            NPCs.FISHING_SPOT_1399,
            NPCs.FISHING_SPOT_3804,
            NPCs.FISHING_SPOT_5470,
            NPCs.FISHING_SPOT_7046,
        ),
        FishingOption.LOBSTER_CAGE,
        FishingOption.HARPOON,
    ),
    NET_HARPOON(
        intArrayOf(
            NPCs.FISHING_SPOT_313,
            NPCs.FISHING_SPOT_322,
            NPCs.FISHING_SPOT_334,
            NPCs.FISHING_SPOT_406,
            NPCs.FISHING_SPOT_1191,
            NPCs.FISHING_SPOT_1333,
            NPCs.FISHING_SPOT_1405,
            NPCs.FISHING_SPOT_1406,
            NPCs.FISHING_SPOT_3574,
            NPCs.FISHING_SPOT_3575,
            NPCs.FISHING_SPOT_5471,
            NPCs.FISHING_SPOT_7044,
        ),
        FishingOption.BIG_NET,
        FishingOption.SHARK_HARPOON,
    ),
    HARPOON_NET(
        intArrayOf(NPCs.FISHING_SPOT_3848, NPCs.FISHING_SPOT_3849),
        FishingOption.HARPOON,
        FishingOption.MONKFISH_NET,
    ),
    EELS_BAIT(
        intArrayOf(NPCs.FISHING_SPOT_1236, NPCs.FISHING_SPOT_1237),
        FishingOption.MORT_MYRE_SWAMP_BAIT,
    ),
    SWAMP_NET_BAIT(
        intArrayOf(NPCs.FISHING_SPOT_2067, NPCs.FISHING_SPOT_2068),
        FishingOption.FROGSPAWN_NET,
        FishingOption.LUMBRIDGE_SWAMP_CAVES_BAIT,
    ),
    SPOT_KBWANJI(
        intArrayOf(NPCs.FISHING_SPOT_1174),
        FishingOption.KBWANJI_NET,
    ),
    SPOT_KARAMBWAN(
        intArrayOf(NPCs.FISHING_SPOT_1177),
        FishingOption.KARAMBWAN_VES,
    ),
    LAVA_EEL_BAIT(
        intArrayOf(NPCs.FISHING_SPOT_800),
        FishingOption.OILY_FISHING_ROD,
    ),
    ;

    companion object {
        private val spotMap: HashMap<Int, FishingSpot> = HashMap()
        private val array: ArrayList<Int> = ArrayList()

        init {
            val spots = values()
            for (spot in spots) {
                for (id in spot.ids) {
                    spotMap[id] = spot
                    array.add(id)
                }
            }
        }

        fun forId(npcID: Int): FishingSpot? {
            return spotMap[npcID]
        }

        fun getAllIds(): IntArray {
            return array.toIntArray()
        }
    }

    fun getOptionByName(option: String): FishingOption? {
        for (o in options) {
            if (o.option == option.lowercase()) {
                return o
            }
        }
        return null
    }
}
