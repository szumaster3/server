package content.region.misthalin.handlers.lumbridge

import core.game.world.map.zone.ZoneBorders
import org.rs.consts.NPCs
import org.rs.consts.Scenery

object LumbridgeUtils {
    val cowPenArea = ZoneBorders(3242, 3255, 3265, 3297)

    var flagInUse: Boolean = false

    val combatTutors = intArrayOf(NPCs.MAGIC_TUTOR_4707, NPCs.RANGED_TUTOR_1861)

    val hamHideoutEntranceVarp = 174
    val hamHideoutVarbit = 2270
    val rfdChestVarbit = 1850

    const val bob = NPCs.BOB_519
    const val doomsayer = NPCs.DOOMSAYER_3777
    const val gnomeAd = Scenery.ADVERTISEMENT_30037
    const val cowfieldSignpost = Scenery.SIGNPOST_31297
    const val churchSignpost = Scenery.SIGNPOST_31299
    const val warnSignpost = Scenery.WARNING_SIGN_15566
    const val rfdChest = Scenery.CHEST_12309
    const val churchOrgans = Scenery.ORGAN_36978
    const val churchBell = Scenery.BELL_36976
    const val castleFlag = Scenery.FLAG_37335
    const val tutorMap = Scenery.LUMBRIDGE_MAP_37655
    const val archeryTarget = Scenery.ARCHERY_TARGET_37095
    const val tools = Scenery.TOOLS_10375
}
