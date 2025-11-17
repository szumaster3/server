package content.global.skill.construction.decoration.chapel

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery

class LampPlugin : InteractionListener {

    override fun defineListeners() {
        on(burnerIds, IntType.SCENERY, "light") { player, node ->
            if (!canUseLamp(player)) return@on true

            checkRequirements(player)?.let { missingMessage ->
                sendDialogue(player, missingMessage)
                return@on true
            }

            removeItem(player, Item(Items.CLEAN_MARRENTILL_251))?.also {
                lock(player, 1)
                animate(player, Animations.USE_TINDERBOX_3687)
                sendMessage(player, "You burn some marrentill in the incense burner.")
                replaceScenery(
                    node.asScenery(),
                    node.id + 1,
                    RandomFunction.random(100, 175),
                    node.location
                )
            }
            return@on true
        }
    }

    private fun canUseLamp(player: Player) =
        !(player.ironmanManager.checkRestriction() && !player.houseManager.isInHouse(player))

    private fun checkRequirements(player: Player): String? {
        val requirements = listOf(
            Items.TINDERBOX_590 to "tinderbox",
            Items.CLEAN_MARRENTILL_251 to "clean marrentill herb"
        )

        val missing = requirements.filter { !inInventory(player, it.first) }.map { it.second }
        return if (missing.isEmpty()) null else "You'll need ${missing.joinToString(" and ")} in order to light the burner."
    }

    companion object {
        private val burnerIds = intArrayOf(
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
            Scenery.INCENSE_BURNER_13213
        )
    }
}
