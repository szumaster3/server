package content.global.skill.crafting.glass

import content.global.skill.crafting.CraftingDefinition
import core.api.*
import core.game.dialogue.InputType
import core.game.event.ResourceProducedEvent
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Items
import shared.consts.Sounds

class GlassblowingPlugin : InteractionListener, InterfaceListener {

    override fun defineListeners() {
        onUseWith(IntType.ITEM, GLASS_BLOWING_PIPE, MOLTEN_GLASS) { player, _, _ ->
            openInterface(player, GLASS_BLOWING_INTERFACE)
            return@onUseWith true
        }
    }

    override fun defineInterfaceListeners() {
        on(GLASS_BLOWING_INTERFACE) { player, _, opcode, buttonID, _, _ ->
            val product = CraftingDefinition.Glass.getById(buttonID) ?: return@on true

            if (!inInventory(player, GLASS_BLOWING_PIPE)) {
                sendMessage(player, "You need a glassblowing pipe to do this.")
                return@on true
            }

            if (!inInventory(player, MOLTEN_GLASS)) {
                sendMessage(player, "You need molten glass to do this.")
                return@on true
            }

            if (!hasLevelDyn(player, Skills.CRAFTING, product.requiredLevel)) {
                sendMessage(player, "You need a crafting level of ${product.requiredLevel} to make this.")
                return@on true
            }

            when (opcode) {
                OP_MAKE_ONE -> make(player, product, 1)
                OP_MAKE_FIVE -> make(player, product, 5)
                OP_MAKE_ALL -> make(player, product, amountInInventory(player, MOLTEN_GLASS))
                OP_MAKE_X ->
                    sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:") { value ->
                        make(player, product, Integer.parseInt(value.toString()))
                    }

                else -> return@on true
            }

            return@on true
        }
    }

    private fun make(player: Player, product: CraftingDefinition.Glass, amount: Int) {
        closeInterface(player)
        handleGlassblowing(player, product, amount)
    }

    companion object {
        private const val OP_MAKE_ONE = 155
        private const val OP_MAKE_FIVE = 196
        private const val OP_MAKE_ALL = 124
        private const val OP_MAKE_X = 199

        private const val GLASS_BLOWING_PIPE = Items.GLASSBLOWING_PIPE_1785
        private const val MOLTEN_GLASS = Items.MOLTEN_GLASS_1775
        private const val GLASS_BLOWING_INTERFACE = Components.CRAFTING_GLASS_542

        fun handleGlassblowing(player: Player, product: CraftingDefinition.Glass, amount: Int) {
            var remaining = amount

            queueScript(player, 0) { stage ->
                if (remaining <= 0) return@queueScript stopExecuting(player)
                if (!clockReady(player, Clocks.SKILLING)) return@queueScript stopExecuting(player)

                if (!inInventory(player, Items.GLASSBLOWING_PIPE_1785) ||
                    !inInventory(player, Items.MOLTEN_GLASS_1775)) {
                    return@queueScript stopExecuting(player)
                }

                when (stage) {
                    0 -> {
                        delayClock(player, Clocks.SKILLING, 3)
                        animate(player, Animations.GLASS_BLOW_884)
                        playAudio(player, Sounds.GLASSBLOWING_2724)
                        delayScript(player, 3)
                    }

                    else -> {
                        if (!removeItem(player, Items.MOLTEN_GLASS_1775)) {
                            return@queueScript stopExecuting(player)
                        }

                        addItem(player, product.productId, product.amount)
                        rewardXP(player, Skills.CRAFTING, product.experience)
                        player.dispatch(ResourceProducedEvent(product.productId, product.amount, player))

                        if (product.productId in intArrayOf(Items.UNPOWERED_ORB_567, Items.OIL_LAMP_4525))
                            sendMessage(player, "You make an ${getItemName(product.productId)}.")
                        else
                            sendMessage(player, "You make a ${getItemName(product.productId)}.")

                        remaining--

                        if (remaining > 0 && inInventory(player, Items.MOLTEN_GLASS_1775)) {
                            setCurrentScriptState(player, 0)
                            delayScript(player, 3)
                        } else {
                            stopExecuting(player)
                        }
                    }
                }
            }
        }
    }
}
