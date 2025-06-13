package content.global.plugin.item.equipment.gloves

import core.game.node.entity.skill.Skills
import org.rs.consts.Items

/**
 * The enum Brawling gloves.
 */
enum class BrawlingGloves(
    /**
     * Gets id.
     *
     * @return the id
     */
    @JvmField val id: Int,
    /**
     * Gets charges.
     *
     * @return the charges
     */
    @JvmField val charges: Int,
    /**
     * Gets indicator.
     *
     * @return the indicator
     */
    @JvmField val indicator: Int,
    /**
     * Gets skill slot.
     *
     * @return the skill slot
     */
    val skillSlot: Int,
) {
    /**
     * Melee brawling gloves.
     */
    MELEE(Items.BRAWLING_GLOVES_MELEE_13845, 500, 1, Skills.ATTACK),

    /**
     * Ranged brawling gloves.
     */
    RANGED(Items.BRAWLING_GLOVES_RANGED_13846, 500, 2, Skills.RANGE),

    /**
     * Magic brawling gloves.
     */
    MAGIC(Items.BRAWLING_GLOVES_MAGIC_13847, 500, 3, Skills.MAGIC),

    /**
     * Prayer brawling gloves.
     */
    PRAYER(Items.BRAWLING_GLOVES_PRAYER_13848, 450, 4, Skills.PRAYER),

    /**
     * Agility brawling gloves.
     */
    AGILITY(Items.BRAWLING_GLOVES_AGILITY_13849, 450, 5, Skills.AGILITY),

    /**
     * Woodcutting brawling gloves.
     */
    WOODCUTTING(Items.BRAWLING_GLOVES_WC_13850, 450, 6, Skills.WOODCUTTING),

    /**
     * Firemaking brawling gloves.
     */
    FIREMAKING(Items.BRAWLING_GLOVES_FM_13851, 450, 7, Skills.FIREMAKING),

    /**
     * Mining brawling gloves.
     */
    MINING(Items.BRAWLING_GLOVES_MINING_13852, 450, 8, Skills.MINING),

    /**
     * Hunter brawling gloves.
     */
    HUNTER(Items.BRAWLING_GLOVES_HUNTER_13853, 450, 9, Skills.HUNTER),

    /**
     * Thieving brawling gloves.
     */
    THIEVING(Items.BRAWLING_GLOVES_THIEVING_13854, 450, 10, Skills.THIEVING),

    /**
     * Smithing brawling gloves.
     */
    SMITHING(Items.BRAWLING_GLOVES_SMITHING_13855, 450, 11, Skills.SMITHING),

    /**
     * Fishing brawling gloves.
     */
    FISHING(Items.BRAWLING_GLOVES_FISHING_13856, 450, 12, Skills.FISHING),

    /**
     * Cooking brawling gloves.
     */
    COOKING(Items.BRAWLING_GLOVES_COOKING_13857, 450, 13, Skills.COOKING),
    ;

    companion object {
        private val glovesMap: MutableMap<Int, BrawlingGloves> = HashMap()
        private val skillMap: MutableMap<Int, BrawlingGloves> = HashMap()
        private val indicatorMap: MutableMap<Int, BrawlingGloves> = HashMap()

        init {
            for (glove in values()) {
                glovesMap[glove.id] = glove
                skillMap[glove.skillSlot] = glove
                indicatorMap[glove.indicator] = glove
            }
        }

        /**
         * For id brawling gloves.
         *
         * @param id the id
         * @return the brawling gloves
         */
        @JvmStatic
        fun forId(id: Int): BrawlingGloves? = glovesMap[id]

        /**
         * For indicator brawling gloves.
         *
         * @param indicator the indicator
         * @return the brawling gloves
         */
        @JvmStatic
        fun forIndicator(indicator: Int): BrawlingGloves? = indicatorMap[indicator]

        /**
         * For skill brawling gloves.
         *
         * @param skillSlot the skill slot
         * @return the brawling gloves
         */
        @JvmStatic
        fun forSkill(skillSlot: Int): BrawlingGloves? = skillMap[skillSlot]
    }
}
