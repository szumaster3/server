package content.global.skill.cooking

import core.api.faceLocation
import core.api.inInventory
import core.api.openDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.Scenery

class FireOptionListener : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles interacting with the pottery oven.
         */

        on(potteryId, IntType.SCENERY, "fire") { player, node ->
            player.faceLocation(node.location)
            player.dialogueInterpreter.open(99843, true, true)
            return@on true
        }

        /*
         * Handles interacting with the cooking range.
         */

        on(rangeId, IntType.SCENERY, "fire") { player, node ->
            if (inInventory(player, Items.UNCOOKED_STEW_2001, 1)) {
                faceLocation(player, node.location)
                openDialogue(player, 43989, Items.UNCOOKED_STEW_2001, "stew")
            }
            return@on true
        }

    }

    companion object {
        private val potteryId = intArrayOf(Scenery.POTTERY_OVEN_2643, Scenery.POTTERY_OVEN_4308, Scenery.POTTERY_OVEN_11601, Scenery.POTTERY_OVEN_34802)
        private val rangeId = intArrayOf(Scenery.COOKING_RANGE_114, Scenery.RANGE_2728, Scenery.RANGE_2729, Scenery.RANGE_2730, Scenery.RANGE_2731, Scenery.COOKING_RANGE_2859, Scenery.RANGE_3039, Scenery.COOKING_RANGE_4172, Scenery.COOKING_RANGE_5275, Scenery.COOKING_RANGE_8750, Scenery.RANGE_9682, Scenery.RANGE_12102, Scenery.RANGE_14919, Scenery.COOKING_RANGE_16893, Scenery.RANGE_21792, Scenery.COOKING_RANGE_22154, Scenery.RANGE_22713, Scenery.RANGE_22714, Scenery.RANGE_24283, Scenery.RANGE_24284, Scenery.RANGE_25730, Scenery.RANGE_33500, Scenery.COOKING_RANGE_34410, Scenery.RANGE_34495, Scenery.RANGE_34546, Scenery.COOKING_RANGE_34565, Scenery.RANGE_36973, Scenery.RANGE_37629)
    }
}