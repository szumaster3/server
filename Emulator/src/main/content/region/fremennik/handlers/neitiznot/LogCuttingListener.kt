package content.region.fremennik.handlers.neitiznot

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Scenery

class LogCuttingListener : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles split arctic pine logs & creating fremennik shield.
         */

        on(Scenery.WOODCUTTING_STUMP_21305, IntType.SCENERY, "cut-wood") { player, _ ->
            logCutting(player)
            return@on true
        }
        onUseWith(IntType.SCENERY, ARCTIC_PINE_LOG, Scenery.WOODCUTTING_STUMP_21305) { player, used, _ ->
            logCutting(player)
            return@onUseWith true
        }
    }

    private fun logCutting(player: Player) {
        if (getStatLevel(player, Skills.WOODCUTTING) < 54) {
            sendMessage(player, "You need a woodcutting level of 54 in order to do this.")
            return
        }

        if (!inInventory(player, Items.ARCTIC_PINE_LOGS_10810)) {
            sendMessage(player, "You don't have required items in your inventory.")
            return
        }

        sendSkillDialogue(player) {
            withItems(FREMENNIK_SHIELD, SPLIT_LOG)
            create { id, amount ->
                submitIndividualPulse(
                    player,
                    if (id == FREMENNIK_SHIELD) {
                        FremennikShieldPulse(player, Item(ARCTIC_PINE_LOG), amount)
                    } else {
                        LogCuttingPulse(player, Item(ARCTIC_PINE_LOG), amount)
                    },
                )
            }

            calculateMaxAmount {
                amountInInventory(player, ARCTIC_PINE_LOG)
            }
        }
    }

    companion object {
        const val ARCTIC_PINE_LOG = Items.ARCTIC_PINE_LOGS_10810
        const val SPLIT_LOG = Items.SPLIT_LOG_10812
        const val FREMENNIK_SHIELD = Items.FREMENNIK_ROUND_SHIELD_10826
    }
}
