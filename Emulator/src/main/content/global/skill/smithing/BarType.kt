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
        fun getBarTypeForId(itemId: Int): BarType? {
            when (itemId) {
                Items.BRONZE_BAR_2349 -> return BRONZE
                Items.IRON_BAR_2351 -> return IRON
                Items.STEEL_BAR_2353 -> return STEEL
                Items.MITHRIL_BAR_2359 -> return MITHRIL
                Items.ADAMANTITE_BAR_2361 -> return ADAMANT
                Items.RUNITE_BAR_2363 -> return RUNITE
                Items.BLURITE_BAR_9467 -> return BLURITE
            }
            return null
        }
    }
}
