package content.global.plugins.item

import content.data.GameAttributes
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Graphics
import shared.consts.Items

/**
 * Handles interaction with broomstick.
 */
class BroomstickOptionPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles sweep interaction with broom.
         */

        on(Items.BROOMSTICK_14057, IntType.ITEM, "sweep") { player, _ ->
            lock(player, 3)
            visualize(player, Animations.SWEEP_BROOM_10532, Graphics.DUST_BROOM_EMOTE_1866)
            return@on true
        }

        /*
         * Handles operate option.
         */

        on(Items.BROOMSTICK_14057, IntType.ITEM, "operate") { player, _ ->
            if (!getAttribute(player, "broom-upgrade", false)) {
                sendMessage(player, "You don't yet know how to operate this item.")
                return@on true
            }

            if (hasTimerActive(player, GameAttributes.TELEBLOCK_TIMER)) {
                sendMessage(player, "A magical force has stopped you from teleporting.")
                return@on true
            }

            sendOptions(player, "Select an Option", "Teleport to Sorceress's Garden", "Cancel")
            addDialogueAction(player) { _, option ->
                when (option) {
                    2 -> {
                        closeInterface(player)
                        lock(player, 3)
                        visualize(player, Animations.BROOMSTICK_TP_T_B_10538, Graphics.BROOMSTICK_TP_1867)
                        val animDuration = animationDuration(Animation(Animations.BROOMSTICK_TP_T_B_10538))
                        queueScript(player, animDuration, QueueStrength.SOFT) {
                            teleport(player, Location.create(2912, 5474, 0))
                            visualize(player, Animations.BROOMSTICK_TP_T_A_10537, Graphics.BROOMSTICK_TP_1867)
                            return@queueScript stopExecuting(player)
                        }
                    }
                    else -> {
                        closeInterface(player)
                        sendMessage(player, "You decide not to teleport.")
                    }
                }
            }

            return@on true
        }

        /*
         * Handles hetty enchant.
         */

        onUseWith(IntType.ITEM, Items.BROOM_OINTMENT_14062, Items.BROOMSTICK_14057) { player, used, _ ->
            val enchanted = player.getAttribute(GameAttributes.QUEST_SWEPT_AWAY_BROOM_POTION, false)
            val ointmentID = player.inventory.getSlot(used.asItem())
            val vialID = Item(Items.VIAL_229, 1)

            if (!enchanted) {
                replaceSlot(player, ointmentID, vialID)
                sendMessages(player, "You smear the broom ointment onto Maggie's broom and feel it tingle as the", "enchantment permeates the wood.")
                setAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_BROOM_POTION, true)
            }
            return@onUseWith true
        }
    }
}
