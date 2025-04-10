package content.global.skill.cooking.recipes

import core.api.Container
import core.api.addItem
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items

class NettleTeaRecipe : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles putting nettles into a bowl of water.
         */

        onUseWith(IntType.ITEM, Items.NETTLES_4241, Items.BOWL_OF_WATER_1921) { player, used, with ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItem(player, Items.NETTLE_WATER_4237, 1)
                sendMessage(player, "You place the nettles into the bowl of water.")
            }
            return@onUseWith true
        }

        /*
         * Handles creating nettle tea (Milk).
         */

        onUseWith(IntType.ITEM, Items.BUCKET_OF_MILK_1927, Items.NETTLE_TEA_4239) { player, used, with ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItem(player, Items.BUCKET_1925, 1)
                addItem(player, Items.NETTLE_TEA_4240, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles adding milk to cup of tea.
         */

        onUseWith(IntType.ITEM, Items.BUCKET_OF_MILK_1927, Items.CUP_OF_TEA_4245) { player, used, with ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItem(player, Items.BUCKET_1925, 1)
                addItem(player, Items.CUP_OF_TEA_4246, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles filling an empty cup with nettle tea.
         */

        onUseWith(IntType.ITEM, Items.NETTLE_TEA_4239, Items.EMPTY_CUP_1980) { player, used, with ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItem(player, Items.BOWL_1923, 1)
                addItem(player, Items.CUP_OF_TEA_4242, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles filling an empty porcelain cup with nettle tea.
         */

        onUseWith(IntType.ITEM, Items.NETTLE_TEA_4240, Items.EMPTY_CUP_1980) { player, used, with ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItem(player, Items.BOWL_1923, 1)
                addItem(player, Items.CUP_OF_TEA_4243, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles filling a porcelain cup with nettle tea.
         */

        onUseWith(IntType.ITEM, Items.NETTLE_TEA_4239, Items.PORCELAIN_CUP_4244) { player, used, with ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItem(player, Items.BOWL_1923, 1)
                addItem(player, Items.CUP_OF_TEA_4245, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles filling a porcelain cup with nettle tea.
         */

        onUseWith(IntType.ITEM, Items.NETTLE_TEA_4240, Items.PORCELAIN_CUP_4244) { player, used, with ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItem(player, Items.BOWL_1923, 1)
                addItem(player, Items.CUP_OF_TEA_4246, 1)
            }
            return@onUseWith true
        }
    }
}