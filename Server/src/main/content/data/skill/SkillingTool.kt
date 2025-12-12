package content.data.skill

import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import shared.consts.Animations
import shared.consts.Items

/**
 * Represents a skilling tool items.
 */
enum class SkillingTool(val id: Int, val level: Int, val ratio: Double, val animation: Int) {
    // Axes.
    BRONZE_AXE(Items.BRONZE_AXE_1351, 1, 0.05, Animations.BRONZE_AXE_879),
    IRON_AXE(Items.IRON_AXE_1349, 1, 0.1, Animations.IRON_AXE_877),
    STEEL_AXE(Items.STEEL_AXE_1353, 6, 0.2, Animations.STEEL_AXE_ALT3_875),
    BLACK_AXE(Items.BLACK_AXE_1361, 6, 0.25, Animations.BLACK_AXE_A_873),
    MITHRIL_AXE(Items.MITHRIL_AXE_1355, 21, 0.30, Animations.MITHRIL_AXE_871),
    ADAMANT_AXE(Items.ADAMANT_AXE_1357, 31, 0.45, Animations.ADAMANT_AXE_869),
    RUNE_AXE(Items.RUNE_AXE_1359, 41, 0.65, Animations.RUNE_AXE_867),
    DRAGON_AXE(Items.DRAGON_AXE_6739, 61, 0.85, Animations.DRAGON_AXE_2846),
    INFERNO_ADZE(Items.INFERNO_ADZE_13661, 61, 0.85, Animations.INFERNO_ADZE_10251),
    AXE_CLASS1(Items.AXE_CLASS_1_14132, 1, 0.1, Animations.NULL_10603),
    AXE_CLASS2(Items.AXE_CLASS_2_14134, 20, 0.3, Animations.NULL_10604),
    AXE_CLASS3(Items.AXE_CLASS_3_14136, 40, 0.65, Animations.NULL_10605),
    AXE_CLASS4(Items.AXE_CLASS_4_14138, 60, 0.85, Animations.NULL_10606),
    AXE_CLASS5(Items.AXE_CLASS_5_14140, 80, 1.0, Animations.NULL_10607),
    // Pickaxes.
    BRONZE_PICKAXE(Items.BRONZE_PICKAXE_1265, 1, 0.05, Animations.MINING_BRONZE_PICKAXE_625),
    IRON_PICKAXE(Items.IRON_PICKAXE_1267, 1, 0.1, Animations.MINING_IRON_PICKAXE_626),
    STEEL_PICKAXE(Items.STEEL_PICKAXE_1269, 6, 0.2, Animations.MINING_STEEL_PICKAXE_627),
    MITHRIL_PICKAXE(Items.MITHRIL_PICKAXE_1273, 21, 0.30, Animations.MINING_MITHRIL_PICKAXE_629),
    ADAMANT_PICKAXE(Items.ADAMANT_PICKAXE_1271, 31, 0.45, Animations.MINING_ADAMANT_PICKAXE_628),
    RUNE_PICKAXE(Items.RUNE_PICKAXE_1275, 41, 0.65, Animations.MINING_RUNE_PICKAXE_624),
    INFERNO_ADZE2(Items.INFERNO_ADZE_13661, 61, 1.0, Animations.MINING_INFERNO_ADZE_10222),
    PICKAXE_CLASS1(Items.PICKAXE_CLASS_1_14122, 1, 0.1, Animations.NULL_10608),
    PICKAXE_CLASS2(Items.PICKAXE_CLASS_2_14124, 20, 0.3, Animations.NULL_10609),
    PICKAXE_CLASS3(Items.PICKAXE_CLASS_3_14126, 40, 0.65, Animations.NULL_10610),
    PICKAXE_CLASS4(Items.PICKAXE_CLASS_4_14128, 60, 0.85, Animations.NULL_10611),
    PICKAXE_CLASS5(Items.PICKAXE_CLASS_5_14130, 80, 1.0, Animations.NULL_10613),
    // Harpoons.
    HARPOON_CLASS1(Items.HARPOON_CLASS_1_14142, 1, 0.1, Animations.NULL_10613),
    HARPOON_CLASS2(Items.HARPOON_CLASS_2_14144, 20, 0.3, Animations.NULL_10614),
    HARPOON_CLASS3(Items.HARPOON_CLASS_3_14146, 40, 0.65, Animations.NULL_10615),
    HARPOON_CLASS4(Items.HARPOON_CLASS_4_14148, 60, 0.85, Animations.NULL_10616),
    HARPOON_CLASS5(Items.HARPOON_CLASS_5_14150, 80, 1.0, Animations.NULL_10617),
    // Butterfly nets.
    BUTTERFLY_NET_CLASS1(Items.BUTTERFLY_NET_CLASS_1_14152, 1, 0.1, Animations.SC_BUTTERFLY_NET_CLASS_1_10618),
    BUTTERFLY_NET_CLASS2(Items.BUTTERFLY_NET_CLASS_2_14154, 20, 0.3, Animations.SC_BUTTERFLY_NET_CLASS_2_10619),
    BUTTERFLY_NET_CLASS3(Items.BUTTERFLY_NET_CLASS_3_14156, 40, 0.65, Animations.SC_BUTTERFLY_NET_CLASS_3_10620),
    BUTTERFLY_NET_CLASS4(Items.BUTTERFLY_NET_CLASS_4_14158, 60, 0.85, Animations.SC_BUTTERFLY_NET_CLASS_4_10621),
    BUTTERFLY_NET_CLASS5(Items.BUTTERFLY_NET_CLASS_5_14160, 80, 1.0, Animations.SC_BUTTERFLY_NET_CLASS_5_10622),
    // Firemaking (barbarian) tools.
    TRAINING_BOW(Items.TRAINING_BOW_9705, 1, 0.1, Animations.LIGHT_FIRE_WITH_BOW_6713),
    LONGBOW(Items.LONGBOW_839, 1, 0.1, Animations.LIGHT_FIRE_WITH_BOW_6714),
    SHORTBOW(Items.SHORTBOW_841, 1, 0.1, Animations.LIGHT_FIRE_WITH_BOW_6714),
    OAK_SHORTBOW(Items.OAK_SHORTBOW_843, 1, 0.2, Animations.LIGHT_FIRE_WITH_BOW_6715),
    OAK_LONGBOW(Items.OAK_LONGBOW_845, 1, 0.2, Animations.LIGHT_FIRE_WITH_BOW_6715),
    WILLOW_SHORTBOW(Items.WILLOW_SHORTBOW_849, 1, 0.30, Animations.LIGHT_FIRE_WITH_BOW_6716),
    WILLOW_LONGBOW(Items.WILLOW_LONGBOW_847, 1, 0.30, Animations.LIGHT_FIRE_WITH_BOW_6716),
    MAPLE_SHORTBOW(Items.MAPLE_SHORTBOW_853, 1, 0.45, Animations.LIGHT_FIRE_WITH_BOW_6717),
    MAPLE_LONGBOW(Items.MAPLE_LONGBOW_851, 1, 0.45, Animations.LIGHT_FIRE_WITH_BOW_6717),
    YEW_SHORTBOW(Items.YEW_SHORTBOW_857, 1, 0.65, Animations.LIGHT_FIRE_WITH_BOW_6718),
    YEW_LONGBOW(Items.YEW_LONGBOW_855, 1, 0.65, Animations.LIGHT_FIRE_WITH_BOW_6718),
    MAGIC_SHORTBOW(Items.MAGIC_SHORTBOW_861, 1, 0.85, Animations.LIGHT_FIRE_WITH_MAGIC_BOW_6719),
    MAGIC_LONGBOW(Items.MAGIC_LONGBOW_859, 1, 0.85, Animations.LIGHT_FIRE_WITH_MAGIC_BOW_6719),
    SEERCULL(Items.SEERCULL_6724, 1, 0.85, Animations.LIGHT_FIRE_WITH_BOW_6720),
    SACRED_CLAY_BOW(Items.SACRED_CLAY_BOW_14121, 1, 1.00, Animations.LIGHT_FIRE_WITH_SC_BOW_10990),
    // Machetes.
    MACHETE(Items.MACHETE_975, 1, 0.24, Animations.SWIPE_WITH_MACHETE_TAI_BWO_WANNAI_CLEANUP_2382),
    OPAL_MACHETE(Items.OPAL_MACHETE_6313, 1, 0.26, Animations.SWING_WITH_OPAL_MACHETE_6085),
    JADE_MACHETE(Items.JADE_MACHETE_6315, 1, 0.32, Animations.SWING_WITH_JADE_MACHETE_6086),
    RED_TOPAZ_MACHETE(Items.RED_TOPAZ_MACHETE_6317, 1, 0.38, Animations.SWING_WITH_RED_TOPAZ_MACHETE_6087),
    ;

    /**
     * Returns mining delay for pickaxes.
     */
    fun getMiningDelay(): Int {
        return when (this) {
            BRONZE_PICKAXE -> 6
            IRON_PICKAXE -> 6
            STEEL_PICKAXE -> 5
            MITHRIL_PICKAXE -> 4
            ADAMANT_PICKAXE -> 3
            RUNE_PICKAXE -> 2
            INFERNO_ADZE2 -> (1..2).random()
            else -> 6
        }
    }

    companion object {
        /**
         * Finds tool by item id.
         */
        @JvmStatic
        fun forId(itemId: Int): SkillingTool? {
            for (tool in values()) {
                if (tool.id == itemId) {
                    return tool
                }
            }
            return null
        }

        /**
         * Returns the axe the player can use.
         */
        @JvmStatic
        fun getAxe(player: Player): SkillingTool? {
            var tool: SkillingTool? = null
            val hatchetPriority =
                arrayOf(AXE_CLASS5, AXE_CLASS4, DRAGON_AXE, AXE_CLASS3, RUNE_AXE, ADAMANT_AXE, AXE_CLASS2, MITHRIL_AXE, BLACK_AXE, STEEL_AXE, AXE_CLASS1, IRON_AXE, BRONZE_AXE)
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

        /**
         * Returns the axe the player can use.
         */
        @JvmStatic
        fun getPickaxe(player: Player): SkillingTool? {
            var tool: SkillingTool? = null
            val pickaxePriority =
                arrayOf(PICKAXE_CLASS5, PICKAXE_CLASS4, RUNE_PICKAXE, PICKAXE_CLASS3, ADAMANT_PICKAXE, PICKAXE_CLASS2, MITHRIL_PICKAXE, STEEL_PICKAXE, PICKAXE_CLASS1, IRON_PICKAXE, BRONZE_PICKAXE)
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

        /**
         * Returns the harpoon the player can use.
         */
        @JvmStatic
        fun getHarpoon(player: Player): SkillingTool? {
            var tool: SkillingTool? = null
            val harpoonPriority =
                arrayOf(HARPOON_CLASS5, HARPOON_CLASS4, HARPOON_CLASS3, HARPOON_CLASS2, HARPOON_CLASS1)
            for (harpoon in harpoonPriority) {
                if (checkTool(player, Skills.FISHING, harpoon)) {
                    tool = harpoon
                    break
                }
            }
            return tool
        }

        /**
         * Returns the butterfly net the player can use.
         */
        @JvmStatic
        fun getButterflyNet(player: Player): SkillingTool? {
            var tool: SkillingTool? = null
            val butterflyNetPriority =
                arrayOf(BUTTERFLY_NET_CLASS5, BUTTERFLY_NET_CLASS4, BUTTERFLY_NET_CLASS3, BUTTERFLY_NET_CLASS2, BUTTERFLY_NET_CLASS1)
            for (butterflyNet in butterflyNetPriority) {
                if (checkTool(player, Skills.HUNTER, butterflyNet)) {
                    tool = butterflyNet
                    break
                }
            }
            return tool
        }

        /**
         * Returns the barbarian fm tool the player can use.
         */
        @JvmStatic
        fun getFiremakingTool(player: Player): SkillingTool? {
            var tool: SkillingTool? = null
            val bowPriority =
                arrayOf(SACRED_CLAY_BOW, SEERCULL, MAGIC_SHORTBOW, MAGIC_LONGBOW, YEW_SHORTBOW, YEW_LONGBOW, MAPLE_SHORTBOW, MAPLE_LONGBOW, WILLOW_SHORTBOW, WILLOW_LONGBOW, OAK_LONGBOW, OAK_SHORTBOW, SHORTBOW, LONGBOW, TRAINING_BOW)
            for (bowId in bowPriority) {
                if (checkTool(player, Skills.FIREMAKING, bowId)) {
                    tool = bowId
                    break
                }
            }
            return tool
        }

        /**
         * Returns the machete the player can use.
         */
        @JvmStatic
        fun getMachete(player: Player?): SkillingTool? {
            var tool: SkillingTool? = null
            val machetePriority =
                arrayOf(RED_TOPAZ_MACHETE, JADE_MACHETE, OPAL_MACHETE, MACHETE)
            for (machete in machetePriority) {
                if (checkTool(player!!, Skills.WOODCUTTING, machete)) {
                    tool = machete
                    break
                }
            }

            return tool
        }

        /**
         * Returns the tool for the skill.
         */
        @JvmStatic
        fun getToolForSkill(player: Player?, skill: Int): SkillingTool? =
            when (skill) {
                Skills.MINING -> getPickaxe(player!!)
                Skills.WOODCUTTING -> getAxe(player!!)
                Skills.FISHING -> getHarpoon(player!!)
                Skills.HUNTER -> getButterflyNet(player!!)
                Skills.FIREMAKING -> getFiremakingTool(player!!)
                else -> null
            }

        /**
         * Checks for tool.
         */
        @JvmStatic
        fun checkTool(player: Player, skillId: Int, tool: SkillingTool): Boolean {
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
