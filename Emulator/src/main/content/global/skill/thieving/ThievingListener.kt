package content.global.skill.thieving

import core.api.lockInteractions
import core.api.submitIndividualPulse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.scenery.Scenery

class ThievingListener : InteractionListener {
    override fun defineListeners() {
        on(IntType.SCENERY, "steal-from", "steal from", "steal") { player, node ->
            submitIndividualPulse(player, StallThiefPulse(player, node as Scenery, Stall.forScenery(node)))
            lockInteractions(player, 6)
            return@on true
        }

        /*
         * Handles clothes stall interaction in Keldagrim.
         */

        on(org.rs.consts.Scenery.CLOTHES_STALL_6165, IntType.SCENERY, "steal-from") { player, _ ->
            player.dialogueInterpreter.sendDialogue(
                "You don't really see anything you'd want to steal from this stall.",
            )
            return@on true
        }
    }
}
