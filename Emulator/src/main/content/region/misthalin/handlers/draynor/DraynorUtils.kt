package content.region.misthalin.handlers.draynor

import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

object DraynorUtils {
    const val feedGuardAttribute = "/save:draynor:feed-tree-guard"

    val treeGuardChat =
        arrayOf(
            "Hey - gerroff me!",
            "You'll blow my cover! I'm meant to be hidden!",
            "Don't draw attention to me!",
            "Will you stop that?",
            "Watch what you're doing with that hatchet, you nit!",
            "Ooooch!",
            "Ow! That really hurt!",
            "Oi!",
        )

    val draynorMarket = ZoneBorders(3074, 3245, 3086, 3255)

    val draynorMansionCourtyard = ZoneBorders(3100, 3333, 3114, 3346)

    val bookshelf = intArrayOf(Scenery.OLD_BOOKSHELF_7065, Scenery.OLD_BOOKSHELF_7066, Scenery.OLD_BOOKSHELF_7068)
    const val trapdoor = Scenery.TRAPDOOR_6434
    const val telescope = Scenery.TELESCOPE_7092
    const val tree = Scenery.TREE_10041
    const val diango = NPCs.DIANGO_970
    const val aggie = NPCs.AGGIE_922

    const val cupBoard = Scenery.CUPBOARD_33502
    const val openedCupBoard = Scenery.CUPBOARD_33503
    const val coffin = Scenery.COFFIN_2614
    const val openedCoffin = Scenery.COFFIN_11208
    const val stairsUp = Scenery.STAIRS_32835
    const val stairsBasement = Scenery.STAIRS_32836

    val basement = Location.create(3077, 9770, 0)
    val groundFloor = Location.create(3115, 3356, 0)

    const val garlic = Items.GARLIC_1550
    const val stake = Items.STAKE_1549
    const val hammer = Items.HAMMER_2347
}
