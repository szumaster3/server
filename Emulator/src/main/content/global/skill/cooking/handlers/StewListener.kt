package content.global.skill.cooking.handlers

import core.api.faceLocation
import core.api.inInventory
import core.api.openDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.Scenery

class StewListener : InteractionListener {
    val cookingRanges =
        intArrayOf(
            Scenery.COOKING_RANGE_114,
            Scenery.RANGE_2728,
            Scenery.RANGE_2729,
            Scenery.RANGE_2730,
            Scenery.RANGE_2731,
            Scenery.COOKING_RANGE_2859,
            Scenery.RANGE_3039,
            Scenery.COOKING_RANGE_4172,
            Scenery.COOKING_RANGE_5275,
            Scenery.COOKING_RANGE_8750,
            Scenery.RANGE_9682,
            Scenery.RANGE_12102,
            Scenery.RANGE_14919,
            Scenery.COOKING_RANGE_16893,
            Scenery.RANGE_21792,
            Scenery.COOKING_RANGE_22154,
            Scenery.RANGE_22713,
            Scenery.RANGE_22714,
            Scenery.RANGE_24283,
            Scenery.RANGE_24284,
            Scenery.RANGE_25730,
            Scenery.RANGE_33500,
            Scenery.COOKING_RANGE_34410,
            Scenery.RANGE_34495,
            Scenery.RANGE_34546,
            Scenery.COOKING_RANGE_34565,
            Scenery.RANGE_36973,
            Scenery.RANGE_37629,
        )

    override fun defineListeners() {
        on(cookingRanges, IntType.SCENERY, "fire") { player, node ->
            if (inInventory(player, Items.UNCOOKED_STEW_2001, 1)) {
                faceLocation(player, node.location)
                openDialogue(player, 43989, Items.UNCOOKED_STEW_2001, "stew")
            }
            return@on true
        }
    }
}
