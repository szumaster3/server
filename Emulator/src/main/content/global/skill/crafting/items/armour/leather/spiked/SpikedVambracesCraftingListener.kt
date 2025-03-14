package content.global.skill.crafting.items.armour.leather.spiked

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items

class SpikedVambracesCraftingListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.KEBBIT_CLAWS_10113, *itemIDs) { player, _, with ->
            if (getStatLevel(player, Skills.CRAFTING) < 32) {
                sendMessage(player, "You need a crafting level of 32 to craft this.")
                return@onUseWith false
            }

            when (with.id) {
                Items.LEATHER_VAMBRACES_1063 ->
                    createVambraces(
                        player = player,
                        base = with.asItem(),
                        product = Items.SPIKY_VAMBRACES_10077,
                        leather = "leather",
                    )

                Items.GREEN_DHIDE_VAMB_1065 ->
                    createVambraces(
                        player = player,
                        base = with.asItem(),
                        product = Items.GREEN_SPIKY_VAMBS_10079,
                        leather = "green dragonhide",
                    )

                Items.BLUE_DHIDE_VAMB_2487 ->
                    createVambraces(
                        player = player,
                        base = with.asItem(),
                        product = Items.BLUE_SPIKY_VAMBS_10081,
                        leather = "blue dragonhide",
                    )

                Items.RED_DHIDE_VAMB_2489 ->
                    createVambraces(
                        player = player,
                        base = with.asItem(),
                        product = Items.RED_SPIKY_VAMBS_10083,
                        leather = "red dragonhide",
                    )

                Items.BLACK_DHIDE_VAMB_2491 ->
                    createVambraces(
                        player = player,
                        base = with.asItem(),
                        product = Items.BLACK_SPIKY_VAMBS_10085,
                        leather = "black dragonhide",
                    )
            }
            return@onUseWith true
        }
    }

    private fun createVambraces(
        player: Player,
        base: Item,
        product: Int,
        leather: String,
    ) {
        if (removeItem(player, Items.KEBBIT_CLAWS_10113, Container.INVENTORY)) {
            replaceSlot(player, base.slot, Item(product))
            rewardXP(player, Skills.CRAFTING, 6.0)
            sendMessage(player, "You carefully attach the sharp claws to the $leather vambraces.")
        }
    }

    companion object {
        private val itemIDs =
            intArrayOf(
                Items.LEATHER_VAMBRACES_1063,
                Items.GREEN_DHIDE_VAMB_1065,
                Items.BLUE_DHIDE_VAMB_2487,
                Items.RED_DHIDE_VAMB_2489,
                Items.BLACK_DHIDE_VAMB_2491,
            )
    }
}
