package content.region.kandarin.handlers

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.ImpactHandler
import core.game.node.item.Item
import org.rs.consts.Items

class BeehiveListener : InteractionListener {
    override fun defineListeners() {
        /*
         * Handle interaction with the beehive.
         */

        on(org.rs.consts.Scenery.BEEHIVE_68, IntType.SCENERY, "take-from", "take-honey") { player, _ ->
            if (!inInventory(player, Items.INSECT_REPELLENT_28, 1)) {
                sendMessage(player, "The bees fly out of the hive and sting you!")
                impact(player, 2, ImpactHandler.HitsplatType.NORMAL, 1)
                sendMessageWithDelay(player, "Maybe you can clear them out somehow.", 2)
            } else {
                when (getUsedOption(player)) {
                    "take-from" -> {
                        if (!removeItem(player, Item(Items.BUCKET_1925, 1), Container.INVENTORY)) {
                            sendMessage(player, "You need a bucket to do that.")
                        } else {
                            addItem(player, Items.BUCKET_OF_WAX_30)
                            sendMessage(player, "You fill your bucket with wax from the hive.")
                        }
                    }

                    "take-honey" -> {
                        if (!hasSpaceFor(player, Item(Items.HONEYCOMB_12156))) {
                            sendMessage(player, "You don't have enough space in your inventory.")
                        } else {
                            addItem(player, Items.HONEYCOMB_12156, 1)
                            sendMessage(player, "You take a chunk of honeycomb from the hive.")
                        }
                    }
                }
            }
            return@on true
        }

        /*
         * Handle using insect repellent or bucket with Beehive.
         */

        onUseWith(
            IntType.SCENERY,
            intArrayOf(Items.INSECT_REPELLENT_28, Items.BUCKET_1925),
            org.rs.consts.Scenery.BEEHIVE_68,
        ) { player, used, _ ->
            val usedItem = used.asItem()

            if (usedItem != null) {
                when (usedItem.id) {
                    Items.INSECT_REPELLENT_28 -> {
                        if (getAttribute(player, "cleared-beehives", false)) {
                            sendDialogueLines(
                                player,
                                "You have already cleared the hive of its bees. You can now safely collect wax from the hive.",
                            )
                        } else {
                            sendDialogueLines(
                                player,
                                "You pour insect repellent on the beehive. You see the bees leaving the hive.",
                            )
                            setAttribute(player, "cleared-beehives", true)
                        }
                    }

                    Items.BUCKET_1925 -> {
                        if (getAttribute(player, "cleared-beehives", false)) {
                            sendDialogueLines(player, "You get some wax from the beehive.")
                            replaceSlot(player, used.asItem().slot, Item(Items.BUCKET_OF_WAX_30, 1))
                        } else {
                            sendDialogueLines(
                                player,
                                "It would be dangerous to stick the bucket into the hive while the bees are still in it. Perhaps you can clear them out somehow.",
                            )
                        }
                    }
                }
            }
            return@onUseWith true
        }
    }
}
