package content.data.items

import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs.consts.Animations
import org.rs.consts.Items

enum class SkillingTool(
    @JvmField val id: Int,
    val level: Int,
    @JvmField val ratio: Double,
    @JvmField val animation: Int,
) {
    BRONZE_AXE(
        id = Items.BRONZE_AXE_1351,
        level = 1,
        ratio = 0.05,
        animation = Animations.BRONZE_AXE_879,
    ),
    IRON_AXE(
        id = Items.IRON_AXE_1349,
        level = 1,
        ratio = 0.1,
        animation = Animations.IRON_AXE_877,
    ),
    STEEL_AXE(
        id = Items.STEEL_AXE_1353,
        level = 6,
        ratio = 0.2,
        animation = Animations.STEEL_AXE_ALT3_875,
    ),
    BLACK_AXE(
        id = Items.BLACK_AXE_1361,
        level = 6,
        ratio = 0.25,
        animation = Animations.BLACK_AXE_A_873,
    ),
    MITHRIL_AXE(
        id = Items.MITHRIL_AXE_1355,
        level = 21,
        ratio = 0.30,
        animation = Animations.MITHRIL_AXE_871,
    ),
    ADAMANT_AXE(
        id = Items.ADAMANT_AXE_1357,
        level = 31,
        ratio = 0.45,
        animation = Animations.ADAMANT_AXE_869,
    ),
    RUNE_AXE(
        id = Items.RUNE_AXE_1359,
        level = 41,
        ratio = 0.65,
        animation = Animations.RUNE_AXE_867,
    ),
    DRAGON_AXE(
        id = Items.DRAGON_AXE_6739,
        level = 61,
        ratio = 0.85,
        animation = Animations.DRAGON_AXE_2846,
    ),
    INFERNO_ADZE(
        id = Items.INFERNO_ADZE_13661,
        level = 61,
        ratio = 0.85,
        animation = Animations.INFERNO_ADZE_10251,
    ),
    AXE_CLASS1(
        id = Items.AXE_CLASS_1_14132,
        level = 1,
        ratio = 0.1,
        animation = Animations.NULL_10603,
    ),
    AXE_CLASS2(
        id = Items.AXE_CLASS_2_14134,
        level = 20,
        ratio = 0.3,
        animation = Animations.NULL_10604,
    ),
    AXE_CLASS3(
        id = Items.AXE_CLASS_3_14136,
        level = 40,
        ratio = 0.65,
        animation = Animations.NULL_10605,
    ),
    AXE_CLASS4(
        id = Items.AXE_CLASS_4_14138,
        level = 60,
        ratio = 0.85,
        animation = Animations.NULL_10606,
    ),
    AXE_CLASS5(
        id = Items.AXE_CLASS_5_14140,
        level = 80,
        ratio = 1.0,
        animation = Animations.NULL_10607,
    ),

    BRONZE_PICKAXE(
        id = Items.BRONZE_PICKAXE_1265,
        level = 1,
        ratio = 0.05,
        animation = Animations.MINING_BRONZE_PICKAXE_625,
    ),
    IRON_PICKAXE(
        id = Items.IRON_PICKAXE_1267,
        level = 1,
        ratio = 0.1,
        animation = Animations.MINING_IRON_PICKAXE_626,
    ),
    STEEL_PICKAXE(
        id = Items.STEEL_PICKAXE_1269,
        level = 6,
        ratio = 0.2,
        animation = Animations.MINING_STEEL_PICKAXE_627,
    ),
    MITHRIL_PICKAXE(
        id = Items.MITHRIL_PICKAXE_1273,
        level = 21,
        ratio = 0.30,
        animation = Animations.MINING_MITHRIL_PICKAXE_629,
    ),
    ADAMANT_PICKAXE(
        id = Items.ADAMANT_PICKAXE_1271,
        level = 31,
        ratio = 0.45,
        animation = Animations.MINING_ADAMANT_PICKAXE_628,
    ),
    RUNE_PICKAXE(
        id = Items.RUNE_PICKAXE_1275,
        level = 41,
        ratio = 0.65,
        animation = Animations.MINING_RUNE_PICKAXE_624,
    ),
    INFERNO_ADZE2(
        id = Items.INFERNO_ADZE_13661,
        level = 61,
        ratio = 1.0,
        animation = Animations.MINING_INFERNO_ADZE_10222,
    ),
    PICKAXE_CLASS1(
        id = Items.PICKAXE_CLASS_1_14122,
        level = 1,
        ratio = 0.1,
        animation = Animations.NULL_10608,
    ),
    PICKAXE_CLASS2(
        id = Items.PICKAXE_CLASS_2_14124,
        level = 20,
        ratio = 0.3,
        animation = Animations.NULL_10609,
    ),
    PICKAXE_CLASS3(
        id = Items.PICKAXE_CLASS_3_14126,
        level = 40,
        ratio = 0.65,
        animation = Animations.NULL_10610,
    ),
    PICKAXE_CLASS4(
        id = Items.PICKAXE_CLASS_4_14128,
        level = 60,
        ratio = 0.85,
        animation = Animations.NULL_10611,
    ),
    PICKAXE_CLASS5(
        id = Items.PICKAXE_CLASS_5_14130,
        level = 80,
        ratio = 1.0,
        animation = Animations.NULL_10613,
    ),

    HARPOON_CLASS1(
        id = Items.HARPOON_CLASS_1_14142,
        level = 1,
        ratio = 0.1,
        animation = Animations.NULL_10613,
    ),
    HARPOON_CLASS2(
        id = Items.HARPOON_CLASS_2_14144,
        level = 20,
        ratio = 0.3,
        animation = Animations.NULL_10614,
    ),
    HARPOON_CLASS3(
        id = Items.HARPOON_CLASS_3_14146,
        level = 40,
        ratio = 0.65,
        animation = Animations.NULL_10615,
    ),
    HARPOON_CLASS4(
        id = Items.HARPOON_CLASS_4_14148,
        level = 60,
        ratio = 0.85,
        animation = Animations.NULL_10616,
    ),
    HARPOON_CLASS5(
        id = Items.HARPOON_CLASS_5_14150,
        level = 80,
        ratio = 1.0,
        animation = Animations.NULL_10617,
    ),

    BUTTERFLY_NET_CLASS1(
        id = Items.BUTTERFLY_NET_CLASS_1_14152,
        level = 1,
        ratio = 0.1,
        animation = Animations.SC_BUTTERFLY_NET_CLASS_1_10618,
    ),
    BUTTERFLY_NET_CLASS2(
        id = Items.BUTTERFLY_NET_CLASS_2_14154,
        level = 20,
        ratio = 0.3,
        animation = Animations.SC_BUTTERFLY_NET_CLASS_2_10619,
    ),
    BUTTERFLY_NET_CLASS3(
        id = Items.BUTTERFLY_NET_CLASS_3_14156,
        level = 40,
        ratio = 0.65,
        animation = Animations.SC_BUTTERFLY_NET_CLASS_3_10620,
    ),
    BUTTERFLY_NET_CLASS4(
        id = Items.BUTTERFLY_NET_CLASS_4_14158,
        level = 60,
        ratio = 0.85,
        animation = Animations.SC_BUTTERFLY_NET_CLASS_4_10621,
    ),
    BUTTERFLY_NET_CLASS5(
        id = Items.BUTTERFLY_NET_CLASS_5_14160,
        level = 80,
        ratio = 1.0,
        animation = Animations.SC_BUTTERFLY_NET_CLASS_5_10622,
    ),

    TRAINING_BOW(
        id = Items.TRAINING_BOW_9705,
        level = 1,
        ratio = 0.1,
        animation = Animations.LIGHT_FIRE_WITH_BOW_6713,
    ),
    LONGBOW(
        id = Items.LONGBOW_839,
        level = 1,
        ratio = 0.1,
        animation = Animations.LIGHT_FIRE_WITH_BOW_6714,
    ),
    SHORTBOW(
        id = Items.SHORTBOW_841,
        level = 1,
        ratio = 0.1,
        animation = Animations.LIGHT_FIRE_WITH_BOW_6714,
    ),
    OAK_SHORTBOW(
        id = Items.OAK_SHORTBOW_843,
        level = 1,
        ratio = 0.2,
        animation = Animations.LIGHT_FIRE_WITH_BOW_6715,
    ),
    OAK_LONGBOW(
        id = Items.OAK_LONGBOW_845,
        level = 1,
        ratio = 0.2,
        animation = Animations.LIGHT_FIRE_WITH_BOW_6715,
    ),
    WILLOW_SHORTBOW(
        id = Items.WILLOW_SHORTBOW_849,
        level = 1,
        ratio = 0.30,
        animation = Animations.LIGHT_FIRE_WITH_BOW_6716,
    ),
    WILLOW_LONGBOW(
        id = Items.WILLOW_LONGBOW_847,
        level = 1,
        ratio = 0.30,
        animation = Animations.LIGHT_FIRE_WITH_BOW_6716,
    ),
    MAPLE_SHORTBOW(
        id = Items.MAPLE_SHORTBOW_853,
        level = 1,
        ratio = 0.45,
        animation = Animations.LIGHT_FIRE_WITH_BOW_6717,
    ),
    MAPLE_LONGBOW(
        id = Items.MAPLE_LONGBOW_851,
        level = 1,
        ratio = 0.45,
        animation = Animations.LIGHT_FIRE_WITH_BOW_6717,
    ),
    YEW_SHORTBOW(
        id = Items.YEW_SHORTBOW_857,
        level = 1,
        ratio = 0.65,
        animation = Animations.LIGHT_FIRE_WITH_BOW_6718,
    ),
    YEW_LONGBOW(
        id = Items.YEW_LONGBOW_855,
        level = 1,
        ratio = 0.65,
        animation = Animations.LIGHT_FIRE_WITH_BOW_6718,
    ),
    MAGIC_SHORTBOW(
        id = Items.MAGIC_SHORTBOW_861,
        level = 1,
        ratio = 0.85,
        animation = Animations.LIGHT_FIRE_WITH_MAGIC_BOW_6719,
    ),
    MAGIC_LONGBOW(
        id = Items.MAGIC_LONGBOW_859,
        level = 1,
        ratio = 0.85,
        animation = Animations.LIGHT_FIRE_WITH_MAGIC_BOW_6719,
    ),
    SEERCULL(
        id = Items.SEERCULL_6724,
        level = 1,
        ratio = 0.85,
        animation = Animations.LIGHT_FIRE_WITH_BOW_6720,
    ),
    SACRED_CLAY_BOW(
        id = Items.SACRED_CLAY_BOW_14121,
        level = 1,
        ratio = 1.00,
        animation = Animations.LIGHT_FIRE_WITH_SC_BOW_10990,
    ),

    MACHETE(
        id = Items.MACHETE_975,
        level = 1,
        ratio = 0.24,
        animation = Animations.SWIPE_WITH_MACHETE_TAI_BWO_WANNAI_CLEANUP_2382,
    ),
    OPAL_MACHETE(
        id = Items.OPAL_MACHETE_6313,
        level = 1,
        ratio = 0.26,
        animation = Animations.SWING_WITH_OPAL_MACHETE_6085,
    ),
    JADE_MACHETE(
        id = Items.JADE_MACHETE_6315,
        level = 1,
        ratio = 0.32,
        animation = Animations.SWING_WITH_JADE_MACHETE_6086,
    ),
    RED_TOPAZ_MACHETE(
        id = Items.RED_TOPAZ_MACHETE_6317,
        level = 1,
        ratio = 0.38,
        animation = Animations.SWING_WITH_RED_TOPAZ_MACHETE_6087,
    ),
    ;

    companion object {
        @JvmStatic
        fun forId(itemId: Int): SkillingTool? {
            for (tool in values()) {
                if (tool.id == itemId) {
                    return tool
                }
            }
            return null
        }

        @JvmStatic
        fun getHatchet(player: Player): SkillingTool? {
            var tool: SkillingTool? = null
            val hatchetPriority =
                arrayOf(
                    AXE_CLASS5,
                    AXE_CLASS4,
                    DRAGON_AXE,
                    AXE_CLASS3,
                    RUNE_AXE,
                    ADAMANT_AXE,
                    AXE_CLASS2,
                    MITHRIL_AXE,
                    BLACK_AXE,
                    STEEL_AXE,
                    AXE_CLASS1,
                    IRON_AXE,
                    BRONZE_AXE,
                )
            for (hatchet in hatchetPriority) {
                if (checkTool(player, Skills.WOODCUTTING, hatchet)) {
                    tool = hatchet
                    break
                }
            }
            if (checkTool(player, Skills.WOODCUTTING, INFERNO_ADZE)) {
                if (player.getSkills().getLevel(Skills.FIREMAKING) >= 92) {
                    tool = INFERNO_ADZE
                }
            }
            return tool
        }

        @JvmStatic
        fun getPickaxe(player: Player): SkillingTool? {
            var tool: SkillingTool? = null
            val pickaxePriority =
                arrayOf(
                    PICKAXE_CLASS5,
                    PICKAXE_CLASS4,
                    RUNE_PICKAXE,
                    PICKAXE_CLASS3,
                    ADAMANT_PICKAXE,
                    PICKAXE_CLASS2,
                    MITHRIL_PICKAXE,
                    STEEL_PICKAXE,
                    PICKAXE_CLASS1,
                    IRON_PICKAXE,
                    BRONZE_PICKAXE,
                )
            for (pickaxe in pickaxePriority) {
                if (checkTool(player, Skills.MINING, pickaxe)) {
                    tool = pickaxe
                    break
                }
            }
            if (checkTool(player, Skills.MINING, INFERNO_ADZE2)) {
                if (player.getSkills().getLevel(Skills.FIREMAKING) >= 92) {
                    tool = INFERNO_ADZE2
                }
            }
            return tool
        }

        @JvmStatic
        fun getHarpoon(player: Player): SkillingTool? {
            var tool: SkillingTool? = null
            val harpoonPriority =
                arrayOf(
                    BUTTERFLY_NET_CLASS5,
                    BUTTERFLY_NET_CLASS4,
                    BUTTERFLY_NET_CLASS3,
                    BUTTERFLY_NET_CLASS2,
                    BUTTERFLY_NET_CLASS1,
                )
            for (harpoon in harpoonPriority) {
                if (checkTool(player, Skills.FISHING, harpoon)) {
                    tool = harpoon
                    break
                }
            }
            return tool
        }

        @JvmStatic
        fun getButterflyNet(player: Player): SkillingTool? {
            var tool: SkillingTool? = null
            val butterflyNetPriority =
                arrayOf(
                    BUTTERFLY_NET_CLASS5,
                    BUTTERFLY_NET_CLASS4,
                    BUTTERFLY_NET_CLASS3,
                    BUTTERFLY_NET_CLASS2,
                    BUTTERFLY_NET_CLASS1,
                )
            for (butterflyNet in butterflyNetPriority) {
                if (checkTool(player, Skills.HUNTER, butterflyNet)) {
                    tool = butterflyNet
                    break
                }
            }
            return tool
        }

        @JvmStatic
        fun getFiremakingTool(player: Player): SkillingTool? {
            var tool: SkillingTool? = null
            val bowPriority =
                arrayOf(
                    SACRED_CLAY_BOW,
                    SEERCULL,
                    MAGIC_SHORTBOW,
                    MAGIC_LONGBOW,
                    YEW_SHORTBOW,
                    YEW_LONGBOW,
                    MAPLE_SHORTBOW,
                    MAPLE_LONGBOW,
                    WILLOW_SHORTBOW,
                    WILLOW_LONGBOW,
                    OAK_LONGBOW,
                    OAK_SHORTBOW,
                    SHORTBOW,
                    LONGBOW,
                    TRAINING_BOW,
                )
            for (bowId in bowPriority) {
                if (checkTool(player, Skills.FIREMAKING, bowId)) {
                    tool = bowId
                    break
                }
            }
            return tool
        }

        @JvmStatic
        fun getMachete(player: Player?): SkillingTool? {
            var tool: SkillingTool? = null
            val machetePriority =
                arrayOf(
                    RED_TOPAZ_MACHETE,
                    JADE_MACHETE,
                    OPAL_MACHETE,
                    MACHETE,
                )
            for (machete in machetePriority) {
                if (checkTool(player!!, Skills.WOODCUTTING, machete)) {
                    tool = machete
                    break
                }
            }

            return tool
        }

        @JvmStatic
        fun getToolForSkill(
            player: Player?,
            skill: Int,
        ): SkillingTool? {
            return when (skill) {
                Skills.MINING -> getPickaxe(player!!)
                Skills.WOODCUTTING -> getHatchet(player!!)
                Skills.FISHING -> getHarpoon(player!!)
                Skills.HUNTER -> getButterflyNet(player!!)
                Skills.FIREMAKING -> getFiremakingTool(player!!)
                else -> null
            }
        }

        @JvmStatic
        fun checkTool(
            player: Player,
            skillId: Int,
            tool: SkillingTool,
        ): Boolean {
            if (player.getSkills().getStaticLevel(skillId) < tool.level) {
                return false
            }
            if (player.equipment.getNew(3).id == tool.id) {
                return true
            }
            return player.inventory.contains(tool.id, 1)
        }
    }
}
