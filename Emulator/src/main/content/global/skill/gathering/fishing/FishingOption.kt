package content.global.skill.gathering.fishing

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items

/**
 * Represents fishing options.
 */
enum class FishingOption(val tool: Int, val level: Int, val animation: Animation, val bait: IntArray?, val option: String, vararg val fish: Fish, ) {
    CRAYFISH_CAGE(Items.CRAYFISH_CAGE_13431, 1, Animation(Animations.USE_CRAYFISH_CAGE_10009), null, "cage", Fish.CRAYFISH),
    SMALL_NET(Items.SMALL_FISHING_NET_303, 1, Animation(Animations.NET_FISHING_621), null, "net", Fish.SHRIMP, Fish.ANCHOVY),
    BAIT(Items.FISHING_ROD_307, 5, Animation(Animations.ROD_FISHING_622), intArrayOf(Items.FISHING_BAIT_313), "bait", Fish.SARDINE, Fish.HERRING),
    LURE(Items.FLY_FISHING_ROD_309, 20, Animation(Animations.ROD_FISHING_622), intArrayOf(Items.FEATHER_314, Items.STRIPY_FEATHER_10087), "lure", Fish.TROUT, Fish.SALMON, Fish.RAINBOW_FISH),
    PIKE_BAIT(Items.FISHING_ROD_307, 25, Animation(Animations.ROD_FISHING_622), intArrayOf(Items.FISHING_BAIT_313), "bait", Fish.PIKE),
    LOBSTER_CAGE(Items.LOBSTER_POT_301, 40, Animation(Animations.LOBSTER_FISHING), null, "cage", Fish.LOBSTER),
    FROGSPAWN_NET(Items.SMALL_FISHING_NET_303, 33, Animation(Animations.NET_FISHING_621), null, "net", Fish.FROG_SPAWN, Fish.SWAMP_WEED),
    HARPOON(Items.HARPOON_311, 35, Animation(Animations.HARPOON_FISHING_618), null, "harpoon", Fish.TUNA, Fish.SWORDFISH),
    BARB_HARPOON(Items.BARB_TAIL_HARPOON_10129, 35, Animation(Animations.HARPOON_FISHING_618), null, "harpoon", Fish.TUNA, Fish.SWORDFISH),
    BIG_NET(Items.BIG_FISHING_NET_305, 16, Animation(Animations.NET_FISHING_620), null, "net", Fish.MACKEREL, Fish.COD, Fish.BASS, Fish.SEAWEED),
    SHARK_HARPOON(Items.HARPOON_311, 76, Animation(Animations.HARPOON_FISHING_618), null, "harpoon", Fish.SHARK),
    MONKFISH_NET(Items.SMALL_FISHING_NET_303, 62, Animation(Animations.NET_FISHING_621), null, "net", Fish.MONKFISH),
    MORT_MYRE_SWAMP_BAIT(Items.FISHING_ROD_307, 5, Animation(Animations.ROD_FISHING_622), intArrayOf(Items.FISHING_BAIT_313), "bait", Fish.SLIMY_EEL),
    LUMBRIDGE_SWAMP_CAVES_BAIT(Items.FISHING_ROD_307, 5, Animation(Animations.ROD_FISHING_622), intArrayOf(Items.FISHING_BAIT_313), "bait", Fish.SLIMY_EEL, Fish.CAVE_EEL),
    KBWANJI_NET(Items.SMALL_FISHING_NET_303, 5, Animation(Animations.NET_FISHING_621), null, "net", Fish.KARAMBWANJI),
    KARAMBWAN_VES(Items.KARAMBWAN_VESSEL_3157, 65, Animation(Animations.FISHING_KARAMBWAN_1193), intArrayOf((Items.RAW_KARAMBWANJI_3150)), "fish", Fish.KARAMBWAN),
    OILY_FISHING_ROD(Items.OILY_FISHING_ROD_1585, 53, Animation(Animations.ROD_FISHING_622), intArrayOf(Items.FISHING_BAIT_313), "bait", Fish.LAVA_EEL),
    FISHING_CONTEST_0(Items.FISHING_ROD_307, 10, Animation(Animations.ROD_FISHING_622), intArrayOf(Items.RED_VINE_WORM_25), "bait", Fish.GIANT_CARP),
    FISHING_CONTEST_1(Items.FISHING_ROD_307, 10, Animation(Animations.ROD_FISHING_622), intArrayOf(Items.RED_VINE_WORM_25), "bait", Fish.SARDINE),
    ;

    companion object {
        /**
         * Maps the option name string to the corresponding [FishingOption] enum instance.
         */
        @JvmStatic
        private val nameMap: HashMap<String, FishingOption> = HashMap()

        /**
         * Initializes the [nameMap] with option names pointing to their respective enum values.
         */
        init {
            for (value in values()) {
                nameMap[value.option] = value
            }
        }

        /**
         * Retrieves a [FishingOption] by its option name.
         */
        @JvmStatic
        fun forName(opName: String): FishingOption? = nameMap[opName]
    }

    /**
     * Attempts to roll a fish that the player can catch using this fishing method.
     */
    fun rollFish(player: Player): Fish? {
        if (this == BIG_NET) {
            when (RandomFunction.randomize(100)) {
                0 -> return Fish.OYSTER
                50 -> return Fish.CASKET
                90 -> return Fish.SEAWEED
            }
        }
        val vlvl = getDynLevel(player, Skills.FISHING)
        val ilvl = vlvl + player.familiarManager.getBoost(Skills.FISHING)
        for (f in fish) {
            if (f.level > vlvl) {
                continue
            }
            if (this == LURE && inInventory(player, Items.STRIPY_FEATHER_10087) != (f == Fish.RAINBOW_FISH)) {
                continue
            }
            val chance = f.getSuccessChance(ilvl)
            if (RandomFunction.random(0.0, 1.0) < chance) {
                return f
            }
        }
        return null
    }

    /**
     * Retrieves the name of the bait item used for this fishing option.
     */
    fun getBaitName(): String {
        if (bait != null && bait.isNotEmpty()) {
            return getItemName(bait[0])
        }
        return "none"
    }

    /**
     * Checks if the player has the required bait for this fishing option.
     */
    fun hasBait(player: Player): Boolean =
        if (bait == null) {
            true
        } else {
            var anyBait = false
            for (b in bait) {
                anyBait = anyBait || inInventory(player, b)
            }
            anyBait
        }

    /**
     * Removes one unit of bait from the player's inventory, if available.
     */
    fun removeBait(player: Player): Boolean {
        return if (bait == null) {
            true
        } else {
            for (i in bait.size downTo 1) {
                if (removeItem(player, bait[i - 1], Container.INVENTORY)) {
                    return true
                }
            }
            false
        }
    }

    /**
     * Returns the message shown when the player starts fishing with this option.
     */
    fun getStartMessage(): String = if (option == "net") "You cast out your net..." else "You attempt to catch a fish."
}
