package content.global.skill.crafting.casting.silver

import core.api.*
import core.game.dialogue.InputType
import core.game.event.ResourceProducedEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import org.rs.consts.*

class SilverCraftingPlugin : InteractionListener, InterfaceListener {

    private val OP_MAKE_ONE = 155
    private val OP_MAKE_FIVE = 196
    private val OP_MAKE_ALL = 124
    private val OP_MAKE_X = 199
    private val ATTRIBUTE_FURNACE_ID = "crafting:silver:furnace_id"
    private val FURNACE_ID = intArrayOf(Scenery.FURNACE_2966, Scenery.FURNACE_3044, Scenery.FURNACE_3294, Scenery.FURNACE_4304, Scenery.FURNACE_6189, Scenery.FURNACE_11009, Scenery.FURNACE_11010, Scenery.FURNACE_11666, Scenery.FURNACE_12100, Scenery.FURNACE_12809, Scenery.FURNACE_18497, Scenery.FURNACE_18525, Scenery.FURNACE_18526, Scenery.FURNACE_21879, Scenery.FURNACE_22721, Scenery.FURNACE_26814, Scenery.FURNACE_28433, Scenery.FURNACE_28434, Scenery.FURNACE_30021, Scenery.FURNACE_30510, Scenery.FURNACE_36956, Scenery.FURNACE_37651)
    private val UNSTRUNG_ID = intArrayOf(Items.UNSTRUNG_SYMBOL_1714, Items.UNSTRUNG_EMBLEM_1720)

    override fun defineListeners() {

        onUseWith(IntType.SCENERY, Items.SILVER_BAR_2355, *FURNACE_ID) { player, _, with ->
            setAttribute(player, ATTRIBUTE_FURNACE_ID, with)
            openInterface(player, Components.CRAFTING_SILVER_CASTING_438)
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.BALL_OF_WOOL_1759, *UNSTRUNG_ID) { player, used, with ->
            Silver.forId(with.id)?.let {
                if (removeItem(player, with.id) && removeItem(player, used.id)) {
                    addItem(player, it.strung)
                }
            }
            return@onUseWith true
        }
    }

    override fun defineInterfaceListeners() {

        onOpen(Components.CRAFTING_SILVER_CASTING_438) { player, _ ->
            val itemsToSend = listOf(Items.HOLY_SYMBOL_1718 to 17, Items.UNHOLY_SYMBOL_1724 to 24, Items.SILVER_SICKLE_2961 to 31, Items.CONDUCTOR_4201 to 38, Items.TIARA_5525 to 45, Items.SILVTHRILL_ROD_7637 to 53, Items.DEMONIC_SIGIL_6748 to 60, Items.SILVER_BOLTS_UNF_9382 to 67, Items.SILVTHRIL_CHAIN_13154 to 74)
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

    private fun make(player: Player, product: Silver, amount: Int, ) {
        closeInterface(player)
        submitIndividualPulse(
            player,
            pulse =
            SilverCraftingPulse(player, product, getAttribute(player, ATTRIBUTE_FURNACE_ID, core.game.node.scenery.Scenery(-1, -1, 0)), amount),
        )
    }

}

/**
 * Handles silver crafting pulse.
 */
private class SilverCraftingPulse(val player: Player, val product: Silver, val furnace: core.game.node.scenery.Scenery, var amount: Int, ) : Pulse() {
    override fun pulse(): Boolean {
        if (amount < 1) return true

        if (!inInventory(player, product.required) || !inInventory(player, Items.SILVER_BAR_2355)) {
            return true
        }

        animate(player, Animations.HUMAN_FURNACE_SMELT_3243)
        playAudio(player, Sounds.FURNACE_2725)
        if (removeItem(player, Items.SILVER_BAR_2355, Container.INVENTORY)) {
            addItem(player, product.product, product.amount)
            rewardXP(player, Skills.CRAFTING, product.experience)

            player.dispatch(
                ResourceProducedEvent(
                    itemId = product.product,
                    amount = product.amount,
                    source = furnace,
                    original = Items.SILVER_BAR_2355,
                ),
            )
        } else {
            return true
        }

        amount--
        delay = 5

        return false
    }
}