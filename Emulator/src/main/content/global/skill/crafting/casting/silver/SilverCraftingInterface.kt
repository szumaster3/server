package content.global.skill.crafting.casting.silver

import core.api.*
import core.game.dialogue.InputType
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import org.rs.consts.Components
import org.rs.consts.Items

class SilverCraftingInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.CRAFTING_SILVER_CASTING_438) { player, _ ->
            val itemsToSend =
                listOf(
                    Items.HOLY_SYMBOL_1718 to 17,
                    Items.UNHOLY_SYMBOL_1724 to 24,
                    Items.SILVER_SICKLE_2961 to 31,
                    Items.CONDUCTOR_4201 to 38,
                    Items.TIARA_5525 to 45,
                    Items.SILVTHRILL_ROD_7637 to 53,
                    Items.DEMONIC_SIGIL_6748 to 60,
                    Items.SILVER_BOLTS_UNF_9382 to 67,
                    Items.SILVTHRIL_CHAIN_13154 to 74,
                )

            itemsToSend.forEach { (item, index) ->
                sendItemOnInterface(player, Components.CRAFTING_SILVER_CASTING_438, index, item)
            }

            return@onOpen true
        }

        on(Components.CRAFTING_SILVER_CASTING_438) { player, _, opcode, buttonID, _, _ ->
            val product = Silver.forButton(buttonID) ?: return@on true

            if (!inInventory(player, product.required)) {
                sendMessage(player, "You need a ${getItemName(product.required).lowercase()} to make this item.")
                return@on true
            }

            if (product == Silver.SILVTHRILL_ROD || product == Silver.SILVTHRIL_CHAIN) {
                sendMessage(player, "You can't do that yet.")
                return@on true
            }

            if (!hasLevelDyn(player, Skills.CRAFTING, product.level)) {
                sendMessage(player, "You need a crafting level of ${product.level} to make this.")
                return@on true
            }

            when (opcode) {
                OP_MAKE_ONE -> make(player, product, 1)
                OP_MAKE_FIVE -> make(player, product, 5)
                OP_MAKE_ALL -> make(player, product, amountInInventory(player, Items.SILVER_BAR_2355))
                OP_MAKE_X ->
                    sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:") { value ->
                        make(player, product, Integer.parseInt(value.toString()))
                    }

                else -> return@on true
            }

            return@on true
        }
    }

    private fun make(
        player: Player,
        product: Silver,
        amount: Int,
    ) {
        closeInterface(player)
        submitIndividualPulse(
            player,
            pulse =
                SilverCraftingPulse(
                    player,
                    product,
                    getAttribute(
                        player,
                        ATTRIBUTE_FURNACE_ID,
                        Scenery(-1, -1, 0),
                    ),
                    amount,
                ),
        )
    }

    companion object {
        private const val OP_MAKE_ONE = 155
        private const val OP_MAKE_FIVE = 196
        private const val OP_MAKE_ALL = 124
        private const val OP_MAKE_X = 199
        private const val ATTRIBUTE_FURNACE_ID = "crafting:silver:furnace_id"
    }
}
