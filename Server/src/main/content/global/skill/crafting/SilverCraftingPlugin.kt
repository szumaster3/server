package content.global.skill.crafting

import core.api.*
import core.game.dialogue.InputType
import core.game.event.ResourceProducedEvent
import core.game.interaction.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import shared.consts.*

/**
 * Handles crafting silver products.
 */
class SilverCraftingPlugin : InteractionListener, InterfaceListener {
    private val OP_MAKE_ONE = 155
    private val OP_MAKE_FIVE = 196
    private val OP_MAKE_ALL = 124
    private val OP_MAKE_X = 199

    override fun defineListeners() {

        /*
         * Handles use of silver bar on furnace.
         */

        onUseWith(IntType.SCENERY, Items.SILVER_BAR_2355, *CraftingObject.FURNACES) { player, _, with ->
            val hasLevel = getStatLevel(player, Skills.CRAFTING) >= 16
            if (!hasLevel) {
                sendDialogue(player, "You need a Crafting level of at least 16 to do this.")
                return@onUseWith true
            }
            setAttribute(player, "crafting:silver:furnace", with)
            openInterface(player, Components.CRAFTING_SILVER_CASTING_438)
            return@onUseWith true
        }

        /*
         * Handles stringing silver jewellery.
         */

        onUseWith(
            IntType.ITEM,
            Items.BALL_OF_WOOL_1759,
            Items.UNSTRUNG_SYMBOL_1714,
            Items.UNPOWERED_SYMBOL_1722
        ) { player, used, with ->
            if (!inInventory(player, used.id, 1))
            {
                sendMessage(player, "You don't have the required item to do that.")
                return@onUseWith true
            }

            if (!inInventory(player, with.id, 1))
            {
                sendMessage(player, "You don't have the item to attach the wool to.")
                return@onUseWith true
            }

            val product = if(with.id == Items.UNSTRUNG_SYMBOL_1714) Items.UNBLESSED_SYMBOL_1716 else Items.UNHOLY_SYMBOL_1724
            val removedUsed = removeItem(player, used.id, Container.INVENTORY)
            val removedWith = removeItem(player, with.id, Container.INVENTORY)

            if (removedUsed && removedWith) {
                addItem(player, product, 1)
                rewardXP(player, Skills.CRAFTING, 4.0)
                sendMessage(player, "You carefully attach the wool to the symbol.")
            }
            return@onUseWith true
        }
    }

    override fun defineInterfaceListeners() {
        onOpen(Components.CRAFTING_SILVER_CASTING_438) { p, c ->
            val slots = listOf(
                17 to CraftingDefinition.Silver.HOLY,
                24 to CraftingDefinition.Silver.UNHOLY,
                31 to CraftingDefinition.Silver.SICKLE,
                38 to CraftingDefinition.Silver.LIGHTNING_ROD,
                45 to CraftingDefinition.Silver.TIARA,
                53 to CraftingDefinition.Silver.SILVTHRILL_ROD,
                60 to CraftingDefinition.Silver.DEMONIC_SIGIL,
                67 to CraftingDefinition.Silver.CROSSBOW_BOLTS,
                74 to CraftingDefinition.Silver.SILVTHRIL_CHAIN
            )

            slots.forEach { (slot, silver) ->
                val hasBar = inInventory(p, Items.SILVER_BAR_2355)
                val hasMithrilBar = inInventory(p, Items.MITHRIL_BAR_2359)
                val hasMould = inInventory(p, silver.required)

                val itemId = if (hasMould) silver.product else silver.required
                sendItemOnInterface(p, c.id, slot, itemId, 1)

                val hasMaterials = hasBar && hasMould
                val hasRequiredLevel = getStatLevel(p, Skills.CRAFTING) >= silver.level

                if (!hasRequiredLevel) {
                    sendInterfaceConfig(p, c.id, slot - 1, true)
                }
                if (!hasMaterials) {
                    if(!hasMithrilBar && !hasRequirement(p, Quests.LEGACY_OF_SEERGAZE, false)) {
                        sendInterfaceConfig(p, c.id, 72, true)
                    }
                    if (slot != 74) {
                        sendInterfaceConfig(p, c.id, slot + 1, true)
                    }
                    sendInterfaceConfig(p, c.id, slot + 2, false)
                }
                /*
                 * sendString(p, "<col=ffaa44>You need a<br><col=ffaa44>chain mould<br><col=ffaa44>to make<br><col=ffaa44>this item.", c.id, 75)
                 */
            }
            return@onOpen true
        }

        on(Components.CRAFTING_SILVER_CASTING_438) { player, _, opcode, buttonID, _, _ ->
            if (!clockReady(player, Clocks.SKILLING)) return@on true
            val product = CraftingDefinition.Silver.forButton(buttonID) ?: return@on true
            val productName = getItemName(product.product).lowercase()
            if (!inInventory(player, Items.SILVER_BAR_2355)) {
                sendDialogue(player, "You need silver bar to make $productName.")
                return@on true
            }
            if (product.product == Items.CONDUCTOR_4201 && getQuestStage(player, Quests.CREATURE_OF_FENKENSTRAIN) < 4) {
                sendMessage(player, "You need partial completion of Creature of Fenkenstrain to do that.")
                return@on true
            }
            if (product.product == Items.SILVTHRIL_CHAIN_13154 && !inInventory(player, Items.MITHRIL_BAR_2359)) {
                sendMessage(player, "You need mithril bar to make chain.")
                return@on true
            }
            val amount =
                when (opcode) {
                    OP_MAKE_ONE -> 1
                    OP_MAKE_FIVE -> 5
                    OP_MAKE_ALL -> amountInInventory(player, Items.SILVER_BAR_2355)
                    OP_MAKE_X -> {
                        sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:") { value ->
                            val amount = value.toString().toIntOrNull() ?: 1
                            handleSilverCrafting(player, product, amount)
                        }
                        return@on true
                    }
                    else -> return@on true
                }
            handleSilverCrafting(player, product, amount)
            return@on true
        }
    }

    private fun handleSilverCrafting(player: Player, product: CraftingDefinition.Silver, amount: Int) {
        var remaining = amount
        closeInterface(player)
        queueScript(player, 0, QueueStrength.WEAK) {
            if (remaining <= 0) return@queueScript stopExecuting(player)
            if (getStatLevel(player, Skills.CRAFTING) < product.level) {
                sendMessage(player, "You need a crafting level of ${product.level} to make this.")
                return@queueScript stopExecuting(player)
            }

            val barsInInventory = amountInInventory(player, Items.SILVER_BAR_2355)
            if (barsInInventory <= 0) {
                sendMessage(player, "You have run out of silver bars.")
                return@queueScript stopExecuting(player)
            }
            if (product.product == Items.SILVTHRIL_CHAIN_13154 && !removeItem(player, Items.MITHRIL_BAR_2359)) {
                sendMessage(player, "You have run out of mithril bars.")
                return@queueScript stopExecuting(player)
            }
            if (removeItem(player, Items.SILVER_BAR_2355)) {
                animate(player, Animations.HUMAN_FURNACE_SMELT_3243)
                playAudio(player, Sounds.FURNACE_2725)
                rewardXP(player, Skills.CRAFTING, product.experience)

                addItem(player, product.product)

                val furnace = getAttribute(player, "crafting:silver:furnace", Scenery(-1, -1, 0))
                player.dispatch(
                    ResourceProducedEvent(
                        itemId = product.product,
                        amount = product.amount,
                        source = furnace,
                        original = Items.SILVER_BAR_2355
                    )
                )
            }

            remaining--
            if (remaining > 0 && amountInInventory(player, Items.SILVER_BAR_2355) > 0) {
                setCurrentScriptState(player, 0)
                delayScript(player, 5)
            } else {
                return@queueScript stopExecuting(player)
            }

            return@queueScript true
        }
    }
}
