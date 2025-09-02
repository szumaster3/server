package content.region.kandarin.ardougne.plugin

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Items

/**
 * Handles interaction with beehive.
 */
class BeehivePlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handle interaction with the beehive.
         */

        on(shared.consts.Scenery.BEEHIVE_68, IntType.SCENERY, "take-from", "take-honey") { player, _ ->
            if (!inInventory(player, Items.INSECT_REPELLENT_28, 1)) {
                impact(player, 2, ImpactHandler.HitsplatType.NORMAL)
                sendMessage(player, "Suddenly bees fly out of the hive and sting you.")
                return@on true
            }

            when (getUsedOption(player)) {
                "take-from" -> {
                    val bucket = player.inventory.getItem(Item(Items.BUCKET_1925))
                    if (bucket == null) {
                        sendMessage(player, "You need a bucket to do that.")
                    } else {
                        setAttribute(player, GameAttributes.BEEHIVE_INTERACTION, true)
                        openDialogue(player, BeehiveWaxDialogue(bucket))
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
            return@on true
        }

        /*
         * Handle using insect repellent or bucket with Beehive.
         */

        onUseWith(IntType.SCENERY, intArrayOf(Items.INSECT_REPELLENT_28, Items.BUCKET_1925), shared.consts.Scenery.BEEHIVE_68) { player, used, _ ->
            val usedItem = used.asItem() ?: return@onUseWith true

            when (usedItem.id) {
                Items.INSECT_REPELLENT_28 -> {
                    if (getAttribute(player, GameAttributes.BEEHIVE_INTERACTION, false)) {
                        sendDialogue(player, "You have already cleared the hive of its bees. You can now safely collect wax from the hive.")
                    } else {
                        sendDialogueLines(player, "You pour insect repellent on the beehive. You see the bees leaving the", "hive.")
                        setAttribute(player, GameAttributes.BEEHIVE_INTERACTION, true)
                    }
                }
                Items.BUCKET_1925 -> openDialogue(player, BeehiveWaxDialogue(usedItem))
            }
            return@onUseWith true
        }
    }

    /**
     * Handles taking wax from the beehive with a bucket.
     */
    inner class BeehiveWaxDialogue(private val bucket: Item) : DialogueFile() {

        init {
            stage = 0
        }

        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> {
                    sendDialogueLines(player!!, "You try to get some wax from the beehive.")
                    stage = 1
                }
                1 -> {
                    if (getAttribute(player!!, GameAttributes.BEEHIVE_INTERACTION, false)) {
                        sendDialogueLines(player!!, "You get some wax from the beehive.")
                        replaceSlot(player!!, bucket.slot, Item(Items.BUCKET_OF_WAX_30, 1))
                        removeAttribute(player!!, GameAttributes.BEEHIVE_INTERACTION)
                        stage = 2
                    } else {
                        sendDialogue(player!!, "It would be dangerous to stick the bucket into the hive while the bees are still in it. Perhaps you can clear them out somehow.")
                        stage = 3
                    }
                }
                2 -> {
                    sendDialogueLines(player!!, "The bees fly back to the hive as the repellent wears off.")
                    stage = 3
                }

                3 -> end()
            }
        }
    }
}