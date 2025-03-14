package content.global.skill.construction.decoration.chapel

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class LampSpace : InteractionListener {
    override fun defineListeners() {
        on(burnerIds, IntType.SCENERY, "light") { player, node ->
            if (player.ironmanManager.checkRestriction() && !player.houseManager.isInHouse(player)) return@on true

            val missingItemsMessage = checkRequirements(player)
            if (missingItemsMessage != null) {
                sendDialogue(player, missingItemsMessage)
                return@on true
            }

            if (removeItem(player, Item(Items.CLEAN_MARRENTILL_251))) {
                lock(player, 1)
                animate(player, Animations.USE_TINDERBOX_3687)
                sendMessage(player, "You burn some marrentill in the incense burner.")
                replaceScenery(node.asScenery(), node.id + 1, RandomFunction.random(100, 175), node.location)
            }
            return@on true
        }
    }

    private fun checkRequirements(player: Player): String? {
        return when {
            !anyInInventory(
                player,
                Items.TINDERBOX_590,
                Items.CLEAN_MARRENTILL_251,
            ) -> "You'll need a tinderbox and a clean marrentill herb in order to light the burner."

            inInventory(player, Items.TINDERBOX_590) &&
                !inInventory(
                    player,
                    Items.CLEAN_MARRENTILL_251,
                ) -> "You'll need a clean marrentill herb in order to light the burner."

            inInventory(player, Items.CLEAN_MARRENTILL_251) &&
                !inInventory(
                    player,
                    Items.TINDERBOX_590,
                ) -> "You'll need a tinderbox in order to light the burner."

            else -> null
        }
    }

    companion object {
        private val burnerIds =
            intArrayOf(
                Scenery.TORCH_13202,
                Scenery.TORCH_13203,
                Scenery.TORCH_13204,
                Scenery.TORCH_13205,
                Scenery.TORCH_13206,
                Scenery.TORCH_13207,
                Scenery.INCENSE_BURNER_13208,
                Scenery.INCENSE_BURNER_13209,
                Scenery.INCENSE_BURNER_13210,
                Scenery.INCENSE_BURNER_13211,
                Scenery.INCENSE_BURNER_13212,
                Scenery.INCENSE_BURNER_13213,
            )
    }
}
