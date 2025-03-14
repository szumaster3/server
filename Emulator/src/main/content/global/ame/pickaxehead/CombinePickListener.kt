package content.global.ame.pickaxehead

import core.api.playAudio
import core.api.removeItem
import core.api.replaceSlot
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Sounds

class CombinePickListener : InteractionListener {
    private val pickaxeHandle = Items.PICKAXE_HANDLE_466
    private val brokenPickaxe =
        intArrayOf(
            Items.BRONZE_PICK_HEAD_480,
            Items.IRON_PICK_HEAD_482,
            Items.STEEL_PICK_HEAD_484,
            Items.MITHRIL_PICK_HEAD_486,
            Items.ADAMANT_PICK_HEAD_488,
            Items.RUNE_PICK_HEAD_490,
        )

    override fun defineListeners() {
        onUseWith(IntType.ITEM, pickaxeHandle, *brokenPickaxe) { player, used, with ->
            val product =
                when (used.id) {
                    Items.BRONZE_PICK_HEAD_480 -> Items.BRONZE_PICKAXE_1265
                    Items.IRON_PICK_HEAD_482 -> Items.IRON_PICKAXE_1267
                    Items.STEEL_PICK_HEAD_484 -> Items.STEEL_PICKAXE_1269
                    Items.MITHRIL_PICK_HEAD_486 -> Items.MITHRIL_PICKAXE_1273
                    Items.ADAMANT_PICK_HEAD_488 -> Items.ADAMANT_PICKAXE_1271
                    Items.RUNE_PICK_HEAD_490 -> Items.RUNE_PICKAXE_1275
                    else -> 0
                }

            if (product != 0 && removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                replaceSlot(player, used.asItem().slot, Item(product))
                sendMessage(player, "You carefully reattach the head to the handle.")
                playAudio(player, Sounds.EYEGLO_COIN_10)
            }
            return@onUseWith true
        }
    }
}
