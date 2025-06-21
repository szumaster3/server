package content.global.skill.cooking.brewing

import org.rs.consts.Items

/**
 * Represents brewable data.
 *
 * @author GregF, Makar
 */
enum class Brewable(val displayName: String, val itemID: Int, val level: Int, val product: IntArray, val levelXP: Double, val vatVarBitOffset: Int, val barrelVarBitOffset: Int, val ingredientAmount: Int = 4) {
    CIDER("Cider", Items.APPLE_MUSH_5992, 14, intArrayOf(Items.CIDER_5763, Items.CIDER4_5849), 182.0, 58, 80),
    DWARVEN_STOUT("Dwarven Stout", Items.HAMMERSTONE_HOPS_5994, 19, intArrayOf(Items.DWARVEN_STOUT_1913, Items.DWARVEN_STOUT4_5777), 215.0, 4, 8),
    KELDA_STOUT("Kelda Stout", Items.KELDA_HOPS_6113, 22, intArrayOf(Items.KELDA_STOUT_6118), 0.0, 68, 3, 1),
    ASGARIAN_ALE("Asgarnian Ale", Items.ASGARNIAN_HOPS_5996, 26, intArrayOf(Items.ASGARNIAN_ALE_1905, Items.ASGARNIAN_ALE4_5785), 248.0, 10, 16),
    GREENMANS_ALE("Greenmans Ale", Items.CLEAN_HARRALANDER_255, 29, intArrayOf(Items.GREENMANS_ALE_1909, Items.GREENMANS_ALE4_5793), 281.0, 16, 24),
    WIZARD_BOMB("Wizard's Mindbomb", Items.YANILLIAN_HOPS_5998, 34, intArrayOf(Items.WIZARDS_MIND_BOMB_1907, Items.MIND_BOMB4_5801), 341.0, 22, 32),
    DRAGON_BITTER("Dragon Bitter", Items.KRANDORIAN_HOPS_6000, 39, intArrayOf(Items.DRAGON_BITTER_1911, Items.DRAGON_BITTER4_5809), 347.0, 28, 40),
    MOONLIGHT_MEAD("Moonlight Mead", Items.MUSHROOM_6004, 44, intArrayOf(Items.MOONLIGHT_MEAD_2955, Items.MOONLIGHT_MEAD4_5817), 380.0, 34, 48),
    AXEMANS_FOLLY("Axeman's Folly", Items.OAK_ROOTS_6043, 49, intArrayOf(Items.AXEMANS_FOLLY_5751, Items.AXEMANS_FOLLY4_5825), 413.0, 40, 56),
    CHEFS_DELIGHT("Chef's Delight", Items.CHOCOLATE_DUST_1975, 54, intArrayOf(Items.CHEFS_DELIGHT_5755, Items.CHEFS_DELIGHT4_5833), 446.0, 46, 64, 1),
    SLAYERS_RESPITE("Slayer's Respite", Items.WILDBLOOD_HOPS_6002, 59, intArrayOf(Items.SLAYERS_RESPITE_5759, Items.SLAYERS_RESPITE4_5841), 479.0, 52, 72),
    ;

    companion object {
        fun getIngredients(): IntArray {
            return values().map { it.itemID }.toIntArray()
        }

        fun getBrewable(itemID: Int): Brewable? {
            return values().find { it.itemID == itemID }
        }

    }

}