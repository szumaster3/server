package content.global.skill.crafting.items.armour.leather.hard

import content.global.skill.crafting.items.armour.leather.Thread
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items

class HardLeatherCraftingPulse(
    player: Player?,
    node: Item?,
    var amount: Int,
) : SkillPulse<Item?>(player, node) {
    private var ticks = 0

    override fun checkRequirements(): Boolean {
        if (!inInventory(player, Items.NEEDLE_1733, 1)) {
            return false
        }
        if (!inInventory(player, Items.HARD_LEATHER_1743, 1)) {
            return false
        }
        if (!inInventory(player, Items.THREAD_1734, 1)) {
            sendDialogue(player, "You need thread to make this.")
            return false
        }
        closeInterface(player)
        return true
    }

    override fun animate() {
        if (ticks % 5 == 0) {
            animate(player, ANIMATION)
        }
    }

    override fun reward(): Boolean {
        if (++ticks % 5 != 0) {
            return false
        }
        if (removeItem(player, Item(Items.HARD_LEATHER_1743, 1))) {
            val item = Item(Items.HARDLEATHER_BODY_1131)
            player.inventory.add(item)
            rewardXP(player, Skills.CRAFTING, 35.0)
            sendMessage(player, "You make a hard leather body.")
            Thread.decayThread(player)
            if (Thread.isLastThread(player)) {
                Thread.removeThread(player)
            }
        }
        amount--
        return amount < 1
    }

    companion object {
        private const val ANIMATION = Animations.CRAFT_LEATHER_1249
    }
}
