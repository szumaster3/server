package content.global.handlers.item.withitem

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class SledInteractionListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.SWAMP_TAR_1939, Items.BUCKET_OF_WAX_30) { player, used, with ->
            val itemSlot = with.asItem().slot
            if (!inInventory(player, Items.CAKE_TIN_1887)) {
                sendMessage(player, "You need cake tin to do that.")
                return@onUseWith true
            }
            if (removeItem(player, Item(used.asItem().id, 1)) && removeItem(player, with.asItem())) {
                removeItem(player, Items.CAKE_TIN_1887)
                replaceSlot(player, itemSlot, Item(Items.WAX_4085))
                addItemOrDrop(player, Items.BUCKET_1925)
                sendMessage(player, "You made some sled wax.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.WAX_4085, Items.SLED_4083) { player, used, with ->
            val itemSlot = used.asItem().slot
            lock(player, 6)
            lockInteractions(player, 6)
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                animate(player, Animations.WAX_SLED_1470)
                playAudio(player, Sounds.WAX_SLED_1871)
                replaceSlot(player, itemSlot, Item(Items.CAKE_TIN_1887))
                replaceSlot(player, with.asItem().slot, Item(Items.SLED_4084))
                sendMessage(player, "You wax the sled. You're now ready to go.")
            }
            return@onUseWith true
        }

        on(Items.SLED_4084, IntType.ITEM, "ride") { player, _ ->
            removeItem(player, Items.SLED_4084)
            animate(player, Animations.SITTING_ON_SLED_1461)
            player.equipment.replace(Item(Items.SLED_4084), 3)
            return@on true
        }

        onEquip(Items.SLED_4084) { player, _ ->
            lock(player, 2)
            lockInteractions(player, 2)
            playAudio(player, Sounds.EQUIP_SLED_1864)
            animate(player, Animations.SITTING_ON_SLED_1461)
            return@onEquip true
        }

        onUnequip(Items.SLED_4084) { _, _ ->
            return@onUnequip true
        }
    }
}
