package content.global.skill.smithing

import org.rs.consts.Items

enum class BarType(
    val barType: Int,
    @JvmField val experience: Double,
    val barName: String,
) {
    BRONZE(Items.BRONZE_BAR_2349, 12.5, "Bronze Smithing"),
    BLURITE(Items.BLURITE_BAR_9467, 16.0, "Blurite Smithing"),
    IRON(Items.IRON_BAR_2351, 25.0, "Iron Smithing"),
    STEEL(Items.STEEL_BAR_2353, 37.5, "Steel Smithing"),
    MITHRIL(Items.MITHRIL_BAR_2359, 50.0, "Mithril Smithing"),
    ADAMANT(Items.ADAMANTITE_BAR_2361, 62.5, "Adamant Smithing"),
    RUNITE(Items.RUNITE_BAR_2363, 75.0, "Runite Smithing"),
    ;

    companion object {
        @JvmStatic
        fun getBarTypeForId(itemId: Int): BarType? {
            when (itemId) {
                2349 -> return BRONZE
                2351 -> return IRON
                2353 -> return STEEL
                2359 -> return MITHRIL
                2361 -> return ADAMANT
                2363 -> return RUNITE
                9467 -> return BLURITE
            }
            return null
        }
    }
}
