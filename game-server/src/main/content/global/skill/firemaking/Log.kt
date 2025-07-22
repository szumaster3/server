package content.global.skill.firemaking

import org.rs.consts.Items
import org.rs.consts.Scenery

/**
 * Represents logs.
 */
enum class Log(val logId: Int, val defaultLevel: Int, val barbarianLevel: Int, val life: Int, val fireId: Int, val xp: Double) {
    NORMAL(Items.LOGS_1511, 1, 21, 180, Scenery.FIRE_2732, 40.0),
    PURPLE(Items.PURPLE_LOGS_10329, 1, 21, 200, Scenery.FIRE_20001, 50.0),
    WHITE(Items.WHITE_LOGS_10328, 1, 21, 200, Scenery.FIRE_20000, 50.0),
    BLUE(Items.BLUE_LOGS_7406, 1, 21, 200, Scenery.FIRE_11406, 50.0),
    GREEN(Items.GREEN_LOGS_7405, 1, 21, 200, Scenery.FIRE_11405, 50.0),
    RED(Items.RED_LOGS_7404, 1, 21, 200, Scenery.FIRE_11404, 50.0),
    JOGRE(Items.JOGRE_BONES_3125, 1, 21, 200, Scenery.BURNING_BONES_3862, 50.0),
    ACHEY(Items.ACHEY_TREE_LOGS_2862, 1, 21, 180, Scenery.FIRE_2732, 40.0),
    OAK(Items.OAK_LOGS_1521, 15, 35, 200, Scenery.FIRE_2732, 60.0),
    WILLOW(Items.WILLOW_LOGS_1519, 30, 50, 250, Scenery.FIRE_2732, 90.0),
    TEAK(Items.TEAK_LOGS_6333, 35, 55, 300, Scenery.FIRE_2732, 105.0),
    ARCTIC_PINE(Items.ARCTIC_PINE_LOGS_10810, 42, 62, 500, Scenery.FIRE_2732, 125.0),
    MAPLE(Items.MAPLE_LOGS_1517, 45, 65, 300, Scenery.FIRE_2732, 135.0),
    MAHOGANY(Items.MAHOGANY_LOGS_6332, 50, 70, 300, Scenery.FIRE_2732, 157.5),
    EUCALYPTUS(Items.EUCALYPTUS_LOGS_12581, 58, 68, 300, Scenery.FIRE_2732, 193.5),
    YEW(Items.YEW_LOGS_1515, 60, 80, 400, Scenery.FIRE_2732, 202.5),
    MAGIC(Items.MAGIC_LOGS_1513, 75, 95, 450, Scenery.FIRE_2732, 303.8),
    CURSED_MAGIC(Items.CURSED_MAGIC_LOGS_13567, 82, 97, 650, Scenery.FIRE_2732, 303.8),
    ;

    companion object {
        private var logMap: HashMap<Int, Log> = HashMap()

        init {
            for (log in values()) {
                logMap[log.logId] = log
            }
        }

        @JvmStatic
        fun forId(id: Int): Log? = logMap[id]
    }
}
