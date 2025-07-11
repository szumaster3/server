package content.global.skill.crafting.items.armour

import content.global.skill.crafting.items.leather.ThreadUtils
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items

class YakHideCraftingPlugin : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.CURED_YAK_HIDE_10820, Items.NEEDLE_1733) { player, used, _ ->
            sendSkillDialogue(player) {
                withItems(BODY, LEGS)
                create { index, amount ->
                    submitIndividualPulse(
                        entity = player,
                        pulse =
                            YakArmourCraftingPulse(
                                player,
                                if (index == 1) BODY else LEGS,
                                index,
                                amount,
                            ),
                    )
                }

                calculateMaxAmount {
                    amountInInventory(player, used.id)
                }
            }
            return@onUseWith true
        }
    }

    companion object {
        private val BODY = Item(Items.YAK_HIDE_ARMOUR_10822)
        private val LEGS = Item(Items.YAK_HIDE_ARMOUR_10824)
    }
}

private class YakArmourCraftingPulse(
    player: Player?,
    node: Item?,
    private val index: Int,
    private var amount: Int,
) : SkillPulse<Item?>(player, node) {
    private var ticks = 0

    override fun checkRequirements(): Boolean {
        val level = if (index == 1) 46 else 43
        if (getStatLevel(player, Skills.CRAFTING) < level) {
            sendDialogue(player, "You need a crafting level of at least $level to do this.")
            return false
        }

        if (!inInventory(player, Items.NEEDLE_1733, 1)) {
            sendDialogue(player, "You need a needle to craft the armor.")
            return false
        }

        if (!inInventory(player, Items.THREAD_1734, 1)) {
            sendDialogue(player, "You need some thread to craft the armor.")
            return false
        }

        val requiredAmount = if (index == 1) 2 else 1
        if (!inInventory(player, Items.CURED_YAK_HIDE_10820, requiredAmount)) {
            sendDialogue(player, "You don't have the required amount of yak hide to craft this.")
            return false
        }

        closeInterface(player)
        return true
    }

    override fun animate() {
        if (ticks % 5 == 0) {
            animate(player, Animations.CRAFT_LEATHER_1249)
        }
    }

    override fun reward(): Boolean {
        if (++ticks % 5 != 0) {
            return false
        }

        val requiredAmount = if (index == 1) 2 else 1
        if (removeItem(player, Item(Items.CURED_YAK_HIDE_10820, requiredAmount))) {
            player.inventory.add(node)
            rewardXP(player, Skills.CRAFTING, 32.0)
            ThreadUtils.decayThread(player)
            if (ThreadUtils.isLastThread(player)) {
                ThreadUtils.removeThread(player)
            }
            sendMessage(player, "You make ${node!!.name.lowercase()}.")
        }

        amount--
        return amount < 1
    }
}
