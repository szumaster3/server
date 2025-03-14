package content.global.skill.herblore.herbs

import org.rs.consts.Items

enum class Tars(
    val ingredient: Int,
    val level: Int,
    val experience: Double,
    val product: Int,
) {
    GUAM_TAR(Herbs.GUAM.product.id, 19, 30.0, Items.GUAM_TAR_10142),
    GROUND_GUAM_TAR(Items.GROUND_GUAM_6681, 19, 30.0, Items.GUAM_TAR_10142),
    MARRENTILL_TAR(Herbs.MARRENTILL.product.id, 31, 42.5, Items.MARRENTILL_TAR_10143),
    TARROMIN_TAR(Herbs.TARROMIN.product.id, 39, 55.0, Items.TARROMIN_TAR_10144),
    HARRALANDER_TAR(Herbs.HARRALANDER.product.id, 44, 72.5, Items.HARRALANDER_TAR_10145),
    ;

    companion object {
        fun forId(id: Int): Tars? {
            for (tar in values()) {
                if (tar.ingredient == id) {
                    return tar
                }
            }
            return null
        }
    }
}
