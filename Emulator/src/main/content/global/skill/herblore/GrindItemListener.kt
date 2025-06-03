package content.global.skill.herblore

import core.api.addItem
import core.api.amountInInventory
import core.api.removeItem
import core.api.ui.repositionChild
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.SkillPulse
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Items
import kotlin.math.ceil
import kotlin.math.roundToInt

class GrindItemListener : InteractionListener {

    override fun defineListeners() {
        val grindable = GrindItem.values().flatMap { it.items }.toSet().toIntArray()

        onUseWith(IntType.ITEM, Items.PESTLE_AND_MORTAR_233, *grindable) { player, _, with ->
            val grindItem = GrindItem.forID(with.id) ?: return@onUseWith false

            val handler = object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, grindItem.product) {

                override fun create(amount: Int, index: Int) {
                    player.pulseManager.run(object : SkillPulse<Item>(player, Item(grindItem.product)) {
                        var remaining = amount

                        init {
                            val inventoryAmount = amountInInventory(player, node.id)
                            remaining = remaining.coerceAtMost(inventoryAmount)

                            if (node.id == FISHING_BAIT) {
                                val maxAmount = ceil(inventoryAmount.toDouble() / 10).roundToInt()
                                remaining = remaining.coerceAtMost(maxAmount)
                            }
                            delay = 2
                        }

                        override fun checkRequirements(): Boolean = true

                        override fun animate() {
                            player.animator.animate(ANIMATION)
                        }

                        override fun reward(): Boolean {
                            return if (node.id == FISHING_BAIT) {
                                val quantityToRemove = 10.coerceAtMost(amountInInventory(player, FISHING_BAIT))
                                if (removeItem(player, Item(node.id, quantityToRemove))) {
                                    addItem(player, grindItem.product, quantityToRemove)
                                    remaining -= 1
                                    remaining <= 0
                                } else {
                                    true
                                }
                            } else {
                                if (removeItem(player, Item(node.id, 1))) {
                                    addItem(player, grindItem.product)
                                    remaining -= 1
                                    remaining <= 0
                                } else {
                                    true
                                }
                            }
                        }
                    })
                }

                override fun getAll(index: Int): Int {
                    return amountInInventory(player, with.id)
                }
            }
            handler.open()
            repositionChild(player, Components.SKILL_MULTI1_309, 2, 210, 15)
            return@onUseWith true
        }
    }

    companion object {
        private val ANIMATION = Animation(Animations.PESTLE_MORTAR_364)
        private const val FISHING_BAIT = Items.FISHING_BAIT_313
    }
}