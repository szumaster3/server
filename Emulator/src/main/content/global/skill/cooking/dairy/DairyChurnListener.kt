package content.global.skill.cooking.dairy

import core.api.anyInInventory
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.Scenery

class DairyChurnListener : InteractionListener {

    private val ingredientsIDs = intArrayOf(
        Items.BUCKET_OF_MILK_1927,
        Items.POT_OF_CREAM_2130,
        Items.PAT_OF_BUTTER_6697
    )

    private val churnIDs = intArrayOf(
        Scenery.DAIRY_CHURN_10093,
        Scenery.DAIRY_CHURN_10094,
        Scenery.DAIRY_CHURN_25720,
        Scenery.DAIRY_CHURN_34800,
        Scenery.DAIRY_CHURN_35931
    )

    override fun defineListeners() {
        /*
         * Handles interaction with dairy churns.
         * Opens the dairy processing dialogue if the player has a valid ingredient:
         *  - Bucket of Milk
         *  - Pot of Cream
         *  - Pat of Butter
         *
         * Ticks: 8 (4.8 seconds)
         */

        on(churnIDs, IntType.SCENERY, "churn") { player, _ ->
            if (!anyInInventory(player, *ingredientsIDs)) {
                sendMessage(player, "You need some milk, cream or butter to use in the churn.")
                return@on true
            }
            player.dialogueInterpreter.open(984374)
            return@on true
        }

        /*
         * Handles using dairy ingredients on a churn.
         */

        onUseWith(IntType.SCENERY, ingredientsIDs, *churnIDs) { player, _, _ ->
            if (!anyInInventory(player, *ingredientsIDs)) {
                sendMessage(player, "You need some milk, cream or butter to use in the churn.")
                return@onUseWith true
            }
            player.dialogueInterpreter.open(984374)
            return@onUseWith true
        }
    }
}