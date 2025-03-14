package content.global.skill.crafting.items.armour.snakeskin

import content.global.skill.crafting.items.armour.leather.Thread
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items

class SnakeskinCraftingPulse(
    player: Player?,
    node: Item?,
    var amount: Int,
    val skin: Snakeskin,
) : SkillPulse<Item?>(player, node) {
    private var ticks = 0

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.CRAFTING) < skin.level) {
            sendDialogue(player, "You need a crafting level of ${skin.level} to make this.")
            return false
        }

        if (!inInventory(player, Items.NEEDLE_1733, 1)) {
            sendDialogue(player, "You need a needle to make this.")
            return false
        }
        if (!inInventory(player, Items.THREAD_1734, 1)) {
            sendDialogue(player, "You need thread to make this.")
            return false
        }
        if (!inInventory(player, Items.SNAKESKIN_6289, skin.amount)) {
            sendDialogue(player, "You need ${skin.amount} snakeskins to do this.")
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

        if (removeItem(player, Item(Items.SNAKESKIN_6289, skin.amount))) {
            val craftedItem = Item(skin.product, amount)
            player.inventory.add(craftedItem)
            rewardXP(player, Skills.CRAFTING, skin.experience)

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
