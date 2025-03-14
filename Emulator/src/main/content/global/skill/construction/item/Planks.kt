package content.global.skill.construction.item

import org.rs.consts.Items

enum class Planks(
    val log: Int,
    val plank: Int,
    val price: Int,
) {
    WOOD(
        log = Items.LOGS_1511,
        plank = Items.PLANK_960,
        price = 100,
    ),
    OAK(
        log = Items.OAK_LOGS_1521,
        plank = Items.OAK_PLANK_8778,
        price = 250,
    ),
    TEAK(
        log = Items.TEAK_LOGS_6333,
        plank = Items.TEAK_PLANK_8780,
        price = 500,
    ),
    MAHOGANY(
        log = Items.MAHOGANY_LOGS_6332,
        plank = Items.MAHOGANY_PLANK_8782,
        price = 1500,
    ),
    ;

    companion object {
        private val product = values().associateBy { it.log }

        fun getForLog(item: Int): Planks? {
            return product[item]
        }

        fun Planks.spellPrice(): Int {
            return (this.price * 0.7).toInt()
        }
    }
}
