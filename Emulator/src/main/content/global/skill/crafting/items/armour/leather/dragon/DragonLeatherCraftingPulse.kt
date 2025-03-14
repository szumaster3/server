package content.global.skill.crafting.items.armour.leather.dragon

import content.global.skill.crafting.items.armour.leather.Thread
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.StringUtils
import org.rs.consts.Animations
import org.rs.consts.Items

class DragonLeatherCraftingPulse(
    player: Player?,
    node: Item?,
    val leather: DragonLeather,
    var amount: Int,
) : SkillPulse<Item?>(player, node) {
    private var ticks = 0

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.CRAFTING) < leather.level) {
            sendDialogue(
                player,
                "You need a crafting level of " + leather.level + " to make " +
                    getItemName(
                        leather.product,
                    ).lowercase() +
                    ".",
            )
            amount = 0
            return false
        }
        if (!inInventory(player, Items.NEEDLE_1733, 1)) {
            sendDialogue(player, "You need a needle to make this.")
            amount = 0
            return false
        }
        if (!inInventory(player, Items.THREAD_1734, 1)) {
            sendDialogue(player, "You need thread to make this.")
            amount = 0
            return false
        }
        if (!inInventory(player, leather.leather, leather.amount)) {
            sendDialogue(
                player,
                "You need " + leather.amount + " " + getItemName(leather.leather).lowercase() + " to make this.",
            )
            amount = 0
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
        if (removeItem(player, Item(leather.leather, leather.amount))) {
            if (leather.name.contains("VAMBS")) {
                sendMessage(player, "You make a pair of " + getItemName(leather.product).lowercase() + "'s.")
            } else {
                sendMessage(
                    player,
                    "You make " + (if (StringUtils.isPlusN(getItemName(leather.product).lowercase())) "an" else "a") +
                        " " +
                        getItemName(
                            leather.product,
                        ).lowercase() + ".",
                )
            }
            val item = Item(leather.product)
            player.inventory.add(item)
            rewardXP(player, Skills.CRAFTING, leather.experience)
            Thread.decayThread(player)
            if (Thread.isLastThread(player)) {
                Thread.removeThread(player)
            }
            resetAnimation = true
            amount--
        }
        return amount < 1
    }

    override fun message(type: Int) {}

    companion object {
        private const val ANIMATION = Animations.CRAFT_LEATHER_1249
    }
}
