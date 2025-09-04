package content.global.skill.crafting.items.armour

import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.StringUtils
import shared.consts.Animations
import shared.consts.Items

/**
 * Handles leather crafting.
 *
 * @param player Player
 * @param node   Source item (leather or base piece), may be null
 * @param craft  Recipe from [LeatherProduct]
 * @param amount How many crafts to attempt
 */
class LeatherCraftingPulse(player: Player?, node: Item?, private val craft: LeatherProduct, private var amount: Int) : SkillPulse<Item?>(player, node) {
    private var ticks = 0

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.CRAFTING) < craft.level) {
            val name = getItemName(craft.product).lowercase()
            sendDialogue(
                player,
                "You need a Crafting level of ${craft.level} to make " + (if (StringUtils.isPlusN(name)) "an " else "a ") + name + "."
            )
            return false
        }
        if (craft.studded && !inInventory(player, Items.STEEL_STUDS_2370, 1)) {
            sendDialogue(player, "You need steel studs to make this.")
            return false
        } else if (!craft.studded) {
            if (!inInventory(player, Items.NEEDLE_1733, 1)) {
                sendDialogue(player, "You need a needle to make this.")
                return false
            }
            if (!inInventory(player, Items.THREAD_1734)) {
                sendDialogue(player, "You need thread to make this.")
                return false
            }
        }
        if (!inInventory(player, craft.input, craft.amount)) {
            sendDialogue(player, "You need ${craft.amount} ${getItemName(craft.input).lowercase()} to make this.")
            amount = 0
            return false
        }

        closeInterface(player)
        return true
    }

    override fun animate() {
        if (ticks % 5 == 0) animate(player, ANIMATION)
    }

    override fun reward(): Boolean {
        if (!clockReady(player, Clocks.SKILLING)) return false
        delayClock(player, Clocks.SKILLING, 5)

        var removed = removeItem(player, Item(craft.input, craft.amount))
        if (craft.studded) {
            sendMessage(player, "You use the studs with the ${getItemName(craft.input).lowercase()}.")
            removed = removed && removeItem(player, Item(Items.STEEL_STUDS_2370, 1))
        }

        if (removed) {
            addItem(player, craft.product, 1)
            rewardXP(player, Skills.CRAFTING, craft.xp)

            if (!craft.studded) {
                ThreadUtils.decayThread(player)
                if (ThreadUtils.isLastThread(player)) ThreadUtils.removeThread(player)
            }

            val name = getItemName(craft.product).lowercase()
            if (craft.pair) {
                sendMessage(player, "You make a pair of $name.")
            } else {
                sendMessage(player, "You make " + (if (StringUtils.isPlusN(name)) "an " else "a ") + name + ".")
            }

            craft.diary?.let { finishDiaryTask(player, it.type, it.stage, it.step) }
        }

        amount--
        return amount < 1
    }

    companion object {
        private const val ANIMATION = Animations.CRAFT_LEATHER_1249
    }
}