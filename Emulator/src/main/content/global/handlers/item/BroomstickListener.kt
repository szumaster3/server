package content.global.handlers.item

import content.data.GameAttributes
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.item.Item
import core.game.world.map.Location
import org.rs.consts.Animations
import org.rs.consts.Graphics
import org.rs.consts.Items

class BroomstickListener : InteractionListener {
    override fun defineListeners() {
        on(Items.BROOMSTICK_14057, IntType.ITEM, "sweep") { player, _ ->
            stopWalk(player)
            lock(player, 1)
            visualize(player, 10532, Graphics.DUST_BROOM_EMOTE_1866)
            return@on true
        }

        on(Items.BROOMSTICK_14057, IntType.ITEM, "operate") { player, _ ->
            if (!getAttribute(player, "broom-upgrade", false)) {
                sendMessage(player, "You don't yet know how to operate this item.")
                return@on true
            }

            if (hasTimerActive(player, "teleblock")) {
                sendMessage(player, "A magical force has stopped you from teleporting.")
                return@on true
            }

            sendDialogueOptions(player, "Select an Option", "Teleport to Sorceress's Garden", "Cancel").also {
                addDialogueAction(player) { player, button ->
                    when (button) {
                        1 -> {
                            closeInterface(player)
                            lock(player, 3)
                            visualize(player, 10538, 1867)
                            queueScript(player, 3, QueueStrength.SOFT) {
                                teleport(player, Location.create(2912, 5474, 0))
                                visualize(player, Animations.BROOMSTICK_TP_T_A_10537, 1867)
                                return@queueScript stopExecuting(player)
                            }
                        }
                        2 -> {
                            closeInterface(player)
                            sendMessage(player, "You decide not to teleport.")
                        }
                    }
                    return@addDialogueAction
                }
            }
            return@on true
        }

        onUseWith(IntType.ITEM, Items.BROOM_OINTMENT_14062, Items.BROOMSTICK_14057) { player, used, _ ->
            replaceSlot(player, used.asItem().slot, Item(Items.VIAL_229, 1))
            addItem(player, Items.VIAL_229)
            sendMessages(
                player,
                "You smear the broom ointment onto Maggie's broom and feel it tingle as the",
                "enchantment permeates the wood.",
            )
            setAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_HETTY_ENCH, true)
            return@onUseWith true
        }
    }
}
