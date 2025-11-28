package content.global.skill.crafting.jewellery

import content.global.skill.crafting.CraftingObject
import core.api.*
import core.game.dialogue.InputType
import core.game.interaction.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import shared.consts.Components
import shared.consts.Items
import core.game.event.ResourceProducedEvent
import shared.consts.Animations
import shared.consts.Sounds
import core.game.node.scenery.Scenery

/**
 * Handles crafting silver products.
 */
class SilverCraftingPlugin : InteractionListener, InterfaceListener {

    private val UNSTRUNG_ID = intArrayOf(Items.UNSTRUNG_SYMBOL_1714, Items.UNSTRUNG_EMBLEM_1720)

    private val OP_MAKE_ONE = 155
    private val OP_MAKE_FIVE = 196
    private val OP_MAKE_ALL = 124
    private val OP_MAKE_X = 199

    override fun defineListeners() {

        /*
         * Handles use of silver bar on furnace.
         */

        onUseWith(IntType.SCENERY, Items.SILVER_BAR_2355, *CraftingObject.FURNACES) { player, _, with ->
            setAttribute(player, "crafting:silver:furnace", with)
            openInterface(player, Components.CRAFTING_SILVER_CASTING_438)
            return@onUseWith true
        }

        /*
         * Handles stringing silver jewellery.
         */

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

            val amount = when (opcode) {
                OP_MAKE_ONE -> 1
                OP_MAKE_FIVE -> 5
                OP_MAKE_ALL -> amountInInventory(player, Items.SILVER_BAR_2355)
                OP_MAKE_X -> {
                    sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:") { value ->
                        handleSilverCrafting(player, product, value.toString().toIntOrNull() ?: 1)
                    }
                    return@on true
                }
                else -> return@on true
            }

            handleSilverCrafting(player, product, amount)
            return@on true
        }
    }

    private fun handleSilverCrafting(player: Player, product: Silver, amount: Int) {
        closeInterface(player)

        val furnace = getAttribute(player, "crafting:silver:furnace", Scenery(-1, -1, 0))
        var remaining = amount

        queueScript(player, 0, QueueStrength.WEAK) { stage ->
            if (remaining <= 0) {
                stopExecuting(player)
                return@queueScript false
            }

            when (stage) {
                0 -> {
                    animate(player, Animations.HUMAN_FURNACE_SMELT_3243)
                    playAudio(player, Sounds.FURNACE_2725)
                    delayClock(player, Clocks.SKILLING, 3)
                    delayScript(player, 3)
                }

                else -> {
                    if (!inInventory(player, product.required) || !inInventory(player, Items.SILVER_BAR_2355)) {
                        sendMessage(player, "You have run out of silver bars.")
                        stopExecuting(player)
                        return@queueScript false
                    }

                    if (removeItem(player, Items.SILVER_BAR_2355)) {
                        addItem(player, product.product, product.amount)
                        rewardXP(player, Skills.CRAFTING, product.experience)

                        player.dispatch(
                            ResourceProducedEvent(
                                itemId = product.product,
                                amount = product.amount,
                                source = furnace,
                                original = Items.SILVER_BAR_2355,
                            )
                        )
                    }

                    remaining--

                    if (remaining > 0) {
                        setCurrentScriptState(player, 0)
                        delayScript(player, 3)
                    } else {
                        stopExecuting(player)
                        return@queueScript false
                    }
                }
            }

            return@queueScript true
        }
    }

    companion object {
        val itemsToSend = listOf(
            Items.HOLY_SYMBOL_1718 to 17,
            Items.UNHOLY_SYMBOL_1724 to 24,
            Items.SILVER_SICKLE_2961 to 31,
            Items.CONDUCTOR_4201 to 38,
            Items.TIARA_5525 to 45,
            Items.SILVTHRILL_ROD_7637 to 53,
            Items.DEMONIC_SIGIL_6748 to 60,
            Items.SILVER_BOLTS_UNF_9382 to 67,
            Items.SILVTHRIL_CHAIN_13154 to 74
        )
    }
}
