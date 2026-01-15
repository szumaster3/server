package content.global.skill.gather.fishing

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items

/**
 * Represents fishing options.
 */
enum class FishingOption(
    val tool: Int,
    val requiredLevel: Int,
    val animationId: Int,
    val bait: IntArray?,
    val optionName: String,
    vararg val fishId: Fish
) {
    CrayfishCage(Items.CRAYFISH_CAGE_13431, 1, Animations.USE_CRAYFISH_CAGE_10009, null, "cage", Fish.CRAYFISH),
    SmallNet(Items.SMALL_FISHING_NET_303, 1, Animations.NET_FISHING_621, null, "net", Fish.SHRIMP, Fish.ANCHOVY),
    Bait(Items.FISHING_ROD_307, 5, Animations.ROD_FISHING_622, intArrayOf(Items.FISHING_BAIT_313), "bait", Fish.SARDINE, Fish.HERRING),
    Lure(Items.FLY_FISHING_ROD_309, 20, Animations.ROD_FISHING_622, intArrayOf(Items.FEATHER_314, Items.STRIPY_FEATHER_10087), "lure", Fish.TROUT, Fish.SALMON, Fish.RAINBOW_FISH),
    PikeBait(Items.FISHING_ROD_307, 25, Animations.ROD_FISHING_622, intArrayOf(Items.FISHING_BAIT_313), "bait", Fish.PIKE),
    LobsterCage(Items.LOBSTER_POT_301, 40, Animations.LOBSTER_FISHING, null, "cage", Fish.LOBSTER),
    FrogspawnNet(Items.SMALL_FISHING_NET_303, 33, Animations.NET_FISHING_621, null, "net", Fish.FROG_SPAWN, Fish.SWAMP_WEED),
    Harpoon(Items.HARPOON_311, 35, Animations.HARPOON_FISHING_618, null, "harpoon", Fish.TUNA, Fish.SWORDFISH),
    BarbHarpoon(Items.BARB_TAIL_HARPOON_10129, 35, Animations.HARPOON_FISHING_618, null, "harpoon", Fish.TUNA, Fish.SWORDFISH),
    BigNet(Items.BIG_FISHING_NET_305, 16, Animations.NET_FISHING_620, null, "net", Fish.MACKEREL, Fish.COD, Fish.BASS, Fish.SEAWEED),
    SharkHarpoon(Items.HARPOON_311, 76, Animations.HARPOON_FISHING_618, null, "harpoon", Fish.SHARK),
    MonkfishNet(Items.SMALL_FISHING_NET_303, 62, Animations.NET_FISHING_621, null, "net", Fish.MONKFISH),
    MortMyreSwampBait(Items.FISHING_ROD_307, 5, Animations.ROD_FISHING_622, intArrayOf(Items.FISHING_BAIT_313), "bait", Fish.SLIMY_EEL),
    LumbridgeSwampCavesBait(Items.FISHING_ROD_307, 5, Animations.ROD_FISHING_622, intArrayOf(Items.FISHING_BAIT_313), "bait", Fish.SLIMY_EEL, Fish.CAVE_EEL),
    KbwanjiNet(Items.SMALL_FISHING_NET_303, 5, Animations.NET_FISHING_621, null, "net", Fish.KARAMBWANJI),
    KarambwanVes(Items.KARAMBWAN_VESSEL_3157, 65, Animations.FISHING_KARAMBWAN_1193, intArrayOf(Items.RAW_KARAMBWANJI_3150), "fish", Fish.KARAMBWAN),
    OilyFishingRod(Items.OILY_FISHING_ROD_1585, 53, Animations.ROD_FISHING_622, intArrayOf(Items.FISHING_BAIT_313), "bait", Fish.LAVA_EEL),
    GiantCarpRod(Items.FISHING_ROD_307, 10, Animations.ROD_FISHING_622, intArrayOf(Items.RED_VINE_WORM_25), "bait", Fish.GIANT_CARP),
    SardinesRod(Items.FISHING_ROD_307, 10, Animations.ROD_FISHING_622, intArrayOf(Items.RED_VINE_WORM_25), "bait", Fish.SARDINE);

    companion object {
        private val nameMap: HashMap<String, FishingOption> = HashMap()

        init {
            values().forEach { nameMap[it.optionName] = it }
        }

        /**
         * Returns the [FishingOption].
         *
         * @param op The fishing option.
         * @return The [FishingOption] enum, or null.
         */
        @JvmStatic
        fun forName(op: String) = nameMap[op]
    }

    /**
     * Attempts to roll a fish.
     *
     * @param player The player fishing.
     * @return The [Fish] caught, or null if none.
     */
    fun rollFish(player: Player): Fish? {
        if (this == BigNet) {
            return when (RandomFunction.randomize(100)) {
                0  -> Fish.OYSTER
                50 -> Fish.CASKET
                90 -> Fish.SEAWEED
                else -> null
            }
        }

        val level = getDynLevel(player, Skills.FISHING)
        val invisibleLevelBoost = level + player.familiarManager.getBoost(Skills.FISHING)

        for (id in fishId) {
            if (id.requiredLevel > level) continue
            if (this == Lure && inInventory(player, Items.STRIPY_FEATHER_10087) != (id == Fish.RAINBOW_FISH)) continue
            if (RandomFunction.random(0.0, 1.0) < id.getSuccessChance(invisibleLevelBoost)) return id
        }
        return null
    }

    /**
     * Returns the name of the bait.
     */
    fun getBaitName(): String = bait?.firstOrNull()?.let { getItemName(it) } ?: "none"

    /**
     * Checks whether the player has any of the required bait in their inventory.
     *
     * @param player The player to check.
     * @return True if the player has bait, or no bait is required.
     */
    fun hasBait(player: Player): Boolean = bait?.any { inInventory(player, it) } ?: true

    /**
     * Removes one unit of bait from the player inventory.
     *
     * @param player The player to remove bait from.
     * @return True if bait was removed or no bait is required, false otherwise.
     */
    fun removeBait(player: Player): Boolean {
        bait?.forEach { if (removeItem(player, it, Container.INVENTORY)) return true }
        return bait == null
    }
}
