package content.global.random.event.brokenpick

import core.api.playAudio
import core.api.removeItem
import core.api.replaceSlot
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.Sounds

/**
 * Handles interaction for the broken pickaxe event.
 * Allows the player to reattach a pickaxe head to a handle.
 * Uses [PickaxeHead] enum for head-product mapping.
 *
 * @author Ceikry
 */
class BrokenPickaxePlugin : InteractionListener {

    override fun defineListeners() {

        onUseWith(IntType.ITEM, Items.PICKAXE_HANDLE_466, *PickaxeHead.values().map { it.head }.toIntArray()) { player, used, with ->
            val pickaxeHead = PickaxeHead.fromHeadId(used.id)
            if (pickaxeHead != null && removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                replaceSlot(player, used.asItem().slot, Item(pickaxeHead.pickaxe))
                sendMessage(player, "You carefully reattach the head to the handle.")
                playAudio(player, Sounds.EYEGLO_COIN_10)
            }
            return@onUseWith true
        }
    }
}
