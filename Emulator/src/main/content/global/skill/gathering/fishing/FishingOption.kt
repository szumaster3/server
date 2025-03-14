package content.global.skill.gathering.fishing

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items

enum class FishingOption(
    val tool: Int,
    val level: Int,
    val animation: Animation,
    val bait: IntArray?,
    val option: String,
    vararg val fish: Fish,
) {
    CRAYFISH_CAGE(
        tool = Items.CRAYFISH_CAGE_13431,
        level = 1,
        animation = Animation(Animations.USE_CRAYFISH_CAGE_10009),
        bait = null,
        option = "cage",
        Fish.CRAYFISH,
    ),
    SMALL_NET(
        tool = Items.SMALL_FISHING_NET_303,
        level = 1,
        animation = Animation(Animations.NET_FISHING_621),
        bait = null,
        option = "net",
        Fish.SHRIMP,
        Fish.ANCHOVIE,
    ),
    BAIT(
        tool = Items.FISHING_ROD_307,
        level = 5,
        animation = Animation(Animations.ROD_FISHING_622),
        bait = intArrayOf(Items.FISHING_BAIT_313),
        option = "bait",
        Fish.SARDINE,
        Fish.HERRING,
    ),
    LURE(
        tool = Items.FLY_FISHING_ROD_309,
        level = 20,
        animation = Animation(Animations.ROD_FISHING_622),
        bait = intArrayOf(Items.FEATHER_314, Items.STRIPY_FEATHER_10087),
        option = "lure",
        Fish.TROUT,
        Fish.SALMON,
        Fish.RAINBOW_FISH,
    ),
    PIKE_BAIT(
        tool = Items.FISHING_ROD_307,
        level = 25,
        animation = Animation(Animations.ROD_FISHING_622),
        bait = intArrayOf(Items.FISHING_BAIT_313),
        option = "bait",
        Fish.PIKE,
    ),
    LOBSTER_CAGE(
        tool = Items.LOBSTER_POT_301,
        level = 40,
        animation = Animation(619),
        bait = null,
        option = "cage",
        Fish.LOBSTER,
    ),
    FROGSPAWN_NET(
        tool = Items.SMALL_FISHING_NET_303,
        level = 33,
        animation = Animation(Animations.NET_FISHING_621),
        bait = null,
        option = "net",
        Fish.FROG_SPAWN,
        Fish.SWAMP_WEED,
    ),
    HARPOON(
        tool = Items.HARPOON_311,
        level = 35,
        animation = Animation(Animations.HARPOON_FISHING_618),
        bait = null,
        option = "harpoon",
        Fish.TUNA,
        Fish.SWORDFISH,
    ),
    BARB_HARPOON(
        tool = Items.BARB_TAIL_HARPOON_10129,
        level = 35,
        animation = Animation(Animations.HARPOON_FISHING_618),
        bait = null,
        option = "harpoon",
        Fish.TUNA,
        Fish.SWORDFISH,
    ),
    BIG_NET(
        tool = Items.BIG_FISHING_NET_305,
        level = 16,
        animation = Animation(Animations.NET_FISHING_620),
        bait = null,
        option = "net",
        Fish.MACKEREL,
        Fish.COD,
        Fish.BASS,
        Fish.SEAWEED,
    ),
    SHARK_HARPOON(
        tool = Items.HARPOON_311,
        level = 76,
        animation = Animation(Animations.HARPOON_FISHING_618),
        bait = null,
        option = "harpoon",
        Fish.SHARK,
    ),
    MONKFISH_NET(
        tool = Items.SMALL_FISHING_NET_303,
        level = 62,
        animation = Animation(Animations.NET_FISHING_621),
        bait = null,
        option = "net",
        Fish.MONKFISH,
    ),
    MORT_MYRE_SWAMP_BAIT(
        tool = Items.FISHING_ROD_307,
        level = 5,
        animation = Animation(Animations.ROD_FISHING_622),
        bait = intArrayOf(Items.FISHING_BAIT_313),
        option = "bait",
        Fish.SLIMY_EEL,
    ),
    LUMBRIDGE_SWAMP_CAVES_BAIT(
        tool = Items.FISHING_ROD_307,
        level = 5,
        animation = Animation(Animations.ROD_FISHING_622),
        bait = intArrayOf(Items.FISHING_BAIT_313),
        option = "bait",
        Fish.SLIMY_EEL,
        Fish.CAVE_EEL,
    ),
    KBWANJI_NET(
        tool = Items.SMALL_FISHING_NET_303,
        level = 5,
        animation = Animation(Animations.NET_FISHING_621),
        bait = null,
        option = "net",
        Fish.KARAMBWANJI,
    ),
    KARAMBWAN_VES(
        tool = Items.KARAMBWAN_VESSEL_3157,
        level = 65,
        animation = Animation(Animations.FISHING_KARAMBWAN_1193),
        bait = intArrayOf((Items.RAW_KARAMBWANJI_3150)),
        option = "fish",
        Fish.KARAMBWAN,
    ),
    OILY_FISHING_ROD(
        tool = Items.OILY_FISHING_ROD_1585,
        level = 53,
        animation = Animation(Animations.ROD_FISHING_622),
        bait = intArrayOf(Items.FISHING_BAIT_313),
        option = "bait",
        Fish.LAVA_EEL,
    ),
    ;

    companion object {
        @JvmStatic
        private val nameMap: HashMap<String, FishingOption> = HashMap()

        init {
            for (value in values()) {
                nameMap[value.option] = value
            }
        }

        @JvmStatic
        fun forName(opName: String): FishingOption? {
            return nameMap[opName]
        }
    }

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

    fun getBaitName(): String {
        if (bait != null && bait.isNotEmpty()) {
            return getItemName(bait[0])
        }
        return "none"
    }

    fun hasBait(player: Player): Boolean {
        return if (bait == null) {
            true
        } else {
            var anyBait = false
            for (b in bait) {
                anyBait = anyBait || inInventory(player, b)
            }
            anyBait
        }
    }

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

    fun getStartMessage(): String {
        return if (option == "net") "You cast out your net..." else "You attempt to catch a fish."
    }
}
